package com.trossense;

import android.graphics.Color;
import com.google.common.base.Ascii;
import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class v extends q {
    private static final long u = dj.a(4886030142210516831L, 4713115428969827211L, MethodHandles.lookup().lookupClass()).a(227440038478028L);
    private static final String[] v;
    private df o;
    private df p;
    private aj q;
    public boolean r;
    final aj s;

    static {
        String[] strArr = new String[2];
        int length = "N4L\u0003\"0L".length();
        int i = 0;
        char c = 3;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = a(46, a("N4L\u0003\"0L".substring(i3, i4)));
            if (i4 >= length) {
                v = strArr;
                return;
            } else {
                i2 = i4;
                c = "N4L\u0003\"0L".charAt(i4);
                i = i5;
            }
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public v(com.trossense.aj r19, short r20, com.trossense.aj r21, long r22) {
        /*
            r18 = this;
            r7 = r18
            r8 = r19
            r9 = r21
            r0 = r20
            long r0 = (long) r0
            r2 = 48
            long r0 = r0 << r2
            r2 = 16
            long r3 = r22 << r2
            long r2 = r3 >>> r2
            long r0 = r0 | r2
            long r2 = com.trossense.v.u
            long r0 = r0 ^ r2
            r2 = 87872738668381(0x4feb775faf5d, double:4.34149013820327E-310)
            long r2 = r2 ^ r0
            r4 = 122846666952161(0x6fba77f20de1, double:6.0694317847164E-310)
            long r16 = r0 ^ r4
            r7.s = r8
            float r0 = com.trossense.aj.a(r21)
            r1 = 1101004800(0x41a00000, float:20.0)
            float r5 = r0 - r1
            r4 = 0
            r6 = 0
            r10 = 1101004800(0x41a00000, float:20.0)
            r0 = r18
            r1 = r2
            r3 = r4
            r4 = r6
            r6 = r10
            r0.<init>(r1, r3, r4, r5, r6)
            com.trossense.df r0 = new com.trossense.df
            com.trossense.d r11 = com.trossense.d.SMOOTH
            r12 = 200(0xc8, double:9.9E-322)
            r10 = r0
            r14 = r16
            r10.<init>(r11, r12, r14)
            r7.o = r0
            com.trossense.df r0 = new com.trossense.df
            com.trossense.d r11 = com.trossense.d.Decelerate
            r12 = 250(0xfa, double:1.235E-321)
            r10 = r0
            r10.<init>(r11, r12, r14)
            r7.p = r0
            r7.q = r9
            com.trossense.at r0 = new com.trossense.at
            r0.<init>(r7, r8, r9)
            r7.a(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.v.<init>(com.trossense.aj, short, com.trossense.aj, long):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static df a(v vVar) {
        return vVar.p;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 80;
                    break;
                case 1:
                    i2 = 48;
                    break;
                case 2:
                    i2 = 70;
                    break;
                case 3:
                    i2 = 21;
                    break;
                case 4:
                    i2 = 34;
                    break;
                case 5:
                    i2 = 29;
                    break;
                default:
                    i2 = 127;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ Ascii.MAX);
        }
        return charArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float b(v vVar) {
        return vVar.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float c(v vVar) {
        return vVar.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float d(v vVar) {
        return vVar.d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float e(v vVar) {
        return vVar.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float f(v vVar) {
        return vVar.d;
    }

    @Override // com.trossense.p
    public void d(long j) {
        float f;
        float f2;
        long j2 = j ^ 100102215916871L;
        long j3 = j ^ 114627851269427L;
        int i = (int) (j3 >>> 48);
        int i2 = (int) ((j3 << 16) >>> 48);
        int i3 = (int) ((j3 << 32) >>> 32);
        long j4 = j ^ 111589528524585L;
        long j5 = j ^ 91228529291838L;
        long j6 = j ^ 121281231864540L;
        int c = (int) (aj.c(this.q) * 255.0f);
        da.a(this.a, this.b, j4, this.d, this.e, this.e / 2.0f, c9.a(Color.argb(c, 45, 45, 45), Color.argb(c, 80, 80, 80), this.p.b()));
        float floatValue = (aj.b(this.q).e().floatValue() - aj.b(this.q).k().floatValue()) / (aj.b(this.q).j().floatValue() - aj.b(this.q).k().floatValue());
        this.o.a(floatValue, j ^ 105732733789211L);
        String[] m = ae.m();
        float b = this.o.b();
        da.a(this.a, this.b, j4, this.d * b, this.e, this.e / 2.0f, Color.argb(c, Color.red(ad.u), Color.green(ad.u), Color.blue(ad.u)));
        da.a(j2, this.a + (this.d * b), this.b + 10.0f, 18.0f, Color.argb(c, 32, 32, 32));
        da.a(j2, this.a + (this.d * b), this.b + 10.0f, 14.0f, Color.argb(c, 255, 255, 255));
        String valueOf = String.valueOf(aj.a(aj.b(this.q).e().doubleValue(), (char) i, 2, (char) i2, i3));
        if (valueOf.contains(".")) {
            String[] strArr = v;
            valueOf = valueOf.replaceAll(strArr[0], "").replaceAll(strArr[1], "");
        }
        float a = (this.a + (b * this.d)) - (aj.n().a(j5, valueOf) / 4.0f);
        float a2 = ((aj.n().a(j5, valueOf) / 2.0f) + a) - 6.0f;
        float f3 = this.a + this.d;
        if (j > 0) {
            if (a2 > f3) {
                a = ((this.a + this.d) - (aj.n().a(j5, valueOf) / 2.0f)) + 6.0f;
            }
            f = a - 5.0f;
            f2 = this.b + this.e + 13.0f;
        } else {
            f = a2;
            f2 = f3;
        }
        da.a(f, f2, j4, (aj.n().a(j5, valueOf) / 2.0f) + 10.0f, 24.0f, 8.0f, Color.argb((int) (this.p.b() * 255.0f * aj.c(this.q)), 45, 45, 45));
        aj.n().a(valueOf, j6, a, this.b + this.e + 15.0f, 20.0f, 0.5f, Color.argb((int) (this.p.b() * 255.0f * aj.c(this.q)), 255, 255, 255));
        if (m == null) {
            PointerHolder.b(new int[1]);
        }
    }
}
