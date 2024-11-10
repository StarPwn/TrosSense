package org.cloudburstmc.netty.channel.raknet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig;

/* loaded from: classes5.dex */
public interface RakChannel extends Channel {
    @Override // io.netty.channel.Channel
    RakChannelConfig config();

    ChannelPipeline rakPipeline();
}
