package com.trossense;

import java.lang.invoke.MethodHandles;

@cg(a = "MoveBypass", b = b.Movement, c = "绕过花雨庭移动限制")
/* loaded from: classes3.dex */
public class bz extends bm {
    private static final long j = dj.a(-4129987650942418319L, -1223037384849343553L, MethodHandles.lookup().lookupClass()).a(281191035127169L);
    private static final String[] k;

    static {
        String[] strArr = new String[2];
        int length = "E\u00050vT\u00130c\r4\nE\u00050vT\u00137y\u0016!".length();
        int i = 0;
        char c = '\n';
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = b(85, b("E\u00050vT\u00130c\r4\nE\u00050vT\u00137y\u0016!".substring(i3, i4)));
            if (i4 >= length) {
                k = strArr;
                return;
            } else {
                i2 = i4;
                c = "E\u00050vT\u00130c\r4\nE\u00050vT\u00137y\u0016!".charAt(i4);
                i = i5;
            }
        }
    }

    public bz(long j2) {
        super((j2 ^ j) ^ 10328273317689L);
    }

    private static String b(int i, char[] cArr) {
        int length = cArr.length;
        for (int i2 = 0; length > i2; i2++) {
            char c = cArr[i2];
            int i3 = 96;
            switch (i2 % 7) {
                case 0:
                    i3 = 67;
                    break;
                case 1:
                    i3 = 53;
                    break;
                case 2:
                    i3 = 17;
                    break;
                case 3:
                case 4:
                    break;
                case 5:
                    i3 = 40;
                    break;
                default:
                    i3 = 47;
                    break;
            }
            cArr[i2] = (char) (c ^ (i ^ i3));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '/');
        }
        return charArray;
    }

    @bk
    public void a(ba baVar) {
        if (baVar.e() instanceof com.trossense.sdk.m) {
            String str = new String(((com.trossense.sdk.m) baVar.e()).b());
            String[] strArr = k;
            if (str.contains(strArr[0]) || str.contains(strArr[1])) {
                baVar.c();
            }
        }
    }
}
