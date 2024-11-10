package io.netty.channel.socket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.DefaultAddressedEnvelope;
import java.net.InetSocketAddress;

/* loaded from: classes4.dex */
public class DatagramPacket extends DefaultAddressedEnvelope<ByteBuf, InetSocketAddress> implements ByteBufHolder {
    @Override // io.netty.channel.DefaultAddressedEnvelope, io.netty.channel.AddressedEnvelope
    public /* bridge */ /* synthetic */ ByteBuf content() {
        return (ByteBuf) super.content();
    }

    public DatagramPacket(ByteBuf data, InetSocketAddress recipient) {
        super(data, recipient);
    }

    public DatagramPacket(ByteBuf data, InetSocketAddress recipient, InetSocketAddress sender) {
        super(data, recipient, sender);
    }

    @Override // io.netty.buffer.ByteBufHolder
    public DatagramPacket copy() {
        return replace(((ByteBuf) content()).copy());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public DatagramPacket duplicate() {
        return replace(((ByteBuf) content()).duplicate());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public DatagramPacket retainedDuplicate() {
        return replace(((ByteBuf) content()).retainedDuplicate());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public DatagramPacket replace(ByteBuf content) {
        return new DatagramPacket(content, recipient(), sender());
    }

    @Override // io.netty.channel.DefaultAddressedEnvelope, io.netty.util.ReferenceCounted
    public DatagramPacket retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.channel.DefaultAddressedEnvelope, io.netty.util.ReferenceCounted
    public DatagramPacket retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.channel.DefaultAddressedEnvelope, io.netty.util.ReferenceCounted
    public DatagramPacket touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.channel.DefaultAddressedEnvelope, io.netty.util.ReferenceCounted
    public DatagramPacket touch(Object hint) {
        super.touch(hint);
        return this;
    }
}
