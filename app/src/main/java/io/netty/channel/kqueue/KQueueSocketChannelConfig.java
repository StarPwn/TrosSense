package io.netty.channel.kqueue;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.util.Map;

/* loaded from: classes4.dex */
public final class KQueueSocketChannelConfig extends KQueueChannelConfig implements SocketChannelConfig {
    private volatile boolean allowHalfClosure;
    private volatile boolean tcpFastopen;

    /* JADX INFO: Access modifiers changed from: package-private */
    public KQueueSocketChannelConfig(KQueueSocketChannel channel) {
        super(channel);
        if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
            setTcpNoDelay(true);
        }
        calculateMaxBytesPerGatheringWrite();
    }

    @Override // io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public Map<ChannelOption<?>, Object> getOptions() {
        return getOptions(super.getOptions(), ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE, KQueueChannelOption.SO_SNDLOWAT, KQueueChannelOption.TCP_NOPUSH);
    }

    @Override // io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == ChannelOption.SO_RCVBUF) {
            return (T) Integer.valueOf(getReceiveBufferSize());
        }
        if (channelOption == ChannelOption.SO_SNDBUF) {
            return (T) Integer.valueOf(getSendBufferSize());
        }
        if (channelOption == ChannelOption.TCP_NODELAY) {
            return (T) Boolean.valueOf(isTcpNoDelay());
        }
        if (channelOption == ChannelOption.SO_KEEPALIVE) {
            return (T) Boolean.valueOf(isKeepAlive());
        }
        if (channelOption == ChannelOption.SO_REUSEADDR) {
            return (T) Boolean.valueOf(isReuseAddress());
        }
        if (channelOption == ChannelOption.SO_LINGER) {
            return (T) Integer.valueOf(getSoLinger());
        }
        if (channelOption == ChannelOption.IP_TOS) {
            return (T) Integer.valueOf(getTrafficClass());
        }
        if (channelOption == ChannelOption.ALLOW_HALF_CLOSURE) {
            return (T) Boolean.valueOf(isAllowHalfClosure());
        }
        if (channelOption == KQueueChannelOption.SO_SNDLOWAT) {
            return (T) Integer.valueOf(getSndLowAt());
        }
        if (channelOption == KQueueChannelOption.TCP_NOPUSH) {
            return (T) Boolean.valueOf(isTcpNoPush());
        }
        if (channelOption == ChannelOption.TCP_FASTOPEN_CONNECT) {
            return (T) Boolean.valueOf(isTcpFastOpenConnect());
        }
        return (T) super.getOption(channelOption);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> boolean setOption(ChannelOption<T> option, T t) {
        validate(option, t);
        if (option == ChannelOption.SO_RCVBUF) {
            setReceiveBufferSize(((Integer) t).intValue());
            return true;
        }
        if (option == ChannelOption.SO_SNDBUF) {
            setSendBufferSize(((Integer) t).intValue());
            return true;
        }
        if (option == ChannelOption.TCP_NODELAY) {
            setTcpNoDelay(((Boolean) t).booleanValue());
            return true;
        }
        if (option == ChannelOption.SO_KEEPALIVE) {
            setKeepAlive(((Boolean) t).booleanValue());
            return true;
        }
        if (option == ChannelOption.SO_REUSEADDR) {
            setReuseAddress(((Boolean) t).booleanValue());
            return true;
        }
        if (option == ChannelOption.SO_LINGER) {
            setSoLinger(((Integer) t).intValue());
            return true;
        }
        if (option == ChannelOption.IP_TOS) {
            setTrafficClass(((Integer) t).intValue());
            return true;
        }
        if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
            setAllowHalfClosure(((Boolean) t).booleanValue());
            return true;
        }
        if (option == KQueueChannelOption.SO_SNDLOWAT) {
            setSndLowAt(((Integer) t).intValue());
            return true;
        }
        if (option == KQueueChannelOption.TCP_NOPUSH) {
            setTcpNoPush(((Boolean) t).booleanValue());
            return true;
        }
        if (option == ChannelOption.TCP_FASTOPEN_CONNECT) {
            setTcpFastOpenConnect(((Boolean) t).booleanValue());
            return true;
        }
        return super.setOption(option, t);
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public int getReceiveBufferSize() {
        try {
            return ((KQueueSocketChannel) this.channel).socket.getReceiveBufferSize();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public int getSendBufferSize() {
        try {
            return ((KQueueSocketChannel) this.channel).socket.getSendBufferSize();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public int getSoLinger() {
        try {
            return ((KQueueSocketChannel) this.channel).socket.getSoLinger();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public int getTrafficClass() {
        try {
            return ((KQueueSocketChannel) this.channel).socket.getTrafficClass();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public boolean isKeepAlive() {
        try {
            return ((KQueueSocketChannel) this.channel).socket.isKeepAlive();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public boolean isReuseAddress() {
        try {
            return ((KQueueSocketChannel) this.channel).socket.isReuseAddress();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public boolean isTcpNoDelay() {
        try {
            return ((KQueueSocketChannel) this.channel).socket.isTcpNoDelay();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public int getSndLowAt() {
        try {
            return ((KQueueSocketChannel) this.channel).socket.getSndLowAt();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public void setSndLowAt(int sndLowAt) {
        try {
            ((KQueueSocketChannel) this.channel).socket.setSndLowAt(sndLowAt);
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public boolean isTcpNoPush() {
        try {
            return ((KQueueSocketChannel) this.channel).socket.isTcpNoPush();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public void setTcpNoPush(boolean tcpNoPush) {
        try {
            ((KQueueSocketChannel) this.channel).socket.setTcpNoPush(tcpNoPush);
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public KQueueSocketChannelConfig setKeepAlive(boolean keepAlive) {
        try {
            ((KQueueSocketChannel) this.channel).socket.setKeepAlive(keepAlive);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public KQueueSocketChannelConfig setReceiveBufferSize(int receiveBufferSize) {
        try {
            ((KQueueSocketChannel) this.channel).socket.setReceiveBufferSize(receiveBufferSize);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public KQueueSocketChannelConfig setReuseAddress(boolean reuseAddress) {
        try {
            ((KQueueSocketChannel) this.channel).socket.setReuseAddress(reuseAddress);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public KQueueSocketChannelConfig setSendBufferSize(int sendBufferSize) {
        try {
            ((KQueueSocketChannel) this.channel).socket.setSendBufferSize(sendBufferSize);
            calculateMaxBytesPerGatheringWrite();
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public KQueueSocketChannelConfig setSoLinger(int soLinger) {
        try {
            ((KQueueSocketChannel) this.channel).socket.setSoLinger(soLinger);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public KQueueSocketChannelConfig setTcpNoDelay(boolean tcpNoDelay) {
        try {
            ((KQueueSocketChannel) this.channel).socket.setTcpNoDelay(tcpNoDelay);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public KQueueSocketChannelConfig setTrafficClass(int trafficClass) {
        try {
            ((KQueueSocketChannel) this.channel).socket.setTrafficClass(trafficClass);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.DuplexChannelConfig
    public boolean isAllowHalfClosure() {
        return this.allowHalfClosure;
    }

    public KQueueSocketChannelConfig setTcpFastOpenConnect(boolean fastOpenConnect) {
        this.tcpFastopen = fastOpenConnect;
        return this;
    }

    public boolean isTcpFastOpenConnect() {
        return this.tcpFastopen;
    }

    @Override // io.netty.channel.kqueue.KQueueChannelConfig
    public KQueueSocketChannelConfig setRcvAllocTransportProvidesGuess(boolean transportProvidesGuess) {
        super.setRcvAllocTransportProvidesGuess(transportProvidesGuess);
        return this;
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public KQueueSocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        return this;
    }

    @Override // io.netty.channel.socket.SocketChannelConfig, io.netty.channel.socket.DuplexChannelConfig
    public KQueueSocketChannelConfig setAllowHalfClosure(boolean allowHalfClosure) {
        this.allowHalfClosure = allowHalfClosure;
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public KQueueSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueSocketChannelConfig setWriteSpinCount(int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueSocketChannelConfig setAllocator(ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueSocketChannelConfig setAutoRead(boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueSocketChannelConfig setAutoClose(boolean autoClose) {
        super.setAutoClose(autoClose);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public KQueueSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public KQueueSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueSocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
        super.setWriteBufferWaterMark(writeBufferWaterMark);
        return this;
    }

    @Override // io.netty.channel.kqueue.KQueueChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }

    private void calculateMaxBytesPerGatheringWrite() {
        int newSendBufferSize = getSendBufferSize() << 1;
        if (newSendBufferSize > 0) {
            setMaxBytesPerGatheringWrite(newSendBufferSize);
        }
    }
}
