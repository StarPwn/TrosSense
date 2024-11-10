package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
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
import it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import java.util.function.IntToLongFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Byte2LongFunction extends Function<Byte, Long>, IntToLongFunction {
    long get(byte b);

    @Override // java.util.function.IntToLongFunction
    @Deprecated
    default long applyAsLong(int operand) {
        return get(SafeMath.safeIntToByte(operand));
    }

    default long put(byte key, long value) {
        throw new UnsupportedOperationException();
    }

    default long getOrDefault(byte key, long defaultValue) {
        long v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default long remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long put(Byte key, Long value) {
        byte k = key.byteValue();
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
        byte k = ((Byte) key).byteValue();
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
        byte k = ((Byte) key).byteValue();
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
        byte k = ((Byte) key).byteValue();
        if (containsKey(k)) {
            return Long.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(byte key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Byte) key).byteValue());
    }

    default void defaultReturnValue(long rv) {
        throw new UnsupportedOperationException();
    }

    default long defaultReturnValue() {
        return 0L;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Long> compose(java.util.function.Function<? super T, ? extends Byte> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Byte, T> andThen(java.util.function.Function<? super Long, ? extends T> after) {
        return super.andThen(after);
    }

    default Byte2ByteFunction andThenByte(final Long2ByteFunction after) {
        return new Byte2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ByteFunction
            public final byte get(byte b) {
                byte b2;
                b2 = after.get(Byte2LongFunction.this.get(b));
                return b2;
            }
        };
    }

    default Byte2LongFunction composeByte(final Byte2ByteFunction before) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = Byte2LongFunction.this.get(before.get(b));
                return j;
            }
        };
    }

    default Byte2ShortFunction andThenShort(final Long2ShortFunction after) {
        return new Byte2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ShortFunction
            public final short get(byte b) {
                short s;
                s = after.get(Byte2LongFunction.this.get(b));
                return s;
            }
        };
    }

    default Short2LongFunction composeShort(final Short2ByteFunction before) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = Byte2LongFunction.this.get(before.get(s));
                return j;
            }
        };
    }

    default Byte2IntFunction andThenInt(final Long2IntFunction after) {
        return new Byte2IntFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.bytes.Byte2IntFunction
            public final int get(byte b) {
                int i;
                i = after.get(Byte2LongFunction.this.get(b));
                return i;
            }
        };
    }

    default Int2LongFunction composeInt(final Int2ByteFunction before) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = Byte2LongFunction.this.get(before.get(i));
                return j;
            }
        };
    }

    default Byte2LongFunction andThenLong(final Long2LongFunction after) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = after.get(Byte2LongFunction.this.get(b));
                return j;
            }
        };
    }

    default Long2LongFunction composeLong(final Long2ByteFunction before) {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                long j2;
                j2 = Byte2LongFunction.this.get(before.get(j));
                return j2;
            }
        };
    }

    default Byte2CharFunction andThenChar(final Long2CharFunction after) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = after.get(Byte2LongFunction.this.get(b));
                return c;
            }
        };
    }

    default Char2LongFunction composeChar(final Char2ByteFunction before) {
        return new Char2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.chars.Char2LongFunction
            public final long get(char c) {
                long j;
                j = Byte2LongFunction.this.get(before.get(c));
                return j;
            }
        };
    }

    default Byte2FloatFunction andThenFloat(final Long2FloatFunction after) {
        return new Byte2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.bytes.Byte2FloatFunction
            public final float get(byte b) {
                float f;
                f = after.get(Byte2LongFunction.this.get(b));
                return f;
            }
        };
    }

    default Float2LongFunction composeFloat(final Float2ByteFunction before) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = Byte2LongFunction.this.get(before.get(f));
                return j;
            }
        };
    }

    default Byte2DoubleFunction andThenDouble(final Long2DoubleFunction after) {
        return new Byte2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction
            public final double get(byte b) {
                double d;
                d = after.get(Byte2LongFunction.this.get(b));
                return d;
            }
        };
    }

    default Double2LongFunction composeDouble(final Double2ByteFunction before) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = Byte2LongFunction.this.get(before.get(d));
                return j;
            }
        };
    }

    default <T> Byte2ObjectFunction<T> andThenObject(final Long2ObjectFunction<? extends T> after) {
        return new Byte2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction
            public final Object get(byte b) {
                Object obj;
                obj = after.get(Byte2LongFunction.this.get(b));
                return obj;
            }
        };
    }

    default <T> Object2LongFunction<T> composeObject(final Object2ByteFunction<? super T> before) {
        return new Object2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.objects.Object2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Byte2LongFunction.this.get(before.getByte(obj));
                return j;
            }
        };
    }

    default <T> Byte2ReferenceFunction<T> andThenReference(final Long2ReferenceFunction<? extends T> after) {
        return new Byte2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction
            public final Object get(byte b) {
                Object obj;
                obj = after.get(Byte2LongFunction.this.get(b));
                return obj;
            }
        };
    }

    default <T> Reference2LongFunction<T> composeReference(final Reference2ByteFunction<? super T> before) {
        return new Reference2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2LongFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.objects.Reference2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Byte2LongFunction.this.get(before.getByte(obj));
                return j;
            }
        };
    }
}
