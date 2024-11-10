package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;
import java.util.function.ToDoubleFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Reference2FloatFunction<K> extends Function<K, Float>, ToDoubleFunction<K> {
    float getFloat(Object obj);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Float put(Object obj, Float f) {
        return put2((Reference2FloatFunction<K>) obj, f);
    }

    @Override // java.util.function.ToDoubleFunction
    default double applyAsDouble(K operand) {
        return getFloat(operand);
    }

    default float put(K key, float value) {
        throw new UnsupportedOperationException();
    }

    default float getOrDefault(Object key, float defaultValue) {
        float v = getFloat(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default float removeFloat(Object key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* renamed from: put, reason: avoid collision after fix types in other method */
    default Float put2(K key, Float value) {
        boolean containsKey = containsKey(key);
        float v = put((Reference2FloatFunction<K>) key, value.floatValue());
        if (containsKey) {
            return Float.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float get(Object key) {
        float v = getFloat(key);
        if (v != defaultReturnValue() || containsKey(key)) {
            return Float.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        float v = getFloat(key);
        return (v != defaultReturnValue() || containsKey(key)) ? Float.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float remove(Object key) {
        if (containsKey(key)) {
            return Float.valueOf(removeFloat(key));
        }
        return null;
    }

    default void defaultReturnValue(float rv) {
        throw new UnsupportedOperationException();
    }

    default float defaultReturnValue() {
        return 0.0f;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super Float, ? extends T> function) {
        return super.andThen(function);
    }

    default Reference2ByteFunction<K> andThenByte(final Float2ByteFunction after) {
        return new Reference2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.objects.Reference2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = after.get(Reference2FloatFunction.this.getFloat(obj));
                return b;
            }
        };
    }

    default Byte2FloatFunction composeByte(final Byte2ReferenceFunction<K> before) {
        return new Byte2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.bytes.Byte2FloatFunction
            public final float get(byte b) {
                float f;
                f = Reference2FloatFunction.this.getFloat(before.get(b));
                return f;
            }
        };
    }

    default Reference2ShortFunction<K> andThenShort(final Float2ShortFunction after) {
        return new Reference2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.objects.Reference2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = after.get(Reference2FloatFunction.this.getFloat(obj));
                return s;
            }
        };
    }

    default Short2FloatFunction composeShort(final Short2ReferenceFunction<K> before) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = Reference2FloatFunction.this.getFloat(before.get(s));
                return f;
            }
        };
    }

    default Reference2IntFunction<K> andThenInt(final Float2IntFunction after) {
        return new Reference2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.objects.Reference2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = after.get(Reference2FloatFunction.this.getFloat(obj));
                return i;
            }
        };
    }

    default Int2FloatFunction composeInt(final Int2ReferenceFunction<K> before) {
        return new Int2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.ints.Int2FloatFunction
            public final float get(int i) {
                float f;
                f = Reference2FloatFunction.this.getFloat(before.get(i));
                return f;
            }
        };
    }

    default Reference2LongFunction<K> andThenLong(final Float2LongFunction after) {
        return new Reference2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.objects.Reference2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = after.get(Reference2FloatFunction.this.getFloat(obj));
                return j;
            }
        };
    }

    default Long2FloatFunction composeLong(final Long2ReferenceFunction<K> before) {
        return new Long2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.longs.Long2FloatFunction
            public final float get(long j) {
                float f;
                f = Reference2FloatFunction.this.getFloat(before.get(j));
                return f;
            }
        };
    }

    default Reference2CharFunction<K> andThenChar(final Float2CharFunction after) {
        return new Reference2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.objects.Reference2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = after.get(Reference2FloatFunction.this.getFloat(obj));
                return c;
            }
        };
    }

    default Char2FloatFunction composeChar(final Char2ReferenceFunction<K> before) {
        return new Char2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.chars.Char2FloatFunction
            public final float get(char c) {
                float f;
                f = Reference2FloatFunction.this.getFloat(before.get(c));
                return f;
            }
        };
    }

    default Reference2FloatFunction<K> andThenFloat(final Float2FloatFunction after) {
        return new Reference2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Reference2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = after.get(Reference2FloatFunction.this.getFloat(obj));
                return f;
            }
        };
    }

    default Float2FloatFunction composeFloat(final Float2ReferenceFunction<K> before) {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                float f2;
                f2 = Reference2FloatFunction.this.getFloat(before.get(f));
                return f2;
            }
        };
    }

    default Reference2DoubleFunction<K> andThenDouble(final Float2DoubleFunction after) {
        return new Reference2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.objects.Reference2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = after.get(Reference2FloatFunction.this.getFloat(obj));
                return d;
            }
        };
    }

    default Double2FloatFunction composeDouble(final Double2ReferenceFunction<K> before) {
        return new Double2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.doubles.Double2FloatFunction
            public final float get(double d) {
                float f;
                f = Reference2FloatFunction.this.getFloat(before.get(d));
                return f;
            }
        };
    }

    default <T> Reference2ObjectFunction<K, T> andThenObject(final Float2ObjectFunction<? extends T> after) {
        return new Reference2ObjectFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.objects.Reference2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Reference2FloatFunction.this.getFloat(obj));
                return obj2;
            }
        };
    }

    default <T> Object2FloatFunction<T> composeObject(final Object2ReferenceFunction<? super T, ? extends K> before) {
        return new Object2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.objects.Object2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = Reference2FloatFunction.this.getFloat(before.get(obj));
                return f;
            }
        };
    }

    default <T> Reference2ReferenceFunction<K, T> andThenReference(final Float2ReferenceFunction<? extends T> after) {
        return new Reference2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Reference2FloatFunction.this.getFloat(obj));
                return obj2;
            }
        };
    }

    default <T> Reference2FloatFunction<T> composeReference(final Reference2ReferenceFunction<? super T, ? extends K> before) {
        return new Reference2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2FloatFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.objects.Reference2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = Reference2FloatFunction.this.getFloat(before.get(obj));
                return f;
            }
        };
    }
}
