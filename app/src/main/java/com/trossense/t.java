package com.trossense;

import android.graphics.Color;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class t extends q {
    private static final long u = dj.a(-7338425839911953492L, 1942787794223468784L, MethodHandles.lookup().lookupClass()).a(167209206811779L);
    private df o;
    public boolean p;
    private cr q;
    private ah r;
    final ah s;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public t(long r15, com.trossense.ah r17, float r18, float r19, com.trossense.cr r20, com.trossense.ah r21) {
        /*
            r14 = this;
            r7 = r14
            long r0 = com.trossense.t.u
            long r0 = r0 ^ r15
            r2 = 8364457763499(0x79b8088a2ab, double:4.132591227035E-311)
            long r2 = r2 ^ r0
            r4 = 43750686785559(0x27ca80250017, double:2.16157113227053E-310)
            long r12 = r0 ^ r4
            r0 = r17
            r7.s = r0
            r4 = 0
            r5 = 0
            r0 = r14
            r1 = r2
            r3 = r4
            r4 = r5
            r5 = r18
            r6 = r19
            r0.<init>(r1, r3, r4, r5, r6)
            com.trossense.df r0 = new com.trossense.df
            com.trossense.d r9 = com.trossense.d.Decelerate
            r10 = 250(0xfa, double:1.235E-321)
            r8 = r0
            r8.<init>(r9, r10, r12)
            r7.o = r0
            r0 = r20
            r7.q = r0
            r0 = r21
            r7.r = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.t.<init>(long, com.trossense.ah, float, float, com.trossense.cr, com.trossense.ah):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static df a(t tVar) {
        return tVar.o;
    }

    @Override // com.trossense.p
    public void a(float f, float f2, long j, long j2) {
        boolean z = this.p;
        if (j2 > 0) {
            z = !z;
        }
        this.p = z;
    }

    @Override // com.trossense.p
    public void d(long j) {
        long j2 = j ^ 111589528524585L;
        da.a(this.a, this.b, j2, this.d, this.e, 15.0f, c9.a(Color.rgb(64, 64, 64), ad.u, this.o.b()));
        da.a(this.a + 3.0f, 3.0f + this.b, j2, this.d - 6.0f, this.e - 6.0f, 13.0f, c9.a(Color.rgb(45, 45, 45), Color.rgb(64, 64, 64), this.o.b()));
        ah.p().a(this.q.i((short) (r7 >>> 48), (int) (((j ^ 116047541577083L) << 16) >>> 32), (short) ((r7 << 48) >>> 48)), j ^ 121281231864540L, this.a + 20.0f, this.b, this.e, 0.25f, -1);
        float f = (this.a + this.d) - 38.0f;
        float a = this.b + ah.q().a(this.e);
        da.a(f, a, ah.q().a(j ^ 91228529291838L, "z"), j ^ 28834871580341L, ah.q().a(), this.o.b() * 180.0f);
        ah.q().b("z", j ^ 50167032995699L, f, a, -1);
        da.b((j ^ 128968524833061L) >>> 8, (byte) ((r1 << 56) >>> 56));
    }
}
