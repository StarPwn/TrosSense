package org.cloudburstmc.netty.channel.raknet;

import io.netty.buffer.ByteBuf;
import java.net.InetSocketAddress;

/* loaded from: classes5.dex */
public class RakPing {
    private final long pingTime;
    private final InetSocketAddress sender;

    public RakPing(long pingTime, InetSocketAddress sender) {
        this.pingTime = pingTime;
        this.sender = sender;
    }

    public long getPingTime() {
        return this.pingTime;
    }

    public InetSocketAddress getSender() {
        return this.sender;
    }

    public RakPong reply(long guid, ByteBuf pongData) {
        return new RakPong(this.pingTime, guid, pongData, this.sender);
    }
}
