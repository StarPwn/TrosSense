package org.cloudburstmc.protocol.common;

import java.net.InetSocketAddress;
import org.cloudburstmc.protocol.common.MinecraftPacket;

/* loaded from: classes5.dex */
public interface MinecraftSession<T extends MinecraftPacket> {
    void disconnect();

    InetSocketAddress getAddress();

    long getLatency();

    boolean isClosed();

    void sendPacket(T t);

    void sendPacketImmediately(T t);

    default InetSocketAddress getRealAddress() {
        return getAddress();
    }
}
