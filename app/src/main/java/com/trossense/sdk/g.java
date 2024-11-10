package com.trossense.sdk;

/* loaded from: classes3.dex */
public class g extends f {
    private static final String[] g;

    static {
        String[] strArr = new String[2];
        int length = "[\u0019)\u0012\u0011X\u001f)\u0012\u0017\u0018~S\u0002}\u0002\u001a\u0001qZ\u0001)".length();
        int i = 0;
        char c = 4;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = a(8, a("[\u0019)\u0012\u0011X\u001f)\u0012\u0017\u0018~S\u0002}\u0002\u001a\u0001qZ\u0001)".substring(i3, i4)));
            if (i4 >= length) {
                g = strArr;
                return;
            } else {
                i2 = i4;
                c = "[\u0019)\u0012\u0011X\u001f)\u0012\u0017\u0018~S\u0002}\u0002\u001a\u0001qZ\u0001)".charAt(i4);
                i = i5;
            }
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public g(int r9) {
        /*
            r8 = this;
            java.lang.String[] r0 = com.trossense.sdk.g.g
            r1 = 1
            r4 = r0[r1]
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            r1 = 0
            r7 = r0[r1]
            r6 = 0
            r2 = r8
            r3 = r9
            r2.<init>(r3, r4, r5, r6, r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.sdk.g.<init>(int):void");
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 61;
                    break;
                case 1:
                    i2 = 126;
                    break;
                case 2:
                    i2 = 79;
                    break;
                case 3:
                    i2 = 127;
                    break;
                case 4:
                    i2 = 124;
                    break;
                case 5:
                    i2 = 98;
                    break;
                default:
                    i2 = 23;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 23);
        }
        return charArray;
    }
}
