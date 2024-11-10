package org.cloudburstmc.netty.channel.raknet.config;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelConfig;

/* loaded from: classes5.dex */
public interface RakServerChannelConfig extends ChannelConfig {
    ByteBuf getAdvertisement();

    int getGlobalPacketLimit();

    long getGuid();

    boolean getHandlePing();

    int getMaxChannels();

    int getMaxConnections();

    int getMaxMtu();

    int getMinMtu();

    int getPacketLimit();

    int[] getSupportedProtocols();

    ByteBuf getUnconnectedMagic();

    int getUnconnectedPacketLimit();

    RakServerChannelConfig setAdvertisement(ByteBuf byteBuf);

    void setGlobalPacketLimit(int i);

    RakServerChannelConfig setGuid(long j);

    RakServerChannelConfig setHandlePing(boolean z);

    RakServerChannelConfig setMaxChannels(int i);

    RakServerChannelConfig setMaxConnections(int i);

    RakServerChannelConfig setMaxMtu(int i);

    RakServerChannelConfig setMinMtu(int i);

    void setPacketLimit(int i);

    RakServerChannelConfig setSupportedProtocols(int[] iArr);

    RakServerChannelConfig setUnconnectedMagic(ByteBuf byteBuf);

    void setUnconnectedPacketLimit(int i);
}
