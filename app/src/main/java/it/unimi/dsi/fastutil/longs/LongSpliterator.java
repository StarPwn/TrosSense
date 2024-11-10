package it.unimi.dsi.fastutil.longs;

import java.util.Spliterator;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public interface LongSpliterator extends Spliterator.OfLong {
    @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
    LongSpliterator trySplit();

    @Override // java.util.Spliterator.OfLong, java.util.Spliterator
    @Deprecated
    default boolean tryAdvance(Consumer<? super Long> action) {
        java.util.function.LongConsumer longIterable$$ExternalSyntheticLambda0;
        if (action instanceof java.util.function.LongConsumer) {
            longIterable$$ExternalSyntheticLambda0 = (java.util.function.LongConsumer) action;
        } else {
            action.getClass();
            longIterable$$ExternalSyntheticLambda0 = new LongIterable$$ExternalSyntheticLambda0(action);
        }
        return tryAdvance(longIterable$$ExternalSyntheticLambda0);
    }

    default boolean tryAdvance(LongConsumer action) {
        return tryAdvance((java.util.function.LongConsumer) action);
    }

    @Override // java.util.Spliterator.OfLong, java.util.Spliterator
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

    default void forEachRemaining(LongConsumer action) {
        forEachRemaining((java.util.function.LongConsumer) action);
    }

    default long skip(long n) {
        long i;
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        long i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !tryAdvance(new LongConsumer() { // from class: it.unimi.dsi.fastutil.longs.LongSpliterator$$ExternalSyntheticLambda0
                @Override // java.util.function.LongConsumer
                public final void accept(long j) {
                    LongSpliterator.lambda$skip$0(j);
                }
            })) {
                break;
            }
            i2 = i;
        }
        return (n - i) - 1;
    }

    static /* synthetic */ void lambda$skip$0(long unused) {
    }

    @Override // java.util.Spliterator
    default LongComparator getComparator() {
        throw new IllegalStateException();
    }
}
