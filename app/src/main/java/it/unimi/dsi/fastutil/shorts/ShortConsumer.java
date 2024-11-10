package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.ints.IntIterators$ShortIteratorWrapper$$ExternalSyntheticLambda0;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface ShortConsumer extends Consumer<Short>, IntConsumer {
    void accept(short s);

    @Override // java.util.function.IntConsumer
    @Deprecated
    default void accept(int t) {
        accept(SafeMath.safeIntToShort(t));
    }

    @Override // java.util.function.Consumer
    @Deprecated
    default void accept(Short t) {
        accept(t.shortValue());
    }

    default ShortConsumer andThen(final ShortConsumer after) {
        Objects.requireNonNull(after);
        return new ShortConsumer() { // from class: it.unimi.dsi.fastutil.shorts.ShortConsumer$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.shorts.ShortConsumer
            public final void accept(short s) {
                ShortConsumer.lambda$andThen$0(ShortConsumer.this, after, s);
            }
        };
    }

    static /* synthetic */ void lambda$andThen$0(ShortConsumer _this, ShortConsumer after, short t) {
        _this.accept(t);
        after.accept(t);
    }

    @Override // java.util.function.IntConsumer
    default ShortConsumer andThen(IntConsumer after) {
        ShortConsumer intIterators$ShortIteratorWrapper$$ExternalSyntheticLambda0;
        if (after instanceof ShortConsumer) {
            intIterators$ShortIteratorWrapper$$ExternalSyntheticLambda0 = (ShortConsumer) after;
        } else {
            after.getClass();
            intIterators$ShortIteratorWrapper$$ExternalSyntheticLambda0 = new IntIterators$ShortIteratorWrapper$$ExternalSyntheticLambda0(after);
        }
        return andThen(intIterators$ShortIteratorWrapper$$ExternalSyntheticLambda0);
    }

    @Override // java.util.function.Consumer
    @Deprecated
    default Consumer<Short> andThen(Consumer<? super Short> after) {
        return super.andThen(after);
    }
}
