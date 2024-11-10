package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import it.unimi.dsi.fastutil.doubles.Double2ObjectFunction;
import it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import java.util.function.IntToDoubleFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Byte2DoubleFunction extends Function<Byte, Double>, IntToDoubleFunction {
    double get(byte b);

    @Override // java.util.function.IntToDoubleFunction
    @Deprecated
    default double applyAsDouble(int operand) {
        return get(SafeMath.safeIntToByte(operand));
    }

    default double put(byte key, double value) {
        throw new UnsupportedOperationException();
    }

    default double getOrDefault(byte key, double defaultValue) {
        double v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default double remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double put(Byte key, Double value) {
        byte k = key.byteValue();
        boolean containsKey = containsKey(k);
        double v = put(k, value.doubleValue());
        if (containsKey) {
            return Double.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double get(Object key) {
        if (key == null) {
            return null;
        }
        byte k = ((Byte) key).byteValue();
        double v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Double.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        byte k = ((Byte) key).byteValue();
        double v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Double.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Double remove(Object key) {
        if (key == null) {
            return null;
        }
        byte k = ((Byte) key).byteValue();
        if (containsKey(k)) {
            return Double.valueOf(remove(k));
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

    default void defaultReturnValue(double rv) {
        throw new UnsupportedOperationException();
    }

    default double defaultReturnValue() {
        return 0.0d;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Double> compose(java.util.function.Function<? super T, ? extends Byte> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Byte, T> andThen(java.util.function.Function<? super Double, ? extends T> after) {
        return super.andThen(after);
    }

    default Byte2ByteFunction andThenByte(final Double2ByteFunction after) {
        return new Byte2ByteFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ByteFunction
            public final byte get(byte b) {
                byte b2;
                b2 = after.get(Byte2DoubleFunction.this.get(b));
                return b2;
            }
        };
    }

    default Byte2DoubleFunction composeByte(final Byte2ByteFunction before) {
        return new Byte2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction
            public final double get(byte b) {
                double d;
                d = Byte2DoubleFunction.this.get(before.get(b));
                return d;
            }
        };
    }

    default Byte2ShortFunction andThenShort(final Double2ShortFunction after) {
        return new Byte2ShortFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ShortFunction
            public final short get(byte b) {
                short s;
                s = after.get(Byte2DoubleFunction.this.get(b));
                return s;
            }
        };
    }

    default Short2DoubleFunction composeShort(final Short2ByteFunction before) {
        return new Short2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.shorts.Short2DoubleFunction
            public final double get(short s) {
                double d;
                d = Byte2DoubleFunction.this.get(before.get(s));
                return d;
            }
        };
    }

    default Byte2IntFunction andThenInt(final Double2IntFunction after) {
        return new Byte2IntFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.bytes.Byte2IntFunction
            public final int get(byte b) {
                int i;
                i = after.get(Byte2DoubleFunction.this.get(b));
                return i;
            }
        };
    }

    default Int2DoubleFunction composeInt(final Int2ByteFunction before) {
        return new Int2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.ints.Int2DoubleFunction
            public final double get(int i) {
                double d;
                d = Byte2DoubleFunction.this.get(before.get(i));
                return d;
            }
        };
    }

    default Byte2LongFunction andThenLong(final Double2LongFunction after) {
        return new Byte2LongFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.bytes.Byte2LongFunction
            public final long get(byte b) {
                long j;
                j = after.get(Byte2DoubleFunction.this.get(b));
                return j;
            }
        };
    }

    default Long2DoubleFunction composeLong(final Long2ByteFunction before) {
        return new Long2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.longs.Long2DoubleFunction
            public final double get(long j) {
                double d;
                d = Byte2DoubleFunction.this.get(before.get(j));
                return d;
            }
        };
    }

    default Byte2CharFunction andThenChar(final Double2CharFunction after) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = after.get(Byte2DoubleFunction.this.get(b));
                return c;
            }
        };
    }

    default Char2DoubleFunction composeChar(final Char2ByteFunction before) {
        return new Char2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.chars.Char2DoubleFunction
            public final double get(char c) {
                double d;
                d = Byte2DoubleFunction.this.get(before.get(c));
                return d;
            }
        };
    }

    default Byte2FloatFunction andThenFloat(final Double2FloatFunction after) {
        return new Byte2FloatFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.bytes.Byte2FloatFunction
            public final float get(byte b) {
                float f;
                f = after.get(Byte2DoubleFunction.this.get(b));
                return f;
            }
        };
    }

    default Float2DoubleFunction composeFloat(final Float2ByteFunction before) {
        return new Float2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.floats.Float2DoubleFunction
            public final double get(float f) {
                double d;
                d = Byte2DoubleFunction.this.get(before.get(f));
                return d;
            }
        };
    }

    default Byte2DoubleFunction andThenDouble(final Double2DoubleFunction after) {
        return new Byte2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction
            public final double get(byte b) {
                double d;
                d = after.get(Byte2DoubleFunction.this.get(b));
                return d;
            }
        };
    }

    default Double2DoubleFunction composeDouble(final Double2ByteFunction before) {
        return new Double2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.doubles.Double2DoubleFunction
            public final double get(double d) {
                double d2;
                d2 = Byte2DoubleFunction.this.get(before.get(d));
                return d2;
            }
        };
    }

    default <T> Byte2ObjectFunction<T> andThenObject(final Double2ObjectFunction<? extends T> after) {
        return new Byte2ObjectFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction
            public final Object get(byte b) {
                Object obj;
                obj = after.get(Byte2DoubleFunction.this.get(b));
                return obj;
            }
        };
    }

    default <T> Object2DoubleFunction<T> composeObject(final Object2ByteFunction<? super T> before) {
        return new Object2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.objects.Object2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = Byte2DoubleFunction.this.get(before.getByte(obj));
                return d;
            }
        };
    }

    default <T> Byte2ReferenceFunction<T> andThenReference(final Double2ReferenceFunction<? extends T> after) {
        return new Byte2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction
            public final Object get(byte b) {
                Object obj;
                obj = after.get(Byte2DoubleFunction.this.get(b));
                return obj;
            }
        };
    }

    default <T> Reference2DoubleFunction<T> composeReference(final Reference2ByteFunction<? super T> before) {
        return new Reference2DoubleFunction() { // from class: it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Reference2DoubleFunction
            public final double getDouble(Object obj) {
                double d;
                d = Byte2DoubleFunction.this.get(before.getByte(obj));
                return d;
            }
        };
    }
}
