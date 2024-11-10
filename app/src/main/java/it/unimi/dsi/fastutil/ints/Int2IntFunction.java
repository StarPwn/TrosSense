package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Int2IntFunction extends Function<Integer, Integer>, java.util.function.IntUnaryOperator {
    int get(int i);

    @Override // java.util.function.IntUnaryOperator
    default int applyAsInt(int operand) {
        return get(operand);
    }

    default int put(int key, int value) {
        throw new UnsupportedOperationException();
    }

    default int getOrDefault(int key, int defaultValue) {
        int v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default int remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer put(Integer key, Integer value) {
        int k = key.intValue();
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
        int k = ((Integer) key).intValue();
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
        int k = ((Integer) key).intValue();
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
        int k = ((Integer) key).intValue();
        if (containsKey(k)) {
            return Integer.valueOf(remove(k));
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

    default void defaultReturnValue(int rv) {
        throw new UnsupportedOperationException();
    }

    default int defaultReturnValue() {
        return 0;
    }

    static Int2IntFunction identity() {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda18
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                return Int2IntFunction.lambda$identity$0(i);
            }
        };
    }

    static /* synthetic */ int lambda$identity$0(int k) {
        return k;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Integer> compose(java.util.function.Function<? super T, ? extends Integer> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Integer, T> andThen(java.util.function.Function<? super Integer, ? extends T> after) {
        return super.andThen(after);
    }

    default Int2ByteFunction andThenByte(final Int2ByteFunction after) {
        return new Int2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.ints.Int2ByteFunction
            public final byte get(int i) {
                byte b;
                b = after.get(Int2IntFunction.this.get(i));
                return b;
            }
        };
    }

    default Byte2IntFunction composeByte(final Byte2IntFunction before) {
        return new Byte2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.bytes.Byte2IntFunction
            public final int get(byte b) {
                int i;
                i = Int2IntFunction.this.get(before.get(b));
                return i;
            }
        };
    }

    default Int2ShortFunction andThenShort(final Int2ShortFunction after) {
        return new Int2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.ints.Int2ShortFunction
            public final short get(int i) {
                short s;
                s = after.get(Int2IntFunction.this.get(i));
                return s;
            }
        };
    }

    default Short2IntFunction composeShort(final Short2IntFunction before) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = Int2IntFunction.this.get(before.get(s));
                return i;
            }
        };
    }

    default Int2IntFunction andThenInt(final Int2IntFunction after) {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                int i2;
                i2 = after.get(Int2IntFunction.this.get(i));
                return i2;
            }
        };
    }

    default Int2IntFunction composeInt(final Int2IntFunction before) {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                int i2;
                i2 = Int2IntFunction.this.get(before.get(i));
                return i2;
            }
        };
    }

    default Int2LongFunction andThenLong(final Int2LongFunction after) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = after.get(Int2IntFunction.this.get(i));
                return j;
            }
        };
    }

    default Long2IntFunction composeLong(final Long2IntFunction before) {
        return new Long2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.longs.Long2IntFunction
            public final int get(long j) {
                int i;
                i = Int2IntFunction.this.get(before.get(j));
                return i;
            }
        };
    }

    default Int2CharFunction andThenChar(final Int2CharFunction after) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = after.get(Int2IntFunction.this.get(i));
                return c;
            }
        };
    }

    default Char2IntFunction composeChar(final Char2IntFunction before) {
        return new Char2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.chars.Char2IntFunction
            public final int get(char c) {
                int i;
                i = Int2IntFunction.this.get(before.get(c));
                return i;
            }
        };
    }

    default Int2FloatFunction andThenFloat(final Int2FloatFunction after) {
        return new Int2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.ints.Int2FloatFunction
            public final float get(int i) {
                float f;
                f = after.get(Int2IntFunction.this.get(i));
                return f;
            }
        };
    }

    default Float2IntFunction composeFloat(final Float2IntFunction before) {
        return new Float2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.floats.Float2IntFunction
            public final int get(float f) {
                int i;
                i = Int2IntFunction.this.get(before.get(f));
                return i;
            }
        };
    }

    default Int2DoubleFunction andThenDouble(final Int2DoubleFunction after) {
        return new Int2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.ints.Int2DoubleFunction
            public final double get(int i) {
                double d;
                d = after.get(Int2IntFunction.this.get(i));
                return d;
            }
        };
    }

    default Double2IntFunction composeDouble(final Double2IntFunction before) {
        return new Double2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.doubles.Double2IntFunction
            public final int get(double d) {
                int i;
                i = Int2IntFunction.this.get(before.get(d));
                return i;
            }
        };
    }

    default <T> Int2ObjectFunction<T> andThenObject(final Int2ObjectFunction<? extends T> after) {
        return new Int2ObjectFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
            public final Object get(int i) {
                Object obj;
                obj = after.get(Int2IntFunction.this.get(i));
                return obj;
            }
        };
    }

    default <T> Object2IntFunction<T> composeObject(final Object2IntFunction<? super T> before) {
        return new Object2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = Int2IntFunction.this.get(before.getInt(obj));
                return i;
            }
        };
    }

    default <T> Int2ReferenceFunction<T> andThenReference(final Int2ReferenceFunction<? extends T> after) {
        return new Int2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.ints.Int2ReferenceFunction
            public final Object get(int i) {
                Object obj;
                obj = after.get(Int2IntFunction.this.get(i));
                return obj;
            }
        };
    }

    default <T> Reference2IntFunction<T> composeReference(final Reference2IntFunction<? super T> before) {
        return new Reference2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2IntFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.objects.Reference2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = Int2IntFunction.this.get(before.getInt(obj));
                return i;
            }
        };
    }
}
