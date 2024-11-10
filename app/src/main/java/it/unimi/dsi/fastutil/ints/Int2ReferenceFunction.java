package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2ReferenceFunction;
import it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import it.unimi.dsi.fastutil.objects.Reference2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction;
import it.unimi.dsi.fastutil.objects.Reference2ShortFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;
import java.util.function.IntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Int2ReferenceFunction<V> extends Function<Integer, V>, IntFunction<V> {
    V get(int i);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Object put(Integer num, Object obj) {
        return put2(num, (Integer) obj);
    }

    @Override // java.util.function.IntFunction
    default V apply(int operand) {
        return get(operand);
    }

    default V put(int key, V value) {
        throw new UnsupportedOperationException();
    }

    default V getOrDefault(int key, V defaultValue) {
        V v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default V remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* renamed from: put, reason: avoid collision after fix types in other method */
    default V put2(Integer key, V value) {
        int k = key.intValue();
        boolean containsKey = containsKey(k);
        V v = put(k, (int) value);
        if (containsKey) {
            return v;
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default V get(Object key) {
        if (key == null) {
            return null;
        }
        int k = ((Integer) key).intValue();
        V v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return v;
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        int k = ((Integer) key).intValue();
        V v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? v : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default V remove(Object key) {
        if (key == null) {
            return null;
        }
        int k = ((Integer) key).intValue();
        if (containsKey(k)) {
            return remove(k);
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

    default void defaultReturnValue(V rv) {
        throw new UnsupportedOperationException();
    }

    default V defaultReturnValue() {
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, V> compose(java.util.function.Function<? super T, ? extends Integer> function) {
        return super.compose(function);
    }

    default Int2ByteFunction andThenByte(final Reference2ByteFunction<V> after) {
        return new Int2ByteFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.ints.Int2ByteFunction
            public final byte get(int i) {
                byte b;
                b = after.getByte(Int2ReferenceFunction.this.get(i));
                return b;
            }
        };
    }

    default Byte2ReferenceFunction<V> composeByte(final Byte2IntFunction before) {
        return new Byte2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction
            public final Object get(byte b) {
                Object obj;
                obj = Int2ReferenceFunction.this.get(before.get(b));
                return obj;
            }
        };
    }

    default Int2ShortFunction andThenShort(final Reference2ShortFunction<V> after) {
        return new Int2ShortFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.ints.Int2ShortFunction
            public final short get(int i) {
                short s;
                s = after.getShort(Int2ReferenceFunction.this.get(i));
                return s;
            }
        };
    }

    default Short2ReferenceFunction<V> composeShort(final Short2IntFunction before) {
        return new Short2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction
            public final Object get(short s) {
                Object obj;
                obj = Int2ReferenceFunction.this.get(before.get(s));
                return obj;
            }
        };
    }

    default Int2IntFunction andThenInt(final Reference2IntFunction<V> after) {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                int i2;
                i2 = after.getInt(Int2ReferenceFunction.this.get(i));
                return i2;
            }
        };
    }

    default Int2ReferenceFunction<V> composeInt(final Int2IntFunction before) {
        return new Int2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.ints.Int2ReferenceFunction
            public final Object get(int i) {
                Object obj;
                obj = Int2ReferenceFunction.this.get(before.get(i));
                return obj;
            }
        };
    }

    default Int2LongFunction andThenLong(final Reference2LongFunction<V> after) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = after.getLong(Int2ReferenceFunction.this.get(i));
                return j;
            }
        };
    }

    default Long2ReferenceFunction<V> composeLong(final Long2IntFunction before) {
        return new Long2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.longs.Long2ReferenceFunction
            public final Object get(long j) {
                Object obj;
                obj = Int2ReferenceFunction.this.get(before.get(j));
                return obj;
            }
        };
    }

    default Int2CharFunction andThenChar(final Reference2CharFunction<V> after) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = after.getChar(Int2ReferenceFunction.this.get(i));
                return c;
            }
        };
    }

    default Char2ReferenceFunction<V> composeChar(final Char2IntFunction before) {
        return new Char2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.chars.Char2ReferenceFunction
            public final Object get(char c) {
                Object obj;
                obj = Int2ReferenceFunction.this.get(before.get(c));
                return obj;
            }
        };
    }

    default Int2FloatFunction andThenFloat(final Reference2FloatFunction<V> after) {
        return new Int2FloatFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.ints.Int2FloatFunction
            public final float get(int i) {
                float f;
                f = after.getFloat(Int2ReferenceFunction.this.get(i));
                return f;
            }
        };
    }

    default Float2ReferenceFunction<V> composeFloat(final Float2IntFunction before) {
        return new Float2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.floats.Float2ReferenceFunction
            public final Object get(float f) {
                Object obj;
                obj = Int2ReferenceFunction.this.get(before.get(f));
                return obj;
            }
        };
    }

    default Int2DoubleFunction andThenDouble(final Reference2DoubleFunction<V> after) {
        return new Int2DoubleFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.ints.Int2DoubleFunction
            public final double get(int i) {
                double d;
                d = after.getDouble(Int2ReferenceFunction.this.get(i));
                return d;
            }
        };
    }

    default Double2ReferenceFunction<V> composeDouble(final Double2IntFunction before) {
        return new Double2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction
            public final Object get(double d) {
                Object obj;
                obj = Int2ReferenceFunction.this.get(before.get(d));
                return obj;
            }
        };
    }

    default <T> Int2ObjectFunction<T> andThenObject(final Reference2ObjectFunction<? super V, ? extends T> after) {
        return new Int2ObjectFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
            public final Object get(int i) {
                Object obj;
                obj = after.get(Int2ReferenceFunction.this.get(i));
                return obj;
            }
        };
    }

    default <T> Object2ReferenceFunction<T, V> composeObject(final Object2IntFunction<? super T> before) {
        return new Object2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.objects.Object2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Int2ReferenceFunction.this.get(before.getInt(obj));
                return obj2;
            }
        };
    }

    default <T> Int2ReferenceFunction<T> andThenReference(final Reference2ReferenceFunction<? super V, ? extends T> after) {
        return new Int2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.ints.Int2ReferenceFunction
            public final Object get(int i) {
                Object obj;
                obj = after.get(Int2ReferenceFunction.this.get(i));
                return obj;
            }
        };
    }

    default <T> Reference2ReferenceFunction<T, V> composeReference(final Reference2IntFunction<? super T> before) {
        return new Reference2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ReferenceFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Int2ReferenceFunction.this.get(before.getInt(obj));
                return obj2;
            }
        };
    }
}
