package org.cloudburstmc.netty.channel.raknet.config;

import io.netty.channel.ChannelConfig;

/* loaded from: classes5.dex */
public interface RakChannelConfig extends ChannelConfig {
    int getFlushInterval();

    long getGuid();

    RakMetrics getMetrics();

    int getMtu();

    int getOrderingChannels();

    int getProtocolVersion();

    long getSessionTimeout();

    boolean isAutoFlush();

    void setAutoFlush(boolean z);

    void setFlushInterval(int i);

    RakChannelConfig setGuid(long j);

    RakChannelConfig setMetrics(RakMetrics rakMetrics);

    RakChannelConfig setMtu(int i);

    RakChannelConfig setOrderingChannels(int i);

    RakChannelConfig setProtocolVersion(int i);

    RakChannelConfig setSessionTimeout(long j);
}
