package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.longs.Long2CharFunction;
import it.unimi.dsi.fastutil.objects.Object2CharFunction;
import it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Char2CharFunction extends Function<Character, Character>, IntUnaryOperator {
    char get(char c);

    @Override // java.util.function.IntUnaryOperator
    @Deprecated
    default int applyAsInt(int operand) {
        return get(SafeMath.safeIntToChar(operand));
    }

    default char put(char key, char value) {
        throw new UnsupportedOperationException();
    }

    default char getOrDefault(char key, char defaultValue) {
        char v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default char remove(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character put(Character key, Character value) {
        char k = key.charValue();
        boolean containsKey = containsKey(k);
        char v = put(k, value.charValue());
        if (containsKey) {
            return Character.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character get(Object key) {
        if (key == null) {
            return null;
        }
        char k = ((Character) key).charValue();
        char v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Character.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        char k = ((Character) key).charValue();
        char v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Character.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character remove(Object key) {
        if (key == null) {
            return null;
        }
        char k = ((Character) key).charValue();
        if (containsKey(k)) {
            return Character.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(char key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Character) key).charValue());
    }

    default void defaultReturnValue(char rv) {
        throw new UnsupportedOperationException();
    }

    default char defaultReturnValue() {
        return (char) 0;
    }

    static Char2CharFunction identity() {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                return Char2CharFunction.lambda$identity$0(c);
            }
        };
    }

    static /* synthetic */ char lambda$identity$0(char k) {
        return k;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Character> compose(java.util.function.Function<? super T, ? extends Character> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Character, T> andThen(java.util.function.Function<? super Character, ? extends T> after) {
        return super.andThen(after);
    }

    default Char2ByteFunction andThenByte(final Char2ByteFunction after) {
        return new Char2ByteFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.chars.Char2ByteFunction
            public final byte get(char c) {
                byte b;
                b = after.get(Char2CharFunction.this.get(c));
                return b;
            }
        };
    }

    default Byte2CharFunction composeByte(final Byte2CharFunction before) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = Char2CharFunction.this.get(before.get(b));
                return c;
            }
        };
    }

    default Char2ShortFunction andThenShort(final Char2ShortFunction after) {
        return new Char2ShortFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.chars.Char2ShortFunction
            public final short get(char c) {
                short s;
                s = after.get(Char2CharFunction.this.get(c));
                return s;
            }
        };
    }

    default Short2CharFunction composeShort(final Short2CharFunction before) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = Char2CharFunction.this.get(before.get(s));
                return c;
            }
        };
    }

    default Char2IntFunction andThenInt(final Char2IntFunction after) {
        return new Char2IntFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.chars.Char2IntFunction
            public final int get(char c) {
                int i;
                i = after.get(Char2CharFunction.this.get(c));
                return i;
            }
        };
    }

    default Int2CharFunction composeInt(final Int2CharFunction before) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = Char2CharFunction.this.get(before.get(i));
                return c;
            }
        };
    }

    default Char2LongFunction andThenLong(final Char2LongFunction after) {
        return new Char2LongFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.chars.Char2LongFunction
            public final long get(char c) {
                long j;
                j = after.get(Char2CharFunction.this.get(c));
                return j;
            }
        };
    }

    default Long2CharFunction composeLong(final Long2CharFunction before) {
        return new Long2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.longs.Long2CharFunction
            public final char get(long j) {
                char c;
                c = Char2CharFunction.this.get(before.get(j));
                return c;
            }
        };
    }

    default Char2CharFunction andThenChar(final Char2CharFunction after) {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                char c2;
                c2 = after.get(Char2CharFunction.this.get(c));
                return c2;
            }
        };
    }

    default Char2CharFunction composeChar(final Char2CharFunction before) {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                char c2;
                c2 = Char2CharFunction.this.get(before.get(c));
                return c2;
            }
        };
    }

    default Char2FloatFunction andThenFloat(final Char2FloatFunction after) {
        return new Char2FloatFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.chars.Char2FloatFunction
            public final float get(char c) {
                float f;
                f = after.get(Char2CharFunction.this.get(c));
                return f;
            }
        };
    }

    default Float2CharFunction composeFloat(final Float2CharFunction before) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda18
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = Char2CharFunction.this.get(before.get(f));
                return c;
            }
        };
    }

    default Char2DoubleFunction andThenDouble(final Char2DoubleFunction after) {
        return new Char2DoubleFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.chars.Char2DoubleFunction
            public final double get(char c) {
                double d;
                d = after.get(Char2CharFunction.this.get(c));
                return d;
            }
        };
    }

    default Double2CharFunction composeDouble(final Double2CharFunction before) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = Char2CharFunction.this.get(before.get(d));
                return c;
            }
        };
    }

    default <T> Char2ObjectFunction<T> andThenObject(final Char2ObjectFunction<? extends T> after) {
        return new Char2ObjectFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.chars.Char2ObjectFunction
            public final Object get(char c) {
                Object obj;
                obj = after.get(Char2CharFunction.this.get(c));
                return obj;
            }
        };
    }

    default <T> Object2CharFunction<T> composeObject(final Object2CharFunction<? super T> before) {
        return new Object2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.objects.Object2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Char2CharFunction.this.get(before.getChar(obj));
                return c;
            }
        };
    }

    default <T> Char2ReferenceFunction<T> andThenReference(final Char2ReferenceFunction<? extends T> after) {
        return new Char2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.chars.Char2ReferenceFunction
            public final Object get(char c) {
                Object obj;
                obj = after.get(Char2CharFunction.this.get(c));
                return obj;
            }
        };
    }

    default <T> Reference2CharFunction<T> composeReference(final Reference2CharFunction<? super T> before) {
        return new Reference2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2CharFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.Reference2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Char2CharFunction.this.get(before.getChar(obj));
                return c;
            }
        };
    }
}
