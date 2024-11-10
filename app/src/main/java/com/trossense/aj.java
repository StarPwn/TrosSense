package com.trossense;

import android.graphics.Color;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.math.RoundingMode;

/* loaded from: classes3.dex */
public class aj extends ae {
    private static az r;
    private static az s;
    private static az t;
    private static final long w;
    private static final String[] x;
    private float q;
    private v u;
    private ct v;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0051. Please report as an issue. */
    static {
        int i;
        int i2;
        long a = dj.a(-4486046464520371656L, -4616354965749980214L, MethodHandles.lookup().lookupClass()).a(227183917664346L);
        w = a;
        long j = (a ^ 19916145714791L) ^ 27506012773032L;
        String[] strArr = new String[6];
        String str = "z\u001fYHRCrwWUF]N(z\u000eQ\u00024Z\u0011z\u001fYHRCrwWUF]N(z\u000eQ\u0003RT\u0013";
        int length = "z\u001fYHRCrwWUF]N(z\u000eQ\u00024Z\u0011z\u001fYHRCrwWUF]N(z\u000eQ\u0003RT\u0013".length();
        char c = 17;
        int i3 = -1;
        int i4 = 0;
        while (true) {
            int i5 = 114;
            int i6 = i3 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String a2 = a(i5, a(substring));
                i = i4 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i4] = a2;
                        i3 = i6 + c;
                        if (i3 < length) {
                            break;
                        }
                        str = "j\u000fIXBSbgDSMG\u0003.@\u0003";
                        length = "j\u000fIXBSbgDSMG\u0003.@\u0003".length();
                        c = '\f';
                        i2 = -1;
                        i4 = i;
                        i5 = 98;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i4] = a2;
                i2 = i6 + c;
                if (i2 >= length) {
                    x = strArr;
                    r = a1.a(strArr[4], 128, j);
                    s = a1.a(strArr[0], 128, j);
                    t = a1.a(strArr[2], 56, j);
                    return;
                }
                c = str.charAt(i2);
                i4 = i;
                i5 = 98;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i3);
            i4 = i;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public aj(ct ctVar, long j) {
        super(0.0f, 0.0f, ctVar);
        long j2 = (j ^ w) ^ 48694214891376L;
        this.d = 360.0f;
        this.v = ctVar;
        v vVar = new v(this, (short) (j2 >>> 48), this, (j2 << 16) >>> 16);
        this.u = vVar;
        a(vVar);
        this.q = 1.0f;
    }

    public static double a(double d, char c, int i, char c2, int i2) {
        if (i >= 0) {
            return new BigDecimal(d).setScale(i, RoundingMode.HALF_UP).doubleValue();
        }
        throw new IllegalArgumentException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float a(aj ajVar) {
        return ajVar.d;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 124;
                    break;
                case 1:
                    i2 = 8;
                    break;
                case 2:
                    i2 = 69;
                    break;
                case 3:
                    i2 = 91;
                    break;
                case 4:
                    i2 = 67;
                    break;
                case 5:
                    i2 = 88;
                    break;
                default:
                    i2 = 116;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 't');
        }
        return charArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ct b(aj ajVar) {
        return ajVar.v;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float c(aj ajVar) {
        return ajVar.q;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static az n() {
        return t;
    }

    @Override // com.trossense.p, com.trossense.aq
    public void a(long j) {
        long j2 = 39159558870452L ^ j;
        long j3 = j ^ 89014590124158L;
        this.e = 80.0f;
        v.a(this.u).a(this.u.r ? 1.0f : 0.0f, j2);
        this.e += v.a(this.u).b() * 20.0f;
        this.u.b(j3, this.a + 10.0f, this.b + 40.0f);
    }

    @Override // com.trossense.p
    public void d(long j) {
        long j2 = j ^ 37087146222550L;
        long j3 = j ^ 114627851269427L;
        int i = (int) (j3 >>> 48);
        int i2 = (int) ((j3 << 16) >>> 48);
        int i3 = (int) ((j3 << 32) >>> 32);
        long j4 = j ^ 0;
        long j5 = j ^ 121281231864540L;
        long j6 = j ^ 91228529291838L;
        r.a(this.v.c(j2), j5, this.a + 20.0f, this.b + 15.0f, 5.0f, 0.25f, Color.argb((int) (this.q * 255.0f), 255, 255, 255));
        r.a(":", j5, ((this.a + 20.0f) + (r.a(j6, this.v.c(j2)) / 4.0f)) - (v.a(this.u).b() * 40.0f), this.b + 15.0f, 5.0f, 0.25f, Color.argb((int) ((1.0f - v.a(this.u).b()) * 255.0f * this.q), 255, 255, 255));
        String valueOf = String.valueOf(a(this.v.e().doubleValue(), (char) i, 2, (char) i2, i3));
        if (valueOf.contains(".")) {
            String[] strArr = x;
            valueOf = valueOf.replaceAll(strArr[5], "").replaceAll(strArr[3], "");
        }
        s.a(valueOf, j5, ((this.a + 20.0f) + (r.a(j6, this.v.c(j2) + x[1]) / 4.0f)) - (v.a(this.u).b() * 40.0f), this.b + 15.0f, 5.0f, 0.25f, Color.argb((int) (this.q * (1.0f - v.a(this.u).b()) * 255.0f), 255, 255, 255));
        this.u.d(j4);
    }

    public void e(float f) {
        this.q = f;
    }
}
