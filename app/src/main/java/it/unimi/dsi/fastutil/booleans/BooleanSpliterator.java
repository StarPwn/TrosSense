package it.unimi.dsi.fastutil.booleans;

import java.util.Spliterator;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public interface BooleanSpliterator extends Spliterator.OfPrimitive<Boolean, BooleanConsumer, BooleanSpliterator> {
    @Override // java.util.Spliterator.OfPrimitive, java.util.Spliterator
    BooleanSpliterator trySplit();

    @Override // java.util.Spliterator
    @Deprecated
    default boolean tryAdvance(Consumer<? super Boolean> action) {
        Consumer booleanIterator$$ExternalSyntheticLambda0;
        if (action instanceof BooleanConsumer) {
            booleanIterator$$ExternalSyntheticLambda0 = (BooleanConsumer) action;
        } else {
            action.getClass();
            booleanIterator$$ExternalSyntheticLambda0 = new BooleanIterator$$ExternalSyntheticLambda0(action);
        }
        return tryAdvance((BooleanSpliterator) booleanIterator$$ExternalSyntheticLambda0);
    }

    @Override // java.util.Spliterator
    @Deprecated
    default void forEachRemaining(Consumer<? super Boolean> action) {
        Consumer booleanIterator$$ExternalSyntheticLambda0;
        if (action instanceof BooleanConsumer) {
            booleanIterator$$ExternalSyntheticLambda0 = (BooleanConsumer) action;
        } else {
            action.getClass();
            booleanIterator$$ExternalSyntheticLambda0 = new BooleanIterator$$ExternalSyntheticLambda0(action);
        }
        forEachRemaining((BooleanSpliterator) booleanIterator$$ExternalSyntheticLambda0);
    }

    default long skip(long n) {
        long i;
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        long i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !tryAdvance((BooleanSpliterator) new BooleanConsumer() { // from class: it.unimi.dsi.fastutil.booleans.BooleanSpliterator$$ExternalSyntheticLambda0
                @Override // it.unimi.dsi.fastutil.booleans.BooleanConsumer
                public final void accept(boolean z) {
                    BooleanSpliterator.lambda$skip$0(z);
                }
            })) {
                break;
            }
            i2 = i;
        }
        return (n - i) - 1;
    }

    static /* synthetic */ void lambda$skip$0(boolean unused) {
    }

    @Override // java.util.Spliterator
    default BooleanComparator getComparator() {
        throw new IllegalStateException();
    }
}
