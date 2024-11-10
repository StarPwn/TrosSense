package io.netty.channel.unix;

import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.ObjectUtil;
import java.net.InetSocketAddress;

/* loaded from: classes4.dex */
public class SegmentedDatagramPacket extends DatagramPacket {
    private final int segmentSize;

    public SegmentedDatagramPacket(ByteBuf data, int segmentSize, InetSocketAddress recipient) {
        super(data, recipient);
        this.segmentSize = ObjectUtil.checkPositive(segmentSize, "segmentSize");
    }

    public SegmentedDatagramPacket(ByteBuf data, int segmentSize, InetSocketAddress recipient, InetSocketAddress sender) {
        super(data, recipient, sender);
        this.segmentSize = ObjectUtil.checkPositive(segmentSize, "segmentSize");
    }

    public int segmentSize() {
        return this.segmentSize;
    }

    @Override // io.netty.channel.socket.DatagramPacket, io.netty.buffer.ByteBufHolder
    public SegmentedDatagramPacket copy() {
        return new SegmentedDatagramPacket(((ByteBuf) content()).copy(), this.segmentSize, recipient(), sender());
    }

    @Override // io.netty.channel.socket.DatagramPacket, io.netty.buffer.ByteBufHolder
    public SegmentedDatagramPacket duplicate() {
        return new SegmentedDatagramPacket(((ByteBuf) content()).duplicate(), this.segmentSize, recipient(), sender());
    }

    @Override // io.netty.channel.socket.DatagramPacket, io.netty.buffer.ByteBufHolder
    public SegmentedDatagramPacket retainedDuplicate() {
        return new SegmentedDatagramPacket(((ByteBuf) content()).retainedDuplicate(), this.segmentSize, recipient(), sender());
    }

    @Override // io.netty.channel.socket.DatagramPacket, io.netty.buffer.ByteBufHolder
    public SegmentedDatagramPacket replace(ByteBuf content) {
        return new SegmentedDatagramPacket(content, this.segmentSize, recipient(), sender());
    }

    @Override // io.netty.channel.socket.DatagramPacket, io.netty.channel.DefaultAddressedEnvelope, io.netty.util.ReferenceCounted
    public SegmentedDatagramPacket retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.channel.socket.DatagramPacket, io.netty.channel.DefaultAddressedEnvelope, io.netty.util.ReferenceCounted
    public SegmentedDatagramPacket retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.channel.socket.DatagramPacket, io.netty.channel.DefaultAddressedEnvelope, io.netty.util.ReferenceCounted
    public SegmentedDatagramPacket touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.channel.socket.DatagramPacket, io.netty.channel.DefaultAddressedEnvelope, io.netty.util.ReferenceCounted
    public SegmentedDatagramPacket touch(Object hint) {
        super.touch(hint);
        return this;
    }
}
