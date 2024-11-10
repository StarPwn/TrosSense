package org.cloudburstmc.netty.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import java.util.Objects;
import org.cloudburstmc.netty.channel.raknet.packet.EncapsulatedPacket;

/* loaded from: classes5.dex */
public class SplitPacketHelper extends AbstractReferenceCounted {
    private final long created = System.currentTimeMillis();
    private final EncapsulatedPacket[] packets;

    public SplitPacketHelper(long expectedLength) {
        if (expectedLength < 2) {
            throw new IllegalArgumentException("expectedLength must be greater than 1");
        }
        this.packets = new EncapsulatedPacket[(int) expectedLength];
    }

    public EncapsulatedPacket add(EncapsulatedPacket packet, ByteBufAllocator alloc) {
        Objects.requireNonNull(packet, "packet cannot be null");
        if (!packet.isSplit()) {
            throw new IllegalArgumentException("Packet is not split");
        }
        if (refCnt() <= 0) {
            throw new IllegalReferenceCountException(refCnt());
        }
        if (packet.getPartIndex() < 0 || packet.getPartIndex() >= this.packets.length) {
            throw new IllegalArgumentException(String.format("Split packet part index out of range. Got %s, expected 0-%s", Integer.valueOf(packet.getPartIndex()), Integer.valueOf(this.packets.length - 1)));
        }
        int partIndex = packet.getPartIndex();
        if (this.packets[partIndex] != null) {
            return null;
        }
        this.packets[partIndex] = packet.retain();
        int sz = 0;
        for (EncapsulatedPacket netPacket : this.packets) {
            if (netPacket == null) {
                return null;
            }
            sz += netPacket.getBuffer().readableBytes();
        }
        ByteBuf reassembled = alloc.ioBuffer(sz);
        for (EncapsulatedPacket encapsulatedPacket : this.packets) {
            ByteBuf buf = encapsulatedPacket.getBuffer();
            reassembled.writeBytes(buf, buf.readerIndex(), buf.readableBytes());
        }
        return packet.fromSplit(reassembled);
    }

    public boolean expired() {
        if (refCnt() > 0) {
            return System.currentTimeMillis() - this.created >= 30000;
        }
        throw new IllegalReferenceCountException(refCnt());
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        for (EncapsulatedPacket packet : this.packets) {
            ReferenceCountUtil.release(packet);
        }
    }

    @Override // io.netty.util.ReferenceCounted
    public ReferenceCounted touch(Object hint) {
        throw new UnsupportedOperationException();
    }
}
