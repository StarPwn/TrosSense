package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction;
import it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import it.unimi.dsi.fastutil.chars.Char2BooleanFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2BooleanFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2BooleanFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.longs.Long2BooleanFunction;
import it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import it.unimi.dsi.fastutil.longs.Long2CharFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.longs.Long2LongFunction;
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2BooleanFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Boolean2LongFunction extends Function<Boolean, Long> {
    long get(boolean z);

    default long put(boolean key, long value) {
        throw new UnsupportedOperationException();
    }

    default long getOrDefault(boolean key, long defaultValue) {
        long v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default long remove(boolean key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long put(Boolean key, Long value) {
        boolean k = key.booleanValue();
        boolean containsKey = containsKey(k);
        long v = put(k, value.longValue());
        if (containsKey) {
            return Long.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long get(Object key) {
        if (key == null) {
            return null;
        }
        boolean k = ((Boolean) key).booleanValue();
        long v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Long.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        boolean k = ((Boolean) key).booleanValue();
        long v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Long.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long remove(Object key) {
        if (key == null) {
            return null;
        }
        boolean k = ((Boolean) key).booleanValue();
        if (containsKey(k)) {
            return Long.valueOf(remove(k));
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

    default void defaultReturnValue(long rv) {
        throw new UnsupportedOperationException();
    }

    default long defaultReturnValue() {
        return 0L;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Long> compose(java.util.function.Function<? super T, ? extends Boolean> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Boolean, T> andThen(java.util.function.Function<? super Long, ? extends T> after) {
        return super.andThen(after);
    }

    default Boolean2ByteFunction andThenByte(final Long2ByteFunction after) {
        return new Boolean2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction
            public final byte get(boolean z) {
                byte b;
                b = after.get(Boolean2LongFunction.this.get(z));
                return b;
            }
        };
    }

    default Byte2LongFunction composeByte(final Byte2BooleanFunction before) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = Boolean2LongFunction.this.get(before.get(b));
                return j;
            }
        };
    }

    default Boolean2ShortFunction andThenShort(final Long2ShortFunction after) {
        return new Boolean2ShortFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction
            public final short get(boolean z) {
                short s;
                s = after.get(Boolean2LongFunction.this.get(z));
                return s;
            }
        };
    }

    default Short2LongFunction composeShort(final Short2BooleanFunction before) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = Boolean2LongFunction.this.get(before.get(s));
                return j;
            }
        };
    }

    default Boolean2IntFunction andThenInt(final Long2IntFunction after) {
        return new Boolean2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2IntFunction
            public final int get(boolean z) {
                int i;
                i = after.get(Boolean2LongFunction.this.get(z));
                return i;
            }
        };
    }

    default Int2LongFunction composeInt(final Int2BooleanFunction before) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = Boolean2LongFunction.this.get(before.get(i));
                return j;
            }
        };
    }

    default Boolean2LongFunction andThenLong(final Long2LongFunction after) {
        return new Boolean2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2LongFunction
            public final long get(boolean z) {
                long j;
                j = after.get(Boolean2LongFunction.this.get(z));
                return j;
            }
        };
    }

    default Long2LongFunction composeLong(final Long2BooleanFunction before) {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                long j2;
                j2 = Boolean2LongFunction.this.get(before.get(j));
                return j2;
            }
        };
    }

    default Boolean2CharFunction andThenChar(final Long2CharFunction after) {
        return new Boolean2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2CharFunction
            public final char get(boolean z) {
                char c;
                c = after.get(Boolean2LongFunction.this.get(z));
                return c;
            }
        };
    }

    default Char2LongFunction composeChar(final Char2BooleanFunction before) {
        return new Char2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.chars.Char2LongFunction
            public final long get(char c) {
                long j;
                j = Boolean2LongFunction.this.get(before.get(c));
                return j;
            }
        };
    }

    default Boolean2FloatFunction andThenFloat(final Long2FloatFunction after) {
        return new Boolean2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction
            public final float get(boolean z) {
                float f;
                f = after.get(Boolean2LongFunction.this.get(z));
                return f;
            }
        };
    }

    default Float2LongFunction composeFloat(final Float2BooleanFunction before) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = Boolean2LongFunction.this.get(before.get(f));
                return j;
            }
        };
    }

    default Boolean2DoubleFunction andThenDouble(final Long2DoubleFunction after) {
        return new Boolean2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction
            public final double get(boolean z) {
                double d;
                d = after.get(Boolean2LongFunction.this.get(z));
                return d;
            }
        };
    }

    default Double2LongFunction composeDouble(final Double2BooleanFunction before) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = Boolean2LongFunction.this.get(before.get(d));
                return j;
            }
        };
    }

    default <T> Boolean2ObjectFunction<T> andThenObject(final Long2ObjectFunction<? extends T> after) {
        return new Boolean2ObjectFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction
            public final Object get(boolean z) {
                Object obj;
                obj = after.get(Boolean2LongFunction.this.get(z));
                return obj;
            }
        };
    }

    default <T> Object2LongFunction<T> composeObject(final Object2BooleanFunction<? super T> before) {
        return new Object2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.objects.Object2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Boolean2LongFunction.this.get(before.getBoolean(obj));
                return j;
            }
        };
    }

    default <T> Boolean2ReferenceFunction<T> andThenReference(final Long2ReferenceFunction<? extends T> after) {
        return new Boolean2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction
            public final Object get(boolean z) {
                Object obj;
                obj = after.get(Boolean2LongFunction.this.get(z));
                return obj;
            }
        };
    }

    default <T> Reference2LongFunction<T> composeReference(final Reference2BooleanFunction<? super T> before) {
        return new Reference2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2LongFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.objects.Reference2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Boolean2LongFunction.this.get(before.getBoolean(obj));
                return j;
            }
        };
    }
}
