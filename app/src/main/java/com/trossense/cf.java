package com.trossense;

import com.trossense.sdk.entity.type.EntityLocalPlayer;
import java.lang.invoke.MethodHandles;

@cg(a = "Timer", b = b.World, c = "变速")
/* loaded from: classes3.dex */
public class cf extends bm {
    private static final long k = dj.a(7289704487268616760L, 2116665539489360859L, MethodHandles.lookup().lookupClass()).a(277351582936L);
    private static final String[] l;
    private final ct j;

    static {
        char c = 2;
        String[] strArr = new String[2];
        int length = "遫廟\u0005 \u0010\u0015(;".length();
        int i = 0;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = b(76, b("遫廟\u0005 \u0010\u0015(;".substring(i3, i4)));
            if (i4 >= length) {
                l = strArr;
                return;
            } else {
                i2 = i4;
                c = "遫廟\u0005 \u0010\u0015(;".charAt(i4);
                i = i5;
            }
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public cf(long r13) {
        /*
            r12 = this;
            long r0 = com.trossense.cf.k
            long r13 = r13 ^ r0
            r0 = 129900537481176(0x7624d36487d8, double:6.41793929457623E-310)
            long r6 = r13 ^ r0
            r0 = 366004035260(0x55378a5ebc, double:1.80830020061E-312)
            long r13 = r13 ^ r0
            com.trossense.ca.n()
            r12.<init>(r13)
            com.trossense.ct r13 = new com.trossense.ct
            java.lang.String[] r14 = com.trossense.cf.l
            r0 = 1
            r3 = r14[r0]
            r1 = 0
            r4 = r14[r1]
            r14 = 40
            java.lang.Integer r8 = java.lang.Integer.valueOf(r14)
            java.lang.Integer r9 = java.lang.Integer.valueOf(r0)
            r14 = 100
            java.lang.Integer r10 = java.lang.Integer.valueOf(r14)
            r0 = 4591870180066957722(0x3fb999999999999a, double:0.1)
            java.lang.Double r11 = java.lang.Double.valueOf(r0)
            r2 = r13
            r5 = r12
            r2.<init>(r3, r4, r5, r6, r8, r9, r10, r11)
            r12.j = r13
            int[] r13 = com.trossense.sdk.PointerHolder.s()
            if (r13 != 0) goto L4c
            r13 = 4
            int[] r13 = new int[r13]
            com.trossense.ca.b(r13)
        L4c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.cf.<init>(long):void");
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 56;
                    break;
                case 1:
                    i2 = 53;
                    break;
                case 2:
                    i2 = 52;
                    break;
                case 3:
                    i2 = 1;
                    break;
                case 4:
                    i2 = 5;
                    break;
                case 5:
                    i2 = 27;
                    break;
                default:
                    i2 = 118;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'v');
        }
        return charArray;
    }

    @Override // com.trossense.bm
    public void j(long j) {
        EntityLocalPlayer.g(this.j.e().floatValue());
    }

    @Override // com.trossense.bm
    public void k(long j) {
        EntityLocalPlayer.g(20.0f);
    }
}
