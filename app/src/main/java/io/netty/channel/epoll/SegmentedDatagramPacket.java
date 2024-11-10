package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import java.net.InetSocketAddress;

@Deprecated
/* loaded from: classes4.dex */
public final class SegmentedDatagramPacket extends io.netty.channel.unix.SegmentedDatagramPacket {
    public SegmentedDatagramPacket(ByteBuf data, int segmentSize, InetSocketAddress recipient) {
        super(data, segmentSize, recipient);
        checkIsSupported();
    }

    public SegmentedDatagramPacket(ByteBuf data, int segmentSize, InetSocketAddress recipient, InetSocketAddress sender) {
        super(data, segmentSize, recipient, sender);
        checkIsSupported();
    }

    public static boolean isSupported() {
        return Epoll.isAvailable() && Native.IS_SUPPORTING_SENDMMSG && Native.IS_SUPPORTING_UDP_SEGMENT;
    }

    @Override // io.netty.channel.unix.SegmentedDatagramPacket, io.netty.channel.socket.DatagramPacket, io.netty.buffer.ByteBufHolder
    public SegmentedDatagramPacket copy() {
        return new SegmentedDatagramPacket(((ByteBuf) content()).copy(), segmentSize(), recipient(), sender());
    }

    @Override // io.netty.channel.unix.SegmentedDatagramPacket, io.netty.channel.socket.DatagramPacket, io.netty.buffer.ByteBufHolder
    public SegmentedDatagramPacket duplicate() {
        return new SegmentedDatagramPacket(((ByteBuf) content()).duplicate(), segmentSize(), recipient(), sender());
    }

    @Override // io.netty.channel.unix.SegmentedDatagramPacket, io.netty.channel.socket.DatagramPacket, io.netty.buffer.ByteBufHolder
    public SegmentedDatagramPacket retainedDuplicate() {
        return new SegmentedDatagramPacket(((ByteBuf) content()).retainedDuplicate(), segmentSize(), recipient(), sender());
    }

    @Override // io.netty.channel.unix.SegmentedDatagramPacket, io.netty.channel.socket.DatagramPacket, io.netty.buffer.ByteBufHolder
    public SegmentedDatagramPacket replace(ByteBuf content) {
        return new SegmentedDatagramPacket(content, segmentSize(), recipient(), sender());
    }

    @Override // io.netty.channel.unix.SegmentedDatagramPacket, io.netty.channel.socket.DatagramPacket, io.netty.channel.DefaultAddressedEnvelope, io.netty.util.ReferenceCounted
    public SegmentedDatagramPacket retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.channel.unix.SegmentedDatagramPacket, io.netty.channel.socket.DatagramPacket, io.netty.channel.DefaultAddressedEnvelope, io.netty.util.ReferenceCounted
    public SegmentedDatagramPacket retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.channel.unix.SegmentedDatagramPacket, io.netty.channel.socket.DatagramPacket, io.netty.channel.DefaultAddressedEnvelope, io.netty.util.ReferenceCounted
    public SegmentedDatagramPacket touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.channel.unix.SegmentedDatagramPacket, io.netty.channel.socket.DatagramPacket, io.netty.channel.DefaultAddressedEnvelope, io.netty.util.ReferenceCounted
    public SegmentedDatagramPacket touch(Object hint) {
        super.touch(hint);
        return this;
    }

    private static void checkIsSupported() {
        if (!isSupported()) {
            throw new IllegalStateException();
        }
    }
}
