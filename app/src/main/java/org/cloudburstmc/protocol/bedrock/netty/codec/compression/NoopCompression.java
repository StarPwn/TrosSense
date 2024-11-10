package org.cloudburstmc.protocol.bedrock.netty.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.cloudburstmc.protocol.bedrock.data.CompressionAlgorithm;
import org.cloudburstmc.protocol.bedrock.data.PacketCompressionAlgorithm;

/* loaded from: classes5.dex */
public class NoopCompression implements BatchCompression {
    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.BatchCompression
    public ByteBuf encode(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        return msg.retainedSlice();
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.BatchCompression
    public ByteBuf decode(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        return msg.retainedSlice();
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.BatchCompression
    public CompressionAlgorithm getAlgorithm() {
        return PacketCompressionAlgorithm.NONE;
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.BatchCompression
    public void setLevel(int level) {
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.BatchCompression
    public int getLevel() {
        return -1;
    }
}
