package it.unimi.dsi.fastutil.longs;

import java.util.Objects;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public interface LongIterable extends Iterable<Long> {
    @Override // java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    LongIterator iterator();

    default LongIterator longIterator() {
        return iterator();
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    default LongSpliterator spliterator() {
        return LongSpliterators.asSpliteratorUnknownSize(iterator(), 0);
    }

    default LongSpliterator longSpliterator() {
        return spliterator();
    }

    default void forEach(java.util.function.LongConsumer action) {
        Objects.requireNonNull(action);
        iterator().forEachRemaining(action);
    }

    default void forEach(LongConsumer action) {
        forEach((java.util.function.LongConsumer) action);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongIterable
    @Deprecated
    default void forEach(Consumer<? super Long> action) {
        java.util.function.LongConsumer longIterable$$ExternalSyntheticLambda0;
        Objects.requireNonNull(action);
        if (action instanceof java.util.function.LongConsumer) {
            longIterable$$ExternalSyntheticLambda0 = (java.util.function.LongConsumer) action;
        } else {
            action.getClass();
            longIterable$$ExternalSyntheticLambda0 = new LongIterable$$ExternalSyntheticLambda0(action);
        }
        forEach(longIterable$$ExternalSyntheticLambda0);
    }
}
