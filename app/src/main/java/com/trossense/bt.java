package com.trossense;

import java.lang.invoke.MethodHandles;

@cg(a = "AntiBot", b = b.Combat, c = "反假人")
/* loaded from: classes3.dex */
public class bt extends bm {
    private static bt j;
    private static String[] l;
    private static final long m = dj.a(7906015350236700901L, 2755175938667027956L, MethodHandles.lookup().lookupClass()).a(173342844928952L);
    private static final String[] n;
    public final cp k;

    static {
        String[] strArr = new String[2];
        a((String[]) null);
        int length = "帓呃岳\u0003R\u0007\u007f".length();
        int i = 0;
        char c = 3;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = b(24, b("帓呃岳\u0003R\u0007\u007f".substring(i3, i4)));
            if (i4 >= length) {
                n = strArr;
                return;
            } else {
                i2 = i4;
                c = "帓呃岳\u0003R\u0007\u007f".charAt(i4);
                i = i5;
            }
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public bt(long r12) {
        /*
            r11 = this;
            long r0 = com.trossense.bt.m
            long r12 = r12 ^ r0
            r0 = 61819134328166(0x3839638abd66, double:3.0542710527192E-310)
            long r0 = r0 ^ r12
            r2 = 138810922843860(0x7e3f6fbf3ed4, double:6.8581708244671E-310)
            long r12 = r12 ^ r2
            r2 = 16
            long r4 = r12 >>> r2
            r2 = 48
            long r12 = r12 << r2
            long r12 = r12 >>> r2
            int r12 = (int) r12
            java.lang.String[] r13 = o()
            r11.<init>(r0)
            com.trossense.cp r0 = new com.trossense.cp
            short r6 = (short) r12
            java.lang.String[] r12 = com.trossense.bt.n
            r1 = 1
            r7 = r12[r1]
            r2 = 0
            r8 = r12[r2]
            java.lang.Boolean r10 = java.lang.Boolean.valueOf(r1)
            r3 = r0
            r9 = r11
            r3.<init>(r4, r6, r7, r8, r9, r10)
            r11.k = r0
            com.trossense.bt.j = r11
            if (r13 == 0) goto L3f
            r12 = 3
            int[] r12 = new int[r12]
            com.trossense.sdk.PointerHolder.b(r12)
        L3f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.bt.<init>(long):void");
    }

    public static void a(String[] strArr) {
        l = strArr;
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 8;
                    break;
                case 1:
                    i2 = 82;
                    break;
                case 2:
                    i2 = 48;
                    break;
                case 3:
                    i2 = 27;
                    break;
                case 4:
                    i2 = 38;
                    break;
                case 5:
                    i2 = 3;
                    break;
                default:
                    i2 = 33;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '!');
        }
        return charArray;
    }

    public static bt n() {
        return j;
    }

    public static String[] o() {
        return l;
    }
}
