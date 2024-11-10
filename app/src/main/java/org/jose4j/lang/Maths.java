package org.jose4j.lang;

/* loaded from: classes5.dex */
public class Maths {
    public static long add(long x, long y) {
        long result = x + y;
        if (0 > ((x ^ result) & (y ^ result))) {
            throw new ArithmeticException("long overflow adding: " + x + " + " + y + " = " + result);
        }
        return result;
    }

    public static long subtract(long x, long y) {
        long result = x - y;
        if (0 > ((x ^ y) & (x ^ result))) {
            throw new ArithmeticException("long overflow subtracting: " + x + " - " + y + " = " + result);
        }
        return result;
    }
}
