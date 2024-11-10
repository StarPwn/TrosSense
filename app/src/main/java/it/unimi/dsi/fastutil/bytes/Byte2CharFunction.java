package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.Char2CharFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import it.unimi.dsi.fastutil.longs.Long2CharFunction;
import it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import it.unimi.dsi.fastutil.objects.Object2CharFunction;
import it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Byte2CharFunction extends Function<Byte, Character>, IntUnaryOperator {
    char get(byte b);

    @Override // java.util.function.IntUnaryOperator
    @Deprecated
    default int applyAsInt(int operand) {
        return get(SafeMath.safeIntToByte(operand));
    }

    default char put(byte key, char value) {
        throw new UnsupportedOperationException();
    }

    default char getOrDefault(byte key, char defaultValue) {
        char v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default char remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character put(Byte key, Character value) {
        byte k = key.byteValue();
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
        byte k = ((Byte) key).byteValue();
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
        byte k = ((Byte) key).byteValue();
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
        byte k = ((Byte) key).byteValue();
        if (containsKey(k)) {
            return Character.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(byte key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Byte) key).byteValue());
    }

    default void defaultReturnValue(char rv) {
        throw new UnsupportedOperationException();
    }

    default char defaultReturnValue() {
        return (char) 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Character> compose(java.util.function.Function<? super T, ? extends Byte> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Byte, T> andThen(java.util.function.Function<? super Character, ? extends T> after) {
        return super.andThen(after);
    }

    default Byte2ByteFunction andThenByte(final Char2ByteFunction after) {
        return new Byte2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ByteFunction
            public final byte get(byte b) {
                byte b2;
                b2 = after.get(Byte2CharFunction.this.get(b));
                return b2;
            }
        };
    }

    default Byte2CharFunction composeByte(final Byte2ByteFunction before) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = Byte2CharFunction.this.get(before.get(b));
                return c;
            }
        };
    }

    default Byte2ShortFunction andThenShort(final Char2ShortFunction after) {
        return new Byte2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ShortFunction
            public final short get(byte b) {
                short s;
                s = after.get(Byte2CharFunction.this.get(b));
                return s;
            }
        };
    }

    default Short2CharFunction composeShort(final Short2ByteFunction before) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = Byte2CharFunction.this.get(before.get(s));
                return c;
            }
        };
    }

    default Byte2IntFunction andThenInt(final Char2IntFunction after) {
        return new Byte2IntFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.bytes.Byte2IntFunction
            public final int get(byte b) {
                int i;
                i = after.get(Byte2CharFunction.this.get(b));
                return i;
            }
        };
    }

    default Int2CharFunction composeInt(final Int2ByteFunction before) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = Byte2CharFunction.this.get(before.get(i));
                return c;
            }
        };
    }

    default Byte2LongFunction andThenLong(final Char2LongFunction after) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = after.get(Byte2CharFunction.this.get(b));
                return j;
            }
        };
    }

    default Long2CharFunction composeLong(final Long2ByteFunction before) {
        return new Long2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.longs.Long2CharFunction
            public final char get(long j) {
                char c;
                c = Byte2CharFunction.this.get(before.get(j));
                return c;
            }
        };
    }

    default Byte2CharFunction andThenChar(final Char2CharFunction after) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = after.get(Byte2CharFunction.this.get(b));
                return c;
            }
        };
    }

    default Char2CharFunction composeChar(final Char2ByteFunction before) {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                char c2;
                c2 = Byte2CharFunction.this.get(before.get(c));
                return c2;
            }
        };
    }

    default Byte2FloatFunction andThenFloat(final Char2FloatFunction after) {
        return new Byte2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.bytes.Byte2FloatFunction
            public final float get(byte b) {
                float f;
                f = after.get(Byte2CharFunction.this.get(b));
                return f;
            }
        };
    }

    default Float2CharFunction composeFloat(final Float2ByteFunction before) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = Byte2CharFunction.this.get(before.get(f));
                return c;
            }
        };
    }

    default Byte2DoubleFunction andThenDouble(final Char2DoubleFunction after) {
        return new Byte2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction
            public final double get(byte b) {
                double d;
                d = after.get(Byte2CharFunction.this.get(b));
                return d;
            }
        };
    }

    default Double2CharFunction composeDouble(final Double2ByteFunction before) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = Byte2CharFunction.this.get(before.get(d));
                return c;
            }
        };
    }

    default <T> Byte2ObjectFunction<T> andThenObject(final Char2ObjectFunction<? extends T> after) {
        return new Byte2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction
            public final Object get(byte b) {
                Object obj;
                obj = after.get(Byte2CharFunction.this.get(b));
                return obj;
            }
        };
    }

    default <T> Object2CharFunction<T> composeObject(final Object2ByteFunction<? super T> before) {
        return new Object2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.objects.Object2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Byte2CharFunction.this.get(before.getByte(obj));
                return c;
            }
        };
    }

    default <T> Byte2ReferenceFunction<T> andThenReference(final Char2ReferenceFunction<? extends T> after) {
        return new Byte2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction
            public final Object get(byte b) {
                Object obj;
                obj = after.get(Byte2CharFunction.this.get(b));
                return obj;
            }
        };
    }

    default <T> Reference2CharFunction<T> composeReference(final Reference2ByteFunction<? super T> before) {
        return new Reference2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2CharFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.objects.Reference2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Byte2CharFunction.this.get(before.getByte(obj));
                return c;
            }
        };
    }
}
