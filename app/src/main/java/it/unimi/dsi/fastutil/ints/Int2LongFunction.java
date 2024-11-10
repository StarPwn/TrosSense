package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import it.unimi.dsi.fastutil.longs.Long2CharFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.longs.Long2LongFunction;
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import java.util.function.IntToLongFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Int2LongFunction extends Function<Integer, Long>, IntToLongFunction {
    long get(int i);

    @Override // java.util.function.IntToLongFunction
    default long applyAsLong(int operand) {
        return get(operand);
    }

    default long put(int key, long value) {
        throw new UnsupportedOperationException();
    }

    default long getOrDefault(int key, long defaultValue) {
        long v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default long remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long put(Integer key, Long value) {
        int k = key.intValue();
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
        int k = ((Integer) key).intValue();
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
        int k = ((Integer) key).intValue();
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
        int k = ((Integer) key).intValue();
        if (containsKey(k)) {
            return Long.valueOf(remove(k));
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

    default void defaultReturnValue(long rv) {
        throw new UnsupportedOperationException();
    }

    default long defaultReturnValue() {
        return 0L;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Long> compose(java.util.function.Function<? super T, ? extends Integer> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Integer, T> andThen(java.util.function.Function<? super Long, ? extends T> after) {
        return super.andThen(after);
    }

    default Int2ByteFunction andThenByte(final Long2ByteFunction after) {
        return new Int2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.ints.Int2ByteFunction
            public final byte get(int i) {
                byte b;
                b = after.get(Int2LongFunction.this.get(i));
                return b;
            }
        };
    }

    default Byte2LongFunction composeByte(final Byte2IntFunction before) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = Int2LongFunction.this.get(before.get(b));
                return j;
            }
        };
    }

    default Int2ShortFunction andThenShort(final Long2ShortFunction after) {
        return new Int2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.ints.Int2ShortFunction
            public final short get(int i) {
                short s;
                s = after.get(Int2LongFunction.this.get(i));
                return s;
            }
        };
    }

    default Short2LongFunction composeShort(final Short2IntFunction before) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = Int2LongFunction.this.get(before.get(s));
                return j;
            }
        };
    }

    default Int2IntFunction andThenInt(final Long2IntFunction after) {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                int i2;
                i2 = after.get(Int2LongFunction.this.get(i));
                return i2;
            }
        };
    }

    default Int2LongFunction composeInt(final Int2IntFunction before) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = Int2LongFunction.this.get(before.get(i));
                return j;
            }
        };
    }

    default Int2LongFunction andThenLong(final Long2LongFunction after) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = after.get(Int2LongFunction.this.get(i));
                return j;
            }
        };
    }

    default Long2LongFunction composeLong(final Long2IntFunction before) {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                long j2;
                j2 = Int2LongFunction.this.get(before.get(j));
                return j2;
            }
        };
    }

    default Int2CharFunction andThenChar(final Long2CharFunction after) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = after.get(Int2LongFunction.this.get(i));
                return c;
            }
        };
    }

    default Char2LongFunction composeChar(final Char2IntFunction before) {
        return new Char2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.chars.Char2LongFunction
            public final long get(char c) {
                long j;
                j = Int2LongFunction.this.get(before.get(c));
                return j;
            }
        };
    }

    default Int2FloatFunction andThenFloat(final Long2FloatFunction after) {
        return new Int2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.ints.Int2FloatFunction
            public final float get(int i) {
                float f;
                f = after.get(Int2LongFunction.this.get(i));
                return f;
            }
        };
    }

    default Float2LongFunction composeFloat(final Float2IntFunction before) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = Int2LongFunction.this.get(before.get(f));
                return j;
            }
        };
    }

    default Int2DoubleFunction andThenDouble(final Long2DoubleFunction after) {
        return new Int2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.ints.Int2DoubleFunction
            public final double get(int i) {
                double d;
                d = after.get(Int2LongFunction.this.get(i));
                return d;
            }
        };
    }

    default Double2LongFunction composeDouble(final Double2IntFunction before) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = Int2LongFunction.this.get(before.get(d));
                return j;
            }
        };
    }

    default <T> Int2ObjectFunction<T> andThenObject(final Long2ObjectFunction<? extends T> after) {
        return new Int2ObjectFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
            public final Object get(int i) {
                Object obj;
                obj = after.get(Int2LongFunction.this.get(i));
                return obj;
            }
        };
    }

    default <T> Object2LongFunction<T> composeObject(final Object2IntFunction<? super T> before) {
        return new Object2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Object2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Int2LongFunction.this.get(before.getInt(obj));
                return j;
            }
        };
    }

    default <T> Int2ReferenceFunction<T> andThenReference(final Long2ReferenceFunction<? extends T> after) {
        return new Int2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.ints.Int2ReferenceFunction
            public final Object get(int i) {
                Object obj;
                obj = after.get(Int2LongFunction.this.get(i));
                return obj;
            }
        };
    }

    default <T> Reference2LongFunction<T> composeReference(final Reference2IntFunction<? super T> before) {
        return new Reference2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2LongFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.objects.Reference2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Int2LongFunction.this.get(before.getInt(obj));
                return j;
            }
        };
    }
}
