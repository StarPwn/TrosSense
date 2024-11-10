package it.unimi.dsi.fastutil.bytes;

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
import java.util.function.IntPredicate;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Byte2BooleanFunction extends Function<Byte, Boolean>, IntPredicate {
    boolean get(byte b);

    @Override // java.util.function.IntPredicate
    @Deprecated
    default boolean test(int operand) {
        return get(SafeMath.safeIntToByte(operand));
    }

    default boolean put(byte key, boolean value) {
        throw new UnsupportedOperationException();
    }

    default boolean getOrDefault(byte key, boolean defaultValue) {
        boolean v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default boolean remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Boolean put(Byte key, Boolean value) {
        byte k = key.byteValue();
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
        byte k = ((Byte) key).byteValue();
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
        byte k = ((Byte) key).byteValue();
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
        byte k = ((Byte) key).byteValue();
        if (containsKey(k)) {
            return Boolean.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(byte key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Byte) key).byteValue());
    }

    default void defaultReturnValue(boolean rv) {
        throw new UnsupportedOperationException();
    }

    default boolean defaultReturnValue() {
        return false;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Boolean> compose(java.util.function.Function<? super T, ? extends Byte> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Byte, T> andThen(java.util.function.Function<? super Boolean, ? extends T> after) {
        return super.andThen(after);
    }

    default Byte2ByteFunction andThenByte(final Boolean2ByteFunction after) {
        return new Byte2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ByteFunction
            public final byte get(byte b) {
                byte b2;
                b2 = after.get(Byte2BooleanFunction.this.get(b));
                return b2;
            }
        };
    }

    default Byte2BooleanFunction composeByte(final Byte2ByteFunction before) {
        return new Byte2BooleanFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction
            public final boolean get(byte b) {
                boolean z;
                z = Byte2BooleanFunction.this.get(before.get(b));
                return z;
            }
        };
    }

    default Byte2ShortFunction andThenShort(final Boolean2ShortFunction after) {
        return new Byte2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ShortFunction
            public final short get(byte b) {
                short s;
                s = after.get(Byte2BooleanFunction.this.get(b));
                return s;
            }
        };
    }

    default Short2BooleanFunction composeShort(final Short2ByteFunction before) {
        return new Short2BooleanFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.shorts.Short2BooleanFunction
            public final boolean get(short s) {
                boolean z;
                z = Byte2BooleanFunction.this.get(before.get(s));
                return z;
            }
        };
    }

    default Byte2IntFunction andThenInt(final Boolean2IntFunction after) {
        return new Byte2IntFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.bytes.Byte2IntFunction
            public final int get(byte b) {
                int i;
                i = after.get(Byte2BooleanFunction.this.get(b));
                return i;
            }
        };
    }

    default Int2BooleanFunction composeInt(final Int2ByteFunction before) {
        return new Int2BooleanFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.ints.Int2BooleanFunction
            public final boolean get(int i) {
                boolean z;
                z = Byte2BooleanFunction.this.get(before.get(i));
                return z;
            }
        };
    }

    default Byte2LongFunction andThenLong(final Boolean2LongFunction after) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = after.get(Byte2BooleanFunction.this.get(b));
                return j;
            }
        };
    }

    default Long2BooleanFunction composeLong(final Long2ByteFunction before) {
        return new Long2BooleanFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.longs.Long2BooleanFunction
            public final boolean get(long j) {
                boolean z;
                z = Byte2BooleanFunction.this.get(before.get(j));
                return z;
            }
        };
    }

    default Byte2CharFunction andThenChar(final Boolean2CharFunction after) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = after.get(Byte2BooleanFunction.this.get(b));
                return c;
            }
        };
    }

    default Char2BooleanFunction composeChar(final Char2ByteFunction before) {
        return new Char2BooleanFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.chars.Char2BooleanFunction
            public final boolean get(char c) {
                boolean z;
                z = Byte2BooleanFunction.this.get(before.get(c));
                return z;
            }
        };
    }

    default Byte2FloatFunction andThenFloat(final Boolean2FloatFunction after) {
        return new Byte2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.bytes.Byte2FloatFunction
            public final float get(byte b) {
                float f;
                f = after.get(Byte2BooleanFunction.this.get(b));
                return f;
            }
        };
    }

    default Float2BooleanFunction composeFloat(final Float2ByteFunction before) {
        return new Float2BooleanFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.floats.Float2BooleanFunction
            public final boolean get(float f) {
                boolean z;
                z = Byte2BooleanFunction.this.get(before.get(f));
                return z;
            }
        };
    }

    default Byte2DoubleFunction andThenDouble(final Boolean2DoubleFunction after) {
        return new Byte2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction
            public final double get(byte b) {
                double d;
                d = after.get(Byte2BooleanFunction.this.get(b));
                return d;
            }
        };
    }

    default Double2BooleanFunction composeDouble(final Double2ByteFunction before) {
        return new Double2BooleanFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.doubles.Double2BooleanFunction
            public final boolean get(double d) {
                boolean z;
                z = Byte2BooleanFunction.this.get(before.get(d));
                return z;
            }
        };
    }

    default <T> Byte2ObjectFunction<T> andThenObject(final Boolean2ObjectFunction<? extends T> after) {
        return new Byte2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction
            public final Object get(byte b) {
                Object obj;
                obj = after.get(Byte2BooleanFunction.this.get(b));
                return obj;
            }
        };
    }

    default <T> Object2BooleanFunction<T> composeObject(final Object2ByteFunction<? super T> before) {
        return new Object2BooleanFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.objects.Object2BooleanFunction
            public final boolean getBoolean(Object obj) {
                boolean z;
                z = Byte2BooleanFunction.this.get(before.getByte(obj));
                return z;
            }
        };
    }

    default <T> Byte2ReferenceFunction<T> andThenReference(final Boolean2ReferenceFunction<? extends T> after) {
        return new Byte2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction
            public final Object get(byte b) {
                Object obj;
                obj = after.get(Byte2BooleanFunction.this.get(b));
                return obj;
            }
        };
    }

    default <T> Reference2BooleanFunction<T> composeReference(final Reference2ByteFunction<? super T> before) {
        return new Reference2BooleanFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Reference2BooleanFunction
            public final boolean getBoolean(Object obj) {
                boolean z;
                z = Byte2BooleanFunction.this.get(before.getByte(obj));
                return z;
            }
        };
    }
}
