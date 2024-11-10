package io.netty.channel.unix;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.DefaultAddressedEnvelope;

/* loaded from: classes4.dex */
public final class DomainDatagramPacket extends DefaultAddressedEnvelope<ByteBuf, DomainSocketAddress> implements ByteBufHolder {
    @Override // io.netty.channel.DefaultAddressedEnvelope, io.netty.channel.AddressedEnvelope
    public /* bridge */ /* synthetic */ ByteBuf content() {
        return (ByteBuf) super.content();
    }

    public DomainDatagramPacket(ByteBuf data, DomainSocketAddress recipient) {
        super(data, recipient);
    }

    public DomainDatagramPacket(ByteBuf data, DomainSocketAddress recipient, DomainSocketAddress sender) {
        super(data, recipient, sender);
    }

    @Override // io.netty.buffer.ByteBufHolder
    public DomainDatagramPacket copy() {
        return replace(((ByteBuf) content()).copy());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public DomainDatagramPacket duplicate() {
        return replace(((ByteBuf) content()).duplicate());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public DomainDatagramPacket replace(ByteBuf content) {
        return new DomainDatagramPacket(content, recipient(), sender());
    }

    @Override // io.netty.channel.DefaultAddressedEnvelope, io.netty.util.ReferenceCounted
    public DomainDatagramPacket retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.channel.DefaultAddressedEnvelope, io.netty.util.ReferenceCounted
    public DomainDatagramPacket retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.buffer.ByteBufHolder
    public DomainDatagramPacket retainedDuplicate() {
        return replace(((ByteBuf) content()).retainedDuplicate());
    }

    @Override // io.netty.channel.DefaultAddressedEnvelope, io.netty.util.ReferenceCounted
    public DomainDatagramPacket touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.channel.DefaultAddressedEnvelope, io.netty.util.ReferenceCounted
    public DomainDatagramPacket touch(Object hint) {
        super.touch(hint);
        return this;
    }
}
