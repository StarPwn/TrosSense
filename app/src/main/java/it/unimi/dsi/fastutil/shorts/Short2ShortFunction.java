package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import it.unimi.dsi.fastutil.objects.Reference2ShortFunction;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Short2ShortFunction extends Function<Short, Short>, IntUnaryOperator {
    short get(short s);

    @Override // java.util.function.IntUnaryOperator
    @Deprecated
    default int applyAsInt(int operand) {
        return get(SafeMath.safeIntToShort(operand));
    }

    default short put(short key, short value) {
        throw new UnsupportedOperationException();
    }

    default short getOrDefault(short key, short defaultValue) {
        short v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default short remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short put(Short key, Short value) {
        short k = key.shortValue();
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
        short k = ((Short) key).shortValue();
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
        short k = ((Short) key).shortValue();
        short v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Short.valueOf(v) : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short remove(Object key) {
        if (key == null) {
            return null;
        }
        short k = ((Short) key).shortValue();
        if (containsKey(k)) {
            return Short.valueOf(remove(k));
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

    default void defaultReturnValue(short rv) {
        throw new UnsupportedOperationException();
    }

    default short defaultReturnValue() {
        return (short) 0;
    }

    static Short2ShortFunction identity() {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                return Short2ShortFunction.lambda$identity$0(s);
            }
        };
    }

    static /* synthetic */ short lambda$identity$0(short k) {
        return k;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Short> compose(java.util.function.Function<? super T, ? extends Short> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Short, T> andThen(java.util.function.Function<? super Short, ? extends T> after) {
        return super.andThen(after);
    }

    default Short2ByteFunction andThenByte(final Short2ByteFunction after) {
        return new Short2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.shorts.Short2ByteFunction
            public final byte get(short s) {
                byte b;
                b = after.get(Short2ShortFunction.this.get(s));
                return b;
            }
        };
    }

    default Byte2ShortFunction composeByte(final Byte2ShortFunction before) {
        return new Byte2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ShortFunction
            public final short get(byte b) {
                short s;
                s = Short2ShortFunction.this.get(before.get(b));
                return s;
            }
        };
    }

    default Short2ShortFunction andThenShort(final Short2ShortFunction after) {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                short s2;
                s2 = after.get(Short2ShortFunction.this.get(s));
                return s2;
            }
        };
    }

    default Short2ShortFunction composeShort(final Short2ShortFunction before) {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                short s2;
                s2 = Short2ShortFunction.this.get(before.get(s));
                return s2;
            }
        };
    }

    default Short2IntFunction andThenInt(final Short2IntFunction after) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = after.get(Short2ShortFunction.this.get(s));
                return i;
            }
        };
    }

    default Int2ShortFunction composeInt(final Int2ShortFunction before) {
        return new Int2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.ints.Int2ShortFunction
            public final short get(int i) {
                short s;
                s = Short2ShortFunction.this.get(before.get(i));
                return s;
            }
        };
    }

    default Short2LongFunction andThenLong(final Short2LongFunction after) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = after.get(Short2ShortFunction.this.get(s));
                return j;
            }
        };
    }

    default Long2ShortFunction composeLong(final Long2ShortFunction before) {
        return new Long2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.longs.Long2ShortFunction
            public final short get(long j) {
                short s;
                s = Short2ShortFunction.this.get(before.get(j));
                return s;
            }
        };
    }

    default Short2CharFunction andThenChar(final Short2CharFunction after) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = after.get(Short2ShortFunction.this.get(s));
                return c;
            }
        };
    }

    default Char2ShortFunction composeChar(final Char2ShortFunction before) {
        return new Char2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.chars.Char2ShortFunction
            public final short get(char c) {
                short s;
                s = Short2ShortFunction.this.get(before.get(c));
                return s;
            }
        };
    }

    default Short2FloatFunction andThenFloat(final Short2FloatFunction after) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = after.get(Short2ShortFunction.this.get(s));
                return f;
            }
        };
    }

    default Float2ShortFunction composeFloat(final Float2ShortFunction before) {
        return new Float2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.floats.Float2ShortFunction
            public final short get(float f) {
                short s;
                s = Short2ShortFunction.this.get(before.get(f));
                return s;
            }
        };
    }

    default Short2DoubleFunction andThenDouble(final Short2DoubleFunction after) {
        return new Short2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.shorts.Short2DoubleFunction
            public final double get(short s) {
                double d;
                d = after.get(Short2ShortFunction.this.get(s));
                return d;
            }
        };
    }

    default Double2ShortFunction composeDouble(final Double2ShortFunction before) {
        return new Double2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.doubles.Double2ShortFunction
            public final short get(double d) {
                short s;
                s = Short2ShortFunction.this.get(before.get(d));
                return s;
            }
        };
    }

    default <T> Short2ObjectFunction<T> andThenObject(final Short2ObjectFunction<? extends T> after) {
        return new Short2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda18
            @Override // it.unimi.dsi.fastutil.shorts.Short2ObjectFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2ShortFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Object2ShortFunction<T> composeObject(final Object2ShortFunction<? super T> before) {
        return new Object2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.objects.Object2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = Short2ShortFunction.this.get(before.getShort(obj));
                return s;
            }
        };
    }

    default <T> Short2ReferenceFunction<T> andThenReference(final Short2ReferenceFunction<? extends T> after) {
        return new Short2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2ShortFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Reference2ShortFunction<T> composeReference(final Reference2ShortFunction<? super T> before) {
        return new Reference2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ShortFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.objects.Reference2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = Short2ShortFunction.this.get(before.getShort(obj));
                return s;
            }
        };
    }
}
