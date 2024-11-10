package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.chars.Char2BooleanFunction;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.Char2CharFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
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

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Boolean2CharFunction extends Function<Boolean, Character> {
    char get(boolean z);

    default char put(boolean key, char value) {
        throw new UnsupportedOperationException();
    }

    default char getOrDefault(boolean key, char defaultValue) {
        char v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default char remove(boolean key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character put(Boolean key, Character value) {
        boolean k = key.booleanValue();
        boolean containsKey = containsKey(k);
        char v = put(k, value.charValue());
        if (containsKey) {
            return Character.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character get(Object key) {
        if (key == null) {
            return null;
        }
        boolean k = ((Boolean) key).booleanValue();
        char v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Character.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        boolean k = ((Boolean) key).booleanValue();
        char v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Character.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character remove(Object key) {
        if (key == null) {
            return null;
        }
        boolean k = ((Boolean) key).booleanValue();
        if (containsKey(k)) {
            return Character.valueOf(remove(k));
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

    default void defaultReturnValue(char rv) {
        throw new UnsupportedOperationException();
    }

    default char defaultReturnValue() {
        return (char) 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Character> compose(java.util.function.Function<? super T, ? extends Boolean> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Boolean, T> andThen(java.util.function.Function<? super Character, ? extends T> after) {
        return super.andThen(after);
    }

    default Boolean2ByteFunction andThenByte(final Char2ByteFunction after) {
        return new Boolean2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction
            public final byte get(boolean z) {
                byte b;
                b = after.get(Boolean2CharFunction.this.get(z));
                return b;
            }
        };
    }

    default Byte2CharFunction composeByte(final Byte2BooleanFunction before) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = Boolean2CharFunction.this.get(before.get(b));
                return c;
            }
        };
    }

    default Boolean2ShortFunction andThenShort(final Char2ShortFunction after) {
        return new Boolean2ShortFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction
            public final short get(boolean z) {
                short s;
                s = after.get(Boolean2CharFunction.this.get(z));
                return s;
            }
        };
    }

    default Short2CharFunction composeShort(final Short2BooleanFunction before) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = Boolean2CharFunction.this.get(before.get(s));
                return c;
            }
        };
    }

    default Boolean2IntFunction andThenInt(final Char2IntFunction after) {
        return new Boolean2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2IntFunction
            public final int get(boolean z) {
                int i;
                i = after.get(Boolean2CharFunction.this.get(z));
                return i;
            }
        };
    }

    default Int2CharFunction composeInt(final Int2BooleanFunction before) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = Boolean2CharFunction.this.get(before.get(i));
                return c;
            }
        };
    }

    default Boolean2LongFunction andThenLong(final Char2LongFunction after) {
        return new Boolean2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2LongFunction
            public final long get(boolean z) {
                long j;
                j = after.get(Boolean2CharFunction.this.get(z));
                return j;
            }
        };
    }

    default Long2CharFunction composeLong(final Long2BooleanFunction before) {
        return new Long2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.longs.Long2CharFunction
            public final char get(long j) {
                char c;
                c = Boolean2CharFunction.this.get(before.get(j));
                return c;
            }
        };
    }

    default Boolean2CharFunction andThenChar(final Char2CharFunction after) {
        return new Boolean2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2CharFunction
            public final char get(boolean z) {
                char c;
                c = after.get(Boolean2CharFunction.this.get(z));
                return c;
            }
        };
    }

    default Char2CharFunction composeChar(final Char2BooleanFunction before) {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                char c2;
                c2 = Boolean2CharFunction.this.get(before.get(c));
                return c2;
            }
        };
    }

    default Boolean2FloatFunction andThenFloat(final Char2FloatFunction after) {
        return new Boolean2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction
            public final float get(boolean z) {
                float f;
                f = after.get(Boolean2CharFunction.this.get(z));
                return f;
            }
        };
    }

    default Float2CharFunction composeFloat(final Float2BooleanFunction before) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = Boolean2CharFunction.this.get(before.get(f));
                return c;
            }
        };
    }

    default Boolean2DoubleFunction andThenDouble(final Char2DoubleFunction after) {
        return new Boolean2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction
            public final double get(boolean z) {
                double d;
                d = after.get(Boolean2CharFunction.this.get(z));
                return d;
            }
        };
    }

    default Double2CharFunction composeDouble(final Double2BooleanFunction before) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = Boolean2CharFunction.this.get(before.get(d));
                return c;
            }
        };
    }

    default <T> Boolean2ObjectFunction<T> andThenObject(final Char2ObjectFunction<? extends T> after) {
        return new Boolean2ObjectFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction
            public final Object get(boolean z) {
                Object obj;
                obj = after.get(Boolean2CharFunction.this.get(z));
                return obj;
            }
        };
    }

    default <T> Object2CharFunction<T> composeObject(final Object2BooleanFunction<? super T> before) {
        return new Object2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.objects.Object2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Boolean2CharFunction.this.get(before.getBoolean(obj));
                return c;
            }
        };
    }

    default <T> Boolean2ReferenceFunction<T> andThenReference(final Char2ReferenceFunction<? extends T> after) {
        return new Boolean2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction
            public final Object get(boolean z) {
                Object obj;
                obj = after.get(Boolean2CharFunction.this.get(z));
                return obj;
            }
        };
    }

    default <T> Reference2CharFunction<T> composeReference(final Reference2BooleanFunction<? super T> before) {
        return new Reference2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2CharFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.objects.Reference2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Boolean2CharFunction.this.get(before.getBoolean(obj));
                return c;
            }
        };
    }
}
