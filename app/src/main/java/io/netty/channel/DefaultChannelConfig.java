package io.netty.channel;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.internal.ObjectUtil;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* loaded from: classes4.dex */
public class DefaultChannelConfig implements ChannelConfig {
    private static final int DEFAULT_CONNECT_TIMEOUT = 30000;
    private volatile ByteBufAllocator allocator;
    private volatile boolean autoClose;
    private volatile int autoRead;
    protected final Channel channel;
    private volatile int connectTimeoutMillis;
    private volatile int maxMessagesPerWrite;
    private volatile MessageSizeEstimator msgSizeEstimator;
    private volatile boolean pinEventExecutor;
    private volatile RecvByteBufAllocator rcvBufAllocator;
    private volatile WriteBufferWaterMark writeBufferWaterMark;
    private volatile int writeSpinCount;
    private static final MessageSizeEstimator DEFAULT_MSG_SIZE_ESTIMATOR = DefaultMessageSizeEstimator.DEFAULT;
    private static final AtomicIntegerFieldUpdater<DefaultChannelConfig> AUTOREAD_UPDATER = AtomicIntegerFieldUpdater.newUpdater(DefaultChannelConfig.class, "autoRead");
    private static final AtomicReferenceFieldUpdater<DefaultChannelConfig, WriteBufferWaterMark> WATERMARK_UPDATER = AtomicReferenceFieldUpdater.newUpdater(DefaultChannelConfig.class, WriteBufferWaterMark.class, "writeBufferWaterMark");

    public DefaultChannelConfig(Channel channel) {
        this(channel, new AdaptiveRecvByteBufAllocator());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DefaultChannelConfig(Channel channel, RecvByteBufAllocator allocator) {
        this.allocator = ByteBufAllocator.DEFAULT;
        this.msgSizeEstimator = DEFAULT_MSG_SIZE_ESTIMATOR;
        this.connectTimeoutMillis = DEFAULT_CONNECT_TIMEOUT;
        this.writeSpinCount = 16;
        this.maxMessagesPerWrite = Integer.MAX_VALUE;
        this.autoRead = 1;
        this.autoClose = true;
        this.writeBufferWaterMark = WriteBufferWaterMark.DEFAULT;
        this.pinEventExecutor = true;
        setRecvByteBufAllocator(allocator, channel.metadata());
        this.channel = channel;
    }

    @Override // io.netty.channel.ChannelConfig
    public Map<ChannelOption<?>, Object> getOptions() {
        return getOptions(null, ChannelOption.CONNECT_TIMEOUT_MILLIS, ChannelOption.MAX_MESSAGES_PER_READ, ChannelOption.WRITE_SPIN_COUNT, ChannelOption.ALLOCATOR, ChannelOption.AUTO_READ, ChannelOption.AUTO_CLOSE, ChannelOption.RCVBUF_ALLOCATOR, ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, ChannelOption.WRITE_BUFFER_WATER_MARK, ChannelOption.MESSAGE_SIZE_ESTIMATOR, ChannelOption.SINGLE_EVENTEXECUTOR_PER_GROUP, ChannelOption.MAX_MESSAGES_PER_WRITE);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Map<ChannelOption<?>, Object> getOptions(Map<ChannelOption<?>, Object> result, ChannelOption<?>... options) {
        if (result == null) {
            result = new IdentityHashMap();
        }
        for (ChannelOption<?> o : options) {
            result.put(o, getOption(o));
        }
        return result;
    }

    @Override // io.netty.channel.ChannelConfig
    public boolean setOptions(Map<ChannelOption<?>, ?> options) {
        ObjectUtil.checkNotNull(options, "options");
        boolean setAllOptions = true;
        for (Map.Entry<ChannelOption<?>, ?> e : options.entrySet()) {
            if (!setOption(e.getKey(), e.getValue())) {
                setAllOptions = false;
            }
        }
        return setAllOptions;
    }

    @Override // io.netty.channel.ChannelConfig
    public <T> T getOption(ChannelOption<T> channelOption) {
        ObjectUtil.checkNotNull(channelOption, "option");
        if (channelOption == ChannelOption.CONNECT_TIMEOUT_MILLIS) {
            return (T) Integer.valueOf(getConnectTimeoutMillis());
        }
        if (channelOption == ChannelOption.MAX_MESSAGES_PER_READ) {
            return (T) Integer.valueOf(getMaxMessagesPerRead());
        }
        if (channelOption == ChannelOption.WRITE_SPIN_COUNT) {
            return (T) Integer.valueOf(getWriteSpinCount());
        }
        if (channelOption == ChannelOption.ALLOCATOR) {
            return (T) getAllocator();
        }
        if (channelOption == ChannelOption.RCVBUF_ALLOCATOR) {
            return (T) getRecvByteBufAllocator();
        }
        if (channelOption == ChannelOption.AUTO_READ) {
            return (T) Boolean.valueOf(isAutoRead());
        }
        if (channelOption == ChannelOption.AUTO_CLOSE) {
            return (T) Boolean.valueOf(isAutoClose());
        }
        if (channelOption == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK) {
            return (T) Integer.valueOf(getWriteBufferHighWaterMark());
        }
        if (channelOption == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK) {
            return (T) Integer.valueOf(getWriteBufferLowWaterMark());
        }
        if (channelOption == ChannelOption.WRITE_BUFFER_WATER_MARK) {
            return (T) getWriteBufferWaterMark();
        }
        if (channelOption == ChannelOption.MESSAGE_SIZE_ESTIMATOR) {
            return (T) getMessageSizeEstimator();
        }
        if (channelOption == ChannelOption.SINGLE_EVENTEXECUTOR_PER_GROUP) {
            return (T) Boolean.valueOf(getPinEventExecutorPerGroup());
        }
        if (channelOption == ChannelOption.MAX_MESSAGES_PER_WRITE) {
            return (T) Integer.valueOf(getMaxMessagesPerWrite());
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.channel.ChannelConfig
    public <T> boolean setOption(ChannelOption<T> option, T t) {
        validate(option, t);
        if (option == ChannelOption.CONNECT_TIMEOUT_MILLIS) {
            setConnectTimeoutMillis(((Integer) t).intValue());
            return true;
        }
        if (option == ChannelOption.MAX_MESSAGES_PER_READ) {
            setMaxMessagesPerRead(((Integer) t).intValue());
            return true;
        }
        if (option == ChannelOption.WRITE_SPIN_COUNT) {
            setWriteSpinCount(((Integer) t).intValue());
            return true;
        }
        if (option == ChannelOption.ALLOCATOR) {
            setAllocator((ByteBufAllocator) t);
            return true;
        }
        if (option == ChannelOption.RCVBUF_ALLOCATOR) {
            setRecvByteBufAllocator((RecvByteBufAllocator) t);
            return true;
        }
        if (option == ChannelOption.AUTO_READ) {
            setAutoRead(((Boolean) t).booleanValue());
            return true;
        }
        if (option == ChannelOption.AUTO_CLOSE) {
            setAutoClose(((Boolean) t).booleanValue());
            return true;
        }
        if (option == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK) {
            setWriteBufferHighWaterMark(((Integer) t).intValue());
            return true;
        }
        if (option == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK) {
            setWriteBufferLowWaterMark(((Integer) t).intValue());
            return true;
        }
        if (option == ChannelOption.WRITE_BUFFER_WATER_MARK) {
            setWriteBufferWaterMark((WriteBufferWaterMark) t);
            return true;
        }
        if (option == ChannelOption.MESSAGE_SIZE_ESTIMATOR) {
            setMessageSizeEstimator((MessageSizeEstimator) t);
            return true;
        }
        if (option == ChannelOption.SINGLE_EVENTEXECUTOR_PER_GROUP) {
            setPinEventExecutorPerGroup(((Boolean) t).booleanValue());
            return true;
        }
        if (option == ChannelOption.MAX_MESSAGES_PER_WRITE) {
            setMaxMessagesPerWrite(((Integer) t).intValue());
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <T> void validate(ChannelOption<T> option, T value) {
        ((ChannelOption) ObjectUtil.checkNotNull(option, "option")).validate(value);
    }

    @Override // io.netty.channel.ChannelConfig
    public int getConnectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }

    @Override // io.netty.channel.ChannelConfig
    public ChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
        ObjectUtil.checkPositiveOrZero(connectTimeoutMillis, "connectTimeoutMillis");
        this.connectTimeoutMillis = connectTimeoutMillis;
        return this;
    }

    @Override // io.netty.channel.ChannelConfig
    @Deprecated
    public int getMaxMessagesPerRead() {
        try {
            MaxMessagesRecvByteBufAllocator allocator = (MaxMessagesRecvByteBufAllocator) getRecvByteBufAllocator();
            return allocator.maxMessagesPerRead();
        } catch (ClassCastException e) {
            throw new IllegalStateException("getRecvByteBufAllocator() must return an object of type MaxMessagesRecvByteBufAllocator", e);
        }
    }

    @Override // io.netty.channel.ChannelConfig
    @Deprecated
    public ChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
        try {
            MaxMessagesRecvByteBufAllocator allocator = (MaxMessagesRecvByteBufAllocator) getRecvByteBufAllocator();
            allocator.maxMessagesPerRead(maxMessagesPerRead);
            return this;
        } catch (ClassCastException e) {
            throw new IllegalStateException("getRecvByteBufAllocator() must return an object of type MaxMessagesRecvByteBufAllocator", e);
        }
    }

    public int getMaxMessagesPerWrite() {
        return this.maxMessagesPerWrite;
    }

    public ChannelConfig setMaxMessagesPerWrite(int maxMessagesPerWrite) {
        this.maxMessagesPerWrite = ObjectUtil.checkPositive(maxMessagesPerWrite, "maxMessagesPerWrite");
        return this;
    }

    @Override // io.netty.channel.ChannelConfig
    public int getWriteSpinCount() {
        return this.writeSpinCount;
    }

    @Override // io.netty.channel.ChannelConfig
    public ChannelConfig setWriteSpinCount(int writeSpinCount) {
        ObjectUtil.checkPositive(writeSpinCount, "writeSpinCount");
        if (writeSpinCount == Integer.MAX_VALUE) {
            writeSpinCount--;
        }
        this.writeSpinCount = writeSpinCount;
        return this;
    }

    @Override // io.netty.channel.ChannelConfig
    public ByteBufAllocator getAllocator() {
        return this.allocator;
    }

    @Override // io.netty.channel.ChannelConfig
    public ChannelConfig setAllocator(ByteBufAllocator allocator) {
        this.allocator = (ByteBufAllocator) ObjectUtil.checkNotNull(allocator, "allocator");
        return this;
    }

    @Override // io.netty.channel.ChannelConfig
    public <T extends RecvByteBufAllocator> T getRecvByteBufAllocator() {
        return (T) this.rcvBufAllocator;
    }

    @Override // io.netty.channel.ChannelConfig
    public ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
        this.rcvBufAllocator = (RecvByteBufAllocator) ObjectUtil.checkNotNull(allocator, "allocator");
        return this;
    }

    private void setRecvByteBufAllocator(RecvByteBufAllocator allocator, ChannelMetadata metadata) {
        ObjectUtil.checkNotNull(allocator, "allocator");
        ObjectUtil.checkNotNull(metadata, "metadata");
        if (allocator instanceof MaxMessagesRecvByteBufAllocator) {
            ((MaxMessagesRecvByteBufAllocator) allocator).maxMessagesPerRead(metadata.defaultMaxMessagesPerRead());
        }
        setRecvByteBufAllocator(allocator);
    }

    @Override // io.netty.channel.ChannelConfig
    public boolean isAutoRead() {
        return this.autoRead == 1;
    }

    @Override // io.netty.channel.ChannelConfig
    public ChannelConfig setAutoRead(boolean z) {
        boolean z2 = AUTOREAD_UPDATER.getAndSet(this, z ? 1 : 0) == 1;
        if (z && !z2) {
            this.channel.read();
        } else if (!z && z2) {
            autoReadCleared();
        }
        return this;
    }

    protected void autoReadCleared() {
    }

    @Override // io.netty.channel.ChannelConfig
    public boolean isAutoClose() {
        return this.autoClose;
    }

    @Override // io.netty.channel.ChannelConfig
    public ChannelConfig setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
        return this;
    }

    @Override // io.netty.channel.ChannelConfig
    public int getWriteBufferHighWaterMark() {
        return this.writeBufferWaterMark.high();
    }

    @Override // io.netty.channel.ChannelConfig
    public ChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
        WriteBufferWaterMark waterMark;
        ObjectUtil.checkPositiveOrZero(writeBufferHighWaterMark, "writeBufferHighWaterMark");
        do {
            waterMark = this.writeBufferWaterMark;
            if (writeBufferHighWaterMark < waterMark.low()) {
                throw new IllegalArgumentException("writeBufferHighWaterMark cannot be less than writeBufferLowWaterMark (" + waterMark.low() + "): " + writeBufferHighWaterMark);
            }
        } while (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(WATERMARK_UPDATER, this, waterMark, new WriteBufferWaterMark(waterMark.low(), writeBufferHighWaterMark, false)));
        return this;
    }

    @Override // io.netty.channel.ChannelConfig
    public int getWriteBufferLowWaterMark() {
        return this.writeBufferWaterMark.low();
    }

    @Override // io.netty.channel.ChannelConfig
    public ChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
        WriteBufferWaterMark waterMark;
        ObjectUtil.checkPositiveOrZero(writeBufferLowWaterMark, "writeBufferLowWaterMark");
        do {
            waterMark = this.writeBufferWaterMark;
            if (writeBufferLowWaterMark > waterMark.high()) {
                throw new IllegalArgumentException("writeBufferLowWaterMark cannot be greater than writeBufferHighWaterMark (" + waterMark.high() + "): " + writeBufferLowWaterMark);
            }
        } while (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(WATERMARK_UPDATER, this, waterMark, new WriteBufferWaterMark(writeBufferLowWaterMark, waterMark.high(), false)));
        return this;
    }

    @Override // io.netty.channel.ChannelConfig
    public ChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
        this.writeBufferWaterMark = (WriteBufferWaterMark) ObjectUtil.checkNotNull(writeBufferWaterMark, "writeBufferWaterMark");
        return this;
    }

    @Override // io.netty.channel.ChannelConfig
    public WriteBufferWaterMark getWriteBufferWaterMark() {
        return this.writeBufferWaterMark;
    }

    @Override // io.netty.channel.ChannelConfig
    public MessageSizeEstimator getMessageSizeEstimator() {
        return this.msgSizeEstimator;
    }

    @Override // io.netty.channel.ChannelConfig
    public ChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
        this.msgSizeEstimator = (MessageSizeEstimator) ObjectUtil.checkNotNull(estimator, "estimator");
        return this;
    }

    private ChannelConfig setPinEventExecutorPerGroup(boolean pinEventExecutor) {
        this.pinEventExecutor = pinEventExecutor;
        return this;
    }

    private boolean getPinEventExecutorPerGroup() {
        return this.pinEventExecutor;
    }
}
