package org.cloudburstmc.protocol.bedrock.netty.codec.encryption;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.concurrent.FastThreadLocal;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import org.cloudburstmc.protocol.bedrock.netty.BedrockBatchWrapper;
import org.jose4j.lang.HashUtil;

/* loaded from: classes5.dex */
public class BedrockEncryptionEncoder extends MessageToMessageEncoder<BedrockBatchWrapper> {
    private static final FastThreadLocal<MessageDigest> DIGEST = new FastThreadLocal<MessageDigest>() { // from class: org.cloudburstmc.protocol.bedrock.netty.codec.encryption.BedrockEncryptionEncoder.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.util.concurrent.FastThreadLocal
        public MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance(HashUtil.SHA_256);
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }
    };
    public static final String NAME = "bedrock-encryption-encoder";
    private final Cipher cipher;
    private final SecretKey key;
    private final AtomicLong packetCounter = new AtomicLong();

    public BedrockEncryptionEncoder(SecretKey key, Cipher cipher) {
        this.key = key;
        this.cipher = cipher;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageEncoder
    public /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, BedrockBatchWrapper bedrockBatchWrapper, List list) throws Exception {
        encode2(channelHandlerContext, bedrockBatchWrapper, (List<Object>) list);
    }

    /* renamed from: encode, reason: avoid collision after fix types in other method */
    protected void encode2(ChannelHandlerContext ctx, BedrockBatchWrapper in, List<Object> out) throws Exception {
        ByteBuf buf = ctx.alloc().ioBuffer(in.getCompressed().readableBytes() + 8);
        try {
            ByteBuffer trailer = ByteBuffer.wrap(generateTrailer(in.getCompressed(), this.key, this.packetCounter));
            ByteBuffer inBuffer = in.getCompressed().nioBuffer();
            ByteBuffer outBuffer = buf.nioBuffer(0, in.getCompressed().readableBytes() + 8);
            int index = this.cipher.update(inBuffer, outBuffer);
            buf.writerIndex(index + this.cipher.update(trailer, outBuffer));
            in.setCompressed(buf.retain());
            out.add(in.retain());
        } finally {
            buf.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] generateTrailer(ByteBuf buf, SecretKey key, AtomicLong counter) {
        MessageDigest digest = DIGEST.get();
        ByteBuf counterBuf = ByteBufAllocator.DEFAULT.directBuffer(8);
        try {
            counterBuf.writeLongLE(counter.getAndIncrement());
            ByteBuffer keyBuffer = ByteBuffer.wrap(key.getEncoded());
            digest.update(counterBuf.nioBuffer(0, 8));
            digest.update(buf.nioBuffer(buf.readerIndex(), buf.readableBytes()));
            digest.update(keyBuffer);
            byte[] hash = digest.digest();
            return Arrays.copyOf(hash, 8);
        } finally {
            counterBuf.release();
            digest.reset();
        }
    }
}
