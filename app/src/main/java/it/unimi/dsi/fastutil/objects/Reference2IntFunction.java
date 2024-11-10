package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;
import java.util.function.ToIntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Reference2IntFunction<K> extends Function<K, Integer>, ToIntFunction<K> {
    int getInt(Object obj);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Integer put(Object obj, Integer num) {
        return put2((Reference2IntFunction<K>) obj, num);
    }

    @Override // java.util.function.ToIntFunction
    default int applyAsInt(K operand) {
        return getInt(operand);
    }

    default int put(K key, int value) {
        throw new UnsupportedOperationException();
    }

    default int getOrDefault(Object key, int defaultValue) {
        int v = getInt(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default int removeInt(Object key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* renamed from: put, reason: avoid collision after fix types in other method */
    default Integer put2(K key, Integer value) {
        boolean containsKey = containsKey(key);
        int v = put((Reference2IntFunction<K>) key, value.intValue());
        if (containsKey) {
            return Integer.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer get(Object key) {
        int v = getInt(key);
        if (v != defaultReturnValue() || containsKey(key)) {
            return Integer.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        int v = getInt(key);
        return (v != defaultReturnValue() || containsKey(key)) ? Integer.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Integer remove(Object key) {
        if (containsKey(key)) {
            return Integer.valueOf(removeInt(key));
        }
        return null;
    }

    default void defaultReturnValue(int rv) {
        throw new UnsupportedOperationException();
    }

    default int defaultReturnValue() {
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super Integer, ? extends T> function) {
        return super.andThen(function);
    }

    default Reference2ByteFunction<K> andThenByte(final Int2ByteFunction after) {
        return new Reference2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.objects.Reference2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = after.get(Reference2IntFunction.this.getInt(obj));
                return b;
            }
        };
    }

    default Byte2IntFunction composeByte(final Byte2ReferenceFunction<K> before) {
        return new Byte2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.bytes.Byte2IntFunction
            public final int get(byte b) {
                int i;
                i = Reference2IntFunction.this.getInt(before.get(b));
                return i;
            }
        };
    }

    default Reference2ShortFunction<K> andThenShort(final Int2ShortFunction after) {
        return new Reference2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.objects.Reference2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = after.get(Reference2IntFunction.this.getInt(obj));
                return s;
            }
        };
    }

    default Short2IntFunction composeShort(final Short2ReferenceFunction<K> before) {
        return new Short2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.shorts.Short2IntFunction
            public final int get(short s) {
                int i;
                i = Reference2IntFunction.this.getInt(before.get(s));
                return i;
            }
        };
    }

    default Reference2IntFunction<K> andThenInt(final Int2IntFunction after) {
        return new Reference2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.objects.Reference2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = after.get(Reference2IntFunction.this.getInt(obj));
                return i;
            }
        };
    }

    default Int2IntFunction composeInt(final Int2ReferenceFunction<K> before) {
        return new Int2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.ints.Int2IntFunction
            public final int get(int i) {
                int i2;
                i2 = Reference2IntFunction.this.getInt(before.get(i));
                return i2;
            }
        };
    }

    default Reference2LongFunction<K> andThenLong(final Int2LongFunction after) {
        return new Reference2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.objects.Reference2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = after.get(Reference2IntFunction.this.getInt(obj));
                return j;
            }
        };
    }

    default Long2IntFunction composeLong(final Long2ReferenceFunction<K> before) {
        return new Long2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.longs.Long2IntFunction
            public final int get(long j) {
                int i;
                i = Reference2IntFunction.this.getInt(before.get(j));
                return i;
            }
        };
    }

    default Reference2CharFunction<K> andThenChar(final Int2CharFunction after) {
        return new Reference2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.objects.Reference2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = after.get(Reference2IntFunction.this.getInt(obj));
                return c;
            }
        };
    }

    default Char2IntFunction composeChar(final Char2ReferenceFunction<K> before) {
        return new Char2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.chars.Char2IntFunction
            public final int get(char c) {
                int i;
                i = Reference2IntFunction.this.getInt(before.get(c));
                return i;
            }
        };
    }

    default Reference2FloatFunction<K> andThenFloat(final Int2FloatFunction after) {
        return new Reference2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Reference2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = after.get(Reference2IntFunction.this.getInt(obj));
                return f;
            }
        };
    }

    default Float2IntFunction composeFloat(final Float2ReferenceFunction<K> before) {
        return new Float2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.floats.Float2IntFunction
            public final int get(float f) {
                int i;
                i = Reference2IntFunction.this.getInt(before.get(f));
                return i;
            }
        };
    }

    default Reference2DoubleFunction<K> andThenDouble(final Int2DoubleFunction after) {
        return new Reference2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.Reference2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = after.get(Reference2IntFunction.this.getInt(obj));
                return d;
            }
        };
    }

    default Double2IntFunction composeDouble(final Double2ReferenceFunction<K> before) {
        return new Double2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.doubles.Double2IntFunction
            public final int get(double d) {
                int i;
                i = Reference2IntFunction.this.getInt(before.get(d));
                return i;
            }
        };
    }

    default <T> Reference2ObjectFunction<K, T> andThenObject(final Int2ObjectFunction<? extends T> after) {
        return new Reference2ObjectFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.objects.Reference2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Reference2IntFunction.this.getInt(obj));
                return obj2;
            }
        };
    }

    default <T> Object2IntFunction<T> composeObject(final Object2ReferenceFunction<? super T, ? extends K> before) {
        return new Object2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = Reference2IntFunction.this.getInt(before.get(obj));
                return i;
            }
        };
    }

    default <T> Reference2ReferenceFunction<K, T> andThenReference(final Int2ReferenceFunction<? extends T> after) {
        return new Reference2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Reference2IntFunction.this.getInt(obj));
                return obj2;
            }
        };
    }

    default <T> Reference2IntFunction<T> composeReference(final Reference2ReferenceFunction<? super T, ? extends K> before) {
        return new Reference2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2IntFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.objects.Reference2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = Reference2IntFunction.this.getInt(before.get(obj));
                return i;
            }
        };
    }
}
