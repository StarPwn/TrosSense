package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2ObjectFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import it.unimi.dsi.fastutil.longs.Long2CharFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.longs.Long2LongFunction;
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import java.util.function.ToLongFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Object2LongFunction<K> extends Function<K, Long>, ToLongFunction<K> {
    long getLong(Object obj);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Long put(Object obj, Long l) {
        return put2((Object2LongFunction<K>) obj, l);
    }

    @Override // java.util.function.ToLongFunction
    default long applyAsLong(K operand) {
        return getLong(operand);
    }

    default long put(K key, long value) {
        throw new UnsupportedOperationException();
    }

    default long getOrDefault(Object key, long defaultValue) {
        long v = getLong(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default long removeLong(Object key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* renamed from: put, reason: avoid collision after fix types in other method */
    default Long put2(K key, Long value) {
        boolean containsKey = containsKey(key);
        long v = put((Object2LongFunction<K>) key, value.longValue());
        if (containsKey) {
            return Long.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long get(Object key) {
        long v = getLong(key);
        if (v != defaultReturnValue() || containsKey(key)) {
            return Long.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        long v = getLong(key);
        return (v != defaultReturnValue() || containsKey(key)) ? Long.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Long remove(Object key) {
        if (containsKey(key)) {
            return Long.valueOf(removeLong(key));
        }
        return null;
    }

    default void defaultReturnValue(long rv) {
        throw new UnsupportedOperationException();
    }

    default long defaultReturnValue() {
        return 0L;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super Long, ? extends T> function) {
        return super.andThen(function);
    }

    default Object2ByteFunction<K> andThenByte(final Long2ByteFunction after) {
        return new Object2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.objects.Object2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = after.get(Object2LongFunction.this.getLong(obj));
                return b;
            }
        };
    }

    default Byte2LongFunction composeByte(final Byte2ObjectFunction<K> before) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = Object2LongFunction.this.getLong(before.get(b));
                return j;
            }
        };
    }

    default Object2ShortFunction<K> andThenShort(final Long2ShortFunction after) {
        return new Object2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Object2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = after.get(Object2LongFunction.this.getLong(obj));
                return s;
            }
        };
    }

    default Short2LongFunction composeShort(final Short2ObjectFunction<K> before) {
        return new Short2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.shorts.Short2LongFunction
            public final long get(short s) {
                long j;
                j = Object2LongFunction.this.getLong(before.get(s));
                return j;
            }
        };
    }

    default Object2IntFunction<K> andThenInt(final Long2IntFunction after) {
        return new Object2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = after.get(Object2LongFunction.this.getLong(obj));
                return i;
            }
        };
    }

    default Int2LongFunction composeInt(final Int2ObjectFunction<K> before) {
        return new Int2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.ints.Int2LongFunction
            public final long get(int i) {
                long j;
                j = Object2LongFunction.this.getLong(before.get(i));
                return j;
            }
        };
    }

    default Object2LongFunction<K> andThenLong(final Long2LongFunction after) {
        return new Object2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.objects.Object2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = after.get(Object2LongFunction.this.getLong(obj));
                return j;
            }
        };
    }

    default Long2LongFunction composeLong(final Long2ObjectFunction<K> before) {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                long j2;
                j2 = Object2LongFunction.this.getLong(before.get(j));
                return j2;
            }
        };
    }

    default Object2CharFunction<K> andThenChar(final Long2CharFunction after) {
        return new Object2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.objects.Object2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = after.get(Object2LongFunction.this.getLong(obj));
                return c;
            }
        };
    }

    default Char2LongFunction composeChar(final Char2ObjectFunction<K> before) {
        return new Char2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.chars.Char2LongFunction
            public final long get(char c) {
                long j;
                j = Object2LongFunction.this.getLong(before.get(c));
                return j;
            }
        };
    }

    default Object2FloatFunction<K> andThenFloat(final Long2FloatFunction after) {
        return new Object2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.objects.Object2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = after.get(Object2LongFunction.this.getLong(obj));
                return f;
            }
        };
    }

    default Float2LongFunction composeFloat(final Float2ObjectFunction<K> before) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = Object2LongFunction.this.getLong(before.get(f));
                return j;
            }
        };
    }

    default Object2DoubleFunction<K> andThenDouble(final Long2DoubleFunction after) {
        return new Object2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.objects.Object2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = after.get(Object2LongFunction.this.getLong(obj));
                return d;
            }
        };
    }

    default Double2LongFunction composeDouble(final Double2ObjectFunction<K> before) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = Object2LongFunction.this.getLong(before.get(d));
                return j;
            }
        };
    }

    default <T> Object2ObjectFunction<K, T> andThenObject(final Long2ObjectFunction<? extends T> after) {
        return new Object2ObjectFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.objects.Object2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Object2LongFunction.this.getLong(obj));
                return obj2;
            }
        };
    }

    default <T> Object2LongFunction<T> composeObject(final Object2ObjectFunction<? super T, ? extends K> before) {
        return new Object2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.objects.Object2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Object2LongFunction.this.getLong(before.get(obj));
                return j;
            }
        };
    }

    default <T> Object2ReferenceFunction<K, T> andThenReference(final Long2ReferenceFunction<? extends T> after) {
        return new Object2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.objects.Object2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Object2LongFunction.this.getLong(obj));
                return obj2;
            }
        };
    }

    default <T> Reference2LongFunction<T> composeReference(final Reference2ObjectFunction<? super T, ? extends K> before) {
        return new Reference2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2LongFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.objects.Reference2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = Object2LongFunction.this.getLong(before.get(obj));
                return j;
            }
        };
    }
}
