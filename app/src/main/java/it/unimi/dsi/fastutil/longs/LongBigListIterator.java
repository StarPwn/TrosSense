package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.SafeMath;

/* loaded from: classes4.dex */
public interface LongBigListIterator extends LongBidirectionalIterator, BigListIterator<Long> {
    default void set(long k) {
        throw new UnsupportedOperationException();
    }

    default void add(long k) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.BigListIterator
    @Deprecated
    default void set(Long k) {
        set(k.longValue());
    }

    @Override // it.unimi.dsi.fastutil.BigListIterator
    @Deprecated
    default void add(Long k) {
        add(k.longValue());
    }

    default long skip(long n) {
        long i;
        long i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !hasNext()) {
                break;
            }
            nextLong();
            i2 = i;
        }
        return (n - i) - 1;
    }

    default long back(long n) {
        long i;
        long i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !hasPrevious()) {
                break;
            }
            previousLong();
            i2 = i;
        }
        return (n - i) - 1;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
    default int skip(int n) {
        return SafeMath.safeLongToInt(skip(n));
    }
}
