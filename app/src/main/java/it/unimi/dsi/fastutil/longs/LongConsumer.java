package it.unimi.dsi.fastutil.longs;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface LongConsumer extends Consumer<Long>, java.util.function.LongConsumer {
    @Override // java.util.function.Consumer
    @Deprecated
    default void accept(Long t) {
        accept(t.longValue());
    }

    @Override // java.util.function.LongConsumer
    default LongConsumer andThen(final java.util.function.LongConsumer after) {
        Objects.requireNonNull(after);
        return new LongConsumer() { // from class: it.unimi.dsi.fastutil.longs.LongConsumer$$ExternalSyntheticLambda0
            @Override // java.util.function.LongConsumer
            public final void accept(long j) {
                LongConsumer.lambda$andThen$0(LongConsumer.this, after, j);
            }
        };
    }

    static /* synthetic */ void lambda$andThen$0(LongConsumer _this, java.util.function.LongConsumer after, long t) {
        _this.accept(t);
        after.accept(t);
    }

    default LongConsumer andThen(LongConsumer after) {
        return andThen((java.util.function.LongConsumer) after);
    }

    @Override // java.util.function.Consumer
    @Deprecated
    default Consumer<Long> andThen(Consumer<? super Long> after) {
        return super.andThen(after);
    }
}
