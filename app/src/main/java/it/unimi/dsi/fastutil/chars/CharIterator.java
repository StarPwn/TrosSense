package it.unimi.dsi.fastutil.chars;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/* loaded from: classes4.dex */
public interface CharIterator extends PrimitiveIterator<Character, CharConsumer> {
    char nextChar();

    @Override // java.util.Iterator
    @Deprecated
    default Character next() {
        return Character.valueOf(nextChar());
    }

    @Override // java.util.PrimitiveIterator
    default void forEachRemaining(CharConsumer action) {
        Objects.requireNonNull(action);
        while (hasNext()) {
            action.accept(nextChar());
        }
    }

    default void forEachRemaining(IntConsumer action) {
        CharConsumer charConsumer$$ExternalSyntheticLambda1;
        Objects.requireNonNull(action);
        if (action instanceof CharConsumer) {
            charConsumer$$ExternalSyntheticLambda1 = (CharConsumer) action;
        } else {
            action.getClass();
            charConsumer$$ExternalSyntheticLambda1 = new CharConsumer$$ExternalSyntheticLambda1(action);
        }
        forEachRemaining(charConsumer$$ExternalSyntheticLambda1);
    }

    @Override // java.util.Iterator
    @Deprecated
    default void forEachRemaining(Consumer<? super Character> action) {
        CharConsumer charIterator$$ExternalSyntheticLambda0;
        if (action instanceof CharConsumer) {
            charIterator$$ExternalSyntheticLambda0 = (CharConsumer) action;
        } else {
            action.getClass();
            charIterator$$ExternalSyntheticLambda0 = new CharIterator$$ExternalSyntheticLambda0(action);
        }
        forEachRemaining(charIterator$$ExternalSyntheticLambda0);
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
            nextChar();
            i2 = i;
        }
        return (n - i) - 1;
    }
}
