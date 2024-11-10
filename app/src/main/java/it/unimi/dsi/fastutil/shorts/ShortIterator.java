package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.ints.IntIterators$ShortIteratorWrapper$$ExternalSyntheticLambda0;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/* loaded from: classes4.dex */
public interface ShortIterator extends PrimitiveIterator<Short, ShortConsumer> {
    short nextShort();

    @Override // java.util.Iterator
    @Deprecated
    default Short next() {
        return Short.valueOf(nextShort());
    }

    @Override // java.util.PrimitiveIterator
    default void forEachRemaining(ShortConsumer action) {
        Objects.requireNonNull(action);
        while (hasNext()) {
            action.accept(nextShort());
        }
    }

    default void forEachRemaining(IntConsumer action) {
        ShortConsumer intIterators$ShortIteratorWrapper$$ExternalSyntheticLambda0;
        Objects.requireNonNull(action);
        if (action instanceof ShortConsumer) {
            intIterators$ShortIteratorWrapper$$ExternalSyntheticLambda0 = (ShortConsumer) action;
        } else {
            action.getClass();
            intIterators$ShortIteratorWrapper$$ExternalSyntheticLambda0 = new IntIterators$ShortIteratorWrapper$$ExternalSyntheticLambda0(action);
        }
        forEachRemaining(intIterators$ShortIteratorWrapper$$ExternalSyntheticLambda0);
    }

    @Override // java.util.Iterator
    @Deprecated
    default void forEachRemaining(Consumer<? super Short> action) {
        ShortConsumer shortIterator$$ExternalSyntheticLambda0;
        if (action instanceof ShortConsumer) {
            shortIterator$$ExternalSyntheticLambda0 = (ShortConsumer) action;
        } else {
            action.getClass();
            shortIterator$$ExternalSyntheticLambda0 = new ShortIterator$$ExternalSyntheticLambda0(action);
        }
        forEachRemaining(shortIterator$$ExternalSyntheticLambda0);
    }

    default int skip(int n) {
        int i;
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        int i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !hasNext()) {
                break;
            }
            nextShort();
            i2 = i;
        }
        return (n - i) - 1;
    }
}
