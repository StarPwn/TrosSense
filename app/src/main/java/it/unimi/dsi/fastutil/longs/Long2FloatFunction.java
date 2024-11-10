package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import java.util.function.LongToDoubleFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Long2FloatFunction extends Function<Long, Float>, LongToDoubleFunction {
    float get(long j);

    @Override // java.util.function.LongToDoubleFunction
    default double applyAsDouble(long operand) {
        return get(operand);
    }

    default float put(long key, float value) {
        throw new UnsupportedOperationException();
    }

    default float getOrDefault(long key, float defaultValue) {
        float v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default float remove(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float put(Long key, Float value) {
        long k = key.longValue();
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
        long k = ((Long) key).longValue();
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
        long k = ((Long) key).longValue();
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
        long k = ((Long) key).longValue();
        if (containsKey(k)) {
            return Float.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(long key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Long) key).longValue());
    }

    default void defaultReturnValue(float rv) {
        throw new UnsupportedOperationException();
    }

    default float defaultReturnValue() {
        return 0.0f;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Float> compose(java.util.function.Function<? super T, ? extends Long> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Long, T> andThen(java.util.function.Function<? super Float, ? extends T> after) {
        return super.andThen(after);
    }

    default Long2ByteFunction andThenByte(final Float2ByteFunction after) {
        return new Long2ByteFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.longs.Long2ByteFunction
            public final byte get(long j) {
                byte b;
                b = after.get(Long2FloatFunction.this.get(j));
                return b;
            }
        };
    }

    default Byte2FloatFunction composeByte(final Byte2LongFunction before) {
        return new Byte2FloatFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.bytes.Byte2FloatFunction
            public final float get(byte b) {
                float f;
                f = Long2FloatFunction.this.get(before.get(b));
                return f;
            }
        };
    }

    default Long2ShortFunction andThenShort(final Float2ShortFunction after) {
        return new Long2ShortFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.longs.Long2ShortFunction
            public final short get(long j) {
                short s;
                s = after.get(Long2FloatFunction.this.get(j));
                return s;
            }
        };
    }

    default Short2FloatFunction composeShort(final Short2LongFunction before) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = Long2FloatFunction.this.get(before.get(s));
                return f;
            }
        };
    }

    default Long2IntFunction andThenInt(final Float2IntFunction after) {
        return new Long2IntFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.longs.Long2IntFunction
            public final int get(long j) {
                int i;
                i = after.get(Long2FloatFunction.this.get(j));
                return i;
            }
        };
    }

    default Int2FloatFunction composeInt(final Int2LongFunction before) {
        return new Int2FloatFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.ints.Int2FloatFunction
            public final float get(int i) {
                float f;
                f = Long2FloatFunction.this.get(before.get(i));
                return f;
            }
        };
    }

    default Long2LongFunction andThenLong(final Float2LongFunction after) {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                long j2;
                j2 = after.get(Long2FloatFunction.this.get(j));
                return j2;
            }
        };
    }

    default Long2FloatFunction composeLong(final Long2LongFunction before) {
        return new Long2FloatFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.longs.Long2FloatFunction
            public final float get(long j) {
                float f;
                f = Long2FloatFunction.this.get(before.get(j));
                return f;
            }
        };
    }

    default Long2CharFunction andThenChar(final Float2CharFunction after) {
        return new Long2CharFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.longs.Long2CharFunction
            public final char get(long j) {
                char c;
                c = after.get(Long2FloatFunction.this.get(j));
                return c;
            }
        };
    }

    default Char2FloatFunction composeChar(final Char2LongFunction before) {
        return new Char2FloatFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.chars.Char2FloatFunction
            public final float get(char c) {
                float f;
                f = Long2FloatFunction.this.get(before.get(c));
                return f;
            }
        };
    }

    default Long2FloatFunction andThenFloat(final Float2FloatFunction after) {
        return new Long2FloatFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.longs.Long2FloatFunction
            public final float get(long j) {
                float f;
                f = after.get(Long2FloatFunction.this.get(j));
                return f;
            }
        };
    }

    default Float2FloatFunction composeFloat(final Float2LongFunction before) {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                float f2;
                f2 = Long2FloatFunction.this.get(before.get(f));
                return f2;
            }
        };
    }

    default Long2DoubleFunction andThenDouble(final Float2DoubleFunction after) {
        return new Long2DoubleFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.longs.Long2DoubleFunction
            public final double get(long j) {
                double d;
                d = after.get(Long2FloatFunction.this.get(j));
                return d;
            }
        };
    }

    default Double2FloatFunction composeDouble(final Double2LongFunction before) {
        return new Double2FloatFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.doubles.Double2FloatFunction
            public final float get(double d) {
                float f;
                f = Long2FloatFunction.this.get(before.get(d));
                return f;
            }
        };
    }

    default <T> Long2ObjectFunction<T> andThenObject(final Float2ObjectFunction<? extends T> after) {
        return new Long2ObjectFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
            public final Object get(long j) {
                Object obj;
                obj = after.get(Long2FloatFunction.this.get(j));
                return obj;
            }
        };
    }

    default <T> Object2FloatFunction<T> composeObject(final Object2LongFunction<? super T> before) {
        return new Object2FloatFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.objects.Object2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = Long2FloatFunction.this.get(before.getLong(obj));
                return f;
            }
        };
    }

    default <T> Long2ReferenceFunction<T> andThenReference(final Float2ReferenceFunction<? extends T> after) {
        return new Long2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.longs.Long2ReferenceFunction
            public final Object get(long j) {
                Object obj;
                obj = after.get(Long2FloatFunction.this.get(j));
                return obj;
            }
        };
    }

    default <T> Reference2FloatFunction<T> composeReference(final Reference2LongFunction<? super T> before) {
        return new Reference2FloatFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2FloatFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Reference2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = Long2FloatFunction.this.get(before.getLong(obj));
                return f;
            }
        };
    }
}
