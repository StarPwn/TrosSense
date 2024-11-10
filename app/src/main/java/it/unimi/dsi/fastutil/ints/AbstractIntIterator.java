package it.unimi.dsi.fastutil.ints;

/* loaded from: classes4.dex */
public abstract class AbstractIntIterator implements IntIterator {
    @Override // it.unimi.dsi.fastutil.ints.IntIterator
    public final void forEachRemaining(IntConsumer action) {
        forEachRemaining((java.util.function.IntConsumer) action);
    }
}
