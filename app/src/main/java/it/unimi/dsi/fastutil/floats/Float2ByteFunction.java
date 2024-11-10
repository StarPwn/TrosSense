package it.unimi.dsi.fastutil.floats;

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
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import java.util.function.DoubleToIntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Float2ByteFunction extends Function<Float, Byte>, DoubleToIntFunction {
    byte get(float f);

    @Override // java.util.function.DoubleToIntFunction
    @Deprecated
    default int applyAsInt(double operand) {
        return get(SafeMath.safeDoubleToFloat(operand));
    }

    default byte put(float key, byte value) {
        throw new UnsupportedOperationException();
    }

    default byte getOrDefault(float key, byte defaultValue) {
        byte v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default byte remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte put(Float key, Byte value) {
        float k = key.floatValue();
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
        float k = ((Float) key).floatValue();
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
        float k = ((Float) key).floatValue();
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
        float k = ((Float) key).floatValue();
        if (containsKey(k)) {
            return Byte.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(float key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Float) key).floatValue());
    }

    default void defaultReturnValue(byte rv) {
        throw new UnsupportedOperationException();
    }

    default byte defaultReturnValue() {
        return (byte) 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Byte> compose(java.util.function.Function<? super T, ? extends Float> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Float, T> andThen(java.util.function.Function<? super Byte, ? extends T> after) {
        return super.andThen(after);
    }

    default Float2ByteFunction andThenByte(final Byte2ByteFunction after) {
        return new Float2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.floats.Float2ByteFunction
            public final byte get(float f) {
                byte b;
                b = after.get(Float2ByteFunction.this.get(f));
                return b;
            }
        };
    }

    default Byte2ByteFunction composeByte(final Byte2FloatFunction before) {
        return new Byte2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ByteFunction
            public final byte get(byte b) {
                byte b2;
                b2 = Float2ByteFunction.this.get(before.get(b));
                return b2;
            }
        };
    }

    default Float2ShortFunction andThenShort(final Byte2ShortFunction after) {
        return new Float2ShortFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.floats.Float2ShortFunction
            public final short get(float f) {
                short s;
                s = after.get(Float2ByteFunction.this.get(f));
                return s;
            }
        };
    }

    default Short2ByteFunction composeShort(final Short2FloatFunction before) {
        return new Short2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.shorts.Short2ByteFunction
            public final byte get(short s) {
                byte b;
                b = Float2ByteFunction.this.get(before.get(s));
                return b;
            }
        };
    }

    default Float2IntFunction andThenInt(final Byte2IntFunction after) {
        return new Float2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.floats.Float2IntFunction
            public final int get(float f) {
                int i;
                i = after.get(Float2ByteFunction.this.get(f));
                return i;
            }
        };
    }

    default Int2ByteFunction composeInt(final Int2FloatFunction before) {
        return new Int2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.ints.Int2ByteFunction
            public final byte get(int i) {
                byte b;
                b = Float2ByteFunction.this.get(before.get(i));
                return b;
            }
        };
    }

    default Float2LongFunction andThenLong(final Byte2LongFunction after) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = after.get(Float2ByteFunction.this.get(f));
                return j;
            }
        };
    }

    default Long2ByteFunction composeLong(final Long2FloatFunction before) {
        return new Long2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.longs.Long2ByteFunction
            public final byte get(long j) {
                byte b;
                b = Float2ByteFunction.this.get(before.get(j));
                return b;
            }
        };
    }

    default Float2CharFunction andThenChar(final Byte2CharFunction after) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = after.get(Float2ByteFunction.this.get(f));
                return c;
            }
        };
    }

    default Char2ByteFunction composeChar(final Char2FloatFunction before) {
        return new Char2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.chars.Char2ByteFunction
            public final byte get(char c) {
                byte b;
                b = Float2ByteFunction.this.get(before.get(c));
                return b;
            }
        };
    }

    default Float2FloatFunction andThenFloat(final Byte2FloatFunction after) {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                float f2;
                f2 = after.get(Float2ByteFunction.this.get(f));
                return f2;
            }
        };
    }

    default Float2ByteFunction composeFloat(final Float2FloatFunction before) {
        return new Float2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.floats.Float2ByteFunction
            public final byte get(float f) {
                byte b;
                b = Float2ByteFunction.this.get(before.get(f));
                return b;
            }
        };
    }

    default Float2DoubleFunction andThenDouble(final Byte2DoubleFunction after) {
        return new Float2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.floats.Float2DoubleFunction
            public final double get(float f) {
                double d;
                d = after.get(Float2ByteFunction.this.get(f));
                return d;
            }
        };
    }

    default Double2ByteFunction composeDouble(final Double2FloatFunction before) {
        return new Double2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.doubles.Double2ByteFunction
            public final byte get(double d) {
                byte b;
                b = Float2ByteFunction.this.get(before.get(d));
                return b;
            }
        };
    }

    default <T> Float2ObjectFunction<T> andThenObject(final Byte2ObjectFunction<? extends T> after) {
        return new Float2ObjectFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.floats.Float2ObjectFunction
            public final Object get(float f) {
                Object obj;
                obj = after.get(Float2ByteFunction.this.get(f));
                return obj;
            }
        };
    }

    default <T> Object2ByteFunction<T> composeObject(final Object2FloatFunction<? super T> before) {
        return new Object2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Object2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = Float2ByteFunction.this.get(before.getFloat(obj));
                return b;
            }
        };
    }

    default <T> Float2ReferenceFunction<T> andThenReference(final Byte2ReferenceFunction<? extends T> after) {
        return new Float2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.floats.Float2ReferenceFunction
            public final Object get(float f) {
                Object obj;
                obj = after.get(Float2ByteFunction.this.get(f));
                return obj;
            }
        };
    }

    default <T> Reference2ByteFunction<T> composeReference(final Reference2FloatFunction<? super T> before) {
        return new Reference2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ByteFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.objects.Reference2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = Float2ByteFunction.this.get(before.getFloat(obj));
                return b;
            }
        };
    }
}
