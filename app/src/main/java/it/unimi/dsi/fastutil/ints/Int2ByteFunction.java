package it.unimi.dsi.fastutil.ints;

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
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Int2ByteFunction extends Function<Integer, Byte>, java.util.function.IntUnaryOperator {
    byte get(int i);

    @Override // java.util.function.IntUnaryOperator
    default int applyAsInt(int operand) {
        return get(operand);
    }

    default byte put(int key, byte value) {
        throw new UnsupportedOperationException();
    }

    default byte getOrDefault(int key, byte defaultValue) {
        byte v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default byte remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte put(Integer key, Byte value) {
        int k = key.intValue();
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
        int k = ((Integer) key).intValue();
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
        int k = ((Integer) key).intValue();
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
        int k = ((Integer) key).intValue();
        if (containsKey(k)) {
            return Byte.valueOf(remove(k));
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

    default void defaultReturnValue(byte rv) {
        throw new UnsupportedOperationException();
    }

    default byte defaultReturnValue() {
        return (byte) 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Byte> compose(java.util.function.Function<? super T, ? extends Integer> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Integer, T> andThen(java.util.function.Function<? super Byte, ? extends T> after) {
        return super.andThen(after);
    }

    default Int2ByteFunction andThenByte(final Byte2ByteFunction after) {
        return new Int2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.ints.Int2ByteFunction
            public final byte get(int i) {
                byte b;
                b = after.get(Int2ByteFunction.this.get(i));
                return b;
            }
        };
    }

    default Byte2ByteFunction composeByte(final Byte2IntFunction before) {
        return new Byte2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ByteFunction
            public final byte get(byte b) {
                byte b2;
                b2 = Int2ByteFunction.this.get(before.get(b));
                return b2;
            }
        };
    }

    default Int2ShortFunction andThenShort(final Byte2ShortFunction after) {
        return new Int2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.ints.Int2ShortFunction
            public final short get(int i) {
                short s;
                s = after.get(Int2ByteFunction.this.get(i));
                return s;
            }
        };
    }

    default Short2ByteFunction composeShort(final Short2IntFunction before) {
        return new Short2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.shorts.Short2ByteFunction
            public final byte get(short s) {
                byte b;
                b = Int2ByteFunction.this.get(before.get(s));
                return b;
            }
        };
    }

    default Int2IntFunction andThenInt(final Byte2IntFunction after) {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                int i2;
                i2 = after.get(Int2ByteFunction.this.get(i));
                return i2;
            }
        };
    }

    default Int2ByteFunction composeInt(final Int2IntFunction before) {
        return new Int2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.ints.Int2ByteFunction
            public final byte get(int i) {
                byte b;
                b = Int2ByteFunction.this.get(before.get(i));
                return b;
            }
        };
    }

    default Int2LongFunction andThenLong(final Byte2LongFunction after) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = after.get(Int2ByteFunction.this.get(i));
                return j;
            }
        };
    }

    default Long2ByteFunction composeLong(final Long2IntFunction before) {
        return new Long2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.longs.Long2ByteFunction
            public final byte get(long j) {
                byte b;
                b = Int2ByteFunction.this.get(before.get(j));
                return b;
            }
        };
    }

    default Int2CharFunction andThenChar(final Byte2CharFunction after) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = after.get(Int2ByteFunction.this.get(i));
                return c;
            }
        };
    }

    default Char2ByteFunction composeChar(final Char2IntFunction before) {
        return new Char2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.chars.Char2ByteFunction
            public final byte get(char c) {
                byte b;
                b = Int2ByteFunction.this.get(before.get(c));
                return b;
            }
        };
    }

    default Int2FloatFunction andThenFloat(final Byte2FloatFunction after) {
        return new Int2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.ints.Int2FloatFunction
            public final float get(int i) {
                float f;
                f = after.get(Int2ByteFunction.this.get(i));
                return f;
            }
        };
    }

    default Float2ByteFunction composeFloat(final Float2IntFunction before) {
        return new Float2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.floats.Float2ByteFunction
            public final byte get(float f) {
                byte b;
                b = Int2ByteFunction.this.get(before.get(f));
                return b;
            }
        };
    }

    default Int2DoubleFunction andThenDouble(final Byte2DoubleFunction after) {
        return new Int2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.ints.Int2DoubleFunction
            public final double get(int i) {
                double d;
                d = after.get(Int2ByteFunction.this.get(i));
                return d;
            }
        };
    }

    default Double2ByteFunction composeDouble(final Double2IntFunction before) {
        return new Double2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.doubles.Double2ByteFunction
            public final byte get(double d) {
                byte b;
                b = Int2ByteFunction.this.get(before.get(d));
                return b;
            }
        };
    }

    default <T> Int2ObjectFunction<T> andThenObject(final Byte2ObjectFunction<? extends T> after) {
        return new Int2ObjectFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
            public final Object get(int i) {
                Object obj;
                obj = after.get(Int2ByteFunction.this.get(i));
                return obj;
            }
        };
    }

    default <T> Object2ByteFunction<T> composeObject(final Object2IntFunction<? super T> before) {
        return new Object2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.objects.Object2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = Int2ByteFunction.this.get(before.getInt(obj));
                return b;
            }
        };
    }

    default <T> Int2ReferenceFunction<T> andThenReference(final Byte2ReferenceFunction<? extends T> after) {
        return new Int2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.ints.Int2ReferenceFunction
            public final Object get(int i) {
                Object obj;
                obj = after.get(Int2ByteFunction.this.get(i));
                return obj;
            }
        };
    }

    default <T> Reference2ByteFunction<T> composeReference(final Reference2IntFunction<? super T> before) {
        return new Reference2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ByteFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.objects.Reference2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = Int2ByteFunction.this.get(before.getInt(obj));
                return b;
            }
        };
    }
}
