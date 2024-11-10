package it.unimi.dsi.fastutil.chars;

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
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.doubles.Double2BooleanFunction;
import it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2BooleanFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.longs.Long2BooleanFunction;
import it.unimi.dsi.fastutil.longs.Long2CharFunction;
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Object2CharFunction;
import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import it.unimi.dsi.fastutil.shorts.Short2BooleanFunction;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import java.util.function.IntPredicate;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Char2BooleanFunction extends Function<Character, Boolean>, IntPredicate {
    boolean get(char c);

    @Override // java.util.function.IntPredicate
    @Deprecated
    default boolean test(int operand) {
        return get(SafeMath.safeIntToChar(operand));
    }

    default boolean put(char key, boolean value) {
        throw new UnsupportedOperationException();
    }

    default boolean getOrDefault(char key, boolean defaultValue) {
        boolean v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default boolean remove(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Boolean put(Character key, Boolean value) {
        char k = key.charValue();
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
        char k = ((Character) key).charValue();
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
        char k = ((Character) key).charValue();
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
        char k = ((Character) key).charValue();
        if (containsKey(k)) {
            return Boolean.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(char key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Character) key).charValue());
    }

    default void defaultReturnValue(boolean rv) {
        throw new UnsupportedOperationException();
    }

    default boolean defaultReturnValue() {
        return false;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Boolean> compose(java.util.function.Function<? super T, ? extends Character> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Character, T> andThen(java.util.function.Function<? super Boolean, ? extends T> after) {
        return super.andThen(after);
    }

    default Char2ByteFunction andThenByte(final Boolean2ByteFunction after) {
        return new Char2ByteFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.chars.Char2ByteFunction
            public final byte get(char c) {
                byte b;
                b = after.get(Char2BooleanFunction.this.get(c));
                return b;
            }
        };
    }

    default Byte2BooleanFunction composeByte(final Byte2CharFunction before) {
        return new Byte2BooleanFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction
            public final boolean get(byte b) {
                boolean z;
                z = Char2BooleanFunction.this.get(before.get(b));
                return z;
            }
        };
    }

    default Char2ShortFunction andThenShort(final Boolean2ShortFunction after) {
        return new Char2ShortFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.chars.Char2ShortFunction
            public final short get(char c) {
                short s;
                s = after.get(Char2BooleanFunction.this.get(c));
                return s;
            }
        };
    }

    default Short2BooleanFunction composeShort(final Short2CharFunction before) {
        return new Short2BooleanFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.shorts.Short2BooleanFunction
            public final boolean get(short s) {
                boolean z;
                z = Char2BooleanFunction.this.get(before.get(s));
                return z;
            }
        };
    }

    default Char2IntFunction andThenInt(final Boolean2IntFunction after) {
        return new Char2IntFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.chars.Char2IntFunction
            public final int get(char c) {
                int i;
                i = after.get(Char2BooleanFunction.this.get(c));
                return i;
            }
        };
    }

    default Int2BooleanFunction composeInt(final Int2CharFunction before) {
        return new Int2BooleanFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.ints.Int2BooleanFunction
            public final boolean get(int i) {
                boolean z;
                z = Char2BooleanFunction.this.get(before.get(i));
                return z;
            }
        };
    }

    default Char2LongFunction andThenLong(final Boolean2LongFunction after) {
        return new Char2LongFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.chars.Char2LongFunction
            public final long get(char c) {
                long j;
                j = after.get(Char2BooleanFunction.this.get(c));
                return j;
            }
        };
    }

    default Long2BooleanFunction composeLong(final Long2CharFunction before) {
        return new Long2BooleanFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.longs.Long2BooleanFunction
            public final boolean get(long j) {
                boolean z;
                z = Char2BooleanFunction.this.get(before.get(j));
                return z;
            }
        };
    }

    default Char2CharFunction andThenChar(final Boolean2CharFunction after) {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                char c2;
                c2 = after.get(Char2BooleanFunction.this.get(c));
                return c2;
            }
        };
    }

    default Char2BooleanFunction composeChar(final Char2CharFunction before) {
        return new Char2BooleanFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.chars.Char2BooleanFunction
            public final boolean get(char c) {
                boolean z;
                z = Char2BooleanFunction.this.get(before.get(c));
                return z;
            }
        };
    }

    default Char2FloatFunction andThenFloat(final Boolean2FloatFunction after) {
        return new Char2FloatFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.chars.Char2FloatFunction
            public final float get(char c) {
                float f;
                f = after.get(Char2BooleanFunction.this.get(c));
                return f;
            }
        };
    }

    default Float2BooleanFunction composeFloat(final Float2CharFunction before) {
        return new Float2BooleanFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.floats.Float2BooleanFunction
            public final boolean get(float f) {
                boolean z;
                z = Char2BooleanFunction.this.get(before.get(f));
                return z;
            }
        };
    }

    default Char2DoubleFunction andThenDouble(final Boolean2DoubleFunction after) {
        return new Char2DoubleFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.chars.Char2DoubleFunction
            public final double get(char c) {
                double d;
                d = after.get(Char2BooleanFunction.this.get(c));
                return d;
            }
        };
    }

    default Double2BooleanFunction composeDouble(final Double2CharFunction before) {
        return new Double2BooleanFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.doubles.Double2BooleanFunction
            public final boolean get(double d) {
                boolean z;
                z = Char2BooleanFunction.this.get(before.get(d));
                return z;
            }
        };
    }

    default <T> Char2ObjectFunction<T> andThenObject(final Boolean2ObjectFunction<? extends T> after) {
        return new Char2ObjectFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.chars.Char2ObjectFunction
            public final Object get(char c) {
                Object obj;
                obj = after.get(Char2BooleanFunction.this.get(c));
                return obj;
            }
        };
    }

    default <T> Object2BooleanFunction<T> composeObject(final Object2CharFunction<? super T> before) {
        return new Object2BooleanFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.objects.Object2BooleanFunction
            public final boolean getBoolean(Object obj) {
                boolean z;
                z = Char2BooleanFunction.this.get(before.getChar(obj));
                return z;
            }
        };
    }

    default <T> Char2ReferenceFunction<T> andThenReference(final Boolean2ReferenceFunction<? extends T> after) {
        return new Char2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.chars.Char2ReferenceFunction
            public final Object get(char c) {
                Object obj;
                obj = after.get(Char2BooleanFunction.this.get(c));
                return obj;
            }
        };
    }

    default <T> Reference2BooleanFunction<T> composeReference(final Reference2CharFunction<? super T> before) {
        return new Reference2BooleanFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2BooleanFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Reference2BooleanFunction
            public final boolean getBoolean(Object obj) {
                boolean z;
                z = Char2BooleanFunction.this.get(before.getChar(obj));
                return z;
            }
        };
    }
}
