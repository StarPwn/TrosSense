package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import it.unimi.dsi.fastutil.objects.Object2ReferenceFunction;
import it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import it.unimi.dsi.fastutil.objects.Reference2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction;
import it.unimi.dsi.fastutil.objects.Reference2ShortFunction;
import java.util.function.IntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Short2ReferenceFunction<V> extends Function<Short, V>, IntFunction<V> {
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

    default Short2ByteFunction andThenByte(final Reference2ByteFunction<V> after) {
        return new Short2ByteFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.shorts.Short2ByteFunction
            public final byte get(short s) {
                byte b;
                b = after.getByte(Short2ReferenceFunction.this.get(s));
                return b;
            }
        };
    }

    default Byte2ReferenceFunction<V> composeByte(final Byte2ShortFunction before) {
        return new Byte2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction
            public final Object get(byte b) {
                Object obj;
                obj = Short2ReferenceFunction.this.get(before.get(b));
                return obj;
            }
        };
    }

    default Short2ShortFunction andThenShort(final Reference2ShortFunction<V> after) {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                short s2;
                s2 = after.getShort(Short2ReferenceFunction.this.get(s));
                return s2;
            }
        };
    }

    default Short2ReferenceFunction<V> composeShort(final Short2ShortFunction before) {
        return new Short2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction
            public final Object get(short s) {
                Object obj;
                obj = Short2ReferenceFunction.this.get(before.get(s));
                return obj;
            }
        };
    }

    default Short2IntFunction andThenInt(final Reference2IntFunction<V> after) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = after.getInt(Short2ReferenceFunction.this.get(s));
                return i;
            }
        };
    }

    default Int2ReferenceFunction<V> composeInt(final Int2ShortFunction before) {
        return new Int2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.ints.Int2ReferenceFunction
            public final Object get(int i) {
                Object obj;
                obj = Short2ReferenceFunction.this.get(before.get(i));
                return obj;
            }
        };
    }

    default Short2LongFunction andThenLong(final Reference2LongFunction<V> after) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = after.getLong(Short2ReferenceFunction.this.get(s));
                return j;
            }
        };
    }

    default Long2ReferenceFunction<V> composeLong(final Long2ShortFunction before) {
        return new Long2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.longs.Long2ReferenceFunction
            public final Object get(long j) {
                Object obj;
                obj = Short2ReferenceFunction.this.get(before.get(j));
                return obj;
            }
        };
    }

    default Short2CharFunction andThenChar(final Reference2CharFunction<V> after) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = after.getChar(Short2ReferenceFunction.this.get(s));
                return c;
            }
        };
    }

    default Char2ReferenceFunction<V> composeChar(final Char2ShortFunction before) {
        return new Char2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.chars.Char2ReferenceFunction
            public final Object get(char c) {
                Object obj;
                obj = Short2ReferenceFunction.this.get(before.get(c));
                return obj;
            }
        };
    }

    default Short2FloatFunction andThenFloat(final Reference2FloatFunction<V> after) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = after.getFloat(Short2ReferenceFunction.this.get(s));
                return f;
            }
        };
    }

    default Float2ReferenceFunction<V> composeFloat(final Float2ShortFunction before) {
        return new Float2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.floats.Float2ReferenceFunction
            public final Object get(float f) {
                Object obj;
                obj = Short2ReferenceFunction.this.get(before.get(f));
                return obj;
            }
        };
    }

    default Short2DoubleFunction andThenDouble(final Reference2DoubleFunction<V> after) {
        return new Short2DoubleFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.shorts.Short2DoubleFunction
            public final double get(short s) {
                double d;
                d = after.getDouble(Short2ReferenceFunction.this.get(s));
                return d;
            }
        };
    }

    default Double2ReferenceFunction<V> composeDouble(final Double2ShortFunction before) {
        return new Double2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction
            public final Object get(double d) {
                Object obj;
                obj = Short2ReferenceFunction.this.get(before.get(d));
                return obj;
            }
        };
    }

    default <T> Short2ObjectFunction<T> andThenObject(final Reference2ObjectFunction<? super V, ? extends T> after) {
        return new Short2ObjectFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.shorts.Short2ObjectFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2ReferenceFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Object2ReferenceFunction<T, V> composeObject(final Object2ShortFunction<? super T> before) {
        return new Object2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.objects.Object2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Short2ReferenceFunction.this.get(before.getShort(obj));
                return obj2;
            }
        };
    }

    default <T> Short2ReferenceFunction<T> andThenReference(final Reference2ReferenceFunction<? super V, ? extends T> after) {
        return new Short2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction
            public final Object get(short s) {
                Object obj;
                obj = after.get(Short2ReferenceFunction.this.get(s));
                return obj;
            }
        };
    }

    default <T> Reference2ReferenceFunction<T, V> composeReference(final Reference2ShortFunction<? super T> before) {
        return new Reference2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Short2ReferenceFunction.this.get(before.getShort(obj));
                return obj2;
            }
        };
    }
}
