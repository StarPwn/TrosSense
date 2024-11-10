package it.unimi.dsi.fastutil.longs;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public interface LongIterator extends PrimitiveIterator.OfLong {
    @Override // java.util.PrimitiveIterator.OfLong
    long nextLong();

    @Override // java.util.PrimitiveIterator.OfLong, java.util.Iterator
    @Deprecated
    default Long next() {
        return Long.valueOf(nextLong());
    }

    default void forEachRemaining(LongConsumer action) {
        forEachRemaining((java.util.function.LongConsumer) action);
    }

    @Override // java.util.PrimitiveIterator.OfLong, java.util.Iterator
    @Deprecated
    default void forEachRemaining(Consumer<? super Long> action) {
        java.util.function.LongConsumer longIterable$$ExternalSyntheticLambda0;
        if (action instanceof java.util.function.LongConsumer) {
            longIterable$$ExternalSyntheticLambda0 = (java.util.function.LongConsumer) action;
        } else {
            action.getClass();
            longIterable$$ExternalSyntheticLambda0 = new LongIterable$$ExternalSyntheticLambda0(action);
        }
        forEachRemaining(longIterable$$ExternalSyntheticLambda0);
    }

    default int skip(int n) {
        int i;
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        int i2 = n;
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
}
