package it.unimi.dsi.fastutil.doubles;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface DoubleConsumer extends Consumer<Double>, java.util.function.DoubleConsumer {
    @Override // java.util.function.Consumer
    @Deprecated
    default void accept(Double t) {
        accept(t.doubleValue());
    }

    @Override // java.util.function.DoubleConsumer
    default DoubleConsumer andThen(final java.util.function.DoubleConsumer after) {
        Objects.requireNonNull(after);
        return new DoubleConsumer() { // from class: it.unimi.dsi.fastutil.doubles.DoubleConsumer$$ExternalSyntheticLambda0
            @Override // java.util.function.DoubleConsumer
            public final void accept(double d) {
                DoubleConsumer.lambda$andThen$0(DoubleConsumer.this, after, d);
            }
        };
    }

    static /* synthetic */ void lambda$andThen$0(DoubleConsumer _this, java.util.function.DoubleConsumer after, double t) {
        _this.accept(t);
        after.accept(t);
    }

    default DoubleConsumer andThen(DoubleConsumer after) {
        return andThen((java.util.function.DoubleConsumer) after);
    }

    @Override // java.util.function.Consumer
    @Deprecated
    default Consumer<Double> andThen(Consumer<? super Double> after) {
        return super.andThen(after);
    }
}
