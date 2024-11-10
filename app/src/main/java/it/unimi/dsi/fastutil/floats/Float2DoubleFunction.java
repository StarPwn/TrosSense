package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2ObjectFunction;
import it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
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
public interface Float2DoubleFunction extends Function<Float, Double>, DoubleUnaryOperator {
    double get(float f);

    @Override // java.util.function.DoubleUnaryOperator
    @Deprecated
    default double applyAsDouble(double operand) {
        return get(SafeMath.safeDoubleToFloat(operand));
    }

    default double put(float key, double value) {
        throw new UnsupportedOperationException();
    }

    default double getOrDefault(float key, double defaultValue) {
        double v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default double remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double put(Float key, Double value) {
        float k = key.floatValue();
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
        float k = ((Float) key).floatValue();
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
        float k = ((Float) key).floatValue();
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
        float k = ((Float) key).floatValue();
        if (containsKey(k)) {
            return Double.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(float key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Float) key).floatValue());
    }

    default void defaultReturnValue(double rv) {
        throw new UnsupportedOperationException();
    }

    default double defaultReturnValue() {
        return 0.0d;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Double> compose(java.util.function.Function<? super T, ? extends Float> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Float, T> andThen(java.util.function.Function<? super Double, ? extends T> after) {
        return super.andThen(after);
    }

    default Float2ByteFunction andThenByte(final Double2ByteFunction after) {
        return new Float2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.floats.Float2ByteFunction
            public final byte get(float f) {
                byte b;
                b = after.get(Float2DoubleFunction.this.get(f));
                return b;
            }
        };
    }

    default Byte2DoubleFunction composeByte(final Byte2FloatFunction before) {
        return new Byte2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction
            public final double get(byte b) {
                double d;
                d = Float2DoubleFunction.this.get(before.get(b));
                return d;
            }
        };
    }

    default Float2ShortFunction andThenShort(final Double2ShortFunction after) {
        return new Float2ShortFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.floats.Float2ShortFunction
            public final short get(float f) {
                short s;
                s = after.get(Float2DoubleFunction.this.get(f));
                return s;
            }
        };
    }

    default Short2DoubleFunction composeShort(final Short2FloatFunction before) {
        return new Short2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.shorts.Short2DoubleFunction
            public final double get(short s) {
                double d;
                d = Float2DoubleFunction.this.get(before.get(s));
                return d;
            }
        };
    }

    default Float2IntFunction andThenInt(final Double2IntFunction after) {
        return new Float2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.floats.Float2IntFunction
            public final int get(float f) {
                int i;
                i = after.get(Float2DoubleFunction.this.get(f));
                return i;
            }
        };
    }

    default Int2DoubleFunction composeInt(final Int2FloatFunction before) {
        return new Int2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.ints.Int2DoubleFunction
            public final double get(int i) {
                double d;
                d = Float2DoubleFunction.this.get(before.get(i));
                return d;
            }
        };
    }

    default Float2LongFunction andThenLong(final Double2LongFunction after) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = after.get(Float2DoubleFunction.this.get(f));
                return j;
            }
        };
    }

    default Long2DoubleFunction composeLong(final Long2FloatFunction before) {
        return new Long2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.longs.Long2DoubleFunction
            public final double get(long j) {
                double d;
                d = Float2DoubleFunction.this.get(before.get(j));
                return d;
            }
        };
    }

    default Float2CharFunction andThenChar(final Double2CharFunction after) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = after.get(Float2DoubleFunction.this.get(f));
                return c;
            }
        };
    }

    default Char2DoubleFunction composeChar(final Char2FloatFunction before) {
        return new Char2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.chars.Char2DoubleFunction
            public final double get(char c) {
                double d;
                d = Float2DoubleFunction.this.get(before.get(c));
                return d;
            }
        };
    }

    default Float2FloatFunction andThenFloat(final Double2FloatFunction after) {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                float f2;
                f2 = after.get(Float2DoubleFunction.this.get(f));
                return f2;
            }
        };
    }

    default Float2DoubleFunction composeFloat(final Float2FloatFunction before) {
        return new Float2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.floats.Float2DoubleFunction
            public final double get(float f) {
                double d;
                d = Float2DoubleFunction.this.get(before.get(f));
                return d;
            }
        };
    }

    default Float2DoubleFunction andThenDouble(final Double2DoubleFunction after) {
        return new Float2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.floats.Float2DoubleFunction
            public final double get(float f) {
                double d;
                d = after.get(Float2DoubleFunction.this.get(f));
                return d;
            }
        };
    }

    default Double2DoubleFunction composeDouble(final Double2FloatFunction before) {
        return new Double2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.doubles.Double2DoubleFunction
            public final double get(double d) {
                double d2;
                d2 = Float2DoubleFunction.this.get(before.get(d));
                return d2;
            }
        };
    }

    default <T> Float2ObjectFunction<T> andThenObject(final Double2ObjectFunction<? extends T> after) {
        return new Float2ObjectFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.floats.Float2ObjectFunction
            public final Object get(float f) {
                Object obj;
                obj = after.get(Float2DoubleFunction.this.get(f));
                return obj;
            }
        };
    }

    default <T> Object2DoubleFunction<T> composeObject(final Object2FloatFunction<? super T> before) {
        return new Object2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.objects.Object2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = Float2DoubleFunction.this.get(before.getFloat(obj));
                return d;
            }
        };
    }

    default <T> Float2ReferenceFunction<T> andThenReference(final Double2ReferenceFunction<? extends T> after) {
        return new Float2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.floats.Float2ReferenceFunction
            public final Object get(float f) {
                Object obj;
                obj = after.get(Float2DoubleFunction.this.get(f));
                return obj;
            }
        };
    }

    default <T> Reference2DoubleFunction<T> composeReference(final Reference2FloatFunction<? super T> before) {
        return new Reference2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2DoubleFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.objects.Reference2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = Float2DoubleFunction.this.get(before.getFloat(obj));
                return d;
            }
        };
    }
}
