package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Byte2ByteFunction extends Function<Byte, Byte>, IntUnaryOperator {
    byte get(byte b);

    @Override // java.util.function.IntUnaryOperator
    @Deprecated
    default int applyAsInt(int operand) {
        return get(SafeMath.safeIntToByte(operand));
    }

    default byte put(byte key, byte value) {
        throw new UnsupportedOperationException();
    }

    default byte getOrDefault(byte key, byte defaultValue) {
        byte v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default byte remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte put(Byte key, Byte value) {
        byte k = key.byteValue();
        boolean containsKey = containsKey(k);
        byte v = put(k, value.byteValue());
        if (containsKey) {
            return Byte.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte get(Object key) {
        if (key == null) {
            return null;
        }
        byte k = ((Byte) key).byteValue();
        byte v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Byte.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        byte k = ((Byte) key).byteValue();
        byte v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Byte.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Byte remove(Object key) {
        if (key == null) {
            return null;
        }
        byte k = ((Byte) key).byteValue();
        if (containsKey(k)) {
            return Byte.valueOf(remove(k));
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

    default void defaultReturnValue(byte rv) {
        throw new UnsupportedOperationException();
    }

    default byte defaultReturnValue() {
        return (byte) 0;
    }

    static Byte2ByteFunction identity() {
        return new Byte2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ByteFunction
            public final byte get(byte b) {
                return Byte2ByteFunction.lambda$identity$0(b);
            }
        };
    }

    static /* synthetic */ byte lambda$identity$0(byte k) {
        return k;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Byte> compose(java.util.function.Function<? super T, ? extends Byte> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Byte, T> andThen(java.util.function.Function<? super Byte, ? extends T> after) {
        return super.andThen(after);
    }

    default Byte2ByteFunction andThenByte(final Byte2ByteFunction after) {
        return new Byte2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda18
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ByteFunction
            public final byte get(byte b) {
                byte b2;
                b2 = after.get(Byte2ByteFunction.this.get(b));
                return b2;
            }
        };
    }

    default Byte2ByteFunction composeByte(final Byte2ByteFunction before) {
        return new Byte2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ByteFunction
            public final byte get(byte b) {
                byte b2;
                b2 = Byte2ByteFunction.this.get(before.get(b));
                return b2;
            }
        };
    }

    default Byte2ShortFunction andThenShort(final Byte2ShortFunction after) {
        return new Byte2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ShortFunction
            public final short get(byte b) {
                short s;
                s = after.get(Byte2ByteFunction.this.get(b));
                return s;
            }
        };
    }

    default Short2ByteFunction composeShort(final Short2ByteFunction before) {
        return new Short2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.shorts.Short2ByteFunction
            public final byte get(short s) {
                byte b;
                b = Byte2ByteFunction.this.get(before.get(s));
                return b;
            }
        };
    }

    default Byte2IntFunction andThenInt(final Byte2IntFunction after) {
        return new Byte2IntFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.bytes.Byte2IntFunction
            public final int get(byte b) {
                int i;
                i = after.get(Byte2ByteFunction.this.get(b));
                return i;
            }
        };
    }

    default Int2ByteFunction composeInt(final Int2ByteFunction before) {
        return new Int2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.ints.Int2ByteFunction
            public final byte get(int i) {
                byte b;
                b = Byte2ByteFunction.this.get(before.get(i));
                return b;
            }
        };
    }

    default Byte2LongFunction andThenLong(final Byte2LongFunction after) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = after.get(Byte2ByteFunction.this.get(b));
                return j;
            }
        };
    }

    default Long2ByteFunction composeLong(final Long2ByteFunction before) {
        return new Long2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.longs.Long2ByteFunction
            public final byte get(long j) {
                byte b;
                b = Byte2ByteFunction.this.get(before.get(j));
                return b;
            }
        };
    }

    default Byte2CharFunction andThenChar(final Byte2CharFunction after) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = after.get(Byte2ByteFunction.this.get(b));
                return c;
            }
        };
    }

    default Char2ByteFunction composeChar(final Char2ByteFunction before) {
        return new Char2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.chars.Char2ByteFunction
            public final byte get(char c) {
                byte b;
                b = Byte2ByteFunction.this.get(before.get(c));
                return b;
            }
        };
    }

    default Byte2FloatFunction andThenFloat(final Byte2FloatFunction after) {
        return new Byte2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.bytes.Byte2FloatFunction
            public final float get(byte b) {
                float f;
                f = after.get(Byte2ByteFunction.this.get(b));
                return f;
            }
        };
    }

    default Float2ByteFunction composeFloat(final Float2ByteFunction before) {
        return new Float2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.floats.Float2ByteFunction
            public final byte get(float f) {
                byte b;
                b = Byte2ByteFunction.this.get(before.get(f));
                return b;
            }
        };
    }

    default Byte2DoubleFunction andThenDouble(final Byte2DoubleFunction after) {
        return new Byte2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction
            public final double get(byte b) {
                double d;
                d = after.get(Byte2ByteFunction.this.get(b));
                return d;
            }
        };
    }

    default Double2ByteFunction composeDouble(final Double2ByteFunction before) {
        return new Double2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.doubles.Double2ByteFunction
            public final byte get(double d) {
                byte b;
                b = Byte2ByteFunction.this.get(before.get(d));
                return b;
            }
        };
    }

    default <T> Byte2ObjectFunction<T> andThenObject(final Byte2ObjectFunction<? extends T> after) {
        return new Byte2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction
            public final Object get(byte b) {
                Object obj;
                obj = after.get(Byte2ByteFunction.this.get(b));
                return obj;
            }
        };
    }

    default <T> Object2ByteFunction<T> composeObject(final Object2ByteFunction<? super T> before) {
        return new Object2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Object2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = Byte2ByteFunction.this.get(before.getByte(obj));
                return b;
            }
        };
    }

    default <T> Byte2ReferenceFunction<T> andThenReference(final Byte2ReferenceFunction<? extends T> after) {
        return new Byte2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction
            public final Object get(byte b) {
                Object obj;
                obj = after.get(Byte2ByteFunction.this.get(b));
                return obj;
            }
        };
    }

    default <T> Reference2ByteFunction<T> composeReference(final Reference2ByteFunction<? super T> before) {
        return new Reference2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2ByteFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.objects.Reference2ByteFunction
            public final byte getByte(Object obj) {
                byte b;
                b = Byte2ByteFunction.this.get(before.getByte(obj));
                return b;
            }
        };
    }
}
