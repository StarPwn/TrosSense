package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2BooleanFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2BooleanFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2BooleanFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.longs.Long2BooleanFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2BooleanFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Boolean2IntFunction extends Function<Boolean, Integer> {
    int get(boolean z);

    default int put(boolean key, int value) {
        throw new UnsupportedOperationException();
    }

    default int getOrDefault(boolean key, int defaultValue) {
        int v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default int remove(boolean key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer put(Boolean key, Integer value) {
        boolean k = key.booleanValue();
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
        boolean k = ((Boolean) key).booleanValue();
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
        boolean k = ((Boolean) key).booleanValue();
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
        boolean k = ((Boolean) key).booleanValue();
        if (containsKey(k)) {
            return Integer.valueOf(remove(k));
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

    default void defaultReturnValue(int rv) {
        throw new UnsupportedOperationException();
    }

    default int defaultReturnValue() {
        return 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Integer> compose(java.util.function.Function<? super T, ? extends Boolean> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Boolean, T> andThen(java.util.function.Function<? super Integer, ? extends T> after) {
        return super.andThen(after);
    }

    default Boolean2ByteFunction andThenByte(final Int2ByteFunction after) {
        return new Boolean2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction
            public final byte get(boolean z) {
                byte b;
                b = after.get(Boolean2IntFunction.this.get(z));
                return b;
            }
        };
    }

    default Byte2IntFunction composeByte(final Byte2BooleanFunction before) {
        return new Byte2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.bytes.Byte2IntFunction
            public final int get(byte b) {
                int i;
                i = Boolean2IntFunction.this.get(before.get(b));
                return i;
            }
        };
    }

    default Boolean2ShortFunction andThenShort(final Int2ShortFunction after) {
        return new Boolean2ShortFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction
            public final short get(boolean z) {
                short s;
                s = after.get(Boolean2IntFunction.this.get(z));
                return s;
            }
        };
    }

    default Short2IntFunction composeShort(final Short2BooleanFunction before) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = Boolean2IntFunction.this.get(before.get(s));
                return i;
            }
        };
    }

    default Boolean2IntFunction andThenInt(final Int2IntFunction after) {
        return new Boolean2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2IntFunction
            public final int get(boolean z) {
                int i;
                i = after.get(Boolean2IntFunction.this.get(z));
                return i;
            }
        };
    }

    default Int2IntFunction composeInt(final Int2BooleanFunction before) {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                int i2;
                i2 = Boolean2IntFunction.this.get(before.get(i));
                return i2;
            }
        };
    }

    default Boolean2LongFunction andThenLong(final Int2LongFunction after) {
        return new Boolean2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2LongFunction
            public final long get(boolean z) {
                long j;
                j = after.get(Boolean2IntFunction.this.get(z));
                return j;
            }
        };
    }

    default Long2IntFunction composeLong(final Long2BooleanFunction before) {
        return new Long2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.longs.Long2IntFunction
            public final int get(long j) {
                int i;
                i = Boolean2IntFunction.this.get(before.get(j));
                return i;
            }
        };
    }

    default Boolean2CharFunction andThenChar(final Int2CharFunction after) {
        return new Boolean2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2CharFunction
            public final char get(boolean z) {
                char c;
                c = after.get(Boolean2IntFunction.this.get(z));
                return c;
            }
        };
    }

    default Char2IntFunction composeChar(final Char2BooleanFunction before) {
        return new Char2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.chars.Char2IntFunction
            public final int get(char c) {
                int i;
                i = Boolean2IntFunction.this.get(before.get(c));
                return i;
            }
        };
    }

    default Boolean2FloatFunction andThenFloat(final Int2FloatFunction after) {
        return new Boolean2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction
            public final float get(boolean z) {
                float f;
                f = after.get(Boolean2IntFunction.this.get(z));
                return f;
            }
        };
    }

    default Float2IntFunction composeFloat(final Float2BooleanFunction before) {
        return new Float2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.floats.Float2IntFunction
            public final int get(float f) {
                int i;
                i = Boolean2IntFunction.this.get(before.get(f));
                return i;
            }
        };
    }

    default Boolean2DoubleFunction andThenDouble(final Int2DoubleFunction after) {
        return new Boolean2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction
            public final double get(boolean z) {
                double d;
                d = after.get(Boolean2IntFunction.this.get(z));
                return d;
            }
        };
    }

    default Double2IntFunction composeDouble(final Double2BooleanFunction before) {
        return new Double2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.doubles.Double2IntFunction
            public final int get(double d) {
                int i;
                i = Boolean2IntFunction.this.get(before.get(d));
                return i;
            }
        };
    }

    default <T> Boolean2ObjectFunction<T> andThenObject(final Int2ObjectFunction<? extends T> after) {
        return new Boolean2ObjectFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction
            public final Object get(boolean z) {
                Object obj;
                obj = after.get(Boolean2IntFunction.this.get(z));
                return obj;
            }
        };
    }

    default <T> Object2IntFunction<T> composeObject(final Object2BooleanFunction<? super T> before) {
        return new Object2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = Boolean2IntFunction.this.get(before.getBoolean(obj));
                return i;
            }
        };
    }

    default <T> Boolean2ReferenceFunction<T> andThenReference(final Int2ReferenceFunction<? extends T> after) {
        return new Boolean2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction
            public final Object get(boolean z) {
                Object obj;
                obj = after.get(Boolean2IntFunction.this.get(z));
                return obj;
            }
        };
    }

    default <T> Reference2IntFunction<T> composeReference(final Reference2BooleanFunction<? super T> before) {
        return new Reference2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2IntFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Reference2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = Boolean2IntFunction.this.get(before.getBoolean(obj));
                return i;
            }
        };
    }
}
