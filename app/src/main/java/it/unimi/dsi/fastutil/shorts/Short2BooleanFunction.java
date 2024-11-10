package it.unimi.dsi.fastutil.shorts;

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
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.chars.Char2BooleanFunction;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.doubles.Double2BooleanFunction;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import it.unimi.dsi.fastutil.floats.Float2BooleanFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.longs.Long2BooleanFunction;
import it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Reference2ShortFunction;
import java.util.function.IntPredicate;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Short2BooleanFunction extends Function<Short, Boolean>, IntPredicate {
    boolean get(short s);

    @Override // java.util.function.IntPredicate
    @Deprecated
    default boolean test(int operand) {
        return get(SafeMath.safeIntToShort(operand));
    }

    default boolean put(short key, boolean value) {
        throw new UnsupportedOperationException();
    }

    default boolean getOrDefault(short key, boolean defaultValue) {
        boolean v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default boolean remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Boolean put(Short key, Boolean value) {
        short k = key.shortValue();
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
        short k = ((Short) key).shortValue();
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
        short k = ((Short) key).shortValue();
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
        short k = ((Short) key).shortValue();
        if (containsKey(k)) {
            return Boolean.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(short key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Short) key).shortValue());
    }

    default void defaultReturnValue(boolean rv) {
        throw new UnsupportedOperationException();
    }

    default boolean defaultReturnValue() {
        return false;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Boolean> compose(java.util.function.Function<? super T, ? extends Short> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Short, T> andThen(java.util.function.Function<? super Boolean, ? extends T> after) {
        return super.andThen(after);
    }

    default Short2ByteFunction andThenByte(final Boolean2ByteFunction after) {
        return new Short2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.shorts.Short2ByteFunction
            public final byte get(short s) {
                byte b;
                b = after.get(Short2BooleanFunction.this.get(s));
                return b;
            }
        };
    }

    default Byte2BooleanFunction composeByte(final Byte2ShortFunction before) {
        return new Byte2BooleanFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction
            public final boolean get(byte b) {
                boolean z;
                z = Short2BooleanFunction.this.get(before.get(b));
                return z;
            }
        };
    }

    default Short2ShortFunction andThenShort(final Boolean2ShortFunction after) {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                short s2;
                s2 = after.get(Short2BooleanFunction.this.get(s));
                return s2;
            }
        };
    }

    default Short2BooleanFunction composeShort(final Short2ShortFunction before) {
        return new Short2BooleanFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.shorts.Short2BooleanFunction
            public final boolean get(short s) {
                boolean z;
                z = Short2BooleanFunction.this.get(before.get(s));
                return z;
            }
        };
    }

    default Short2IntFunction andThenInt(final Boolean2IntFunction after) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = after.get(Short2BooleanFunction.this.get(s));
                return i;
            }
        };
    }

    default Int2BooleanFunction composeInt(final Int2ShortFunction before) {
        return new Int2BooleanFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.ints.Int2BooleanFunction
            public final boolean get(int i) {
                boolean z;
                z = Short2BooleanFunction.this.get(before.get(i));
                return z;
            }
        };
    }

    default Short2LongFunction andThenLong(final Boolean2LongFunction after) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = after.get(Short2BooleanFunction.this.get(s));
                return j;
            }
        };
    }

    default Long2BooleanFunction composeLong(final Long2ShortFunction before) {
        return new Long2BooleanFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.longs.Long2BooleanFunction
            public final boolean get(long j) {
                boolean z;
                z = Short2BooleanFunction.this.get(before.get(j));
                return z;
            }
        };
    }

    default Short2CharFunction andThenChar(final Boolean2CharFunction after) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = after.get(Short2BooleanFunction.this.get(s));
                return c;
            }
        };
    }

    default Char2BooleanFunction composeChar(final Char2ShortFunction before) {
        return new Char2BooleanFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.chars.Char2BooleanFunction
            public final boolean get(char c) {
                boolean z;
                z = Short2BooleanFunction.this.get(before.get(c));
                return z;
            }
        };
    }

    default Short2FloatFunction andThenFloat(final Boolean2FloatFunction after) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = after.get(Short2BooleanFunction.this.get(s));
                return f;
            }
        };
    }

    default Float2BooleanFunction composeFloat(final Float2ShortFunction before) {
        return new Float2BooleanFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.floats.Float2BooleanFunction
            public final boolean get(float f) {
                boolean z;
                z = Short2BooleanFunction.this.get(before.get(f));
                return z;
            }
        };
    }

    default Short2DoubleFunction andThenDouble(final Boolean2DoubleFunction after) {
        return new Short2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.shorts.Short2DoubleFunction
            public final double get(short s) {
                double d;
                d = after.get(Short2BooleanFunction.this.get(s));
                return d;
            }
        };
    }

    default Double2BooleanFunction composeDouble(final Double2ShortFunction before) {
        return new Double2BooleanFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.doubles.Double2BooleanFunction
            public final boolean get(double d) {
                boolean z;
                z = Short2BooleanFunction.this.get(before.get(d));
                return z;
            }
        };
    }

    default <T> Short2ObjectFunction<T> andThenObject(final Boolean2ObjectFunction<? extends T> after) {
        return new Short2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.shorts.Short2ObjectFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2BooleanFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Object2BooleanFunction<T> composeObject(final Object2ShortFunction<? super T> before) {
        return new Object2BooleanFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.objects.Object2BooleanFunction
            public final boolean getBoolean(Object obj) {
                boolean z;
                z = Short2BooleanFunction.this.get(before.getShort(obj));
                return z;
            }
        };
    }

    default <T> Short2ReferenceFunction<T> andThenReference(final Boolean2ReferenceFunction<? extends T> after) {
        return new Short2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2BooleanFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Reference2BooleanFunction<T> composeReference(final Reference2ShortFunction<? super T> before) {
        return new Reference2BooleanFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2BooleanFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.objects.Reference2BooleanFunction
            public final boolean getBoolean(Object obj) {
                boolean z;
                z = Short2BooleanFunction.this.get(before.getShort(obj));
                return z;
            }
        };
    }
}
