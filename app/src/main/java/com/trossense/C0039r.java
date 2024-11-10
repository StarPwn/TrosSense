package com.trossense;

import android.graphics.Color;
import java.lang.invoke.MethodHandles;

/* JADX INFO: Access modifiers changed from: package-private */
/* renamed from: com.trossense.r, reason: case insensitive filesystem */
/* loaded from: classes3.dex */
public class C0039r extends q {
    private static final long r = dj.a(8496426420237156118L, -1939136278240143597L, MethodHandles.lookup().lookupClass()).a(67518884874375L);
    public df o;
    private cp p;
    final af q;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public C0039r(char r15, com.trossense.af r16, float r17, float r18, com.trossense.cp r19, int r20, short r21) {
        /*
            r14 = this;
            r7 = r14
            r0 = r15
            long r0 = (long) r0
            r2 = 48
            long r0 = r0 << r2
            r3 = r20
            long r3 = (long) r3
            r5 = 32
            long r3 = r3 << r5
            r5 = 16
            long r3 = r3 >>> r5
            long r0 = r0 | r3
            r3 = r21
            long r3 = (long) r3
            long r3 = r3 << r2
            long r2 = r3 >>> r2
            long r0 = r0 | r2
            long r2 = com.trossense.C0039r.r
            long r0 = r0 ^ r2
            r2 = 100524397324460(0x5b6d28fe68ac, double:4.96656512869114E-310)
            long r2 = r2 ^ r0
            r4 = 135498304834064(0x7b3c2853ca10, double:6.6945057488236E-310)
            long r12 = r0 ^ r4
            r0 = r16
            r7.q = r0
            r4 = 0
            r5 = 0
            r0 = r14
            r1 = r2
            r3 = r4
            r4 = r5
            r5 = r17
            r6 = r18
            r0.<init>(r1, r3, r4, r5, r6)
            com.trossense.df r0 = new com.trossense.df
            com.trossense.d r9 = com.trossense.d.Decelerate
            r10 = 250(0xfa, double:1.235E-321)
            r8 = r0
            r8.<init>(r9, r10, r12)
            r7.o = r0
            r0 = r19
            r7.p = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.C0039r.<init>(char, com.trossense.af, float, float, com.trossense.cp, int, short):void");
    }

    @Override // com.trossense.p
    public void a(float f, float f2, long j, long j2) {
        cp cpVar = this.p;
        cpVar.a(Boolean.valueOf(!cpVar.e().booleanValue()), (char) (r1 >>> 48), ((71909112912147L ^ j2) << 16) >>> 16);
    }

    @Override // com.trossense.p
    public void d(long j) {
        long j2 = j ^ 100102215916871L;
        long j3 = j ^ 111589528524585L;
        this.o.a(this.p.e().booleanValue() ? 1.0f : 0.0f, j ^ 105732733789211L);
        da.a(this.a, this.b, j3, this.d, this.e, this.e / 2.0f, c9.a(Color.rgb(64, 64, 64), ad.u, this.o.b()));
        float f = (this.e / 2.0f) - 3.5f;
        da.a(j2, this.a + f + 3.0f + (((this.d - (f * 2.0f)) - 6.0f) * this.o.b()), this.b + (this.e / 2.0f), f, -1);
    }
}
