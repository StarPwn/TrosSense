package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;
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
public interface Reference2ShortFunction<K> extends Function<K, Short>, ToIntFunction<K> {
    short getShort(Object obj);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Short put(Object obj, Short sh) {
        return put2((Reference2ShortFunction<K>) obj, sh);
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
        short v = put((Reference2ShortFunction<K>) key, value.shortValue());
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

    default Reference2ByteFunction<K> andThenByte(final Short2ByteFunction after) {
        return new Reference2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.objects.Reference2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = after.get(Reference2ShortFunction.this.getShort(obj));
                return b;
            }
        };
    }

    default Byte2ShortFunction composeByte(final Byte2ReferenceFunction<K> before) {
        return new Byte2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ShortFunction
            public final short get(byte b) {
                short s;
                s = Reference2ShortFunction.this.getShort(before.get(b));
                return s;
            }
        };
    }

    default Reference2ShortFunction<K> andThenShort(final Short2ShortFunction after) {
        return new Reference2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.objects.Reference2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = after.get(Reference2ShortFunction.this.getShort(obj));
                return s;
            }
        };
    }

    default Short2ShortFunction composeShort(final Short2ReferenceFunction<K> before) {
        return new Short2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.shorts.Short2ShortFunction
            public final short get(short s) {
                short s2;
                s2 = Reference2ShortFunction.this.getShort(before.get(s));
                return s2;
            }
        };
    }

    default Reference2IntFunction<K> andThenInt(final Short2IntFunction after) {
        return new Reference2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Reference2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = after.get(Reference2ShortFunction.this.getShort(obj));
                return i;
            }
        };
    }

    default Int2ShortFunction composeInt(final Int2ReferenceFunction<K> before) {
        return new Int2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.ints.Int2ShortFunction
            public final short get(int i) {
                short s;
                s = Reference2ShortFunction.this.getShort(before.get(i));
                return s;
            }
        };
    }

    default Reference2LongFunction<K> andThenLong(final Short2LongFunction after) {
        return new Reference2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.objects.Reference2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = after.get(Reference2ShortFunction.this.getShort(obj));
                return j;
            }
        };
    }

    default Long2ShortFunction composeLong(final Long2ReferenceFunction<K> before) {
        return new Long2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.longs.Long2ShortFunction
            public final short get(long j) {
                short s;
                s = Reference2ShortFunction.this.getShort(before.get(j));
                return s;
            }
        };
    }

    default Reference2CharFunction<K> andThenChar(final Short2CharFunction after) {
        return new Reference2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.objects.Reference2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = after.get(Reference2ShortFunction.this.getShort(obj));
                return c;
            }
        };
    }

    default Char2ShortFunction composeChar(final Char2ReferenceFunction<K> before) {
        return new Char2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.chars.Char2ShortFunction
            public final short get(char c) {
                short s;
                s = Reference2ShortFunction.this.getShort(before.get(c));
                return s;
            }
        };
    }

    default Reference2FloatFunction<K> andThenFloat(final Short2FloatFunction after) {
        return new Reference2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.objects.Reference2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = after.get(Reference2ShortFunction.this.getShort(obj));
                return f;
            }
        };
    }

    default Float2ShortFunction composeFloat(final Float2ReferenceFunction<K> before) {
        return new Float2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.floats.Float2ShortFunction
            public final short get(float f) {
                short s;
                s = Reference2ShortFunction.this.getShort(before.get(f));
                return s;
            }
        };
    }

    default Reference2DoubleFunction<K> andThenDouble(final Short2DoubleFunction after) {
        return new Reference2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.objects.Reference2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = after.get(Reference2ShortFunction.this.getShort(obj));
                return d;
            }
        };
    }

    default Double2ShortFunction composeDouble(final Double2ReferenceFunction<K> before) {
        return new Double2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.doubles.Double2ShortFunction
            public final short get(double d) {
                short s;
                s = Reference2ShortFunction.this.getShort(before.get(d));
                return s;
            }
        };
    }

    default <T> Reference2ObjectFunction<K, T> andThenObject(final Short2ObjectFunction<? extends T> after) {
        return new Reference2ObjectFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Reference2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Reference2ShortFunction.this.getShort(obj));
                return obj2;
            }
        };
    }

    default <T> Object2ShortFunction<T> composeObject(final Object2ReferenceFunction<? super T, ? extends K> before) {
        return new Object2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.objects.Object2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = Reference2ShortFunction.this.getShort(before.get(obj));
                return s;
            }
        };
    }

    default <T> Reference2ReferenceFunction<K, T> andThenReference(final Short2ReferenceFunction<? extends T> after) {
        return new Reference2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Reference2ShortFunction.this.getShort(obj));
                return obj2;
            }
        };
    }

    default <T> Reference2ShortFunction<T> composeReference(final Reference2ReferenceFunction<? super T, ? extends K> before) {
        return new Reference2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ShortFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.Reference2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = Reference2ShortFunction.this.getShort(before.get(obj));
                return s;
            }
        };
    }
}
