package org.cloudburstmc.math;

import com.google.common.primitives.Longs;
import java.awt.Color;
import org.cloudburstmc.math.imaginary.Quaterniond;
import org.cloudburstmc.math.imaginary.Quaternionf;
import org.cloudburstmc.math.vector.Vector2d;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3d;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector4d;
import org.cloudburstmc.math.vector.Vector4f;
import org.cloudburstmc.math.vector.VectorNd;
import org.cloudburstmc.math.vector.VectorNf;

/* loaded from: classes5.dex */
public class GenericMath {
    public static final double DBL_EPSILON = Double.longBitsToDouble(4372995238176751616L);
    public static final float FLT_EPSILON = Float.intBitsToFloat(872415232);

    private GenericMath() {
    }

    public static float getDegreeDifference(float angle1, float angle2) {
        return Math.abs(wrapAngleDeg(angle1 - angle2));
    }

    public static double getRadianDifference(double radian1, double radian2) {
        return Math.abs(wrapAngleRad(radian1 - radian2));
    }

    public static float wrapAngleDeg(float angle) {
        float angle2 = angle % 360.0f;
        if (angle2 <= -180.0f) {
            return 360.0f + angle2;
        }
        if (angle2 > 180.0f) {
            return angle2 - 360.0f;
        }
        return angle2;
    }

    public static double wrapAngleRad(double angle) {
        double angle2 = angle % 6.283185307179586d;
        if (angle2 <= -3.141592653589793d) {
            return 6.283185307179586d + angle2;
        }
        if (angle2 > 3.141592653589793d) {
            return angle2 - 6.283185307179586d;
        }
        return angle2;
    }

    public static float wrapAnglePitchDeg(float angle) {
        float angle2 = wrapAngleDeg(angle);
        if (angle2 < -90.0f) {
            return -90.0f;
        }
        if (angle2 > 90.0f) {
            return 90.0f;
        }
        return angle2;
    }

    public static byte wrapByte(int value) {
        int value2 = value % 256;
        if (value2 < 0) {
            value2 += 256;
        }
        return (byte) value2;
    }

    public static double round(double input, int decimals) {
        double p = Math.pow(10.0d, decimals);
        return Math.round(input * p) / p;
    }

    public static double lerp(double a, double b, double percent) {
        return ((1.0d - percent) * a) + (percent * b);
    }

    public static float lerp(float a, float b, float percent) {
        return ((1.0f - percent) * a) + (percent * b);
    }

    public static int lerp(int a, int b, int percent) {
        return ((1 - percent) * a) + (percent * b);
    }

    public static Vector3f lerp(Vector3f a, Vector3f b, float percent) {
        return a.mul(1.0f - percent).add(b.mul(percent));
    }

    public static Vector3d lerp(Vector3d a, Vector3d b, double percent) {
        return a.mul(1.0d - percent).add(b.mul(percent));
    }

    public static Vector2f lerp(Vector2f a, Vector2f b, float percent) {
        return a.mul(1.0f - percent).add(b.mul(percent));
    }

    public static Vector2d lerp(Vector2d a, Vector2d b, double percent) {
        return a.mul(1.0d - percent).add(b.mul(percent));
    }

    public static double lerp(double x, double x1, double x2, double q0, double q1) {
        return (((x2 - x) / (x2 - x1)) * q0) + (((x - x1) / (x2 - x1)) * q1);
    }

    public static Color lerp(Color a, Color b, float percent) {
        int red = (int) lerp(a.getRed(), b.getRed(), percent);
        int blue = (int) lerp(a.getBlue(), b.getBlue(), percent);
        int green = (int) lerp(a.getGreen(), b.getGreen(), percent);
        int alpha = (int) lerp(a.getAlpha(), b.getAlpha(), percent);
        return new Color(red, green, blue, alpha);
    }

    public static Quaternionf slerp(Quaternionf a, Quaternionf b, float percent) {
        float inverted;
        float cosineTheta = a.dot(b);
        if (cosineTheta < 0.0f) {
            cosineTheta = -cosineTheta;
            inverted = -1.0f;
        } else {
            inverted = 1.0f;
        }
        if (1.0f - cosineTheta < FLT_EPSILON) {
            return a.mul(1.0f - percent).add(b.mul(percent * inverted));
        }
        float theta = (float) TrigMath.acos(cosineTheta);
        float sineTheta = TrigMath.sin(theta);
        float coefficient1 = TrigMath.sin((1.0f - percent) * theta) / sineTheta;
        float coefficient2 = (TrigMath.sin(percent * theta) / sineTheta) * inverted;
        return a.mul(coefficient1).add(b.mul(coefficient2));
    }

    public static Quaterniond slerp(Quaterniond a, Quaterniond b, double percent) {
        double inverted;
        double cosineTheta = a.dot(b);
        if (cosineTheta < 0.0d) {
            cosineTheta = -cosineTheta;
            inverted = -1.0d;
        } else {
            inverted = 1.0d;
        }
        if (1.0d - cosineTheta < DBL_EPSILON) {
            return a.mul(1.0d - percent).add(b.mul(percent * inverted));
        }
        double theta = TrigMath.acos(cosineTheta);
        double sineTheta = TrigMath.sin(theta);
        double coefficient1 = TrigMath.sin((1.0d - percent) * theta) / sineTheta;
        double coefficient2 = (TrigMath.sin(percent * theta) / sineTheta) * inverted;
        return a.mul(coefficient1).add(b.mul(coefficient2));
    }

    public static Quaternionf lerp(Quaternionf a, Quaternionf b, float percent) {
        return a.mul(1.0f - percent).add(b.mul(percent));
    }

    public static Quaterniond lerp(Quaterniond a, Quaterniond b, double percent) {
        return a.mul(1.0d - percent).add(b.mul(percent));
    }

    public static double biLerp(double x, double y, double q00, double q01, double q10, double q11, double x1, double x2, double y1, double y2) {
        double q0 = lerp(x, x1, x2, q00, q10);
        double q1 = lerp(x, x1, x2, q01, q11);
        return lerp(y, y1, y2, q0, q1);
    }

    public static double triLerp(double x, double y, double z, double q000, double q001, double q010, double q011, double q100, double q101, double q110, double q111, double x1, double x2, double y1, double y2, double z1, double z2) {
        double q00 = lerp(x, x1, x2, q000, q100);
        double q01 = lerp(x, x1, x2, q010, q110);
        double q10 = lerp(x, x1, x2, q001, q101);
        double q11 = lerp(x, x1, x2, q011, q111);
        double q0 = lerp(y, y1, y2, q00, q10);
        double q1 = lerp(y, y1, y2, q01, q11);
        return lerp(z, z1, z2, q0, q1);
    }

    public static Color blend(Color a, Color b) {
        return lerp(a, b, a.getAlpha() / 255.0f);
    }

    public static double clamp(double value, double low, double high) {
        if (value < low) {
            return low;
        }
        if (value > high) {
            return high;
        }
        return value;
    }

    public static int clamp(int value, int low, int high) {
        if (value < low) {
            return low;
        }
        if (value > high) {
            return high;
        }
        return value;
    }

    public static double inverseSqrt(double a) {
        double halfA = 0.5d * a;
        double a2 = Double.longBitsToDouble(6910469410427058090L - (Double.doubleToRawLongBits(a) >> 1));
        return (1.5d - ((halfA * a2) * a2)) * a2;
    }

    public static double sqrt(double a) {
        return inverseSqrt(a) * a;
    }

    public static int ceil(double a) {
        int possible_result = (int) a;
        if (a - possible_result > 0.0d) {
            return possible_result + 1;
        }
        return possible_result;
    }

    public static int ceil(float a) {
        int possible_result = (int) a;
        if (a - possible_result > 0.0f) {
            return possible_result + 1;
        }
        return possible_result;
    }

    public static long ceil64(double a) {
        long possible_result = (long) a;
        if (a - possible_result > 0.0d) {
            return possible_result + 1;
        }
        return possible_result;
    }

    public static long ceil64(float a) {
        long possible_result = a;
        if (a - ((float) possible_result) > 0.0f) {
            return possible_result + 1;
        }
        return possible_result;
    }

    public static int floor(double a) {
        int y = (int) a;
        if (a < y) {
            return y - 1;
        }
        return y;
    }

    public static int floor(float a) {
        int y = (int) a;
        if (a < y) {
            return y - 1;
        }
        return y;
    }

    public static long floor64(double a) {
        long y = (long) a;
        if (a < y) {
            return y - 1;
        }
        return y;
    }

    public static long floor64(float a) {
        long y = a;
        if (a < ((float) y)) {
            return y - 1;
        }
        return y;
    }

    public static byte max(byte value1, byte value2) {
        return value1 > value2 ? value1 : value2;
    }

    public static int roundUpPow2(int a) {
        if (a <= 0) {
            return 1;
        }
        if (a > 1073741824) {
            throw new IllegalArgumentException("Rounding " + a + " to the next highest power of two would exceed the int range");
        }
        int a2 = a - 1;
        int a3 = a2 | (a2 >> 1);
        int a4 = a3 | (a3 >> 2);
        int a5 = a4 | (a4 >> 4);
        int a6 = a5 | (a5 >> 8);
        return (a6 | (a6 >> 16)) + 1;
    }

    public static long roundUpPow2(long a) {
        if (a <= 0) {
            return 1L;
        }
        if (a > Longs.MAX_POWER_OF_TWO) {
            throw new IllegalArgumentException("Rounding " + a + " to the next highest power of two would exceed the int range");
        }
        long a2 = a - 1;
        long a3 = a2 | (a2 >> 1);
        long a4 = a3 | (a3 >> 2);
        long a5 = a4 | (a4 >> 4);
        long a6 = a5 | (a5 >> 8);
        long a7 = a6 | (a6 >> 16);
        return (a7 | (a7 >> 32)) + 1;
    }

    public static Float castFloat(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return Float.valueOf(((Number) o).floatValue());
        }
        try {
            return Float.valueOf(o.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Byte castByte(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return Byte.valueOf(((Number) o).byteValue());
        }
        try {
            return Byte.valueOf(o.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Short castShort(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return Short.valueOf(((Number) o).shortValue());
        }
        try {
            return Short.valueOf(o.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer castInt(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return Integer.valueOf(((Number) o).intValue());
        }
        try {
            return Integer.valueOf(o.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double castDouble(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return Double.valueOf(((Number) o).doubleValue());
        }
        try {
            return Double.valueOf(o.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long castLong(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return Long.valueOf(((Number) o).longValue());
        }
        try {
            return Long.valueOf(o.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Boolean castBoolean(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Boolean) {
            return (Boolean) o;
        }
        if (!(o instanceof String)) {
            return null;
        }
        try {
            return Boolean.valueOf(Boolean.parseBoolean((String) o));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static int mean(int... values) {
        int sum = 0;
        for (int v : values) {
            sum += v;
        }
        return sum / values.length;
    }

    public static double mean(double... values) {
        double sum = 0.0d;
        for (double v : values) {
            sum += v;
        }
        return sum / values.length;
    }

    public static String decToHex(int dec, int minDigits) {
        StringBuilder ret = new StringBuilder(Integer.toHexString(dec));
        while (ret.length() < minDigits) {
            ret.insert(0, '0');
        }
        return ret.toString();
    }

    public static int mod(int a, int div) {
        int remainder = a % div;
        return remainder < 0 ? remainder + div : remainder;
    }

    public static float mod(float a, float div) {
        float remainder = a % div;
        return remainder < 0.0f ? remainder + div : remainder;
    }

    public static double mod(double a, double div) {
        double remainder = a % div;
        return remainder < 0.0d ? remainder + div : remainder;
    }

    public static boolean isPowerOfTwo(int num) {
        return num > 0 && ((num + (-1)) & num) == 0;
    }

    public static int multiplyToShift(int a) {
        if (a < 1) {
            throw new IllegalArgumentException("Multiplicand must be at least 1");
        }
        int shift = 31 - Integer.numberOfLeadingZeros(a);
        if ((1 << shift) != a) {
            throw new IllegalArgumentException("Multiplicand must be a power of 2");
        }
        return shift;
    }

    public static Vector2f normalizeSafe(Vector2f v) {
        try {
            return v.normalize();
        } catch (ArithmeticException e) {
            return Vector2f.ZERO;
        }
    }

    public static Vector2d normalizeSafe(Vector2d v) {
        try {
            return v.normalize();
        } catch (ArithmeticException e) {
            return Vector2d.ZERO;
        }
    }

    public static Vector3f normalizeSafe(Vector3f v) {
        try {
            return v.normalize();
        } catch (ArithmeticException e) {
            return Vector3f.ZERO;
        }
    }

    public static Vector3d normalizeSafe(Vector3d v) {
        try {
            return v.normalize();
        } catch (ArithmeticException e) {
            return Vector3d.ZERO;
        }
    }

    public static Vector4f normalizeSafe(Vector4f v) {
        try {
            return v.normalize();
        } catch (ArithmeticException e) {
            return Vector4f.ZERO;
        }
    }

    public static Vector4d normalizeSafe(Vector4d v) {
        try {
            return v.normalize();
        } catch (ArithmeticException e) {
            return Vector4d.ZERO;
        }
    }

    public static VectorNf normalizeSafe(VectorNf v) {
        try {
            return v.normalize();
        } catch (ArithmeticException e) {
            return VectorNf.from(v.size());
        }
    }

    public static VectorNd normalizeSafe(VectorNd v) {
        try {
            return v.normalize();
        } catch (ArithmeticException e) {
            return VectorNd.from(v.size());
        }
    }
}
