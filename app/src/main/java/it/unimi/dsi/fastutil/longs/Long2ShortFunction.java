package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import it.unimi.dsi.fastutil.objects.Reference2ShortFunction;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;
import it.unimi.dsi.fastutil.shorts.Short2ShortFunction;
import java.util.function.LongToIntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Long2ShortFunction extends Function<Long, Short>, LongToIntFunction {
    short get(long j);

    @Override // java.util.function.LongToIntFunction
    default int applyAsInt(long operand) {
        return get(operand);
    }

    default short put(long key, short value) {
        throw new UnsupportedOperationException();
    }

    default short getOrDefault(long key, short defaultValue) {
        short v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default short remove(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short put(Long key, Short value) {
        long k = key.longValue();
        boolean containsKey = containsKey(k);
        short v = put(k, value.shortValue());
        if (containsKey) {
            return Short.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short get(Object key) {
        if (key == null) {
            return null;
        }
        long k = ((Long) key).longValue();
        short v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Short.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        long k = ((Long) key).longValue();
        short v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Short.valueOf(v) : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short remove(Object key) {
        if (key == null) {
            return null;
        }
        long k = ((Long) key).longValue();
        if (containsKey(k)) {
            return Short.valueOf(remove(k));
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

    default void defaultReturnValue(short rv) {
        throw new UnsupportedOperationException();
    }

    default short defaultReturnValue() {
        return (short) 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Short> compose(java.util.function.Function<? super T, ? extends Long> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Long, T> andThen(java.util.function.Function<? super Short, ? extends T> after) {
        return super.andThen(after);
    }

    default Long2ByteFunction andThenByte(final Short2ByteFunction after) {
        return new Long2ByteFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.longs.Long2ByteFunction
            public final byte get(long j) {
                byte b;
                b = after.get(Long2ShortFunction.this.get(j));
                return b;
            }
        };
    }

    default Byte2ShortFunction composeByte(final Byte2LongFunction before) {
        return new Byte2ShortFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ShortFunction
            public final short get(byte b) {
                short s;
                s = Long2ShortFunction.this.get(before.get(b));
                return s;
            }
        };
    }

    default Long2ShortFunction andThenShort(final Short2ShortFunction after) {
        return new Long2ShortFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.longs.Long2ShortFunction
            public final short get(long j) {
                short s;
                s = after.get(Long2ShortFunction.this.get(j));
                return s;
            }
        };
    }

    default Short2ShortFunction composeShort(final Short2LongFunction before) {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                short s2;
                s2 = Long2ShortFunction.this.get(before.get(s));
                return s2;
            }
        };
    }

    default Long2IntFunction andThenInt(final Short2IntFunction after) {
        return new Long2IntFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.longs.Long2IntFunction
            public final int get(long j) {
                int i;
                i = after.get(Long2ShortFunction.this.get(j));
                return i;
            }
        };
    }

    default Int2ShortFunction composeInt(final Int2LongFunction before) {
        return new Int2ShortFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.ints.Int2ShortFunction
            public final short get(int i) {
                short s;
                s = Long2ShortFunction.this.get(before.get(i));
                return s;
            }
        };
    }

    default Long2LongFunction andThenLong(final Short2LongFunction after) {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                long j2;
                j2 = after.get(Long2ShortFunction.this.get(j));
                return j2;
            }
        };
    }

    default Long2ShortFunction composeLong(final Long2LongFunction before) {
        return new Long2ShortFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.longs.Long2ShortFunction
            public final short get(long j) {
                short s;
                s = Long2ShortFunction.this.get(before.get(j));
                return s;
            }
        };
    }

    default Long2CharFunction andThenChar(final Short2CharFunction after) {
        return new Long2CharFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.longs.Long2CharFunction
            public final char get(long j) {
                char c;
                c = after.get(Long2ShortFunction.this.get(j));
                return c;
            }
        };
    }

    default Char2ShortFunction composeChar(final Char2LongFunction before) {
        return new Char2ShortFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.chars.Char2ShortFunction
            public final short get(char c) {
                short s;
                s = Long2ShortFunction.this.get(before.get(c));
                return s;
            }
        };
    }

    default Long2FloatFunction andThenFloat(final Short2FloatFunction after) {
        return new Long2FloatFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.longs.Long2FloatFunction
            public final float get(long j) {
                float f;
                f = after.get(Long2ShortFunction.this.get(j));
                return f;
            }
        };
    }

    default Float2ShortFunction composeFloat(final Float2LongFunction before) {
        return new Float2ShortFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.floats.Float2ShortFunction
            public final short get(float f) {
                short s;
                s = Long2ShortFunction.this.get(before.get(f));
                return s;
            }
        };
    }

    default Long2DoubleFunction andThenDouble(final Short2DoubleFunction after) {
        return new Long2DoubleFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.longs.Long2DoubleFunction
            public final double get(long j) {
                double d;
                d = after.get(Long2ShortFunction.this.get(j));
                return d;
            }
        };
    }

    default Double2ShortFunction composeDouble(final Double2LongFunction before) {
        return new Double2ShortFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.doubles.Double2ShortFunction
            public final short get(double d) {
                short s;
                s = Long2ShortFunction.this.get(before.get(d));
                return s;
            }
        };
    }

    default <T> Long2ObjectFunction<T> andThenObject(final Short2ObjectFunction<? extends T> after) {
        return new Long2ObjectFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
            public final Object get(long j) {
                Object obj;
                obj = after.get(Long2ShortFunction.this.get(j));
                return obj;
            }
        };
    }

    default <T> Object2ShortFunction<T> composeObject(final Object2LongFunction<? super T> before) {
        return new Object2ShortFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.objects.Object2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = Long2ShortFunction.this.get(before.getLong(obj));
                return s;
            }
        };
    }

    default <T> Long2ReferenceFunction<T> andThenReference(final Short2ReferenceFunction<? extends T> after) {
        return new Long2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.longs.Long2ReferenceFunction
            public final Object get(long j) {
                Object obj;
                obj = after.get(Long2ShortFunction.this.get(j));
                return obj;
            }
        };
    }

    default <T> Reference2ShortFunction<T> composeReference(final Reference2LongFunction<? super T> before) {
        return new Reference2ShortFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ShortFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.objects.Reference2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = Long2ShortFunction.this.get(before.getLong(obj));
                return s;
            }
        };
    }
}
