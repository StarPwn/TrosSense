package it.unimi.dsi.fastutil;

/* loaded from: classes4.dex */
public class HashCommon {
    private static final int INT_PHI = -1640531527;
    private static final int INV_INT_PHI = 340573321;
    private static final long INV_LONG_PHI = -1018231460777725123L;
    private static final long LONG_PHI = -7046029254386353131L;

    protected HashCommon() {
    }

    public static int murmurHash3(int x) {
        int x2 = (x ^ (x >>> 16)) * (-2048144789);
        int x3 = (x2 ^ (x2 >>> 13)) * (-1028477387);
        return x3 ^ (x3 >>> 16);
    }

    public static long murmurHash3(long x) {
        long x2 = (x ^ (x >>> 33)) * (-49064778989728563L);
        long x3 = (x2 ^ (x2 >>> 33)) * (-4265267296055464877L);
        return x3 ^ (x3 >>> 33);
    }

    public static int mix(int x) {
        int h = INT_PHI * x;
        return (h >>> 16) ^ h;
    }

    public static int invMix(int x) {
        return ((x >>> 16) ^ x) * INV_INT_PHI;
    }

    public static long mix(long x) {
        long h = LONG_PHI * x;
        long h2 = h ^ (h >>> 32);
        return (h2 >>> 16) ^ h2;
    }

    public static long invMix(long x) {
        long x2 = x ^ (x >>> 32);
        long x3 = x2 ^ (x2 >>> 16);
        return ((x3 >>> 32) ^ x3) * INV_LONG_PHI;
    }

    public static int float2int(float f) {
        return Float.floatToRawIntBits(f);
    }

    public static int double2int(double d) {
        long l = Double.doubleToRawLongBits(d);
        return (int) ((l >>> 32) ^ l);
    }

    public static int long2int(long l) {
        return (int) ((l >>> 32) ^ l);
    }

    public static int nextPowerOfTwo(int x) {
        return 1 << (32 - Integer.numberOfLeadingZeros(x - 1));
    }

    public static long nextPowerOfTwo(long x) {
        return 1 << (64 - Long.numberOfLeadingZeros(x - 1));
    }

    public static int maxFill(int n, float f) {
        return Math.min((int) Math.ceil(n * f), n - 1);
    }

    public static long maxFill(long n, float f) {
        return Math.min((long) Math.ceil(((float) n) * f), n - 1);
    }

    public static int arraySize(int expected, float f) {
        long s = Math.max(2L, nextPowerOfTwo((long) Math.ceil(expected / f)));
        if (s > 1073741824) {
            throw new IllegalArgumentException("Too large (" + expected + " expected elements with load factor " + f + ")");
        }
        return (int) s;
    }

    public static long bigArraySize(long expected, float f) {
        return nextPowerOfTwo((long) Math.ceil(((float) expected) / f));
    }
}
