package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import it.unimi.dsi.fastutil.longs.Long2CharFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.longs.Long2LongFunction;
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import it.unimi.dsi.fastutil.objects.Object2CharFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import java.util.function.IntToLongFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Char2LongFunction extends Function<Character, Long>, IntToLongFunction {
    long get(char c);

    @Override // java.util.function.IntToLongFunction
    @Deprecated
    default long applyAsLong(int operand) {
        return get(SafeMath.safeIntToChar(operand));
    }

    default long put(char key, long value) {
        throw new UnsupportedOperationException();
    }

    default long getOrDefault(char key, long defaultValue) {
        long v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default long remove(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long put(Character key, Long value) {
        char k = key.charValue();
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
        char k = ((Character) key).charValue();
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
        char k = ((Character) key).charValue();
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
        char k = ((Character) key).charValue();
        if (containsKey(k)) {
            return Long.valueOf(remove(k));
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

    default void defaultReturnValue(long rv) {
        throw new UnsupportedOperationException();
    }

    default long defaultReturnValue() {
        return 0L;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Long> compose(java.util.function.Function<? super T, ? extends Character> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Character, T> andThen(java.util.function.Function<? super Long, ? extends T> after) {
        return super.andThen(after);
    }

    default Char2ByteFunction andThenByte(final Long2ByteFunction after) {
        return new Char2ByteFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.chars.Char2ByteFunction
            public final byte get(char c) {
                byte b;
                b = after.get(Char2LongFunction.this.get(c));
                return b;
            }
        };
    }

    default Byte2LongFunction composeByte(final Byte2CharFunction before) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = Char2LongFunction.this.get(before.get(b));
                return j;
            }
        };
    }

    default Char2ShortFunction andThenShort(final Long2ShortFunction after) {
        return new Char2ShortFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.chars.Char2ShortFunction
            public final short get(char c) {
                short s;
                s = after.get(Char2LongFunction.this.get(c));
                return s;
            }
        };
    }

    default Short2LongFunction composeShort(final Short2CharFunction before) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = Char2LongFunction.this.get(before.get(s));
                return j;
            }
        };
    }

    default Char2IntFunction andThenInt(final Long2IntFunction after) {
        return new Char2IntFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.chars.Char2IntFunction
            public final int get(char c) {
                int i;
                i = after.get(Char2LongFunction.this.get(c));
                return i;
            }
        };
    }

    default Int2LongFunction composeInt(final Int2CharFunction before) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = Char2LongFunction.this.get(before.get(i));
                return j;
            }
        };
    }

    default Char2LongFunction andThenLong(final Long2LongFunction after) {
        return new Char2LongFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.chars.Char2LongFunction
            public final long get(char c) {
                long j;
                j = after.get(Char2LongFunction.this.get(c));
                return j;
            }
        };
    }

    default Long2LongFunction composeLong(final Long2CharFunction before) {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                long j2;
                j2 = Char2LongFunction.this.get(before.get(j));
                return j2;
            }
        };
    }

    default Char2CharFunction andThenChar(final Long2CharFunction after) {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                char c2;
                c2 = after.get(Char2LongFunction.this.get(c));
                return c2;
            }
        };
    }

    default Char2LongFunction composeChar(final Char2CharFunction before) {
        return new Char2LongFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.chars.Char2LongFunction
            public final long get(char c) {
                long j;
                j = Char2LongFunction.this.get(before.get(c));
                return j;
            }
        };
    }

    default Char2FloatFunction andThenFloat(final Long2FloatFunction after) {
        return new Char2FloatFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.chars.Char2FloatFunction
            public final float get(char c) {
                float f;
                f = after.get(Char2LongFunction.this.get(c));
                return f;
            }
        };
    }

    default Float2LongFunction composeFloat(final Float2CharFunction before) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = Char2LongFunction.this.get(before.get(f));
                return j;
            }
        };
    }

    default Char2DoubleFunction andThenDouble(final Long2DoubleFunction after) {
        return new Char2DoubleFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.chars.Char2DoubleFunction
            public final double get(char c) {
                double d;
                d = after.get(Char2LongFunction.this.get(c));
                return d;
            }
        };
    }

    default Double2LongFunction composeDouble(final Double2CharFunction before) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = Char2LongFunction.this.get(before.get(d));
                return j;
            }
        };
    }

    default <T> Char2ObjectFunction<T> andThenObject(final Long2ObjectFunction<? extends T> after) {
        return new Char2ObjectFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.chars.Char2ObjectFunction
            public final Object get(char c) {
                Object obj;
                obj = after.get(Char2LongFunction.this.get(c));
                return obj;
            }
        };
    }

    default <T> Object2LongFunction<T> composeObject(final Object2CharFunction<? super T> before) {
        return new Object2LongFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Object2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Char2LongFunction.this.get(before.getChar(obj));
                return j;
            }
        };
    }

    default <T> Char2ReferenceFunction<T> andThenReference(final Long2ReferenceFunction<? extends T> after) {
        return new Char2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.chars.Char2ReferenceFunction
            public final Object get(char c) {
                Object obj;
                obj = after.get(Char2LongFunction.this.get(c));
                return obj;
            }
        };
    }

    default <T> Reference2LongFunction<T> composeReference(final Reference2CharFunction<? super T> before) {
        return new Reference2LongFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2LongFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.objects.Reference2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Char2LongFunction.this.get(before.getChar(obj));
                return j;
            }
        };
    }
}
