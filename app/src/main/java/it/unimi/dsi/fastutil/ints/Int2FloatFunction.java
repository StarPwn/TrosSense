package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import java.util.function.IntToDoubleFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Int2FloatFunction extends Function<Integer, Float>, IntToDoubleFunction {
    float get(int i);

    @Override // java.util.function.IntToDoubleFunction
    default double applyAsDouble(int operand) {
        return get(operand);
    }

    default float put(int key, float value) {
        throw new UnsupportedOperationException();
    }

    default float getOrDefault(int key, float defaultValue) {
        float v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default float remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float put(Integer key, Float value) {
        int k = key.intValue();
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
        int k = ((Integer) key).intValue();
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
        int k = ((Integer) key).intValue();
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
        int k = ((Integer) key).intValue();
        if (containsKey(k)) {
            return Float.valueOf(remove(k));
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

    default void defaultReturnValue(float rv) {
        throw new UnsupportedOperationException();
    }

    default float defaultReturnValue() {
        return 0.0f;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Float> compose(java.util.function.Function<? super T, ? extends Integer> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Integer, T> andThen(java.util.function.Function<? super Float, ? extends T> after) {
        return super.andThen(after);
    }

    default Int2ByteFunction andThenByte(final Float2ByteFunction after) {
        return new Int2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.ints.Int2ByteFunction
            public final byte get(int i) {
                byte b;
                b = after.get(Int2FloatFunction.this.get(i));
                return b;
            }
        };
    }

    default Byte2FloatFunction composeByte(final Byte2IntFunction before) {
        return new Byte2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.bytes.Byte2FloatFunction
            public final float get(byte b) {
                float f;
                f = Int2FloatFunction.this.get(before.get(b));
                return f;
            }
        };
    }

    default Int2ShortFunction andThenShort(final Float2ShortFunction after) {
        return new Int2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.ints.Int2ShortFunction
            public final short get(int i) {
                short s;
                s = after.get(Int2FloatFunction.this.get(i));
                return s;
            }
        };
    }

    default Short2FloatFunction composeShort(final Short2IntFunction before) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = Int2FloatFunction.this.get(before.get(s));
                return f;
            }
        };
    }

    default Int2IntFunction andThenInt(final Float2IntFunction after) {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                int i2;
                i2 = after.get(Int2FloatFunction.this.get(i));
                return i2;
            }
        };
    }

    default Int2FloatFunction composeInt(final Int2IntFunction before) {
        return new Int2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.ints.Int2FloatFunction
            public final float get(int i) {
                float f;
                f = Int2FloatFunction.this.get(before.get(i));
                return f;
            }
        };
    }

    default Int2LongFunction andThenLong(final Float2LongFunction after) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = after.get(Int2FloatFunction.this.get(i));
                return j;
            }
        };
    }

    default Long2FloatFunction composeLong(final Long2IntFunction before) {
        return new Long2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.longs.Long2FloatFunction
            public final float get(long j) {
                float f;
                f = Int2FloatFunction.this.get(before.get(j));
                return f;
            }
        };
    }

    default Int2CharFunction andThenChar(final Float2CharFunction after) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = after.get(Int2FloatFunction.this.get(i));
                return c;
            }
        };
    }

    default Char2FloatFunction composeChar(final Char2IntFunction before) {
        return new Char2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.chars.Char2FloatFunction
            public final float get(char c) {
                float f;
                f = Int2FloatFunction.this.get(before.get(c));
                return f;
            }
        };
    }

    default Int2FloatFunction andThenFloat(final Float2FloatFunction after) {
        return new Int2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.ints.Int2FloatFunction
            public final float get(int i) {
                float f;
                f = after.get(Int2FloatFunction.this.get(i));
                return f;
            }
        };
    }

    default Float2FloatFunction composeFloat(final Float2IntFunction before) {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                float f2;
                f2 = Int2FloatFunction.this.get(before.get(f));
                return f2;
            }
        };
    }

    default Int2DoubleFunction andThenDouble(final Float2DoubleFunction after) {
        return new Int2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.ints.Int2DoubleFunction
            public final double get(int i) {
                double d;
                d = after.get(Int2FloatFunction.this.get(i));
                return d;
            }
        };
    }

    default Double2FloatFunction composeDouble(final Double2IntFunction before) {
        return new Double2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.doubles.Double2FloatFunction
            public final float get(double d) {
                float f;
                f = Int2FloatFunction.this.get(before.get(d));
                return f;
            }
        };
    }

    default <T> Int2ObjectFunction<T> andThenObject(final Float2ObjectFunction<? extends T> after) {
        return new Int2ObjectFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
            public final Object get(int i) {
                Object obj;
                obj = after.get(Int2FloatFunction.this.get(i));
                return obj;
            }
        };
    }

    default <T> Object2FloatFunction<T> composeObject(final Object2IntFunction<? super T> before) {
        return new Object2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.objects.Object2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = Int2FloatFunction.this.get(before.getInt(obj));
                return f;
            }
        };
    }

    default <T> Int2ReferenceFunction<T> andThenReference(final Float2ReferenceFunction<? extends T> after) {
        return new Int2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.ints.Int2ReferenceFunction
            public final Object get(int i) {
                Object obj;
                obj = after.get(Int2FloatFunction.this.get(i));
                return obj;
            }
        };
    }

    default <T> Reference2FloatFunction<T> composeReference(final Reference2IntFunction<? super T> before) {
        return new Reference2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2FloatFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Reference2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = Int2FloatFunction.this.get(before.getInt(obj));
                return f;
            }
        };
    }
}
