package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction;
import it.unimi.dsi.fastutil.chars.Char2BooleanFunction;
import it.unimi.dsi.fastutil.doubles.Double2BooleanFunction;
import it.unimi.dsi.fastutil.floats.Float2BooleanFunction;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import it.unimi.dsi.fastutil.longs.Long2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import it.unimi.dsi.fastutil.shorts.Short2BooleanFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Boolean2BooleanFunction extends Function<Boolean, Boolean> {
    boolean get(boolean z);

    default boolean put(boolean key, boolean value) {
        throw new UnsupportedOperationException();
    }

    default boolean getOrDefault(boolean key, boolean defaultValue) {
        boolean v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default boolean remove(boolean key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Boolean put(Boolean key, Boolean value) {
        boolean k = key.booleanValue();
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
        boolean k = ((Boolean) key).booleanValue();
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
        boolean k = ((Boolean) key).booleanValue();
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
        boolean k = ((Boolean) key).booleanValue();
        if (containsKey(k)) {
            return Boolean.valueOf(remove(k));
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

    default void defaultReturnValue(boolean rv) {
        throw new UnsupportedOperationException();
    }

    default boolean defaultReturnValue() {
        return false;
    }

    static Boolean2BooleanFunction identity() {
        return new Boolean2BooleanFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda18
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction
            public final boolean get(boolean z) {
                return Boolean2BooleanFunction.lambda$identity$0(z);
            }
        };
    }

    static /* synthetic */ boolean lambda$identity$0(boolean k) {
        return k;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Boolean> compose(java.util.function.Function<? super T, ? extends Boolean> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Boolean, T> andThen(java.util.function.Function<? super Boolean, ? extends T> after) {
        return super.andThen(after);
    }

    default Boolean2ByteFunction andThenByte(final Boolean2ByteFunction after) {
        return new Boolean2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction
            public final byte get(boolean z) {
                byte b;
                b = after.get(Boolean2BooleanFunction.this.get(z));
                return b;
            }
        };
    }

    default Byte2BooleanFunction composeByte(final Byte2BooleanFunction before) {
        return new Byte2BooleanFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction
            public final boolean get(byte b) {
                boolean z;
                z = Boolean2BooleanFunction.this.get(before.get(b));
                return z;
            }
        };
    }

    default Boolean2ShortFunction andThenShort(final Boolean2ShortFunction after) {
        return new Boolean2ShortFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction
            public final short get(boolean z) {
                short s;
                s = after.get(Boolean2BooleanFunction.this.get(z));
                return s;
            }
        };
    }

    default Short2BooleanFunction composeShort(final Short2BooleanFunction before) {
        return new Short2BooleanFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.shorts.Short2BooleanFunction
            public final boolean get(short s) {
                boolean z;
                z = Boolean2BooleanFunction.this.get(before.get(s));
                return z;
            }
        };
    }

    default Boolean2IntFunction andThenInt(final Boolean2IntFunction after) {
        return new Boolean2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2IntFunction
            public final int get(boolean z) {
                int i;
                i = after.get(Boolean2BooleanFunction.this.get(z));
                return i;
            }
        };
    }

    default Int2BooleanFunction composeInt(final Int2BooleanFunction before) {
        return new Int2BooleanFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.ints.Int2BooleanFunction
            public final boolean get(int i) {
                boolean z;
                z = Boolean2BooleanFunction.this.get(before.get(i));
                return z;
            }
        };
    }

    default Boolean2LongFunction andThenLong(final Boolean2LongFunction after) {
        return new Boolean2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2LongFunction
            public final long get(boolean z) {
                long j;
                j = after.get(Boolean2BooleanFunction.this.get(z));
                return j;
            }
        };
    }

    default Long2BooleanFunction composeLong(final Long2BooleanFunction before) {
        return new Long2BooleanFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.longs.Long2BooleanFunction
            public final boolean get(long j) {
                boolean z;
                z = Boolean2BooleanFunction.this.get(before.get(j));
                return z;
            }
        };
    }

    default Boolean2CharFunction andThenChar(final Boolean2CharFunction after) {
        return new Boolean2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2CharFunction
            public final char get(boolean z) {
                char c;
                c = after.get(Boolean2BooleanFunction.this.get(z));
                return c;
            }
        };
    }

    default Char2BooleanFunction composeChar(final Char2BooleanFunction before) {
        return new Char2BooleanFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.chars.Char2BooleanFunction
            public final boolean get(char c) {
                boolean z;
                z = Boolean2BooleanFunction.this.get(before.get(c));
                return z;
            }
        };
    }

    default Boolean2FloatFunction andThenFloat(final Boolean2FloatFunction after) {
        return new Boolean2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction
            public final float get(boolean z) {
                float f;
                f = after.get(Boolean2BooleanFunction.this.get(z));
                return f;
            }
        };
    }

    default Float2BooleanFunction composeFloat(final Float2BooleanFunction before) {
        return new Float2BooleanFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.floats.Float2BooleanFunction
            public final boolean get(float f) {
                boolean z;
                z = Boolean2BooleanFunction.this.get(before.get(f));
                return z;
            }
        };
    }

    default Boolean2DoubleFunction andThenDouble(final Boolean2DoubleFunction after) {
        return new Boolean2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction
            public final double get(boolean z) {
                double d;
                d = after.get(Boolean2BooleanFunction.this.get(z));
                return d;
            }
        };
    }

    default Double2BooleanFunction composeDouble(final Double2BooleanFunction before) {
        return new Double2BooleanFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.doubles.Double2BooleanFunction
            public final boolean get(double d) {
                boolean z;
                z = Boolean2BooleanFunction.this.get(before.get(d));
                return z;
            }
        };
    }

    default <T> Boolean2ObjectFunction<T> andThenObject(final Boolean2ObjectFunction<? extends T> after) {
        return new Boolean2ObjectFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction
            public final Object get(boolean z) {
                Object obj;
                obj = after.get(Boolean2BooleanFunction.this.get(z));
                return obj;
            }
        };
    }

    default <T> Object2BooleanFunction<T> composeObject(final Object2BooleanFunction<? super T> before) {
        return new Object2BooleanFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.objects.Object2BooleanFunction
            public final boolean getBoolean(Object obj) {
                boolean z;
                z = Boolean2BooleanFunction.this.get(before.getBoolean(obj));
                return z;
            }
        };
    }

    default <T> Boolean2ReferenceFunction<T> andThenReference(final Boolean2ReferenceFunction<? extends T> after) {
        return new Boolean2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction
            public final Object get(boolean z) {
                Object obj;
                obj = after.get(Boolean2BooleanFunction.this.get(z));
                return obj;
            }
        };
    }

    default <T> Reference2BooleanFunction<T> composeReference(final Reference2BooleanFunction<? super T> before) {
        return new Reference2BooleanFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2BooleanFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.objects.Reference2BooleanFunction
            public final boolean getBoolean(Object obj) {
                boolean z;
                z = Boolean2BooleanFunction.this.get(before.getBoolean(obj));
                return z;
            }
        };
    }
}
