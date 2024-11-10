package com.trossense;

import java.lang.invoke.MethodHandles;

@cg(a = "Team", b = b.Combat, c = "团队")
/* loaded from: classes3.dex */
public class br extends bm {
    private static br j;
    private static int[] m;
    private static final long n = dj.a(-5199049852207026223L, -5111689036688634335L, MethodHandles.lookup().lookupClass()).a(75044559149838L);
    private static final String[] o;
    public final cp k;
    public final cp l;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x004a. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[4];
        String str = "FR0$&\u000ephL29\u0004裂大飁船";
        int length = "FR0$&\u000ephL29\u0004裂大飁船".length();
        b(new int[1]);
        char c = 11;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 2;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String b = b(i5, b(substring));
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
                        str = "帍吠峏稸峆\u0006Ld\u0003b\u000ep";
                        length = "帍吠峏稸峆\u0006Ld\u0003b\u000ep".length();
                        c = 5;
                        i3 = i;
                        i2 = -1;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        i5 = 11;
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
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                i5 = 11;
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
    public br(long r14) {
        /*
            r13 = this;
            long r0 = com.trossense.br.n
            long r14 = r14 ^ r0
            r0 = 82741444334344(0x4b40be65d308, double:4.0879705132885E-310)
            long r0 = r0 ^ r14
            r2 = 14597290479802(0xd46b25050ba, double:7.2120197484357E-311)
            long r14 = r14 ^ r2
            r2 = 16
            long r11 = r14 >>> r2
            r2 = 48
            long r14 = r14 << r2
            long r14 = r14 >>> r2
            int r14 = (int) r14
            r13.<init>(r0)
            com.trossense.cp r15 = new com.trossense.cp
            short r14 = (short) r14
            java.lang.String[] r0 = com.trossense.br.o
            r1 = 3
            r7 = r0[r1]
            r1 = 2
            r8 = r0[r1]
            r1 = 0
            java.lang.Boolean r10 = java.lang.Boolean.valueOf(r1)
            r3 = r15
            r4 = r11
            r6 = r14
            r9 = r13
            r3.<init>(r4, r6, r7, r8, r9, r10)
            r13.k = r15
            com.trossense.cp r15 = new com.trossense.cp
            r7 = r0[r1]
            r1 = 1
            r8 = r0[r1]
            java.lang.Boolean r10 = java.lang.Boolean.valueOf(r1)
            r3 = r15
            r3.<init>(r4, r6, r7, r8, r9, r10)
            r13.l = r15
            com.trossense.br.j = r13
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.br.<init>(long):void");
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 5;
                    break;
                case 1:
                    i2 = 34;
                    break;
                case 2:
                    i2 = 95;
                    break;
                case 3:
                    i2 = 73;
                    break;
                case 4:
                    i2 = 86;
                    break;
                case 5:
                    i2 = 44;
                    break;
                default:
                    i2 = 49;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    public static void b(int[] iArr) {
        m = iArr;
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '1');
        }
        return charArray;
    }

    public static br n() {
        return j;
    }

    public static int[] o() {
        return m;
    }
}
