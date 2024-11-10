package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
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
import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import java.util.function.DoubleToLongFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Float2LongFunction extends Function<Float, Long>, DoubleToLongFunction {
    long get(float f);

    @Override // java.util.function.DoubleToLongFunction
    @Deprecated
    default long applyAsLong(double operand) {
        return get(SafeMath.safeDoubleToFloat(operand));
    }

    default long put(float key, long value) {
        throw new UnsupportedOperationException();
    }

    default long getOrDefault(float key, long defaultValue) {
        long v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default long remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long put(Float key, Long value) {
        float k = key.floatValue();
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
        float k = ((Float) key).floatValue();
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
        float k = ((Float) key).floatValue();
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
        float k = ((Float) key).floatValue();
        if (containsKey(k)) {
            return Long.valueOf(remove(k));
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

    default void defaultReturnValue(long rv) {
        throw new UnsupportedOperationException();
    }

    default long defaultReturnValue() {
        return 0L;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Long> compose(java.util.function.Function<? super T, ? extends Float> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Float, T> andThen(java.util.function.Function<? super Long, ? extends T> after) {
        return super.andThen(after);
    }

    default Float2ByteFunction andThenByte(final Long2ByteFunction after) {
        return new Float2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.floats.Float2ByteFunction
            public final byte get(float f) {
                byte b;
                b = after.get(Float2LongFunction.this.get(f));
                return b;
            }
        };
    }

    default Byte2LongFunction composeByte(final Byte2FloatFunction before) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = Float2LongFunction.this.get(before.get(b));
                return j;
            }
        };
    }

    default Float2ShortFunction andThenShort(final Long2ShortFunction after) {
        return new Float2ShortFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.floats.Float2ShortFunction
            public final short get(float f) {
                short s;
                s = after.get(Float2LongFunction.this.get(f));
                return s;
            }
        };
    }

    default Short2LongFunction composeShort(final Short2FloatFunction before) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = Float2LongFunction.this.get(before.get(s));
                return j;
            }
        };
    }

    default Float2IntFunction andThenInt(final Long2IntFunction after) {
        return new Float2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.floats.Float2IntFunction
            public final int get(float f) {
                int i;
                i = after.get(Float2LongFunction.this.get(f));
                return i;
            }
        };
    }

    default Int2LongFunction composeInt(final Int2FloatFunction before) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = Float2LongFunction.this.get(before.get(i));
                return j;
            }
        };
    }

    default Float2LongFunction andThenLong(final Long2LongFunction after) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = after.get(Float2LongFunction.this.get(f));
                return j;
            }
        };
    }

    default Long2LongFunction composeLong(final Long2FloatFunction before) {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                long j2;
                j2 = Float2LongFunction.this.get(before.get(j));
                return j2;
            }
        };
    }

    default Float2CharFunction andThenChar(final Long2CharFunction after) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = after.get(Float2LongFunction.this.get(f));
                return c;
            }
        };
    }

    default Char2LongFunction composeChar(final Char2FloatFunction before) {
        return new Char2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.chars.Char2LongFunction
            public final long get(char c) {
                long j;
                j = Float2LongFunction.this.get(before.get(c));
                return j;
            }
        };
    }

    default Float2FloatFunction andThenFloat(final Long2FloatFunction after) {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                float f2;
                f2 = after.get(Float2LongFunction.this.get(f));
                return f2;
            }
        };
    }

    default Float2LongFunction composeFloat(final Float2FloatFunction before) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = Float2LongFunction.this.get(before.get(f));
                return j;
            }
        };
    }

    default Float2DoubleFunction andThenDouble(final Long2DoubleFunction after) {
        return new Float2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.floats.Float2DoubleFunction
            public final double get(float f) {
                double d;
                d = after.get(Float2LongFunction.this.get(f));
                return d;
            }
        };
    }

    default Double2LongFunction composeDouble(final Double2FloatFunction before) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = Float2LongFunction.this.get(before.get(d));
                return j;
            }
        };
    }

    default <T> Float2ObjectFunction<T> andThenObject(final Long2ObjectFunction<? extends T> after) {
        return new Float2ObjectFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.floats.Float2ObjectFunction
            public final Object get(float f) {
                Object obj;
                obj = after.get(Float2LongFunction.this.get(f));
                return obj;
            }
        };
    }

    default <T> Object2LongFunction<T> composeObject(final Object2FloatFunction<? super T> before) {
        return new Object2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.objects.Object2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Float2LongFunction.this.get(before.getFloat(obj));
                return j;
            }
        };
    }

    default <T> Float2ReferenceFunction<T> andThenReference(final Long2ReferenceFunction<? extends T> after) {
        return new Float2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.floats.Float2ReferenceFunction
            public final Object get(float f) {
                Object obj;
                obj = after.get(Float2LongFunction.this.get(f));
                return obj;
            }
        };
    }

    default <T> Reference2LongFunction<T> composeReference(final Reference2FloatFunction<? super T> before) {
        return new Reference2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2LongFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.objects.Reference2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Float2LongFunction.this.get(before.getFloat(obj));
                return j;
            }
        };
    }
}
