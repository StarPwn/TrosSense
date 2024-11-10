package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2BooleanFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2BooleanFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.floats.Float2BooleanFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.longs.Long2BooleanFunction;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import it.unimi.dsi.fastutil.shorts.Short2BooleanFunction;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Boolean2FloatFunction extends Function<Boolean, Float> {
    float get(boolean z);

    default float put(boolean key, float value) {
        throw new UnsupportedOperationException();
    }

    default float getOrDefault(boolean key, float defaultValue) {
        float v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default float remove(boolean key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float put(Boolean key, Float value) {
        boolean k = key.booleanValue();
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
        boolean k = ((Boolean) key).booleanValue();
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
        boolean k = ((Boolean) key).booleanValue();
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
        boolean k = ((Boolean) key).booleanValue();
        if (containsKey(k)) {
            return Float.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(boolean key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Boolean) key).booleanValue());
    }

    default void defaultReturnValue(float rv) {
        throw new UnsupportedOperationException();
    }

    default float defaultReturnValue() {
        return 0.0f;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Float> compose(java.util.function.Function<? super T, ? extends Boolean> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Boolean, T> andThen(java.util.function.Function<? super Float, ? extends T> after) {
        return super.andThen(after);
    }

    default Boolean2ByteFunction andThenByte(final Float2ByteFunction after) {
        return new Boolean2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction
            public final byte get(boolean z) {
                byte b;
                b = after.get(Boolean2FloatFunction.this.get(z));
                return b;
            }
        };
    }

    default Byte2FloatFunction composeByte(final Byte2BooleanFunction before) {
        return new Byte2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.bytes.Byte2FloatFunction
            public final float get(byte b) {
                float f;
                f = Boolean2FloatFunction.this.get(before.get(b));
                return f;
            }
        };
    }

    default Boolean2ShortFunction andThenShort(final Float2ShortFunction after) {
        return new Boolean2ShortFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction
            public final short get(boolean z) {
                short s;
                s = after.get(Boolean2FloatFunction.this.get(z));
                return s;
            }
        };
    }

    default Short2FloatFunction composeShort(final Short2BooleanFunction before) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = Boolean2FloatFunction.this.get(before.get(s));
                return f;
            }
        };
    }

    default Boolean2IntFunction andThenInt(final Float2IntFunction after) {
        return new Boolean2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2IntFunction
            public final int get(boolean z) {
                int i;
                i = after.get(Boolean2FloatFunction.this.get(z));
                return i;
            }
        };
    }

    default Int2FloatFunction composeInt(final Int2BooleanFunction before) {
        return new Int2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.ints.Int2FloatFunction
            public final float get(int i) {
                float f;
                f = Boolean2FloatFunction.this.get(before.get(i));
                return f;
            }
        };
    }

    default Boolean2LongFunction andThenLong(final Float2LongFunction after) {
        return new Boolean2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2LongFunction
            public final long get(boolean z) {
                long j;
                j = after.get(Boolean2FloatFunction.this.get(z));
                return j;
            }
        };
    }

    default Long2FloatFunction composeLong(final Long2BooleanFunction before) {
        return new Long2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.longs.Long2FloatFunction
            public final float get(long j) {
                float f;
                f = Boolean2FloatFunction.this.get(before.get(j));
                return f;
            }
        };
    }

    default Boolean2CharFunction andThenChar(final Float2CharFunction after) {
        return new Boolean2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2CharFunction
            public final char get(boolean z) {
                char c;
                c = after.get(Boolean2FloatFunction.this.get(z));
                return c;
            }
        };
    }

    default Char2FloatFunction composeChar(final Char2BooleanFunction before) {
        return new Char2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.chars.Char2FloatFunction
            public final float get(char c) {
                float f;
                f = Boolean2FloatFunction.this.get(before.get(c));
                return f;
            }
        };
    }

    default Boolean2FloatFunction andThenFloat(final Float2FloatFunction after) {
        return new Boolean2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction
            public final float get(boolean z) {
                float f;
                f = after.get(Boolean2FloatFunction.this.get(z));
                return f;
            }
        };
    }

    default Float2FloatFunction composeFloat(final Float2BooleanFunction before) {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                float f2;
                f2 = Boolean2FloatFunction.this.get(before.get(f));
                return f2;
            }
        };
    }

    default Boolean2DoubleFunction andThenDouble(final Float2DoubleFunction after) {
        return new Boolean2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction
            public final double get(boolean z) {
                double d;
                d = after.get(Boolean2FloatFunction.this.get(z));
                return d;
            }
        };
    }

    default Double2FloatFunction composeDouble(final Double2BooleanFunction before) {
        return new Double2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.doubles.Double2FloatFunction
            public final float get(double d) {
                float f;
                f = Boolean2FloatFunction.this.get(before.get(d));
                return f;
            }
        };
    }

    default <T> Boolean2ObjectFunction<T> andThenObject(final Float2ObjectFunction<? extends T> after) {
        return new Boolean2ObjectFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction
            public final Object get(boolean z) {
                Object obj;
                obj = after.get(Boolean2FloatFunction.this.get(z));
                return obj;
            }
        };
    }

    default <T> Object2FloatFunction<T> composeObject(final Object2BooleanFunction<? super T> before) {
        return new Object2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Object2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = Boolean2FloatFunction.this.get(before.getBoolean(obj));
                return f;
            }
        };
    }

    default <T> Boolean2ReferenceFunction<T> andThenReference(final Float2ReferenceFunction<? extends T> after) {
        return new Boolean2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction
            public final Object get(boolean z) {
                Object obj;
                obj = after.get(Boolean2FloatFunction.this.get(z));
                return obj;
            }
        };
    }

    default <T> Reference2FloatFunction<T> composeReference(final Reference2BooleanFunction<? super T> before) {
        return new Reference2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.Reference2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = Boolean2FloatFunction.this.get(before.getBoolean(obj));
                return f;
            }
        };
    }
}
