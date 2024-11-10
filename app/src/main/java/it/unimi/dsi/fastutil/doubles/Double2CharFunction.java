package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.Char2CharFunction;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.Char2LongFunction;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2CharFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Object2CharFunction;
import it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import java.util.function.DoubleToIntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Double2CharFunction extends Function<Double, Character>, DoubleToIntFunction {
    char get(double d);

    @Override // java.util.function.DoubleToIntFunction
    default int applyAsInt(double operand) {
        return get(operand);
    }

    default char put(double key, char value) {
        throw new UnsupportedOperationException();
    }

    default char getOrDefault(double key, char defaultValue) {
        char v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default char remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character put(Double key, Character value) {
        double k = key.doubleValue();
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
        double k = ((Double) key).doubleValue();
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
        double k = ((Double) key).doubleValue();
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
        double k = ((Double) key).doubleValue();
        if (containsKey(k)) {
            return Character.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(double key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Double) key).doubleValue());
    }

    default void defaultReturnValue(char rv) {
        throw new UnsupportedOperationException();
    }

    default char defaultReturnValue() {
        return (char) 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Character> compose(java.util.function.Function<? super T, ? extends Double> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Double, T> andThen(java.util.function.Function<? super Character, ? extends T> after) {
        return super.andThen(after);
    }

    default Double2ByteFunction andThenByte(final Char2ByteFunction after) {
        return new Double2ByteFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.doubles.Double2ByteFunction
            public final byte get(double d) {
                byte b;
                b = after.get(Double2CharFunction.this.get(d));
                return b;
            }
        };
    }

    default Byte2CharFunction composeByte(final Byte2DoubleFunction before) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = Double2CharFunction.this.get(before.get(b));
                return c;
            }
        };
    }

    default Double2ShortFunction andThenShort(final Char2ShortFunction after) {
        return new Double2ShortFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.doubles.Double2ShortFunction
            public final short get(double d) {
                short s;
                s = after.get(Double2CharFunction.this.get(d));
                return s;
            }
        };
    }

    default Short2CharFunction composeShort(final Short2DoubleFunction before) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = Double2CharFunction.this.get(before.get(s));
                return c;
            }
        };
    }

    default Double2IntFunction andThenInt(final Char2IntFunction after) {
        return new Double2IntFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.doubles.Double2IntFunction
            public final int get(double d) {
                int i;
                i = after.get(Double2CharFunction.this.get(d));
                return i;
            }
        };
    }

    default Int2CharFunction composeInt(final Int2DoubleFunction before) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = Double2CharFunction.this.get(before.get(i));
                return c;
            }
        };
    }

    default Double2LongFunction andThenLong(final Char2LongFunction after) {
        return new Double2LongFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.doubles.Double2LongFunction
            public final long get(double d) {
                long j;
                j = after.get(Double2CharFunction.this.get(d));
                return j;
            }
        };
    }

    default Long2CharFunction composeLong(final Long2DoubleFunction before) {
        return new Long2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.longs.Long2CharFunction
            public final char get(long j) {
                char c;
                c = Double2CharFunction.this.get(before.get(j));
                return c;
            }
        };
    }

    default Double2CharFunction andThenChar(final Char2CharFunction after) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = after.get(Double2CharFunction.this.get(d));
                return c;
            }
        };
    }

    default Char2CharFunction composeChar(final Char2DoubleFunction before) {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                char c2;
                c2 = Double2CharFunction.this.get(before.get(c));
                return c2;
            }
        };
    }

    default Double2FloatFunction andThenFloat(final Char2FloatFunction after) {
        return new Double2FloatFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.doubles.Double2FloatFunction
            public final float get(double d) {
                float f;
                f = after.get(Double2CharFunction.this.get(d));
                return f;
            }
        };
    }

    default Float2CharFunction composeFloat(final Float2DoubleFunction before) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = Double2CharFunction.this.get(before.get(f));
                return c;
            }
        };
    }

    default Double2DoubleFunction andThenDouble(final Char2DoubleFunction after) {
        return new Double2DoubleFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.doubles.Double2DoubleFunction
            public final double get(double d) {
                double d2;
                d2 = after.get(Double2CharFunction.this.get(d));
                return d2;
            }
        };
    }

    default Double2CharFunction composeDouble(final Double2DoubleFunction before) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = Double2CharFunction.this.get(before.get(d));
                return c;
            }
        };
    }

    default <T> Double2ObjectFunction<T> andThenObject(final Char2ObjectFunction<? extends T> after) {
        return new Double2ObjectFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.doubles.Double2ObjectFunction
            public final Object get(double d) {
                Object obj;
                obj = after.get(Double2CharFunction.this.get(d));
                return obj;
            }
        };
    }

    default <T> Object2CharFunction<T> composeObject(final Object2DoubleFunction<? super T> before) {
        return new Object2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.objects.Object2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Double2CharFunction.this.get(before.getDouble(obj));
                return c;
            }
        };
    }

    default <T> Double2ReferenceFunction<T> andThenReference(final Char2ReferenceFunction<? extends T> after) {
        return new Double2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction
            public final Object get(double d) {
                Object obj;
                obj = after.get(Double2CharFunction.this.get(d));
                return obj;
            }
        };
    }

    default <T> Reference2CharFunction<T> composeReference(final Reference2DoubleFunction<? super T> before) {
        return new Reference2CharFunction() { // from class: it.unimi.dsi.fastutil.doubles.Double2CharFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.Reference2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Double2CharFunction.this.get(before.getDouble(obj));
                return c;
            }
        };
    }
}
