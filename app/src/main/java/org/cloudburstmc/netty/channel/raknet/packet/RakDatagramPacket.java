package org.cloudburstmc.netty.channel.raknet.packet;

import io.netty.util.AbstractReferenceCounted;
import io.netty.util.internal.ObjectPool;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes5.dex */
public class RakDatagramPacket extends AbstractReferenceCounted {
    private static final ObjectPool<RakDatagramPacket> RECYCLER = ObjectPool.newPool(new ObjectPool.ObjectCreator() { // from class: org.cloudburstmc.netty.channel.raknet.packet.RakDatagramPacket$$ExternalSyntheticLambda0
        @Override // io.netty.util.internal.ObjectPool.ObjectCreator
        public final Object newObject(ObjectPool.Handle handle) {
            return RakDatagramPacket.m2038$r8$lambda$KFcXUearte2d7muB_TYPEdAUE(handle);
        }
    });
    private final ObjectPool.Handle<RakDatagramPacket> handle;
    private long nextSend;
    private long sendTime;
    private final List<EncapsulatedPacket> packets = new ArrayList();
    private byte flags = -124;
    private int sequenceIndex = -1;

    /* renamed from: $r8$lambda$KF-cXUeart-e2d7muB_TYPEdAUE, reason: not valid java name */
    public static /* synthetic */ RakDatagramPacket m2038$r8$lambda$KFcXUearte2d7muB_TYPEdAUE(ObjectPool.Handle handle) {
        return new RakDatagramPacket(handle);
    }

    public static RakDatagramPacket newInstance() {
        return RECYCLER.get();
    }

    private RakDatagramPacket(ObjectPool.Handle<RakDatagramPacket> handle) {
        this.handle = handle;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public RakDatagramPacket retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public RakDatagramPacket retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public RakDatagramPacket touch(Object hint) {
        for (EncapsulatedPacket packet : this.packets) {
            packet.touch(hint);
        }
        return this;
    }

    public boolean tryAddPacket(EncapsulatedPacket packet, int mtu) {
        if (getSize() + packet.getSize() > mtu - 4) {
            return false;
        }
        this.packets.add(packet);
        if (packet.isSplit()) {
            this.flags = (byte) (this.flags | 8);
            return true;
        }
        return true;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public boolean release() {
        return super.release();
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        for (EncapsulatedPacket packet : this.packets) {
            packet.release();
        }
        this.packets.clear();
        this.flags = (byte) -124;
        this.sendTime = 0L;
        this.nextSend = 0L;
        this.sequenceIndex = -1;
        setRefCnt(1);
        this.handle.recycle(this);
    }

    public int getSize() {
        int size = 4;
        for (EncapsulatedPacket packet : this.packets) {
            size += packet.getSize();
        }
        return size;
    }

    public List<EncapsulatedPacket> getPackets() {
        return this.packets;
    }

    public byte getFlags() {
        return this.flags;
    }

    public void setFlags(byte flags) {
        this.flags = flags;
    }

    public long getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public long getNextSend() {
        return this.nextSend;
    }

    public void setNextSend(long nextSend) {
        this.nextSend = nextSend;
    }

    public int getSequenceIndex() {
        return this.sequenceIndex;
    }

    public void setSequenceIndex(int sequenceIndex) {
        this.sequenceIndex = sequenceIndex;
    }

    public String toString() {
        return "RakDatagramPacket{handle=" + this.handle + ", packets=" + this.packets + ", flags=" + ((int) this.flags) + ", sendTime=" + this.sendTime + ", nextSend=" + this.nextSend + ", sequenceIndex=" + this.sequenceIndex + '}';
    }
}
