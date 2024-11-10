package io.netty.bootstrap;

import io.netty.channel.Channel;
import io.netty.channel.ServerChannel;

/* loaded from: classes.dex */
public abstract class ChannelInitializerExtension {
    public static final String EXTENSIONS_SYSTEM_PROPERTY = "io.netty.bootstrap.extensions";

    public double priority() {
        return 0.0d;
    }

    public void postInitializeClientChannel(Channel channel) {
    }

    public void postInitializeServerListenerChannel(ServerChannel channel) {
    }

    public void postInitializeServerChildChannel(Channel channel) {
    }
}
