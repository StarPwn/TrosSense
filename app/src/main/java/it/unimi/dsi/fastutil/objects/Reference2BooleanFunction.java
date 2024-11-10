package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2CharFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2IntFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2LongFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction;
import it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction;
import it.unimi.dsi.fastutil.chars.Char2BooleanFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.doubles.Double2BooleanFunction;
import it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import it.unimi.dsi.fastutil.floats.Float2BooleanFunction;
import it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import it.unimi.dsi.fastutil.longs.Long2BooleanFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import it.unimi.dsi.fastutil.shorts.Short2BooleanFunction;
import it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;
import java.util.function.Predicate;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Reference2BooleanFunction<K> extends Function<K, Boolean>, Predicate<K> {
    boolean getBoolean(Object obj);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Boolean put(Object obj, Boolean bool) {
        return put2((Reference2BooleanFunction<K>) obj, bool);
    }

    @Override // java.util.function.Predicate
    default boolean test(K operand) {
        return getBoolean(operand);
    }

    default boolean put(K key, boolean value) {
        throw new UnsupportedOperationException();
    }

    default boolean getOrDefault(Object key, boolean defaultValue) {
        boolean v = getBoolean(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default boolean removeBoolean(Object key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* renamed from: put, reason: avoid collision after fix types in other method */
    default Boolean put2(K key, Boolean value) {
        boolean containsKey = containsKey(key);
        boolean v = put((Reference2BooleanFunction<K>) key, value.booleanValue());
        if (containsKey) {
            return Boolean.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Boolean get(Object key) {
        boolean v = getBoolean(key);
        if (v != defaultReturnValue() || containsKey(key)) {
            return Boolean.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Boolean getOrDefault(Object key, Boolean defaultValue) {
        boolean v = getBoolean(key);
        return (v != defaultReturnValue() || containsKey(key)) ? Boolean.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Boolean remove(Object key) {
        if (containsKey(key)) {
            return Boolean.valueOf(removeBoolean(key));
        }
        return null;
    }

    default void defaultReturnValue(boolean rv) {
        throw new UnsupportedOperationException();
    }

    default boolean defaultReturnValue() {
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super Boolean, ? extends T> function) {
        return super.andThen(function);
    }

    default Reference2ByteFunction<K> andThenByte(final Boolean2ByteFunction after) {
        return new Reference2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.Reference2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = after.get(Reference2BooleanFunction.this.getBoolean(obj));
                return b;
            }
        };
    }

    default Byte2BooleanFunction composeByte(final Byte2ReferenceFunction<K> before) {
        return new Byte2BooleanFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.bytes.Byte2BooleanFunction
            public final boolean get(byte b) {
                boolean z;
                z = Reference2BooleanFunction.this.getBoolean(before.get(b));
                return z;
            }
        };
    }

    default Reference2ShortFunction<K> andThenShort(final Boolean2ShortFunction after) {
        return new Reference2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.objects.Reference2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = after.get(Reference2BooleanFunction.this.getBoolean(obj));
                return s;
            }
        };
    }

    default Short2BooleanFunction composeShort(final Short2ReferenceFunction<K> before) {
        return new Short2BooleanFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.shorts.Short2BooleanFunction
            public final boolean get(short s) {
                boolean z;
                z = Reference2BooleanFunction.this.getBoolean(before.get(s));
                return z;
            }
        };
    }

    default Reference2IntFunction<K> andThenInt(final Boolean2IntFunction after) {
        return new Reference2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Reference2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = after.get(Reference2BooleanFunction.this.getBoolean(obj));
                return i;
            }
        };
    }

    default Int2BooleanFunction composeInt(final Int2ReferenceFunction<K> before) {
        return new Int2BooleanFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.ints.Int2BooleanFunction
            public final boolean get(int i) {
                boolean z;
                z = Reference2BooleanFunction.this.getBoolean(before.get(i));
                return z;
            }
        };
    }

    default Reference2LongFunction<K> andThenLong(final Boolean2LongFunction after) {
        return new Reference2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.objects.Reference2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = after.get(Reference2BooleanFunction.this.getBoolean(obj));
                return j;
            }
        };
    }

    default Long2BooleanFunction composeLong(final Long2ReferenceFunction<K> before) {
        return new Long2BooleanFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.longs.Long2BooleanFunction
            public final boolean get(long j) {
                boolean z;
                z = Reference2BooleanFunction.this.getBoolean(before.get(j));
                return z;
            }
        };
    }

    default Reference2CharFunction<K> andThenChar(final Boolean2CharFunction after) {
        return new Reference2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.objects.Reference2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = after.get(Reference2BooleanFunction.this.getBoolean(obj));
                return c;
            }
        };
    }

    default Char2BooleanFunction composeChar(final Char2ReferenceFunction<K> before) {
        return new Char2BooleanFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.chars.Char2BooleanFunction
            public final boolean get(char c) {
                boolean z;
                z = Reference2BooleanFunction.this.getBoolean(before.get(c));
                return z;
            }
        };
    }

    default Reference2FloatFunction<K> andThenFloat(final Boolean2FloatFunction after) {
        return new Reference2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Reference2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = after.get(Reference2BooleanFunction.this.getBoolean(obj));
                return f;
            }
        };
    }

    default Float2BooleanFunction composeFloat(final Float2ReferenceFunction<K> before) {
        return new Float2BooleanFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.floats.Float2BooleanFunction
            public final boolean get(float f) {
                boolean z;
                z = Reference2BooleanFunction.this.getBoolean(before.get(f));
                return z;
            }
        };
    }

    default Reference2DoubleFunction<K> andThenDouble(final Boolean2DoubleFunction after) {
        return new Reference2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.objects.Reference2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = after.get(Reference2BooleanFunction.this.getBoolean(obj));
                return d;
            }
        };
    }

    default Double2BooleanFunction composeDouble(final Double2ReferenceFunction<K> before) {
        return new Double2BooleanFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.doubles.Double2BooleanFunction
            public final boolean get(double d) {
                boolean z;
                z = Reference2BooleanFunction.this.getBoolean(before.get(d));
                return z;
            }
        };
    }

    default <T> Reference2ObjectFunction<K, T> andThenObject(final Boolean2ObjectFunction<? extends T> after) {
        return new Reference2ObjectFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.objects.Reference2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Reference2BooleanFunction.this.getBoolean(obj));
                return obj2;
            }
        };
    }

    default <T> Object2BooleanFunction<T> composeObject(final Object2ReferenceFunction<? super T, ? extends K> before) {
        return new Object2BooleanFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.objects.Object2BooleanFunction
            public final boolean getBoolean(Object obj) {
                boolean z;
                z = Reference2BooleanFunction.this.getBoolean(before.get(obj));
                return z;
            }
        };
    }

    default <T> Reference2ReferenceFunction<K, T> andThenReference(final Boolean2ReferenceFunction<? extends T> after) {
        return new Reference2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Reference2BooleanFunction.this.getBoolean(obj));
                return obj2;
            }
        };
    }

    default <T> Reference2BooleanFunction<T> composeReference(final Reference2ReferenceFunction<? super T, ? extends K> before) {
        return new Reference2BooleanFunction() { // from class: it.unimi.dsi.fastutil.objects.Reference2BooleanFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.objects.Reference2BooleanFunction
            public final boolean getBoolean(Object obj) {
                boolean z;
                z = Reference2BooleanFunction.this.getBoolean(before.get(obj));
                return z;
            }
        };
    }
}
