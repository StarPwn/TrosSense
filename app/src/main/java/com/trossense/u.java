package com.trossense;

import android.graphics.Color;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class u extends q {
    private static final long r = dj.a(8336595527349470911L, -257599814319105039L, MethodHandles.lookup().lookupClass()).a(33744653034787L);
    private String o;
    private ah p;
    final ah q;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public u(long r8, com.trossense.ah r10, float r11, float r12, java.lang.String r13, com.trossense.ah r14) {
        /*
            r7 = this;
            long r0 = com.trossense.u.r
            long r0 = r0 ^ r8
            r2 = 4462867848050(0x40f17a71b72, double:2.204949685651E-311)
            long r1 = r0 ^ r2
            r7.q = r10
            r3 = 0
            r4 = 0
            r0 = r7
            r5 = r11
            r6 = r12
            r0.<init>(r1, r3, r4, r5, r6)
            r7.o = r13
            r7.p = r14
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.u.<init>(long, com.trossense.ah, float, float, java.lang.String, com.trossense.ah):void");
    }

    @Override // com.trossense.p
    public void a(float f, float f2, long j, long j2) {
        ah.a(this.p).p = false;
        ah.b(this.p).a(this.o, 11997373842671L ^ j2);
    }

    public void a(String str) {
        this.o = str;
    }

    @Override // com.trossense.p
    public void d(long j) {
        ah.p().a(this.o, j ^ 121281231864540L, this.a + 20.0f, this.b, this.e, 0.25f, Color.argb((int) (t.a(ah.a(this.p)).b() * 255.0f), 255, 255, 255));
    }
}
