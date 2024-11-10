package com.trossense;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

/* loaded from: classes3.dex */
public class cp extends co<Boolean> {
    private static final long k = dj.a(-4665936700735584995L, 6802185834879104367L, MethodHandles.lookup().lookupClass()).a(202055068182236L);
    private boolean i;
    private boolean j;

    public cp(int i, String str, short s, String str2, bm bmVar, Boolean bool, BooleanSupplier booleanSupplier, int i2) {
        super(str, str2, bmVar, bool, booleanSupplier, ((((i << 32) | ((s << 48) >>> 32)) | ((i2 << 48) >>> 48)) ^ k) ^ 5101709377755L);
    }

    public cp(long j, short s, String str, String str2, bm bmVar, Boolean bool) {
        super(str, str2, (((j << 16) | ((s << 48) >>> 48)) ^ k) ^ 11993979881609L, bmVar, bool);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public cp(java.lang.String r14, char r15, java.lang.String r16, com.trossense.bm r17, short r18, int r19, java.lang.Boolean r20, java.lang.Boolean r21) {
        /*
            r13 = this;
            r0 = r15
            long r0 = (long) r0
            r2 = 48
            long r0 = r0 << r2
            r3 = r18
            long r3 = (long) r3
            long r2 = r3 << r2
            r4 = 16
            long r2 = r2 >>> r4
            long r0 = r0 | r2
            r2 = r19
            long r2 = (long) r2
            r4 = 32
            long r2 = r2 << r4
            long r2 = r2 >>> r4
            long r0 = r0 | r2
            long r2 = com.trossense.cp.k
            long r0 = r0 ^ r2
            r2 = 126007461225242(0x729a65f7231a, double:6.2255957711065E-310)
            long r0 = r0 ^ r2
            long r2 = r0 >>> r4
            int r9 = (int) r2
            long r0 = r0 << r4
            long r0 = r0 >>> r4
            int r10 = (int) r0
            r5 = r13
            r6 = r14
            r7 = r16
            r8 = r17
            r11 = r20
            r12 = r21
            r5.<init>(r6, r7, r8, r9, r10, r11, r12)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.cp.<init>(java.lang.String, char, java.lang.String, com.trossense.bm, short, int, java.lang.Boolean, java.lang.Boolean):void");
    }

    public cp(String str, String str2, char c, bm bmVar, BooleanSupplier booleanSupplier, Boolean bool, int i, Boolean bool2, int i2, cv cvVar) {
        super(str, str2, bmVar, booleanSupplier, ((((c << 48) | ((i << 32) >>> 16)) | ((i2 << 48) >>> 48)) ^ k) ^ 72260174761373L, bool, bool2, cvVar);
    }

    public cp(short s, String str, int i, String str2, bm bmVar, Boolean bool, Boolean bool2, short s2, BooleanSupplier booleanSupplier) {
        super(str, str2, bmVar, bool, bool2, ((((s << 48) | ((i << 32) >>> 16)) | ((s2 << 48) >>> 48)) ^ k) ^ 108110407025289L, booleanSupplier);
    }
}
