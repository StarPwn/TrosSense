package io.netty.channel.socket;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;

/* loaded from: classes4.dex */
public interface DuplexChannelConfig extends ChannelConfig {
    boolean isAllowHalfClosure();

    @Override // io.netty.channel.ChannelConfig
    DuplexChannelConfig setAllocator(ByteBufAllocator byteBufAllocator);

    DuplexChannelConfig setAllowHalfClosure(boolean z);

    @Override // io.netty.channel.ChannelConfig
    DuplexChannelConfig setAutoClose(boolean z);

    @Override // io.netty.channel.ChannelConfig
    DuplexChannelConfig setAutoRead(boolean z);

    @Override // io.netty.channel.ChannelConfig
    @Deprecated
    DuplexChannelConfig setMaxMessagesPerRead(int i);

    @Override // io.netty.channel.ChannelConfig
    DuplexChannelConfig setMessageSizeEstimator(MessageSizeEstimator messageSizeEstimator);

    @Override // io.netty.channel.ChannelConfig
    DuplexChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator recvByteBufAllocator);

    @Override // io.netty.channel.ChannelConfig
    DuplexChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark);

    @Override // io.netty.channel.ChannelConfig
    DuplexChannelConfig setWriteSpinCount(int i);
}
