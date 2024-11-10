package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.longs.Long2CharFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2CharFunction;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Char2IntFunction extends Function<Character, Integer>, IntUnaryOperator {
    int get(char c);

    @Override // java.util.function.IntUnaryOperator
    @Deprecated
    default int applyAsInt(int operand) {
        return get(SafeMath.safeIntToChar(operand));
    }

    default int put(char key, int value) {
        throw new UnsupportedOperationException();
    }

    default int getOrDefault(char key, int defaultValue) {
        int v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default int remove(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer put(Character key, Integer value) {
        char k = key.charValue();
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
        char k = ((Character) key).charValue();
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
        char k = ((Character) key).charValue();
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
        char k = ((Character) key).charValue();
        if (containsKey(k)) {
            return Integer.valueOf(remove(k));
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

    default void defaultReturnValue(int rv) {
        throw new UnsupportedOperationException();
    }

    default int defaultReturnValue() {
        return 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Integer> compose(java.util.function.Function<? super T, ? extends Character> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Character, T> andThen(java.util.function.Function<? super Integer, ? extends T> after) {
        return super.andThen(after);
    }

    default Char2ByteFunction andThenByte(final Int2ByteFunction after) {
        return new Char2ByteFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.chars.Char2ByteFunction
            public final byte get(char c) {
                byte b;
                b = after.get(Char2IntFunction.this.get(c));
                return b;
            }
        };
    }

    default Byte2IntFunction composeByte(final Byte2CharFunction before) {
        return new Byte2IntFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.bytes.Byte2IntFunction
            public final int get(byte b) {
                int i;
                i = Char2IntFunction.this.get(before.get(b));
                return i;
            }
        };
    }

    default Char2ShortFunction andThenShort(final Int2ShortFunction after) {
        return new Char2ShortFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.chars.Char2ShortFunction
            public final short get(char c) {
                short s;
                s = after.get(Char2IntFunction.this.get(c));
                return s;
            }
        };
    }

    default Short2IntFunction composeShort(final Short2CharFunction before) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = Char2IntFunction.this.get(before.get(s));
                return i;
            }
        };
    }

    default Char2IntFunction andThenInt(final Int2IntFunction after) {
        return new Char2IntFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.chars.Char2IntFunction
            public final int get(char c) {
                int i;
                i = after.get(Char2IntFunction.this.get(c));
                return i;
            }
        };
    }

    default Int2IntFunction composeInt(final Int2CharFunction before) {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                int i2;
                i2 = Char2IntFunction.this.get(before.get(i));
                return i2;
            }
        };
    }

    default Char2LongFunction andThenLong(final Int2LongFunction after) {
        return new Char2LongFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.chars.Char2LongFunction
            public final long get(char c) {
                long j;
                j = after.get(Char2IntFunction.this.get(c));
                return j;
            }
        };
    }

    default Long2IntFunction composeLong(final Long2CharFunction before) {
        return new Long2IntFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.longs.Long2IntFunction
            public final int get(long j) {
                int i;
                i = Char2IntFunction.this.get(before.get(j));
                return i;
            }
        };
    }

    default Char2CharFunction andThenChar(final Int2CharFunction after) {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                char c2;
                c2 = after.get(Char2IntFunction.this.get(c));
                return c2;
            }
        };
    }

    default Char2IntFunction composeChar(final Char2CharFunction before) {
        return new Char2IntFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.chars.Char2IntFunction
            public final int get(char c) {
                int i;
                i = Char2IntFunction.this.get(before.get(c));
                return i;
            }
        };
    }

    default Char2FloatFunction andThenFloat(final Int2FloatFunction after) {
        return new Char2FloatFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.chars.Char2FloatFunction
            public final float get(char c) {
                float f;
                f = after.get(Char2IntFunction.this.get(c));
                return f;
            }
        };
    }

    default Float2IntFunction composeFloat(final Float2CharFunction before) {
        return new Float2IntFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.floats.Float2IntFunction
            public final int get(float f) {
                int i;
                i = Char2IntFunction.this.get(before.get(f));
                return i;
            }
        };
    }

    default Char2DoubleFunction andThenDouble(final Int2DoubleFunction after) {
        return new Char2DoubleFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.chars.Char2DoubleFunction
            public final double get(char c) {
                double d;
                d = after.get(Char2IntFunction.this.get(c));
                return d;
            }
        };
    }

    default Double2IntFunction composeDouble(final Double2CharFunction before) {
        return new Double2IntFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.doubles.Double2IntFunction
            public final int get(double d) {
                int i;
                i = Char2IntFunction.this.get(before.get(d));
                return i;
            }
        };
    }

    default <T> Char2ObjectFunction<T> andThenObject(final Int2ObjectFunction<? extends T> after) {
        return new Char2ObjectFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.chars.Char2ObjectFunction
            public final Object get(char c) {
                Object obj;
                obj = after.get(Char2IntFunction.this.get(c));
                return obj;
            }
        };
    }

    default <T> Object2IntFunction<T> composeObject(final Object2CharFunction<? super T> before) {
        return new Object2IntFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = Char2IntFunction.this.get(before.getChar(obj));
                return i;
            }
        };
    }

    default <T> Char2ReferenceFunction<T> andThenReference(final Int2ReferenceFunction<? extends T> after) {
        return new Char2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.chars.Char2ReferenceFunction
            public final Object get(char c) {
                Object obj;
                obj = after.get(Char2IntFunction.this.get(c));
                return obj;
            }
        };
    }

    default <T> Reference2IntFunction<T> composeReference(final Reference2CharFunction<? super T> before) {
        return new Reference2IntFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2IntFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Reference2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = Char2IntFunction.this.get(before.getChar(obj));
                return i;
            }
        };
    }
}
