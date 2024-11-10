package org.cloudburstmc.protocol.bedrock.netty.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.cloudburstmc.protocol.bedrock.data.CompressionAlgorithm;
import org.cloudburstmc.protocol.bedrock.data.PacketCompressionAlgorithm;
import org.cloudburstmc.protocol.common.util.Zlib;

/* loaded from: classes5.dex */
public class ZlibCompression implements BatchCompression {
    private static final int MAX_DECOMPRESSED_BYTES = Integer.getInteger("bedrock.maxDecompressedBytes", 10485760).intValue();
    private int level = 7;
    private final Zlib zlib;

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.BatchCompression
    public int getLevel() {
        return this.level;
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.BatchCompression
    public void setLevel(int level) {
        this.level = level;
    }

    public ZlibCompression(Zlib zlib) {
        this.zlib = zlib;
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.BatchCompression
    public ByteBuf encode(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        ByteBuf outBuf = ctx.alloc().ioBuffer(msg.readableBytes() << 3);
        try {
            this.zlib.deflate(msg, outBuf, this.level);
            return outBuf.retain();
        } finally {
            outBuf.release();
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.BatchCompression
    public ByteBuf decode(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        return this.zlib.inflate(msg, MAX_DECOMPRESSED_BYTES);
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.BatchCompression
    public CompressionAlgorithm getAlgorithm() {
        return PacketCompressionAlgorithm.ZLIB;
    }
}
