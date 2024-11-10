package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import it.unimi.dsi.fastutil.doubles.Double2ObjectFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ByteFunction;
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
import it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import it.unimi.dsi.fastutil.objects.Reference2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import java.util.function.IntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Byte2ObjectFunction<V> extends Function<Byte, V>, IntFunction<V> {
    V get(byte b);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Object put(Byte b, Object obj) {
        return put2(b, (Byte) obj);
    }

    @Override // java.util.function.IntFunction
    @Deprecated
    default V apply(int operand) {
        return get(SafeMath.safeIntToByte(operand));
    }

    default V put(byte key, V value) {
        throw new UnsupportedOperationException();
    }

    default V getOrDefault(byte key, V defaultValue) {
        V v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default V remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* renamed from: put, reason: avoid collision after fix types in other method */
    default V put2(Byte key, V value) {
        byte k = key.byteValue();
        boolean containsKey = containsKey(k);
        V v = put(k, (byte) value);
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
        byte k = ((Byte) key).byteValue();
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
        byte k = ((Byte) key).byteValue();
        V v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? v : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default V remove(Object key) {
        if (key == null) {
            return null;
        }
        byte k = ((Byte) key).byteValue();
        if (containsKey(k)) {
            return remove(k);
        }
        return null;
    }

    default boolean containsKey(byte key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Byte) key).byteValue());
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
    default <T> java.util.function.Function<T, V> compose(java.util.function.Function<? super T, ? extends Byte> function) {
        return super.compose(function);
    }

    default Byte2ByteFunction andThenByte(final Object2ByteFunction<V> after) {
        return new Byte2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ByteFunction
            public final byte get(byte b) {
                byte b2;
                b2 = after.getByte(Byte2ObjectFunction.this.get(b));
                return b2;
            }
        };
    }

    default Byte2ObjectFunction<V> composeByte(final Byte2ByteFunction before) {
        return new Byte2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction
            public final Object get(byte b) {
                Object obj;
                obj = Byte2ObjectFunction.this.get(before.get(b));
                return obj;
            }
        };
    }

    default Byte2ShortFunction andThenShort(final Object2ShortFunction<V> after) {
        return new Byte2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ShortFunction
            public final short get(byte b) {
                short s;
                s = after.getShort(Byte2ObjectFunction.this.get(b));
                return s;
            }
        };
    }

    default Short2ObjectFunction<V> composeShort(final Short2ByteFunction before) {
        return new Short2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.shorts.Short2ObjectFunction
            public final Object get(short s) {
                Object obj;
                obj = Byte2ObjectFunction.this.get(before.get(s));
                return obj;
            }
        };
    }

    default Byte2IntFunction andThenInt(final Object2IntFunction<V> after) {
        return new Byte2IntFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.bytes.Byte2IntFunction
            public final int get(byte b) {
                int i;
                i = after.getInt(Byte2ObjectFunction.this.get(b));
                return i;
            }
        };
    }

    default Int2ObjectFunction<V> composeInt(final Int2ByteFunction before) {
        return new Int2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
            public final Object get(int i) {
                Object obj;
                obj = Byte2ObjectFunction.this.get(before.get(i));
                return obj;
            }
        };
    }

    default Byte2LongFunction andThenLong(final Object2LongFunction<V> after) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = after.getLong(Byte2ObjectFunction.this.get(b));
                return j;
            }
        };
    }

    default Long2ObjectFunction<V> composeLong(final Long2ByteFunction before) {
        return new Long2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
            public final Object get(long j) {
                Object obj;
                obj = Byte2ObjectFunction.this.get(before.get(j));
                return obj;
            }
        };
    }

    default Byte2CharFunction andThenChar(final Object2CharFunction<V> after) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = after.getChar(Byte2ObjectFunction.this.get(b));
                return c;
            }
        };
    }

    default Char2ObjectFunction<V> composeChar(final Char2ByteFunction before) {
        return new Char2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.chars.Char2ObjectFunction
            public final Object get(char c) {
                Object obj;
                obj = Byte2ObjectFunction.this.get(before.get(c));
                return obj;
            }
        };
    }

    default Byte2FloatFunction andThenFloat(final Object2FloatFunction<V> after) {
        return new Byte2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.bytes.Byte2FloatFunction
            public final float get(byte b) {
                float f;
                f = after.getFloat(Byte2ObjectFunction.this.get(b));
                return f;
            }
        };
    }

    default Float2ObjectFunction<V> composeFloat(final Float2ByteFunction before) {
        return new Float2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.floats.Float2ObjectFunction
            public final Object get(float f) {
                Object obj;
                obj = Byte2ObjectFunction.this.get(before.get(f));
                return obj;
            }
        };
    }

    default Byte2DoubleFunction andThenDouble(final Object2DoubleFunction<V> after) {
        return new Byte2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction
            public final double get(byte b) {
                double d;
                d = after.getDouble(Byte2ObjectFunction.this.get(b));
                return d;
            }
        };
    }

    default Double2ObjectFunction<V> composeDouble(final Double2ByteFunction before) {
        return new Double2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.doubles.Double2ObjectFunction
            public final Object get(double d) {
                Object obj;
                obj = Byte2ObjectFunction.this.get(before.get(d));
                return obj;
            }
        };
    }

    default <T> Byte2ObjectFunction<T> andThenObject(final Object2ObjectFunction<? super V, ? extends T> after) {
        return new Byte2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction
            public final Object get(byte b) {
                Object obj;
                obj = after.get(Byte2ObjectFunction.this.get(b));
                return obj;
            }
        };
    }

    default <T> Object2ObjectFunction<T, V> composeObject(final Object2ByteFunction<? super T> before) {
        return new Object2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.objects.Object2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Byte2ObjectFunction.this.get(before.getByte(obj));
                return obj2;
            }
        };
    }

    default <T> Byte2ReferenceFunction<T> andThenReference(final Object2ReferenceFunction<? super V, ? extends T> after) {
        return new Byte2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction
            public final Object get(byte b) {
                Object obj;
                obj = after.get(Byte2ObjectFunction.this.get(b));
                return obj;
            }
        };
    }

    default <T> Reference2ObjectFunction<T, V> composeReference(final Reference2ByteFunction<? super T> before) {
        return new Reference2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.objects.Reference2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Byte2ObjectFunction.this.get(before.getByte(obj));
                return obj2;
            }
        };
    }
}
