package it.unimi.dsi.fastutil.chars;

import java.util.Spliterator;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public interface CharSpliterator extends Spliterator.OfPrimitive<Character, CharConsumer, CharSpliterator> {
    @Override // java.util.Spliterator.OfPrimitive, java.util.Spliterator
    CharSpliterator trySplit();

    @Override // java.util.Spliterator
    @Deprecated
    default boolean tryAdvance(Consumer<? super Character> action) {
        Consumer charIterator$$ExternalSyntheticLambda0;
        if (action instanceof CharConsumer) {
            charIterator$$ExternalSyntheticLambda0 = (CharConsumer) action;
        } else {
            action.getClass();
            charIterator$$ExternalSyntheticLambda0 = new CharIterator$$ExternalSyntheticLambda0(action);
        }
        return tryAdvance((CharSpliterator) charIterator$$ExternalSyntheticLambda0);
    }

    @Override // java.util.Spliterator
    @Deprecated
    default void forEachRemaining(Consumer<? super Character> action) {
        Consumer charIterator$$ExternalSyntheticLambda0;
        if (action instanceof CharConsumer) {
            charIterator$$ExternalSyntheticLambda0 = (CharConsumer) action;
        } else {
            action.getClass();
            charIterator$$ExternalSyntheticLambda0 = new CharIterator$$ExternalSyntheticLambda0(action);
        }
        forEachRemaining((CharSpliterator) charIterator$$ExternalSyntheticLambda0);
    }

    default long skip(long n) {
        long i;
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        long i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !tryAdvance((CharSpliterator) new CharConsumer() { // from class: it.unimi.dsi.fastutil.chars.CharSpliterator$$ExternalSyntheticLambda0
                @Override // it.unimi.dsi.fastutil.chars.CharConsumer
                public final void accept(char c) {
                    CharSpliterator.lambda$skip$0(c);
                }
            })) {
                break;
            }
            i2 = i;
        }
        return (n - i) - 1;
    }

    static /* synthetic */ void lambda$skip$0(char unused) {
    }

    @Override // java.util.Spliterator
    default CharComparator getComparator() {
        throw new IllegalStateException();
    }
}
