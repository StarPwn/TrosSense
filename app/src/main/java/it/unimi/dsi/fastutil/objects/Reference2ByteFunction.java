package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2ByteFunction;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;
import java.util.function.ToIntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Reference2ByteFunction<K> extends Function<K, Byte>, ToIntFunction<K> {
    byte getByte(Object obj);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Byte put(Object obj, Byte b) {
        return put2((Reference2ByteFunction<K>) obj, b);
    }

    @Override // java.util.function.ToIntFunction
    default int applyAsInt(K operand) {
        return getByte(operand);
    }

    default byte put(K key, byte value) {
        throw new UnsupportedOperationException();
    }

    default byte getOrDefault(Object key, byte defaultValue) {
        byte v = getByte(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default byte removeByte(Object key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* renamed from: put, reason: avoid collision after fix types in other method */
    default Byte put2(K key, Byte value) {
        boolean containsKey = containsKey(key);
        byte v = put((Reference2ByteFunction<K>) key, value.byteValue());
        if (containsKey) {
            return Byte.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte get(Object key) {
        byte v = getByte(key);
        if (v != defaultReturnValue() || containsKey(key)) {
            return Byte.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        byte v = getByte(key);
        return (v != defaultReturnValue() || containsKey(key)) ? Byte.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte remove(Object key) {
        if (containsKey(key)) {
            return Byte.valueOf(removeByte(key));
        }
        return null;
    }

    default void defaultReturnValue(byte rv) {
        throw new UnsupportedOperationException();
    }

    default byte defaultReturnValue() {
        return (byte) 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super Byte, ? extends T> function) {
        return super.andThen(function);
    }

    default Reference2ByteFunction<K> andThenByte(final Byte2ByteFunction after) {
        return new Reference2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.objects.Reference2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = after.get(Reference2ByteFunction.this.getByte(obj));
                return b;
            }
        };
    }

    default Byte2ByteFunction composeByte(final Byte2ReferenceFunction<K> before) {
        return new Byte2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ByteFunction
            public final byte get(byte b) {
                byte b2;
                b2 = Reference2ByteFunction.this.getByte(before.get(b));
                return b2;
            }
        };
    }

    default Reference2ShortFunction<K> andThenShort(final Byte2ShortFunction after) {
        return new Reference2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.objects.Reference2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = after.get(Reference2ByteFunction.this.getByte(obj));
                return s;
            }
        };
    }

    default Short2ByteFunction composeShort(final Short2ReferenceFunction<K> before) {
        return new Short2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.shorts.Short2ByteFunction
            public final byte get(short s) {
                byte b;
                b = Reference2ByteFunction.this.getByte(before.get(s));
                return b;
            }
        };
    }

    default Reference2IntFunction<K> andThenInt(final Byte2IntFunction after) {
        return new Reference2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.objects.Reference2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = after.get(Reference2ByteFunction.this.getByte(obj));
                return i;
            }
        };
    }

    default Int2ByteFunction composeInt(final Int2ReferenceFunction<K> before) {
        return new Int2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.ints.Int2ByteFunction
            public final byte get(int i) {
                byte b;
                b = Reference2ByteFunction.this.getByte(before.get(i));
                return b;
            }
        };
    }

    default Reference2LongFunction<K> andThenLong(final Byte2LongFunction after) {
        return new Reference2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.objects.Reference2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = after.get(Reference2ByteFunction.this.getByte(obj));
                return j;
            }
        };
    }

    default Long2ByteFunction composeLong(final Long2ReferenceFunction<K> before) {
        return new Long2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.longs.Long2ByteFunction
            public final byte get(long j) {
                byte b;
                b = Reference2ByteFunction.this.getByte(before.get(j));
                return b;
            }
        };
    }

    default Reference2CharFunction<K> andThenChar(final Byte2CharFunction after) {
        return new Reference2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.objects.Reference2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = after.get(Reference2ByteFunction.this.getByte(obj));
                return c;
            }
        };
    }

    default Char2ByteFunction composeChar(final Char2ReferenceFunction<K> before) {
        return new Char2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.chars.Char2ByteFunction
            public final byte get(char c) {
                byte b;
                b = Reference2ByteFunction.this.getByte(before.get(c));
                return b;
            }
        };
    }

    default Reference2FloatFunction<K> andThenFloat(final Byte2FloatFunction after) {
        return new Reference2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.objects.Reference2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = after.get(Reference2ByteFunction.this.getByte(obj));
                return f;
            }
        };
    }

    default Float2ByteFunction composeFloat(final Float2ReferenceFunction<K> before) {
        return new Float2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.floats.Float2ByteFunction
            public final byte get(float f) {
                byte b;
                b = Reference2ByteFunction.this.getByte(before.get(f));
                return b;
            }
        };
    }

    default Reference2DoubleFunction<K> andThenDouble(final Byte2DoubleFunction after) {
        return new Reference2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Reference2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = after.get(Reference2ByteFunction.this.getByte(obj));
                return d;
            }
        };
    }

    default Double2ByteFunction composeDouble(final Double2ReferenceFunction<K> before) {
        return new Double2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.doubles.Double2ByteFunction
            public final byte get(double d) {
                byte b;
                b = Reference2ByteFunction.this.getByte(before.get(d));
                return b;
            }
        };
    }

    default <T> Reference2ObjectFunction<K, T> andThenObject(final Byte2ObjectFunction<? extends T> after) {
        return new Reference2ObjectFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.objects.Reference2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Reference2ByteFunction.this.getByte(obj));
                return obj2;
            }
        };
    }

    default <T> Object2ByteFunction<T> composeObject(final Object2ReferenceFunction<? super T, ? extends K> before) {
        return new Object2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.objects.Object2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = Reference2ByteFunction.this.getByte(before.get(obj));
                return b;
            }
        };
    }

    default <T> Reference2ReferenceFunction<K, T> andThenReference(final Byte2ReferenceFunction<? extends T> after) {
        return new Reference2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Reference2ByteFunction.this.getByte(obj));
                return obj2;
            }
        };
    }

    default <T> Reference2ByteFunction<T> composeReference(final Reference2ReferenceFunction<? super T, ? extends K> before) {
        return new Reference2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2ByteFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.objects.Reference2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = Reference2ByteFunction.this.getByte(before.get(obj));
                return b;
            }
        };
    }
}
