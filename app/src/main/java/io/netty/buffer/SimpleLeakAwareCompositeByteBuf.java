package io.netty.buffer;

import io.netty.util.ResourceLeakTracker;
import io.netty.util.internal.ObjectUtil;
import java.nio.ByteOrder;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class SimpleLeakAwareCompositeByteBuf extends WrappedCompositeByteBuf {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    final ResourceLeakTracker<ByteBuf> leak;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SimpleLeakAwareCompositeByteBuf(CompositeByteBuf wrapped, ResourceLeakTracker<ByteBuf> leak) {
        super(wrapped);
        this.leak = (ResourceLeakTracker) ObjectUtil.checkNotNull(leak, "leak");
    }

    @Override // io.netty.buffer.WrappedCompositeByteBuf, io.netty.buffer.AbstractReferenceCountedByteBuf, io.netty.util.ReferenceCounted
    public boolean release() {
        ByteBuf unwrapped = unwrap();
        if (super.release()) {
            closeLeak(unwrapped);
            return true;
        }
        return false;
    }

    @Override // io.netty.buffer.WrappedCompositeByteBuf, io.netty.buffer.AbstractReferenceCountedByteBuf, io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        ByteBuf unwrapped = unwrap();
        if (super.release(decrement)) {
            closeLeak(unwrapped);
            return true;
        }
        return false;
    }

    private void closeLeak(ByteBuf trackedByteBuf) {
        boolean closed = this.leak.close(trackedByteBuf);
        if (!closed) {
            throw new AssertionError();
        }
    }

    @Override // io.netty.buffer.WrappedCompositeByteBuf, io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf order(ByteOrder endianness) {
        if (order() == endianness) {
            return this;
        }
        return newLeakAwareByteBuf(super.order(endianness));
    }

    @Override // io.netty.buffer.WrappedCompositeByteBuf, io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf slice() {
        return newLeakAwareByteBuf(super.slice());
    }

    @Override // io.netty.buffer.WrappedCompositeByteBuf, io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf retainedSlice() {
        return newLeakAwareByteBuf(super.retainedSlice());
    }

    @Override // io.netty.buffer.WrappedCompositeByteBuf, io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf slice(int index, int length) {
        return newLeakAwareByteBuf(super.slice(index, length));
    }

    @Override // io.netty.buffer.WrappedCompositeByteBuf, io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf retainedSlice(int index, int length) {
        return newLeakAwareByteBuf(super.retainedSlice(index, length));
    }

    @Override // io.netty.buffer.WrappedCompositeByteBuf, io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf duplicate() {
        return newLeakAwareByteBuf(super.duplicate());
    }

    @Override // io.netty.buffer.WrappedCompositeByteBuf, io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf retainedDuplicate() {
        return newLeakAwareByteBuf(super.retainedDuplicate());
    }

    @Override // io.netty.buffer.WrappedCompositeByteBuf, io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readSlice(int length) {
        return newLeakAwareByteBuf(super.readSlice(length));
    }

    @Override // io.netty.buffer.WrappedCompositeByteBuf, io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readRetainedSlice(int length) {
        return newLeakAwareByteBuf(super.readRetainedSlice(length));
    }

    @Override // io.netty.buffer.WrappedCompositeByteBuf, io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf asReadOnly() {
        return newLeakAwareByteBuf(super.asReadOnly());
    }

    private SimpleLeakAwareByteBuf newLeakAwareByteBuf(ByteBuf wrapped) {
        return newLeakAwareByteBuf(wrapped, unwrap(), this.leak);
    }

    protected SimpleLeakAwareByteBuf newLeakAwareByteBuf(ByteBuf wrapped, ByteBuf trackedByteBuf, ResourceLeakTracker<ByteBuf> leakTracker) {
        return new SimpleLeakAwareByteBuf(wrapped, trackedByteBuf, leakTracker);
    }
}
