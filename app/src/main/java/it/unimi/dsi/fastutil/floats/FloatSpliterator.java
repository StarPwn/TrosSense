package it.unimi.dsi.fastutil.floats;

import java.util.Spliterator;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public interface FloatSpliterator extends Spliterator.OfPrimitive<Float, FloatConsumer, FloatSpliterator> {
    @Override // java.util.Spliterator.OfPrimitive, java.util.Spliterator
    FloatSpliterator trySplit();

    @Override // java.util.Spliterator
    @Deprecated
    default boolean tryAdvance(Consumer<? super Float> action) {
        Consumer floatIterator$$ExternalSyntheticLambda0;
        if (action instanceof FloatConsumer) {
            floatIterator$$ExternalSyntheticLambda0 = (FloatConsumer) action;
        } else {
            action.getClass();
            floatIterator$$ExternalSyntheticLambda0 = new FloatIterator$$ExternalSyntheticLambda0(action);
        }
        return tryAdvance((FloatSpliterator) floatIterator$$ExternalSyntheticLambda0);
    }

    @Override // java.util.Spliterator
    @Deprecated
    default void forEachRemaining(Consumer<? super Float> action) {
        Consumer floatIterator$$ExternalSyntheticLambda0;
        if (action instanceof FloatConsumer) {
            floatIterator$$ExternalSyntheticLambda0 = (FloatConsumer) action;
        } else {
            action.getClass();
            floatIterator$$ExternalSyntheticLambda0 = new FloatIterator$$ExternalSyntheticLambda0(action);
        }
        forEachRemaining((FloatSpliterator) floatIterator$$ExternalSyntheticLambda0);
    }

    default long skip(long n) {
        long i;
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        long i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !tryAdvance((FloatSpliterator) new FloatConsumer() { // from class: it.unimi.dsi.fastutil.floats.FloatSpliterator$$ExternalSyntheticLambda0
                @Override // it.unimi.dsi.fastutil.floats.FloatConsumer
                public final void accept(float f) {
                    FloatSpliterator.lambda$skip$0(f);
                }
            })) {
                break;
            }
            i2 = i;
        }
        return (n - i) - 1;
    }

    static /* synthetic */ void lambda$skip$0(float unused) {
    }

    @Override // java.util.Spliterator
    default FloatComparator getComparator() {
        throw new IllegalStateException();
    }
}
