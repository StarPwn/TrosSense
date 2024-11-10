package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.Char2CharFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.longs.Long2CharFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2CharFunction;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Int2CharFunction extends Function<Integer, Character>, java.util.function.IntUnaryOperator {
    char get(int i);

    @Override // java.util.function.IntUnaryOperator
    default int applyAsInt(int operand) {
        return get(operand);
    }

    default char put(int key, char value) {
        throw new UnsupportedOperationException();
    }

    default char getOrDefault(int key, char defaultValue) {
        char v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default char remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character put(Integer key, Character value) {
        int k = key.intValue();
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
        int k = ((Integer) key).intValue();
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
        int k = ((Integer) key).intValue();
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
        int k = ((Integer) key).intValue();
        if (containsKey(k)) {
            return Character.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(int key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Integer) key).intValue());
    }

    default void defaultReturnValue(char rv) {
        throw new UnsupportedOperationException();
    }

    default char defaultReturnValue() {
        return (char) 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Character> compose(java.util.function.Function<? super T, ? extends Integer> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Integer, T> andThen(java.util.function.Function<? super Character, ? extends T> after) {
        return super.andThen(after);
    }

    default Int2ByteFunction andThenByte(final Char2ByteFunction after) {
        return new Int2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.ints.Int2ByteFunction
            public final byte get(int i) {
                byte b;
                b = after.get(Int2CharFunction.this.get(i));
                return b;
            }
        };
    }

    default Byte2CharFunction composeByte(final Byte2IntFunction before) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = Int2CharFunction.this.get(before.get(b));
                return c;
            }
        };
    }

    default Int2ShortFunction andThenShort(final Char2ShortFunction after) {
        return new Int2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.ints.Int2ShortFunction
            public final short get(int i) {
                short s;
                s = after.get(Int2CharFunction.this.get(i));
                return s;
            }
        };
    }

    default Short2CharFunction composeShort(final Short2IntFunction before) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = Int2CharFunction.this.get(before.get(s));
                return c;
            }
        };
    }

    default Int2IntFunction andThenInt(final Char2IntFunction after) {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                int i2;
                i2 = after.get(Int2CharFunction.this.get(i));
                return i2;
            }
        };
    }

    default Int2CharFunction composeInt(final Int2IntFunction before) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = Int2CharFunction.this.get(before.get(i));
                return c;
            }
        };
    }

    default Int2LongFunction andThenLong(final Char2LongFunction after) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = after.get(Int2CharFunction.this.get(i));
                return j;
            }
        };
    }

    default Long2CharFunction composeLong(final Long2IntFunction before) {
        return new Long2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.longs.Long2CharFunction
            public final char get(long j) {
                char c;
                c = Int2CharFunction.this.get(before.get(j));
                return c;
            }
        };
    }

    default Int2CharFunction andThenChar(final Char2CharFunction after) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = after.get(Int2CharFunction.this.get(i));
                return c;
            }
        };
    }

    default Char2CharFunction composeChar(final Char2IntFunction before) {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                char c2;
                c2 = Int2CharFunction.this.get(before.get(c));
                return c2;
            }
        };
    }

    default Int2FloatFunction andThenFloat(final Char2FloatFunction after) {
        return new Int2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.ints.Int2FloatFunction
            public final float get(int i) {
                float f;
                f = after.get(Int2CharFunction.this.get(i));
                return f;
            }
        };
    }

    default Float2CharFunction composeFloat(final Float2IntFunction before) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = Int2CharFunction.this.get(before.get(f));
                return c;
            }
        };
    }

    default Int2DoubleFunction andThenDouble(final Char2DoubleFunction after) {
        return new Int2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.ints.Int2DoubleFunction
            public final double get(int i) {
                double d;
                d = after.get(Int2CharFunction.this.get(i));
                return d;
            }
        };
    }

    default Double2CharFunction composeDouble(final Double2IntFunction before) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = Int2CharFunction.this.get(before.get(d));
                return c;
            }
        };
    }

    default <T> Int2ObjectFunction<T> andThenObject(final Char2ObjectFunction<? extends T> after) {
        return new Int2ObjectFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
            public final Object get(int i) {
                Object obj;
                obj = after.get(Int2CharFunction.this.get(i));
                return obj;
            }
        };
    }

    default <T> Object2CharFunction<T> composeObject(final Object2IntFunction<? super T> before) {
        return new Object2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.objects.Object2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Int2CharFunction.this.get(before.getInt(obj));
                return c;
            }
        };
    }

    default <T> Int2ReferenceFunction<T> andThenReference(final Char2ReferenceFunction<? extends T> after) {
        return new Int2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.ints.Int2ReferenceFunction
            public final Object get(int i) {
                Object obj;
                obj = after.get(Int2CharFunction.this.get(i));
                return obj;
            }
        };
    }

    default <T> Reference2CharFunction<T> composeReference(final Reference2IntFunction<? super T> before) {
        return new Reference2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2CharFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.objects.Reference2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Int2CharFunction.this.get(before.getInt(obj));
                return c;
            }
        };
    }
}
