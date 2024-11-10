package it.unimi.dsi.fastutil.ints;

import java.util.Spliterator;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public interface IntSpliterator extends Spliterator.OfInt {
    @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive, java.util.Spliterator
    IntSpliterator trySplit();

    @Override // java.util.Spliterator.OfInt, java.util.Spliterator
    @Deprecated
    default boolean tryAdvance(Consumer<? super Integer> action) {
        java.util.function.IntConsumer intIterable$$ExternalSyntheticLambda0;
        if (action instanceof java.util.function.IntConsumer) {
            intIterable$$ExternalSyntheticLambda0 = (java.util.function.IntConsumer) action;
        } else {
            action.getClass();
            intIterable$$ExternalSyntheticLambda0 = new IntIterable$$ExternalSyntheticLambda0(action);
        }
        return tryAdvance(intIterable$$ExternalSyntheticLambda0);
    }

    default boolean tryAdvance(IntConsumer action) {
        return tryAdvance((java.util.function.IntConsumer) action);
    }

    @Override // java.util.Spliterator.OfInt, java.util.Spliterator
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

    default void forEachRemaining(IntConsumer action) {
        forEachRemaining((java.util.function.IntConsumer) action);
    }

    default long skip(long n) {
        long i;
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        long i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !tryAdvance(new IntConsumer() { // from class: it.unimi.dsi.fastutil.ints.IntSpliterator$$ExternalSyntheticLambda0
                @Override // java.util.function.IntConsumer
                public final void accept(int i3) {
                    IntSpliterator.lambda$skip$0(i3);
                }
            })) {
                break;
            }
            i2 = i;
        }
        return (n - i) - 1;
    }

    static /* synthetic */ void lambda$skip$0(int unused) {
    }

    @Override // java.util.Spliterator
    default IntComparator getComparator() {
        throw new IllegalStateException();
    }
}
