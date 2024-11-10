package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface ByteConsumer extends Consumer<Byte>, IntConsumer {
    void accept(byte b);

    @Override // java.util.function.IntConsumer
    @Deprecated
    default void accept(int t) {
        accept(SafeMath.safeIntToByte(t));
    }

    @Override // java.util.function.Consumer
    @Deprecated
    default void accept(Byte t) {
        accept(t.byteValue());
    }

    default ByteConsumer andThen(final ByteConsumer after) {
        Objects.requireNonNull(after);
        return new ByteConsumer() { // from class: it.unimi.dsi.fastutil.bytes.ByteConsumer$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.bytes.ByteConsumer
            public final void accept(byte b) {
                ByteConsumer.lambda$andThen$0(ByteConsumer.this, after, b);
            }
        };
    }

    static /* synthetic */ void lambda$andThen$0(ByteConsumer _this, ByteConsumer after, byte t) {
        _this.accept(t);
        after.accept(t);
    }

    @Override // java.util.function.IntConsumer
    default ByteConsumer andThen(IntConsumer after) {
        ByteConsumer byteConsumer$$ExternalSyntheticLambda0;
        if (after instanceof ByteConsumer) {
            byteConsumer$$ExternalSyntheticLambda0 = (ByteConsumer) after;
        } else {
            after.getClass();
            byteConsumer$$ExternalSyntheticLambda0 = new ByteConsumer$$ExternalSyntheticLambda0(after);
        }
        return andThen(byteConsumer$$ExternalSyntheticLambda0);
    }

    @Override // java.util.function.Consumer
    @Deprecated
    default Consumer<Byte> andThen(Consumer<? super Byte> after) {
        return super.andThen(after);
    }
}
