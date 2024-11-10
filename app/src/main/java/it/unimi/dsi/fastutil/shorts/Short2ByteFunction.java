package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
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
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import it.unimi.dsi.fastutil.objects.Reference2ShortFunction;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Short2ByteFunction extends Function<Short, Byte>, IntUnaryOperator {
    byte get(short s);

    @Override // java.util.function.IntUnaryOperator
    @Deprecated
    default int applyAsInt(int operand) {
        return get(SafeMath.safeIntToShort(operand));
    }

    default byte put(short key, byte value) {
        throw new UnsupportedOperationException();
    }

    default byte getOrDefault(short key, byte defaultValue) {
        byte v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default byte remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte put(Short key, Byte value) {
        short k = key.shortValue();
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
        short k = ((Short) key).shortValue();
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
        short k = ((Short) key).shortValue();
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
        short k = ((Short) key).shortValue();
        if (containsKey(k)) {
            return Byte.valueOf(remove(k));
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

    default void defaultReturnValue(byte rv) {
        throw new UnsupportedOperationException();
    }

    default byte defaultReturnValue() {
        return (byte) 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Byte> compose(java.util.function.Function<? super T, ? extends Short> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Short, T> andThen(java.util.function.Function<? super Byte, ? extends T> after) {
        return super.andThen(after);
    }

    default Short2ByteFunction andThenByte(final Byte2ByteFunction after) {
        return new Short2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.shorts.Short2ByteFunction
            public final byte get(short s) {
                byte b;
                b = after.get(Short2ByteFunction.this.get(s));
                return b;
            }
        };
    }

    default Byte2ByteFunction composeByte(final Byte2ShortFunction before) {
        return new Byte2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ByteFunction
            public final byte get(byte b) {
                byte b2;
                b2 = Short2ByteFunction.this.get(before.get(b));
                return b2;
            }
        };
    }

    default Short2ShortFunction andThenShort(final Byte2ShortFunction after) {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                short s2;
                s2 = after.get(Short2ByteFunction.this.get(s));
                return s2;
            }
        };
    }

    default Short2ByteFunction composeShort(final Short2ShortFunction before) {
        return new Short2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.shorts.Short2ByteFunction
            public final byte get(short s) {
                byte b;
                b = Short2ByteFunction.this.get(before.get(s));
                return b;
            }
        };
    }

    default Short2IntFunction andThenInt(final Byte2IntFunction after) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = after.get(Short2ByteFunction.this.get(s));
                return i;
            }
        };
    }

    default Int2ByteFunction composeInt(final Int2ShortFunction before) {
        return new Int2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.ints.Int2ByteFunction
            public final byte get(int i) {
                byte b;
                b = Short2ByteFunction.this.get(before.get(i));
                return b;
            }
        };
    }

    default Short2LongFunction andThenLong(final Byte2LongFunction after) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = after.get(Short2ByteFunction.this.get(s));
                return j;
            }
        };
    }

    default Long2ByteFunction composeLong(final Long2ShortFunction before) {
        return new Long2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.longs.Long2ByteFunction
            public final byte get(long j) {
                byte b;
                b = Short2ByteFunction.this.get(before.get(j));
                return b;
            }
        };
    }

    default Short2CharFunction andThenChar(final Byte2CharFunction after) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = after.get(Short2ByteFunction.this.get(s));
                return c;
            }
        };
    }

    default Char2ByteFunction composeChar(final Char2ShortFunction before) {
        return new Char2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.chars.Char2ByteFunction
            public final byte get(char c) {
                byte b;
                b = Short2ByteFunction.this.get(before.get(c));
                return b;
            }
        };
    }

    default Short2FloatFunction andThenFloat(final Byte2FloatFunction after) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = after.get(Short2ByteFunction.this.get(s));
                return f;
            }
        };
    }

    default Float2ByteFunction composeFloat(final Float2ShortFunction before) {
        return new Float2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.floats.Float2ByteFunction
            public final byte get(float f) {
                byte b;
                b = Short2ByteFunction.this.get(before.get(f));
                return b;
            }
        };
    }

    default Short2DoubleFunction andThenDouble(final Byte2DoubleFunction after) {
        return new Short2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.shorts.Short2DoubleFunction
            public final double get(short s) {
                double d;
                d = after.get(Short2ByteFunction.this.get(s));
                return d;
            }
        };
    }

    default Double2ByteFunction composeDouble(final Double2ShortFunction before) {
        return new Double2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.doubles.Double2ByteFunction
            public final byte get(double d) {
                byte b;
                b = Short2ByteFunction.this.get(before.get(d));
                return b;
            }
        };
    }

    default <T> Short2ObjectFunction<T> andThenObject(final Byte2ObjectFunction<? extends T> after) {
        return new Short2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.shorts.Short2ObjectFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2ByteFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Object2ByteFunction<T> composeObject(final Object2ShortFunction<? super T> before) {
        return new Object2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Object2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = Short2ByteFunction.this.get(before.getShort(obj));
                return b;
            }
        };
    }

    default <T> Short2ReferenceFunction<T> andThenReference(final Byte2ReferenceFunction<? extends T> after) {
        return new Short2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2ByteFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Reference2ByteFunction<T> composeReference(final Reference2ShortFunction<? super T> before) {
        return new Reference2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ByteFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.objects.Reference2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = Short2ByteFunction.this.get(before.getShort(obj));
                return b;
            }
        };
    }
}
