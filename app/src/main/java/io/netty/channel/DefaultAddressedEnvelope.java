package io.netty.channel;

import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.net.SocketAddress;

/* loaded from: classes4.dex */
public class DefaultAddressedEnvelope<M, A extends SocketAddress> implements AddressedEnvelope<M, A> {
    private final M message;
    private final A recipient;
    private final A sender;

    public DefaultAddressedEnvelope(M message, A recipient, A sender) {
        ObjectUtil.checkNotNull(message, "message");
        if (recipient == null && sender == null) {
            throw new NullPointerException("recipient and sender");
        }
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
    }

    public DefaultAddressedEnvelope(M message, A recipient) {
        this(message, recipient, null);
    }

    @Override // io.netty.channel.AddressedEnvelope
    public M content() {
        return this.message;
    }

    @Override // io.netty.channel.AddressedEnvelope
    public A sender() {
        return this.sender;
    }

    @Override // io.netty.channel.AddressedEnvelope
    public A recipient() {
        return this.recipient;
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        if (this.message instanceof ReferenceCounted) {
            return ((ReferenceCounted) this.message).refCnt();
        }
        return 1;
    }

    @Override // io.netty.util.ReferenceCounted
    public AddressedEnvelope<M, A> retain() {
        ReferenceCountUtil.retain(this.message);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public AddressedEnvelope<M, A> retain(int increment) {
        ReferenceCountUtil.retain(this.message, increment);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return ReferenceCountUtil.release(this.message);
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return ReferenceCountUtil.release(this.message, decrement);
    }

    @Override // io.netty.util.ReferenceCounted
    public AddressedEnvelope<M, A> touch() {
        ReferenceCountUtil.touch(this.message);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public AddressedEnvelope<M, A> touch(Object hint) {
        ReferenceCountUtil.touch(this.message, hint);
        return this;
    }

    public String toString() {
        if (this.sender != null) {
            return StringUtil.simpleClassName(this) + '(' + this.sender + " => " + this.recipient + ", " + this.message + ')';
        }
        return StringUtil.simpleClassName(this) + "(=> " + this.recipient + ", " + this.message + ')';
    }
}
