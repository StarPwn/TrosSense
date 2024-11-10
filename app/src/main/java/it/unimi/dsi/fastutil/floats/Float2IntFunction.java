package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import java.util.function.DoubleToIntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Float2IntFunction extends Function<Float, Integer>, DoubleToIntFunction {
    int get(float f);

    @Override // java.util.function.DoubleToIntFunction
    @Deprecated
    default int applyAsInt(double operand) {
        return get(SafeMath.safeDoubleToFloat(operand));
    }

    default int put(float key, int value) {
        throw new UnsupportedOperationException();
    }

    default int getOrDefault(float key, int defaultValue) {
        int v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default int remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer put(Float key, Integer value) {
        float k = key.floatValue();
        boolean containsKey = containsKey(k);
        int v = put(k, value.intValue());
        if (containsKey) {
            return Integer.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer get(Object key) {
        if (key == null) {
            return null;
        }
        float k = ((Float) key).floatValue();
        int v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Integer.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        float k = ((Float) key).floatValue();
        int v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Integer.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer remove(Object key) {
        if (key == null) {
            return null;
        }
        float k = ((Float) key).floatValue();
        if (containsKey(k)) {
            return Integer.valueOf(remove(k));
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

    default void defaultReturnValue(int rv) {
        throw new UnsupportedOperationException();
    }

    default int defaultReturnValue() {
        return 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Integer> compose(java.util.function.Function<? super T, ? extends Float> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Float, T> andThen(java.util.function.Function<? super Integer, ? extends T> after) {
        return super.andThen(after);
    }

    default Float2ByteFunction andThenByte(final Int2ByteFunction after) {
        return new Float2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.floats.Float2ByteFunction
            public final byte get(float f) {
                byte b;
                b = after.get(Float2IntFunction.this.get(f));
                return b;
            }
        };
    }

    default Byte2IntFunction composeByte(final Byte2FloatFunction before) {
        return new Byte2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.bytes.Byte2IntFunction
            public final int get(byte b) {
                int i;
                i = Float2IntFunction.this.get(before.get(b));
                return i;
            }
        };
    }

    default Float2ShortFunction andThenShort(final Int2ShortFunction after) {
        return new Float2ShortFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.floats.Float2ShortFunction
            public final short get(float f) {
                short s;
                s = after.get(Float2IntFunction.this.get(f));
                return s;
            }
        };
    }

    default Short2IntFunction composeShort(final Short2FloatFunction before) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = Float2IntFunction.this.get(before.get(s));
                return i;
            }
        };
    }

    default Float2IntFunction andThenInt(final Int2IntFunction after) {
        return new Float2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.floats.Float2IntFunction
            public final int get(float f) {
                int i;
                i = after.get(Float2IntFunction.this.get(f));
                return i;
            }
        };
    }

    default Int2IntFunction composeInt(final Int2FloatFunction before) {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                int i2;
                i2 = Float2IntFunction.this.get(before.get(i));
                return i2;
            }
        };
    }

    default Float2LongFunction andThenLong(final Int2LongFunction after) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = after.get(Float2IntFunction.this.get(f));
                return j;
            }
        };
    }

    default Long2IntFunction composeLong(final Long2FloatFunction before) {
        return new Long2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.longs.Long2IntFunction
            public final int get(long j) {
                int i;
                i = Float2IntFunction.this.get(before.get(j));
                return i;
            }
        };
    }

    default Float2CharFunction andThenChar(final Int2CharFunction after) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = after.get(Float2IntFunction.this.get(f));
                return c;
            }
        };
    }

    default Char2IntFunction composeChar(final Char2FloatFunction before) {
        return new Char2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.chars.Char2IntFunction
            public final int get(char c) {
                int i;
                i = Float2IntFunction.this.get(before.get(c));
                return i;
            }
        };
    }

    default Float2FloatFunction andThenFloat(final Int2FloatFunction after) {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                float f2;
                f2 = after.get(Float2IntFunction.this.get(f));
                return f2;
            }
        };
    }

    default Float2IntFunction composeFloat(final Float2FloatFunction before) {
        return new Float2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.floats.Float2IntFunction
            public final int get(float f) {
                int i;
                i = Float2IntFunction.this.get(before.get(f));
                return i;
            }
        };
    }

    default Float2DoubleFunction andThenDouble(final Int2DoubleFunction after) {
        return new Float2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.floats.Float2DoubleFunction
            public final double get(float f) {
                double d;
                d = after.get(Float2IntFunction.this.get(f));
                return d;
            }
        };
    }

    default Double2IntFunction composeDouble(final Double2FloatFunction before) {
        return new Double2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.doubles.Double2IntFunction
            public final int get(double d) {
                int i;
                i = Float2IntFunction.this.get(before.get(d));
                return i;
            }
        };
    }

    default <T> Float2ObjectFunction<T> andThenObject(final Int2ObjectFunction<? extends T> after) {
        return new Float2ObjectFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.floats.Float2ObjectFunction
            public final Object get(float f) {
                Object obj;
                obj = after.get(Float2IntFunction.this.get(f));
                return obj;
            }
        };
    }

    default <T> Object2IntFunction<T> composeObject(final Object2FloatFunction<? super T> before) {
        return new Object2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = Float2IntFunction.this.get(before.getFloat(obj));
                return i;
            }
        };
    }

    default <T> Float2ReferenceFunction<T> andThenReference(final Int2ReferenceFunction<? extends T> after) {
        return new Float2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.floats.Float2ReferenceFunction
            public final Object get(float f) {
                Object obj;
                obj = after.get(Float2IntFunction.this.get(f));
                return obj;
            }
        };
    }

    default <T> Reference2IntFunction<T> composeReference(final Reference2FloatFunction<? super T> before) {
        return new Reference2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2IntFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.objects.Reference2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = Float2IntFunction.this.get(before.getFloat(obj));
                return i;
            }
        };
    }
}
