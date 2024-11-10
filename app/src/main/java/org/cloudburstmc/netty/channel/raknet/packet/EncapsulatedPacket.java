package org.cloudburstmc.netty.channel.raknet.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.internal.ObjectPool;
import org.cloudburstmc.netty.channel.raknet.RakReliability;

/* loaded from: classes5.dex */
public class EncapsulatedPacket extends AbstractReferenceCounted {
    private static final ObjectPool<EncapsulatedPacket> RECYCLER = ObjectPool.newPool(new ObjectPool.ObjectCreator() { // from class: org.cloudburstmc.netty.channel.raknet.packet.EncapsulatedPacket$$ExternalSyntheticLambda0
        @Override // io.netty.util.internal.ObjectPool.ObjectCreator
        public final Object newObject(ObjectPool.Handle handle) {
            return EncapsulatedPacket.m2037$r8$lambda$Roa9tqpAfW8D2u5UvYl1aA_UE(handle);
        }
    });
    private ByteBuf buffer;
    private final ObjectPool.Handle<EncapsulatedPacket> handle;
    private boolean needsBAS;
    private short orderingChannel;
    private int orderingIndex;
    private int partCount;
    private int partId;
    private int partIndex;
    private RakReliability reliability;
    private int reliabilityIndex;
    private int sequenceIndex;
    private boolean split;

    /* renamed from: $r8$lambda$Roa9tqp-A-fW8D2u5UvYl1aA_UE, reason: not valid java name */
    public static /* synthetic */ EncapsulatedPacket m2037$r8$lambda$Roa9tqpAfW8D2u5UvYl1aA_UE(ObjectPool.Handle handle) {
        return new EncapsulatedPacket(handle);
    }

    public static EncapsulatedPacket newInstance() {
        return RECYCLER.get();
    }

    private EncapsulatedPacket(ObjectPool.Handle<EncapsulatedPacket> handle) {
        this.handle = handle;
    }

    public void encode(CompositeByteBuf buffer) {
        RakReliability reliability = this.reliability;
        ByteBuf header = buffer.alloc().ioBuffer(reliability.getSize() + 3 + (this.split ? 10 : 0));
        int flags = this.reliability.ordinal() << 5;
        if (this.split) {
            flags |= 16;
        }
        if (this.needsBAS) {
            flags |= 4;
        }
        header.writeByte(flags);
        header.writeShort(this.buffer.readableBytes() << 3);
        if (reliability.isReliable()) {
            header.writeMediumLE(this.reliabilityIndex);
        }
        if (reliability.isSequenced()) {
            header.writeMediumLE(this.sequenceIndex);
        }
        if (reliability.isOrdered() || reliability.isSequenced()) {
            header.writeMediumLE(this.orderingIndex);
            header.writeByte(this.orderingChannel);
        }
        if (this.split) {
            header.writeInt(this.partCount);
            header.writeShort(this.partId);
            header.writeInt(this.partIndex);
        }
        buffer.addComponent(true, header);
        buffer.addComponent(true, this.buffer.retain());
    }

    public void decode(ByteBuf buf) {
        byte flags = buf.readByte();
        this.reliability = RakReliability.fromId(flags >>> 5);
        this.split = (flags & 16) != 0;
        this.needsBAS = (flags & 4) != 0;
        int size = (buf.readUnsignedShort() + 7) >> 3;
        if (this.reliability.isReliable()) {
            this.reliabilityIndex = buf.readUnsignedMediumLE();
        }
        if (this.reliability.isSequenced()) {
            this.sequenceIndex = buf.readUnsignedMediumLE();
        }
        if (this.reliability.isOrdered() || this.reliability.isSequenced()) {
            this.orderingIndex = buf.readUnsignedMediumLE();
            this.orderingChannel = buf.readUnsignedByte();
        }
        if (this.split) {
            this.partCount = buf.readInt();
            this.partId = buf.readUnsignedShort();
            this.partIndex = buf.readInt();
        }
        this.buffer = buf.readRetainedSlice(size);
    }

    public int getSize() {
        return this.reliability.getSize() + 3 + (this.split ? 10 : 0) + this.buffer.readableBytes();
    }

    public EncapsulatedPacket fromSplit(ByteBuf reassembled) {
        EncapsulatedPacket packet = newInstance();
        packet.reliability = this.reliability;
        packet.reliabilityIndex = this.reliabilityIndex;
        packet.sequenceIndex = this.sequenceIndex;
        packet.orderingIndex = this.orderingIndex;
        packet.orderingChannel = this.orderingChannel;
        packet.buffer = reassembled;
        return packet;
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        this.buffer.release();
        this.reliability = null;
        this.reliabilityIndex = 0;
        this.sequenceIndex = 0;
        this.orderingIndex = 0;
        this.orderingChannel = (short) 0;
        this.split = false;
        this.partCount = 0;
        this.partId = 0;
        this.partIndex = 0;
        this.buffer = null;
        setRefCnt(1);
        this.handle.recycle(this);
    }

    @Override // io.netty.util.ReferenceCounted
    public EncapsulatedPacket touch(Object o) {
        this.buffer.touch();
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public EncapsulatedPacket retain() {
        return (EncapsulatedPacket) super.retain();
    }

    public RakReliability getReliability() {
        return this.reliability;
    }

    public void setReliability(RakReliability reliability) {
        this.reliability = reliability;
    }

    public int getReliabilityIndex() {
        return this.reliabilityIndex;
    }

    public void setReliabilityIndex(int reliabilityIndex) {
        this.reliabilityIndex = reliabilityIndex;
    }

    public int getSequenceIndex() {
        return this.sequenceIndex;
    }

    public void setSequenceIndex(int sequenceIndex) {
        this.sequenceIndex = sequenceIndex;
    }

    public int getOrderingIndex() {
        return this.orderingIndex;
    }

    public void setOrderingIndex(int orderingIndex) {
        this.orderingIndex = orderingIndex;
    }

    public short getOrderingChannel() {
        return this.orderingChannel;
    }

    public void setOrderingChannel(short orderingChannel) {
        this.orderingChannel = orderingChannel;
    }

    public boolean isSplit() {
        return this.split;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }

    public int getPartCount() {
        return this.partCount;
    }

    public void setPartCount(int partCount) {
        this.partCount = partCount;
    }

    public int getPartId() {
        return this.partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public int getPartIndex() {
        return this.partIndex;
    }

    public void setPartIndex(int partIndex) {
        this.partIndex = partIndex;
    }

    public ByteBuf getBuffer() {
        return this.buffer;
    }

    public void setBuffer(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public boolean isNeedsBAS() {
        return this.needsBAS;
    }

    public void setNeedsBAS(boolean needsBAS) {
        this.needsBAS = needsBAS;
    }

    public RakMessage toMessage() {
        return new RakMessage(this.buffer, this.reliability);
    }

    public String toString() {
        return "EncapsulatedPacket{handle=" + this.handle + ", reliability=" + this.reliability + ", reliabilityIndex=" + this.reliabilityIndex + ", sequenceIndex=" + this.sequenceIndex + ", orderingIndex=" + this.orderingIndex + ", orderingChannel=" + ((int) this.orderingChannel) + ", split=" + this.split + ", partCount=" + this.partCount + ", partId=" + this.partId + ", partIndex=" + this.partIndex + ", buffer=" + ByteBufUtil.hexDump(this.buffer) + '}';
    }
}
