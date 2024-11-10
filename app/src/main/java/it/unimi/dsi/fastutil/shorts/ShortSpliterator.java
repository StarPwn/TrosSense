package it.unimi.dsi.fastutil.shorts;

import java.util.Spliterator;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public interface ShortSpliterator extends Spliterator.OfPrimitive<Short, ShortConsumer, ShortSpliterator> {
    @Override // java.util.Spliterator.OfPrimitive, java.util.Spliterator
    ShortSpliterator trySplit();

    @Override // java.util.Spliterator
    @Deprecated
    default boolean tryAdvance(Consumer<? super Short> action) {
        Consumer shortIterator$$ExternalSyntheticLambda0;
        if (action instanceof ShortConsumer) {
            shortIterator$$ExternalSyntheticLambda0 = (ShortConsumer) action;
        } else {
            action.getClass();
            shortIterator$$ExternalSyntheticLambda0 = new ShortIterator$$ExternalSyntheticLambda0(action);
        }
        return tryAdvance((ShortSpliterator) shortIterator$$ExternalSyntheticLambda0);
    }

    @Override // java.util.Spliterator
    @Deprecated
    default void forEachRemaining(Consumer<? super Short> action) {
        Consumer shortIterator$$ExternalSyntheticLambda0;
        if (action instanceof ShortConsumer) {
            shortIterator$$ExternalSyntheticLambda0 = (ShortConsumer) action;
        } else {
            action.getClass();
            shortIterator$$ExternalSyntheticLambda0 = new ShortIterator$$ExternalSyntheticLambda0(action);
        }
        forEachRemaining((ShortSpliterator) shortIterator$$ExternalSyntheticLambda0);
    }

    default long skip(long n) {
        long i;
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        long i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !tryAdvance((ShortSpliterator) new ShortConsumer() { // from class: it.unimi.dsi.fastutil.shorts.ShortSpliterator$$ExternalSyntheticLambda0
                @Override // it.unimi.dsi.fastutil.shorts.ShortConsumer
                public final void accept(short s) {
                    ShortSpliterator.lambda$skip$0(s);
                }
            })) {
                break;
            }
            i2 = i;
        }
        return (n - i) - 1;
    }

    static /* synthetic */ void lambda$skip$0(short unused) {
    }

    @Override // java.util.Spliterator
    default ShortComparator getComparator() {
        throw new IllegalStateException();
    }
}
