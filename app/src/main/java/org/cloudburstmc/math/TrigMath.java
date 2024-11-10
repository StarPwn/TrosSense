package org.cloudburstmc.math;

/* loaded from: classes5.dex */
public class TrigMath {
    private static final int COS_OFFSET = 1048576;
    public static final double DEG_TO_RAD = 0.017453292519943295d;
    public static final double HALF_DEG_TO_RAD = 0.008726646259971648d;
    public static final double HALF_PI = 1.5707963267948966d;
    public static final double PI = 3.141592653589793d;
    public static final double QUARTER_PI = 0.7853981633974483d;
    public static final double RAD_TO_DEG = 57.29577951308232d;
    private static final int SIN_BITS = 22;
    private static final double SIN_CONVERSION_FACTOR = 667544.214430109d;
    private static final int SIN_MASK = 4194303;
    private static final int SIN_SIZE = 4194304;
    public static final double SQUARED_PI = 9.869604401089358d;
    public static final double THREE_PI_HALVES = 4.71238898038469d;
    public static final double TWO_PI = 6.283185307179586d;
    private static final double p0 = 896.7859740366387d;
    private static final double p1 = 1780.406316433197d;
    private static final double p2 = 1153.029351540485d;
    private static final double p3 = 268.42548195503974d;
    private static final double p4 = 16.15364129822302d;
    private static final double q0 = 896.7859740366387d;
    private static final double q1 = 2079.33497444541d;
    private static final double q2 = 1666.7838148816338d;
    private static final double q3 = 536.2653740312153d;
    private static final double q4 = 58.95697050844462d;
    private static final double sq2m1 = 0.41421356237309503d;
    private static final double sq2p1 = 2.414213562373095d;
    public static final double SQRT_OF_TWO = Math.sqrt(2.0d);
    public static final double HALF_SQRT_OF_TWO = SQRT_OF_TWO / 2.0d;
    private static final float[] SIN_TABLE = new float[4194304];

    static {
        for (int i = 0; i < 4194304; i++) {
            SIN_TABLE[i] = (float) Math.sin((i * 6.283185307179586d) / 4194304.0d);
        }
    }

    private TrigMath() {
    }

    public static float sin(double angle) {
        return sinRaw(GenericMath.floor(SIN_CONVERSION_FACTOR * angle));
    }

    public static float cos(double angle) {
        return cosRaw(GenericMath.floor(SIN_CONVERSION_FACTOR * angle));
    }

    public static float tan(double angle) {
        int idx = GenericMath.floor(SIN_CONVERSION_FACTOR * angle);
        return sinRaw(idx) / cosRaw(idx);
    }

    public static float csc(double angle) {
        return 1.0f / sin(angle);
    }

    public static float sec(double angle) {
        return 1.0f / cos(angle);
    }

    public static float cot(double angle) {
        int idx = GenericMath.floor(SIN_CONVERSION_FACTOR * angle);
        return cosRaw(idx) / sinRaw(idx);
    }

    public static double asin(double value) {
        if (value > 1.0d) {
            return Double.NaN;
        }
        if (value >= 0.0d) {
            double temp = Math.sqrt(1.0d - (value * value));
            if (value > 0.7d) {
                return 1.5707963267948966d - msatan(temp / value);
            }
            return msatan(value / temp);
        }
        return -asin(-value);
    }

    public static double acos(double value) {
        if (value > 1.0d || value < -1.0d) {
            return Double.NaN;
        }
        return 1.5707963267948966d - asin(value);
    }

    public static double atan(double value) {
        if (value > 0.0d) {
            return msatan(value);
        }
        return -msatan(-value);
    }

    public static double atan2(double y, double x) {
        if (y + x == y) {
            return y >= 0.0d ? 1.5707963267948966d : -1.5707963267948966d;
        }
        double y2 = atan(y / x);
        if (x >= 0.0d) {
            return y2;
        }
        if (y2 <= 0.0d) {
            return 3.141592653589793d + y2;
        }
        return y2 - 3.141592653589793d;
    }

    public static double acsc(double value) {
        if (value == 0.0d) {
            return Double.NaN;
        }
        return asin(1.0d / value);
    }

    public static double asec(double value) {
        if (value == 0.0d) {
            return Double.NaN;
        }
        return acos(1.0d / value);
    }

    public static double acot(double value) {
        if (value == 0.0d) {
            return Double.NaN;
        }
        if (value > 0.0d) {
            return atan(1.0d / value);
        }
        return atan(1.0d / value) + 3.141592653589793d;
    }

    private static float sinRaw(int idx) {
        return SIN_TABLE[SIN_MASK & idx];
    }

    private static float cosRaw(int idx) {
        return SIN_TABLE[(1048576 + idx) & SIN_MASK];
    }

    private static double mxatan(double arg) {
        double argsq = arg * arg;
        double value = (((((((p4 * argsq) + p3) * argsq) + p2) * argsq) + p1) * argsq) + 896.7859740366387d;
        return (value / (((((((((q4 + argsq) * argsq) + q3) * argsq) + q2) * argsq) + q1) * argsq) + 896.7859740366387d)) * arg;
    }

    private static double msatan(double arg) {
        if (arg < sq2m1) {
            return mxatan(arg);
        }
        if (arg > sq2p1) {
            return 1.5707963267948966d - mxatan(1.0d / arg);
        }
        return mxatan((arg - 1.0d) / (1.0d + arg)) + 0.7853981633974483d;
    }
}
