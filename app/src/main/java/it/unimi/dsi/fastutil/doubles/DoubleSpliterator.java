package it.unimi.dsi.fastutil.doubles;

import java.util.Spliterator;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public interface DoubleSpliterator extends Spliterator.OfDouble {
    @Override // java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive, java.util.Spliterator
    DoubleSpliterator trySplit();

    @Override // java.util.Spliterator.OfDouble, java.util.Spliterator
    @Deprecated
    default boolean tryAdvance(Consumer<? super Double> action) {
        java.util.function.DoubleConsumer doubleIterator$$ExternalSyntheticLambda0;
        if (action instanceof java.util.function.DoubleConsumer) {
            doubleIterator$$ExternalSyntheticLambda0 = (java.util.function.DoubleConsumer) action;
        } else {
            action.getClass();
            doubleIterator$$ExternalSyntheticLambda0 = new DoubleIterator$$ExternalSyntheticLambda0(action);
        }
        return tryAdvance(doubleIterator$$ExternalSyntheticLambda0);
    }

    default boolean tryAdvance(DoubleConsumer action) {
        return tryAdvance((java.util.function.DoubleConsumer) action);
    }

    @Override // java.util.Spliterator.OfDouble, java.util.Spliterator
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

    default void forEachRemaining(DoubleConsumer action) {
        forEachRemaining((java.util.function.DoubleConsumer) action);
    }

    default long skip(long n) {
        long i;
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        long i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !tryAdvance(new DoubleConsumer() { // from class: it.unimi.dsi.fastutil.doubles.DoubleSpliterator$$ExternalSyntheticLambda0
                @Override // java.util.function.DoubleConsumer
                public final void accept(double d) {
                    DoubleSpliterator.lambda$skip$0(d);
                }
            })) {
                break;
            }
            i2 = i;
        }
        return (n - i) - 1;
    }

    static /* synthetic */ void lambda$skip$0(double unused) {
    }

    @Override // java.util.Spliterator
    default DoubleComparator getComparator() {
        throw new IllegalStateException();
    }
}
