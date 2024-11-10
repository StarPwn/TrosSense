package it.unimi.dsi.fastutil.ints;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public interface IntIterator extends PrimitiveIterator.OfInt {
    @Override // java.util.PrimitiveIterator.OfInt
    int nextInt();

    @Override // java.util.PrimitiveIterator.OfInt, java.util.Iterator
    @Deprecated
    default Integer next() {
        return Integer.valueOf(nextInt());
    }

    default void forEachRemaining(IntConsumer action) {
        forEachRemaining((java.util.function.IntConsumer) action);
    }

    @Override // java.util.PrimitiveIterator.OfInt, java.util.Iterator
    @Deprecated
    default void forEachRemaining(Consumer<? super Integer> action) {
        java.util.function.IntConsumer intIterable$$ExternalSyntheticLambda0;
        if (action instanceof java.util.function.IntConsumer) {
            intIterable$$ExternalSyntheticLambda0 = (java.util.function.IntConsumer) action;
        } else {
            action.getClass();
            intIterable$$ExternalSyntheticLambda0 = new IntIterable$$ExternalSyntheticLambda0(action);
        }
        forEachRemaining(intIterable$$ExternalSyntheticLambda0);
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
            nextInt();
            i2 = i;
        }
        return (n - i) - 1;
    }
}
