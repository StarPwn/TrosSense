package com.google.common.math;

import java.math.BigInteger;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
final class MathPreconditions {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int checkPositive(@Nullable String role, int x) {
        if (x <= 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
        }
        return x;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long checkPositive(@Nullable String role, long x) {
        if (x <= 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
        }
        return x;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BigInteger checkPositive(@Nullable String role, BigInteger x) {
        if (x.signum() <= 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
        }
        return x;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int checkNonNegative(@Nullable String role, int x) {
        if (x < 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long checkNonNegative(@Nullable String role, long x) {
        if (x < 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BigInteger checkNonNegative(@Nullable String role, BigInteger x) {
        if (x.signum() < 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static double checkNonNegative(@Nullable String role, double x) {
        if (x < 0.0d) {
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkRoundingUnnecessary(boolean condition) {
        if (!condition) {
            throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkInRange(boolean condition) {
        if (!condition) {
            throw new ArithmeticException("not in range");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkNoOverflow(boolean condition) {
        if (!condition) {
            throw new ArithmeticException("overflow");
        }
    }

    private MathPreconditions() {
    }
}
