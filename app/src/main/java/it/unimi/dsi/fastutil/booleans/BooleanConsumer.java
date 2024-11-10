package it.unimi.dsi.fastutil.booleans;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface BooleanConsumer extends Consumer<Boolean> {
    void accept(boolean z);

    @Override // java.util.function.Consumer
    @Deprecated
    default void accept(Boolean t) {
        accept(t.booleanValue());
    }

    default BooleanConsumer andThen(final BooleanConsumer after) {
        Objects.requireNonNull(after);
        return new BooleanConsumer() { // from class: it.unimi.dsi.fastutil.booleans.BooleanConsumer$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.booleans.BooleanConsumer
            public final void accept(boolean z) {
                BooleanConsumer.lambda$andThen$0(BooleanConsumer.this, after, z);
            }
        };
    }

    static /* synthetic */ void lambda$andThen$0(BooleanConsumer _this, BooleanConsumer after, boolean t) {
        _this.accept(t);
        after.accept(t);
    }

    @Override // java.util.function.Consumer
    @Deprecated
    default Consumer<Boolean> andThen(Consumer<? super Boolean> after) {
        return super.andThen(after);
    }
}
