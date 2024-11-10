package io.netty.channel.udt;

import com.barchart.udt.nio.ChannelUDT;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;
import java.io.IOException;
import java.util.Map;

@Deprecated
/* loaded from: classes4.dex */
public class DefaultUdtServerChannelConfig extends DefaultUdtChannelConfig implements UdtServerChannelConfig {
    private volatile int backlog;

    public DefaultUdtServerChannelConfig(UdtChannel channel, ChannelUDT channelUDT, boolean apply) throws IOException {
        super(channel, channelUDT, apply);
        this.backlog = 64;
        if (apply) {
            apply(channelUDT);
        }
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig
    protected void apply(ChannelUDT channelUDT) throws IOException {
    }

    @Override // io.netty.channel.udt.UdtServerChannelConfig
    public int getBacklog() {
        return this.backlog;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == ChannelOption.SO_BACKLOG) {
            return (T) Integer.valueOf(getBacklog());
        }
        return (T) super.getOption(channelOption);
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public Map<ChannelOption<?>, Object> getOptions() {
        return getOptions(super.getOptions(), ChannelOption.SO_BACKLOG);
    }

    @Override // io.netty.channel.udt.UdtServerChannelConfig
    public UdtServerChannelConfig setBacklog(int backlog) {
        this.backlog = backlog;
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> boolean setOption(ChannelOption<T> option, T t) {
        validate(option, t);
        if (option == ChannelOption.SO_BACKLOG) {
            setBacklog(((Integer) t).intValue());
            return true;
        }
        return super.setOption(option, t);
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.udt.UdtChannelConfig
    public UdtServerChannelConfig setProtocolReceiveBufferSize(int protocolReceiveBufferSize) {
        super.setProtocolReceiveBufferSize(protocolReceiveBufferSize);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.udt.UdtChannelConfig
    public UdtServerChannelConfig setProtocolSendBufferSize(int protocolSendBufferSize) {
        super.setProtocolSendBufferSize(protocolSendBufferSize);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.udt.UdtChannelConfig
    public UdtServerChannelConfig setReceiveBufferSize(int receiveBufferSize) {
        super.setReceiveBufferSize(receiveBufferSize);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.udt.UdtChannelConfig
    public UdtServerChannelConfig setReuseAddress(boolean reuseAddress) {
        super.setReuseAddress(reuseAddress);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.udt.UdtChannelConfig
    public UdtServerChannelConfig setSendBufferSize(int sendBufferSize) {
        super.setSendBufferSize(sendBufferSize);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.udt.UdtChannelConfig
    public UdtServerChannelConfig setSoLinger(int soLinger) {
        super.setSoLinger(soLinger);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.udt.UdtChannelConfig
    public UdtServerChannelConfig setSystemReceiveBufferSize(int systemReceiveBufferSize) {
        super.setSystemReceiveBufferSize(systemReceiveBufferSize);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.udt.UdtChannelConfig
    public UdtServerChannelConfig setSystemSendBufferSize(int systemSendBufferSize) {
        super.setSystemSendBufferSize(systemSendBufferSize);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtServerChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public UdtServerChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtServerChannelConfig setWriteSpinCount(int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtServerChannelConfig setAllocator(ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtServerChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtServerChannelConfig setAutoRead(boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtServerChannelConfig setAutoClose(boolean autoClose) {
        super.setAutoClose(autoClose);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtServerChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtServerChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtServerChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
        super.setWriteBufferWaterMark(writeBufferWaterMark);
        return this;
    }

    @Override // io.netty.channel.udt.DefaultUdtChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtServerChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
}
