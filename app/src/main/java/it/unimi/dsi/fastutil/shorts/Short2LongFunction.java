package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
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
import it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import it.unimi.dsi.fastutil.longs.Long2CharFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.longs.Long2LongFunction;
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import it.unimi.dsi.fastutil.objects.Reference2ShortFunction;
import java.util.function.IntToLongFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Short2LongFunction extends Function<Short, Long>, IntToLongFunction {
    long get(short s);

    @Override // java.util.function.IntToLongFunction
    @Deprecated
    default long applyAsLong(int operand) {
        return get(SafeMath.safeIntToShort(operand));
    }

    default long put(short key, long value) {
        throw new UnsupportedOperationException();
    }

    default long getOrDefault(short key, long defaultValue) {
        long v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default long remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long put(Short key, Long value) {
        short k = key.shortValue();
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
        short k = ((Short) key).shortValue();
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
        short k = ((Short) key).shortValue();
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
        short k = ((Short) key).shortValue();
        if (containsKey(k)) {
            return Long.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(short key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Short) key).shortValue());
    }

    default void defaultReturnValue(long rv) {
        throw new UnsupportedOperationException();
    }

    default long defaultReturnValue() {
        return 0L;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Long> compose(java.util.function.Function<? super T, ? extends Short> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Short, T> andThen(java.util.function.Function<? super Long, ? extends T> after) {
        return super.andThen(after);
    }

    default Short2ByteFunction andThenByte(final Long2ByteFunction after) {
        return new Short2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.shorts.Short2ByteFunction
            public final byte get(short s) {
                byte b;
                b = after.get(Short2LongFunction.this.get(s));
                return b;
            }
        };
    }

    default Byte2LongFunction composeByte(final Byte2ShortFunction before) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = Short2LongFunction.this.get(before.get(b));
                return j;
            }
        };
    }

    default Short2ShortFunction andThenShort(final Long2ShortFunction after) {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                short s2;
                s2 = after.get(Short2LongFunction.this.get(s));
                return s2;
            }
        };
    }

    default Short2LongFunction composeShort(final Short2ShortFunction before) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = Short2LongFunction.this.get(before.get(s));
                return j;
            }
        };
    }

    default Short2IntFunction andThenInt(final Long2IntFunction after) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = after.get(Short2LongFunction.this.get(s));
                return i;
            }
        };
    }

    default Int2LongFunction composeInt(final Int2ShortFunction before) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = Short2LongFunction.this.get(before.get(i));
                return j;
            }
        };
    }

    default Short2LongFunction andThenLong(final Long2LongFunction after) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = after.get(Short2LongFunction.this.get(s));
                return j;
            }
        };
    }

    default Long2LongFunction composeLong(final Long2ShortFunction before) {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                long j2;
                j2 = Short2LongFunction.this.get(before.get(j));
                return j2;
            }
        };
    }

    default Short2CharFunction andThenChar(final Long2CharFunction after) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = after.get(Short2LongFunction.this.get(s));
                return c;
            }
        };
    }

    default Char2LongFunction composeChar(final Char2ShortFunction before) {
        return new Char2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.chars.Char2LongFunction
            public final long get(char c) {
                long j;
                j = Short2LongFunction.this.get(before.get(c));
                return j;
            }
        };
    }

    default Short2FloatFunction andThenFloat(final Long2FloatFunction after) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = after.get(Short2LongFunction.this.get(s));
                return f;
            }
        };
    }

    default Float2LongFunction composeFloat(final Float2ShortFunction before) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = Short2LongFunction.this.get(before.get(f));
                return j;
            }
        };
    }

    default Short2DoubleFunction andThenDouble(final Long2DoubleFunction after) {
        return new Short2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.shorts.Short2DoubleFunction
            public final double get(short s) {
                double d;
                d = after.get(Short2LongFunction.this.get(s));
                return d;
            }
        };
    }

    default Double2LongFunction composeDouble(final Double2ShortFunction before) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = Short2LongFunction.this.get(before.get(d));
                return j;
            }
        };
    }

    default <T> Short2ObjectFunction<T> andThenObject(final Long2ObjectFunction<? extends T> after) {
        return new Short2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.shorts.Short2ObjectFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2LongFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Object2LongFunction<T> composeObject(final Object2ShortFunction<? super T> before) {
        return new Object2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.objects.Object2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Short2LongFunction.this.get(before.getShort(obj));
                return j;
            }
        };
    }

    default <T> Short2ReferenceFunction<T> andThenReference(final Long2ReferenceFunction<? extends T> after) {
        return new Short2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2LongFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Reference2LongFunction<T> composeReference(final Reference2ShortFunction<? super T> before) {
        return new Reference2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2LongFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.objects.Reference2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Short2LongFunction.this.get(before.getShort(obj));
                return j;
            }
        };
    }
}
