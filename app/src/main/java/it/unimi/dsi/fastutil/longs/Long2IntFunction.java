package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import java.util.function.LongToIntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Long2IntFunction extends Function<Long, Integer>, LongToIntFunction {
    int get(long j);

    @Override // java.util.function.LongToIntFunction
    default int applyAsInt(long operand) {
        return get(operand);
    }

    default int put(long key, int value) {
        throw new UnsupportedOperationException();
    }

    default int getOrDefault(long key, int defaultValue) {
        int v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default int remove(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer put(Long key, Integer value) {
        long k = key.longValue();
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
        long k = ((Long) key).longValue();
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
        long k = ((Long) key).longValue();
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
        long k = ((Long) key).longValue();
        if (containsKey(k)) {
            return Integer.valueOf(remove(k));
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

    default void defaultReturnValue(int rv) {
        throw new UnsupportedOperationException();
    }

    default int defaultReturnValue() {
        return 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Integer> compose(java.util.function.Function<? super T, ? extends Long> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Long, T> andThen(java.util.function.Function<? super Integer, ? extends T> after) {
        return super.andThen(after);
    }

    default Long2ByteFunction andThenByte(final Int2ByteFunction after) {
        return new Long2ByteFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.longs.Long2ByteFunction
            public final byte get(long j) {
                byte b;
                b = after.get(Long2IntFunction.this.get(j));
                return b;
            }
        };
    }

    default Byte2IntFunction composeByte(final Byte2LongFunction before) {
        return new Byte2IntFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.bytes.Byte2IntFunction
            public final int get(byte b) {
                int i;
                i = Long2IntFunction.this.get(before.get(b));
                return i;
            }
        };
    }

    default Long2ShortFunction andThenShort(final Int2ShortFunction after) {
        return new Long2ShortFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.longs.Long2ShortFunction
            public final short get(long j) {
                short s;
                s = after.get(Long2IntFunction.this.get(j));
                return s;
            }
        };
    }

    default Short2IntFunction composeShort(final Short2LongFunction before) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = Long2IntFunction.this.get(before.get(s));
                return i;
            }
        };
    }

    default Long2IntFunction andThenInt(final Int2IntFunction after) {
        return new Long2IntFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.longs.Long2IntFunction
            public final int get(long j) {
                int i;
                i = after.get(Long2IntFunction.this.get(j));
                return i;
            }
        };
    }

    default Int2IntFunction composeInt(final Int2LongFunction before) {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                int i2;
                i2 = Long2IntFunction.this.get(before.get(i));
                return i2;
            }
        };
    }

    default Long2LongFunction andThenLong(final Int2LongFunction after) {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                long j2;
                j2 = after.get(Long2IntFunction.this.get(j));
                return j2;
            }
        };
    }

    default Long2IntFunction composeLong(final Long2LongFunction before) {
        return new Long2IntFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.longs.Long2IntFunction
            public final int get(long j) {
                int i;
                i = Long2IntFunction.this.get(before.get(j));
                return i;
            }
        };
    }

    default Long2CharFunction andThenChar(final Int2CharFunction after) {
        return new Long2CharFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.longs.Long2CharFunction
            public final char get(long j) {
                char c;
                c = after.get(Long2IntFunction.this.get(j));
                return c;
            }
        };
    }

    default Char2IntFunction composeChar(final Char2LongFunction before) {
        return new Char2IntFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.chars.Char2IntFunction
            public final int get(char c) {
                int i;
                i = Long2IntFunction.this.get(before.get(c));
                return i;
            }
        };
    }

    default Long2FloatFunction andThenFloat(final Int2FloatFunction after) {
        return new Long2FloatFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.longs.Long2FloatFunction
            public final float get(long j) {
                float f;
                f = after.get(Long2IntFunction.this.get(j));
                return f;
            }
        };
    }

    default Float2IntFunction composeFloat(final Float2LongFunction before) {
        return new Float2IntFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.floats.Float2IntFunction
            public final int get(float f) {
                int i;
                i = Long2IntFunction.this.get(before.get(f));
                return i;
            }
        };
    }

    default Long2DoubleFunction andThenDouble(final Int2DoubleFunction after) {
        return new Long2DoubleFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.longs.Long2DoubleFunction
            public final double get(long j) {
                double d;
                d = after.get(Long2IntFunction.this.get(j));
                return d;
            }
        };
    }

    default Double2IntFunction composeDouble(final Double2LongFunction before) {
        return new Double2IntFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.doubles.Double2IntFunction
            public final int get(double d) {
                int i;
                i = Long2IntFunction.this.get(before.get(d));
                return i;
            }
        };
    }

    default <T> Long2ObjectFunction<T> andThenObject(final Int2ObjectFunction<? extends T> after) {
        return new Long2ObjectFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
            public final Object get(long j) {
                Object obj;
                obj = after.get(Long2IntFunction.this.get(j));
                return obj;
            }
        };
    }

    default <T> Object2IntFunction<T> composeObject(final Object2LongFunction<? super T> before) {
        return new Object2IntFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = Long2IntFunction.this.get(before.getLong(obj));
                return i;
            }
        };
    }

    default <T> Long2ReferenceFunction<T> andThenReference(final Int2ReferenceFunction<? extends T> after) {
        return new Long2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.longs.Long2ReferenceFunction
            public final Object get(long j) {
                Object obj;
                obj = after.get(Long2IntFunction.this.get(j));
                return obj;
            }
        };
    }

    default <T> Reference2IntFunction<T> composeReference(final Reference2LongFunction<? super T> before) {
        return new Reference2IntFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2IntFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Reference2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = Long2IntFunction.this.get(before.getLong(obj));
                return i;
            }
        };
    }
}
