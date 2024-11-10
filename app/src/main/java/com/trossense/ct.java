package com.trossense;

import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class ct extends co<Number> {
    private static String l;
    private static final long m = dj.a(8954882171490247362L, -8732662596839717196L, MethodHandles.lookup().lookupClass()).a(203471098680847L);
    private Number i;
    private Number j;
    private Number k;

    static {
        if (l() == null) {
            b("m0sGO");
        }
    }

    public ct(String str, String str2, bm bmVar, long j, Number number, Number number2, Number number3, Number number4) {
        super(str, str2, (m ^ j) ^ 29636150452498L, bmVar, number);
        this.i = number2;
        this.j = number3;
        this.k = number4;
    }

    public static double a(double d, long j, double d2, double d3) {
        return d < d2 ? d2 : Math.min(d, d3);
    }

    public static Number a(long j, Number number, Number number2, Number number3) {
        long j2 = j ^ m;
        long j3 = j2 ^ 112925874764640L;
        double doubleValue = number.doubleValue();
        double doubleValue2 = number.doubleValue();
        if (number2 != null) {
            doubleValue2 = number2.doubleValue();
        }
        double a = a(doubleValue, j3, doubleValue2, (j2 < 0 || number3 != null) ? number3.doubleValue() : number.doubleValue());
        boolean z = number instanceof Integer;
        if (j2 > 0) {
            if (z) {
                return Integer.valueOf((int) a);
            }
            z = number instanceof Long;
        }
        if (j2 >= 0) {
            if (z) {
                return Long.valueOf((long) a);
            }
            z = number instanceof Float;
        }
        return z ? Float.valueOf((float) a) : Double.valueOf(a);
    }

    public static void b(String str) {
        l = str;
    }

    public static String l() {
        return l;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void a(Number number, long j) {
        long j2 = j ^ m;
        long j3 = 98983614564816L ^ j2;
        int i = (int) (j3 >>> 48);
        long j4 = (j3 << 16) >>> 16;
        long j5 = j2 ^ 74993707644887L;
        if (this.k == null) {
            this.d = number;
        } else {
            super.a(Double.valueOf(Math.round(a(j5, number, this.i, this.j).doubleValue() * (1.0d / this.k.doubleValue())) / (1.0d / this.k.doubleValue())), (char) i, j4);
        }
    }

    @Override // com.trossense.co
    public /* bridge */ /* synthetic */ void a(Object obj, char c, long j) {
        a((Number) obj, (((j << 16) >>> 16) | (c << 48)) ^ 5300949485847L);
    }

    /* JADX WARN: Type inference failed for: r5v12, types: [T, java.lang.Double] */
    public void b(int i, char c, short s, Number number) {
        this.d = Double.valueOf(Math.round(a(((((s << 48) >>> 48) | ((i << 32) | ((c << 48) >>> 32))) ^ m) ^ 44923744799856L, number, this.i, this.j).doubleValue() * (1.0d / this.k.doubleValue())) / (1.0d / this.k.doubleValue()));
    }

    public Number i() {
        return this.k;
    }

    public Number j() {
        return this.j;
    }

    public Number k() {
        return this.i;
    }
}
