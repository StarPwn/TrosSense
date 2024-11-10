package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import java.util.function.DoubleToIntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Double2IntFunction extends Function<Double, Integer>, DoubleToIntFunction {
    int get(double d);

    @Override // java.util.function.DoubleToIntFunction
    default int applyAsInt(double operand) {
        return get(operand);
    }

    default int put(double key, int value) {
        throw new UnsupportedOperationException();
    }

    default int getOrDefault(double key, int defaultValue) {
        int v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default int remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer put(Double key, Integer value) {
        double k = key.doubleValue();
        boolean containsKey = containsKey(k);
        int v = put(k, value.intValue());
        if (containsKey) {
            return Integer.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer get(Object key) {
        if (key == null) {
            return null;
        }
        double k = ((Double) key).doubleValue();
        int v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Integer.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        double k = ((Double) key).doubleValue();
        int v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Integer.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer remove(Object key) {
        if (key == null) {
            return null;
        }
        double k = ((Double) key).doubleValue();
        if (containsKey(k)) {
            return Integer.valueOf(remove(k));
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

    default void defaultReturnValue(int rv) {
        throw new UnsupportedOperationException();
    }

    default int defaultReturnValue() {
        return 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Integer> compose(java.util.function.Function<? super T, ? extends Double> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Double, T> andThen(java.util.function.Function<? super Integer, ? extends T> after) {
        return super.andThen(after);
    }

    default Double2ByteFunction andThenByte(final Int2ByteFunction after) {
        return new Double2ByteFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.doubles.Double2ByteFunction
            public final byte get(double d) {
                byte b;
                b = after.get(Double2IntFunction.this.get(d));
                return b;
            }
        };
    }

    default Byte2IntFunction composeByte(final Byte2DoubleFunction before) {
        return new Byte2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.bytes.Byte2IntFunction
            public final int get(byte b) {
                int i;
                i = Double2IntFunction.this.get(before.get(b));
                return i;
            }
        };
    }

    default Double2ShortFunction andThenShort(final Int2ShortFunction after) {
        return new Double2ShortFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.doubles.Double2ShortFunction
            public final short get(double d) {
                short s;
                s = after.get(Double2IntFunction.this.get(d));
                return s;
            }
        };
    }

    default Short2IntFunction composeShort(final Short2DoubleFunction before) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = Double2IntFunction.this.get(before.get(s));
                return i;
            }
        };
    }

    default Double2IntFunction andThenInt(final Int2IntFunction after) {
        return new Double2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.doubles.Double2IntFunction
            public final int get(double d) {
                int i;
                i = after.get(Double2IntFunction.this.get(d));
                return i;
            }
        };
    }

    default Int2IntFunction composeInt(final Int2DoubleFunction before) {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                int i2;
                i2 = Double2IntFunction.this.get(before.get(i));
                return i2;
            }
        };
    }

    default Double2LongFunction andThenLong(final Int2LongFunction after) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = after.get(Double2IntFunction.this.get(d));
                return j;
            }
        };
    }

    default Long2IntFunction composeLong(final Long2DoubleFunction before) {
        return new Long2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.longs.Long2IntFunction
            public final int get(long j) {
                int i;
                i = Double2IntFunction.this.get(before.get(j));
                return i;
            }
        };
    }

    default Double2CharFunction andThenChar(final Int2CharFunction after) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = after.get(Double2IntFunction.this.get(d));
                return c;
            }
        };
    }

    default Char2IntFunction composeChar(final Char2DoubleFunction before) {
        return new Char2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.chars.Char2IntFunction
            public final int get(char c) {
                int i;
                i = Double2IntFunction.this.get(before.get(c));
                return i;
            }
        };
    }

    default Double2FloatFunction andThenFloat(final Int2FloatFunction after) {
        return new Double2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.doubles.Double2FloatFunction
            public final float get(double d) {
                float f;
                f = after.get(Double2IntFunction.this.get(d));
                return f;
            }
        };
    }

    default Float2IntFunction composeFloat(final Float2DoubleFunction before) {
        return new Float2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.floats.Float2IntFunction
            public final int get(float f) {
                int i;
                i = Double2IntFunction.this.get(before.get(f));
                return i;
            }
        };
    }

    default Double2DoubleFunction andThenDouble(final Int2DoubleFunction after) {
        return new Double2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.doubles.Double2DoubleFunction
            public final double get(double d) {
                double d2;
                d2 = after.get(Double2IntFunction.this.get(d));
                return d2;
            }
        };
    }

    default Double2IntFunction composeDouble(final Double2DoubleFunction before) {
        return new Double2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.doubles.Double2IntFunction
            public final int get(double d) {
                int i;
                i = Double2IntFunction.this.get(before.get(d));
                return i;
            }
        };
    }

    default <T> Double2ObjectFunction<T> andThenObject(final Int2ObjectFunction<? extends T> after) {
        return new Double2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.doubles.Double2ObjectFunction
            public final Object get(double d) {
                Object obj;
                obj = after.get(Double2IntFunction.this.get(d));
                return obj;
            }
        };
    }

    default <T> Object2IntFunction<T> composeObject(final Object2DoubleFunction<? super T> before) {
        return new Object2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = Double2IntFunction.this.get(before.getDouble(obj));
                return i;
            }
        };
    }

    default <T> Double2ReferenceFunction<T> andThenReference(final Int2ReferenceFunction<? extends T> after) {
        return new Double2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction
            public final Object get(double d) {
                Object obj;
                obj = after.get(Double2IntFunction.this.get(d));
                return obj;
            }
        };
    }

    default <T> Reference2IntFunction<T> composeReference(final Reference2DoubleFunction<? super T> before) {
        return new Reference2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2IntFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.objects.Reference2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = Double2IntFunction.this.get(before.getDouble(obj));
                return i;
            }
        };
    }
}
