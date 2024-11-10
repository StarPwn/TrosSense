package org.cloudburstmc.protocol.common;

import org.cloudburstmc.protocol.common.MinecraftPacket;

/* loaded from: classes5.dex */
public interface MinecraftServerSession<T extends MinecraftPacket> extends MinecraftSession<T> {
    void disconnect(String str);
}
