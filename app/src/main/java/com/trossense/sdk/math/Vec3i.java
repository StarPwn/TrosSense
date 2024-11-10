package com.trossense.sdk.math;

import com.trossense.dj;
import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class Vec3i {
    private static final long a = dj.a(-7791472683054789173L, -2471182786130924075L, MethodHandles.lookup().lookupClass()).a(192768638051139L);
    private static final String[] b;
    public final int x;
    public final int y;
    public final int z;

    static {
        String[] strArr = new String[3];
        int length = "o\u007f[}#5\u0012\u0004\u0004\u0015:As\u0004\u0015:Bs".length();
        int i = 0;
        char c = '\b';
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = a(126, a("o\u007f[}#5\u0012\u0004\u0004\u0015:As\u0004\u0015:Bs".substring(i3, i4)));
            if (i4 >= length) {
                b = strArr;
                return;
            } else {
                i2 = i4;
                c = "o\u007f[}#5\u0012\u0004\u0004\u0015:As\u0004\u0015:Bs".charAt(i4);
                i = i5;
            }
        }
    }

    public Vec3i(int i, int i2, int i3) {
        this.x = i;
        this.y = i2;
        this.z = i3;
    }

    private static String a(int i, char[] cArr) {
        int length = cArr.length;
        for (int i2 = 0; length > i2; i2++) {
            char c = cArr[i2];
            int i3 = 48;
            switch (i2 % 7) {
                case 0:
                    i3 = 71;
                    break;
                case 1:
                    i3 = 100;
                    break;
                case 2:
                    i3 = 70;
                    break;
                case 3:
                case 5:
                    break;
                case 4:
                    i3 = 52;
                    break;
                default:
                    i3 = 20;
                    break;
            }
            cArr[i2] = (char) (c ^ (i ^ i3));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 20);
        }
        return charArray;
    }

    public String toString() {
        Vec3f.b();
        StringBuilder sb = new StringBuilder();
        String[] strArr = b;
        String sb2 = sb.append(strArr[0]).append(this.x).append(strArr[1]).append(this.y).append(strArr[2]).append(this.z).append('}').toString();
        if (PointerHolder.s() == null) {
            Vec3f.b(new int[3]);
        }
        return sb2;
    }
}
