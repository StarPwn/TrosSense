package it.unimi.dsi.fastutil.floats;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

/* loaded from: classes4.dex */
public interface FloatIterator extends PrimitiveIterator<Float, FloatConsumer> {
    float nextFloat();

    @Override // java.util.Iterator
    @Deprecated
    default Float next() {
        return Float.valueOf(nextFloat());
    }

    @Override // java.util.PrimitiveIterator
    default void forEachRemaining(FloatConsumer action) {
        Objects.requireNonNull(action);
        while (hasNext()) {
            action.accept(nextFloat());
        }
    }

    default void forEachRemaining(DoubleConsumer action) {
        FloatConsumer floatConsumer$$ExternalSyntheticLambda0;
        Objects.requireNonNull(action);
        if (action instanceof FloatConsumer) {
            floatConsumer$$ExternalSyntheticLambda0 = (FloatConsumer) action;
        } else {
            action.getClass();
            floatConsumer$$ExternalSyntheticLambda0 = new FloatConsumer$$ExternalSyntheticLambda0(action);
        }
        forEachRemaining(floatConsumer$$ExternalSyntheticLambda0);
    }

    @Override // java.util.Iterator
    @Deprecated
    default void forEachRemaining(Consumer<? super Float> action) {
        FloatConsumer floatIterator$$ExternalSyntheticLambda0;
        if (action instanceof FloatConsumer) {
            floatIterator$$ExternalSyntheticLambda0 = (FloatConsumer) action;
        } else {
            action.getClass();
            floatIterator$$ExternalSyntheticLambda0 = new FloatIterator$$ExternalSyntheticLambda0(action);
        }
        forEachRemaining(floatIterator$$ExternalSyntheticLambda0);
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
            nextFloat();
            i2 = i;
        }
        return (n - i) - 1;
    }
}
