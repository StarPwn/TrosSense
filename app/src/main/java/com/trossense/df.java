package com.trossense;

import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class df {
    private static String i;
    private static final long j = dj.a(4634124295878412455L, -7166034147329922840L, MethodHandles.lookup().lookupClass()).a(252213878032882L);
    private d a;
    private long b;
    private long c;
    private long d = System.currentTimeMillis();
    private float e;
    private float f;
    private float g;
    private boolean h;

    static {
        if (f() != null) {
            b("ThCzh");
        }
    }

    public df(d dVar, long j2, long j3) {
        this.a = dVar;
        f();
        this.b = j2;
        if (PointerHolder.s() == null) {
            b("uLwp0");
        }
    }

    public static void b(String str) {
        i = str;
    }

    public static String f() {
        return i;
    }

    public float a() {
        return this.f;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0059  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(float r10, long r11) {
        /*
            r9 = this;
            long r0 = com.trossense.df.j
            long r11 = r11 ^ r0
            java.lang.String r0 = f()
            long r1 = java.lang.System.currentTimeMillis()
            r9.c = r1
            float r1 = r9.f
            int r1 = (r1 > r10 ? 1 : (r1 == r10 ? 0 : -1))
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L29
            r9.f = r10
            r9.e()
            int r1 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1))
            if (r1 <= 0) goto L27
            if (r0 == 0) goto L46
            int[] r1 = new int[r2]
            com.trossense.sdk.PointerHolder.b(r1)
            goto L29
        L27:
            r1 = r0
            goto L5a
        L29:
            long r5 = r9.c
            long r7 = r9.b
            long r5 = r5 - r7
            long r7 = r9.d
            int r1 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            int r5 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1))
            if (r5 < 0) goto L3b
            if (r1 <= 0) goto L39
            goto L3c
        L39:
            r1 = 0
            goto L3d
        L3b:
            r2 = r1
        L3c:
            r1 = r2
        L3d:
            r9.h = r1
            if (r5 <= 0) goto L59
            if (r1 == 0) goto L46
            r9.g = r10
            return
        L46:
            com.trossense.d r1 = r9.a
            java.util.function.Function r1 = r1.a()
            double r5 = r9.c()
            java.lang.Double r2 = java.lang.Double.valueOf(r5)
            java.lang.Object r1 = r1.apply(r2)
            goto L5a
        L59:
            r1 = r9
        L5a:
            java.lang.Double r1 = (java.lang.Double) r1
            double r1 = r1.doubleValue()
            int r11 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1))
            if (r11 < 0) goto L78
            float r12 = r9.g
            int r12 = (r12 > r10 ? 1 : (r12 == r10 ? 0 : -1))
            if (r12 <= 0) goto L78
            float r12 = r9.e
            double r3 = (double) r12
            float r12 = r12 - r10
            double r5 = (double) r12
            double r5 = r5 * r1
            double r3 = r3 - r5
            float r12 = (float) r3
            if (r11 < 0) goto L80
            r9.g = r12
            if (r0 == 0) goto L82
        L78:
            float r11 = r9.e
            double r3 = (double) r11
            float r10 = r10 - r11
            double r10 = (double) r10
            double r10 = r10 * r1
            double r3 = r3 + r10
            float r12 = (float) r3
        L80:
            r9.g = r12
        L82:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.df.a(float, long):void");
    }

    public void a(long j2) {
        this.d = j2;
    }

    public void a(boolean z) {
        this.h = z;
    }

    public float b() {
        return this.g;
    }

    public void b(long j2) {
        this.b = j2;
    }

    public boolean b(long j2, float f) {
        return ((float) (System.currentTimeMillis() - this.d)) > f;
    }

    public double c() {
        return (System.currentTimeMillis() - this.d) / this.b;
    }

    public boolean d() {
        return this.h;
    }

    public void e() {
        this.d = System.currentTimeMillis();
        this.e = this.g;
        this.h = false;
    }
}
