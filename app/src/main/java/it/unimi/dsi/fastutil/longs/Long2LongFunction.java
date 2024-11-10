package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Long2LongFunction extends Function<Long, Long>, java.util.function.LongUnaryOperator {
    long get(long j);

    @Override // java.util.function.LongUnaryOperator
    default long applyAsLong(long operand) {
        return get(operand);
    }

    default long put(long key, long value) {
        throw new UnsupportedOperationException();
    }

    default long getOrDefault(long key, long defaultValue) {
        long v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default long remove(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long put(Long key, Long value) {
        long k = key.longValue();
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
        long k = ((Long) key).longValue();
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
        long k = ((Long) key).longValue();
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
        long k = ((Long) key).longValue();
        if (containsKey(k)) {
            return Long.valueOf(remove(k));
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

    default void defaultReturnValue(long rv) {
        throw new UnsupportedOperationException();
    }

    default long defaultReturnValue() {
        return 0L;
    }

    static Long2LongFunction identity() {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                return Long2LongFunction.lambda$identity$0(j);
            }
        };
    }

    static /* synthetic */ long lambda$identity$0(long k) {
        return k;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Long> compose(java.util.function.Function<? super T, ? extends Long> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Long, T> andThen(java.util.function.Function<? super Long, ? extends T> after) {
        return super.andThen(after);
    }

    default Long2ByteFunction andThenByte(final Long2ByteFunction after) {
        return new Long2ByteFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.longs.Long2ByteFunction
            public final byte get(long j) {
                byte b;
                b = after.get(Long2LongFunction.this.get(j));
                return b;
            }
        };
    }

    default Byte2LongFunction composeByte(final Byte2LongFunction before) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = Long2LongFunction.this.get(before.get(b));
                return j;
            }
        };
    }

    default Long2ShortFunction andThenShort(final Long2ShortFunction after) {
        return new Long2ShortFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.longs.Long2ShortFunction
            public final short get(long j) {
                short s;
                s = after.get(Long2LongFunction.this.get(j));
                return s;
            }
        };
    }

    default Short2LongFunction composeShort(final Short2LongFunction before) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = Long2LongFunction.this.get(before.get(s));
                return j;
            }
        };
    }

    default Long2IntFunction andThenInt(final Long2IntFunction after) {
        return new Long2IntFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.longs.Long2IntFunction
            public final int get(long j) {
                int i;
                i = after.get(Long2LongFunction.this.get(j));
                return i;
            }
        };
    }

    default Int2LongFunction composeInt(final Int2LongFunction before) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = Long2LongFunction.this.get(before.get(i));
                return j;
            }
        };
    }

    default Long2LongFunction andThenLong(final Long2LongFunction after) {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                long j2;
                j2 = after.get(Long2LongFunction.this.get(j));
                return j2;
            }
        };
    }

    default Long2LongFunction composeLong(final Long2LongFunction before) {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda18
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                long j2;
                j2 = Long2LongFunction.this.get(before.get(j));
                return j2;
            }
        };
    }

    default Long2CharFunction andThenChar(final Long2CharFunction after) {
        return new Long2CharFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.longs.Long2CharFunction
            public final char get(long j) {
                char c;
                c = after.get(Long2LongFunction.this.get(j));
                return c;
            }
        };
    }

    default Char2LongFunction composeChar(final Char2LongFunction before) {
        return new Char2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.chars.Char2LongFunction
            public final long get(char c) {
                long j;
                j = Long2LongFunction.this.get(before.get(c));
                return j;
            }
        };
    }

    default Long2FloatFunction andThenFloat(final Long2FloatFunction after) {
        return new Long2FloatFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.longs.Long2FloatFunction
            public final float get(long j) {
                float f;
                f = after.get(Long2LongFunction.this.get(j));
                return f;
            }
        };
    }

    default Float2LongFunction composeFloat(final Float2LongFunction before) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = Long2LongFunction.this.get(before.get(f));
                return j;
            }
        };
    }

    default Long2DoubleFunction andThenDouble(final Long2DoubleFunction after) {
        return new Long2DoubleFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.longs.Long2DoubleFunction
            public final double get(long j) {
                double d;
                d = after.get(Long2LongFunction.this.get(j));
                return d;
            }
        };
    }

    default Double2LongFunction composeDouble(final Double2LongFunction before) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = Long2LongFunction.this.get(before.get(d));
                return j;
            }
        };
    }

    default <T> Long2ObjectFunction<T> andThenObject(final Long2ObjectFunction<? extends T> after) {
        return new Long2ObjectFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
            public final Object get(long j) {
                Object obj;
                obj = after.get(Long2LongFunction.this.get(j));
                return obj;
            }
        };
    }

    default <T> Object2LongFunction<T> composeObject(final Object2LongFunction<? super T> before) {
        return new Object2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Object2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Long2LongFunction.this.get(before.getLong(obj));
                return j;
            }
        };
    }

    default <T> Long2ReferenceFunction<T> andThenReference(final Long2ReferenceFunction<? extends T> after) {
        return new Long2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.longs.Long2ReferenceFunction
            public final Object get(long j) {
                Object obj;
                obj = after.get(Long2LongFunction.this.get(j));
                return obj;
            }
        };
    }

    default <T> Reference2LongFunction<T> composeReference(final Reference2LongFunction<? super T> before) {
        return new Reference2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2LongFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.objects.Reference2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Long2LongFunction.this.get(before.getLong(obj));
                return j;
            }
        };
    }
}
