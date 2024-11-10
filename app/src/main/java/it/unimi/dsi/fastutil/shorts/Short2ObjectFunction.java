package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.doubles.Double2ObjectFunction;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import it.unimi.dsi.fastutil.objects.Object2CharFunction;
import it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.Object2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2ReferenceFunction;
import it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import it.unimi.dsi.fastutil.objects.Reference2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Reference2ShortFunction;
import java.util.function.IntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Short2ObjectFunction<V> extends Function<Short, V>, IntFunction<V> {
    V get(short s);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Object put(Short sh, Object obj) {
        return put2(sh, (Short) obj);
    }

    @Override // java.util.function.IntFunction
    @Deprecated
    default V apply(int operand) {
        return get(SafeMath.safeIntToShort(operand));
    }

    default V put(short key, V value) {
        throw new UnsupportedOperationException();
    }

    default V getOrDefault(short key, V defaultValue) {
        V v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default V remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* renamed from: put, reason: avoid collision after fix types in other method */
    default V put2(Short key, V value) {
        short k = key.shortValue();
        boolean containsKey = containsKey(k);
        V v = put(k, (short) value);
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
        short k = ((Short) key).shortValue();
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
        short k = ((Short) key).shortValue();
        V v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? v : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default V remove(Object key) {
        if (key == null) {
            return null;
        }
        short k = ((Short) key).shortValue();
        if (containsKey(k)) {
            return remove(k);
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

    default void defaultReturnValue(V rv) {
        throw new UnsupportedOperationException();
    }

    default V defaultReturnValue() {
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, V> compose(java.util.function.Function<? super T, ? extends Short> function) {
        return super.compose(function);
    }

    default Short2ByteFunction andThenByte(final Object2ByteFunction<V> after) {
        return new Short2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.shorts.Short2ByteFunction
            public final byte get(short s) {
                byte b;
                b = after.getByte(Short2ObjectFunction.this.get(s));
                return b;
            }
        };
    }

    default Byte2ObjectFunction<V> composeByte(final Byte2ShortFunction before) {
        return new Byte2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction
            public final Object get(byte b) {
                Object obj;
                obj = Short2ObjectFunction.this.get(before.get(b));
                return obj;
            }
        };
    }

    default Short2ShortFunction andThenShort(final Object2ShortFunction<V> after) {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                short s2;
                s2 = after.getShort(Short2ObjectFunction.this.get(s));
                return s2;
            }
        };
    }

    default Short2ObjectFunction<V> composeShort(final Short2ShortFunction before) {
        return new Short2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.shorts.Short2ObjectFunction
            public final Object get(short s) {
                Object obj;
                obj = Short2ObjectFunction.this.get(before.get(s));
                return obj;
            }
        };
    }

    default Short2IntFunction andThenInt(final Object2IntFunction<V> after) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = after.getInt(Short2ObjectFunction.this.get(s));
                return i;
            }
        };
    }

    default Int2ObjectFunction<V> composeInt(final Int2ShortFunction before) {
        return new Int2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
            public final Object get(int i) {
                Object obj;
                obj = Short2ObjectFunction.this.get(before.get(i));
                return obj;
            }
        };
    }

    default Short2LongFunction andThenLong(final Object2LongFunction<V> after) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = after.getLong(Short2ObjectFunction.this.get(s));
                return j;
            }
        };
    }

    default Long2ObjectFunction<V> composeLong(final Long2ShortFunction before) {
        return new Long2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
            public final Object get(long j) {
                Object obj;
                obj = Short2ObjectFunction.this.get(before.get(j));
                return obj;
            }
        };
    }

    default Short2CharFunction andThenChar(final Object2CharFunction<V> after) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = after.getChar(Short2ObjectFunction.this.get(s));
                return c;
            }
        };
    }

    default Char2ObjectFunction<V> composeChar(final Char2ShortFunction before) {
        return new Char2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.chars.Char2ObjectFunction
            public final Object get(char c) {
                Object obj;
                obj = Short2ObjectFunction.this.get(before.get(c));
                return obj;
            }
        };
    }

    default Short2FloatFunction andThenFloat(final Object2FloatFunction<V> after) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = after.getFloat(Short2ObjectFunction.this.get(s));
                return f;
            }
        };
    }

    default Float2ObjectFunction<V> composeFloat(final Float2ShortFunction before) {
        return new Float2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.floats.Float2ObjectFunction
            public final Object get(float f) {
                Object obj;
                obj = Short2ObjectFunction.this.get(before.get(f));
                return obj;
            }
        };
    }

    default Short2DoubleFunction andThenDouble(final Object2DoubleFunction<V> after) {
        return new Short2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.shorts.Short2DoubleFunction
            public final double get(short s) {
                double d;
                d = after.getDouble(Short2ObjectFunction.this.get(s));
                return d;
            }
        };
    }

    default Double2ObjectFunction<V> composeDouble(final Double2ShortFunction before) {
        return new Double2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.doubles.Double2ObjectFunction
            public final Object get(double d) {
                Object obj;
                obj = Short2ObjectFunction.this.get(before.get(d));
                return obj;
            }
        };
    }

    default <T> Short2ObjectFunction<T> andThenObject(final Object2ObjectFunction<? super V, ? extends T> after) {
        return new Short2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.shorts.Short2ObjectFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2ObjectFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Object2ObjectFunction<T, V> composeObject(final Object2ShortFunction<? super T> before) {
        return new Object2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.objects.Object2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Short2ObjectFunction.this.get(before.getShort(obj));
                return obj2;
            }
        };
    }

    default <T> Short2ReferenceFunction<T> andThenReference(final Object2ReferenceFunction<? super V, ? extends T> after) {
        return new Short2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2ObjectFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Reference2ObjectFunction<T, V> composeReference(final Reference2ShortFunction<? super T> before) {
        return new Reference2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ObjectFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.objects.Reference2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Short2ObjectFunction.this.get(before.getShort(obj));
                return obj2;
            }
        };
    }
}
