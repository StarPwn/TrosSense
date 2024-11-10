package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
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
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;
import java.util.function.DoubleFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Float2ReferenceFunction<V> extends Function<Float, V>, DoubleFunction<V> {
    V get(float f);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Object put(Float f, Object obj) {
        return put2(f, (Float) obj);
    }

    @Override // java.util.function.DoubleFunction
    @Deprecated
    default V apply(double operand) {
        return get(SafeMath.safeDoubleToFloat(operand));
    }

    default V put(float key, V value) {
        throw new UnsupportedOperationException();
    }

    default V getOrDefault(float key, V defaultValue) {
        V v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default V remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* renamed from: put, reason: avoid collision after fix types in other method */
    default V put2(Float key, V value) {
        float k = key.floatValue();
        boolean containsKey = containsKey(k);
        V v = put(k, (float) value);
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
        float k = ((Float) key).floatValue();
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
        float k = ((Float) key).floatValue();
        V v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? v : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default V remove(Object key) {
        if (key == null) {
            return null;
        }
        float k = ((Float) key).floatValue();
        if (containsKey(k)) {
            return remove(k);
        }
        return null;
    }

    default boolean containsKey(float key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Float) key).floatValue());
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
    default <T> java.util.function.Function<T, V> compose(java.util.function.Function<? super T, ? extends Float> function) {
        return super.compose(function);
    }

    default Float2ByteFunction andThenByte(final Reference2ByteFunction<V> after) {
        return new Float2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.floats.Float2ByteFunction
            public final byte get(float f) {
                byte b;
                b = after.getByte(Float2ReferenceFunction.this.get(f));
                return b;
            }
        };
    }

    default Byte2ReferenceFunction<V> composeByte(final Byte2FloatFunction before) {
        return new Byte2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction
            public final Object get(byte b) {
                Object obj;
                obj = Float2ReferenceFunction.this.get(before.get(b));
                return obj;
            }
        };
    }

    default Float2ShortFunction andThenShort(final Reference2ShortFunction<V> after) {
        return new Float2ShortFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.floats.Float2ShortFunction
            public final short get(float f) {
                short s;
                s = after.getShort(Float2ReferenceFunction.this.get(f));
                return s;
            }
        };
    }

    default Short2ReferenceFunction<V> composeShort(final Short2FloatFunction before) {
        return new Short2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction
            public final Object get(short s) {
                Object obj;
                obj = Float2ReferenceFunction.this.get(before.get(s));
                return obj;
            }
        };
    }

    default Float2IntFunction andThenInt(final Reference2IntFunction<V> after) {
        return new Float2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.floats.Float2IntFunction
            public final int get(float f) {
                int i;
                i = after.getInt(Float2ReferenceFunction.this.get(f));
                return i;
            }
        };
    }

    default Int2ReferenceFunction<V> composeInt(final Int2FloatFunction before) {
        return new Int2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.ints.Int2ReferenceFunction
            public final Object get(int i) {
                Object obj;
                obj = Float2ReferenceFunction.this.get(before.get(i));
                return obj;
            }
        };
    }

    default Float2LongFunction andThenLong(final Reference2LongFunction<V> after) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = after.getLong(Float2ReferenceFunction.this.get(f));
                return j;
            }
        };
    }

    default Long2ReferenceFunction<V> composeLong(final Long2FloatFunction before) {
        return new Long2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.longs.Long2ReferenceFunction
            public final Object get(long j) {
                Object obj;
                obj = Float2ReferenceFunction.this.get(before.get(j));
                return obj;
            }
        };
    }

    default Float2CharFunction andThenChar(final Reference2CharFunction<V> after) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = after.getChar(Float2ReferenceFunction.this.get(f));
                return c;
            }
        };
    }

    default Char2ReferenceFunction<V> composeChar(final Char2FloatFunction before) {
        return new Char2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.chars.Char2ReferenceFunction
            public final Object get(char c) {
                Object obj;
                obj = Float2ReferenceFunction.this.get(before.get(c));
                return obj;
            }
        };
    }

    default Float2FloatFunction andThenFloat(final Reference2FloatFunction<V> after) {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                float f2;
                f2 = after.getFloat(Float2ReferenceFunction.this.get(f));
                return f2;
            }
        };
    }

    default Float2ReferenceFunction<V> composeFloat(final Float2FloatFunction before) {
        return new Float2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.floats.Float2ReferenceFunction
            public final Object get(float f) {
                Object obj;
                obj = Float2ReferenceFunction.this.get(before.get(f));
                return obj;
            }
        };
    }

    default Float2DoubleFunction andThenDouble(final Reference2DoubleFunction<V> after) {
        return new Float2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.floats.Float2DoubleFunction
            public final double get(float f) {
                double d;
                d = after.getDouble(Float2ReferenceFunction.this.get(f));
                return d;
            }
        };
    }

    default Double2ReferenceFunction<V> composeDouble(final Double2FloatFunction before) {
        return new Double2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction
            public final Object get(double d) {
                Object obj;
                obj = Float2ReferenceFunction.this.get(before.get(d));
                return obj;
            }
        };
    }

    default <T> Float2ObjectFunction<T> andThenObject(final Reference2ObjectFunction<? super V, ? extends T> after) {
        return new Float2ObjectFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.floats.Float2ObjectFunction
            public final Object get(float f) {
                Object obj;
                obj = after.get(Float2ReferenceFunction.this.get(f));
                return obj;
            }
        };
    }

    default <T> Object2ReferenceFunction<T, V> composeObject(final Object2FloatFunction<? super T> before) {
        return new Object2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.objects.Object2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Float2ReferenceFunction.this.get(before.getFloat(obj));
                return obj2;
            }
        };
    }

    default <T> Float2ReferenceFunction<T> andThenReference(final Reference2ReferenceFunction<? super V, ? extends T> after) {
        return new Float2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.floats.Float2ReferenceFunction
            public final Object get(float f) {
                Object obj;
                obj = after.get(Float2ReferenceFunction.this.get(f));
                return obj;
            }
        };
    }

    default <T> Reference2ReferenceFunction<T, V> composeReference(final Reference2FloatFunction<? super T> before) {
        return new Reference2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2ReferenceFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Float2ReferenceFunction.this.get(before.getFloat(obj));
                return obj2;
            }
        };
    }
}
