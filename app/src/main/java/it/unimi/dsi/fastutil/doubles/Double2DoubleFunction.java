package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import java.util.function.DoubleUnaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Double2DoubleFunction extends Function<Double, Double>, DoubleUnaryOperator {
    double get(double d);

    @Override // java.util.function.DoubleUnaryOperator
    default double applyAsDouble(double operand) {
        return get(operand);
    }

    default double put(double key, double value) {
        throw new UnsupportedOperationException();
    }

    default double getOrDefault(double key, double defaultValue) {
        double v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default double remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double put(Double key, Double value) {
        double k = key.doubleValue();
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
        double k = ((Double) key).doubleValue();
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
        double k = ((Double) key).doubleValue();
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
        double k = ((Double) key).doubleValue();
        if (containsKey(k)) {
            return Double.valueOf(remove(k));
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

    default void defaultReturnValue(double rv) {
        throw new UnsupportedOperationException();
    }

    default double defaultReturnValue() {
        return 0.0d;
    }

    static Double2DoubleFunction identity() {
        return new Double2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.doubles.Double2DoubleFunction
            public final double get(double d) {
                return Double2DoubleFunction.lambda$identity$0(d);
            }
        };
    }

    static /* synthetic */ double lambda$identity$0(double k) {
        return k;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Double> compose(java.util.function.Function<? super T, ? extends Double> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Double, T> andThen(java.util.function.Function<? super Double, ? extends T> after) {
        return super.andThen(after);
    }

    default Double2ByteFunction andThenByte(final Double2ByteFunction after) {
        return new Double2ByteFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.doubles.Double2ByteFunction
            public final byte get(double d) {
                byte b;
                b = after.get(Double2DoubleFunction.this.get(d));
                return b;
            }
        };
    }

    default Byte2DoubleFunction composeByte(final Byte2DoubleFunction before) {
        return new Byte2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction
            public final double get(byte b) {
                double d;
                d = Double2DoubleFunction.this.get(before.get(b));
                return d;
            }
        };
    }

    default Double2ShortFunction andThenShort(final Double2ShortFunction after) {
        return new Double2ShortFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.doubles.Double2ShortFunction
            public final short get(double d) {
                short s;
                s = after.get(Double2DoubleFunction.this.get(d));
                return s;
            }
        };
    }

    default Short2DoubleFunction composeShort(final Short2DoubleFunction before) {
        return new Short2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.shorts.Short2DoubleFunction
            public final double get(short s) {
                double d;
                d = Double2DoubleFunction.this.get(before.get(s));
                return d;
            }
        };
    }

    default Double2IntFunction andThenInt(final Double2IntFunction after) {
        return new Double2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.doubles.Double2IntFunction
            public final int get(double d) {
                int i;
                i = after.get(Double2DoubleFunction.this.get(d));
                return i;
            }
        };
    }

    default Int2DoubleFunction composeInt(final Int2DoubleFunction before) {
        return new Int2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda18
            @Override // it.unimi.dsi.fastutil.ints.Int2DoubleFunction
            public final double get(int i) {
                double d;
                d = Double2DoubleFunction.this.get(before.get(i));
                return d;
            }
        };
    }

    default Double2LongFunction andThenLong(final Double2LongFunction after) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = after.get(Double2DoubleFunction.this.get(d));
                return j;
            }
        };
    }

    default Long2DoubleFunction composeLong(final Long2DoubleFunction before) {
        return new Long2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.longs.Long2DoubleFunction
            public final double get(long j) {
                double d;
                d = Double2DoubleFunction.this.get(before.get(j));
                return d;
            }
        };
    }

    default Double2CharFunction andThenChar(final Double2CharFunction after) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = after.get(Double2DoubleFunction.this.get(d));
                return c;
            }
        };
    }

    default Char2DoubleFunction composeChar(final Char2DoubleFunction before) {
        return new Char2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.chars.Char2DoubleFunction
            public final double get(char c) {
                double d;
                d = Double2DoubleFunction.this.get(before.get(c));
                return d;
            }
        };
    }

    default Double2FloatFunction andThenFloat(final Double2FloatFunction after) {
        return new Double2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.doubles.Double2FloatFunction
            public final float get(double d) {
                float f;
                f = after.get(Double2DoubleFunction.this.get(d));
                return f;
            }
        };
    }

    default Float2DoubleFunction composeFloat(final Float2DoubleFunction before) {
        return new Float2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.floats.Float2DoubleFunction
            public final double get(float f) {
                double d;
                d = Double2DoubleFunction.this.get(before.get(f));
                return d;
            }
        };
    }

    default Double2DoubleFunction andThenDouble(final Double2DoubleFunction after) {
        return new Double2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.doubles.Double2DoubleFunction
            public final double get(double d) {
                double d2;
                d2 = after.get(Double2DoubleFunction.this.get(d));
                return d2;
            }
        };
    }

    default Double2DoubleFunction composeDouble(final Double2DoubleFunction before) {
        return new Double2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.doubles.Double2DoubleFunction
            public final double get(double d) {
                double d2;
                d2 = Double2DoubleFunction.this.get(before.get(d));
                return d2;
            }
        };
    }

    default <T> Double2ObjectFunction<T> andThenObject(final Double2ObjectFunction<? extends T> after) {
        return new Double2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.doubles.Double2ObjectFunction
            public final Object get(double d) {
                Object obj;
                obj = after.get(Double2DoubleFunction.this.get(d));
                return obj;
            }
        };
    }

    default <T> Object2DoubleFunction<T> composeObject(final Object2DoubleFunction<? super T> before) {
        return new Object2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.objects.Object2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = Double2DoubleFunction.this.get(before.getDouble(obj));
                return d;
            }
        };
    }

    default <T> Double2ReferenceFunction<T> andThenReference(final Double2ReferenceFunction<? extends T> after) {
        return new Double2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction
            public final Object get(double d) {
                Object obj;
                obj = after.get(Double2DoubleFunction.this.get(d));
                return obj;
            }
        };
    }

    default <T> Reference2DoubleFunction<T> composeReference(final Reference2DoubleFunction<? super T> before) {
        return new Reference2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2DoubleFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.objects.Reference2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = Double2DoubleFunction.this.get(before.getDouble(obj));
                return d;
            }
        };
    }
}
