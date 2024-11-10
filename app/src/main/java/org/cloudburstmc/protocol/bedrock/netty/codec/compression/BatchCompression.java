package org.cloudburstmc.protocol.bedrock.netty.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.cloudburstmc.protocol.bedrock.data.CompressionAlgorithm;

/* loaded from: classes5.dex */
public interface BatchCompression {
    ByteBuf decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception;

    ByteBuf encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception;

    CompressionAlgorithm getAlgorithm();

    int getLevel();

    void setLevel(int i);
}
