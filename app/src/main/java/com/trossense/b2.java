package com.trossense;

import java.lang.invoke.MethodHandles;

@cg(a = "TargetStrafe", b = b.Movement, c = "环绕", d = 50)
/* loaded from: classes3.dex */
public class b2 extends bm {
    private static b2 l;
    private static String m;
    private static final long n = dj.a(-1283773982595764324L, 7294846413127923960L, MethodHandles.lookup().lookupClass()).a(170480010001913L);
    private static final String[] o;
    public final ct j;
    public final cp k;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x004a. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[4];
        b("S5JAsb");
        String str = "\u000eU/\u0003Y\rx)\u0006\u0010[(\u0001n\u001f";
        int length = "\u000eU/\u0003Y\rx)\u0006\u0010[(\u0001n\u001f".length();
        char c = '\b';
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 74;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String b = b(i5, c(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = b;
                        i4 = i6 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = "匀徶\u0002镋胾";
                        length = "匀徶\u0002镋胾".length();
                        c = 2;
                        i2 = -1;
                        i3 = i;
                        i5 = 66;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = b;
                i2 = i6 + c;
                if (i2 >= length) {
                    o = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 66;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i4);
            i3 = i;
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public b2(int r21, char r22, short r23) {
        /*
            r20 = this;
            r10 = r20
            r0 = r21
            long r0 = (long) r0
            r2 = 32
            long r0 = r0 << r2
            r3 = r22
            long r3 = (long) r3
            r5 = 48
            long r3 = r3 << r5
            long r2 = r3 >>> r2
            long r0 = r0 | r2
            r2 = r23
            long r2 = (long) r2
            long r2 = r2 << r5
            long r2 = r2 >>> r5
            long r0 = r0 | r2
            long r2 = com.trossense.b2.n
            long r0 = r0 ^ r2
            r2 = 122646982821589(0x6f8bf9d806d5, double:6.05956607782286E-310)
            long r6 = r0 ^ r2
            r2 = 28562022653873(0x19fa1d36dfb1, double:1.4111514169018E-310)
            long r2 = r2 ^ r0
            r8 = 105536221830147(0x5ffc11035c03, double:5.21418215981566E-310)
            long r0 = r0 ^ r8
            r4 = 16
            long r11 = r0 >>> r4
            long r0 = r0 << r5
            long r0 = r0 >>> r5
            int r13 = (int) r0
            r10.<init>(r2)
            com.trossense.ct r14 = new com.trossense.ct
            java.lang.String[] r15 = com.trossense.b2.o
            r16 = 1
            r1 = r15[r16]
            r0 = 2
            r2 = r15[r0]
            java.lang.Integer r8 = java.lang.Integer.valueOf(r0)
            r17 = 0
            java.lang.Integer r9 = java.lang.Integer.valueOf(r17)
            r0 = 5
            java.lang.Integer r18 = java.lang.Integer.valueOf(r0)
            r3 = 4591870180066957722(0x3fb999999999999a, double:0.1)
            java.lang.Double r19 = java.lang.Double.valueOf(r3)
            r0 = r14
            r3 = r20
            r4 = r6
            r6 = r8
            r7 = r9
            r8 = r18
            r9 = r19
            r0.<init>(r1, r2, r3, r4, r6, r7, r8, r9)
            r10.j = r14
            com.trossense.cp r8 = new com.trossense.cp
            short r3 = (short) r13
            r4 = r15[r17]
            r0 = 3
            r5 = r15[r0]
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r16)
            r0 = r8
            r1 = r11
            r6 = r20
            r0.<init>(r1, r3, r4, r5, r6, r7)
            r10.k = r8
            com.trossense.b2.l = r10
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.b2.<init>(int, char, short):void");
    }

    private static String b(int i, char[] cArr) {
        int length = cArr.length;
        for (int i2 = 0; length > i2; i2++) {
            char c = cArr[i2];
            int i3 = 81;
            switch (i2 % 7) {
                case 0:
                    i3 = 8;
                    break;
                case 1:
                    i3 = 112;
                    break;
                case 2:
                    i3 = 6;
                    break;
                case 3:
                    i3 = 34;
                    break;
                case 5:
                    i3 = 38;
                    break;
            }
            cArr[i2] = (char) (c ^ (i ^ i3));
        }
        return new String(cArr).intern();
    }

    public static void b(String str) {
        m = str;
    }

    private static char[] c(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'Q');
        }
        return charArray;
    }

    public static b2 n() {
        return l;
    }

    public static String o() {
        return m;
    }
}
