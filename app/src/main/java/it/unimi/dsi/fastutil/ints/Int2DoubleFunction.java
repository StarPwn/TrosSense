package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
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
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import java.util.function.IntToDoubleFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Int2DoubleFunction extends Function<Integer, Double>, IntToDoubleFunction {
    double get(int i);

    @Override // java.util.function.IntToDoubleFunction
    default double applyAsDouble(int operand) {
        return get(operand);
    }

    default double put(int key, double value) {
        throw new UnsupportedOperationException();
    }

    default double getOrDefault(int key, double defaultValue) {
        double v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default double remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double put(Integer key, Double value) {
        int k = key.intValue();
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
        int k = ((Integer) key).intValue();
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
        int k = ((Integer) key).intValue();
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
        int k = ((Integer) key).intValue();
        if (containsKey(k)) {
            return Double.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(int key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Integer) key).intValue());
    }

    default void defaultReturnValue(double rv) {
        throw new UnsupportedOperationException();
    }

    default double defaultReturnValue() {
        return 0.0d;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Double> compose(java.util.function.Function<? super T, ? extends Integer> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Integer, T> andThen(java.util.function.Function<? super Double, ? extends T> after) {
        return super.andThen(after);
    }

    default Int2ByteFunction andThenByte(final Double2ByteFunction after) {
        return new Int2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.ints.Int2ByteFunction
            public final byte get(int i) {
                byte b;
                b = after.get(Int2DoubleFunction.this.get(i));
                return b;
            }
        };
    }

    default Byte2DoubleFunction composeByte(final Byte2IntFunction before) {
        return new Byte2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction
            public final double get(byte b) {
                double d;
                d = Int2DoubleFunction.this.get(before.get(b));
                return d;
            }
        };
    }

    default Int2ShortFunction andThenShort(final Double2ShortFunction after) {
        return new Int2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.ints.Int2ShortFunction
            public final short get(int i) {
                short s;
                s = after.get(Int2DoubleFunction.this.get(i));
                return s;
            }
        };
    }

    default Short2DoubleFunction composeShort(final Short2IntFunction before) {
        return new Short2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.shorts.Short2DoubleFunction
            public final double get(short s) {
                double d;
                d = Int2DoubleFunction.this.get(before.get(s));
                return d;
            }
        };
    }

    default Int2IntFunction andThenInt(final Double2IntFunction after) {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                int i2;
                i2 = after.get(Int2DoubleFunction.this.get(i));
                return i2;
            }
        };
    }

    default Int2DoubleFunction composeInt(final Int2IntFunction before) {
        return new Int2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.ints.Int2DoubleFunction
            public final double get(int i) {
                double d;
                d = Int2DoubleFunction.this.get(before.get(i));
                return d;
            }
        };
    }

    default Int2LongFunction andThenLong(final Double2LongFunction after) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = after.get(Int2DoubleFunction.this.get(i));
                return j;
            }
        };
    }

    default Long2DoubleFunction composeLong(final Long2IntFunction before) {
        return new Long2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.longs.Long2DoubleFunction
            public final double get(long j) {
                double d;
                d = Int2DoubleFunction.this.get(before.get(j));
                return d;
            }
        };
    }

    default Int2CharFunction andThenChar(final Double2CharFunction after) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = after.get(Int2DoubleFunction.this.get(i));
                return c;
            }
        };
    }

    default Char2DoubleFunction composeChar(final Char2IntFunction before) {
        return new Char2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.chars.Char2DoubleFunction
            public final double get(char c) {
                double d;
                d = Int2DoubleFunction.this.get(before.get(c));
                return d;
            }
        };
    }

    default Int2FloatFunction andThenFloat(final Double2FloatFunction after) {
        return new Int2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.ints.Int2FloatFunction
            public final float get(int i) {
                float f;
                f = after.get(Int2DoubleFunction.this.get(i));
                return f;
            }
        };
    }

    default Float2DoubleFunction composeFloat(final Float2IntFunction before) {
        return new Float2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.floats.Float2DoubleFunction
            public final double get(float f) {
                double d;
                d = Int2DoubleFunction.this.get(before.get(f));
                return d;
            }
        };
    }

    default Int2DoubleFunction andThenDouble(final Double2DoubleFunction after) {
        return new Int2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.ints.Int2DoubleFunction
            public final double get(int i) {
                double d;
                d = after.get(Int2DoubleFunction.this.get(i));
                return d;
            }
        };
    }

    default Double2DoubleFunction composeDouble(final Double2IntFunction before) {
        return new Double2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.doubles.Double2DoubleFunction
            public final double get(double d) {
                double d2;
                d2 = Int2DoubleFunction.this.get(before.get(d));
                return d2;
            }
        };
    }

    default <T> Int2ObjectFunction<T> andThenObject(final Double2ObjectFunction<? extends T> after) {
        return new Int2ObjectFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
            public final Object get(int i) {
                Object obj;
                obj = after.get(Int2DoubleFunction.this.get(i));
                return obj;
            }
        };
    }

    default <T> Object2DoubleFunction<T> composeObject(final Object2IntFunction<? super T> before) {
        return new Object2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.objects.Object2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = Int2DoubleFunction.this.get(before.getInt(obj));
                return d;
            }
        };
    }

    default <T> Int2ReferenceFunction<T> andThenReference(final Double2ReferenceFunction<? extends T> after) {
        return new Int2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.ints.Int2ReferenceFunction
            public final Object get(int i) {
                Object obj;
                obj = after.get(Int2DoubleFunction.this.get(i));
                return obj;
            }
        };
    }

    default <T> Reference2DoubleFunction<T> composeReference(final Reference2IntFunction<? super T> before) {
        return new Reference2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2DoubleFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.objects.Reference2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = Int2DoubleFunction.this.get(before.getInt(obj));
                return d;
            }
        };
    }
}
