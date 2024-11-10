package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import it.unimi.dsi.fastutil.doubles.Double2ObjectFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2CharFunction;
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
import it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import it.unimi.dsi.fastutil.objects.Reference2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import java.util.function.IntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Char2ObjectFunction<V> extends Function<Character, V>, IntFunction<V> {
    V get(char c);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Object put(Character ch, Object obj) {
        return put2(ch, (Character) obj);
    }

    @Override // java.util.function.IntFunction
    @Deprecated
    default V apply(int operand) {
        return get(SafeMath.safeIntToChar(operand));
    }

    default V put(char key, V value) {
        throw new UnsupportedOperationException();
    }

    default V getOrDefault(char key, V defaultValue) {
        V v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default V remove(char key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* renamed from: put, reason: avoid collision after fix types in other method */
    default V put2(Character key, V value) {
        char k = key.charValue();
        boolean containsKey = containsKey(k);
        V v = put(k, (char) value);
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
        char k = ((Character) key).charValue();
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
        char k = ((Character) key).charValue();
        V v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? v : defaultValue;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default V remove(Object key) {
        if (key == null) {
            return null;
        }
        char k = ((Character) key).charValue();
        if (containsKey(k)) {
            return remove(k);
        }
        return null;
    }

    default boolean containsKey(char key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Character) key).charValue());
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
    default <T> java.util.function.Function<T, V> compose(java.util.function.Function<? super T, ? extends Character> function) {
        return super.compose(function);
    }

    default Char2ByteFunction andThenByte(final Object2ByteFunction<V> after) {
        return new Char2ByteFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.chars.Char2ByteFunction
            public final byte get(char c) {
                byte b;
                b = after.getByte(Char2ObjectFunction.this.get(c));
                return b;
            }
        };
    }

    default Byte2ObjectFunction<V> composeByte(final Byte2CharFunction before) {
        return new Byte2ObjectFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction
            public final Object get(byte b) {
                Object obj;
                obj = Char2ObjectFunction.this.get(before.get(b));
                return obj;
            }
        };
    }

    default Char2ShortFunction andThenShort(final Object2ShortFunction<V> after) {
        return new Char2ShortFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.chars.Char2ShortFunction
            public final short get(char c) {
                short s;
                s = after.getShort(Char2ObjectFunction.this.get(c));
                return s;
            }
        };
    }

    default Short2ObjectFunction<V> composeShort(final Short2CharFunction before) {
        return new Short2ObjectFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.shorts.Short2ObjectFunction
            public final Object get(short s) {
                Object obj;
                obj = Char2ObjectFunction.this.get(before.get(s));
                return obj;
            }
        };
    }

    default Char2IntFunction andThenInt(final Object2IntFunction<V> after) {
        return new Char2IntFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.chars.Char2IntFunction
            public final int get(char c) {
                int i;
                i = after.getInt(Char2ObjectFunction.this.get(c));
                return i;
            }
        };
    }

    default Int2ObjectFunction<V> composeInt(final Int2CharFunction before) {
        return new Int2ObjectFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
            public final Object get(int i) {
                Object obj;
                obj = Char2ObjectFunction.this.get(before.get(i));
                return obj;
            }
        };
    }

    default Char2LongFunction andThenLong(final Object2LongFunction<V> after) {
        return new Char2LongFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.chars.Char2LongFunction
            public final long get(char c) {
                long j;
                j = after.getLong(Char2ObjectFunction.this.get(c));
                return j;
            }
        };
    }

    default Long2ObjectFunction<V> composeLong(final Long2CharFunction before) {
        return new Long2ObjectFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
            public final Object get(long j) {
                Object obj;
                obj = Char2ObjectFunction.this.get(before.get(j));
                return obj;
            }
        };
    }

    default Char2CharFunction andThenChar(final Object2CharFunction<V> after) {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                char c2;
                c2 = after.getChar(Char2ObjectFunction.this.get(c));
                return c2;
            }
        };
    }

    default Char2ObjectFunction<V> composeChar(final Char2CharFunction before) {
        return new Char2ObjectFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.chars.Char2ObjectFunction
            public final Object get(char c) {
                Object obj;
                obj = Char2ObjectFunction.this.get(before.get(c));
                return obj;
            }
        };
    }

    default Char2FloatFunction andThenFloat(final Object2FloatFunction<V> after) {
        return new Char2FloatFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.chars.Char2FloatFunction
            public final float get(char c) {
                float f;
                f = after.getFloat(Char2ObjectFunction.this.get(c));
                return f;
            }
        };
    }

    default Float2ObjectFunction<V> composeFloat(final Float2CharFunction before) {
        return new Float2ObjectFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.floats.Float2ObjectFunction
            public final Object get(float f) {
                Object obj;
                obj = Char2ObjectFunction.this.get(before.get(f));
                return obj;
            }
        };
    }

    default Char2DoubleFunction andThenDouble(final Object2DoubleFunction<V> after) {
        return new Char2DoubleFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.chars.Char2DoubleFunction
            public final double get(char c) {
                double d;
                d = after.getDouble(Char2ObjectFunction.this.get(c));
                return d;
            }
        };
    }

    default Double2ObjectFunction<V> composeDouble(final Double2CharFunction before) {
        return new Double2ObjectFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.doubles.Double2ObjectFunction
            public final Object get(double d) {
                Object obj;
                obj = Char2ObjectFunction.this.get(before.get(d));
                return obj;
            }
        };
    }

    default <T> Char2ObjectFunction<T> andThenObject(final Object2ObjectFunction<? super V, ? extends T> after) {
        return new Char2ObjectFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.chars.Char2ObjectFunction
            public final Object get(char c) {
                Object obj;
                obj = after.get(Char2ObjectFunction.this.get(c));
                return obj;
            }
        };
    }

    default <T> Object2ObjectFunction<T, V> composeObject(final Object2CharFunction<? super T> before) {
        return new Object2ObjectFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.objects.Object2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Char2ObjectFunction.this.get(before.getChar(obj));
                return obj2;
            }
        };
    }

    default <T> Char2ReferenceFunction<T> andThenReference(final Object2ReferenceFunction<? super V, ? extends T> after) {
        return new Char2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.chars.Char2ReferenceFunction
            public final Object get(char c) {
                Object obj;
                obj = after.get(Char2ObjectFunction.this.get(c));
                return obj;
            }
        };
    }

    default <T> Reference2ObjectFunction<T, V> composeReference(final Reference2CharFunction<? super T> before) {
        return new Reference2ObjectFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2ObjectFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Reference2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = Char2ObjectFunction.this.get(before.getChar(obj));
                return obj2;
            }
        };
    }
}
