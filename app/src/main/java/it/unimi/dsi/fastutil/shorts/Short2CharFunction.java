package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.Char2CharFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.longs.Long2CharFunction;
import it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import it.unimi.dsi.fastutil.objects.Object2CharFunction;
import it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import it.unimi.dsi.fastutil.objects.Reference2ShortFunction;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Short2CharFunction extends Function<Short, Character>, IntUnaryOperator {
    char get(short s);

    @Override // java.util.function.IntUnaryOperator
    @Deprecated
    default int applyAsInt(int operand) {
        return get(SafeMath.safeIntToShort(operand));
    }

    default char put(short key, char value) {
        throw new UnsupportedOperationException();
    }

    default char getOrDefault(short key, char defaultValue) {
        char v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default char remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character put(Short key, Character value) {
        short k = key.shortValue();
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
        short k = ((Short) key).shortValue();
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
        short k = ((Short) key).shortValue();
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
        short k = ((Short) key).shortValue();
        if (containsKey(k)) {
            return Character.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(short key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Short) key).shortValue());
    }

    default void defaultReturnValue(char rv) {
        throw new UnsupportedOperationException();
    }

    default char defaultReturnValue() {
        return (char) 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Character> compose(java.util.function.Function<? super T, ? extends Short> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Short, T> andThen(java.util.function.Function<? super Character, ? extends T> after) {
        return super.andThen(after);
    }

    default Short2ByteFunction andThenByte(final Char2ByteFunction after) {
        return new Short2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.shorts.Short2ByteFunction
            public final byte get(short s) {
                byte b;
                b = after.get(Short2CharFunction.this.get(s));
                return b;
            }
        };
    }

    default Byte2CharFunction composeByte(final Byte2ShortFunction before) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = Short2CharFunction.this.get(before.get(b));
                return c;
            }
        };
    }

    default Short2ShortFunction andThenShort(final Char2ShortFunction after) {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                short s2;
                s2 = after.get(Short2CharFunction.this.get(s));
                return s2;
            }
        };
    }

    default Short2CharFunction composeShort(final Short2ShortFunction before) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = Short2CharFunction.this.get(before.get(s));
                return c;
            }
        };
    }

    default Short2IntFunction andThenInt(final Char2IntFunction after) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = after.get(Short2CharFunction.this.get(s));
                return i;
            }
        };
    }

    default Int2CharFunction composeInt(final Int2ShortFunction before) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = Short2CharFunction.this.get(before.get(i));
                return c;
            }
        };
    }

    default Short2LongFunction andThenLong(final Char2LongFunction after) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = after.get(Short2CharFunction.this.get(s));
                return j;
            }
        };
    }

    default Long2CharFunction composeLong(final Long2ShortFunction before) {
        return new Long2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.longs.Long2CharFunction
            public final char get(long j) {
                char c;
                c = Short2CharFunction.this.get(before.get(j));
                return c;
            }
        };
    }

    default Short2CharFunction andThenChar(final Char2CharFunction after) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = after.get(Short2CharFunction.this.get(s));
                return c;
            }
        };
    }

    default Char2CharFunction composeChar(final Char2ShortFunction before) {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                char c2;
                c2 = Short2CharFunction.this.get(before.get(c));
                return c2;
            }
        };
    }

    default Short2FloatFunction andThenFloat(final Char2FloatFunction after) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = after.get(Short2CharFunction.this.get(s));
                return f;
            }
        };
    }

    default Float2CharFunction composeFloat(final Float2ShortFunction before) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = Short2CharFunction.this.get(before.get(f));
                return c;
            }
        };
    }

    default Short2DoubleFunction andThenDouble(final Char2DoubleFunction after) {
        return new Short2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.shorts.Short2DoubleFunction
            public final double get(short s) {
                double d;
                d = after.get(Short2CharFunction.this.get(s));
                return d;
            }
        };
    }

    default Double2CharFunction composeDouble(final Double2ShortFunction before) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = Short2CharFunction.this.get(before.get(d));
                return c;
            }
        };
    }

    default <T> Short2ObjectFunction<T> andThenObject(final Char2ObjectFunction<? extends T> after) {
        return new Short2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.shorts.Short2ObjectFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2CharFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Object2CharFunction<T> composeObject(final Object2ShortFunction<? super T> before) {
        return new Object2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.Object2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Short2CharFunction.this.get(before.getShort(obj));
                return c;
            }
        };
    }

    default <T> Short2ReferenceFunction<T> andThenReference(final Char2ReferenceFunction<? extends T> after) {
        return new Short2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2CharFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Reference2CharFunction<T> composeReference(final Reference2ShortFunction<? super T> before) {
        return new Reference2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2CharFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Reference2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Short2CharFunction.this.get(before.getShort(obj));
                return c;
            }
        };
    }
}
