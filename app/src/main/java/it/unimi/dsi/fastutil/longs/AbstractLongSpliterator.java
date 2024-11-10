package it.unimi.dsi.fastutil.longs;

/* loaded from: classes4.dex */
public abstract class AbstractLongSpliterator implements LongSpliterator {
    @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
    public final boolean tryAdvance(LongConsumer action) {
        return tryAdvance((java.util.function.LongConsumer) action);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
    public final void forEachRemaining(LongConsumer action) {
        forEachRemaining((java.util.function.LongConsumer) action);
    }
}
