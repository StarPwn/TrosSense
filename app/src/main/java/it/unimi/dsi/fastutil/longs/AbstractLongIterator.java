package it.unimi.dsi.fastutil.longs;

/* loaded from: classes4.dex */
public abstract class AbstractLongIterator implements LongIterator {
    @Override // it.unimi.dsi.fastutil.longs.LongIterator
    public final void forEachRemaining(LongConsumer action) {
        forEachRemaining((java.util.function.LongConsumer) action);
    }
}
