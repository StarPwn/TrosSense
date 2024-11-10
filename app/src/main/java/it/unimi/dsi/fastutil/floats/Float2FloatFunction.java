package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import java.util.function.DoubleUnaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Float2FloatFunction extends Function<Float, Float>, DoubleUnaryOperator {
    float get(float f);

    @Override // java.util.function.DoubleUnaryOperator
    @Deprecated
    default double applyAsDouble(double operand) {
        return get(SafeMath.safeDoubleToFloat(operand));
    }

    default float put(float key, float value) {
        throw new UnsupportedOperationException();
    }

    default float getOrDefault(float key, float defaultValue) {
        float v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default float remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float put(Float key, Float value) {
        float k = key.floatValue();
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
        float k = ((Float) key).floatValue();
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
        float k = ((Float) key).floatValue();
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
        float k = ((Float) key).floatValue();
        if (containsKey(k)) {
            return Float.valueOf(remove(k));
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

    default void defaultReturnValue(float rv) {
        throw new UnsupportedOperationException();
    }

    default float defaultReturnValue() {
        return 0.0f;
    }

    static Float2FloatFunction identity() {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                return Float2FloatFunction.lambda$identity$0(f);
            }
        };
    }

    static /* synthetic */ float lambda$identity$0(float k) {
        return k;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Float> compose(java.util.function.Function<? super T, ? extends Float> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Float, T> andThen(java.util.function.Function<? super Float, ? extends T> after) {
        return super.andThen(after);
    }

    default Float2ByteFunction andThenByte(final Float2ByteFunction after) {
        return new Float2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.floats.Float2ByteFunction
            public final byte get(float f) {
                byte b;
                b = after.get(Float2FloatFunction.this.get(f));
                return b;
            }
        };
    }

    default Byte2FloatFunction composeByte(final Byte2FloatFunction before) {
        return new Byte2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.bytes.Byte2FloatFunction
            public final float get(byte b) {
                float f;
                f = Float2FloatFunction.this.get(before.get(b));
                return f;
            }
        };
    }

    default Float2ShortFunction andThenShort(final Float2ShortFunction after) {
        return new Float2ShortFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.floats.Float2ShortFunction
            public final short get(float f) {
                short s;
                s = after.get(Float2FloatFunction.this.get(f));
                return s;
            }
        };
    }

    default Short2FloatFunction composeShort(final Short2FloatFunction before) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = Float2FloatFunction.this.get(before.get(s));
                return f;
            }
        };
    }

    default Float2IntFunction andThenInt(final Float2IntFunction after) {
        return new Float2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.floats.Float2IntFunction
            public final int get(float f) {
                int i;
                i = after.get(Float2FloatFunction.this.get(f));
                return i;
            }
        };
    }

    default Int2FloatFunction composeInt(final Int2FloatFunction before) {
        return new Int2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.ints.Int2FloatFunction
            public final float get(int i) {
                float f;
                f = Float2FloatFunction.this.get(before.get(i));
                return f;
            }
        };
    }

    default Float2LongFunction andThenLong(final Float2LongFunction after) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = after.get(Float2FloatFunction.this.get(f));
                return j;
            }
        };
    }

    default Long2FloatFunction composeLong(final Long2FloatFunction before) {
        return new Long2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.longs.Long2FloatFunction
            public final float get(long j) {
                float f;
                f = Float2FloatFunction.this.get(before.get(j));
                return f;
            }
        };
    }

    default Float2CharFunction andThenChar(final Float2CharFunction after) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = after.get(Float2FloatFunction.this.get(f));
                return c;
            }
        };
    }

    default Char2FloatFunction composeChar(final Char2FloatFunction before) {
        return new Char2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.chars.Char2FloatFunction
            public final float get(char c) {
                float f;
                f = Float2FloatFunction.this.get(before.get(c));
                return f;
            }
        };
    }

    default Float2FloatFunction andThenFloat(final Float2FloatFunction after) {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                float f2;
                f2 = after.get(Float2FloatFunction.this.get(f));
                return f2;
            }
        };
    }

    default Float2FloatFunction composeFloat(final Float2FloatFunction before) {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                float f2;
                f2 = Float2FloatFunction.this.get(before.get(f));
                return f2;
            }
        };
    }

    default Float2DoubleFunction andThenDouble(final Float2DoubleFunction after) {
        return new Float2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.floats.Float2DoubleFunction
            public final double get(float f) {
                double d;
                d = after.get(Float2FloatFunction.this.get(f));
                return d;
            }
        };
    }

    default Double2FloatFunction composeDouble(final Double2FloatFunction before) {
        return new Double2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.doubles.Double2FloatFunction
            public final float get(double d) {
                float f;
                f = Float2FloatFunction.this.get(before.get(d));
                return f;
            }
        };
    }

    default <T> Float2ObjectFunction<T> andThenObject(final Float2ObjectFunction<? extends T> after) {
        return new Float2ObjectFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.floats.Float2ObjectFunction
            public final Object get(float f) {
                Object obj;
                obj = after.get(Float2FloatFunction.this.get(f));
                return obj;
            }
        };
    }

    default <T> Object2FloatFunction<T> composeObject(final Object2FloatFunction<? super T> before) {
        return new Object2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.objects.Object2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = Float2FloatFunction.this.get(before.getFloat(obj));
                return f;
            }
        };
    }

    default <T> Float2ReferenceFunction<T> andThenReference(final Float2ReferenceFunction<? extends T> after) {
        return new Float2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.floats.Float2ReferenceFunction
            public final Object get(float f) {
                Object obj;
                obj = after.get(Float2FloatFunction.this.get(f));
                return obj;
            }
        };
    }

    default <T> Reference2FloatFunction<T> composeReference(final Reference2FloatFunction<? super T> before) {
        return new Reference2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2FloatFunction$$ExternalSyntheticLambda18
            @Override // it.unimi.dsi.fastutil.objects.Reference2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = Float2FloatFunction.this.get(before.getFloat(obj));
                return f;
            }
        };
    }
}
