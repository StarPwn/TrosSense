package org.cloudburstmc.netty.channel.raknet.config;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import java.util.Map;
import org.cloudburstmc.netty.channel.raknet.RakConstants;

/* loaded from: classes5.dex */
public class DefaultRakClientConfig extends DefaultRakSessionConfig {
    private volatile long connectTimeout;
    private volatile long serverGuid;
    private volatile long sessionTimeout;
    private volatile ByteBuf unconnectedMagic;

    public DefaultRakClientConfig(Channel channel) {
        super(channel);
        this.unconnectedMagic = Unpooled.wrappedBuffer(RakConstants.DEFAULT_UNCONNECTED_MAGIC);
        this.connectTimeout = 10000L;
        this.sessionTimeout = 10000L;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.DefaultRakSessionConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public Map<ChannelOption<?>, Object> getOptions() {
        return getOptions(super.getOptions(), RakChannelOption.RAK_UNCONNECTED_MAGIC);
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.DefaultRakSessionConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == RakChannelOption.RAK_UNCONNECTED_MAGIC) {
            return (T) getUnconnectedMagic();
        }
        if (channelOption == RakChannelOption.RAK_CONNECT_TIMEOUT) {
            return (T) Long.valueOf(getConnectTimeout());
        }
        if (channelOption == RakChannelOption.RAK_REMOTE_GUID) {
            return (T) Long.valueOf(getServerGuid());
        }
        if (channelOption == RakChannelOption.RAK_SESSION_TIMEOUT) {
            return (T) Long.valueOf(getSessionTimeout());
        }
        return (T) super.getOption(channelOption);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.cloudburstmc.netty.channel.raknet.config.DefaultRakSessionConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> boolean setOption(ChannelOption<T> option, T t) {
        validate(option, t);
        if (option == RakChannelOption.RAK_UNCONNECTED_MAGIC) {
            setUnconnectedMagic((ByteBuf) t);
            return true;
        }
        if (option == RakChannelOption.RAK_CONNECT_TIMEOUT) {
            setConnectTimeout(((Long) t).longValue());
            return true;
        }
        if (option == RakChannelOption.RAK_REMOTE_GUID) {
            setServerGuid(((Long) t).longValue());
            return true;
        }
        if (option == RakChannelOption.RAK_SESSION_TIMEOUT) {
            setSessionTimeout(((Long) t).longValue());
            return true;
        }
        return super.setOption(option, t);
    }

    public ByteBuf getUnconnectedMagic() {
        return this.unconnectedMagic.slice();
    }

    public RakServerChannelConfig setUnconnectedMagic(ByteBuf unconnectedMagic) {
        if (unconnectedMagic.readableBytes() < 16) {
            throw new IllegalArgumentException("Unconnect magic must at least be 16 bytes");
        }
        this.unconnectedMagic = unconnectedMagic.copy().asReadOnly();
        return null;
    }

    public long getConnectTimeout() {
        return this.connectTimeout;
    }

    public DefaultRakClientConfig setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public long getServerGuid() {
        return this.serverGuid;
    }

    public DefaultRakClientConfig setServerGuid(long serverGuid) {
        this.serverGuid = serverGuid;
        return this;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.DefaultRakSessionConfig, org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig
    public RakChannelConfig setSessionTimeout(long timeout) {
        this.sessionTimeout = timeout;
        return this;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.DefaultRakSessionConfig, org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig
    public long getSessionTimeout() {
        return this.sessionTimeout;
    }
}
