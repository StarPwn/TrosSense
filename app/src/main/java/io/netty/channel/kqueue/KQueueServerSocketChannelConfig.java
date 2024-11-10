package io.netty.channel.kqueue;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.unix.UnixChannelOption;
import java.io.IOException;
import java.util.Map;

/* loaded from: classes4.dex */
public class KQueueServerSocketChannelConfig extends KQueueServerChannelConfig {
    /* JADX INFO: Access modifiers changed from: package-private */
    public KQueueServerSocketChannelConfig(KQueueServerSocketChannel channel) {
        super(channel);
        setReuseAddress(true);
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public Map<ChannelOption<?>, Object> getOptions() {
        return getOptions(super.getOptions(), UnixChannelOption.SO_REUSEPORT, KQueueChannelOption.SO_ACCEPTFILTER);
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == UnixChannelOption.SO_REUSEPORT) {
            return (T) Boolean.valueOf(isReusePort());
        }
        if (channelOption == KQueueChannelOption.SO_ACCEPTFILTER) {
            return (T) getAcceptFilter();
        }
        return (T) super.getOption(channelOption);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> boolean setOption(ChannelOption<T> option, T t) {
        validate(option, t);
        if (option == UnixChannelOption.SO_REUSEPORT) {
            setReusePort(((Boolean) t).booleanValue());
            return true;
        }
        if (option == KQueueChannelOption.SO_ACCEPTFILTER) {
            setAcceptFilter((AcceptFilter) t);
            return true;
        }
        return super.setOption(option, t);
    }

    public KQueueServerSocketChannelConfig setReusePort(boolean reusePort) {
        try {
            ((KQueueServerSocketChannel) this.channel).socket.setReusePort(reusePort);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public boolean isReusePort() {
        try {
            return ((KQueueServerSocketChannel) this.channel).socket.isReusePort();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public KQueueServerSocketChannelConfig setAcceptFilter(AcceptFilter acceptFilter) {
        try {
            ((KQueueServerSocketChannel) this.channel).socket.setAcceptFilter(acceptFilter);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public AcceptFilter getAcceptFilter() {
        try {
            return ((KQueueServerSocketChannel) this.channel).socket.getAcceptFilter();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.kqueue.KQueueChannelConfig
    public KQueueServerSocketChannelConfig setRcvAllocTransportProvidesGuess(boolean transportProvidesGuess) {
        super.setRcvAllocTransportProvidesGuess(transportProvidesGuess);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.socket.ServerSocketChannelConfig
    public KQueueServerSocketChannelConfig setReuseAddress(boolean reuseAddress) {
        super.setReuseAddress(reuseAddress);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.socket.ServerSocketChannelConfig
    public KQueueServerSocketChannelConfig setReceiveBufferSize(int receiveBufferSize) {
        super.setReceiveBufferSize(receiveBufferSize);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.socket.ServerSocketChannelConfig
    public KQueueServerSocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.socket.ServerSocketChannelConfig
    public KQueueServerSocketChannelConfig setBacklog(int backlog) {
        super.setBacklog(backlog);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig
    public KQueueServerSocketChannelConfig setTcpFastOpen(boolean enableTcpFastOpen) {
        super.setTcpFastOpen(enableTcpFastOpen);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueServerSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public KQueueServerSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueServerSocketChannelConfig setWriteSpinCount(int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueServerSocketChannelConfig setAllocator(ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueServerSocketChannelConfig setAutoRead(boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public KQueueServerSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public KQueueServerSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueServerSocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
        super.setWriteBufferWaterMark(writeBufferWaterMark);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueServerChannelConfig, io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
}
