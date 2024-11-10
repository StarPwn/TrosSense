package io.netty.util.internal;

import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ReferenceCounted;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/* loaded from: classes4.dex */
public abstract class ReferenceCountUpdater<T extends ReferenceCounted> {
    protected abstract long unsafeOffset();

    protected abstract AtomicIntegerFieldUpdater<T> updater();

    public static long getUnsafeOffset(Class<? extends ReferenceCounted> clz, String fieldName) {
        try {
            if (PlatformDependent.hasUnsafe()) {
                return PlatformDependent.objectFieldOffset(clz.getDeclaredField(fieldName));
            }
            return -1L;
        } catch (Throwable th) {
            return -1L;
        }
    }

    public final int initialValue() {
        return 2;
    }

    public void setInitialValue(T instance) {
        long offset = unsafeOffset();
        if (offset == -1) {
            updater().set(instance, initialValue());
        } else {
            PlatformDependent.safeConstructPutInt(instance, offset, initialValue());
        }
    }

    private static int realRefCnt(int rawCnt) {
        if (rawCnt == 2 || rawCnt == 4 || (rawCnt & 1) == 0) {
            return rawCnt >>> 1;
        }
        return 0;
    }

    private static int toLiveRealRefCnt(int rawCnt, int decrement) {
        if (rawCnt == 2 || rawCnt == 4 || (rawCnt & 1) == 0) {
            return rawCnt >>> 1;
        }
        throw new IllegalReferenceCountException(0, -decrement);
    }

    private int nonVolatileRawCnt(T instance) {
        long offset = unsafeOffset();
        return offset != -1 ? PlatformDependent.getInt(instance, offset) : updater().get(instance);
    }

    public final int refCnt(T instance) {
        return realRefCnt(updater().get(instance));
    }

    public final boolean isLiveNonVolatile(T instance) {
        long offset = unsafeOffset();
        int rawCnt = offset != -1 ? PlatformDependent.getInt(instance, offset) : updater().get(instance);
        return rawCnt == 2 || rawCnt == 4 || rawCnt == 6 || rawCnt == 8 || (rawCnt & 1) == 0;
    }

    public final void setRefCnt(T instance, int refCnt) {
        updater().set(instance, refCnt > 0 ? refCnt << 1 : 1);
    }

    public final void resetRefCnt(T instance) {
        updater().lazySet(instance, initialValue());
    }

    public final T retain(T instance) {
        return retain0(instance, 1, 2);
    }

    public final T retain(T instance, int increment) {
        int rawIncrement = ObjectUtil.checkPositive(increment, "increment") << 1;
        return retain0(instance, increment, rawIncrement);
    }

    private T retain0(T instance, int increment, int rawIncrement) {
        int oldRef = updater().getAndAdd(instance, rawIncrement);
        if (oldRef != 2 && oldRef != 4 && (oldRef & 1) != 0) {
            throw new IllegalReferenceCountException(0, increment);
        }
        if ((oldRef <= 0 && oldRef + rawIncrement >= 0) || (oldRef >= 0 && oldRef + rawIncrement < oldRef)) {
            updater().getAndAdd(instance, -rawIncrement);
            throw new IllegalReferenceCountException(realRefCnt(oldRef), increment);
        }
        return instance;
    }

    public final boolean release(T instance) {
        int rawCnt = nonVolatileRawCnt(instance);
        if (rawCnt == 2) {
            return tryFinalRelease0(instance, 2) || retryRelease0(instance, 1);
        }
        return nonFinalRelease0(instance, 1, rawCnt, toLiveRealRefCnt(rawCnt, 1));
    }

    public final boolean release(T instance, int decrement) {
        int rawCnt = nonVolatileRawCnt(instance);
        int realCnt = toLiveRealRefCnt(rawCnt, ObjectUtil.checkPositive(decrement, "decrement"));
        if (decrement == realCnt) {
            return tryFinalRelease0(instance, rawCnt) || retryRelease0(instance, decrement);
        }
        return nonFinalRelease0(instance, decrement, rawCnt, realCnt);
    }

    private boolean tryFinalRelease0(T instance, int expectRawCnt) {
        return updater().compareAndSet(instance, expectRawCnt, 1);
    }

    private boolean nonFinalRelease0(T instance, int decrement, int rawCnt, int realCnt) {
        if (decrement < realCnt && updater().compareAndSet(instance, rawCnt, rawCnt - (decrement << 1))) {
            return false;
        }
        return retryRelease0(instance, decrement);
    }

    private boolean retryRelease0(T instance, int decrement) {
        while (true) {
            int rawCnt = updater().get(instance);
            int realCnt = toLiveRealRefCnt(rawCnt, decrement);
            if (decrement == realCnt) {
                if (tryFinalRelease0(instance, rawCnt)) {
                    return true;
                }
            } else if (decrement < realCnt) {
                if (updater().compareAndSet(instance, rawCnt, rawCnt - (decrement << 1))) {
                    return false;
                }
            } else {
                throw new IllegalReferenceCountException(realCnt, -decrement);
            }
            Thread.yield();
        }
    }
}
