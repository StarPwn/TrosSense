package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.Char2CharFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import it.unimi.dsi.fastutil.doubles.Double2ObjectFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2CharFunction;
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import java.util.function.ToIntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Object2CharFunction<K> extends Function<K, Character>, ToIntFunction<K> {
    char getChar(Object obj);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    /* bridge */ /* synthetic */ default Character put(Object obj, Character ch) {
        return put2((Object2CharFunction<K>) obj, ch);
    }

    @Override // java.util.function.ToIntFunction
    default int applyAsInt(K operand) {
        return getChar(operand);
    }

    default char put(K key, char value) {
        throw new UnsupportedOperationException();
    }

    default char getOrDefault(Object key, char defaultValue) {
        char v = getChar(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default char removeChar(Object key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* renamed from: put, reason: avoid collision after fix types in other method */
    default Character put2(K key, Character value) {
        boolean containsKey = containsKey(key);
        char v = put((Object2CharFunction<K>) key, value.charValue());
        if (containsKey) {
            return Character.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character get(Object key) {
        char v = getChar(key);
        if (v != defaultReturnValue() || containsKey(key)) {
            return Character.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        char v = getChar(key);
        return (v != defaultReturnValue() || containsKey(key)) ? Character.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character remove(Object key) {
        if (containsKey(key)) {
            return Character.valueOf(removeChar(key));
        }
        return null;
    }

    default void defaultReturnValue(char rv) {
        throw new UnsupportedOperationException();
    }

    default char defaultReturnValue() {
        return (char) 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super Character, ? extends T> function) {
        return super.andThen(function);
    }

    default Object2ByteFunction<K> andThenByte(final Char2ByteFunction after) {
        return new Object2ByteFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.objects.Object2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = after.get(Object2CharFunction.this.getChar(obj));
                return b;
            }
        };
    }

    default Byte2CharFunction composeByte(final Byte2ObjectFunction<K> before) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = Object2CharFunction.this.getChar(before.get(b));
                return c;
            }
        };
    }

    default Object2ShortFunction<K> andThenShort(final Char2ShortFunction after) {
        return new Object2ShortFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.objects.Object2ShortFunction
            public final short getShort(Object obj) {
                short s;
                s = after.get(Object2CharFunction.this.getChar(obj));
                return s;
            }
        };
    }

    default Short2CharFunction composeShort(final Short2ObjectFunction<K> before) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = Object2CharFunction.this.getChar(before.get(s));
                return c;
            }
        };
    }

    default Object2IntFunction<K> andThenInt(final Char2IntFunction after) {
        return new Object2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
            public final int getInt(Object obj) {
                int i;
                i = after.get(Object2CharFunction.this.getChar(obj));
                return i;
            }
        };
    }

    default Int2CharFunction composeInt(final Int2ObjectFunction<K> before) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = Object2CharFunction.this.getChar(before.get(i));
                return c;
            }
        };
    }

    default Object2LongFunction<K> andThenLong(final Char2LongFunction after) {
        return new Object2LongFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.objects.Object2LongFunction
            public final long getLong(Object obj) {
                long j;
                j = after.get(Object2CharFunction.this.getChar(obj));
                return j;
            }
        };
    }

    default Long2CharFunction composeLong(final Long2ObjectFunction<K> before) {
        return new Long2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.longs.Long2CharFunction
            public final char get(long j) {
                char c;
                c = Object2CharFunction.this.getChar(before.get(j));
                return c;
            }
        };
    }

    default Object2CharFunction<K> andThenChar(final Char2CharFunction after) {
        return new Object2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.objects.Object2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = after.get(Object2CharFunction.this.getChar(obj));
                return c;
            }
        };
    }

    default Char2CharFunction composeChar(final Char2ObjectFunction<K> before) {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                char c2;
                c2 = Object2CharFunction.this.getChar(before.get(c));
                return c2;
            }
        };
    }

    default Object2FloatFunction<K> andThenFloat(final Char2FloatFunction after) {
        return new Object2FloatFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.objects.Object2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = after.get(Object2CharFunction.this.getChar(obj));
                return f;
            }
        };
    }

    default Float2CharFunction composeFloat(final Float2ObjectFunction<K> before) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = Object2CharFunction.this.getChar(before.get(f));
                return c;
            }
        };
    }

    default Object2DoubleFunction<K> andThenDouble(final Char2DoubleFunction after) {
        return new Object2DoubleFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.objects.Object2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = after.get(Object2CharFunction.this.getChar(obj));
                return d;
            }
        };
    }

    default Double2CharFunction composeDouble(final Double2ObjectFunction<K> before) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = Object2CharFunction.this.getChar(before.get(d));
                return c;
            }
        };
    }

    default <T> Object2ObjectFunction<K, T> andThenObject(final Char2ObjectFunction<? extends T> after) {
        return new Object2ObjectFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.objects.Object2ObjectFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Object2CharFunction.this.getChar(obj));
                return obj2;
            }
        };
    }

    default <T> Object2CharFunction<T> composeObject(final Object2ObjectFunction<? super T, ? extends K> before) {
        return new Object2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.objects.Object2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Object2CharFunction.this.getChar(before.get(obj));
                return c;
            }
        };
    }

    default <T> Object2ReferenceFunction<K, T> andThenReference(final Char2ReferenceFunction<? extends T> after) {
        return new Object2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.objects.Object2ReferenceFunction, it.unimi.dsi.fastutil.Function
            public final Object get(Object obj) {
                Object obj2;
                obj2 = after.get(Object2CharFunction.this.getChar(obj));
                return obj2;
            }
        };
    }

    default <T> Reference2CharFunction<T> composeReference(final Reference2ObjectFunction<? super T, ? extends K> before) {
        return new Reference2CharFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2CharFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.objects.Reference2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Object2CharFunction.this.getChar(before.get(obj));
                return c;
            }
        };
    }
}
