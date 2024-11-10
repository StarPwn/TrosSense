package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import it.unimi.dsi.fastutil.objects.Reference2ShortFunction;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;
import it.unimi.dsi.fastutil.shorts.Short2ShortFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Int2ShortFunction extends Function<Integer, Short>, java.util.function.IntUnaryOperator {
    short get(int i);

    @Override // java.util.function.IntUnaryOperator
    default int applyAsInt(int operand) {
        return get(operand);
    }

    default short put(int key, short value) {
        throw new UnsupportedOperationException();
    }

    default short getOrDefault(int key, short defaultValue) {
        short v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default short remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short put(Integer key, Short value) {
        int k = key.intValue();
        boolean containsKey = containsKey(k);
        short v = put(k, value.shortValue());
        if (containsKey) {
            return Short.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short get(Object key) {
        if (key == null) {
            return null;
        }
        int k = ((Integer) key).intValue();
        short v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Short.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        int k = ((Integer) key).intValue();
        short v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Short.valueOf(v) : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short remove(Object key) {
        if (key == null) {
            return null;
        }
        int k = ((Integer) key).intValue();
        if (containsKey(k)) {
            return Short.valueOf(remove(k));
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

    default void defaultReturnValue(short rv) {
        throw new UnsupportedOperationException();
    }

    default short defaultReturnValue() {
        return (short) 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Short> compose(java.util.function.Function<? super T, ? extends Integer> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Integer, T> andThen(java.util.function.Function<? super Short, ? extends T> after) {
        return super.andThen(after);
    }

    default Int2ByteFunction andThenByte(final Short2ByteFunction after) {
        return new Int2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.ints.Int2ByteFunction
            public final byte get(int i) {
                byte b;
                b = after.get(Int2ShortFunction.this.get(i));
                return b;
            }
        };
    }

    default Byte2ShortFunction composeByte(final Byte2IntFunction before) {
        return new Byte2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ShortFunction
            public final short get(byte b) {
                short s;
                s = Int2ShortFunction.this.get(before.get(b));
                return s;
            }
        };
    }

    default Int2ShortFunction andThenShort(final Short2ShortFunction after) {
        return new Int2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.ints.Int2ShortFunction
            public final short get(int i) {
                short s;
                s = after.get(Int2ShortFunction.this.get(i));
                return s;
            }
        };
    }

    default Short2ShortFunction composeShort(final Short2IntFunction before) {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                short s2;
                s2 = Int2ShortFunction.this.get(before.get(s));
                return s2;
            }
        };
    }

    default Int2IntFunction andThenInt(final Short2IntFunction after) {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                int i2;
                i2 = after.get(Int2ShortFunction.this.get(i));
                return i2;
            }
        };
    }

    default Int2ShortFunction composeInt(final Int2IntFunction before) {
        return new Int2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.ints.Int2ShortFunction
            public final short get(int i) {
                short s;
                s = Int2ShortFunction.this.get(before.get(i));
                return s;
            }
        };
    }

    default Int2LongFunction andThenLong(final Short2LongFunction after) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = after.get(Int2ShortFunction.this.get(i));
                return j;
            }
        };
    }

    default Long2ShortFunction composeLong(final Long2IntFunction before) {
        return new Long2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.longs.Long2ShortFunction
            public final short get(long j) {
                short s;
                s = Int2ShortFunction.this.get(before.get(j));
                return s;
            }
        };
    }

    default Int2CharFunction andThenChar(final Short2CharFunction after) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = after.get(Int2ShortFunction.this.get(i));
                return c;
            }
        };
    }

    default Char2ShortFunction composeChar(final Char2IntFunction before) {
        return new Char2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.chars.Char2ShortFunction
            public final short get(char c) {
                short s;
                s = Int2ShortFunction.this.get(before.get(c));
                return s;
            }
        };
    }

    default Int2FloatFunction andThenFloat(final Short2FloatFunction after) {
        return new Int2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.ints.Int2FloatFunction
            public final float get(int i) {
                float f;
                f = after.get(Int2ShortFunction.this.get(i));
                return f;
            }
        };
    }

    default Float2ShortFunction composeFloat(final Float2IntFunction before) {
        return new Float2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.floats.Float2ShortFunction
            public final short get(float f) {
                short s;
                s = Int2ShortFunction.this.get(before.get(f));
                return s;
            }
        };
    }

    default Int2DoubleFunction andThenDouble(final Short2DoubleFunction after) {
        return new Int2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.ints.Int2DoubleFunction
            public final double get(int i) {
                double d;
                d = after.get(Int2ShortFunction.this.get(i));
                return d;
            }
        };
    }

    default Double2ShortFunction composeDouble(final Double2IntFunction before) {
        return new Double2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.doubles.Double2ShortFunction
            public final short get(double d) {
                short s;
                s = Int2ShortFunction.this.get(before.get(d));
                return s;
            }
        };
    }

    default <T> Int2ObjectFunction<T> andThenObject(final Short2ObjectFunction<? extends T> after) {
        return new Int2ObjectFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
            public final Object get(int i) {
                Object obj;
                obj = after.get(Int2ShortFunction.this.get(i));
                return obj;
            }
        };
    }

    default <T> Object2ShortFunction<T> composeObject(final Object2IntFunction<? super T> before) {
        return new Object2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.objects.Object2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = Int2ShortFunction.this.get(before.getInt(obj));
                return s;
            }
        };
    }

    default <T> Int2ReferenceFunction<T> andThenReference(final Short2ReferenceFunction<? extends T> after) {
        return new Int2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.ints.Int2ReferenceFunction
            public final Object get(int i) {
                Object obj;
                obj = after.get(Int2ShortFunction.this.get(i));
                return obj;
            }
        };
    }

    default <T> Reference2ShortFunction<T> composeReference(final Reference2IntFunction<? super T> before) {
        return new Reference2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ShortFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.objects.Reference2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = Int2ShortFunction.this.get(before.getInt(obj));
                return s;
            }
        };
    }
}
