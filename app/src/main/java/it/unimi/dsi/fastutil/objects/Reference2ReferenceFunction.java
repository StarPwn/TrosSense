package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Reference2ReferenceFunction<K, V> extends Function<K, V> {
    @Override // it.unimi.dsi.fastutil.Function
    V get(Object obj);

    @Override // it.unimi.dsi.fastutil.Function
    default V put(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    default V getOrDefault(Object key, V defaultValue) {
        V v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.Function
    default V remove(Object key) {
        throw new UnsupportedOperationException();
    }

    default void defaultReturnValue(V rv) {
        throw new UnsupportedOperationException();
    }

    default V defaultReturnValue() {
        return null;
    }

    default Reference2ByteFunction<K> andThenByte(final Reference2ByteFunction<V> after) {
        return new Reference2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.objects.Reference2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = after.getByte(Reference2ReferenceFunction.this.get(obj));
                return b;
            }
        };
    }

    default Byte2ReferenceFunction<V> composeByte(final Byte2ReferenceFunction<K> before) {
        return new Byte2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction
            public final Object get(byte b) {
                Object obj;
                obj = Reference2ReferenceFunction.this.get(before.get(b));
                return obj;
            }
        };
    }

    default Reference2ShortFunction<K> andThenShort(final Reference2ShortFunction<V> after) {
        return new Reference2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.objects.Reference2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = after.getShort(Reference2ReferenceFunction.this.get(obj));
                return s;
            }
        };
    }

    default Short2ReferenceFunction<V> composeShort(final Short2ReferenceFunction<K> before) {
        return new Short2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction
            public final Object get(short s) {
                Object obj;
                obj = Reference2ReferenceFunction.this.get(before.get(s));
                return obj;
            }
        };
    }

    default Reference2IntFunction<K> andThenInt(final Reference2IntFunction<V> after) {
        return new Reference2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.objects.Reference2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = after.getInt(Reference2ReferenceFunction.this.get(obj));
                return i;
            }
        };
    }

    default Int2ReferenceFunction<V> composeInt(final Int2ReferenceFunction<K> before) {
        return new Int2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.ints.Int2ReferenceFunction
            public final Object get(int i) {
                Object obj;
                obj = Reference2ReferenceFunction.this.get(before.get(i));
                return obj;
            }
        };
    }

    default Reference2LongFunction<K> andThenLong(final Reference2LongFunction<V> after) {
        return new Reference2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.objects.Reference2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = after.getLong(Reference2ReferenceFunction.this.get(obj));
                return j;
            }
        };
    }

    default Long2ReferenceFunction<V> composeLong(final Long2ReferenceFunction<K> before) {
        return new Long2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.longs.Long2ReferenceFunction
            public final Object get(long j) {
                Object obj;
                obj = Reference2ReferenceFunction.this.get(before.get(j));
                return obj;
            }
        };
    }

    default Reference2CharFunction<K> andThenChar(final Reference2CharFunction<V> after) {
        return new Reference2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.objects.Reference2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = after.getChar(Reference2ReferenceFunction.this.get(obj));
                return c;
            }
        };
    }

    default Char2ReferenceFunction<V> composeChar(final Char2ReferenceFunction<K> before) {
        return new Char2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.chars.Char2ReferenceFunction
            public final Object get(char c) {
                Object obj;
                obj = Reference2ReferenceFunction.this.get(before.get(c));
                return obj;
            }
        };
    }

    default Reference2FloatFunction<K> andThenFloat(final Reference2FloatFunction<V> after) {
        return new Reference2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.objects.Reference2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = after.getFloat(Reference2ReferenceFunction.this.get(obj));
                return f;
            }
        };
    }

    default Float2ReferenceFunction<V> composeFloat(final Float2ReferenceFunction<K> before) {
        return new Float2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.floats.Float2ReferenceFunction
            public final Object get(float f) {
                Object obj;
                obj = Reference2ReferenceFunction.this.get(before.get(f));
                return obj;
            }
        };
    }

    default Reference2DoubleFunction<K> andThenDouble(final Reference2DoubleFunction<V> after) {
        return new Reference2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.objects.Reference2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = after.getDouble(Reference2ReferenceFunction.this.get(obj));
                return d;
            }
        };
    }

    default Double2ReferenceFunction<V> composeDouble(final Double2ReferenceFunction<K> before) {
        return new Double2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction
            public final Object get(double d) {
                Object obj;
                obj = Reference2ReferenceFunction.this.get(before.get(d));
                return obj;
            }
        };
    }

    default <T> Reference2ObjectFunction<K, T> andThenObject(final Reference2ObjectFunction<? super V, ? extends T> after) {
        return new Reference2ObjectFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.objects.Reference2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Reference2ReferenceFunction.this.get(obj));
                return obj2;
            }
        };
    }

    default <T> Object2ReferenceFunction<T, V> composeObject(final Object2ReferenceFunction<? super T, ? extends K> before) {
        return new Object2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Object2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Reference2ReferenceFunction.this.get(before.get(obj));
                return obj2;
            }
        };
    }

    default <T> Reference2ReferenceFunction<K, T> andThenReference(final Reference2ReferenceFunction<? super V, ? extends T> after) {
        return new Reference2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Reference2ReferenceFunction.this.get(obj));
                return obj2;
            }
        };
    }

    default <T> Reference2ReferenceFunction<T, V> composeReference(final Reference2ReferenceFunction<? super T, ? extends K> before) {
        return new Reference2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Reference2ReferenceFunction.this.get(before.get(obj));
                return obj2;
            }
        };
    }
}
