package com.trossense;

import android.graphics.Color;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class z extends p {
    private static final String q = a(5, a("7qwB\u0005yU=wuC\u0004yR9"));
    private ag o;
    final ag p;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public z(ag agVar, ag agVar2) {
        super(0.0f, 0.0f);
        this.p = agVar;
        this.o = agVar2;
        this.d = ag.c(agVar2) - 20.0f;
        a(new as(this, agVar, agVar2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float a(z zVar) {
        return zVar.a;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 91;
                    break;
                case 1:
                    i2 = 25;
                    break;
                case 2:
                    i2 = 21;
                    break;
                case 3:
                    i2 = 52;
                    break;
                case 4:
                    i2 = 47;
                    break;
                case 5:
                    i2 = 12;
                    break;
                default:
                    i2 = 57;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '9');
        }
        return charArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float b(z zVar) {
        return zVar.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float c(z zVar) {
        return zVar.d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float d(z zVar) {
        return zVar.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float e(z zVar) {
        return zVar.d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float f(z zVar) {
        return zVar.b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float g(z zVar) {
        return zVar.b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float h(z zVar) {
        return zVar.e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float i(z zVar) {
        return zVar.b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float j(z zVar) {
        return zVar.e;
    }

    @Override // com.trossense.p
    public void d(long j) {
        int i = (int) (((j ^ 116692849973949L) << 16) >>> 32);
        int b = (int) (this.o.A.b() * 255.0f);
        float[] fArr = {ag.b(this.o).p(), ag.b(this.o).q(), ag.b(this.o).o()};
        char c = (char) (r1 >>> 48);
        short s = (short) ((r1 << 48) >>> 48);
        int a = c9.a(fArr[0], c, i, 1.0f, 1.0f, s);
        int a2 = c9.a(fArr[0], c, i, 0.0f, 1.0f, s);
        int a3 = c9.a(fArr[0], c, i, 1.0f, 0.0f, s);
        da.a(this.a, this.b, j ^ 111589528524585L, this.d, this.e, 10.0f, Color.argb(b, Color.red(a), Color.green(a), Color.blue(a)));
        da.a(this.a, this.b, this.d, this.e, 10.0f, j ^ 124618597675762L, Color.argb(b, Color.red(a2), Color.green(a2), Color.blue(a2)), Color.argb(0, Color.red(a2), Color.green(a2), Color.blue(a2)), false);
        da.a(this.a, this.b, this.d, this.e, 10.0f, j ^ 84803153043256L, Color.argb(0, Color.red(a3), Color.green(a3), Color.blue(a3)), Color.argb(b, Color.red(a3), Color.green(a3), Color.blue(a3)));
        da.a(q, (this.a + (this.d * fArr[1])) - 7.5f, (this.b + (this.e * (1.0f - fArr[2]))) - 7.5f, 15.0f, j ^ 56614365134728L, 15.0f, Color.argb(b, 255, 255, 255));
    }
}
