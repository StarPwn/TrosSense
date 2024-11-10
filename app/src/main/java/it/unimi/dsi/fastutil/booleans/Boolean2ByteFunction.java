package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ByteFunction;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.chars.Char2BooleanFunction;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.doubles.Double2BooleanFunction;
import it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2BooleanFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.longs.Long2BooleanFunction;
import it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import it.unimi.dsi.fastutil.shorts.Short2BooleanFunction;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Boolean2ByteFunction extends Function<Boolean, Byte> {
    byte get(boolean z);

    default byte put(boolean key, byte value) {
        throw new UnsupportedOperationException();
    }

    default byte getOrDefault(boolean key, byte defaultValue) {
        byte v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default byte remove(boolean key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte put(Boolean key, Byte value) {
        boolean k = key.booleanValue();
        boolean containsKey = containsKey(k);
        byte v = put(k, value.byteValue());
        if (containsKey) {
            return Byte.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte get(Object key) {
        if (key == null) {
            return null;
        }
        boolean k = ((Boolean) key).booleanValue();
        byte v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Byte.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        boolean k = ((Boolean) key).booleanValue();
        byte v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Byte.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte remove(Object key) {
        if (key == null) {
            return null;
        }
        boolean k = ((Boolean) key).booleanValue();
        if (containsKey(k)) {
            return Byte.valueOf(remove(k));
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

    default void defaultReturnValue(byte rv) {
        throw new UnsupportedOperationException();
    }

    default byte defaultReturnValue() {
        return (byte) 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Byte> compose(java.util.function.Function<? super T, ? extends Boolean> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Boolean, T> andThen(java.util.function.Function<? super Byte, ? extends T> after) {
        return super.andThen(after);
    }

    default Boolean2ByteFunction andThenByte(final Byte2ByteFunction after) {
        return new Boolean2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction
            public final byte get(boolean z) {
                byte b;
                b = after.get(Boolean2ByteFunction.this.get(z));
                return b;
            }
        };
    }

    default Byte2ByteFunction composeByte(final Byte2BooleanFunction before) {
        return new Byte2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ByteFunction
            public final byte get(byte b) {
                byte b2;
                b2 = Boolean2ByteFunction.this.get(before.get(b));
                return b2;
            }
        };
    }

    default Boolean2ShortFunction andThenShort(final Byte2ShortFunction after) {
        return new Boolean2ShortFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction
            public final short get(boolean z) {
                short s;
                s = after.get(Boolean2ByteFunction.this.get(z));
                return s;
            }
        };
    }

    default Short2ByteFunction composeShort(final Short2BooleanFunction before) {
        return new Short2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.shorts.Short2ByteFunction
            public final byte get(short s) {
                byte b;
                b = Boolean2ByteFunction.this.get(before.get(s));
                return b;
            }
        };
    }

    default Boolean2IntFunction andThenInt(final Byte2IntFunction after) {
        return new Boolean2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2IntFunction
            public final int get(boolean z) {
                int i;
                i = after.get(Boolean2ByteFunction.this.get(z));
                return i;
            }
        };
    }

    default Int2ByteFunction composeInt(final Int2BooleanFunction before) {
        return new Int2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.ints.Int2ByteFunction
            public final byte get(int i) {
                byte b;
                b = Boolean2ByteFunction.this.get(before.get(i));
                return b;
            }
        };
    }

    default Boolean2LongFunction andThenLong(final Byte2LongFunction after) {
        return new Boolean2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2LongFunction
            public final long get(boolean z) {
                long j;
                j = after.get(Boolean2ByteFunction.this.get(z));
                return j;
            }
        };
    }

    default Long2ByteFunction composeLong(final Long2BooleanFunction before) {
        return new Long2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.longs.Long2ByteFunction
            public final byte get(long j) {
                byte b;
                b = Boolean2ByteFunction.this.get(before.get(j));
                return b;
            }
        };
    }

    default Boolean2CharFunction andThenChar(final Byte2CharFunction after) {
        return new Boolean2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2CharFunction
            public final char get(boolean z) {
                char c;
                c = after.get(Boolean2ByteFunction.this.get(z));
                return c;
            }
        };
    }

    default Char2ByteFunction composeChar(final Char2BooleanFunction before) {
        return new Char2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.chars.Char2ByteFunction
            public final byte get(char c) {
                byte b;
                b = Boolean2ByteFunction.this.get(before.get(c));
                return b;
            }
        };
    }

    default Boolean2FloatFunction andThenFloat(final Byte2FloatFunction after) {
        return new Boolean2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction
            public final float get(boolean z) {
                float f;
                f = after.get(Boolean2ByteFunction.this.get(z));
                return f;
            }
        };
    }

    default Float2ByteFunction composeFloat(final Float2BooleanFunction before) {
        return new Float2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.floats.Float2ByteFunction
            public final byte get(float f) {
                byte b;
                b = Boolean2ByteFunction.this.get(before.get(f));
                return b;
            }
        };
    }

    default Boolean2DoubleFunction andThenDouble(final Byte2DoubleFunction after) {
        return new Boolean2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction
            public final double get(boolean z) {
                double d;
                d = after.get(Boolean2ByteFunction.this.get(z));
                return d;
            }
        };
    }

    default Double2ByteFunction composeDouble(final Double2BooleanFunction before) {
        return new Double2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.doubles.Double2ByteFunction
            public final byte get(double d) {
                byte b;
                b = Boolean2ByteFunction.this.get(before.get(d));
                return b;
            }
        };
    }

    default <T> Boolean2ObjectFunction<T> andThenObject(final Byte2ObjectFunction<? extends T> after) {
        return new Boolean2ObjectFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction
            public final Object get(boolean z) {
                Object obj;
                obj = after.get(Boolean2ByteFunction.this.get(z));
                return obj;
            }
        };
    }

    default <T> Object2ByteFunction<T> composeObject(final Object2BooleanFunction<? super T> before) {
        return new Object2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.objects.Object2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = Boolean2ByteFunction.this.get(before.getBoolean(obj));
                return b;
            }
        };
    }

    default <T> Boolean2ReferenceFunction<T> andThenReference(final Byte2ReferenceFunction<? extends T> after) {
        return new Boolean2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction
            public final Object get(boolean z) {
                Object obj;
                obj = after.get(Boolean2ByteFunction.this.get(z));
                return obj;
            }
        };
    }

    default <T> Reference2ByteFunction<T> composeReference(final Reference2BooleanFunction<? super T> before) {
        return new Reference2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.objects.Reference2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = Boolean2ByteFunction.this.get(before.getBoolean(obj));
                return b;
            }
        };
    }
}
