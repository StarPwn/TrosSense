package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
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
import java.util.function.IntToDoubleFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Byte2FloatFunction extends Function<Byte, Float>, IntToDoubleFunction {
    float get(byte b);

    @Override // java.util.function.IntToDoubleFunction
    @Deprecated
    default double applyAsDouble(int operand) {
        return get(SafeMath.safeIntToByte(operand));
    }

    default float put(byte key, float value) {
        throw new UnsupportedOperationException();
    }

    default float getOrDefault(byte key, float defaultValue) {
        float v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default float remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float put(Byte key, Float value) {
        byte k = key.byteValue();
        boolean containsKey = containsKey(k);
        float v = put(k, value.floatValue());
        if (containsKey) {
            return Float.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float get(Object key) {
        if (key == null) {
            return null;
        }
        byte k = ((Byte) key).byteValue();
        float v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Float.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        byte k = ((Byte) key).byteValue();
        float v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Float.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float remove(Object key) {
        if (key == null) {
            return null;
        }
        byte k = ((Byte) key).byteValue();
        if (containsKey(k)) {
            return Float.valueOf(remove(k));
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

    default void defaultReturnValue(float rv) {
        throw new UnsupportedOperationException();
    }

    default float defaultReturnValue() {
        return 0.0f;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Float> compose(java.util.function.Function<? super T, ? extends Byte> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Byte, T> andThen(java.util.function.Function<? super Float, ? extends T> after) {
        return super.andThen(after);
    }

    default Byte2ByteFunction andThenByte(final Float2ByteFunction after) {
        return new Byte2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ByteFunction
            public final byte get(byte b) {
                byte b2;
                b2 = after.get(Byte2FloatFunction.this.get(b));
                return b2;
            }
        };
    }

    default Byte2FloatFunction composeByte(final Byte2ByteFunction before) {
        return new Byte2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.bytes.Byte2FloatFunction
            public final float get(byte b) {
                float f;
                f = Byte2FloatFunction.this.get(before.get(b));
                return f;
            }
        };
    }

    default Byte2ShortFunction andThenShort(final Float2ShortFunction after) {
        return new Byte2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ShortFunction
            public final short get(byte b) {
                short s;
                s = after.get(Byte2FloatFunction.this.get(b));
                return s;
            }
        };
    }

    default Short2FloatFunction composeShort(final Short2ByteFunction before) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = Byte2FloatFunction.this.get(before.get(s));
                return f;
            }
        };
    }

    default Byte2IntFunction andThenInt(final Float2IntFunction after) {
        return new Byte2IntFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.bytes.Byte2IntFunction
            public final int get(byte b) {
                int i;
                i = after.get(Byte2FloatFunction.this.get(b));
                return i;
            }
        };
    }

    default Int2FloatFunction composeInt(final Int2ByteFunction before) {
        return new Int2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.ints.Int2FloatFunction
            public final float get(int i) {
                float f;
                f = Byte2FloatFunction.this.get(before.get(i));
                return f;
            }
        };
    }

    default Byte2LongFunction andThenLong(final Float2LongFunction after) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = after.get(Byte2FloatFunction.this.get(b));
                return j;
            }
        };
    }

    default Long2FloatFunction composeLong(final Long2ByteFunction before) {
        return new Long2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.longs.Long2FloatFunction
            public final float get(long j) {
                float f;
                f = Byte2FloatFunction.this.get(before.get(j));
                return f;
            }
        };
    }

    default Byte2CharFunction andThenChar(final Float2CharFunction after) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = after.get(Byte2FloatFunction.this.get(b));
                return c;
            }
        };
    }

    default Char2FloatFunction composeChar(final Char2ByteFunction before) {
        return new Char2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.chars.Char2FloatFunction
            public final float get(char c) {
                float f;
                f = Byte2FloatFunction.this.get(before.get(c));
                return f;
            }
        };
    }

    default Byte2FloatFunction andThenFloat(final Float2FloatFunction after) {
        return new Byte2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.bytes.Byte2FloatFunction
            public final float get(byte b) {
                float f;
                f = after.get(Byte2FloatFunction.this.get(b));
                return f;
            }
        };
    }

    default Float2FloatFunction composeFloat(final Float2ByteFunction before) {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                float f2;
                f2 = Byte2FloatFunction.this.get(before.get(f));
                return f2;
            }
        };
    }

    default Byte2DoubleFunction andThenDouble(final Float2DoubleFunction after) {
        return new Byte2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction
            public final double get(byte b) {
                double d;
                d = after.get(Byte2FloatFunction.this.get(b));
                return d;
            }
        };
    }

    default Double2FloatFunction composeDouble(final Double2ByteFunction before) {
        return new Double2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.doubles.Double2FloatFunction
            public final float get(double d) {
                float f;
                f = Byte2FloatFunction.this.get(before.get(d));
                return f;
            }
        };
    }

    default <T> Byte2ObjectFunction<T> andThenObject(final Float2ObjectFunction<? extends T> after) {
        return new Byte2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction
            public final Object get(byte b) {
                Object obj;
                obj = after.get(Byte2FloatFunction.this.get(b));
                return obj;
            }
        };
    }

    default <T> Object2FloatFunction<T> composeObject(final Object2ByteFunction<? super T> before) {
        return new Object2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.objects.Object2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = Byte2FloatFunction.this.get(before.getByte(obj));
                return f;
            }
        };
    }

    default <T> Byte2ReferenceFunction<T> andThenReference(final Float2ReferenceFunction<? extends T> after) {
        return new Byte2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction
            public final Object get(byte b) {
                Object obj;
                obj = after.get(Byte2FloatFunction.this.get(b));
                return obj;
            }
        };
    }

    default <T> Reference2FloatFunction<T> composeReference(final Reference2ByteFunction<? super T> before) {
        return new Reference2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2FloatFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Reference2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = Byte2FloatFunction.this.get(before.getByte(obj));
                return f;
            }
        };
    }
}
