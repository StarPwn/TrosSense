package it.unimi.dsi.fastutil.doubles;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public interface DoubleIterator extends PrimitiveIterator.OfDouble {
    @Override // java.util.PrimitiveIterator.OfDouble
    double nextDouble();

    @Override // java.util.PrimitiveIterator.OfDouble, java.util.Iterator
    @Deprecated
    default Double next() {
        return Double.valueOf(nextDouble());
    }

    default void forEachRemaining(DoubleConsumer action) {
        forEachRemaining((java.util.function.DoubleConsumer) action);
    }

    @Override // java.util.PrimitiveIterator.OfDouble, java.util.Iterator
    @Deprecated
    default void forEachRemaining(Consumer<? super Double> action) {
        java.util.function.DoubleConsumer doubleIterator$$ExternalSyntheticLambda0;
        if (action instanceof java.util.function.DoubleConsumer) {
            doubleIterator$$ExternalSyntheticLambda0 = (java.util.function.DoubleConsumer) action;
        } else {
            action.getClass();
            doubleIterator$$ExternalSyntheticLambda0 = new DoubleIterator$$ExternalSyntheticLambda0(action);
        }
        forEachRemaining(doubleIterator$$ExternalSyntheticLambda0);
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
            nextDouble();
            i2 = i;
        }
        return (n - i) - 1;
    }
}
