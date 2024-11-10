package io.netty.channel.epoll;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

/* loaded from: classes4.dex */
public final class EpollSocketChannelConfig extends EpollChannelConfig implements SocketChannelConfig {
    private volatile boolean allowHalfClosure;
    private volatile boolean tcpFastopen;

    /* JADX INFO: Access modifiers changed from: package-private */
    public EpollSocketChannelConfig(EpollSocketChannel channel) {
        super(channel);
        if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
            setTcpNoDelay(true);
        }
        calculateMaxBytesPerGatheringWrite();
    }

    @Override // io.netty.channel.epoll.EpollChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public Map<ChannelOption<?>, Object> getOptions() {
        return getOptions(super.getOptions(), ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE, EpollChannelOption.TCP_CORK, EpollChannelOption.TCP_NOTSENT_LOWAT, EpollChannelOption.TCP_KEEPCNT, EpollChannelOption.TCP_KEEPIDLE, EpollChannelOption.TCP_KEEPINTVL, EpollChannelOption.TCP_MD5SIG, EpollChannelOption.TCP_QUICKACK, EpollChannelOption.IP_TRANSPARENT, ChannelOption.TCP_FASTOPEN_CONNECT, EpollChannelOption.SO_BUSY_POLL);
    }

    @Override // io.netty.channel.epoll.EpollChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
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
        if (channelOption == EpollChannelOption.TCP_CORK) {
            return (T) Boolean.valueOf(isTcpCork());
        }
        if (channelOption == EpollChannelOption.TCP_NOTSENT_LOWAT) {
            return (T) Long.valueOf(getTcpNotSentLowAt());
        }
        if (channelOption == EpollChannelOption.TCP_KEEPIDLE) {
            return (T) Integer.valueOf(getTcpKeepIdle());
        }
        if (channelOption == EpollChannelOption.TCP_KEEPINTVL) {
            return (T) Integer.valueOf(getTcpKeepIntvl());
        }
        if (channelOption == EpollChannelOption.TCP_KEEPCNT) {
            return (T) Integer.valueOf(getTcpKeepCnt());
        }
        if (channelOption == EpollChannelOption.TCP_USER_TIMEOUT) {
            return (T) Integer.valueOf(getTcpUserTimeout());
        }
        if (channelOption == EpollChannelOption.TCP_QUICKACK) {
            return (T) Boolean.valueOf(isTcpQuickAck());
        }
        if (channelOption == EpollChannelOption.IP_TRANSPARENT) {
            return (T) Boolean.valueOf(isIpTransparent());
        }
        if (channelOption == ChannelOption.TCP_FASTOPEN_CONNECT) {
            return (T) Boolean.valueOf(isTcpFastOpenConnect());
        }
        if (channelOption == EpollChannelOption.SO_BUSY_POLL) {
            return (T) Integer.valueOf(getSoBusyPoll());
        }
        return (T) super.getOption(channelOption);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.channel.epoll.EpollChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
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
        if (option == EpollChannelOption.TCP_CORK) {
            setTcpCork(((Boolean) t).booleanValue());
            return true;
        }
        if (option == EpollChannelOption.TCP_NOTSENT_LOWAT) {
            setTcpNotSentLowAt(((Long) t).longValue());
            return true;
        }
        if (option == EpollChannelOption.TCP_KEEPIDLE) {
            setTcpKeepIdle(((Integer) t).intValue());
            return true;
        }
        if (option == EpollChannelOption.TCP_KEEPCNT) {
            setTcpKeepCnt(((Integer) t).intValue());
            return true;
        }
        if (option == EpollChannelOption.TCP_KEEPINTVL) {
            setTcpKeepIntvl(((Integer) t).intValue());
            return true;
        }
        if (option == EpollChannelOption.TCP_USER_TIMEOUT) {
            setTcpUserTimeout(((Integer) t).intValue());
            return true;
        }
        if (option == EpollChannelOption.IP_TRANSPARENT) {
            setIpTransparent(((Boolean) t).booleanValue());
            return true;
        }
        if (option == EpollChannelOption.TCP_MD5SIG) {
            Map<InetAddress, byte[]> m = (Map) t;
            setTcpMd5Sig(m);
            return true;
        }
        if (option == EpollChannelOption.TCP_QUICKACK) {
            setTcpQuickAck(((Boolean) t).booleanValue());
            return true;
        }
        if (option == ChannelOption.TCP_FASTOPEN_CONNECT) {
            setTcpFastOpenConnect(((Boolean) t).booleanValue());
            return true;
        }
        if (option == EpollChannelOption.SO_BUSY_POLL) {
            setSoBusyPoll(((Integer) t).intValue());
            return true;
        }
        return super.setOption(option, t);
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public int getReceiveBufferSize() {
        try {
            return ((EpollSocketChannel) this.channel).socket.getReceiveBufferSize();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public int getSendBufferSize() {
        try {
            return ((EpollSocketChannel) this.channel).socket.getSendBufferSize();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public int getSoLinger() {
        try {
            return ((EpollSocketChannel) this.channel).socket.getSoLinger();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public int getTrafficClass() {
        try {
            return ((EpollSocketChannel) this.channel).socket.getTrafficClass();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public boolean isKeepAlive() {
        try {
            return ((EpollSocketChannel) this.channel).socket.isKeepAlive();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public boolean isReuseAddress() {
        try {
            return ((EpollSocketChannel) this.channel).socket.isReuseAddress();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public boolean isTcpNoDelay() {
        try {
            return ((EpollSocketChannel) this.channel).socket.isTcpNoDelay();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public boolean isTcpCork() {
        try {
            return ((EpollSocketChannel) this.channel).socket.isTcpCork();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public int getSoBusyPoll() {
        try {
            return ((EpollSocketChannel) this.channel).socket.getSoBusyPoll();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public long getTcpNotSentLowAt() {
        try {
            return ((EpollSocketChannel) this.channel).socket.getTcpNotSentLowAt();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public int getTcpKeepIdle() {
        try {
            return ((EpollSocketChannel) this.channel).socket.getTcpKeepIdle();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public int getTcpKeepIntvl() {
        try {
            return ((EpollSocketChannel) this.channel).socket.getTcpKeepIntvl();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public int getTcpKeepCnt() {
        try {
            return ((EpollSocketChannel) this.channel).socket.getTcpKeepCnt();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public int getTcpUserTimeout() {
        try {
            return ((EpollSocketChannel) this.channel).socket.getTcpUserTimeout();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public EpollSocketChannelConfig setKeepAlive(boolean keepAlive) {
        try {
            ((EpollSocketChannel) this.channel).socket.setKeepAlive(keepAlive);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public EpollSocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        return this;
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public EpollSocketChannelConfig setReceiveBufferSize(int receiveBufferSize) {
        try {
            ((EpollSocketChannel) this.channel).socket.setReceiveBufferSize(receiveBufferSize);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public EpollSocketChannelConfig setReuseAddress(boolean reuseAddress) {
        try {
            ((EpollSocketChannel) this.channel).socket.setReuseAddress(reuseAddress);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public EpollSocketChannelConfig setSendBufferSize(int sendBufferSize) {
        try {
            ((EpollSocketChannel) this.channel).socket.setSendBufferSize(sendBufferSize);
            calculateMaxBytesPerGatheringWrite();
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public EpollSocketChannelConfig setSoLinger(int soLinger) {
        try {
            ((EpollSocketChannel) this.channel).socket.setSoLinger(soLinger);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public EpollSocketChannelConfig setTcpNoDelay(boolean tcpNoDelay) {
        try {
            ((EpollSocketChannel) this.channel).socket.setTcpNoDelay(tcpNoDelay);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public EpollSocketChannelConfig setTcpCork(boolean tcpCork) {
        try {
            ((EpollSocketChannel) this.channel).socket.setTcpCork(tcpCork);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public EpollSocketChannelConfig setSoBusyPoll(int loopMicros) {
        try {
            ((EpollSocketChannel) this.channel).socket.setSoBusyPoll(loopMicros);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public EpollSocketChannelConfig setTcpNotSentLowAt(long tcpNotSentLowAt) {
        try {
            ((EpollSocketChannel) this.channel).socket.setTcpNotSentLowAt(tcpNotSentLowAt);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.SocketChannelConfig
    public EpollSocketChannelConfig setTrafficClass(int trafficClass) {
        try {
            ((EpollSocketChannel) this.channel).socket.setTrafficClass(trafficClass);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public EpollSocketChannelConfig setTcpKeepIdle(int seconds) {
        try {
            ((EpollSocketChannel) this.channel).socket.setTcpKeepIdle(seconds);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public EpollSocketChannelConfig setTcpKeepIntvl(int seconds) {
        try {
            ((EpollSocketChannel) this.channel).socket.setTcpKeepIntvl(seconds);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Deprecated
    public EpollSocketChannelConfig setTcpKeepCntl(int probes) {
        return setTcpKeepCnt(probes);
    }

    public EpollSocketChannelConfig setTcpKeepCnt(int probes) {
        try {
            ((EpollSocketChannel) this.channel).socket.setTcpKeepCnt(probes);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public EpollSocketChannelConfig setTcpUserTimeout(int milliseconds) {
        try {
            ((EpollSocketChannel) this.channel).socket.setTcpUserTimeout(milliseconds);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public boolean isIpTransparent() {
        try {
            return ((EpollSocketChannel) this.channel).socket.isIpTransparent();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public EpollSocketChannelConfig setIpTransparent(boolean transparent) {
        try {
            ((EpollSocketChannel) this.channel).socket.setIpTransparent(transparent);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public EpollSocketChannelConfig setTcpMd5Sig(Map<InetAddress, byte[]> keys) {
        try {
            ((EpollSocketChannel) this.channel).setTcpMd5Sig(keys);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public EpollSocketChannelConfig setTcpQuickAck(boolean quickAck) {
        try {
            ((EpollSocketChannel) this.channel).socket.setTcpQuickAck(quickAck);
            return this;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public boolean isTcpQuickAck() {
        try {
            return ((EpollSocketChannel) this.channel).socket.isTcpQuickAck();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public EpollSocketChannelConfig setTcpFastOpenConnect(boolean fastOpenConnect) {
        this.tcpFastopen = fastOpenConnect;
        return this;
    }

    public boolean isTcpFastOpenConnect() {
        return this.tcpFastopen;
    }

    @Override // io.netty.channel.socket.DuplexChannelConfig
    public boolean isAllowHalfClosure() {
        return this.allowHalfClosure;
    }

    @Override // io.netty.channel.socket.SocketChannelConfig, io.netty.channel.socket.DuplexChannelConfig
    public EpollSocketChannelConfig setAllowHalfClosure(boolean allowHalfClosure) {
        this.allowHalfClosure = allowHalfClosure;
        return this;
    }

    @Override // io.netty.channel.epoll.EpollChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public EpollSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }

    @Override // io.netty.channel.epoll.EpollChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public EpollSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }

    @Override // io.netty.channel.epoll.EpollChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public EpollSocketChannelConfig setWriteSpinCount(int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }

    @Override // io.netty.channel.epoll.EpollChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public EpollSocketChannelConfig setAllocator(ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }

    @Override // io.netty.channel.epoll.EpollChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public EpollSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }

    @Override // io.netty.channel.epoll.EpollChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public EpollSocketChannelConfig setAutoRead(boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public EpollSocketChannelConfig setAutoClose(boolean autoClose) {
        super.setAutoClose(autoClose);
        return this;
    }

    @Override // io.netty.channel.epoll.EpollChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public EpollSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }

    @Override // io.netty.channel.epoll.EpollChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public EpollSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }

    @Override // io.netty.channel.epoll.EpollChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public EpollSocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
        super.setWriteBufferWaterMark(writeBufferWaterMark);
        return this;
    }

    @Override // io.netty.channel.epoll.EpollChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public EpollSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }

    @Override // io.netty.channel.epoll.EpollChannelConfig
    public EpollSocketChannelConfig setEpollMode(EpollMode mode) {
        super.setEpollMode(mode);
        return this;
    }

    private void calculateMaxBytesPerGatheringWrite() {
        int newSendBufferSize = getSendBufferSize() << 1;
        if (newSendBufferSize > 0) {
            setMaxBytesPerGatheringWrite(newSendBufferSize);
        }
    }
}
