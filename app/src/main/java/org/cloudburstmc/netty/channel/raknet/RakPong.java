package org.cloudburstmc.netty.channel.raknet;

import io.netty.buffer.ByteBuf;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCountUtil;
import java.net.InetSocketAddress;

/* loaded from: classes5.dex */
public class RakPong extends AbstractReferenceCounted {
    private final long guid;
    private final long pingTime;
    private final ByteBuf pongData;
    private final InetSocketAddress sender;

    public RakPong(long pingTime, long guid, ByteBuf pongData, InetSocketAddress sender) {
        this.pingTime = pingTime;
        this.guid = guid;
        this.pongData = pongData;
        this.sender = sender;
    }

    public long getPingTime() {
        return this.pingTime;
    }

    public long getGuid() {
        return this.guid;
    }

    public ByteBuf getPongData() {
        return this.pongData;
    }

    public InetSocketAddress getSender() {
        return this.sender;
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        ReferenceCountUtil.release(this.pongData);
    }

    @Override // io.netty.util.ReferenceCounted
    public RakPong touch(Object hint) {
        if (this.pongData != null) {
            this.pongData.touch(hint);
        }
        return this;
    }
}
