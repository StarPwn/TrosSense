package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import it.unimi.dsi.fastutil.objects.Reference2ShortFunction;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Short2IntFunction extends Function<Short, Integer>, IntUnaryOperator {
    int get(short s);

    @Override // java.util.function.IntUnaryOperator
    @Deprecated
    default int applyAsInt(int operand) {
        return get(SafeMath.safeIntToShort(operand));
    }

    default int put(short key, int value) {
        throw new UnsupportedOperationException();
    }

    default int getOrDefault(short key, int defaultValue) {
        int v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default int remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer put(Short key, Integer value) {
        short k = key.shortValue();
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
        short k = ((Short) key).shortValue();
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
        short k = ((Short) key).shortValue();
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
        short k = ((Short) key).shortValue();
        if (containsKey(k)) {
            return Integer.valueOf(remove(k));
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

    default void defaultReturnValue(int rv) {
        throw new UnsupportedOperationException();
    }

    default int defaultReturnValue() {
        return 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Integer> compose(java.util.function.Function<? super T, ? extends Short> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Short, T> andThen(java.util.function.Function<? super Integer, ? extends T> after) {
        return super.andThen(after);
    }

    default Short2ByteFunction andThenByte(final Int2ByteFunction after) {
        return new Short2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.shorts.Short2ByteFunction
            public final byte get(short s) {
                byte b;
                b = after.get(Short2IntFunction.this.get(s));
                return b;
            }
        };
    }

    default Byte2IntFunction composeByte(final Byte2ShortFunction before) {
        return new Byte2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.bytes.Byte2IntFunction
            public final int get(byte b) {
                int i;
                i = Short2IntFunction.this.get(before.get(b));
                return i;
            }
        };
    }

    default Short2ShortFunction andThenShort(final Int2ShortFunction after) {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                short s2;
                s2 = after.get(Short2IntFunction.this.get(s));
                return s2;
            }
        };
    }

    default Short2IntFunction composeShort(final Short2ShortFunction before) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = Short2IntFunction.this.get(before.get(s));
                return i;
            }
        };
    }

    default Short2IntFunction andThenInt(final Int2IntFunction after) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = after.get(Short2IntFunction.this.get(s));
                return i;
            }
        };
    }

    default Int2IntFunction composeInt(final Int2ShortFunction before) {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                int i2;
                i2 = Short2IntFunction.this.get(before.get(i));
                return i2;
            }
        };
    }

    default Short2LongFunction andThenLong(final Int2LongFunction after) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = after.get(Short2IntFunction.this.get(s));
                return j;
            }
        };
    }

    default Long2IntFunction composeLong(final Long2ShortFunction before) {
        return new Long2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.longs.Long2IntFunction
            public final int get(long j) {
                int i;
                i = Short2IntFunction.this.get(before.get(j));
                return i;
            }
        };
    }

    default Short2CharFunction andThenChar(final Int2CharFunction after) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = after.get(Short2IntFunction.this.get(s));
                return c;
            }
        };
    }

    default Char2IntFunction composeChar(final Char2ShortFunction before) {
        return new Char2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.chars.Char2IntFunction
            public final int get(char c) {
                int i;
                i = Short2IntFunction.this.get(before.get(c));
                return i;
            }
        };
    }

    default Short2FloatFunction andThenFloat(final Int2FloatFunction after) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = after.get(Short2IntFunction.this.get(s));
                return f;
            }
        };
    }

    default Float2IntFunction composeFloat(final Float2ShortFunction before) {
        return new Float2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.floats.Float2IntFunction
            public final int get(float f) {
                int i;
                i = Short2IntFunction.this.get(before.get(f));
                return i;
            }
        };
    }

    default Short2DoubleFunction andThenDouble(final Int2DoubleFunction after) {
        return new Short2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.shorts.Short2DoubleFunction
            public final double get(short s) {
                double d;
                d = after.get(Short2IntFunction.this.get(s));
                return d;
            }
        };
    }

    default Double2IntFunction composeDouble(final Double2ShortFunction before) {
        return new Double2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.doubles.Double2IntFunction
            public final int get(double d) {
                int i;
                i = Short2IntFunction.this.get(before.get(d));
                return i;
            }
        };
    }

    default <T> Short2ObjectFunction<T> andThenObject(final Int2ObjectFunction<? extends T> after) {
        return new Short2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.shorts.Short2ObjectFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2IntFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Object2IntFunction<T> composeObject(final Object2ShortFunction<? super T> before) {
        return new Object2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = Short2IntFunction.this.get(before.getShort(obj));
                return i;
            }
        };
    }

    default <T> Short2ReferenceFunction<T> andThenReference(final Int2ReferenceFunction<? extends T> after) {
        return new Short2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2IntFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Reference2IntFunction<T> composeReference(final Reference2ShortFunction<? super T> before) {
        return new Reference2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2IntFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.Reference2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = Short2IntFunction.this.get(before.getShort(obj));
                return i;
            }
        };
    }
}
