package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface FloatConsumer extends Consumer<Float>, DoubleConsumer {
    void accept(float f);

    @Override // java.util.function.DoubleConsumer
    @Deprecated
    default void accept(double t) {
        accept(SafeMath.safeDoubleToFloat(t));
    }

    @Override // java.util.function.Consumer
    @Deprecated
    default void accept(Float t) {
        accept(t.floatValue());
    }

    default FloatConsumer andThen(final FloatConsumer after) {
        Objects.requireNonNull(after);
        return new FloatConsumer() { // from class: it.unimi.dsi.fastutil.floats.FloatConsumer$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.floats.FloatConsumer
            public final void accept(float f) {
                FloatConsumer.lambda$andThen$0(FloatConsumer.this, after, f);
            }
        };
    }

    static /* synthetic */ void lambda$andThen$0(FloatConsumer _this, FloatConsumer after, float t) {
        _this.accept(t);
        after.accept(t);
    }

    @Override // java.util.function.DoubleConsumer
    default FloatConsumer andThen(DoubleConsumer after) {
        FloatConsumer floatConsumer$$ExternalSyntheticLambda0;
        if (after instanceof FloatConsumer) {
            floatConsumer$$ExternalSyntheticLambda0 = (FloatConsumer) after;
        } else {
            after.getClass();
            floatConsumer$$ExternalSyntheticLambda0 = new FloatConsumer$$ExternalSyntheticLambda0(after);
        }
        return andThen(floatConsumer$$ExternalSyntheticLambda0);
    }

    @Override // java.util.function.Consumer
    @Deprecated
    default Consumer<Float> andThen(Consumer<? super Float> after) {
        return super.andThen(after);
    }
}
