package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
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
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;
import it.unimi.dsi.fastutil.shorts.Short2ShortFunction;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Byte2ShortFunction extends Function<Byte, Short>, IntUnaryOperator {
    short get(byte b);

    @Override // java.util.function.IntUnaryOperator
    @Deprecated
    default int applyAsInt(int operand) {
        return get(SafeMath.safeIntToByte(operand));
    }

    default short put(byte key, short value) {
        throw new UnsupportedOperationException();
    }

    default short getOrDefault(byte key, short defaultValue) {
        short v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default short remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short put(Byte key, Short value) {
        byte k = key.byteValue();
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
        byte k = ((Byte) key).byteValue();
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
        byte k = ((Byte) key).byteValue();
        short v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Short.valueOf(v) : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short remove(Object key) {
        if (key == null) {
            return null;
        }
        byte k = ((Byte) key).byteValue();
        if (containsKey(k)) {
            return Short.valueOf(remove(k));
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

    default void defaultReturnValue(short rv) {
        throw new UnsupportedOperationException();
    }

    default short defaultReturnValue() {
        return (short) 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Short> compose(java.util.function.Function<? super T, ? extends Byte> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Byte, T> andThen(java.util.function.Function<? super Short, ? extends T> after) {
        return super.andThen(after);
    }

    default Byte2ByteFunction andThenByte(final Short2ByteFunction after) {
        return new Byte2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ByteFunction
            public final byte get(byte b) {
                byte b2;
                b2 = after.get(Byte2ShortFunction.this.get(b));
                return b2;
            }
        };
    }

    default Byte2ShortFunction composeByte(final Byte2ByteFunction before) {
        return new Byte2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ShortFunction
            public final short get(byte b) {
                short s;
                s = Byte2ShortFunction.this.get(before.get(b));
                return s;
            }
        };
    }

    default Byte2ShortFunction andThenShort(final Short2ShortFunction after) {
        return new Byte2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ShortFunction
            public final short get(byte b) {
                short s;
                s = after.get(Byte2ShortFunction.this.get(b));
                return s;
            }
        };
    }

    default Short2ShortFunction composeShort(final Short2ByteFunction before) {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                short s2;
                s2 = Byte2ShortFunction.this.get(before.get(s));
                return s2;
            }
        };
    }

    default Byte2IntFunction andThenInt(final Short2IntFunction after) {
        return new Byte2IntFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.bytes.Byte2IntFunction
            public final int get(byte b) {
                int i;
                i = after.get(Byte2ShortFunction.this.get(b));
                return i;
            }
        };
    }

    default Int2ShortFunction composeInt(final Int2ByteFunction before) {
        return new Int2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.ints.Int2ShortFunction
            public final short get(int i) {
                short s;
                s = Byte2ShortFunction.this.get(before.get(i));
                return s;
            }
        };
    }

    default Byte2LongFunction andThenLong(final Short2LongFunction after) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = after.get(Byte2ShortFunction.this.get(b));
                return j;
            }
        };
    }

    default Long2ShortFunction composeLong(final Long2ByteFunction before) {
        return new Long2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.longs.Long2ShortFunction
            public final short get(long j) {
                short s;
                s = Byte2ShortFunction.this.get(before.get(j));
                return s;
            }
        };
    }

    default Byte2CharFunction andThenChar(final Short2CharFunction after) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = after.get(Byte2ShortFunction.this.get(b));
                return c;
            }
        };
    }

    default Char2ShortFunction composeChar(final Char2ByteFunction before) {
        return new Char2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.chars.Char2ShortFunction
            public final short get(char c) {
                short s;
                s = Byte2ShortFunction.this.get(before.get(c));
                return s;
            }
        };
    }

    default Byte2FloatFunction andThenFloat(final Short2FloatFunction after) {
        return new Byte2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.bytes.Byte2FloatFunction
            public final float get(byte b) {
                float f;
                f = after.get(Byte2ShortFunction.this.get(b));
                return f;
            }
        };
    }

    default Float2ShortFunction composeFloat(final Float2ByteFunction before) {
        return new Float2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.floats.Float2ShortFunction
            public final short get(float f) {
                short s;
                s = Byte2ShortFunction.this.get(before.get(f));
                return s;
            }
        };
    }

    default Byte2DoubleFunction andThenDouble(final Short2DoubleFunction after) {
        return new Byte2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction
            public final double get(byte b) {
                double d;
                d = after.get(Byte2ShortFunction.this.get(b));
                return d;
            }
        };
    }

    default Double2ShortFunction composeDouble(final Double2ByteFunction before) {
        return new Double2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.doubles.Double2ShortFunction
            public final short get(double d) {
                short s;
                s = Byte2ShortFunction.this.get(before.get(d));
                return s;
            }
        };
    }

    default <T> Byte2ObjectFunction<T> andThenObject(final Short2ObjectFunction<? extends T> after) {
        return new Byte2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction
            public final Object get(byte b) {
                Object obj;
                obj = after.get(Byte2ShortFunction.this.get(b));
                return obj;
            }
        };
    }

    default <T> Object2ShortFunction<T> composeObject(final Object2ByteFunction<? super T> before) {
        return new Object2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Object2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = Byte2ShortFunction.this.get(before.getByte(obj));
                return s;
            }
        };
    }

    default <T> Byte2ReferenceFunction<T> andThenReference(final Short2ReferenceFunction<? extends T> after) {
        return new Byte2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction
            public final Object get(byte b) {
                Object obj;
                obj = after.get(Byte2ShortFunction.this.get(b));
                return obj;
            }
        };
    }

    default <T> Reference2ShortFunction<T> composeReference(final Reference2ByteFunction<? super T> before) {
        return new Reference2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ShortFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.objects.Reference2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = Byte2ShortFunction.this.get(before.getByte(obj));
                return s;
            }
        };
    }
}
