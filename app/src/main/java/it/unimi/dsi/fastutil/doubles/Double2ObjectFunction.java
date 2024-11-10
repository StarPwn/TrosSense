package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import it.unimi.dsi.fastutil.objects.Object2CharFunction;
import it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.Object2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2ReferenceFunction;
import it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Reference2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import java.util.function.DoubleFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Double2ObjectFunction<V> extends Function<Double, V>, DoubleFunction<V> {
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

    default Double2ByteFunction andThenByte(final Object2ByteFunction<V> after) {
        return new Double2ByteFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.doubles.Double2ByteFunction
            public final byte get(double d) {
                byte b;
                b = after.getByte(Double2ObjectFunction.this.get(d));
                return b;
            }
        };
    }

    default Byte2ObjectFunction<V> composeByte(final Byte2DoubleFunction before) {
        return new Byte2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction
            public final Object get(byte b) {
                Object obj;
                obj = Double2ObjectFunction.this.get(before.get(b));
                return obj;
            }
        };
    }

    default Double2ShortFunction andThenShort(final Object2ShortFunction<V> after) {
        return new Double2ShortFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.doubles.Double2ShortFunction
            public final short get(double d) {
                short s;
                s = after.getShort(Double2ObjectFunction.this.get(d));
                return s;
            }
        };
    }

    default Short2ObjectFunction<V> composeShort(final Short2DoubleFunction before) {
        return new Short2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.shorts.Short2ObjectFunction
            public final Object get(short s) {
                Object obj;
                obj = Double2ObjectFunction.this.get(before.get(s));
                return obj;
            }
        };
    }

    default Double2IntFunction andThenInt(final Object2IntFunction<V> after) {
        return new Double2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.doubles.Double2IntFunction
            public final int get(double d) {
                int i;
                i = after.getInt(Double2ObjectFunction.this.get(d));
                return i;
            }
        };
    }

    default Int2ObjectFunction<V> composeInt(final Int2DoubleFunction before) {
        return new Int2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
            public final Object get(int i) {
                Object obj;
                obj = Double2ObjectFunction.this.get(before.get(i));
                return obj;
            }
        };
    }

    default Double2LongFunction andThenLong(final Object2LongFunction<V> after) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = after.getLong(Double2ObjectFunction.this.get(d));
                return j;
            }
        };
    }

    default Long2ObjectFunction<V> composeLong(final Long2DoubleFunction before) {
        return new Long2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
            public final Object get(long j) {
                Object obj;
                obj = Double2ObjectFunction.this.get(before.get(j));
                return obj;
            }
        };
    }

    default Double2CharFunction andThenChar(final Object2CharFunction<V> after) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = after.getChar(Double2ObjectFunction.this.get(d));
                return c;
            }
        };
    }

    default Char2ObjectFunction<V> composeChar(final Char2DoubleFunction before) {
        return new Char2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.chars.Char2ObjectFunction
            public final Object get(char c) {
                Object obj;
                obj = Double2ObjectFunction.this.get(before.get(c));
                return obj;
            }
        };
    }

    default Double2FloatFunction andThenFloat(final Object2FloatFunction<V> after) {
        return new Double2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.doubles.Double2FloatFunction
            public final float get(double d) {
                float f;
                f = after.getFloat(Double2ObjectFunction.this.get(d));
                return f;
            }
        };
    }

    default Float2ObjectFunction<V> composeFloat(final Float2DoubleFunction before) {
        return new Float2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.floats.Float2ObjectFunction
            public final Object get(float f) {
                Object obj;
                obj = Double2ObjectFunction.this.get(before.get(f));
                return obj;
            }
        };
    }

    default Double2DoubleFunction andThenDouble(final Object2DoubleFunction<V> after) {
        return new Double2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.doubles.Double2DoubleFunction
            public final double get(double d) {
                double d2;
                d2 = after.getDouble(Double2ObjectFunction.this.get(d));
                return d2;
            }
        };
    }

    default Double2ObjectFunction<V> composeDouble(final Double2DoubleFunction before) {
        return new Double2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.doubles.Double2ObjectFunction
            public final Object get(double d) {
                Object obj;
                obj = Double2ObjectFunction.this.get(before.get(d));
                return obj;
            }
        };
    }

    default <T> Double2ObjectFunction<T> andThenObject(final Object2ObjectFunction<? super V, ? extends T> after) {
        return new Double2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.doubles.Double2ObjectFunction
            public final Object get(double d) {
                Object obj;
                obj = after.get(Double2ObjectFunction.this.get(d));
                return obj;
            }
        };
    }

    default <T> Object2ObjectFunction<T, V> composeObject(final Object2DoubleFunction<? super T> before) {
        return new Object2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.objects.Object2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Double2ObjectFunction.this.get(before.getDouble(obj));
                return obj2;
            }
        };
    }

    default <T> Double2ReferenceFunction<T> andThenReference(final Object2ReferenceFunction<? super V, ? extends T> after) {
        return new Double2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction
            public final Object get(double d) {
                Object obj;
                obj = after.get(Double2ObjectFunction.this.get(d));
                return obj;
            }
        };
    }

    default <T> Reference2ObjectFunction<T, V> composeReference(final Reference2DoubleFunction<? super T> before) {
        return new Reference2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2ObjectFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.objects.Reference2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Double2ObjectFunction.this.get(before.getDouble(obj));
                return obj2;
            }
        };
    }
}
