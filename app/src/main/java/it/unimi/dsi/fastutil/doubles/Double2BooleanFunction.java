package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;
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
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2BooleanFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.floats.Float2BooleanFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2BooleanFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2BooleanFunction;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import java.util.function.DoublePredicate;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Double2BooleanFunction extends Function<Double, Boolean>, DoublePredicate {
    boolean get(double d);

    @Override // java.util.function.DoublePredicate
    default boolean test(double operand) {
        return get(operand);
    }

    default boolean put(double key, boolean value) {
        throw new UnsupportedOperationException();
    }

    default boolean getOrDefault(double key, boolean defaultValue) {
        boolean v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default boolean remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Boolean put(Double key, Boolean value) {
        double k = key.doubleValue();
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
        double k = ((Double) key).doubleValue();
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
        double k = ((Double) key).doubleValue();
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
        double k = ((Double) key).doubleValue();
        if (containsKey(k)) {
            return Boolean.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(double key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Double) key).doubleValue());
    }

    default void defaultReturnValue(boolean rv) {
        throw new UnsupportedOperationException();
    }

    default boolean defaultReturnValue() {
        return false;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Boolean> compose(java.util.function.Function<? super T, ? extends Double> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Double, T> andThen(java.util.function.Function<? super Boolean, ? extends T> after) {
        return super.andThen(after);
    }

    default Double2ByteFunction andThenByte(final Boolean2ByteFunction after) {
        return new Double2ByteFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.doubles.Double2ByteFunction
            public final byte get(double d) {
                byte b;
                b = after.get(Double2BooleanFunction.this.get(d));
                return b;
            }
        };
    }

    default Byte2BooleanFunction composeByte(final Byte2DoubleFunction before) {
        return new Byte2BooleanFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction
            public final boolean get(byte b) {
                boolean z;
                z = Double2BooleanFunction.this.get(before.get(b));
                return z;
            }
        };
    }

    default Double2ShortFunction andThenShort(final Boolean2ShortFunction after) {
        return new Double2ShortFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.doubles.Double2ShortFunction
            public final short get(double d) {
                short s;
                s = after.get(Double2BooleanFunction.this.get(d));
                return s;
            }
        };
    }

    default Short2BooleanFunction composeShort(final Short2DoubleFunction before) {
        return new Short2BooleanFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.shorts.Short2BooleanFunction
            public final boolean get(short s) {
                boolean z;
                z = Double2BooleanFunction.this.get(before.get(s));
                return z;
            }
        };
    }

    default Double2IntFunction andThenInt(final Boolean2IntFunction after) {
        return new Double2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.doubles.Double2IntFunction
            public final int get(double d) {
                int i;
                i = after.get(Double2BooleanFunction.this.get(d));
                return i;
            }
        };
    }

    default Int2BooleanFunction composeInt(final Int2DoubleFunction before) {
        return new Int2BooleanFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.ints.Int2BooleanFunction
            public final boolean get(int i) {
                boolean z;
                z = Double2BooleanFunction.this.get(before.get(i));
                return z;
            }
        };
    }

    default Double2LongFunction andThenLong(final Boolean2LongFunction after) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = after.get(Double2BooleanFunction.this.get(d));
                return j;
            }
        };
    }

    default Long2BooleanFunction composeLong(final Long2DoubleFunction before) {
        return new Long2BooleanFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.longs.Long2BooleanFunction
            public final boolean get(long j) {
                boolean z;
                z = Double2BooleanFunction.this.get(before.get(j));
                return z;
            }
        };
    }

    default Double2CharFunction andThenChar(final Boolean2CharFunction after) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = after.get(Double2BooleanFunction.this.get(d));
                return c;
            }
        };
    }

    default Char2BooleanFunction composeChar(final Char2DoubleFunction before) {
        return new Char2BooleanFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.chars.Char2BooleanFunction
            public final boolean get(char c) {
                boolean z;
                z = Double2BooleanFunction.this.get(before.get(c));
                return z;
            }
        };
    }

    default Double2FloatFunction andThenFloat(final Boolean2FloatFunction after) {
        return new Double2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.doubles.Double2FloatFunction
            public final float get(double d) {
                float f;
                f = after.get(Double2BooleanFunction.this.get(d));
                return f;
            }
        };
    }

    default Float2BooleanFunction composeFloat(final Float2DoubleFunction before) {
        return new Float2BooleanFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.floats.Float2BooleanFunction
            public final boolean get(float f) {
                boolean z;
                z = Double2BooleanFunction.this.get(before.get(f));
                return z;
            }
        };
    }

    default Double2DoubleFunction andThenDouble(final Boolean2DoubleFunction after) {
        return new Double2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.doubles.Double2DoubleFunction
            public final double get(double d) {
                double d2;
                d2 = after.get(Double2BooleanFunction.this.get(d));
                return d2;
            }
        };
    }

    default Double2BooleanFunction composeDouble(final Double2DoubleFunction before) {
        return new Double2BooleanFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.doubles.Double2BooleanFunction
            public final boolean get(double d) {
                boolean z;
                z = Double2BooleanFunction.this.get(before.get(d));
                return z;
            }
        };
    }

    default <T> Double2ObjectFunction<T> andThenObject(final Boolean2ObjectFunction<? extends T> after) {
        return new Double2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.doubles.Double2ObjectFunction
            public final Object get(double d) {
                Object obj;
                obj = after.get(Double2BooleanFunction.this.get(d));
                return obj;
            }
        };
    }

    default <T> Object2BooleanFunction<T> composeObject(final Object2DoubleFunction<? super T> before) {
        return new Object2BooleanFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.objects.Object2BooleanFunction
            public final boolean getBoolean(Object obj) {
                boolean z;
                z = Double2BooleanFunction.this.get(before.getDouble(obj));
                return z;
            }
        };
    }

    default <T> Double2ReferenceFunction<T> andThenReference(final Boolean2ReferenceFunction<? extends T> after) {
        return new Double2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction
            public final Object get(double d) {
                Object obj;
                obj = after.get(Double2BooleanFunction.this.get(d));
                return obj;
            }
        };
    }

    default <T> Reference2BooleanFunction<T> composeReference(final Reference2DoubleFunction<? super T> before) {
        return new Reference2BooleanFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2BooleanFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.objects.Reference2BooleanFunction
            public final boolean getBoolean(Object obj) {
                boolean z;
                z = Double2BooleanFunction.this.get(before.getDouble(obj));
                return z;
            }
        };
    }
}
