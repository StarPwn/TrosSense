package io.netty.channel.udt;

import com.barchart.udt.OptionUDT;
import com.barchart.udt.SocketUDT;
import com.barchart.udt.nio.ChannelUDT;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;
import java.io.IOException;
import java.util.Map;

@Deprecated
/* loaded from: classes4.dex */
public class DefaultUdtChannelConfig extends DefaultChannelConfig implements UdtChannelConfig {
    private static final int K = 1024;
    private static final int M = 1048576;
    private volatile int allocatorReceiveBufferSize;
    private volatile int allocatorSendBufferSize;
    private volatile int protocolReceiveBufferSize;
    private volatile int protocolSendBufferSize;
    private volatile boolean reuseAddress;
    private volatile int soLinger;
    private volatile int systemReceiveBufferSize;
    private volatile int systemSendBufferSize;

    public DefaultUdtChannelConfig(UdtChannel channel, ChannelUDT channelUDT, boolean apply) throws IOException {
        super(channel);
        this.protocolReceiveBufferSize = 10485760;
        this.protocolSendBufferSize = 10485760;
        this.systemReceiveBufferSize = 1048576;
        this.systemSendBufferSize = 1048576;
        this.allocatorReceiveBufferSize = 131072;
        this.allocatorSendBufferSize = 131072;
        this.reuseAddress = true;
        if (apply) {
            apply(channelUDT);
        }
    }

    protected void apply(ChannelUDT channelUDT) throws IOException {
        SocketUDT socketUDT = channelUDT.socketUDT();
        socketUDT.setReuseAddress(isReuseAddress());
        socketUDT.setSendBufferSize(getSendBufferSize());
        if (getSoLinger() <= 0) {
            socketUDT.setSoLinger(false, 0);
        } else {
            socketUDT.setSoLinger(true, getSoLinger());
        }
        socketUDT.setOption(OptionUDT.Protocol_Receive_Buffer_Size, Integer.valueOf(getProtocolReceiveBufferSize()));
        socketUDT.setOption(OptionUDT.Protocol_Send_Buffer_Size, Integer.valueOf(getProtocolSendBufferSize()));
        socketUDT.setOption(OptionUDT.System_Receive_Buffer_Size, Integer.valueOf(getSystemReceiveBufferSize()));
        socketUDT.setOption(OptionUDT.System_Send_Buffer_Size, Integer.valueOf(getSystemSendBufferSize()));
    }

    @Override // io.netty.channel.udt.UdtChannelConfig
    public int getProtocolReceiveBufferSize() {
        return this.protocolReceiveBufferSize;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE) {
            return (T) Integer.valueOf(getProtocolReceiveBufferSize());
        }
        if (channelOption == UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE) {
            return (T) Integer.valueOf(getProtocolSendBufferSize());
        }
        if (channelOption == UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE) {
            return (T) Integer.valueOf(getSystemReceiveBufferSize());
        }
        if (channelOption == UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE) {
            return (T) Integer.valueOf(getSystemSendBufferSize());
        }
        if (channelOption == ChannelOption.SO_RCVBUF) {
            return (T) Integer.valueOf(getReceiveBufferSize());
        }
        if (channelOption == ChannelOption.SO_SNDBUF) {
            return (T) Integer.valueOf(getSendBufferSize());
        }
        if (channelOption == ChannelOption.SO_REUSEADDR) {
            return (T) Boolean.valueOf(isReuseAddress());
        }
        if (channelOption == ChannelOption.SO_LINGER) {
            return (T) Integer.valueOf(getSoLinger());
        }
        return (T) super.getOption(channelOption);
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public Map<ChannelOption<?>, Object> getOptions() {
        return getOptions(super.getOptions(), UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE, UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE, UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE, UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE, ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER);
    }

    @Override // io.netty.channel.udt.UdtChannelConfig
    public int getReceiveBufferSize() {
        return this.allocatorReceiveBufferSize;
    }

    @Override // io.netty.channel.udt.UdtChannelConfig
    public int getSendBufferSize() {
        return this.allocatorSendBufferSize;
    }

    @Override // io.netty.channel.udt.UdtChannelConfig
    public int getSoLinger() {
        return this.soLinger;
    }

    @Override // io.netty.channel.udt.UdtChannelConfig
    public boolean isReuseAddress() {
        return this.reuseAddress;
    }

    @Override // io.netty.channel.udt.UdtChannelConfig
    public UdtChannelConfig setProtocolReceiveBufferSize(int protocolReceiveBufferSize) {
        this.protocolReceiveBufferSize = protocolReceiveBufferSize;
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> boolean setOption(ChannelOption<T> option, T t) {
        validate(option, t);
        if (option == UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE) {
            setProtocolReceiveBufferSize(((Integer) t).intValue());
            return true;
        }
        if (option == UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE) {
            setProtocolSendBufferSize(((Integer) t).intValue());
            return true;
        }
        if (option == UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE) {
            setSystemReceiveBufferSize(((Integer) t).intValue());
            return true;
        }
        if (option == UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE) {
            setSystemSendBufferSize(((Integer) t).intValue());
            return true;
        }
        if (option == ChannelOption.SO_RCVBUF) {
            setReceiveBufferSize(((Integer) t).intValue());
            return true;
        }
        if (option == ChannelOption.SO_SNDBUF) {
            setSendBufferSize(((Integer) t).intValue());
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
        return super.setOption(option, t);
    }

    @Override // io.netty.channel.udt.UdtChannelConfig
    public UdtChannelConfig setReceiveBufferSize(int receiveBufferSize) {
        this.allocatorReceiveBufferSize = receiveBufferSize;
        return this;
    }

    @Override // io.netty.channel.udt.UdtChannelConfig
    public UdtChannelConfig setReuseAddress(boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
        return this;
    }

    @Override // io.netty.channel.udt.UdtChannelConfig
    public UdtChannelConfig setSendBufferSize(int sendBufferSize) {
        this.allocatorSendBufferSize = sendBufferSize;
        return this;
    }

    @Override // io.netty.channel.udt.UdtChannelConfig
    public UdtChannelConfig setSoLinger(int soLinger) {
        this.soLinger = soLinger;
        return this;
    }

    @Override // io.netty.channel.udt.UdtChannelConfig
    public int getSystemReceiveBufferSize() {
        return this.systemReceiveBufferSize;
    }

    @Override // io.netty.channel.udt.UdtChannelConfig
    public UdtChannelConfig setSystemReceiveBufferSize(int systemReceiveBufferSize) {
        this.systemReceiveBufferSize = systemReceiveBufferSize;
        return this;
    }

    @Override // io.netty.channel.udt.UdtChannelConfig
    public int getProtocolSendBufferSize() {
        return this.protocolSendBufferSize;
    }

    @Override // io.netty.channel.udt.UdtChannelConfig
    public UdtChannelConfig setProtocolSendBufferSize(int protocolSendBufferSize) {
        this.protocolSendBufferSize = protocolSendBufferSize;
        return this;
    }

    @Override // io.netty.channel.udt.UdtChannelConfig
    public UdtChannelConfig setSystemSendBufferSize(int systemSendBufferSize) {
        this.systemSendBufferSize = systemSendBufferSize;
        return this;
    }

    @Override // io.netty.channel.udt.UdtChannelConfig
    public int getSystemSendBufferSize() {
        return this.systemSendBufferSize;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    @Deprecated
    public UdtChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtChannelConfig setWriteSpinCount(int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtChannelConfig setAllocator(ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtChannelConfig setAutoRead(boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtChannelConfig setAutoClose(boolean autoClose) {
        super.setAutoClose(autoClose);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
        super.setWriteBufferWaterMark(writeBufferWaterMark);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public UdtChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
}
