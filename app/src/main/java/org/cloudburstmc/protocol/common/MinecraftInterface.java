package org.cloudburstmc.protocol.common;

import java.net.InetSocketAddress;
import java.util.concurrent.Future;

/* loaded from: classes5.dex */
public interface MinecraftInterface {
    Future<Void> bind(InetSocketAddress inetSocketAddress);

    void close();

    InetSocketAddress getBindAddress();
}
