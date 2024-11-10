package com.trossense;

import android.graphics.Color;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class al extends p {
    private static az o;
    private static final long v;
    private static final String[] w;
    private String p;
    private String q;
    private boolean r;
    private boolean s;
    public df t;
    public df u;

    static {
        long a = dj.a(1358601887489622442L, -9042692541995574301L, MethodHandles.lookup().lookupClass()).a(241172647531830L);
        v = a;
        long j = (a ^ 83237875398952L) ^ 72454917381894L;
        String[] strArr = new String[3];
        int length = "j\u001b\fj\u0019Q`gP\u0016\u007f\u001c\u0007\\窧肬采絚宥安\tJ\f\rx)]zm\u001b".length();
        char c = '\f';
        int i = -1;
        int i2 = 0;
        while (true) {
            int i3 = i + 1;
            int i4 = c + i3;
            int i5 = i2 + 1;
            strArr[i2] = a(119, a("j\u001b\fj\u0019Q`gP\u0016\u007f\u001c\u0007\\窧肬采絚宥安\tJ\f\rx)]zm\u001b".substring(i3, i4)));
            if (i4 >= length) {
                w = strArr;
                o = a1.a(strArr[0], 50, j);
                return;
            } else {
                i2 = i5;
                i = i4;
                c = "j\u001b\fj\u0019Q`gP\u0016\u007f\u001c\u0007\\窧肬采絚宥安\tJ\f\rx)]zm\u001b".charAt(i4);
            }
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public al(int i, byte b, int i2) {
        super(0.0f, 0.0f);
        long j = (((((b << 56) >>> 32) | (i << 32)) | ((i2 << 40) >>> 40)) ^ v) ^ 51735288307643L;
        String[] strArr = w;
        this.p = strArr[2];
        this.q = strArr[1];
        this.r = false;
        this.s = false;
        this.t = new df(d.LINEAR, 2500L, j);
        this.u = new df(d.LINEAR, 1500L, j);
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 105;
                    break;
                case 1:
                    i2 = 9;
                    break;
                case 2:
                    i2 = 21;
                    break;
                case 3:
                    i2 = 124;
                    break;
                case 4:
                    i2 = 13;
                    break;
                case 5:
                    i2 = 79;
                    break;
                default:
                    i2 = 99;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'c');
        }
        return charArray;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    @Override // com.trossense.p, com.trossense.aq
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(long r9) {
        /*
            r8 = this;
            r0 = 39159558870452(0x239d8b9895b4, double:1.9347392744188E-310)
            long r0 = r0 ^ r9
            r2 = 19020623741841(0x114c95858391, double:9.397436753316E-311)
            long r2 = r2 ^ r9
            com.trossense.az r4 = com.trossense.al.o
            java.lang.String r5 = r8.q
            float r4 = r4.a(r2, r5)
            com.trossense.az r5 = com.trossense.al.o
            java.lang.String r6 = r8.p
            float r2 = r5.a(r2, r6)
            float r2 = java.lang.Math.max(r4, r2)
            r8.d = r2
            com.trossense.az r2 = com.trossense.al.o
            float r2 = r2.a()
            r3 = 1073741824(0x40000000, float:2.0)
            float r2 = r2 * r3
            r8.e = r2
            boolean r2 = com.trossense.w.l()
            float r3 = r8.e
            r4 = 1149698048(0x44870000, float:1080.0)
            float r4 = r4 - r3
            float r3 = r8.d
            r5 = 1156579328(0x44f00000, float:1920.0)
            float r5 = r5 - r3
            com.trossense.df r3 = r8.t
            boolean r6 = r8.r
            r7 = 0
            if (r6 == 0) goto L43
            r5 = r7
        L43:
            r3.a(r5, r0)
            com.trossense.df r3 = r8.u
            boolean r5 = r8.s
            if (r5 == 0) goto L4d
            r4 = r7
        L4d:
            r3.a(r4, r0)
            com.trossense.df r0 = r8.t
            boolean r0 = r0.d()
            r3 = 0
            int r9 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1))
            r10 = 1
            r1 = 0
            if (r9 <= 0) goto L73
            if (r0 == 0) goto L6b
            boolean r0 = r8.r
            if (r9 < 0) goto L69
            if (r0 != 0) goto L68
            r0 = r10
            goto L69
        L68:
            r0 = r1
        L69:
            r8.r = r0
        L6b:
            if (r9 < 0) goto L88
            com.trossense.df r0 = r8.u
            boolean r0 = r0.d()
        L73:
            if (r0 == 0) goto L80
            boolean r0 = r8.s
            if (r9 <= 0) goto L7e
            if (r0 != 0) goto L7d
            r0 = r10
            goto L7e
        L7d:
            r0 = r1
        L7e:
            r8.s = r0
        L80:
            com.trossense.df r0 = r8.t
            float r0 = r0.b()
            r8.a = r0
        L88:
            com.trossense.df r0 = r8.u
            float r0 = r0.b()
            r8.b = r0
            int[] r0 = com.trossense.sdk.PointerHolder.s()
            if (r0 != 0) goto L9f
            if (r9 <= 0) goto L9b
            if (r2 == 0) goto L9c
            r2 = r1
        L9b:
            r10 = r2
        L9c:
            com.trossense.w.c(r10)
        L9f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.al.a(long):void");
    }

    @Override // com.trossense.p
    public void d(long j) {
        long j2 = j ^ 75602871466904L;
        o.a(this.p, this.a + (this.d / 2.0f), this.b, Color.argb(122, 122, 122, 122), j2);
        o.a(this.q, (this.d / 2.0f) + this.a, 10.0f + this.b + o.a(), Color.argb(122, 122, 122, 122), j2);
    }
}
