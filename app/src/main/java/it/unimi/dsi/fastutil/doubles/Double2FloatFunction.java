package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import java.util.function.DoubleUnaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Double2FloatFunction extends Function<Double, Float>, DoubleUnaryOperator {
    float get(double d);

    @Override // java.util.function.DoubleUnaryOperator
    default double applyAsDouble(double operand) {
        return get(operand);
    }

    default float put(double key, float value) {
        throw new UnsupportedOperationException();
    }

    default float getOrDefault(double key, float defaultValue) {
        float v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default float remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float put(Double key, Float value) {
        double k = key.doubleValue();
        boolean containsKey = containsKey(k);
        float v = put(k, value.floatValue());
        if (containsKey) {
            return Float.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float get(Object key) {
        if (key == null) {
            return null;
        }
        double k = ((Double) key).doubleValue();
        float v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Float.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        double k = ((Double) key).doubleValue();
        float v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Float.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float remove(Object key) {
        if (key == null) {
            return null;
        }
        double k = ((Double) key).doubleValue();
        if (containsKey(k)) {
            return Float.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(double key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Double) key).doubleValue());
    }

    default void defaultReturnValue(float rv) {
        throw new UnsupportedOperationException();
    }

    default float defaultReturnValue() {
        return 0.0f;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Float> compose(java.util.function.Function<? super T, ? extends Double> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Double, T> andThen(java.util.function.Function<? super Float, ? extends T> after) {
        return super.andThen(after);
    }

    default Double2ByteFunction andThenByte(final Float2ByteFunction after) {
        return new Double2ByteFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.doubles.Double2ByteFunction
            public final byte get(double d) {
                byte b;
                b = after.get(Double2FloatFunction.this.get(d));
                return b;
            }
        };
    }

    default Byte2FloatFunction composeByte(final Byte2DoubleFunction before) {
        return new Byte2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.bytes.Byte2FloatFunction
            public final float get(byte b) {
                float f;
                f = Double2FloatFunction.this.get(before.get(b));
                return f;
            }
        };
    }

    default Double2ShortFunction andThenShort(final Float2ShortFunction after) {
        return new Double2ShortFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.doubles.Double2ShortFunction
            public final short get(double d) {
                short s;
                s = after.get(Double2FloatFunction.this.get(d));
                return s;
            }
        };
    }

    default Short2FloatFunction composeShort(final Short2DoubleFunction before) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = Double2FloatFunction.this.get(before.get(s));
                return f;
            }
        };
    }

    default Double2IntFunction andThenInt(final Float2IntFunction after) {
        return new Double2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.doubles.Double2IntFunction
            public final int get(double d) {
                int i;
                i = after.get(Double2FloatFunction.this.get(d));
                return i;
            }
        };
    }

    default Int2FloatFunction composeInt(final Int2DoubleFunction before) {
        return new Int2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.ints.Int2FloatFunction
            public final float get(int i) {
                float f;
                f = Double2FloatFunction.this.get(before.get(i));
                return f;
            }
        };
    }

    default Double2LongFunction andThenLong(final Float2LongFunction after) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = after.get(Double2FloatFunction.this.get(d));
                return j;
            }
        };
    }

    default Long2FloatFunction composeLong(final Long2DoubleFunction before) {
        return new Long2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.longs.Long2FloatFunction
            public final float get(long j) {
                float f;
                f = Double2FloatFunction.this.get(before.get(j));
                return f;
            }
        };
    }

    default Double2CharFunction andThenChar(final Float2CharFunction after) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = after.get(Double2FloatFunction.this.get(d));
                return c;
            }
        };
    }

    default Char2FloatFunction composeChar(final Char2DoubleFunction before) {
        return new Char2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.chars.Char2FloatFunction
            public final float get(char c) {
                float f;
                f = Double2FloatFunction.this.get(before.get(c));
                return f;
            }
        };
    }

    default Double2FloatFunction andThenFloat(final Float2FloatFunction after) {
        return new Double2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.doubles.Double2FloatFunction
            public final float get(double d) {
                float f;
                f = after.get(Double2FloatFunction.this.get(d));
                return f;
            }
        };
    }

    default Float2FloatFunction composeFloat(final Float2DoubleFunction before) {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                float f2;
                f2 = Double2FloatFunction.this.get(before.get(f));
                return f2;
            }
        };
    }

    default Double2DoubleFunction andThenDouble(final Float2DoubleFunction after) {
        return new Double2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.doubles.Double2DoubleFunction
            public final double get(double d) {
                double d2;
                d2 = after.get(Double2FloatFunction.this.get(d));
                return d2;
            }
        };
    }

    default Double2FloatFunction composeDouble(final Double2DoubleFunction before) {
        return new Double2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.doubles.Double2FloatFunction
            public final float get(double d) {
                float f;
                f = Double2FloatFunction.this.get(before.get(d));
                return f;
            }
        };
    }

    default <T> Double2ObjectFunction<T> andThenObject(final Float2ObjectFunction<? extends T> after) {
        return new Double2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.doubles.Double2ObjectFunction
            public final Object get(double d) {
                Object obj;
                obj = after.get(Double2FloatFunction.this.get(d));
                return obj;
            }
        };
    }

    default <T> Object2FloatFunction<T> composeObject(final Object2DoubleFunction<? super T> before) {
        return new Object2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.objects.Object2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = Double2FloatFunction.this.get(before.getDouble(obj));
                return f;
            }
        };
    }

    default <T> Double2ReferenceFunction<T> andThenReference(final Float2ReferenceFunction<? extends T> after) {
        return new Double2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction
            public final Object get(double d) {
                Object obj;
                obj = after.get(Double2FloatFunction.this.get(d));
                return obj;
            }
        };
    }

    default <T> Reference2FloatFunction<T> composeReference(final Reference2DoubleFunction<? super T> before) {
        return new Reference2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2FloatFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.Reference2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = Double2FloatFunction.this.get(before.getDouble(obj));
                return f;
            }
        };
    }
}
