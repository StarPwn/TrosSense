package com.trossense.sdk.math;

import io.netty.util.internal.StringUtil;

/* loaded from: classes3.dex */
public class Vec3f {
    private static final String[] a;
    private static int[] b;
    public final float x;
    public final float y;
    public final float z;

    static {
        String[] strArr = new String[3];
        int length = "=mXGd!mV\u0004G(BI\u0004G(AI".length();
        b(new int[4]);
        int i = 0;
        char c = '\b';
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = a(57, a("=mXGd!mV\u0004G(BI\u0004G(AI".substring(i3, i4)));
            if (i4 >= length) {
                a = strArr;
                return;
            } else {
                i2 = i4;
                c = "=mXGd!mV\u0004G(BI\u0004G(AI".charAt(i4);
                i = i5;
            }
        }
    }

    public Vec3f(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.z = f3;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 82;
                    break;
                case 1:
                    i2 = 49;
                    break;
                case 2:
                    i2 = 2;
                    break;
                case 3:
                    i2 = 77;
                    break;
                case 4:
                    i2 = 59;
                    break;
                case 5:
                    i2 = 99;
                    break;
                default:
                    i2 = 44;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ StringUtil.COMMA);
        }
        return charArray;
    }

    public static void b(int[] iArr) {
        b = iArr;
    }

    public static int[] b() {
        return b;
    }

    public Vec3f add(float f, float f2, float f3) {
        return new Vec3f(this.x + f, this.y + f2, this.z + f3);
    }

    public Vec3f sub(float f, float f2, float f3) {
        return new Vec3f(this.x - f, this.y - f2, this.z - f3);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String[] strArr = a;
        return sb.append(strArr[0]).append(this.x).append(strArr[1]).append(this.y).append(strArr[2]).append(this.z).append('}').toString();
    }
}
