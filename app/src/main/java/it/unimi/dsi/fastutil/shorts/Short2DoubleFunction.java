package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2ObjectFunction;
import it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Reference2ShortFunction;
import java.util.function.IntToDoubleFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Short2DoubleFunction extends Function<Short, Double>, IntToDoubleFunction {
    double get(short s);

    @Override // java.util.function.IntToDoubleFunction
    @Deprecated
    default double applyAsDouble(int operand) {
        return get(SafeMath.safeIntToShort(operand));
    }

    default double put(short key, double value) {
        throw new UnsupportedOperationException();
    }

    default double getOrDefault(short key, double defaultValue) {
        double v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default double remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double put(Short key, Double value) {
        short k = key.shortValue();
        boolean containsKey = containsKey(k);
        double v = put(k, value.doubleValue());
        if (containsKey) {
            return Double.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double get(Object key) {
        if (key == null) {
            return null;
        }
        short k = ((Short) key).shortValue();
        double v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Double.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        short k = ((Short) key).shortValue();
        double v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Double.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double remove(Object key) {
        if (key == null) {
            return null;
        }
        short k = ((Short) key).shortValue();
        if (containsKey(k)) {
            return Double.valueOf(remove(k));
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

    default void defaultReturnValue(double rv) {
        throw new UnsupportedOperationException();
    }

    default double defaultReturnValue() {
        return 0.0d;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Double> compose(java.util.function.Function<? super T, ? extends Short> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Short, T> andThen(java.util.function.Function<? super Double, ? extends T> after) {
        return super.andThen(after);
    }

    default Short2ByteFunction andThenByte(final Double2ByteFunction after) {
        return new Short2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.shorts.Short2ByteFunction
            public final byte get(short s) {
                byte b;
                b = after.get(Short2DoubleFunction.this.get(s));
                return b;
            }
        };
    }

    default Byte2DoubleFunction composeByte(final Byte2ShortFunction before) {
        return new Byte2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction
            public final double get(byte b) {
                double d;
                d = Short2DoubleFunction.this.get(before.get(b));
                return d;
            }
        };
    }

    default Short2ShortFunction andThenShort(final Double2ShortFunction after) {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                short s2;
                s2 = after.get(Short2DoubleFunction.this.get(s));
                return s2;
            }
        };
    }

    default Short2DoubleFunction composeShort(final Short2ShortFunction before) {
        return new Short2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.shorts.Short2DoubleFunction
            public final double get(short s) {
                double d;
                d = Short2DoubleFunction.this.get(before.get(s));
                return d;
            }
        };
    }

    default Short2IntFunction andThenInt(final Double2IntFunction after) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = after.get(Short2DoubleFunction.this.get(s));
                return i;
            }
        };
    }

    default Int2DoubleFunction composeInt(final Int2ShortFunction before) {
        return new Int2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.ints.Int2DoubleFunction
            public final double get(int i) {
                double d;
                d = Short2DoubleFunction.this.get(before.get(i));
                return d;
            }
        };
    }

    default Short2LongFunction andThenLong(final Double2LongFunction after) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = after.get(Short2DoubleFunction.this.get(s));
                return j;
            }
        };
    }

    default Long2DoubleFunction composeLong(final Long2ShortFunction before) {
        return new Long2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.longs.Long2DoubleFunction
            public final double get(long j) {
                double d;
                d = Short2DoubleFunction.this.get(before.get(j));
                return d;
            }
        };
    }

    default Short2CharFunction andThenChar(final Double2CharFunction after) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = after.get(Short2DoubleFunction.this.get(s));
                return c;
            }
        };
    }

    default Char2DoubleFunction composeChar(final Char2ShortFunction before) {
        return new Char2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.chars.Char2DoubleFunction
            public final double get(char c) {
                double d;
                d = Short2DoubleFunction.this.get(before.get(c));
                return d;
            }
        };
    }

    default Short2FloatFunction andThenFloat(final Double2FloatFunction after) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = after.get(Short2DoubleFunction.this.get(s));
                return f;
            }
        };
    }

    default Float2DoubleFunction composeFloat(final Float2ShortFunction before) {
        return new Float2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.floats.Float2DoubleFunction
            public final double get(float f) {
                double d;
                d = Short2DoubleFunction.this.get(before.get(f));
                return d;
            }
        };
    }

    default Short2DoubleFunction andThenDouble(final Double2DoubleFunction after) {
        return new Short2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.shorts.Short2DoubleFunction
            public final double get(short s) {
                double d;
                d = after.get(Short2DoubleFunction.this.get(s));
                return d;
            }
        };
    }

    default Double2DoubleFunction composeDouble(final Double2ShortFunction before) {
        return new Double2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.doubles.Double2DoubleFunction
            public final double get(double d) {
                double d2;
                d2 = Short2DoubleFunction.this.get(before.get(d));
                return d2;
            }
        };
    }

    default <T> Short2ObjectFunction<T> andThenObject(final Double2ObjectFunction<? extends T> after) {
        return new Short2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.shorts.Short2ObjectFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2DoubleFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Object2DoubleFunction<T> composeObject(final Object2ShortFunction<? super T> before) {
        return new Object2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.objects.Object2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = Short2DoubleFunction.this.get(before.getShort(obj));
                return d;
            }
        };
    }

    default <T> Short2ReferenceFunction<T> andThenReference(final Double2ReferenceFunction<? extends T> after) {
        return new Short2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2DoubleFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Reference2DoubleFunction<T> composeReference(final Reference2ShortFunction<? super T> before) {
        return new Reference2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2DoubleFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.objects.Reference2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = Short2DoubleFunction.this.get(before.getShort(obj));
                return d;
            }
        };
    }
}
