package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface CharConsumer extends Consumer<Character>, IntConsumer {
    void accept(char c);

    @Override // java.util.function.IntConsumer
    @Deprecated
    default void accept(int t) {
        accept(SafeMath.safeIntToChar(t));
    }

    @Override // java.util.function.Consumer
    @Deprecated
    default void accept(Character t) {
        accept(t.charValue());
    }

    default CharConsumer andThen(final CharConsumer after) {
        Objects.requireNonNull(after);
        return new CharConsumer() { // from class: it.unimi.dsi.fastutil.chars.CharConsumer$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.chars.CharConsumer
            public final void accept(char c) {
                CharConsumer.lambda$andThen$0(CharConsumer.this, after, c);
            }
        };
    }

    static /* synthetic */ void lambda$andThen$0(CharConsumer _this, CharConsumer after, char t) {
        _this.accept(t);
        after.accept(t);
    }

    @Override // java.util.function.IntConsumer
    default CharConsumer andThen(IntConsumer after) {
        CharConsumer charConsumer$$ExternalSyntheticLambda1;
        if (after instanceof CharConsumer) {
            charConsumer$$ExternalSyntheticLambda1 = (CharConsumer) after;
        } else {
            after.getClass();
            charConsumer$$ExternalSyntheticLambda1 = new CharConsumer$$ExternalSyntheticLambda1(after);
        }
        return andThen(charConsumer$$ExternalSyntheticLambda1);
    }

    @Override // java.util.function.Consumer
    @Deprecated
    default Consumer<Character> andThen(Consumer<? super Character> after) {
        return super.andThen(after);
    }
}
