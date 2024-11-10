package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;
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
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;
import it.unimi.dsi.fastutil.shorts.Short2ShortFunction;
import java.util.function.ToIntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Object2ShortFunction<K> extends Function<K, Short>, ToIntFunction<K> {
    short getShort(Object obj);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Short put(Object obj, Short sh) {
        return put2((Object2ShortFunction<K>) obj, sh);
    }

    @Override // java.util.function.ToIntFunction
    default int applyAsInt(K operand) {
        return getShort(operand);
    }

    default short put(K key, short value) {
        throw new UnsupportedOperationException();
    }

    default short getOrDefault(Object key, short defaultValue) {
        short v = getShort(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default short removeShort(Object key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* renamed from: put, reason: avoid collision after fix types in other method */
    default Short put2(K key, Short value) {
        boolean containsKey = containsKey(key);
        short v = put((Object2ShortFunction<K>) key, value.shortValue());
        if (containsKey) {
            return Short.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short get(Object key) {
        short v = getShort(key);
        if (v != defaultReturnValue() || containsKey(key)) {
            return Short.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        short v = getShort(key);
        return (v != defaultReturnValue() || containsKey(key)) ? Short.valueOf(v) : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Short remove(Object key) {
        if (containsKey(key)) {
            return Short.valueOf(removeShort(key));
        }
        return null;
    }

    default void defaultReturnValue(short rv) {
        throw new UnsupportedOperationException();
    }

    default short defaultReturnValue() {
        return (short) 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super Short, ? extends T> function) {
        return super.andThen(function);
    }

    default Object2ByteFunction<K> andThenByte(final Short2ByteFunction after) {
        return new Object2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.objects.Object2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = after.get(Object2ShortFunction.this.getShort(obj));
                return b;
            }
        };
    }

    default Byte2ShortFunction composeByte(final Byte2ObjectFunction<K> before) {
        return new Byte2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ShortFunction
            public final short get(byte b) {
                short s;
                s = Object2ShortFunction.this.getShort(before.get(b));
                return s;
            }
        };
    }

    default Object2ShortFunction<K> andThenShort(final Short2ShortFunction after) {
        return new Object2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.objects.Object2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = after.get(Object2ShortFunction.this.getShort(obj));
                return s;
            }
        };
    }

    default Short2ShortFunction composeShort(final Short2ObjectFunction<K> before) {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                short s2;
                s2 = Object2ShortFunction.this.getShort(before.get(s));
                return s2;
            }
        };
    }

    default Object2IntFunction<K> andThenInt(final Short2IntFunction after) {
        return new Object2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = after.get(Object2ShortFunction.this.getShort(obj));
                return i;
            }
        };
    }

    default Int2ShortFunction composeInt(final Int2ObjectFunction<K> before) {
        return new Int2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.ints.Int2ShortFunction
            public final short get(int i) {
                short s;
                s = Object2ShortFunction.this.getShort(before.get(i));
                return s;
            }
        };
    }

    default Object2LongFunction<K> andThenLong(final Short2LongFunction after) {
        return new Object2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.objects.Object2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = after.get(Object2ShortFunction.this.getShort(obj));
                return j;
            }
        };
    }

    default Long2ShortFunction composeLong(final Long2ObjectFunction<K> before) {
        return new Long2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.longs.Long2ShortFunction
            public final short get(long j) {
                short s;
                s = Object2ShortFunction.this.getShort(before.get(j));
                return s;
            }
        };
    }

    default Object2CharFunction<K> andThenChar(final Short2CharFunction after) {
        return new Object2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.objects.Object2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = after.get(Object2ShortFunction.this.getShort(obj));
                return c;
            }
        };
    }

    default Char2ShortFunction composeChar(final Char2ObjectFunction<K> before) {
        return new Char2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.chars.Char2ShortFunction
            public final short get(char c) {
                short s;
                s = Object2ShortFunction.this.getShort(before.get(c));
                return s;
            }
        };
    }

    default Object2FloatFunction<K> andThenFloat(final Short2FloatFunction after) {
        return new Object2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.objects.Object2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = after.get(Object2ShortFunction.this.getShort(obj));
                return f;
            }
        };
    }

    default Float2ShortFunction composeFloat(final Float2ObjectFunction<K> before) {
        return new Float2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.floats.Float2ShortFunction
            public final short get(float f) {
                short s;
                s = Object2ShortFunction.this.getShort(before.get(f));
                return s;
            }
        };
    }

    default Object2DoubleFunction<K> andThenDouble(final Short2DoubleFunction after) {
        return new Object2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.objects.Object2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = after.get(Object2ShortFunction.this.getShort(obj));
                return d;
            }
        };
    }

    default Double2ShortFunction composeDouble(final Double2ObjectFunction<K> before) {
        return new Double2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.doubles.Double2ShortFunction
            public final short get(double d) {
                short s;
                s = Object2ShortFunction.this.getShort(before.get(d));
                return s;
            }
        };
    }

    default <T> Object2ObjectFunction<K, T> andThenObject(final Short2ObjectFunction<? extends T> after) {
        return new Object2ObjectFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Object2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Object2ShortFunction.this.getShort(obj));
                return obj2;
            }
        };
    }

    default <T> Object2ShortFunction<T> composeObject(final Object2ObjectFunction<? super T, ? extends K> before) {
        return new Object2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.objects.Object2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = Object2ShortFunction.this.getShort(before.get(obj));
                return s;
            }
        };
    }

    default <T> Object2ReferenceFunction<K, T> andThenReference(final Short2ReferenceFunction<? extends T> after) {
        return new Object2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.objects.Object2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Object2ShortFunction.this.getShort(obj));
                return obj2;
            }
        };
    }

    default <T> Reference2ShortFunction<T> composeReference(final Reference2ObjectFunction<? super T, ? extends K> before) {
        return new Reference2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2ShortFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.objects.Reference2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = Object2ShortFunction.this.getShort(before.get(obj));
                return s;
            }
        };
    }
}
