package it.unimi.dsi.fastutil.ints;

import java.util.Objects;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public interface IntIterable extends Iterable<Integer> {
    @Override // java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    IntIterator iterator();

    default IntIterator intIterator() {
        return iterator();
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    default IntSpliterator spliterator() {
        return IntSpliterators.asSpliteratorUnknownSize(iterator(), 0);
    }

    default IntSpliterator intSpliterator() {
        return spliterator();
    }

    default void forEach(java.util.function.IntConsumer action) {
        Objects.requireNonNull(action);
        iterator().forEachRemaining(action);
    }

    default void forEach(IntConsumer action) {
        forEach((java.util.function.IntConsumer) action);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntIterable
    @Deprecated
    default void forEach(Consumer<? super Integer> action) {
        java.util.function.IntConsumer intIterable$$ExternalSyntheticLambda0;
        Objects.requireNonNull(action);
        if (action instanceof java.util.function.IntConsumer) {
            intIterable$$ExternalSyntheticLambda0 = (java.util.function.IntConsumer) action;
        } else {
            action.getClass();
            intIterable$$ExternalSyntheticLambda0 = new IntIterable$$ExternalSyntheticLambda0(action);
        }
        forEach(intIterable$$ExternalSyntheticLambda0);
    }
}
