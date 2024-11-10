package com.google.common.math;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Booleans;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;
import org.jose4j.jwk.RsaJsonWebKey;

/* loaded from: classes.dex */
public final class DoubleMath {
    static final int MAX_FACTORIAL = 170;
    private static final double MAX_INT_AS_DOUBLE = 2.147483647E9d;
    private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18d;
    private static final double MIN_INT_AS_DOUBLE = -2.147483648E9d;
    private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18d;
    private static final double LN_2 = Math.log(2.0d);
    static final double[] everySixteenthFactorial = {1.0d, 2.0922789888E13d, 2.631308369336935E35d, 1.2413915592536073E61d, 1.2688693218588417E89d, 7.156945704626381E118d, 9.916779348709496E149d, 1.974506857221074E182d, 3.856204823625804E215d, 5.5502938327393044E249d, 4.7147236359920616E284d};

    static double roundIntermediate(double x, RoundingMode mode) {
        if (!DoubleUtils.isFinite(x)) {
            throw new ArithmeticException("input is infinite or NaN");
        }
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(x));
                return x;
            case 2:
                if (x >= 0.0d || isMathematicalInteger(x)) {
                    return x;
                }
                return ((long) x) - 1;
            case 3:
                if (x <= 0.0d || isMathematicalInteger(x)) {
                    return x;
                }
                return ((long) x) + 1;
            case 4:
                return x;
            case 5:
                if (isMathematicalInteger(x)) {
                    return x;
                }
                return ((long) x) + (x > 0.0d ? 1 : -1);
            case 6:
                return Math.rint(x);
            case 7:
                double z = Math.rint(x);
                if (Math.abs(x - z) == 0.5d) {
                    return Math.copySign(0.5d, x) + x;
                }
                return z;
            case 8:
                double z2 = Math.rint(x);
                if (Math.abs(x - z2) == 0.5d) {
                    return x;
                }
                return z2;
            default:
                throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.common.math.DoubleMath$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];

        static {
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    public static int roundToInt(double x, RoundingMode mode) {
        double z = roundIntermediate(x, mode);
        MathPreconditions.checkInRange((z > -2.147483649E9d) & (z < 2.147483648E9d));
        return (int) z;
    }

    public static long roundToLong(double x, RoundingMode mode) {
        double z = roundIntermediate(x, mode);
        MathPreconditions.checkInRange((MIN_LONG_AS_DOUBLE - z < 1.0d) & (z < MAX_LONG_AS_DOUBLE_PLUS_ONE));
        return (long) z;
    }

    public static BigInteger roundToBigInteger(double x, RoundingMode mode) {
        double x2 = roundIntermediate(x, mode);
        if ((MIN_LONG_AS_DOUBLE - x2 < 1.0d) & (x2 < MAX_LONG_AS_DOUBLE_PLUS_ONE)) {
            return BigInteger.valueOf((long) x2);
        }
        int exponent = Math.getExponent(x2);
        long significand = DoubleUtils.getSignificand(x2);
        BigInteger result = BigInteger.valueOf(significand).shiftLeft(exponent - 52);
        return x2 < 0.0d ? result.negate() : result;
    }

    public static boolean isPowerOfTwo(double x) {
        return x > 0.0d && DoubleUtils.isFinite(x) && LongMath.isPowerOfTwo(DoubleUtils.getSignificand(x));
    }

    public static double log2(double x) {
        return Math.log(x) / LN_2;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:12:0x0033. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:19:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0072  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int log2(double r9, java.math.RoundingMode r11) {
        /*
            r0 = 0
            int r0 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            r1 = 0
            r2 = 1
            if (r0 <= 0) goto L10
            boolean r0 = com.google.common.math.DoubleUtils.isFinite(r9)
            if (r0 == 0) goto L10
            r0 = r2
            goto L11
        L10:
            r0 = r1
        L11:
            java.lang.String r3 = "x must be positive and finite"
            com.google.common.base.Preconditions.checkArgument(r0, r3)
            int r0 = java.lang.Math.getExponent(r9)
            boolean r3 = com.google.common.math.DoubleUtils.isNormal(r9)
            if (r3 != 0) goto L2b
            r1 = 4841369599423283200(0x4330000000000000, double:4.503599627370496E15)
            double r1 = r1 * r9
            int r1 = log2(r1, r11)
            int r1 = r1 + (-52)
            return r1
        L2b:
            int[] r3 = com.google.common.math.DoubleMath.AnonymousClass1.$SwitchMap$java$math$RoundingMode
            int r4 = r11.ordinal()
            r3 = r3[r4]
            switch(r3) {
                case 1: goto L64;
                case 2: goto L6b;
                case 3: goto L5e;
                case 4: goto L54;
                case 5: goto L4a;
                case 6: goto L3c;
                case 7: goto L3c;
                case 8: goto L3c;
                default: goto L36;
            }
        L36:
            java.lang.AssertionError r1 = new java.lang.AssertionError
            r1.<init>()
            throw r1
        L3c:
            double r3 = com.google.common.math.DoubleUtils.scaleNormalize(r9)
            double r5 = r3 * r3
            r7 = 4611686018427387904(0x4000000000000000, double:2.0)
            int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r5 <= 0) goto L49
            r1 = r2
        L49:
            goto L6d
        L4a:
            if (r0 < 0) goto L4d
            r1 = r2
        L4d:
            boolean r3 = isPowerOfTwo(r9)
            r2 = r2 ^ r3
            r1 = r1 & r2
            goto L6d
        L54:
            if (r0 >= 0) goto L57
            r1 = r2
        L57:
            boolean r3 = isPowerOfTwo(r9)
            r2 = r2 ^ r3
            r1 = r1 & r2
            goto L6d
        L5e:
            boolean r1 = isPowerOfTwo(r9)
            r1 = r1 ^ r2
            goto L6d
        L64:
            boolean r1 = isPowerOfTwo(r9)
            com.google.common.math.MathPreconditions.checkRoundingUnnecessary(r1)
        L6b:
            r1 = 0
        L6d:
            if (r1 == 0) goto L72
            int r2 = r0 + 1
            goto L73
        L72:
            r2 = r0
        L73:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.math.DoubleMath.log2(double, java.math.RoundingMode):int");
    }

    public static boolean isMathematicalInteger(double x) {
        return DoubleUtils.isFinite(x) && (x == 0.0d || 52 - Long.numberOfTrailingZeros(DoubleUtils.getSignificand(x)) <= Math.getExponent(x));
    }

    public static double factorial(int n) {
        MathPreconditions.checkNonNegative(RsaJsonWebKey.MODULUS_MEMBER_NAME, n);
        if (n > 170) {
            return Double.POSITIVE_INFINITY;
        }
        double accum = 1.0d;
        for (int i = (n & (-16)) + 1; i <= n; i++) {
            accum *= i;
        }
        return everySixteenthFactorial[n >> 4] * accum;
    }

    public static boolean fuzzyEquals(double a, double b, double tolerance) {
        MathPreconditions.checkNonNegative("tolerance", tolerance);
        return Math.copySign(a - b, 1.0d) <= tolerance || a == b || (Double.isNaN(a) && Double.isNaN(b));
    }

    public static int fuzzyCompare(double a, double b, double tolerance) {
        if (fuzzyEquals(a, b, tolerance)) {
            return 0;
        }
        if (a < b) {
            return -1;
        }
        if (a > b) {
            return 1;
        }
        return Booleans.compare(Double.isNaN(a), Double.isNaN(b));
    }

    @Deprecated
    public static double mean(double... values) {
        Preconditions.checkArgument(values.length > 0, "Cannot take mean of 0 values");
        long count = 1;
        double mean = checkFinite(values[0]);
        for (int index = 1; index < values.length; index++) {
            checkFinite(values[index]);
            count++;
            mean += (values[index] - mean) / count;
        }
        return mean;
    }

    @Deprecated
    public static double mean(int... values) {
        Preconditions.checkArgument(values.length > 0, "Cannot take mean of 0 values");
        long sum = 0;
        for (int i : values) {
            sum += i;
        }
        return sum / values.length;
    }

    @Deprecated
    public static double mean(long... values) {
        Preconditions.checkArgument(values.length > 0, "Cannot take mean of 0 values");
        long count = 1;
        double mean = values[0];
        for (int index = 1; index < values.length; index++) {
            count++;
            mean += (values[index] - mean) / count;
        }
        return mean;
    }

    @Deprecated
    public static double mean(Iterable<? extends Number> values) {
        return mean(values.iterator());
    }

    @Deprecated
    public static double mean(Iterator<? extends Number> values) {
        Preconditions.checkArgument(values.hasNext(), "Cannot take mean of 0 values");
        long count = 1;
        double mean = checkFinite(values.next().doubleValue());
        while (values.hasNext()) {
            double value = checkFinite(values.next().doubleValue());
            count++;
            mean += (value - mean) / count;
        }
        return mean;
    }

    private static double checkFinite(double argument) {
        Preconditions.checkArgument(DoubleUtils.isFinite(argument));
        return argument;
    }

    private DoubleMath() {
    }
}
