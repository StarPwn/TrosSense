package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2ObjectFunction;
import it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;
import java.util.function.ToDoubleFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Reference2DoubleFunction<K> extends Function<K, Double>, ToDoubleFunction<K> {
    double getDouble(Object obj);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Double put(Object obj, Double d) {
        return put2((Reference2DoubleFunction<K>) obj, d);
    }

    @Override // java.util.function.ToDoubleFunction
    default double applyAsDouble(K operand) {
        return getDouble(operand);
    }

    default double put(K key, double value) {
        throw new UnsupportedOperationException();
    }

    default double getOrDefault(Object key, double defaultValue) {
        double v = getDouble(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default double removeDouble(Object key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* renamed from: put, reason: avoid collision after fix types in other method */
    default Double put2(K key, Double value) {
        boolean containsKey = containsKey(key);
        double v = put((Reference2DoubleFunction<K>) key, value.doubleValue());
        if (containsKey) {
            return Double.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double get(Object key) {
        double v = getDouble(key);
        if (v != defaultReturnValue() || containsKey(key)) {
            return Double.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        double v = getDouble(key);
        return (v != defaultReturnValue() || containsKey(key)) ? Double.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double remove(Object key) {
        if (containsKey(key)) {
            return Double.valueOf(removeDouble(key));
        }
        return null;
    }

    default void defaultReturnValue(double rv) {
        throw new UnsupportedOperationException();
    }

    default double defaultReturnValue() {
        return 0.0d;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super Double, ? extends T> function) {
        return super.andThen(function);
    }

    default Reference2ByteFunction<K> andThenByte(final Double2ByteFunction after) {
        return new Reference2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.objects.Reference2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = after.get(Reference2DoubleFunction.this.getDouble(obj));
                return b;
            }
        };
    }

    default Byte2DoubleFunction composeByte(final Byte2ReferenceFunction<K> before) {
        return new Byte2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction
            public final double get(byte b) {
                double d;
                d = Reference2DoubleFunction.this.getDouble(before.get(b));
                return d;
            }
        };
    }

    default Reference2ShortFunction<K> andThenShort(final Double2ShortFunction after) {
        return new Reference2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.objects.Reference2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = after.get(Reference2DoubleFunction.this.getDouble(obj));
                return s;
            }
        };
    }

    default Short2DoubleFunction composeShort(final Short2ReferenceFunction<K> before) {
        return new Short2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.shorts.Short2DoubleFunction
            public final double get(short s) {
                double d;
                d = Reference2DoubleFunction.this.getDouble(before.get(s));
                return d;
            }
        };
    }

    default Reference2IntFunction<K> andThenInt(final Double2IntFunction after) {
        return new Reference2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.objects.Reference2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = after.get(Reference2DoubleFunction.this.getDouble(obj));
                return i;
            }
        };
    }

    default Int2DoubleFunction composeInt(final Int2ReferenceFunction<K> before) {
        return new Int2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.ints.Int2DoubleFunction
            public final double get(int i) {
                double d;
                d = Reference2DoubleFunction.this.getDouble(before.get(i));
                return d;
            }
        };
    }

    default Reference2LongFunction<K> andThenLong(final Double2LongFunction after) {
        return new Reference2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.objects.Reference2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = after.get(Reference2DoubleFunction.this.getDouble(obj));
                return j;
            }
        };
    }

    default Long2DoubleFunction composeLong(final Long2ReferenceFunction<K> before) {
        return new Long2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.longs.Long2DoubleFunction
            public final double get(long j) {
                double d;
                d = Reference2DoubleFunction.this.getDouble(before.get(j));
                return d;
            }
        };
    }

    default Reference2CharFunction<K> andThenChar(final Double2CharFunction after) {
        return new Reference2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.objects.Reference2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = after.get(Reference2DoubleFunction.this.getDouble(obj));
                return c;
            }
        };
    }

    default Char2DoubleFunction composeChar(final Char2ReferenceFunction<K> before) {
        return new Char2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.chars.Char2DoubleFunction
            public final double get(char c) {
                double d;
                d = Reference2DoubleFunction.this.getDouble(before.get(c));
                return d;
            }
        };
    }

    default Reference2FloatFunction<K> andThenFloat(final Double2FloatFunction after) {
        return new Reference2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.objects.Reference2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = after.get(Reference2DoubleFunction.this.getDouble(obj));
                return f;
            }
        };
    }

    default Float2DoubleFunction composeFloat(final Float2ReferenceFunction<K> before) {
        return new Float2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.floats.Float2DoubleFunction
            public final double get(float f) {
                double d;
                d = Reference2DoubleFunction.this.getDouble(before.get(f));
                return d;
            }
        };
    }

    default Reference2DoubleFunction<K> andThenDouble(final Double2DoubleFunction after) {
        return new Reference2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.objects.Reference2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = after.get(Reference2DoubleFunction.this.getDouble(obj));
                return d;
            }
        };
    }

    default Double2DoubleFunction composeDouble(final Double2ReferenceFunction<K> before) {
        return new Double2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.doubles.Double2DoubleFunction
            public final double get(double d) {
                double d2;
                d2 = Reference2DoubleFunction.this.getDouble(before.get(d));
                return d2;
            }
        };
    }

    default <T> Reference2ObjectFunction<K, T> andThenObject(final Double2ObjectFunction<? extends T> after) {
        return new Reference2ObjectFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.objects.Reference2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Reference2DoubleFunction.this.getDouble(obj));
                return obj2;
            }
        };
    }

    default <T> Object2DoubleFunction<T> composeObject(final Object2ReferenceFunction<? super T, ? extends K> before) {
        return new Object2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.objects.Object2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = Reference2DoubleFunction.this.getDouble(before.get(obj));
                return d;
            }
        };
    }

    default <T> Reference2ReferenceFunction<K, T> andThenReference(final Double2ReferenceFunction<? extends T> after) {
        return new Reference2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Reference2DoubleFunction.this.getDouble(obj));
                return obj2;
            }
        };
    }

    default <T> Reference2DoubleFunction<T> composeReference(final Reference2ReferenceFunction<? super T, ? extends K> before) {
        return new Reference2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2DoubleFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Reference2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = Reference2DoubleFunction.this.getDouble(before.get(obj));
                return d;
            }
        };
    }
}
