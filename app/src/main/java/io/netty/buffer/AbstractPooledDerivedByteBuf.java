package io.netty.buffer;

import io.netty.util.Recycler;
import io.netty.util.internal.ObjectPool;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public abstract class AbstractPooledDerivedByteBuf extends AbstractReferenceCountedByteBuf {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private ByteBuf parent;
    private final Recycler.EnhancedHandle<AbstractPooledDerivedByteBuf> recyclerHandle;
    private AbstractByteBuf rootParent;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractPooledDerivedByteBuf(ObjectPool.Handle<? extends AbstractPooledDerivedByteBuf> recyclerHandle) {
        super(0);
        this.recyclerHandle = (Recycler.EnhancedHandle) recyclerHandle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void parent(ByteBuf newParent) {
        if (!(newParent instanceof SimpleLeakAwareByteBuf)) {
            throw new AssertionError();
        }
        this.parent = newParent;
    }

    @Override // io.netty.buffer.ByteBuf
    public final AbstractByteBuf unwrap() {
        return this.rootParent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public final <U extends AbstractPooledDerivedByteBuf> U init(AbstractByteBuf unwrapped, ByteBuf wrapped, int readerIndex, int writerIndex, int maxCapacity) {
        wrapped.retain();
        this.parent = wrapped;
        this.rootParent = unwrapped;
        try {
            maxCapacity(maxCapacity);
            setIndex0(readerIndex, writerIndex);
            resetRefCnt();
            wrapped = null;
            return this;
        } finally {
            if (wrapped != null) {
                this.rootParent = null;
                this.parent = null;
                wrapped.release();
            }
        }
    }

    @Override // io.netty.buffer.AbstractReferenceCountedByteBuf
    protected final void deallocate() {
        ByteBuf parent = this.parent;
        this.recyclerHandle.unguardedRecycle(this);
        parent.release();
    }

    @Override // io.netty.buffer.ByteBuf
    public final ByteBufAllocator alloc() {
        return unwrap().alloc();
    }

    @Override // io.netty.buffer.ByteBuf
    @Deprecated
    public final ByteOrder order() {
        return unwrap().order();
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public boolean isReadOnly() {
        return unwrap().isReadOnly();
    }

    @Override // io.netty.buffer.ByteBuf
    public final boolean isDirect() {
        return unwrap().isDirect();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean hasArray() {
        return unwrap().hasArray();
    }

    @Override // io.netty.buffer.ByteBuf
    public byte[] array() {
        return unwrap().array();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean hasMemoryAddress() {
        return unwrap().hasMemoryAddress();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isContiguous() {
        return unwrap().isContiguous();
    }

    @Override // io.netty.buffer.ByteBuf
    public final int nioBufferCount() {
        return unwrap().nioBufferCount();
    }

    @Override // io.netty.buffer.ByteBuf
    public final ByteBuffer internalNioBuffer(int index, int length) {
        return nioBuffer(index, length);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public final ByteBuf retainedSlice() {
        int index = readerIndex();
        return retainedSlice(index, writerIndex() - index);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf slice(int index, int length) {
        ensureAccessible();
        return new PooledNonRetainedSlicedByteBuf(this, unwrap(), index, length);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final ByteBuf duplicate0() {
        ensureAccessible();
        return new PooledNonRetainedDuplicateByteBuf(this, unwrap());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class PooledNonRetainedDuplicateByteBuf extends UnpooledDuplicatedByteBuf {
        private final ByteBuf referenceCountDelegate;

        PooledNonRetainedDuplicateByteBuf(ByteBuf referenceCountDelegate, AbstractByteBuf buffer) {
            super(buffer);
            this.referenceCountDelegate = referenceCountDelegate;
        }

        @Override // io.netty.buffer.AbstractDerivedByteBuf
        boolean isAccessible0() {
            return this.referenceCountDelegate.isAccessible();
        }

        @Override // io.netty.buffer.AbstractDerivedByteBuf
        int refCnt0() {
            return this.referenceCountDelegate.refCnt();
        }

        @Override // io.netty.buffer.AbstractDerivedByteBuf
        ByteBuf retain0() {
            this.referenceCountDelegate.retain();
            return this;
        }

        @Override // io.netty.buffer.AbstractDerivedByteBuf
        ByteBuf retain0(int increment) {
            this.referenceCountDelegate.retain(increment);
            return this;
        }

        @Override // io.netty.buffer.AbstractDerivedByteBuf
        ByteBuf touch0() {
            this.referenceCountDelegate.touch();
            return this;
        }

        @Override // io.netty.buffer.AbstractDerivedByteBuf
        ByteBuf touch0(Object hint) {
            this.referenceCountDelegate.touch(hint);
            return this;
        }

        @Override // io.netty.buffer.AbstractDerivedByteBuf
        boolean release0() {
            return this.referenceCountDelegate.release();
        }

        @Override // io.netty.buffer.AbstractDerivedByteBuf
        boolean release0(int decrement) {
            return this.referenceCountDelegate.release(decrement);
        }

        @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
        public ByteBuf duplicate() {
            ensureAccessible();
            return new PooledNonRetainedDuplicateByteBuf(this.referenceCountDelegate, this);
        }

        @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
        public ByteBuf retainedDuplicate() {
            return PooledDuplicatedByteBuf.newInstance(unwrap(), this, readerIndex(), writerIndex());
        }

        @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
        public ByteBuf slice(int index, int length) {
            checkIndex(index, length);
            return new PooledNonRetainedSlicedByteBuf(this.referenceCountDelegate, unwrap(), index, length);
        }

        @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
        public ByteBuf retainedSlice() {
            return retainedSlice(readerIndex(), capacity());
        }

        @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
        public ByteBuf retainedSlice(int index, int length) {
            return PooledSlicedByteBuf.newInstance(unwrap(), this, index, length);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class PooledNonRetainedSlicedByteBuf extends UnpooledSlicedByteBuf {
        private final ByteBuf referenceCountDelegate;

        PooledNonRetainedSlicedByteBuf(ByteBuf referenceCountDelegate, AbstractByteBuf buffer, int index, int length) {
            super(buffer, index, length);
            this.referenceCountDelegate = referenceCountDelegate;
        }

        @Override // io.netty.buffer.AbstractDerivedByteBuf
        boolean isAccessible0() {
            return this.referenceCountDelegate.isAccessible();
        }

        @Override // io.netty.buffer.AbstractDerivedByteBuf
        int refCnt0() {
            return this.referenceCountDelegate.refCnt();
        }

        @Override // io.netty.buffer.AbstractDerivedByteBuf
        ByteBuf retain0() {
            this.referenceCountDelegate.retain();
            return this;
        }

        @Override // io.netty.buffer.AbstractDerivedByteBuf
        ByteBuf retain0(int increment) {
            this.referenceCountDelegate.retain(increment);
            return this;
        }

        @Override // io.netty.buffer.AbstractDerivedByteBuf
        ByteBuf touch0() {
            this.referenceCountDelegate.touch();
            return this;
        }

        @Override // io.netty.buffer.AbstractDerivedByteBuf
        ByteBuf touch0(Object hint) {
            this.referenceCountDelegate.touch(hint);
            return this;
        }

        @Override // io.netty.buffer.AbstractDerivedByteBuf
        boolean release0() {
            return this.referenceCountDelegate.release();
        }

        @Override // io.netty.buffer.AbstractDerivedByteBuf
        boolean release0(int decrement) {
            return this.referenceCountDelegate.release(decrement);
        }

        @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
        public ByteBuf duplicate() {
            ensureAccessible();
            return new PooledNonRetainedDuplicateByteBuf(this.referenceCountDelegate, unwrap()).setIndex(idx(readerIndex()), idx(writerIndex()));
        }

        @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
        public ByteBuf retainedDuplicate() {
            return PooledDuplicatedByteBuf.newInstance(unwrap(), this, idx(readerIndex()), idx(writerIndex()));
        }

        @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
        public ByteBuf slice(int index, int length) {
            checkIndex(index, length);
            return new PooledNonRetainedSlicedByteBuf(this.referenceCountDelegate, unwrap(), idx(index), length);
        }

        @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
        public ByteBuf retainedSlice() {
            return retainedSlice(0, capacity());
        }

        @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
        public ByteBuf retainedSlice(int index, int length) {
            return PooledSlicedByteBuf.newInstance(unwrap(), this, idx(index), length);
        }
    }
}
