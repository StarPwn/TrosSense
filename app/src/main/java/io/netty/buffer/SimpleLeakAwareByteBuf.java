package io.netty.buffer;

import io.netty.util.ResourceLeakTracker;
import io.netty.util.internal.ObjectUtil;
import java.nio.ByteOrder;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class SimpleLeakAwareByteBuf extends WrappedByteBuf {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    final ResourceLeakTracker<ByteBuf> leak;
    private final ByteBuf trackedByteBuf;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SimpleLeakAwareByteBuf(ByteBuf wrapped, ByteBuf trackedByteBuf, ResourceLeakTracker<ByteBuf> leak) {
        super(wrapped);
        this.trackedByteBuf = (ByteBuf) ObjectUtil.checkNotNull(trackedByteBuf, "trackedByteBuf");
        this.leak = (ResourceLeakTracker) ObjectUtil.checkNotNull(leak, "leak");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SimpleLeakAwareByteBuf(ByteBuf wrapped, ResourceLeakTracker<ByteBuf> leak) {
        this(wrapped, wrapped, leak);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf slice() {
        return newSharedLeakAwareByteBuf(super.slice());
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf retainedSlice() {
        return unwrappedDerived(super.retainedSlice());
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf retainedSlice(int index, int length) {
        return unwrappedDerived(super.retainedSlice(index, length));
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf retainedDuplicate() {
        return unwrappedDerived(super.retainedDuplicate());
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readRetainedSlice(int length) {
        return unwrappedDerived(super.readRetainedSlice(length));
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf slice(int index, int length) {
        return newSharedLeakAwareByteBuf(super.slice(index, length));
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf duplicate() {
        return newSharedLeakAwareByteBuf(super.duplicate());
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readSlice(int length) {
        return newSharedLeakAwareByteBuf(super.readSlice(length));
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf asReadOnly() {
        return newSharedLeakAwareByteBuf(super.asReadOnly());
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf touch() {
        return this;
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf touch(Object hint) {
        return this;
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.util.ReferenceCounted
    public boolean release() {
        if (super.release()) {
            closeLeak();
            return true;
        }
        return false;
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        if (super.release(decrement)) {
            closeLeak();
            return true;
        }
        return false;
    }

    private void closeLeak() {
        boolean closed = this.leak.close(this.trackedByteBuf);
        if (!closed) {
            throw new AssertionError();
        }
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf order(ByteOrder endianness) {
        if (order() == endianness) {
            return this;
        }
        return newSharedLeakAwareByteBuf(super.order(endianness));
    }

    private ByteBuf unwrappedDerived(ByteBuf derived) {
        ByteBuf unwrappedDerived = unwrapSwapped(derived);
        if (unwrappedDerived instanceof AbstractPooledDerivedByteBuf) {
            ((AbstractPooledDerivedByteBuf) unwrappedDerived).parent(this);
            return newLeakAwareByteBuf(derived, AbstractByteBuf.leakDetector.trackForcibly(derived));
        }
        return newSharedLeakAwareByteBuf(derived);
    }

    private static ByteBuf unwrapSwapped(ByteBuf buf) {
        if (!(buf instanceof SwappedByteBuf)) {
            return buf;
        }
        do {
            buf = buf.unwrap();
        } while (buf instanceof SwappedByteBuf);
        return buf;
    }

    private SimpleLeakAwareByteBuf newSharedLeakAwareByteBuf(ByteBuf wrapped) {
        return newLeakAwareByteBuf(wrapped, this.trackedByteBuf, this.leak);
    }

    private SimpleLeakAwareByteBuf newLeakAwareByteBuf(ByteBuf wrapped, ResourceLeakTracker<ByteBuf> leakTracker) {
        return newLeakAwareByteBuf(wrapped, wrapped, leakTracker);
    }

    protected SimpleLeakAwareByteBuf newLeakAwareByteBuf(ByteBuf buf, ByteBuf trackedByteBuf, ResourceLeakTracker<ByteBuf> leakTracker) {
        return new SimpleLeakAwareByteBuf(buf, trackedByteBuf, leakTracker);
    }
}
