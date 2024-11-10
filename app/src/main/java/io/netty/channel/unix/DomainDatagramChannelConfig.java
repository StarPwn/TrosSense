package io.netty.channel.unix;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;

/* loaded from: classes4.dex */
public interface DomainDatagramChannelConfig extends ChannelConfig {
    int getSendBufferSize();

    @Override // io.netty.channel.ChannelConfig
    DomainDatagramChannelConfig setAllocator(ByteBufAllocator byteBufAllocator);

    @Override // io.netty.channel.ChannelConfig
    DomainDatagramChannelConfig setAutoClose(boolean z);

    @Override // io.netty.channel.ChannelConfig
    DomainDatagramChannelConfig setAutoRead(boolean z);

    @Override // io.netty.channel.ChannelConfig
    DomainDatagramChannelConfig setConnectTimeoutMillis(int i);

    @Override // io.netty.channel.ChannelConfig
    @Deprecated
    DomainDatagramChannelConfig setMaxMessagesPerRead(int i);

    @Override // io.netty.channel.ChannelConfig
    DomainDatagramChannelConfig setMessageSizeEstimator(MessageSizeEstimator messageSizeEstimator);

    @Override // io.netty.channel.ChannelConfig
    DomainDatagramChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator recvByteBufAllocator);

    DomainDatagramChannelConfig setSendBufferSize(int i);

    @Override // io.netty.channel.ChannelConfig
    DomainDatagramChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark);

    @Override // io.netty.channel.ChannelConfig
    DomainDatagramChannelConfig setWriteSpinCount(int i);
}
