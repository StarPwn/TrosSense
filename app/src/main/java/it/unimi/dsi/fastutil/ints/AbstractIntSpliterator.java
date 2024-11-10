package it.unimi.dsi.fastutil.ints;

/* loaded from: classes4.dex */
public abstract class AbstractIntSpliterator implements IntSpliterator {
    @Override // it.unimi.dsi.fastutil.ints.IntSpliterator
    public final boolean tryAdvance(IntConsumer action) {
        return tryAdvance((java.util.function.IntConsumer) action);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntSpliterator
    public final void forEachRemaining(IntConsumer action) {
        forEachRemaining((java.util.function.IntConsumer) action);
    }
}
