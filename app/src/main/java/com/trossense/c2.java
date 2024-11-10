package com.trossense;

import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class c2 {
    private static final long a = dj.a(1505659220205980578L, -3290422488563097826L, MethodHandles.lookup().lookupClass()).a(11509548548599L);

    public static double a(float[] fArr, long j) {
        if (fArr.length < 2) {
            return 0.0d;
        }
        return Math.hypot(fArr[0], fArr[1]);
    }

    public static float a(float f, float f2, double d) {
        return a(f, f2, (float) d).floatValue();
    }

    public static float a(float f, long j) {
        float f2 = f % 360.0f;
        if (f2 >= 180.0f) {
            f2 -= 360.0f;
        }
        return f2 < -180.0f ? f2 + 360.0f : f2;
    }

    public static int a(int i, int i2, double d) {
        return a(i, i2, (float) d).intValue();
    }

    public static int a(long j, char c, double d) {
        int i = (int) d;
        return d >= ((double) i) ? i : i - 1;
    }

    public static Double a(double d, double d2, double d3) {
        return Double.valueOf(d + ((d2 - d) * d3));
    }

    public static Number a(Number number, long j, Number number2, Number number3) {
        long j2 = a ^ j;
        long j3 = 9338433701907L ^ j2;
        int i = (int) (j3 >>> 48);
        int i2 = (int) ((j3 << 16) >>> 48);
        int i3 = (int) ((j3 << 32) >>> 32);
        double doubleValue = number.doubleValue();
        int[] b = c3.b();
        double doubleValue2 = number.doubleValue();
        if (number2 != null) {
            doubleValue2 = number2.doubleValue();
        }
        double d = doubleValue2;
        double doubleValue3 = number.doubleValue();
        if (j2 <= 0 || number3 != null) {
            doubleValue3 = number3.doubleValue();
        }
        double b2 = b((char) i, (short) i2, i3, doubleValue, d, doubleValue3);
        boolean z = number instanceof Integer;
        if (j2 > 0) {
            if (z) {
                return Integer.valueOf((int) b2);
            }
            z = number instanceof Long;
        }
        if (j2 >= 0) {
            if (z) {
                return Long.valueOf((long) b2);
            }
            z = number instanceof Float;
        }
        if (z) {
            return Float.valueOf((float) b2);
        }
        Double valueOf = Double.valueOf(b2);
        if (j2 >= 0) {
            if (b != null) {
                b = new int[2];
            }
            return valueOf;
        }
        PointerHolder.b(b);
        return valueOf;
    }

    public static double b(char c, short s, int i, double d, double d2, double d3) {
        return d < d2 ? d2 : Math.min(d, d3);
    }

    public static double b(double d, long j) {
        double d2 = d % 360.0d;
        if (d2 >= 180.0d) {
            d2 -= 360.0d;
        }
        return d2 < -180.0d ? d2 + 360.0d : d2;
    }
}
