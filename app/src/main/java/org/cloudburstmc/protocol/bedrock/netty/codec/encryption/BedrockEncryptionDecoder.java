package org.cloudburstmc.protocol.bedrock.netty.codec.encryption;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import org.cloudburstmc.protocol.bedrock.netty.BedrockBatchWrapper;

/* loaded from: classes5.dex */
public class BedrockEncryptionDecoder extends MessageToMessageDecoder<BedrockBatchWrapper> {
    public static final String NAME = "bedrock-encryption-decoder";
    private static final boolean VALIDATE = Boolean.getBoolean("cloudburst.validateEncryption");
    private final Cipher cipher;
    private final SecretKey key;
    private final AtomicLong packetCounter = new AtomicLong();

    public BedrockEncryptionDecoder(SecretKey key, Cipher cipher) {
        this.key = key;
        this.cipher = cipher;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, BedrockBatchWrapper bedrockBatchWrapper, List list) throws Exception {
        decode2(channelHandlerContext, bedrockBatchWrapper, (List<Object>) list);
    }

    /* renamed from: decode, reason: avoid collision after fix types in other method */
    protected void decode2(ChannelHandlerContext ctx, BedrockBatchWrapper msg, List<Object> out) throws Exception {
        ByteBuffer inBuffer = msg.getCompressed().nioBuffer();
        ByteBuffer outBuffer = inBuffer.duplicate();
        this.cipher.update(inBuffer, outBuffer);
        ByteBuf output = msg.getCompressed().readSlice(msg.getCompressed().readableBytes() - 8);
        if (VALIDATE) {
            ByteBuf trailer = msg.getCompressed().readSlice(8);
            byte[] actual = new byte[8];
            trailer.readBytes(actual);
            byte[] expected = BedrockEncryptionEncoder.generateTrailer(output, this.key, this.packetCounter);
            if (!Arrays.equals(expected, actual)) {
                throw new CorruptedFrameException("Invalid encryption trailer");
            }
        }
        ByteBuf trailer2 = output.retain();
        msg.setCompressed(trailer2);
        out.add(msg.retain());
    }
}
