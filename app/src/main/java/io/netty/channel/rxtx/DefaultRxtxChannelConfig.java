package io.netty.channel.rxtx;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.PreferHeapByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.rxtx.RxtxChannelConfig;
import io.netty.util.internal.ObjectUtil;
import java.util.Map;

@Deprecated
/* loaded from: classes4.dex */
final class DefaultRxtxChannelConfig extends DefaultChannelConfig implements RxtxChannelConfig {
    private volatile int baudrate;
    private volatile RxtxChannelConfig.Databits databits;
    private volatile boolean dtr;
    private volatile RxtxChannelConfig.Paritybit paritybit;
    private volatile int readTimeout;
    private volatile boolean rts;
    private volatile RxtxChannelConfig.Stopbits stopbits;
    private volatile int waitTime;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultRxtxChannelConfig(RxtxChannel channel) {
        super(channel);
        this.baudrate = 115200;
        this.stopbits = RxtxChannelConfig.Stopbits.STOPBITS_1;
        this.databits = RxtxChannelConfig.Databits.DATABITS_8;
        this.paritybit = RxtxChannelConfig.Paritybit.NONE;
        this.readTimeout = 1000;
        setAllocator((ByteBufAllocator) new PreferHeapByteBufAllocator(getAllocator()));
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public Map<ChannelOption<?>, Object> getOptions() {
        return getOptions(super.getOptions(), RxtxChannelOption.BAUD_RATE, RxtxChannelOption.DTR, RxtxChannelOption.RTS, RxtxChannelOption.STOP_BITS, RxtxChannelOption.DATA_BITS, RxtxChannelOption.PARITY_BIT, RxtxChannelOption.WAIT_TIME);
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == RxtxChannelOption.BAUD_RATE) {
            return (T) Integer.valueOf(getBaudrate());
        }
        if (channelOption == RxtxChannelOption.DTR) {
            return (T) Boolean.valueOf(isDtr());
        }
        if (channelOption == RxtxChannelOption.RTS) {
            return (T) Boolean.valueOf(isRts());
        }
        if (channelOption == RxtxChannelOption.STOP_BITS) {
            return (T) getStopbits();
        }
        if (channelOption == RxtxChannelOption.DATA_BITS) {
            return (T) getDatabits();
        }
        if (channelOption == RxtxChannelOption.PARITY_BIT) {
            return (T) getParitybit();
        }
        if (channelOption == RxtxChannelOption.WAIT_TIME) {
            return (T) Integer.valueOf(getWaitTimeMillis());
        }
        if (channelOption == RxtxChannelOption.READ_TIMEOUT) {
            return (T) Integer.valueOf(getReadTimeout());
        }
        return (T) super.getOption(channelOption);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> boolean setOption(ChannelOption<T> option, T t) {
        validate(option, t);
        if (option == RxtxChannelOption.BAUD_RATE) {
            setBaudrate(((Integer) t).intValue());
            return true;
        }
        if (option == RxtxChannelOption.DTR) {
            setDtr(((Boolean) t).booleanValue());
            return true;
        }
        if (option == RxtxChannelOption.RTS) {
            setRts(((Boolean) t).booleanValue());
            return true;
        }
        if (option == RxtxChannelOption.STOP_BITS) {
            setStopbits((RxtxChannelConfig.Stopbits) t);
            return true;
        }
        if (option == RxtxChannelOption.DATA_BITS) {
            setDatabits((RxtxChannelConfig.Databits) t);
            return true;
        }
        if (option == RxtxChannelOption.PARITY_BIT) {
            setParitybit((RxtxChannelConfig.Paritybit) t);
            return true;
        }
        if (option == RxtxChannelOption.WAIT_TIME) {
            setWaitTimeMillis(((Integer) t).intValue());
            return true;
        }
        if (option == RxtxChannelOption.READ_TIMEOUT) {
            setReadTimeout(((Integer) t).intValue());
            return true;
        }
        return super.setOption(option, t);
    }

    @Override // io.netty.channel.rxtx.RxtxChannelConfig
    public RxtxChannelConfig setBaudrate(int baudrate) {
        this.baudrate = baudrate;
        return this;
    }

    @Override // io.netty.channel.rxtx.RxtxChannelConfig
    public RxtxChannelConfig setStopbits(RxtxChannelConfig.Stopbits stopbits) {
        this.stopbits = stopbits;
        return this;
    }

    @Override // io.netty.channel.rxtx.RxtxChannelConfig
    public RxtxChannelConfig setDatabits(RxtxChannelConfig.Databits databits) {
        this.databits = databits;
        return this;
    }

    @Override // io.netty.channel.rxtx.RxtxChannelConfig
    public RxtxChannelConfig setParitybit(RxtxChannelConfig.Paritybit paritybit) {
        this.paritybit = paritybit;
        return this;
    }

    @Override // io.netty.channel.rxtx.RxtxChannelConfig
    public int getBaudrate() {
        return this.baudrate;
    }

    @Override // io.netty.channel.rxtx.RxtxChannelConfig
    public RxtxChannelConfig.Stopbits getStopbits() {
        return this.stopbits;
    }

    @Override // io.netty.channel.rxtx.RxtxChannelConfig
    public RxtxChannelConfig.Databits getDatabits() {
        return this.databits;
    }

    @Override // io.netty.channel.rxtx.RxtxChannelConfig
    public RxtxChannelConfig.Paritybit getParitybit() {
        return this.paritybit;
    }

    @Override // io.netty.channel.rxtx.RxtxChannelConfig
    public boolean isDtr() {
        return this.dtr;
    }

    @Override // io.netty.channel.rxtx.RxtxChannelConfig
    public RxtxChannelConfig setDtr(boolean dtr) {
        this.dtr = dtr;
        return this;
    }

    @Override // io.netty.channel.rxtx.RxtxChannelConfig
    public boolean isRts() {
        return this.rts;
    }

    @Override // io.netty.channel.rxtx.RxtxChannelConfig
    public RxtxChannelConfig setRts(boolean rts) {
        this.rts = rts;
        return this;
    }

    @Override // io.netty.channel.rxtx.RxtxChannelConfig
    public int getWaitTimeMillis() {
        return this.waitTime;
    }

    @Override // io.netty.channel.rxtx.RxtxChannelConfig
    public RxtxChannelConfig setWaitTimeMillis(int waitTimeMillis) {
        this.waitTime = ObjectUtil.checkPositiveOrZero(waitTimeMillis, "waitTimeMillis");
        return this;
    }

    @Override // io.netty.channel.rxtx.RxtxChannelConfig
    public RxtxChannelConfig setReadTimeout(int readTimeout) {
        this.readTimeout = ObjectUtil.checkPositiveOrZero(readTimeout, "readTimeout");
        return this;
    }

    @Override // io.netty.channel.rxtx.RxtxChannelConfig
    public int getReadTimeout() {
        return this.readTimeout;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public RxtxChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public RxtxChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public RxtxChannelConfig setWriteSpinCount(int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public RxtxChannelConfig setAllocator(ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public RxtxChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public RxtxChannelConfig setAutoRead(boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public RxtxChannelConfig setAutoClose(boolean autoClose) {
        super.setAutoClose(autoClose);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public RxtxChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public RxtxChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public RxtxChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
        super.setWriteBufferWaterMark(writeBufferWaterMark);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public RxtxChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
}
