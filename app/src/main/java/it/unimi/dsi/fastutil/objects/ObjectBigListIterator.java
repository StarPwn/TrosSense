package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.SafeMath;

/* loaded from: classes4.dex */
public interface ObjectBigListIterator<K> extends ObjectBidirectionalIterator<K>, BigListIterator<K> {
    @Override // it.unimi.dsi.fastutil.BigListIterator
    default void set(K k) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.BigListIterator
    default void add(K k) {
        throw new UnsupportedOperationException();
    }

    default long skip(long n) {
        long i;
        long i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !hasNext()) {
                break;
            }
            next();
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
            previous();
            i2 = i;
        }
        return (n - i) - 1;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
    default int skip(int n) {
        return SafeMath.safeLongToInt(skip(n));
    }
}
