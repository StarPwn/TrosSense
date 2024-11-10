package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import it.unimi.dsi.fastutil.longs.Long2CharFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.longs.Long2LongFunction;
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import java.util.function.DoubleToLongFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Double2LongFunction extends Function<Double, Long>, DoubleToLongFunction {
    long get(double d);

    @Override // java.util.function.DoubleToLongFunction
    default long applyAsLong(double operand) {
        return get(operand);
    }

    default long put(double key, long value) {
        throw new UnsupportedOperationException();
    }

    default long getOrDefault(double key, long defaultValue) {
        long v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default long remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long put(Double key, Long value) {
        double k = key.doubleValue();
        boolean containsKey = containsKey(k);
        long v = put(k, value.longValue());
        if (containsKey) {
            return Long.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long get(Object key) {
        if (key == null) {
            return null;
        }
        double k = ((Double) key).doubleValue();
        long v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Long.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        double k = ((Double) key).doubleValue();
        long v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Long.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long remove(Object key) {
        if (key == null) {
            return null;
        }
        double k = ((Double) key).doubleValue();
        if (containsKey(k)) {
            return Long.valueOf(remove(k));
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

    default void defaultReturnValue(long rv) {
        throw new UnsupportedOperationException();
    }

    default long defaultReturnValue() {
        return 0L;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Long> compose(java.util.function.Function<? super T, ? extends Double> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Double, T> andThen(java.util.function.Function<? super Long, ? extends T> after) {
        return super.andThen(after);
    }

    default Double2ByteFunction andThenByte(final Long2ByteFunction after) {
        return new Double2ByteFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.doubles.Double2ByteFunction
            public final byte get(double d) {
                byte b;
                b = after.get(Double2LongFunction.this.get(d));
                return b;
            }
        };
    }

    default Byte2LongFunction composeByte(final Byte2DoubleFunction before) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = Double2LongFunction.this.get(before.get(b));
                return j;
            }
        };
    }

    default Double2ShortFunction andThenShort(final Long2ShortFunction after) {
        return new Double2ShortFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.doubles.Double2ShortFunction
            public final short get(double d) {
                short s;
                s = after.get(Double2LongFunction.this.get(d));
                return s;
            }
        };
    }

    default Short2LongFunction composeShort(final Short2DoubleFunction before) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = Double2LongFunction.this.get(before.get(s));
                return j;
            }
        };
    }

    default Double2IntFunction andThenInt(final Long2IntFunction after) {
        return new Double2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.doubles.Double2IntFunction
            public final int get(double d) {
                int i;
                i = after.get(Double2LongFunction.this.get(d));
                return i;
            }
        };
    }

    default Int2LongFunction composeInt(final Int2DoubleFunction before) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = Double2LongFunction.this.get(before.get(i));
                return j;
            }
        };
    }

    default Double2LongFunction andThenLong(final Long2LongFunction after) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = after.get(Double2LongFunction.this.get(d));
                return j;
            }
        };
    }

    default Long2LongFunction composeLong(final Long2DoubleFunction before) {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                long j2;
                j2 = Double2LongFunction.this.get(before.get(j));
                return j2;
            }
        };
    }

    default Double2CharFunction andThenChar(final Long2CharFunction after) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = after.get(Double2LongFunction.this.get(d));
                return c;
            }
        };
    }

    default Char2LongFunction composeChar(final Char2DoubleFunction before) {
        return new Char2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.chars.Char2LongFunction
            public final long get(char c) {
                long j;
                j = Double2LongFunction.this.get(before.get(c));
                return j;
            }
        };
    }

    default Double2FloatFunction andThenFloat(final Long2FloatFunction after) {
        return new Double2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.doubles.Double2FloatFunction
            public final float get(double d) {
                float f;
                f = after.get(Double2LongFunction.this.get(d));
                return f;
            }
        };
    }

    default Float2LongFunction composeFloat(final Float2DoubleFunction before) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = Double2LongFunction.this.get(before.get(f));
                return j;
            }
        };
    }

    default Double2DoubleFunction andThenDouble(final Long2DoubleFunction after) {
        return new Double2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.doubles.Double2DoubleFunction
            public final double get(double d) {
                double d2;
                d2 = after.get(Double2LongFunction.this.get(d));
                return d2;
            }
        };
    }

    default Double2LongFunction composeDouble(final Double2DoubleFunction before) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = Double2LongFunction.this.get(before.get(d));
                return j;
            }
        };
    }

    default <T> Double2ObjectFunction<T> andThenObject(final Long2ObjectFunction<? extends T> after) {
        return new Double2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.doubles.Double2ObjectFunction
            public final Object get(double d) {
                Object obj;
                obj = after.get(Double2LongFunction.this.get(d));
                return obj;
            }
        };
    }

    default <T> Object2LongFunction<T> composeObject(final Object2DoubleFunction<? super T> before) {
        return new Object2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.objects.Object2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Double2LongFunction.this.get(before.getDouble(obj));
                return j;
            }
        };
    }

    default <T> Double2ReferenceFunction<T> andThenReference(final Long2ReferenceFunction<? extends T> after) {
        return new Double2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction
            public final Object get(double d) {
                Object obj;
                obj = after.get(Double2LongFunction.this.get(d));
                return obj;
            }
        };
    }

    default <T> Reference2LongFunction<T> composeReference(final Reference2DoubleFunction<? super T> before) {
        return new Reference2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2LongFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.objects.Reference2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Double2LongFunction.this.get(before.getDouble(obj));
                return j;
            }
        };
    }
}
