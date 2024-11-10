package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2BooleanFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.doubles.Double2BooleanFunction;
import it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2ObjectFunction;
import it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
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

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Boolean2DoubleFunction extends Function<Boolean, Double> {
    double get(boolean z);

    default double put(boolean key, double value) {
        throw new UnsupportedOperationException();
    }

    default double getOrDefault(boolean key, double defaultValue) {
        double v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default double remove(boolean key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double put(Boolean key, Double value) {
        boolean k = key.booleanValue();
        boolean containsKey = containsKey(k);
        double v = put(k, value.doubleValue());
        if (containsKey) {
            return Double.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double get(Object key) {
        if (key == null) {
            return null;
        }
        boolean k = ((Boolean) key).booleanValue();
        double v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Double.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        boolean k = ((Boolean) key).booleanValue();
        double v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Double.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double remove(Object key) {
        if (key == null) {
            return null;
        }
        boolean k = ((Boolean) key).booleanValue();
        if (containsKey(k)) {
            return Double.valueOf(remove(k));
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

    default void defaultReturnValue(double rv) {
        throw new UnsupportedOperationException();
    }

    default double defaultReturnValue() {
        return 0.0d;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Double> compose(java.util.function.Function<? super T, ? extends Boolean> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Boolean, T> andThen(java.util.function.Function<? super Double, ? extends T> after) {
        return super.andThen(after);
    }

    default Boolean2ByteFunction andThenByte(final Double2ByteFunction after) {
        return new Boolean2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction
            public final byte get(boolean z) {
                byte b;
                b = after.get(Boolean2DoubleFunction.this.get(z));
                return b;
            }
        };
    }

    default Byte2DoubleFunction composeByte(final Byte2BooleanFunction before) {
        return new Byte2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction
            public final double get(byte b) {
                double d;
                d = Boolean2DoubleFunction.this.get(before.get(b));
                return d;
            }
        };
    }

    default Boolean2ShortFunction andThenShort(final Double2ShortFunction after) {
        return new Boolean2ShortFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction
            public final short get(boolean z) {
                short s;
                s = after.get(Boolean2DoubleFunction.this.get(z));
                return s;
            }
        };
    }

    default Short2DoubleFunction composeShort(final Short2BooleanFunction before) {
        return new Short2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.shorts.Short2DoubleFunction
            public final double get(short s) {
                double d;
                d = Boolean2DoubleFunction.this.get(before.get(s));
                return d;
            }
        };
    }

    default Boolean2IntFunction andThenInt(final Double2IntFunction after) {
        return new Boolean2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2IntFunction
            public final int get(boolean z) {
                int i;
                i = after.get(Boolean2DoubleFunction.this.get(z));
                return i;
            }
        };
    }

    default Int2DoubleFunction composeInt(final Int2BooleanFunction before) {
        return new Int2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.ints.Int2DoubleFunction
            public final double get(int i) {
                double d;
                d = Boolean2DoubleFunction.this.get(before.get(i));
                return d;
            }
        };
    }

    default Boolean2LongFunction andThenLong(final Double2LongFunction after) {
        return new Boolean2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2LongFunction
            public final long get(boolean z) {
                long j;
                j = after.get(Boolean2DoubleFunction.this.get(z));
                return j;
            }
        };
    }

    default Long2DoubleFunction composeLong(final Long2BooleanFunction before) {
        return new Long2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.longs.Long2DoubleFunction
            public final double get(long j) {
                double d;
                d = Boolean2DoubleFunction.this.get(before.get(j));
                return d;
            }
        };
    }

    default Boolean2CharFunction andThenChar(final Double2CharFunction after) {
        return new Boolean2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2CharFunction
            public final char get(boolean z) {
                char c;
                c = after.get(Boolean2DoubleFunction.this.get(z));
                return c;
            }
        };
    }

    default Char2DoubleFunction composeChar(final Char2BooleanFunction before) {
        return new Char2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.chars.Char2DoubleFunction
            public final double get(char c) {
                double d;
                d = Boolean2DoubleFunction.this.get(before.get(c));
                return d;
            }
        };
    }

    default Boolean2FloatFunction andThenFloat(final Double2FloatFunction after) {
        return new Boolean2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction
            public final float get(boolean z) {
                float f;
                f = after.get(Boolean2DoubleFunction.this.get(z));
                return f;
            }
        };
    }

    default Float2DoubleFunction composeFloat(final Float2BooleanFunction before) {
        return new Float2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.floats.Float2DoubleFunction
            public final double get(float f) {
                double d;
                d = Boolean2DoubleFunction.this.get(before.get(f));
                return d;
            }
        };
    }

    default Boolean2DoubleFunction andThenDouble(final Double2DoubleFunction after) {
        return new Boolean2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction
            public final double get(boolean z) {
                double d;
                d = after.get(Boolean2DoubleFunction.this.get(z));
                return d;
            }
        };
    }

    default Double2DoubleFunction composeDouble(final Double2BooleanFunction before) {
        return new Double2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.doubles.Double2DoubleFunction
            public final double get(double d) {
                double d2;
                d2 = Boolean2DoubleFunction.this.get(before.get(d));
                return d2;
            }
        };
    }

    default <T> Boolean2ObjectFunction<T> andThenObject(final Double2ObjectFunction<? extends T> after) {
        return new Boolean2ObjectFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction
            public final Object get(boolean z) {
                Object obj;
                obj = after.get(Boolean2DoubleFunction.this.get(z));
                return obj;
            }
        };
    }

    default <T> Object2DoubleFunction<T> composeObject(final Object2BooleanFunction<? super T> before) {
        return new Object2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.objects.Object2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = Boolean2DoubleFunction.this.get(before.getBoolean(obj));
                return d;
            }
        };
    }

    default <T> Boolean2ReferenceFunction<T> andThenReference(final Double2ReferenceFunction<? extends T> after) {
        return new Boolean2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction
            public final Object get(boolean z) {
                Object obj;
                obj = after.get(Boolean2DoubleFunction.this.get(z));
                return obj;
            }
        };
    }

    default <T> Reference2DoubleFunction<T> composeReference(final Reference2BooleanFunction<? super T> before) {
        return new Reference2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Reference2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = Boolean2DoubleFunction.this.get(before.getBoolean(obj));
                return d;
            }
        };
    }
}
