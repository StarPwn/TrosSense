package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
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
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;
import java.util.function.DoubleFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Double2ReferenceFunction<V> extends Function<Double, V>, DoubleFunction<V> {
    V get(double d);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Object put(Double d, Object obj) {
        return put2(d, (Double) obj);
    }

    @Override // java.util.function.DoubleFunction
    default V apply(double operand) {
        return get(operand);
    }

    default V put(double key, V value) {
        throw new UnsupportedOperationException();
    }

    default V getOrDefault(double key, V defaultValue) {
        V v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default V remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* renamed from: put, reason: avoid collision after fix types in other method */
    default V put2(Double key, V value) {
        double k = key.doubleValue();
        boolean containsKey = containsKey(k);
        V v = put(k, (double) value);
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
        double k = ((Double) key).doubleValue();
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
        double k = ((Double) key).doubleValue();
        V v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? v : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default V remove(Object key) {
        if (key == null) {
            return null;
        }
        double k = ((Double) key).doubleValue();
        if (containsKey(k)) {
            return remove(k);
        }
        return null;
    }

    default boolean containsKey(double key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Double) key).doubleValue());
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
    default <T> java.util.function.Function<T, V> compose(java.util.function.Function<? super T, ? extends Double> function) {
        return super.compose(function);
    }

    default Double2ByteFunction andThenByte(final Reference2ByteFunction<V> after) {
        return new Double2ByteFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.doubles.Double2ByteFunction
            public final byte get(double d) {
                byte b;
                b = after.getByte(Double2ReferenceFunction.this.get(d));
                return b;
            }
        };
    }

    default Byte2ReferenceFunction<V> composeByte(final Byte2DoubleFunction before) {
        return new Byte2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction
            public final Object get(byte b) {
                Object obj;
                obj = Double2ReferenceFunction.this.get(before.get(b));
                return obj;
            }
        };
    }

    default Double2ShortFunction andThenShort(final Reference2ShortFunction<V> after) {
        return new Double2ShortFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.doubles.Double2ShortFunction
            public final short get(double d) {
                short s;
                s = after.getShort(Double2ReferenceFunction.this.get(d));
                return s;
            }
        };
    }

    default Short2ReferenceFunction<V> composeShort(final Short2DoubleFunction before) {
        return new Short2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction
            public final Object get(short s) {
                Object obj;
                obj = Double2ReferenceFunction.this.get(before.get(s));
                return obj;
            }
        };
    }

    default Double2IntFunction andThenInt(final Reference2IntFunction<V> after) {
        return new Double2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.doubles.Double2IntFunction
            public final int get(double d) {
                int i;
                i = after.getInt(Double2ReferenceFunction.this.get(d));
                return i;
            }
        };
    }

    default Int2ReferenceFunction<V> composeInt(final Int2DoubleFunction before) {
        return new Int2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.ints.Int2ReferenceFunction
            public final Object get(int i) {
                Object obj;
                obj = Double2ReferenceFunction.this.get(before.get(i));
                return obj;
            }
        };
    }

    default Double2LongFunction andThenLong(final Reference2LongFunction<V> after) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = after.getLong(Double2ReferenceFunction.this.get(d));
                return j;
            }
        };
    }

    default Long2ReferenceFunction<V> composeLong(final Long2DoubleFunction before) {
        return new Long2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.longs.Long2ReferenceFunction
            public final Object get(long j) {
                Object obj;
                obj = Double2ReferenceFunction.this.get(before.get(j));
                return obj;
            }
        };
    }

    default Double2CharFunction andThenChar(final Reference2CharFunction<V> after) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = after.getChar(Double2ReferenceFunction.this.get(d));
                return c;
            }
        };
    }

    default Char2ReferenceFunction<V> composeChar(final Char2DoubleFunction before) {
        return new Char2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.chars.Char2ReferenceFunction
            public final Object get(char c) {
                Object obj;
                obj = Double2ReferenceFunction.this.get(before.get(c));
                return obj;
            }
        };
    }

    default Double2FloatFunction andThenFloat(final Reference2FloatFunction<V> after) {
        return new Double2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.doubles.Double2FloatFunction
            public final float get(double d) {
                float f;
                f = after.getFloat(Double2ReferenceFunction.this.get(d));
                return f;
            }
        };
    }

    default Float2ReferenceFunction<V> composeFloat(final Float2DoubleFunction before) {
        return new Float2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.floats.Float2ReferenceFunction
            public final Object get(float f) {
                Object obj;
                obj = Double2ReferenceFunction.this.get(before.get(f));
                return obj;
            }
        };
    }

    default Double2DoubleFunction andThenDouble(final Reference2DoubleFunction<V> after) {
        return new Double2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.doubles.Double2DoubleFunction
            public final double get(double d) {
                double d2;
                d2 = after.getDouble(Double2ReferenceFunction.this.get(d));
                return d2;
            }
        };
    }

    default Double2ReferenceFunction<V> composeDouble(final Double2DoubleFunction before) {
        return new Double2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction
            public final Object get(double d) {
                Object obj;
                obj = Double2ReferenceFunction.this.get(before.get(d));
                return obj;
            }
        };
    }

    default <T> Double2ObjectFunction<T> andThenObject(final Reference2ObjectFunction<? super V, ? extends T> after) {
        return new Double2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.doubles.Double2ObjectFunction
            public final Object get(double d) {
                Object obj;
                obj = after.get(Double2ReferenceFunction.this.get(d));
                return obj;
            }
        };
    }

    default <T> Object2ReferenceFunction<T, V> composeObject(final Object2DoubleFunction<? super T> before) {
        return new Object2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.objects.Object2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Double2ReferenceFunction.this.get(before.getDouble(obj));
                return obj2;
            }
        };
    }

    default <T> Double2ReferenceFunction<T> andThenReference(final Reference2ReferenceFunction<? super V, ? extends T> after) {
        return new Double2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction
            public final Object get(double d) {
                Object obj;
                obj = after.get(Double2ReferenceFunction.this.get(d));
                return obj;
            }
        };
    }

    default <T> Reference2ReferenceFunction<T, V> composeReference(final Reference2DoubleFunction<? super T> before) {
        return new Reference2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Double2ReferenceFunction.this.get(before.getDouble(obj));
                return obj2;
            }
        };
    }
}
