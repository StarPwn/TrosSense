package it.unimi.dsi.fastutil.bytes;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/* loaded from: classes4.dex */
public interface ByteIterator extends PrimitiveIterator<Byte, ByteConsumer> {
    byte nextByte();

    @Override // java.util.Iterator
    @Deprecated
    default Byte next() {
        return Byte.valueOf(nextByte());
    }

    @Override // java.util.PrimitiveIterator
    default void forEachRemaining(ByteConsumer action) {
        Objects.requireNonNull(action);
        while (hasNext()) {
            action.accept(nextByte());
        }
    }

    default void forEachRemaining(IntConsumer action) {
        ByteConsumer byteConsumer$$ExternalSyntheticLambda0;
        Objects.requireNonNull(action);
        if (action instanceof ByteConsumer) {
            byteConsumer$$ExternalSyntheticLambda0 = (ByteConsumer) action;
        } else {
            action.getClass();
            byteConsumer$$ExternalSyntheticLambda0 = new ByteConsumer$$ExternalSyntheticLambda0(action);
        }
        forEachRemaining(byteConsumer$$ExternalSyntheticLambda0);
    }

    @Override // java.util.Iterator
    @Deprecated
    default void forEachRemaining(Consumer<? super Byte> action) {
        ByteConsumer byteIterator$$ExternalSyntheticLambda0;
        if (action instanceof ByteConsumer) {
            byteIterator$$ExternalSyntheticLambda0 = (ByteConsumer) action;
        } else {
            action.getClass();
            byteIterator$$ExternalSyntheticLambda0 = new ByteIterator$$ExternalSyntheticLambda0(action);
        }
        forEachRemaining(byteIterator$$ExternalSyntheticLambda0);
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
            nextByte();
            i2 = i;
        }
        return (n - i) - 1;
    }
}
