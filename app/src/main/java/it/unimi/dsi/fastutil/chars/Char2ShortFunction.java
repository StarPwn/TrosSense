package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
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
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;
import it.unimi.dsi.fastutil.shorts.Short2ShortFunction;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Char2ShortFunction extends Function<Character, Short>, IntUnaryOperator {
    short get(char c);

    @Override // java.util.function.IntUnaryOperator
    @Deprecated
    default int applyAsInt(int operand) {
        return get(SafeMath.safeIntToChar(operand));
    }

    default short put(char key, short value) {
        throw new UnsupportedOperationException();
    }

    default short getOrDefault(char key, short defaultValue) {
        short v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default short remove(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short put(Character key, Short value) {
        char k = key.charValue();
        boolean containsKey = containsKey(k);
        short v = put(k, value.shortValue());
        if (containsKey) {
            return Short.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short get(Object key) {
        if (key == null) {
            return null;
        }
        char k = ((Character) key).charValue();
        short v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Short.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        char k = ((Character) key).charValue();
        short v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Short.valueOf(v) : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short remove(Object key) {
        if (key == null) {
            return null;
        }
        char k = ((Character) key).charValue();
        if (containsKey(k)) {
            return Short.valueOf(remove(k));
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

    default void defaultReturnValue(short rv) {
        throw new UnsupportedOperationException();
    }

    default short defaultReturnValue() {
        return (short) 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Short> compose(java.util.function.Function<? super T, ? extends Character> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Character, T> andThen(java.util.function.Function<? super Short, ? extends T> after) {
        return super.andThen(after);
    }

    default Char2ByteFunction andThenByte(final Short2ByteFunction after) {
        return new Char2ByteFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.chars.Char2ByteFunction
            public final byte get(char c) {
                byte b;
                b = after.get(Char2ShortFunction.this.get(c));
                return b;
            }
        };
    }

    default Byte2ShortFunction composeByte(final Byte2CharFunction before) {
        return new Byte2ShortFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ShortFunction
            public final short get(byte b) {
                short s;
                s = Char2ShortFunction.this.get(before.get(b));
                return s;
            }
        };
    }

    default Char2ShortFunction andThenShort(final Short2ShortFunction after) {
        return new Char2ShortFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.chars.Char2ShortFunction
            public final short get(char c) {
                short s;
                s = after.get(Char2ShortFunction.this.get(c));
                return s;
            }
        };
    }

    default Short2ShortFunction composeShort(final Short2CharFunction before) {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                short s2;
                s2 = Char2ShortFunction.this.get(before.get(s));
                return s2;
            }
        };
    }

    default Char2IntFunction andThenInt(final Short2IntFunction after) {
        return new Char2IntFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.chars.Char2IntFunction
            public final int get(char c) {
                int i;
                i = after.get(Char2ShortFunction.this.get(c));
                return i;
            }
        };
    }

    default Int2ShortFunction composeInt(final Int2CharFunction before) {
        return new Int2ShortFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.ints.Int2ShortFunction
            public final short get(int i) {
                short s;
                s = Char2ShortFunction.this.get(before.get(i));
                return s;
            }
        };
    }

    default Char2LongFunction andThenLong(final Short2LongFunction after) {
        return new Char2LongFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.chars.Char2LongFunction
            public final long get(char c) {
                long j;
                j = after.get(Char2ShortFunction.this.get(c));
                return j;
            }
        };
    }

    default Long2ShortFunction composeLong(final Long2CharFunction before) {
        return new Long2ShortFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.longs.Long2ShortFunction
            public final short get(long j) {
                short s;
                s = Char2ShortFunction.this.get(before.get(j));
                return s;
            }
        };
    }

    default Char2CharFunction andThenChar(final Short2CharFunction after) {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                char c2;
                c2 = after.get(Char2ShortFunction.this.get(c));
                return c2;
            }
        };
    }

    default Char2ShortFunction composeChar(final Char2CharFunction before) {
        return new Char2ShortFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.chars.Char2ShortFunction
            public final short get(char c) {
                short s;
                s = Char2ShortFunction.this.get(before.get(c));
                return s;
            }
        };
    }

    default Char2FloatFunction andThenFloat(final Short2FloatFunction after) {
        return new Char2FloatFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.chars.Char2FloatFunction
            public final float get(char c) {
                float f;
                f = after.get(Char2ShortFunction.this.get(c));
                return f;
            }
        };
    }

    default Float2ShortFunction composeFloat(final Float2CharFunction before) {
        return new Float2ShortFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.floats.Float2ShortFunction
            public final short get(float f) {
                short s;
                s = Char2ShortFunction.this.get(before.get(f));
                return s;
            }
        };
    }

    default Char2DoubleFunction andThenDouble(final Short2DoubleFunction after) {
        return new Char2DoubleFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.chars.Char2DoubleFunction
            public final double get(char c) {
                double d;
                d = after.get(Char2ShortFunction.this.get(c));
                return d;
            }
        };
    }

    default Double2ShortFunction composeDouble(final Double2CharFunction before) {
        return new Double2ShortFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.doubles.Double2ShortFunction
            public final short get(double d) {
                short s;
                s = Char2ShortFunction.this.get(before.get(d));
                return s;
            }
        };
    }

    default <T> Char2ObjectFunction<T> andThenObject(final Short2ObjectFunction<? extends T> after) {
        return new Char2ObjectFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.chars.Char2ObjectFunction
            public final Object get(char c) {
                Object obj;
                obj = after.get(Char2ShortFunction.this.get(c));
                return obj;
            }
        };
    }

    default <T> Object2ShortFunction<T> composeObject(final Object2CharFunction<? super T> before) {
        return new Object2ShortFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.Object2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = Char2ShortFunction.this.get(before.getChar(obj));
                return s;
            }
        };
    }

    default <T> Char2ReferenceFunction<T> andThenReference(final Short2ReferenceFunction<? extends T> after) {
        return new Char2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.chars.Char2ReferenceFunction
            public final Object get(char c) {
                Object obj;
                obj = after.get(Char2ShortFunction.this.get(c));
                return obj;
            }
        };
    }

    default <T> Reference2ShortFunction<T> composeReference(final Reference2CharFunction<? super T> before) {
        return new Reference2ShortFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ShortFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.objects.Reference2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = Char2ShortFunction.this.get(before.getChar(obj));
                return s;
            }
        };
    }
}
