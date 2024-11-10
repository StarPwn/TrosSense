package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.SafeMath;

/* loaded from: classes4.dex */
public interface IntBigListIterator extends IntBidirectionalIterator, BigListIterator<Integer> {
    default void set(int k) {
        throw new UnsupportedOperationException();
    }

    default void add(int k) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.BigListIterator
    @Deprecated
    default void set(Integer k) {
        set(k.intValue());
    }

    @Override // it.unimi.dsi.fastutil.BigListIterator
    @Deprecated
    default void add(Integer k) {
        add(k.intValue());
    }

    default long skip(long n) {
        long i;
        long i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !hasNext()) {
                break;
            }
            nextInt();
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
            previousInt();
            i2 = i;
        }
        return (n - i) - 1;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.ints.IntIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
    default int skip(int n) {
        return SafeMath.safeLongToInt(skip(n));
    }
}
