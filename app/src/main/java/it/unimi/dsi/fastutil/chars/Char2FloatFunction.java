package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import it.unimi.dsi.fastutil.floats.Float2CharFunction;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.floats.Float2LongFunction;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import it.unimi.dsi.fastutil.floats.Float2ShortFunction;
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
import java.util.function.IntToDoubleFunction;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface Char2FloatFunction extends Function<Character, Float>, IntToDoubleFunction {
    float get(char c);

    @Override // java.util.function.IntToDoubleFunction
    @Deprecated
    default double applyAsDouble(int operand) {
        return get(SafeMath.safeIntToChar(operand));
    }

    default float put(char key, float value) {
        throw new UnsupportedOperationException();
    }

    default float getOrDefault(char key, float defaultValue) {
        float v = get(key);
        return (v != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
    }

    default float remove(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float put(Character key, Float value) {
        char k = key.charValue();
        boolean containsKey = containsKey(k);
        float v = put(k, value.floatValue());
        if (containsKey) {
            return Float.valueOf(v);
        }
        return null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float get(Object key) {
        if (key == null) {
            return null;
        }
        char k = ((Character) key).charValue();
        float v = get(k);
        if (v != defaultReturnValue() || containsKey(k)) {
            return Float.valueOf(v);
        }
        return null;
    }

    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        char k = ((Character) key).charValue();
        float v = get(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Float.valueOf(v) : defaultValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Function
    @Deprecated
    default Float remove(Object key) {
        if (key == null) {
            return null;
        }
        char k = ((Character) key).charValue();
        if (containsKey(k)) {
            return Float.valueOf(remove(k));
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

    default void defaultReturnValue(float rv) {
        throw new UnsupportedOperationException();
    }

    default float defaultReturnValue() {
        return 0.0f;
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<T, Float> compose(java.util.function.Function<? super T, ? extends Character> before) {
        return super.compose(before);
    }

    @Override // java.util.function.Function
    @Deprecated
    default <T> java.util.function.Function<Character, T> andThen(java.util.function.Function<? super Float, ? extends T> after) {
        return super.andThen(after);
    }

    default Char2ByteFunction andThenByte(final Float2ByteFunction after) {
        return new Char2ByteFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda14
            @Override // it.unimi.dsi.fastutil.chars.Char2ByteFunction
            public final byte get(char c) {
                byte b;
                b = after.get(Char2FloatFunction.this.get(c));
                return b;
            }
        };
    }

    default Byte2FloatFunction composeByte(final Byte2CharFunction before) {
        return new Byte2FloatFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda5
            @Override // it.unimi.dsi.fastutil.bytes.Byte2FloatFunction
            public final float get(byte b) {
                float f;
                f = Char2FloatFunction.this.get(before.get(b));
                return f;
            }
        };
    }

    default Char2ShortFunction andThenShort(final Float2ShortFunction after) {
        return new Char2ShortFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda12
            @Override // it.unimi.dsi.fastutil.chars.Char2ShortFunction
            public final short get(char c) {
                short s;
                s = after.get(Char2FloatFunction.this.get(c));
                return s;
            }
        };
    }

    default Short2FloatFunction composeShort(final Short2CharFunction before) {
        return new Short2FloatFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda17
            @Override // it.unimi.dsi.fastutil.shorts.Short2FloatFunction
            public final float get(short s) {
                float f;
                f = Char2FloatFunction.this.get(before.get(s));
                return f;
            }
        };
    }

    default Char2IntFunction andThenInt(final Float2IntFunction after) {
        return new Char2IntFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda7
            @Override // it.unimi.dsi.fastutil.chars.Char2IntFunction
            public final int get(char c) {
                int i;
                i = after.get(Char2FloatFunction.this.get(c));
                return i;
            }
        };
    }

    default Int2FloatFunction composeInt(final Int2CharFunction before) {
        return new Int2FloatFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.ints.Int2FloatFunction
            public final float get(int i) {
                float f;
                f = Char2FloatFunction.this.get(before.get(i));
                return f;
            }
        };
    }

    default Char2LongFunction andThenLong(final Float2LongFunction after) {
        return new Char2LongFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda11
            @Override // it.unimi.dsi.fastutil.chars.Char2LongFunction
            public final long get(char c) {
                long j;
                j = after.get(Char2FloatFunction.this.get(c));
                return j;
            }
        };
    }

    default Long2FloatFunction composeLong(final Long2CharFunction before) {
        return new Long2FloatFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda16
            @Override // it.unimi.dsi.fastutil.longs.Long2FloatFunction
            public final float get(long j) {
                float f;
                f = Char2FloatFunction.this.get(before.get(j));
                return f;
            }
        };
    }

    default Char2CharFunction andThenChar(final Float2CharFunction after) {
        return new Char2CharFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda9
            @Override // it.unimi.dsi.fastutil.chars.Char2CharFunction
            public final char get(char c) {
                char c2;
                c2 = after.get(Char2FloatFunction.this.get(c));
                return c2;
            }
        };
    }

    default Char2FloatFunction composeChar(final Char2CharFunction before) {
        return new Char2FloatFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda10
            @Override // it.unimi.dsi.fastutil.chars.Char2FloatFunction
            public final float get(char c) {
                float f;
                f = Char2FloatFunction.this.get(before.get(c));
                return f;
            }
        };
    }

    default Char2FloatFunction andThenFloat(final Float2FloatFunction after) {
        return new Char2FloatFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda15
            @Override // it.unimi.dsi.fastutil.chars.Char2FloatFunction
            public final float get(char c) {
                float f;
                f = after.get(Char2FloatFunction.this.get(c));
                return f;
            }
        };
    }

    default Float2FloatFunction composeFloat(final Float2CharFunction before) {
        return new Float2FloatFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda4
            @Override // it.unimi.dsi.fastutil.floats.Float2FloatFunction
            public final float get(float f) {
                float f2;
                f2 = Char2FloatFunction.this.get(before.get(f));
                return f2;
            }
        };
    }

    default Char2DoubleFunction andThenDouble(final Float2DoubleFunction after) {
        return new Char2DoubleFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda8
            @Override // it.unimi.dsi.fastutil.chars.Char2DoubleFunction
            public final double get(char c) {
                double d;
                d = after.get(Char2FloatFunction.this.get(c));
                return d;
            }
        };
    }

    default Double2FloatFunction composeDouble(final Double2CharFunction before) {
        return new Double2FloatFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda3
            @Override // it.unimi.dsi.fastutil.doubles.Double2FloatFunction
            public final float get(double d) {
                float f;
                f = Char2FloatFunction.this.get(before.get(d));
                return f;
            }
        };
    }

    default <T> Char2ObjectFunction<T> andThenObject(final Float2ObjectFunction<? extends T> after) {
        return new Char2ObjectFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda2
            @Override // it.unimi.dsi.fastutil.chars.Char2ObjectFunction
            public final Object get(char c) {
                Object obj;
                obj = after.get(Char2FloatFunction.this.get(c));
                return obj;
            }
        };
    }

    default <T> Object2FloatFunction<T> composeObject(final Object2CharFunction<? super T> before) {
        return new Object2FloatFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.objects.Object2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = Char2FloatFunction.this.get(before.getChar(obj));
                return f;
            }
        };
    }

    default <T> Char2ReferenceFunction<T> andThenReference(final Float2ReferenceFunction<? extends T> after) {
        return new Char2ReferenceFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda6
            @Override // it.unimi.dsi.fastutil.chars.Char2ReferenceFunction
            public final Object get(char c) {
                Object obj;
                obj = after.get(Char2FloatFunction.this.get(c));
                return obj;
            }
        };
    }

    default <T> Reference2FloatFunction<T> composeReference(final Reference2CharFunction<? super T> before) {
        return new Reference2FloatFunction() { // from class: it.unimi.dsi.fastutil.chars.Char2FloatFunction$$ExternalSyntheticLambda13
            @Override // it.unimi.dsi.fastutil.objects.Reference2FloatFunction
            public final float getFloat(Object obj) {
                float f;
                f = Char2FloatFunction.this.get(before.getChar(obj));
                return f;
            }
        };
    }
}
