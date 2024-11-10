package org.cloudburstmc.protocol.common;

/* loaded from: classes5.dex */
public interface PlayerSession {
    void close();

    boolean isClosed();

    void onDisconnect(String str);
}
