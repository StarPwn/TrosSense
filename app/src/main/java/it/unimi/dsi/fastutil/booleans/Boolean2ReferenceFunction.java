package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.Function;
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
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Object2ReferenceFunction;
import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import it.unimi.dsi.fastutil.objects.Reference2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction;
import it.unimi.dsi.fastutil.objects.Reference2ShortFunction;
import it.unimi.dsi.fastutil.shorts.Short2BooleanFunction;
import it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Boolean2ReferenceFunction<V> extends Function<Boolean, V> {
    V get(boolean z);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Object put(Boolean bool, Object obj) {
        return put2(bool, (Boolean) obj);
    }

    default V put(boolean key, V value) {
        throw new UnsupportedOperationException();
    }

    default V getOrDefault(boolean key, V defaultValue) {
        V v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default V remove(boolean key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* renamed from: put, reason: avoid collision after fix types in other method */
    default V put2(Boolean key, V value) {
        boolean k = key.booleanValue();
        boolean containsKey = containsKey(k);
        V v = put(k, (boolean) value);
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
        boolean k = ((Boolean) key).booleanValue();
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
        boolean k = ((Boolean) key).booleanValue();
        V v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? v : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default V remove(Object key) {
        if (key == null) {
            return null;
        }
        boolean k = ((Boolean) key).booleanValue();
        if (containsKey(k)) {
            return remove(k);
        }
        return null;
    }

    default boolean containsKey(boolean key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Boolean) key).booleanValue());
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
    default <T> java.util.function.Function<T, V> compose(java.util.function.Function<? super T, ? extends Boolean> function) {
        return super.compose(function);
    }

    default Boolean2ByteFunction andThenByte(final Reference2ByteFunction<V> after) {
        return new Boolean2ByteFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ByteFunction
            public final byte get(boolean z) {
                byte b;
                b = after.getByte(Boolean2ReferenceFunction.this.get(z));
                return b;
            }
        };
    }

    default Byte2ReferenceFunction<V> composeByte(final Byte2BooleanFunction before) {
        return new Byte2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction
            public final Object get(byte b) {
                Object obj;
                obj = Boolean2ReferenceFunction.this.get(before.get(b));
                return obj;
            }
        };
    }

    default Boolean2ShortFunction andThenShort(final Reference2ShortFunction<V> after) {
        return new Boolean2ShortFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction
            public final short get(boolean z) {
                short s;
                s = after.getShort(Boolean2ReferenceFunction.this.get(z));
                return s;
            }
        };
    }

    default Short2ReferenceFunction<V> composeShort(final Short2BooleanFunction before) {
        return new Short2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction
            public final Object get(short s) {
                Object obj;
                obj = Boolean2ReferenceFunction.this.get(before.get(s));
                return obj;
            }
        };
    }

    default Boolean2IntFunction andThenInt(final Reference2IntFunction<V> after) {
        return new Boolean2IntFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2IntFunction
            public final int get(boolean z) {
                int i;
                i = after.getInt(Boolean2ReferenceFunction.this.get(z));
                return i;
            }
        };
    }

    default Int2ReferenceFunction<V> composeInt(final Int2BooleanFunction before) {
        return new Int2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.ints.Int2ReferenceFunction
            public final Object get(int i) {
                Object obj;
                obj = Boolean2ReferenceFunction.this.get(before.get(i));
                return obj;
            }
        };
    }

    default Boolean2LongFunction andThenLong(final Reference2LongFunction<V> after) {
        return new Boolean2LongFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2LongFunction
            public final long get(boolean z) {
                long j;
                j = after.getLong(Boolean2ReferenceFunction.this.get(z));
                return j;
            }
        };
    }

    default Long2ReferenceFunction<V> composeLong(final Long2BooleanFunction before) {
        return new Long2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.longs.Long2ReferenceFunction
            public final Object get(long j) {
                Object obj;
                obj = Boolean2ReferenceFunction.this.get(before.get(j));
                return obj;
            }
        };
    }

    default Boolean2CharFunction andThenChar(final Reference2CharFunction<V> after) {
        return new Boolean2CharFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2CharFunction
            public final char get(boolean z) {
                char c;
                c = after.getChar(Boolean2ReferenceFunction.this.get(z));
                return c;
            }
        };
    }

    default Char2ReferenceFunction<V> composeChar(final Char2BooleanFunction before) {
        return new Char2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.chars.Char2ReferenceFunction
            public final Object get(char c) {
                Object obj;
                obj = Boolean2ReferenceFunction.this.get(before.get(c));
                return obj;
            }
        };
    }

    default Boolean2FloatFunction andThenFloat(final Reference2FloatFunction<V> after) {
        return new Boolean2FloatFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction
            public final float get(boolean z) {
                float f;
                f = after.getFloat(Boolean2ReferenceFunction.this.get(z));
                return f;
            }
        };
    }

    default Float2ReferenceFunction<V> composeFloat(final Float2BooleanFunction before) {
        return new Float2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.floats.Float2ReferenceFunction
            public final Object get(float f) {
                Object obj;
                obj = Boolean2ReferenceFunction.this.get(before.get(f));
                return obj;
            }
        };
    }

    default Boolean2DoubleFunction andThenDouble(final Reference2DoubleFunction<V> after) {
        return new Boolean2DoubleFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction
            public final double get(boolean z) {
                double d;
                d = after.getDouble(Boolean2ReferenceFunction.this.get(z));
                return d;
            }
        };
    }

    default Double2ReferenceFunction<V> composeDouble(final Double2BooleanFunction before) {
        return new Double2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction
            public final Object get(double d) {
                Object obj;
                obj = Boolean2ReferenceFunction.this.get(before.get(d));
                return obj;
            }
        };
    }

    default <T> Boolean2ObjectFunction<T> andThenObject(final Reference2ObjectFunction<? super V, ? extends T> after) {
        return new Boolean2ObjectFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction
            public final Object get(boolean z) {
                Object obj;
                obj = after.get(Boolean2ReferenceFunction.this.get(z));
                return obj;
            }
        };
    }

    default <T> Object2ReferenceFunction<T, V> composeObject(final Object2BooleanFunction<? super T> before) {
        return new Object2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.Object2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Boolean2ReferenceFunction.this.get(before.getBoolean(obj));
                return obj2;
            }
        };
    }

    default <T> Boolean2ReferenceFunction<T> andThenReference(final Reference2ReferenceFunction<? super V, ? extends T> after) {
        return new Boolean2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction
            public final Object get(boolean z) {
                Object obj;
                obj = after.get(Boolean2ReferenceFunction.this.get(z));
                return obj;
            }
        };
    }

    default <T> Reference2ReferenceFunction<T, V> composeReference(final Reference2BooleanFunction<? super T> before) {
        return new Reference2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.booleans.Boolean2ReferenceFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Boolean2ReferenceFunction.this.get(before.getBoolean(obj));
                return obj2;
            }
        };
    }
}
