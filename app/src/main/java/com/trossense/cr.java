package com.trossense;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

/* loaded from: classes3.dex */
public class cr extends co<String> {
    private static final long k = dj.a(8233527421895076939L, -4186507216405210846L, MethodHandles.lookup().lookupClass()).a(240274238052352L);
    private int i;
    private List<cu> j;

    public cr(long j, String str, String str2, bm bmVar, BooleanSupplier booleanSupplier, String str3, String str4, cv cvVar) {
        super(str, str2, bmVar, booleanSupplier, (k ^ j) ^ 66496135176844L, str3, str4, cvVar);
        this.j = new ArrayList();
    }

    public cr(String str, int i, String str2, char c, bm bmVar, String str3, BooleanSupplier booleanSupplier, char c2) {
        super(str, str2, bmVar, str3, booleanSupplier, ((((i << 32) | ((c << 48) >>> 32)) | ((c2 << 48) >>> 48)) ^ k) ^ 34710243145679L);
        this.j = new ArrayList();
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public cr(java.lang.String r14, java.lang.String r15, long r16, com.trossense.bm r18, java.lang.String r19, java.lang.String r20) {
        /*
            r13 = this;
            long r0 = com.trossense.cr.k
            long r0 = r0 ^ r16
            r2 = 117879076230155(0x6b35dc77140b, double:5.8240001928821E-310)
            long r0 = r0 ^ r2
            r2 = 32
            long r3 = r0 >>> r2
            int r9 = (int) r3
            long r0 = r0 << r2
            long r0 = r0 >>> r2
            int r10 = (int) r0
            r5 = r13
            r6 = r14
            r7 = r15
            r8 = r18
            r11 = r19
            r12 = r20
            r5.<init>(r6, r7, r8, r9, r10, r11, r12)
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = r13
            r1.j = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.cr.<init>(java.lang.String, java.lang.String, long, com.trossense.bm, java.lang.String, java.lang.String):void");
    }

    public cr(String str, String str2, long j, bm bmVar, String str3, String str4, BooleanSupplier booleanSupplier) {
        super(str, str2, bmVar, str3, str4, (k ^ j) ^ 98017419806730L, booleanSupplier);
        this.j = new ArrayList();
    }

    public cr(String str, String str2, bm bmVar, long j, String str3) {
        super(str, str2, (j ^ k) ^ 138846900471401L, bmVar, str3);
        this.j = new ArrayList();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public cr a(short s, int i, String str, char c, String str2) {
        long j = ((((c << 48) >>> 48) | ((s << 48) | ((i << 32) >>> 16))) ^ k) ^ 86388887885509L;
        this.j.add(new cu(str, str2));
        a((String) this.e, j);
        return this;
    }

    @Override // com.trossense.co
    public /* bridge */ /* synthetic */ void a(Object obj, char c, long j) {
        a((String) obj, (((j << 16) >>> 16) | (c << 48)) ^ 83081815219708L);
    }

    public void a(String str, long j) {
        long j2 = j ^ k;
        long j3 = 66722030422646L ^ j2;
        long j4 = j2 ^ 81251952175984L;
        int i = (int) (j4 >>> 48);
        long j5 = (j4 << 16) >>> 16;
        int b = b(str, j3);
        if (b == -1) {
            return;
        }
        this.i = b;
        super.a(str, (char) i, j5);
    }

    public int b(String str, long j) {
        long j2 = j ^ k;
        boolean h = co.h();
        if (this.j == null) {
            return -1;
        }
        int i = 0;
        while (i < this.j.size()) {
            cu cuVar = this.j.get(i);
            boolean equals = cuVar.b.equals(str);
            if (!h) {
                return equals ? 1 : 0;
            }
            if (!equals) {
                boolean equals2 = cuVar.a.equals(str);
                if (j2 >= 0) {
                    if (!equals2) {
                        i++;
                        equals2 = h;
                    }
                }
                if (!equals2) {
                    return -1;
                }
            }
            return i;
        }
        return -1;
    }

    @Override // com.trossense.co
    public /* bridge */ /* synthetic */ String e() {
        return i((short) (r0 >>> 48), (int) ((((k ^ 5307833645439L) ^ 2960811180030L) << 16) >>> 32), (short) ((r0 << 48) >>> 48));
    }

    public String i(short s, int i, short s2) {
        long j = ((((s << 48) | ((i << 32) >>> 16)) | ((s2 << 48) >>> 48)) ^ k) ^ 58893536589484L;
        int i2 = (int) (j >>> 32);
        int i3 = (int) ((j << 32) >>> 48);
        int i4 = (int) ((j << 48) >>> 48);
        int i5 = this.i;
        if (i5 == -1) {
            return null;
        }
        return this.j.get(i5).a(i2, (short) i3, i4);
    }

    public cu j(long j) {
        int i = this.i;
        if (i == -1) {
            return null;
        }
        return this.j.get(i);
    }

    public int k() {
        return this.i;
    }

    public List<cu> l() {
        return this.j;
    }
}
