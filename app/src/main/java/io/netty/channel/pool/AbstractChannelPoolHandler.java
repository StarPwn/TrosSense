package io.netty.channel.pool;

import io.netty.channel.Channel;

/* loaded from: classes4.dex */
public abstract class AbstractChannelPoolHandler implements ChannelPoolHandler {
    @Override // io.netty.channel.pool.ChannelPoolHandler
    public void channelAcquired(Channel ch) throws Exception {
    }

    @Override // io.netty.channel.pool.ChannelPoolHandler
    public void channelReleased(Channel ch) throws Exception {
    }
}
