package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2ByteFunction;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import java.util.function.LongToIntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Long2ByteFunction extends Function<Long, Byte>, LongToIntFunction {
    byte get(long j);

    @Override // java.util.function.LongToIntFunction
    default int applyAsInt(long operand) {
        return get(operand);
    }

    default byte put(long key, byte value) {
        throw new UnsupportedOperationException();
    }

    default byte getOrDefault(long key, byte defaultValue) {
        byte v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default byte remove(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte put(Long key, Byte value) {
        long k = key.longValue();
        boolean containsKey = containsKey(k);
        byte v = put(k, value.byteValue());
        if (containsKey) {
            return Byte.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte get(Object key) {
        if (key == null) {
            return null;
        }
        long k = ((Long) key).longValue();
        byte v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Byte.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        long k = ((Long) key).longValue();
        byte v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Byte.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte remove(Object key) {
        if (key == null) {
            return null;
        }
        long k = ((Long) key).longValue();
        if (containsKey(k)) {
            return Byte.valueOf(remove(k));
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

    default void defaultReturnValue(byte rv) {
        throw new UnsupportedOperationException();
    }

    default byte defaultReturnValue() {
        return (byte) 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Byte> compose(java.util.function.Function<? super T, ? extends Long> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Long, T> andThen(java.util.function.Function<? super Byte, ? extends T> after) {
        return super.andThen(after);
    }

    default Long2ByteFunction andThenByte(final Byte2ByteFunction after) {
        return new Long2ByteFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.longs.Long2ByteFunction
            public final byte get(long j) {
                byte b;
                b = after.get(Long2ByteFunction.this.get(j));
                return b;
            }
        };
    }

    default Byte2ByteFunction composeByte(final Byte2LongFunction before) {
        return new Byte2ByteFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ByteFunction
            public final byte get(byte b) {
                byte b2;
                b2 = Long2ByteFunction.this.get(before.get(b));
                return b2;
            }
        };
    }

    default Long2ShortFunction andThenShort(final Byte2ShortFunction after) {
        return new Long2ShortFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.longs.Long2ShortFunction
            public final short get(long j) {
                short s;
                s = after.get(Long2ByteFunction.this.get(j));
                return s;
            }
        };
    }

    default Short2ByteFunction composeShort(final Short2LongFunction before) {
        return new Short2ByteFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.shorts.Short2ByteFunction
            public final byte get(short s) {
                byte b;
                b = Long2ByteFunction.this.get(before.get(s));
                return b;
            }
        };
    }

    default Long2IntFunction andThenInt(final Byte2IntFunction after) {
        return new Long2IntFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.longs.Long2IntFunction
            public final int get(long j) {
                int i;
                i = after.get(Long2ByteFunction.this.get(j));
                return i;
            }
        };
    }

    default Int2ByteFunction composeInt(final Int2LongFunction before) {
        return new Int2ByteFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.ints.Int2ByteFunction
            public final byte get(int i) {
                byte b;
                b = Long2ByteFunction.this.get(before.get(i));
                return b;
            }
        };
    }

    default Long2LongFunction andThenLong(final Byte2LongFunction after) {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                long j2;
                j2 = after.get(Long2ByteFunction.this.get(j));
                return j2;
            }
        };
    }

    default Long2ByteFunction composeLong(final Long2LongFunction before) {
        return new Long2ByteFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.longs.Long2ByteFunction
            public final byte get(long j) {
                byte b;
                b = Long2ByteFunction.this.get(before.get(j));
                return b;
            }
        };
    }

    default Long2CharFunction andThenChar(final Byte2CharFunction after) {
        return new Long2CharFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.longs.Long2CharFunction
            public final char get(long j) {
                char c;
                c = after.get(Long2ByteFunction.this.get(j));
                return c;
            }
        };
    }

    default Char2ByteFunction composeChar(final Char2LongFunction before) {
        return new Char2ByteFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.chars.Char2ByteFunction
            public final byte get(char c) {
                byte b;
                b = Long2ByteFunction.this.get(before.get(c));
                return b;
            }
        };
    }

    default Long2FloatFunction andThenFloat(final Byte2FloatFunction after) {
        return new Long2FloatFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.longs.Long2FloatFunction
            public final float get(long j) {
                float f;
                f = after.get(Long2ByteFunction.this.get(j));
                return f;
            }
        };
    }

    default Float2ByteFunction composeFloat(final Float2LongFunction before) {
        return new Float2ByteFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.floats.Float2ByteFunction
            public final byte get(float f) {
                byte b;
                b = Long2ByteFunction.this.get(before.get(f));
                return b;
            }
        };
    }

    default Long2DoubleFunction andThenDouble(final Byte2DoubleFunction after) {
        return new Long2DoubleFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.longs.Long2DoubleFunction
            public final double get(long j) {
                double d;
                d = after.get(Long2ByteFunction.this.get(j));
                return d;
            }
        };
    }

    default Double2ByteFunction composeDouble(final Double2LongFunction before) {
        return new Double2ByteFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.doubles.Double2ByteFunction
            public final byte get(double d) {
                byte b;
                b = Long2ByteFunction.this.get(before.get(d));
                return b;
            }
        };
    }

    default <T> Long2ObjectFunction<T> andThenObject(final Byte2ObjectFunction<? extends T> after) {
        return new Long2ObjectFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
            public final Object get(long j) {
                Object obj;
                obj = after.get(Long2ByteFunction.this.get(j));
                return obj;
            }
        };
    }

    default <T> Object2ByteFunction<T> composeObject(final Object2LongFunction<? super T> before) {
        return new Object2ByteFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.objects.Object2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = Long2ByteFunction.this.get(before.getLong(obj));
                return b;
            }
        };
    }

    default <T> Long2ReferenceFunction<T> andThenReference(final Byte2ReferenceFunction<? extends T> after) {
        return new Long2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.longs.Long2ReferenceFunction
            public final Object get(long j) {
                Object obj;
                obj = after.get(Long2ByteFunction.this.get(j));
                return obj;
            }
        };
    }

    default <T> Reference2ByteFunction<T> composeReference(final Reference2LongFunction<? super T> before) {
        return new Reference2ByteFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ByteFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.objects.Reference2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = Long2ByteFunction.this.get(before.getLong(obj));
                return b;
            }
        };
    }
}
