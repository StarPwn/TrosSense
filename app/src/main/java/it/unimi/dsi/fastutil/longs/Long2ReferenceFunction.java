package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
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
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;
import java.util.function.LongFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Long2ReferenceFunction<V> extends Function<Long, V>, LongFunction<V> {
    V get(long j);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Object put(Long l, Object obj) {
        return put2(l, (Long) obj);
    }

    @Override // java.util.function.LongFunction
    default V apply(long operand) {
        return get(operand);
    }

    default V put(long key, V value) {
        throw new UnsupportedOperationException();
    }

    default V getOrDefault(long key, V defaultValue) {
        V v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default V remove(long key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* renamed from: put, reason: avoid collision after fix types in other method */
    default V put2(Long key, V value) {
        long k = key.longValue();
        boolean containsKey = containsKey(k);
        V v = put(k, (long) value);
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
        long k = ((Long) key).longValue();
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
        long k = ((Long) key).longValue();
        V v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? v : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default V remove(Object key) {
        if (key == null) {
            return null;
        }
        long k = ((Long) key).longValue();
        if (containsKey(k)) {
            return remove(k);
        }
        return null;
    }

    default boolean containsKey(long key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Long) key).longValue());
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
    default <T> java.util.function.Function<T, V> compose(java.util.function.Function<? super T, ? extends Long> function) {
        return super.compose(function);
    }

    default Long2ByteFunction andThenByte(final Reference2ByteFunction<V> after) {
        return new Long2ByteFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.longs.Long2ByteFunction
            public final byte get(long j) {
                byte b;
                b = after.getByte(Long2ReferenceFunction.this.get(j));
                return b;
            }
        };
    }

    default Byte2ReferenceFunction<V> composeByte(final Byte2LongFunction before) {
        return new Byte2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction
            public final Object get(byte b) {
                Object obj;
                obj = Long2ReferenceFunction.this.get(before.get(b));
                return obj;
            }
        };
    }

    default Long2ShortFunction andThenShort(final Reference2ShortFunction<V> after) {
        return new Long2ShortFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.longs.Long2ShortFunction
            public final short get(long j) {
                short s;
                s = after.getShort(Long2ReferenceFunction.this.get(j));
                return s;
            }
        };
    }

    default Short2ReferenceFunction<V> composeShort(final Short2LongFunction before) {
        return new Short2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction
            public final Object get(short s) {
                Object obj;
                obj = Long2ReferenceFunction.this.get(before.get(s));
                return obj;
            }
        };
    }

    default Long2IntFunction andThenInt(final Reference2IntFunction<V> after) {
        return new Long2IntFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.longs.Long2IntFunction
            public final int get(long j) {
                int i;
                i = after.getInt(Long2ReferenceFunction.this.get(j));
                return i;
            }
        };
    }

    default Int2ReferenceFunction<V> composeInt(final Int2LongFunction before) {
        return new Int2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.ints.Int2ReferenceFunction
            public final Object get(int i) {
                Object obj;
                obj = Long2ReferenceFunction.this.get(before.get(i));
                return obj;
            }
        };
    }

    default Long2LongFunction andThenLong(final Reference2LongFunction<V> after) {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                long j2;
                j2 = after.getLong(Long2ReferenceFunction.this.get(j));
                return j2;
            }
        };
    }

    default Long2ReferenceFunction<V> composeLong(final Long2LongFunction before) {
        return new Long2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.longs.Long2ReferenceFunction
            public final Object get(long j) {
                Object obj;
                obj = Long2ReferenceFunction.this.get(before.get(j));
                return obj;
            }
        };
    }

    default Long2CharFunction andThenChar(final Reference2CharFunction<V> after) {
        return new Long2CharFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.longs.Long2CharFunction
            public final char get(long j) {
                char c;
                c = after.getChar(Long2ReferenceFunction.this.get(j));
                return c;
            }
        };
    }

    default Char2ReferenceFunction<V> composeChar(final Char2LongFunction before) {
        return new Char2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.chars.Char2ReferenceFunction
            public final Object get(char c) {
                Object obj;
                obj = Long2ReferenceFunction.this.get(before.get(c));
                return obj;
            }
        };
    }

    default Long2FloatFunction andThenFloat(final Reference2FloatFunction<V> after) {
        return new Long2FloatFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.longs.Long2FloatFunction
            public final float get(long j) {
                float f;
                f = after.getFloat(Long2ReferenceFunction.this.get(j));
                return f;
            }
        };
    }

    default Float2ReferenceFunction<V> composeFloat(final Float2LongFunction before) {
        return new Float2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.floats.Float2ReferenceFunction
            public final Object get(float f) {
                Object obj;
                obj = Long2ReferenceFunction.this.get(before.get(f));
                return obj;
            }
        };
    }

    default Long2DoubleFunction andThenDouble(final Reference2DoubleFunction<V> after) {
        return new Long2DoubleFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.longs.Long2DoubleFunction
            public final double get(long j) {
                double d;
                d = after.getDouble(Long2ReferenceFunction.this.get(j));
                return d;
            }
        };
    }

    default Double2ReferenceFunction<V> composeDouble(final Double2LongFunction before) {
        return new Double2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction
            public final Object get(double d) {
                Object obj;
                obj = Long2ReferenceFunction.this.get(before.get(d));
                return obj;
            }
        };
    }

    default <T> Long2ObjectFunction<T> andThenObject(final Reference2ObjectFunction<? super V, ? extends T> after) {
        return new Long2ObjectFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
            public final Object get(long j) {
                Object obj;
                obj = after.get(Long2ReferenceFunction.this.get(j));
                return obj;
            }
        };
    }

    default <T> Object2ReferenceFunction<T, V> composeObject(final Object2LongFunction<? super T> before) {
        return new Object2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.Object2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Long2ReferenceFunction.this.get(before.getLong(obj));
                return obj2;
            }
        };
    }

    default <T> Long2ReferenceFunction<T> andThenReference(final Reference2ReferenceFunction<? super V, ? extends T> after) {
        return new Long2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.longs.Long2ReferenceFunction
            public final Object get(long j) {
                Object obj;
                obj = after.get(Long2ReferenceFunction.this.get(j));
                return obj;
            }
        };
    }

    default <T> Reference2ReferenceFunction<T, V> composeReference(final Reference2LongFunction<? super T> before) {
        return new Reference2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2ReferenceFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Long2ReferenceFunction.this.get(before.getLong(obj));
                return obj2;
            }
        };
    }
}
