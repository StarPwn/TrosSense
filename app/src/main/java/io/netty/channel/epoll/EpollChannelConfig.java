package io.netty.channel.epoll;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.unix.IntegerUnixChannelOption;
import io.netty.channel.unix.Limits;
import io.netty.channel.unix.RawUnixChannelOption;
import io.netty.handler.codec.rtsp.RtspHeaders;
import io.netty.util.internal.ObjectUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

/* loaded from: classes4.dex */
public class EpollChannelConfig extends DefaultChannelConfig {
    private volatile long maxBytesPerGatheringWrite;

    /* JADX INFO: Access modifiers changed from: protected */
    public EpollChannelConfig(Channel channel) {
        super(checkAbstractEpollChannel(channel));
        this.maxBytesPerGatheringWrite = Limits.SSIZE_MAX;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public EpollChannelConfig(Channel channel, RecvByteBufAllocator recvByteBufAllocator) {
        super(checkAbstractEpollChannel(channel), recvByteBufAllocator);
        this.maxBytesPerGatheringWrite = Limits.SSIZE_MAX;
    }

    protected LinuxSocket socket() {
        return ((AbstractEpollChannel) this.channel).socket;
    }

    private static Channel checkAbstractEpollChannel(Channel channel) {
        if (!(channel instanceof AbstractEpollChannel)) {
            throw new IllegalArgumentException("channel is not AbstractEpollChannel: " + channel.getClass());
        }
        return channel;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public Map<ChannelOption<?>, Object> getOptions() {
        return getOptions(super.getOptions(), EpollChannelOption.EPOLL_MODE);
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == EpollChannelOption.EPOLL_MODE) {
            return (T) getEpollMode();
        }
        try {
            if (channelOption instanceof IntegerUnixChannelOption) {
                IntegerUnixChannelOption integerUnixChannelOption = (IntegerUnixChannelOption) channelOption;
                return (T) Integer.valueOf(((AbstractEpollChannel) this.channel).socket.getIntOpt(integerUnixChannelOption.level(), integerUnixChannelOption.optname()));
            }
            if (channelOption instanceof RawUnixChannelOption) {
                RawUnixChannelOption rawUnixChannelOption = (RawUnixChannelOption) channelOption;
                ByteBuffer allocate = ByteBuffer.allocate(rawUnixChannelOption.length());
                ((AbstractEpollChannel) this.channel).socket.getRawOpt(rawUnixChannelOption.level(), rawUnixChannelOption.optname(), allocate);
                return (T) allocate.flip();
            }
            return (T) super.getOption(channelOption);
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> boolean setOption(ChannelOption<T> option, T t) {
        validate(option, t);
        if (option == EpollChannelOption.EPOLL_MODE) {
            setEpollMode((EpollMode) t);
            return true;
        }
        try {
            if (option instanceof IntegerUnixChannelOption) {
                IntegerUnixChannelOption opt = (IntegerUnixChannelOption) option;
                ((AbstractEpollChannel) this.channel).socket.setIntOpt(opt.level(), opt.optname(), ((Integer) t).intValue());
                return true;
            }
            if (option instanceof RawUnixChannelOption) {
                RawUnixChannelOption opt2 = (RawUnixChannelOption) option;
                ((AbstractEpollChannel) this.channel).socket.setRawOpt(opt2.level(), opt2.optname(), (ByteBuffer) t);
                return true;
            }
            return super.setOption(option, t);
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public EpollChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public EpollChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public EpollChannelConfig setWriteSpinCount(int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public EpollChannelConfig setAllocator(ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public EpollChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
        if (!(allocator.newHandle() instanceof RecvByteBufAllocator.ExtendedHandle)) {
            throw new IllegalArgumentException("allocator.newHandle() must return an object of type: " + RecvByteBufAllocator.ExtendedHandle.class);
        }
        super.setRecvByteBufAllocator(allocator);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public EpollChannelConfig setAutoRead(boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public EpollChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public EpollChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public EpollChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
        super.setWriteBufferWaterMark(writeBufferWaterMark);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public EpollChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }

    public EpollMode getEpollMode() {
        return ((AbstractEpollChannel) this.channel).isFlagSet(Native.EPOLLET) ? EpollMode.EDGE_TRIGGERED : EpollMode.LEVEL_TRIGGERED;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x000d. Please report as an issue. */
    public EpollChannelConfig setEpollMode(EpollMode mode) {
        ObjectUtil.checkNotNull(mode, RtspHeaders.Values.MODE);
        try {
            switch (mode) {
                case EDGE_TRIGGERED:
                    checkChannelNotRegistered();
                    ((AbstractEpollChannel) this.channel).setFlag(Native.EPOLLET);
                    return this;
                case LEVEL_TRIGGERED:
                    checkChannelNotRegistered();
                    ((AbstractEpollChannel) this.channel).clearFlag(Native.EPOLLET);
                    return this;
                default:
                    throw new Error();
            }
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    private void checkChannelNotRegistered() {
        if (this.channel.isRegistered()) {
            throw new IllegalStateException("EpollMode can only be changed before channel is registered");
        }
    }

    @Override // io.netty.channel.DefaultChannelConfig
    protected final void autoReadCleared() {
        ((AbstractEpollChannel) this.channel).clearEpollIn();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void setMaxBytesPerGatheringWrite(long maxBytesPerGatheringWrite) {
        this.maxBytesPerGatheringWrite = maxBytesPerGatheringWrite;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final long getMaxBytesPerGatheringWrite() {
        return this.maxBytesPerGatheringWrite;
    }
}
