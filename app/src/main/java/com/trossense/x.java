package com.trossense;

import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class x extends w {
    private static final long u = dj.a(-4894971743228205839L, -5040721728956276102L, MethodHandles.lookup().lookupClass()).a(214060988456283L);
    private static final String w = a(100, a("%k/\u0003}0\\#hf\u0000<>"));
    private boolean q;
    private float r;
    private float s;

    public x(float f, long j, float f2, float f3, float f4) {
        super(f, f2, f3, (u ^ j) ^ 101591699439215L, f4, w);
        this.q = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float a(x xVar) {
        return xVar.r;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float a(x xVar, float f) {
        xVar.r = f;
        return f;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 40;
                    break;
                case 1:
                    i2 = 98;
                    break;
                case 2:
                    i2 = 44;
                    break;
                case 3:
                    i2 = 20;
                    break;
                case 4:
                    i2 = 54;
                    break;
                case 5:
                    i2 = 61;
                    break;
                default:
                    i2 = 91;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(x xVar, boolean z) {
        xVar.q = z;
        return z;
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '[');
        }
        return charArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float b(x xVar) {
        return xVar.s;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float b(x xVar, float f) {
        xVar.s = f;
        return f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean c(x xVar) {
        return xVar.q;
    }

    @Override // com.trossense.p
    protected void c() {
        a(new av(this));
    }

    public boolean n() {
        return this.q;
    }
}
