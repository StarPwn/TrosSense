package com.trossense;

import android.graphics.Color;
import java.lang.invoke.MethodHandles;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class s extends q {
    private static final long q = dj.a(8755769578857777051L, 6232664953492104931L, MethodHandles.lookup().lookupClass()).a(46252151964206L);
    private static final String r = a(48, a("\u0016W'[Kw\u0017\u001a\u00140F\u0003"));
    private ag o;
    final ag p;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public s(com.trossense.ag r10, com.trossense.ag r11, long r12) {
        /*
            r9 = this;
            long r0 = com.trossense.s.q
            long r12 = r12 ^ r0
            r0 = 122143915513034(0x6f16d8b214ca, double:6.0347112503526E-310)
            long r3 = r12 ^ r0
            r9.p = r10
            float r12 = com.trossense.ag.d(r11)
            r13 = 1101004800(0x41a00000, float:20.0)
            float r7 = r12 - r13
            r5 = 0
            r6 = 0
            r8 = 1101004800(0x41a00000, float:20.0)
            r2 = r9
            r2.<init>(r3, r5, r6, r7, r8)
            r9.o = r11
            com.trossense.ar r12 = new com.trossense.ar
            r12.<init>(r9, r10, r11)
            r9.a(r12)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.s.<init>(com.trossense.ag, com.trossense.ag, long):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float a(s sVar) {
        return sVar.a;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 79;
                    break;
                case 1:
                    i2 = 10;
                    break;
                case 2:
                    i2 = 112;
                    break;
                case 3:
                    i2 = 24;
                    break;
                case 4:
                    i2 = 84;
                    break;
                case 5:
                    i2 = 47;
                    break;
                default:
                    i2 = 82;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'R');
        }
        return charArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float b(s sVar) {
        return sVar.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float c(s sVar) {
        return sVar.d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float d(s sVar) {
        return sVar.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float e(s sVar) {
        return sVar.d;
    }

    @Override // com.trossense.p
    public void d(long j) {
        long j2 = j ^ 100102215916871L;
        int b = (int) (this.o.A.b() * 255.0f);
        float p = ag.b(this.o).p();
        int a = c9.a(p, (char) (r1 >>> 48), (int) (((j ^ 116692849973949L) << 16) >>> 32), 1.0f, 1.0f, (short) ((r1 << 48) >>> 48));
        da.a(r, this.a, this.b, this.d, this.e, this.e / 2.0f, Color.argb(b, 255, 255, 255), j ^ 86657065868312L);
        da.a(j2, this.a + (this.d * p), this.b + 10.0f, 15.0f, Color.argb(b, 255, 255, 255));
        da.a(j2, this.a + (this.d * p), this.b + 10.0f, 10.0f, Color.argb(b, Color.red(a), Color.green(a), Color.blue(a)));
    }
}
