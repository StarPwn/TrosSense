package org.cloudburstmc.netty.channel.raknet.config;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.cloudburstmc.netty.channel.raknet.RakConstants;
import org.cloudburstmc.netty.channel.raknet.RakServerChannel;

/* loaded from: classes5.dex */
public class DefaultRakServerConfig extends DefaultChannelConfig implements RakServerChannelConfig {
    private volatile ByteBuf advertisement;
    private volatile int globalPacketLimit;
    private volatile long guid;
    private volatile boolean handlePing;
    private volatile int maxChannels;
    private volatile int maxConnections;
    private volatile int maxMtu;
    private volatile int minMtu;
    private volatile int packetLimit;
    private volatile int[] supportedProtocols;
    private volatile ByteBuf unconnectedMagic;
    private volatile int unconnectedPacketLimit;

    public DefaultRakServerConfig(RakServerChannel channel) {
        super(channel);
        this.guid = ThreadLocalRandom.current().nextLong();
        this.unconnectedMagic = Unpooled.wrappedBuffer(RakConstants.DEFAULT_UNCONNECTED_MAGIC);
        this.maxMtu = RakConstants.MAXIMUM_MTU_SIZE;
        this.minMtu = RakConstants.MINIMUM_MTU_SIZE;
        this.packetLimit = 120;
        this.globalPacketLimit = 100000;
        this.unconnectedPacketLimit = 10;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public Map<ChannelOption<?>, Object> getOptions() {
        return getOptions(super.getOptions(), RakChannelOption.RAK_GUID, RakChannelOption.RAK_MAX_CHANNELS, RakChannelOption.RAK_MAX_CONNECTIONS, RakChannelOption.RAK_SUPPORTED_PROTOCOLS, RakChannelOption.RAK_UNCONNECTED_MAGIC, RakChannelOption.RAK_ADVERTISEMENT);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == RakChannelOption.RAK_MAX_MTU) {
            return (T) Integer.valueOf(getMaxMtu());
        }
        if (channelOption == RakChannelOption.RAK_MIN_MTU) {
            return (T) Integer.valueOf(getMinMtu());
        }
        if (channelOption == RakChannelOption.RAK_GUID) {
            return (T) Long.valueOf(getGuid());
        }
        if (channelOption == RakChannelOption.RAK_MAX_CHANNELS) {
            return (T) Integer.valueOf(getMaxChannels());
        }
        if (channelOption == RakChannelOption.RAK_MAX_CONNECTIONS) {
            return (T) Integer.valueOf(getMaxConnections());
        }
        if (channelOption == RakChannelOption.RAK_SUPPORTED_PROTOCOLS) {
            return (T) getSupportedProtocols();
        }
        if (channelOption == RakChannelOption.RAK_UNCONNECTED_MAGIC) {
            return (T) getUnconnectedMagic();
        }
        if (channelOption == RakChannelOption.RAK_ADVERTISEMENT) {
            return (T) getAdvertisement();
        }
        if (channelOption == RakChannelOption.RAK_HANDLE_PING) {
            return (T) Boolean.valueOf(getHandlePing());
        }
        if (channelOption == RakChannelOption.RAK_PACKET_LIMIT) {
            return (T) Integer.valueOf(getPacketLimit());
        }
        if (channelOption == RakChannelOption.RAK_GLOBAL_PACKET_LIMIT) {
            return (T) Integer.valueOf(getGlobalPacketLimit());
        }
        if (channelOption == RakChannelOption.RAK_OFFLINE_PACKET_LIMIT) {
            return (T) Integer.valueOf(getUnconnectedPacketLimit());
        }
        return (T) this.channel.parent().config().getOption(channelOption);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> boolean setOption(ChannelOption<T> option, T t) {
        validate(option, t);
        if (option == RakChannelOption.RAK_GUID) {
            setGuid(((Long) t).longValue());
            return true;
        }
        if (option == RakChannelOption.RAK_MAX_CHANNELS) {
            setMaxChannels(((Integer) t).intValue());
            return true;
        }
        if (option == RakChannelOption.RAK_MAX_CONNECTIONS) {
            setMaxConnections(((Integer) t).intValue());
            return true;
        }
        if (option == RakChannelOption.RAK_SUPPORTED_PROTOCOLS) {
            setSupportedProtocols((int[]) t);
            return true;
        }
        if (option == RakChannelOption.RAK_UNCONNECTED_MAGIC) {
            setUnconnectedMagic((ByteBuf) t);
            return true;
        }
        if (option == RakChannelOption.RAK_ADVERTISEMENT) {
            setAdvertisement((ByteBuf) t);
            return true;
        }
        if (option == RakChannelOption.RAK_HANDLE_PING) {
            setHandlePing(((Boolean) t).booleanValue());
            return true;
        }
        if (option == RakChannelOption.RAK_MAX_MTU) {
            setMaxMtu(((Integer) t).intValue());
            return true;
        }
        if (option == RakChannelOption.RAK_MIN_MTU) {
            setMinMtu(((Integer) t).intValue());
            return true;
        }
        if (option == RakChannelOption.RAK_PACKET_LIMIT) {
            setPacketLimit(((Integer) t).intValue());
            return true;
        }
        if (option == RakChannelOption.RAK_OFFLINE_PACKET_LIMIT) {
            setUnconnectedPacketLimit(((Integer) t).intValue());
            return true;
        }
        if (option == RakChannelOption.RAK_GLOBAL_PACKET_LIMIT) {
            setGlobalPacketLimit(((Integer) t).intValue());
            return true;
        }
        return this.channel.parent().config().setOption(option, t);
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public int getMaxChannels() {
        return this.maxChannels;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public RakServerChannelConfig setMaxChannels(int maxChannels) {
        if (maxChannels < 1 || maxChannels > 256) {
            throw new IllegalArgumentException("maxChannels can only be a value between 1 and 256");
        }
        this.maxChannels = maxChannels;
        return this;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public long getGuid() {
        return this.guid;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public RakServerChannelConfig setGuid(long guid) {
        this.guid = guid;
        return this;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public int[] getSupportedProtocols() {
        return this.supportedProtocols;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public RakServerChannelConfig setSupportedProtocols(int[] supportedProtocols) {
        if (supportedProtocols == null) {
            this.supportedProtocols = null;
        } else {
            this.supportedProtocols = Arrays.copyOf(supportedProtocols, supportedProtocols.length);
            Arrays.sort(this.supportedProtocols);
        }
        return this;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public int getMaxConnections() {
        return this.maxConnections;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public RakServerChannelConfig setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
        return this;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public ByteBuf getUnconnectedMagic() {
        return this.unconnectedMagic.slice();
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public RakServerChannelConfig setUnconnectedMagic(ByteBuf unconnectedMagic) {
        if (unconnectedMagic.readableBytes() < 16) {
            throw new IllegalArgumentException("Unconnected magic must at least be 16 bytes");
        }
        this.unconnectedMagic = unconnectedMagic.copy().asReadOnly();
        return this;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public ByteBuf getAdvertisement() {
        return this.advertisement;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public RakServerChannelConfig setAdvertisement(ByteBuf advertisement) {
        this.advertisement = advertisement.copy().asReadOnly();
        return this;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public boolean getHandlePing() {
        return this.handlePing;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public RakServerChannelConfig setHandlePing(boolean handlePing) {
        this.handlePing = handlePing;
        return this;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public RakServerChannelConfig setMaxMtu(int maxMtu) {
        this.maxMtu = maxMtu;
        return this;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public int getMaxMtu() {
        return this.maxMtu;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public RakServerChannelConfig setMinMtu(int minMtu) {
        this.minMtu = minMtu;
        return this;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public int getMinMtu() {
        return this.minMtu;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public void setPacketLimit(int limit) {
        this.packetLimit = limit;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public int getPacketLimit() {
        return this.packetLimit;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public int getUnconnectedPacketLimit() {
        return this.unconnectedPacketLimit;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public void setUnconnectedPacketLimit(int unconnectedPacketLimit) {
        this.unconnectedPacketLimit = unconnectedPacketLimit;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public int getGlobalPacketLimit() {
        return this.globalPacketLimit;
    }

    @Override // org.cloudburstmc.netty.channel.raknet.config.RakServerChannelConfig
    public void setGlobalPacketLimit(int globalPacketLimit) {
        this.globalPacketLimit = globalPacketLimit;
    }
}
