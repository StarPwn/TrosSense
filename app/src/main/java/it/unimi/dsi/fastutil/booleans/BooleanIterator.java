package it.unimi.dsi.fastutil.booleans;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public interface BooleanIterator extends PrimitiveIterator<Boolean, BooleanConsumer> {
    boolean nextBoolean();

    @Override // java.util.Iterator
    @Deprecated
    default Boolean next() {
        return Boolean.valueOf(nextBoolean());
    }

    @Override // java.util.PrimitiveIterator
    default void forEachRemaining(BooleanConsumer action) {
        Objects.requireNonNull(action);
        while (hasNext()) {
            action.accept(nextBoolean());
        }
    }

    @Override // java.util.Iterator
    @Deprecated
    default void forEachRemaining(Consumer<? super Boolean> action) {
        BooleanConsumer booleanIterator$$ExternalSyntheticLambda0;
        if (action instanceof BooleanConsumer) {
            booleanIterator$$ExternalSyntheticLambda0 = (BooleanConsumer) action;
        } else {
            action.getClass();
            booleanIterator$$ExternalSyntheticLambda0 = new BooleanIterator$$ExternalSyntheticLambda0(action);
        }
        forEachRemaining(booleanIterator$$ExternalSyntheticLambda0);
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
            nextBoolean();
            i2 = i;
        }
        return (n - i) - 1;
    }
}
