package io.netty.channel.kqueue;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.unix.IntegerUnixChannelOption;
import io.netty.channel.unix.Limits;
import io.netty.channel.unix.RawUnixChannelOption;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

/* loaded from: classes4.dex */
public class KQueueChannelConfig extends DefaultChannelConfig {
    private volatile long maxBytesPerGatheringWrite;
    private volatile boolean transportProvidesGuess;

    /* JADX INFO: Access modifiers changed from: package-private */
    public KQueueChannelConfig(AbstractKQueueChannel channel) {
        super(channel);
        this.maxBytesPerGatheringWrite = Limits.SSIZE_MAX;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public KQueueChannelConfig(AbstractKQueueChannel channel, RecvByteBufAllocator recvByteBufAllocator) {
        super(channel, recvByteBufAllocator);
        this.maxBytesPerGatheringWrite = Limits.SSIZE_MAX;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public Map<ChannelOption<?>, Object> getOptions() {
        return getOptions(super.getOptions(), KQueueChannelOption.RCV_ALLOC_TRANSPORT_PROVIDES_GUESS);
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == KQueueChannelOption.RCV_ALLOC_TRANSPORT_PROVIDES_GUESS) {
            return (T) Boolean.valueOf(getRcvAllocTransportProvidesGuess());
        }
        try {
            if (channelOption instanceof IntegerUnixChannelOption) {
                IntegerUnixChannelOption integerUnixChannelOption = (IntegerUnixChannelOption) channelOption;
                return (T) Integer.valueOf(((AbstractKQueueChannel) this.channel).socket.getIntOpt(integerUnixChannelOption.level(), integerUnixChannelOption.optname()));
            }
            if (channelOption instanceof RawUnixChannelOption) {
                RawUnixChannelOption rawUnixChannelOption = (RawUnixChannelOption) channelOption;
                ByteBuffer allocate = ByteBuffer.allocate(rawUnixChannelOption.length());
                ((AbstractKQueueChannel) this.channel).socket.getRawOpt(rawUnixChannelOption.level(), rawUnixChannelOption.optname(), allocate);
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
        if (option == KQueueChannelOption.RCV_ALLOC_TRANSPORT_PROVIDES_GUESS) {
            setRcvAllocTransportProvidesGuess(((Boolean) t).booleanValue());
            return true;
        }
        try {
            if (option instanceof IntegerUnixChannelOption) {
                IntegerUnixChannelOption opt = (IntegerUnixChannelOption) option;
                ((AbstractKQueueChannel) this.channel).socket.setIntOpt(opt.level(), opt.optname(), ((Integer) t).intValue());
                return true;
            }
            if (option instanceof RawUnixChannelOption) {
                RawUnixChannelOption opt2 = (RawUnixChannelOption) option;
                ((AbstractKQueueChannel) this.channel).socket.setRawOpt(opt2.level(), opt2.optname(), (ByteBuffer) t);
                return true;
            }
            return super.setOption(option, t);
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public KQueueChannelConfig setRcvAllocTransportProvidesGuess(boolean transportProvidesGuess) {
        this.transportProvidesGuess = transportProvidesGuess;
        return this;
    }

    public boolean getRcvAllocTransportProvidesGuess() {
        return this.transportProvidesGuess;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public KQueueChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueChannelConfig setWriteSpinCount(int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueChannelConfig setAllocator(ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
        if (!(allocator.newHandle() instanceof RecvByteBufAllocator.ExtendedHandle)) {
            throw new IllegalArgumentException("allocator.newHandle() must return an object of type: " + RecvByteBufAllocator.ExtendedHandle.class);
        }
        super.setRecvByteBufAllocator(allocator);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueChannelConfig setAutoRead(boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public KQueueChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public KQueueChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
        super.setWriteBufferWaterMark(writeBufferWaterMark);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public KQueueChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig
    protected final void autoReadCleared() {
        ((AbstractKQueueChannel) this.channel).clearReadFilter();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void setMaxBytesPerGatheringWrite(long maxBytesPerGatheringWrite) {
        this.maxBytesPerGatheringWrite = Math.min(Limits.SSIZE_MAX, maxBytesPerGatheringWrite);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long getMaxBytesPerGatheringWrite() {
        return this.maxBytesPerGatheringWrite;
    }
}
