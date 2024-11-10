package org.cloudburstmc.netty.channel.raknet.config;

import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import java.util.Map;
import org.cloudburstmc.netty.channel.raknet.RakConstants;

/* loaded from: classes5.dex */
public class DefaultRakSessionConfig extends DefaultChannelConfig implements RakChannelConfig {
    private volatile boolean autoFlush;
    private volatile int flushInterval;
    private volatile long guid;
    private volatile RakMetrics metrics;
    private volatile int mtu;
    private volatile int orderingChannels;
    private volatile int protocolVersion;
    private volatile long sessionTimeout;

    public DefaultRakSessionConfig(Channel channel) {
        super(channel);
        this.mtu = RakConstants.MAXIMUM_MTU_SIZE;
        this.orderingChannels = 16;
        this.sessionTimeout = 10000L;
        this.autoFlush = true;
        this.flushInterval = 10;
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public Map<ChannelOption<?>, Object> getOptions() {
        return getOptions(super.getOptions(), RakChannelOption.RAK_GUID, RakChannelOption.RAK_MAX_CHANNELS, RakChannelOption.RAK_MTU, RakChannelOption.RAK_PROTOCOL_VERSION);
    }

    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == RakChannelOption.RAK_GUID) {
            return (T) Long.valueOf(getGuid());
        }
        if (channelOption == RakChannelOption.RAK_MTU) {
            return (T) Integer.valueOf(getMtu());
        }
        if (channelOption == RakChannelOption.RAK_PROTOCOL_VERSION) {
            return (T) Integer.valueOf(getProtocolVersion());
        }
        if (channelOption == RakChannelOption.RAK_ORDERING_CHANNELS) {
            return (T) Integer.valueOf(getOrderingChannels());
        }
        if (channelOption == RakChannelOption.RAK_METRICS) {
            return (T) getMetrics();
        }
        if (channelOption == RakChannelOption.RAK_SESSION_TIMEOUT) {
            return (T) Long.valueOf(getSessionTimeout());
        }
        if (channelOption == RakChannelOption.RAK_AUTO_FLUSH) {
            return (T) Boolean.valueOf(isAutoFlush());
        }
        if (channelOption == RakChannelOption.RAK_FLUSH_INTERVAL) {
            return (T) Integer.valueOf(getFlushInterval());
        }
        return (T) this.channel.parent().config().getOption(channelOption);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> boolean setOption(ChannelOption<T> option, T t) {
        validate(option, t);
        if (option == RakChannelOption.RAK_GUID) {
            setGuid(((Long) t).longValue());
        } else if (option == RakChannelOption.RAK_MTU) {
            setMtu(((Integer) t).intValue());
        } else if (option == RakChannelOption.RAK_PROTOCOL_VERSION) {
            setProtocolVersion(((Integer) t).intValue());
        } else if (option == RakChannelOption.RAK_ORDERING_CHANNELS) {
            setOrderingChannels(((Integer) t).intValue());
        } else if (option == RakChannelOption.RAK_METRICS) {
            setMetrics((RakMetrics) t);
        } else {
            if (option == RakChannelOption.RAK_SESSION_TIMEOUT) {
                setSessionTimeout(((Long) t).longValue());
                return true;
            }
            if (option == RakChannelOption.RAK_AUTO_FLUSH) {
                setAutoFlush(((Boolean) t).booleanValue());
            } else if (option == RakChannelOption.RAK_FLUSH_INTERVAL) {
                setFlushInterval(((Integer) t).intValue());
            } else {
                return this.channel.parent().config().setOption(option, t);
            }
        }
        return true;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig
    public long getGuid() {
        return this.guid;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig
    public RakChannelConfig setGuid(long guid) {
        this.guid = guid;
        return this;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig
    public int getMtu() {
        return this.mtu;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig
    public RakChannelConfig setMtu(int mtu) {
        this.mtu = mtu;
        return this;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig
    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig
    public RakChannelConfig setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
        return this;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig
    public int getOrderingChannels() {
        return this.orderingChannels;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig
    public RakChannelConfig setOrderingChannels(int orderingChannels) {
        this.orderingChannels = orderingChannels;
        return this;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig
    public RakMetrics getMetrics() {
        return this.metrics;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig
    public RakChannelConfig setMetrics(RakMetrics metrics) {
        this.metrics = metrics;
        return this;
    }

    public RakChannelConfig setSessionTimeout(long timeout) {
        this.sessionTimeout = timeout;
        return this;
    }

    public long getSessionTimeout() {
        return this.sessionTimeout;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig
    public boolean isAutoFlush() {
        return this.autoFlush;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig
    public void setAutoFlush(boolean autoFlush) {
        this.autoFlush = autoFlush;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig
    public int getFlushInterval() {
        return this.flushInterval;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig
    public void setFlushInterval(int flushInterval) {
        this.flushInterval = flushInterval;
    }
}
