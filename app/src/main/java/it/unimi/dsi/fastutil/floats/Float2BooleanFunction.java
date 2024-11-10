package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2CharFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2IntFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2LongFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction;
import it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2BooleanFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2BooleanFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
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
import java.util.function.DoublePredicate;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Float2BooleanFunction extends Function<Float, Boolean>, DoublePredicate {
    boolean get(float f);

    @Override // java.util.function.DoublePredicate
    @Deprecated
    default boolean test(double operand) {
        return get(SafeMath.safeDoubleToFloat(operand));
    }

    default boolean put(float key, boolean value) {
        throw new UnsupportedOperationException();
    }

    default boolean getOrDefault(float key, boolean defaultValue) {
        boolean v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default boolean remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Boolean put(Float key, Boolean value) {
        float k = key.floatValue();
        boolean containsKey = containsKey(k);
        boolean v = put(k, value.booleanValue());
        if (containsKey) {
            return Boolean.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Boolean get(Object key) {
        if (key == null) {
            return null;
        }
        float k = ((Float) key).floatValue();
        boolean v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Boolean.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Boolean getOrDefault(Object key, Boolean defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        float k = ((Float) key).floatValue();
        boolean v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Boolean.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Boolean remove(Object key) {
        if (key == null) {
            return null;
        }
        float k = ((Float) key).floatValue();
        if (containsKey(k)) {
            return Boolean.valueOf(remove(k));
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

    default void defaultReturnValue(boolean rv) {
        throw new UnsupportedOperationException();
    }

    default boolean defaultReturnValue() {
        return false;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Boolean> compose(java.util.function.Function<? super T, ? extends Float> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Float, T> andThen(java.util.function.Function<? super Boolean, ? extends T> after) {
        return super.andThen(after);
    }

    default Float2ByteFunction andThenByte(final Boolean2ByteFunction after) {
        return new Float2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.floats.Float2ByteFunction
            public final byte get(float f) {
                byte b;
                b = after.get(Float2BooleanFunction.this.get(f));
                return b;
            }
        };
    }

    default Byte2BooleanFunction composeByte(final Byte2FloatFunction before) {
        return new Byte2BooleanFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction
            public final boolean get(byte b) {
                boolean z;
                z = Float2BooleanFunction.this.get(before.get(b));
                return z;
            }
        };
    }

    default Float2ShortFunction andThenShort(final Boolean2ShortFunction after) {
        return new Float2ShortFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.floats.Float2ShortFunction
            public final short get(float f) {
                short s;
                s = after.get(Float2BooleanFunction.this.get(f));
                return s;
            }
        };
    }

    default Short2BooleanFunction composeShort(final Short2FloatFunction before) {
        return new Short2BooleanFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.shorts.Short2BooleanFunction
            public final boolean get(short s) {
                boolean z;
                z = Float2BooleanFunction.this.get(before.get(s));
                return z;
            }
        };
    }

    default Float2IntFunction andThenInt(final Boolean2IntFunction after) {
        return new Float2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.floats.Float2IntFunction
            public final int get(float f) {
                int i;
                i = after.get(Float2BooleanFunction.this.get(f));
                return i;
            }
        };
    }

    default Int2BooleanFunction composeInt(final Int2FloatFunction before) {
        return new Int2BooleanFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.ints.Int2BooleanFunction
            public final boolean get(int i) {
                boolean z;
                z = Float2BooleanFunction.this.get(before.get(i));
                return z;
            }
        };
    }

    default Float2LongFunction andThenLong(final Boolean2LongFunction after) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = after.get(Float2BooleanFunction.this.get(f));
                return j;
            }
        };
    }

    default Long2BooleanFunction composeLong(final Long2FloatFunction before) {
        return new Long2BooleanFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.longs.Long2BooleanFunction
            public final boolean get(long j) {
                boolean z;
                z = Float2BooleanFunction.this.get(before.get(j));
                return z;
            }
        };
    }

    default Float2CharFunction andThenChar(final Boolean2CharFunction after) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = after.get(Float2BooleanFunction.this.get(f));
                return c;
            }
        };
    }

    default Char2BooleanFunction composeChar(final Char2FloatFunction before) {
        return new Char2BooleanFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.chars.Char2BooleanFunction
            public final boolean get(char c) {
                boolean z;
                z = Float2BooleanFunction.this.get(before.get(c));
                return z;
            }
        };
    }

    default Float2FloatFunction andThenFloat(final Boolean2FloatFunction after) {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                float f2;
                f2 = after.get(Float2BooleanFunction.this.get(f));
                return f2;
            }
        };
    }

    default Float2BooleanFunction composeFloat(final Float2FloatFunction before) {
        return new Float2BooleanFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.floats.Float2BooleanFunction
            public final boolean get(float f) {
                boolean z;
                z = Float2BooleanFunction.this.get(before.get(f));
                return z;
            }
        };
    }

    default Float2DoubleFunction andThenDouble(final Boolean2DoubleFunction after) {
        return new Float2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.floats.Float2DoubleFunction
            public final double get(float f) {
                double d;
                d = after.get(Float2BooleanFunction.this.get(f));
                return d;
            }
        };
    }

    default Double2BooleanFunction composeDouble(final Double2FloatFunction before) {
        return new Double2BooleanFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.doubles.Double2BooleanFunction
            public final boolean get(double d) {
                boolean z;
                z = Float2BooleanFunction.this.get(before.get(d));
                return z;
            }
        };
    }

    default <T> Float2ObjectFunction<T> andThenObject(final Boolean2ObjectFunction<? extends T> after) {
        return new Float2ObjectFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.floats.Float2ObjectFunction
            public final Object get(float f) {
                Object obj;
                obj = after.get(Float2BooleanFunction.this.get(f));
                return obj;
            }
        };
    }

    default <T> Object2BooleanFunction<T> composeObject(final Object2FloatFunction<? super T> before) {
        return new Object2BooleanFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.objects.Object2BooleanFunction
            public final boolean getBoolean(Object obj) {
                boolean z;
                z = Float2BooleanFunction.this.get(before.getFloat(obj));
                return z;
            }
        };
    }

    default <T> Float2ReferenceFunction<T> andThenReference(final Boolean2ReferenceFunction<? extends T> after) {
        return new Float2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.floats.Float2ReferenceFunction
            public final Object get(float f) {
                Object obj;
                obj = after.get(Float2BooleanFunction.this.get(f));
                return obj;
            }
        };
    }

    default <T> Reference2BooleanFunction<T> composeReference(final Reference2FloatFunction<? super T> before) {
        return new Reference2BooleanFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2BooleanFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.objects.Reference2BooleanFunction
            public final boolean getBoolean(Object obj) {
                boolean z;
                z = Float2BooleanFunction.this.get(before.getFloat(obj));
                return z;
            }
        };
    }
}
