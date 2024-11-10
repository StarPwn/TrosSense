package io.netty.util.internal;

/* loaded from: classes4.dex */
public final class ConstantTimeUtils {
    private ConstantTimeUtils() {
    }

    public static int equalsConstantTime(int x, int y) {
        int z = ~(x ^ y);
        int z2 = z & (z >> 16);
        int z3 = z2 & (z2 >> 8);
        int z4 = z3 & (z3 >> 4);
        int z5 = z4 & (z4 >> 2);
        return z5 & (z5 >> 1) & 1;
    }

    public static int equalsConstantTime(long x, long y) {
        long z = ~(x ^ y);
        long z2 = z & (z >> 32);
        long z3 = z2 & (z2 >> 16);
        long z4 = z3 & (z3 >> 8);
        long z5 = z4 & (z4 >> 4);
        long z6 = z5 & (z5 >> 2);
        return (int) (1 & z6 & (z6 >> 1));
    }

    public static int equalsConstantTime(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
        int b = 0;
        int end = startPos1 + length;
        while (startPos1 < end) {
            b |= bytes1[startPos1] ^ bytes2[startPos2];
            startPos1++;
            startPos2++;
        }
        return equalsConstantTime(b, 0);
    }

    public static int equalsConstantTime(CharSequence s1, CharSequence s2) {
        if (s1.length() != s2.length()) {
            return 0;
        }
        int c = 0;
        for (int i = 0; i < s1.length(); i++) {
            c |= s1.charAt(i) ^ s2.charAt(i);
        }
        int i2 = equalsConstantTime(c, 0);
        return i2;
    }
}
