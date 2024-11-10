package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
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
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.longs.Long2CharFunction;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.objects.Object2CharFunction;
import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import java.util.function.DoubleToIntFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Float2CharFunction extends Function<Float, Character>, DoubleToIntFunction {
    char get(float f);

    @Override // java.util.function.DoubleToIntFunction
    @Deprecated
    default int applyAsInt(double operand) {
        return get(SafeMath.safeDoubleToFloat(operand));
    }

    default char put(float key, char value) {
        throw new UnsupportedOperationException();
    }

    default char getOrDefault(float key, char defaultValue) {
        char v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default char remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Character put(Float key, Character value) {
        float k = key.floatValue();
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
        float k = ((Float) key).floatValue();
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
        float k = ((Float) key).floatValue();
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
        float k = ((Float) key).floatValue();
        if (containsKey(k)) {
            return Character.valueOf(remove(k));
        }
        return null;
    }

    default boolean containsKey(float key) {
        return true;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return containsKey(((Float) key).floatValue());
    }

    default void defaultReturnValue(char rv) {
        throw new UnsupportedOperationException();
    }

    default char defaultReturnValue() {
        return (char) 0;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Character> compose(java.util.function.Function<? super T, ? extends Float> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Float, T> andThen(java.util.function.Function<? super Character, ? extends T> after) {
        return super.andThen(after);
    }

    default Float2ByteFunction andThenByte(final Char2ByteFunction after) {
        return new Float2ByteFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.floats.Float2ByteFunction
            public final byte get(float f) {
                byte b;
                b = after.get(Float2CharFunction.this.get(f));
                return b;
            }
        };
    }

    default Byte2CharFunction composeByte(final Byte2FloatFunction before) {
        return new Byte2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.bytes.Byte2CharFunction
            public final char get(byte b) {
                char c;
                c = Float2CharFunction.this.get(before.get(b));
                return c;
            }
        };
    }

    default Float2ShortFunction andThenShort(final Char2ShortFunction after) {
        return new Float2ShortFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.floats.Float2ShortFunction
            public final short get(float f) {
                short s;
                s = after.get(Float2CharFunction.this.get(f));
                return s;
            }
        };
    }

    default Short2CharFunction composeShort(final Short2FloatFunction before) {
        return new Short2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.shorts.Short2CharFunction
            public final char get(short s) {
                char c;
                c = Float2CharFunction.this.get(before.get(s));
                return c;
            }
        };
    }

    default Float2IntFunction andThenInt(final Char2IntFunction after) {
        return new Float2IntFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.floats.Float2IntFunction
            public final int get(float f) {
                int i;
                i = after.get(Float2CharFunction.this.get(f));
                return i;
            }
        };
    }

    default Int2CharFunction composeInt(final Int2FloatFunction before) {
        return new Int2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.ints.Int2CharFunction
            public final char get(int i) {
                char c;
                c = Float2CharFunction.this.get(before.get(i));
                return c;
            }
        };
    }

    default Float2LongFunction andThenLong(final Char2LongFunction after) {
        return new Float2LongFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.floats.Float2LongFunction
            public final long get(float f) {
                long j;
                j = after.get(Float2CharFunction.this.get(f));
                return j;
            }
        };
    }

    default Long2CharFunction composeLong(final Long2FloatFunction before) {
        return new Long2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.longs.Long2CharFunction
            public final char get(long j) {
                char c;
                c = Float2CharFunction.this.get(before.get(j));
                return c;
            }
        };
    }

    default Float2CharFunction andThenChar(final Char2CharFunction after) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = after.get(Float2CharFunction.this.get(f));
                return c;
            }
        };
    }

    default Char2CharFunction composeChar(final Char2FloatFunction before) {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                char c2;
                c2 = Float2CharFunction.this.get(before.get(c));
                return c2;
            }
        };
    }

    default Float2FloatFunction andThenFloat(final Char2FloatFunction after) {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                float f2;
                f2 = after.get(Float2CharFunction.this.get(f));
                return f2;
            }
        };
    }

    default Float2CharFunction composeFloat(final Float2FloatFunction before) {
        return new Float2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.floats.Float2CharFunction
            public final char get(float f) {
                char c;
                c = Float2CharFunction.this.get(before.get(f));
                return c;
            }
        };
    }

    default Float2DoubleFunction andThenDouble(final Char2DoubleFunction after) {
        return new Float2DoubleFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.floats.Float2DoubleFunction
            public final double get(float f) {
                double d;
                d = after.get(Float2CharFunction.this.get(f));
                return d;
            }
        };
    }

    default Double2CharFunction composeDouble(final Double2FloatFunction before) {
        return new Double2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.doubles.Double2CharFunction
            public final char get(double d) {
                char c;
                c = Float2CharFunction.this.get(before.get(d));
                return c;
            }
        };
    }

    default <T> Float2ObjectFunction<T> andThenObject(final Char2ObjectFunction<? extends T> after) {
        return new Float2ObjectFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.floats.Float2ObjectFunction
            public final Object get(float f) {
                Object obj;
                obj = after.get(Float2CharFunction.this.get(f));
                return obj;
            }
        };
    }

    default <T> Object2CharFunction<T> composeObject(final Object2FloatFunction<? super T> before) {
        return new Object2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.objects.Object2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Float2CharFunction.this.get(before.getFloat(obj));
                return c;
            }
        };
    }

    default <T> Float2ReferenceFunction<T> andThenReference(final Char2ReferenceFunction<? extends T> after) {
        return new Float2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.floats.Float2ReferenceFunction
            public final Object get(float f) {
                Object obj;
                obj = after.get(Float2CharFunction.this.get(f));
                return obj;
            }
        };
    }

    default <T> Reference2CharFunction<T> composeReference(final Reference2FloatFunction<? super T> before) {
        return new Reference2CharFunction() { // from class: it.unimi.dsi.fastutil.floats.Float2CharFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.objects.Reference2CharFunction
            public final char getChar(Object obj) {
                char c;
                c = Float2CharFunction.this.get(before.getFloat(obj));
                return c;
            }
        };
    }
}
