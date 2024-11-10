package it.unimi.dsi.fastutil.ints;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface IntConsumer extends Consumer<Integer>, java.util.function.IntConsumer {
    @Override // java.util.function.Consumer
    @Deprecated
    default void accept(Integer t) {
        accept(t.intValue());
    }

    @Override // java.util.function.IntConsumer
    default IntConsumer andThen(final java.util.function.IntConsumer after) {
        Objects.requireNonNull(after);
        return new IntConsumer() { // from class: it.unimi.dsi.fastutil.ints.IntConsumer$$ExternalSyntheticLambda0
            @Override // java.util.function.IntConsumer
            public final void accept(int i) {
                IntConsumer.lambda$andThen$0(IntConsumer.this, after, i);
            }
        };
    }

    static /* synthetic */ void lambda$andThen$0(IntConsumer _this, java.util.function.IntConsumer after, int t) {
        _this.accept(t);
        after.accept(t);
    }

    default IntConsumer andThen(IntConsumer after) {
        return andThen((java.util.function.IntConsumer) after);
    }

    @Override // java.util.function.Consumer
    @Deprecated
    default Consumer<Integer> andThen(Consumer<? super Integer> after) {
        return super.andThen(after);
    }
}
