package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
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
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.objects.Object2CharFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import java.util.function.LongToIntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Long2CharFunction extends Function<Long, Character>, LongToIntFunction {
    char get(long j);

    @Override // java.util.function.LongToIntFunction
    default int applyAsInt(long operand) {
        return get(operand);
    }

    default char put(long key, char value) {
        throw new UnsupportedOperationException();
    }

    default char getOrDefault(long key, char defaultValue) {
        char v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default char remove(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character put(Long key, Character value) {
        long k = key.longValue();
        boolean containsKey = containsKey(k);
        char v = put(k, value.charValue());
        if (containsKey) {
            return Character.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character get(Object key) {
        if (key == null) {
            return null;
        }
        long k = ((Long) key).longValue();
        char v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Character.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        long k = ((Long) key).longValue();
        char v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Character.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character remove(Object key) {
        if (key == null) {
            return null;
        }
        long k = ((Long) key).longValue();
        if (containsKey(k)) {
            return Character.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(long key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Long) key).longValue());
    }

    default void defaultReturnValue(char rv) {
        throw new UnsupportedOperationException();
    }

    default char defaultReturnValue() {
        return (char) 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Character> compose(java.util.function.Function<? super T, ? extends Long> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Long, T> andThen(java.util.function.Function<? super Character, ? extends T> after) {
        return super.andThen(after);
    }

    default Long2ByteFunction andThenByte(final Char2ByteFunction after) {
        return new Long2ByteFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.longs.Long2ByteFunction
            public final byte get(long j) {
                byte b;
                b = after.get(Long2CharFunction.this.get(j));
                return b;
            }
        };
    }

    default Byte2CharFunction composeByte(final Byte2LongFunction before) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = Long2CharFunction.this.get(before.get(b));
                return c;
            }
        };
    }

    default Long2ShortFunction andThenShort(final Char2ShortFunction after) {
        return new Long2ShortFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.longs.Long2ShortFunction
            public final short get(long j) {
                short s;
                s = after.get(Long2CharFunction.this.get(j));
                return s;
            }
        };
    }

    default Short2CharFunction composeShort(final Short2LongFunction before) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = Long2CharFunction.this.get(before.get(s));
                return c;
            }
        };
    }

    default Long2IntFunction andThenInt(final Char2IntFunction after) {
        return new Long2IntFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.longs.Long2IntFunction
            public final int get(long j) {
                int i;
                i = after.get(Long2CharFunction.this.get(j));
                return i;
            }
        };
    }

    default Int2CharFunction composeInt(final Int2LongFunction before) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = Long2CharFunction.this.get(before.get(i));
                return c;
            }
        };
    }

    default Long2LongFunction andThenLong(final Char2LongFunction after) {
        return new Long2LongFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.longs.Long2LongFunction
            public final long get(long j) {
                long j2;
                j2 = after.get(Long2CharFunction.this.get(j));
                return j2;
            }
        };
    }

    default Long2CharFunction composeLong(final Long2LongFunction before) {
        return new Long2CharFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.longs.Long2CharFunction
            public final char get(long j) {
                char c;
                c = Long2CharFunction.this.get(before.get(j));
                return c;
            }
        };
    }

    default Long2CharFunction andThenChar(final Char2CharFunction after) {
        return new Long2CharFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.longs.Long2CharFunction
            public final char get(long j) {
                char c;
                c = after.get(Long2CharFunction.this.get(j));
                return c;
            }
        };
    }

    default Char2CharFunction composeChar(final Char2LongFunction before) {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                char c2;
                c2 = Long2CharFunction.this.get(before.get(c));
                return c2;
            }
        };
    }

    default Long2FloatFunction andThenFloat(final Char2FloatFunction after) {
        return new Long2FloatFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.longs.Long2FloatFunction
            public final float get(long j) {
                float f;
                f = after.get(Long2CharFunction.this.get(j));
                return f;
            }
        };
    }

    default Float2CharFunction composeFloat(final Float2LongFunction before) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = Long2CharFunction.this.get(before.get(f));
                return c;
            }
        };
    }

    default Long2DoubleFunction andThenDouble(final Char2DoubleFunction after) {
        return new Long2DoubleFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.longs.Long2DoubleFunction
            public final double get(long j) {
                double d;
                d = after.get(Long2CharFunction.this.get(j));
                return d;
            }
        };
    }

    default Double2CharFunction composeDouble(final Double2LongFunction before) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = Long2CharFunction.this.get(before.get(d));
                return c;
            }
        };
    }

    default <T> Long2ObjectFunction<T> andThenObject(final Char2ObjectFunction<? extends T> after) {
        return new Long2ObjectFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
            public final Object get(long j) {
                Object obj;
                obj = after.get(Long2CharFunction.this.get(j));
                return obj;
            }
        };
    }

    default <T> Object2CharFunction<T> composeObject(final Object2LongFunction<? super T> before) {
        return new Object2CharFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.objects.Object2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Long2CharFunction.this.get(before.getLong(obj));
                return c;
            }
        };
    }

    default <T> Long2ReferenceFunction<T> andThenReference(final Char2ReferenceFunction<? extends T> after) {
        return new Long2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.longs.Long2ReferenceFunction
            public final Object get(long j) {
                Object obj;
                obj = after.get(Long2CharFunction.this.get(j));
                return obj;
            }
        };
    }

    default <T> Reference2CharFunction<T> composeReference(final Reference2LongFunction<? super T> before) {
        return new Reference2CharFunction() { // from class: it.unimi.dsi.fastutil.longs.Long2CharFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.objects.Reference2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Long2CharFunction.this.get(before.getLong(obj));
                return c;
            }
        };
    }
}
