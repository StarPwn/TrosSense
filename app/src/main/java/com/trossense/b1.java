package com.trossense;

import io.netty.handler.codec.rtsp.RtspHeaders;
import java.lang.invoke.MethodHandles;

@cg(a = RtspHeaders.Names.SPEED, b = b.Movement, c = "加速", d = 50)
/* loaded from: classes3.dex */
public class b1 extends bm {
    private static final long m = dj.a(5300980148886046293L, 8360920390921744389L, MethodHandles.lookup().lookupClass()).a(52265522105790L);
    private static final String[] n;
    private final ct j;
    private boolean k;
    private boolean l;

    static {
        char c = 2;
        String[] strArr = new String[2];
        int length = "遽廍\u00051\u001b:%Q".length();
        int i = 0;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = b(4, b("遽廍\u00051\u001b:%Q".substring(i3, i4)));
            if (i4 >= length) {
                n = strArr;
                return;
            } else {
                i2 = i4;
                c = "遽廍\u00051\u001b:%Q".charAt(i4);
                i = i5;
            }
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public b1(int r13, char r14, short r15) {
        /*
            r12 = this;
            long r0 = (long) r13
            r13 = 32
            long r0 = r0 << r13
            long r2 = (long) r14
            r14 = 48
            long r2 = r2 << r14
            long r2 = r2 >>> r13
            long r0 = r0 | r2
            long r2 = (long) r15
            long r2 = r2 << r14
            long r13 = r2 >>> r14
            long r13 = r13 | r0
            long r0 = com.trossense.b1.m
            long r13 = r13 ^ r0
            r0 = 121040282813562(0x6e15e30f847a, double:5.98018455010896E-310)
            long r6 = r13 ^ r0
            r0 = 26817908006174(0x186407e15d1e, double:1.32498070391815E-310)
            long r13 = r13 ^ r0
            r12.<init>(r13)
            com.trossense.ct r13 = new com.trossense.ct
            java.lang.String[] r14 = com.trossense.b1.n
            r15 = 1
            r3 = r14[r15]
            r0 = 0
            r4 = r14[r0]
            r1 = 4599075939470750515(0x3fd3333333333333, double:0.3)
            java.lang.Double r8 = java.lang.Double.valueOf(r1)
            java.lang.Integer r9 = java.lang.Integer.valueOf(r0)
            java.lang.Integer r10 = java.lang.Integer.valueOf(r15)
            r14 = 4576918229304087675(0x3f847ae147ae147b, double:0.01)
            java.lang.Double r11 = java.lang.Double.valueOf(r14)
            r2 = r13
            r5 = r12
            r2.<init>(r3, r4, r5, r6, r8, r9, r10, r11)
            r12.j = r13
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.b1.<init>(int, char, short):void");
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 102;
                    break;
                case 1:
                    i2 = 111;
                    break;
                case 2:
                    i2 = 91;
                    break;
                case 3:
                    i2 = 68;
                    break;
                case 4:
                    i2 = 49;
                    break;
                case 5:
                    i2 = 117;
                    break;
                default:
                    i2 = 113;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'q');
        }
        return charArray;
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x00ad, code lost:            if (r8 == null) goto L14;     */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0181  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0192  */
    /* JADX WARN: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    @com.trossense.bk
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(com.trossense.bg r33) {
        /*
            Method dump skipped, instructions count: 408
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.b1.a(com.trossense.bg):void");
    }
}
