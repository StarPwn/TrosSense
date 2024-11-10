package it.unimi.dsi.fastutil.bytes;

import java.util.Spliterator;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public interface ByteSpliterator extends Spliterator.OfPrimitive<Byte, ByteConsumer, ByteSpliterator> {
    @Override // java.util.Spliterator.OfPrimitive, java.util.Spliterator
    ByteSpliterator trySplit();

    @Override // java.util.Spliterator
    @Deprecated
    default boolean tryAdvance(Consumer<? super Byte> action) {
        Consumer byteIterator$$ExternalSyntheticLambda0;
        if (action instanceof ByteConsumer) {
            byteIterator$$ExternalSyntheticLambda0 = (ByteConsumer) action;
        } else {
            action.getClass();
            byteIterator$$ExternalSyntheticLambda0 = new ByteIterator$$ExternalSyntheticLambda0(action);
        }
        return tryAdvance((ByteSpliterator) byteIterator$$ExternalSyntheticLambda0);
    }

    @Override // java.util.Spliterator
    @Deprecated
    default void forEachRemaining(Consumer<? super Byte> action) {
        Consumer byteIterator$$ExternalSyntheticLambda0;
        if (action instanceof ByteConsumer) {
            byteIterator$$ExternalSyntheticLambda0 = (ByteConsumer) action;
        } else {
            action.getClass();
            byteIterator$$ExternalSyntheticLambda0 = new ByteIterator$$ExternalSyntheticLambda0(action);
        }
        forEachRemaining((ByteSpliterator) byteIterator$$ExternalSyntheticLambda0);
    }

    default long skip(long n) {
        long i;
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        long i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !tryAdvance((ByteSpliterator) new ByteConsumer() { // from class: it.unimi.dsi.fastutil.bytes.ByteSpliterator$$ExternalSyntheticLambda0
                @Override // it.unimi.dsi.fastutil.bytes.ByteConsumer
                public final void accept(byte b) {
                    ByteSpliterator.lambda$skip$0(b);
                }
            })) {
                break;
            }
            i2 = i;
        }
        return (n - i) - 1;
    }

    static /* synthetic */ void lambda$skip$0(byte unused) {
    }

    @Override // java.util.Spliterator
    default ByteComparator getComparator() {
        throw new IllegalStateException();
    }
}
