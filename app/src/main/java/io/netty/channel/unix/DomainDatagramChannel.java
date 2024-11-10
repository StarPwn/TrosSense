package io.netty.channel.unix;

import io.netty.channel.Channel;

/* loaded from: classes4.dex */
public interface DomainDatagramChannel extends UnixChannel, Channel {
    @Override // io.netty.channel.Channel
    DomainDatagramChannelConfig config();

    boolean isConnected();

    @Override // io.netty.channel.Channel
    DomainSocketAddress localAddress();

    @Override // io.netty.channel.Channel
    DomainSocketAddress remoteAddress();
}
