package org.cloudburstmc.protocol.bedrock.netty.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.CompressionAlgorithm;
import org.cloudburstmc.protocol.bedrock.data.PacketCompressionAlgorithm;
import org.cloudburstmc.protocol.bedrock.netty.BedrockBatchWrapper;

/* loaded from: classes5.dex */
public class CompressionCodec extends MessageToMessageCodec<BedrockBatchWrapper, BedrockBatchWrapper> {
    public static final String NAME = "compression-codec";
    private final boolean prefixed;
    private final CompressionStrategy strategy;

    @Override // io.netty.handler.codec.MessageToMessageCodec
    protected /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, BedrockBatchWrapper bedrockBatchWrapper, List list) throws Exception {
        decode2(channelHandlerContext, bedrockBatchWrapper, (List<Object>) list);
    }

    @Override // io.netty.handler.codec.MessageToMessageCodec
    protected /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, BedrockBatchWrapper bedrockBatchWrapper, List list) throws Exception {
        encode2(channelHandlerContext, bedrockBatchWrapper, (List<Object>) list);
    }

    public CompressionCodec(CompressionStrategy strategy, boolean prefixed) {
        this.strategy = strategy;
        this.prefixed = prefixed;
    }

    /* renamed from: encode, reason: avoid collision after fix types in other method */
    protected void encode2(ChannelHandlerContext ctx, BedrockBatchWrapper msg, List<Object> out) throws Exception {
        ByteBuf outBuf;
        if (msg.getCompressed() == null && msg.getUncompressed() == null) {
            throw new IllegalStateException("Batch was not encoded before");
        }
        if (msg.getCompressed() != null && !msg.isModified()) {
            onPassedThrough(ctx, msg);
            out.add(msg.retain());
            return;
        }
        BatchCompression compression = this.strategy.getCompression(msg);
        if (!this.prefixed && this.strategy.getDefaultCompression().getAlgorithm() != compression.getAlgorithm()) {
            throw new IllegalStateException("Non-default compression algorithm used without prefixing");
        }
        ByteBuf compressed = compression.encode(ctx, msg.getUncompressed());
        try {
            if (this.prefixed) {
                outBuf = ctx.alloc().ioBuffer(compressed.readableBytes() + 1);
                outBuf.writeByte(getCompressionHeader(compression.getAlgorithm()));
                outBuf.writeBytes(compressed);
            } else {
                outBuf = compressed.retain();
            }
            msg.setCompressed(outBuf, compression.getAlgorithm());
            compressed.release();
            onCompressed(ctx, msg);
            out.add(msg.retain());
        } catch (Throwable th) {
            compressed.release();
            throw th;
        }
    }

    /* renamed from: decode, reason: avoid collision after fix types in other method */
    protected void decode2(ChannelHandlerContext ctx, BedrockBatchWrapper msg, List<Object> out) throws Exception {
        BatchCompression compression;
        ByteBuf compressed = msg.getCompressed().slice();
        if (this.prefixed) {
            CompressionAlgorithm algorithm = getCompressionAlgorithm(compressed.readByte());
            compression = this.strategy.getCompression(algorithm);
        } else {
            compression = this.strategy.getDefaultCompression();
        }
        msg.setAlgorithm(compression.getAlgorithm());
        msg.setUncompressed(compression.decode(ctx, compressed.slice()));
        onDecompressed(ctx, msg);
        out.add(msg.retain());
    }

    protected void onPassedThrough(ChannelHandlerContext ctx, BedrockBatchWrapper msg) {
    }

    protected void onCompressed(ChannelHandlerContext ctx, BedrockBatchWrapper msg) {
    }

    protected void onDecompressed(ChannelHandlerContext ctx, BedrockBatchWrapper msg) {
    }

    protected final byte getCompressionHeader(CompressionAlgorithm algorithm) {
        if (algorithm.equals(PacketCompressionAlgorithm.NONE)) {
            return (byte) -1;
        }
        if (algorithm.equals(PacketCompressionAlgorithm.ZLIB)) {
            return (byte) 0;
        }
        if (algorithm.equals(PacketCompressionAlgorithm.SNAPPY)) {
            return (byte) 1;
        }
        byte header = getCompressionHeader0(algorithm);
        if (header == -1) {
            throw new IllegalArgumentException("Unknown compression algorithm " + algorithm);
        }
        return header;
    }

    protected final CompressionAlgorithm getCompressionAlgorithm(byte header) {
        switch (header) {
            case -1:
                return PacketCompressionAlgorithm.NONE;
            case 0:
                return PacketCompressionAlgorithm.ZLIB;
            case 1:
                return PacketCompressionAlgorithm.SNAPPY;
            default:
                CompressionAlgorithm algorithm = getCompressionAlgorithm0(header);
                if (algorithm == null) {
                    throw new IllegalArgumentException("Unknown compression algorithm " + ((int) header));
                }
                return algorithm;
        }
    }

    protected byte getCompressionHeader0(CompressionAlgorithm algorithm) {
        return (byte) -1;
    }

    protected CompressionAlgorithm getCompressionAlgorithm0(byte header) {
        return null;
    }

    public CompressionStrategy getStrategy() {
        return this.strategy;
    }
}
