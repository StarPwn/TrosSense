package io.netty.util;

import io.netty.util.internal.ReferenceCountUpdater;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/* loaded from: classes4.dex */
public abstract class AbstractReferenceCounted implements ReferenceCounted {
    private volatile int refCnt = updater.initialValue();
    private static final long REFCNT_FIELD_OFFSET = ReferenceCountUpdater.getUnsafeOffset(AbstractReferenceCounted.class, "refCnt");
    private static final AtomicIntegerFieldUpdater<AbstractReferenceCounted> AIF_UPDATER = AtomicIntegerFieldUpdater.newUpdater(AbstractReferenceCounted.class, "refCnt");
    private static final ReferenceCountUpdater<AbstractReferenceCounted> updater = new ReferenceCountUpdater<AbstractReferenceCounted>() { // from class: io.netty.util.AbstractReferenceCounted.1
        @Override // io.netty.util.internal.ReferenceCountUpdater
        protected AtomicIntegerFieldUpdater<AbstractReferenceCounted> updater() {
            return AbstractReferenceCounted.AIF_UPDATER;
        }

        @Override // io.netty.util.internal.ReferenceCountUpdater
        protected long unsafeOffset() {
            return AbstractReferenceCounted.REFCNT_FIELD_OFFSET;
        }
    };

    protected abstract void deallocate();

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return updater.refCnt(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void setRefCnt(int refCnt) {
        updater.setRefCnt(this, refCnt);
    }

    @Override // io.netty.util.ReferenceCounted
    public ReferenceCounted retain() {
        return updater.retain(this);
    }

    @Override // io.netty.util.ReferenceCounted
    public ReferenceCounted retain(int increment) {
        return updater.retain(this, increment);
    }

    @Override // io.netty.util.ReferenceCounted
    public ReferenceCounted touch() {
        return touch(null);
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return handleRelease(updater.release(this));
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return handleRelease(updater.release(this, decrement));
    }

    private boolean handleRelease(boolean result) {
        if (result) {
            deallocate();
        }
        return result;
    }
}
