package com.trossense;

import android.graphics.Color;
import java.lang.invoke.MethodHandles;
import java.util.List;

/* loaded from: classes3.dex */
public class ab extends p {
    private static final long A;
    private static az q = null;
    private static az r = null;
    private static az s = null;
    private static final int w;
    public static final float x = 58.0f;
    public static final float y = 360.0f;
    public static final int z;
    public df o;
    public df p;
    private bm t;
    private boolean u;
    private List<ae> v;

    static {
        long a = dj.a(-5862016483507231079L, 1841167380821326604L, MethodHandles.lookup().lookupClass()).a(108906812454776L);
        A = a;
        long j = (a ^ 60082738937343L) ^ 20659421365598L;
        String[] strArr = new String[3];
        int length = "\u001ag$F_P\u0006\u0015\f\u0007a%I\u0012M\u0006\n*?\\\u0017\u0011\u0007a%I\u0012M\u0006\n))G\u001d@\\\u0007p-".length();
        char c = '\b';
        int i = -1;
        int i2 = 0;
        while (true) {
            int i3 = i + 1;
            int i4 = c + i3;
            int i5 = i2 + 1;
            strArr[i2] = a(18, a("\u001ag$F_P\u0006\u0015\f\u0007a%I\u0012M\u0006\n*?\\\u0017\u0011\u0007a%I\u0012M\u0006\n))G\u001d@\\\u0007p-".substring(i3, i4)));
            if (i4 >= length) {
                q = a1.a(strArr[2], 36, j);
                r = a1.a(strArr[1], 36, j);
                s = a1.a(strArr[0], 40, j);
                w = Color.rgb(35, 37, 43);
                z = Color.rgb(32, 32, 32);
                return;
            }
            i2 = i5;
            i = i4;
            c = "\u001ag$F_P\u0006\u0015\f\u0007a%I\u0012M\u0006\n*?\\\u0017\u0011\u0007a%I\u0012M\u0006\n))G\u001d@\\\u0007p-".charAt(i4);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0155 A[EDGE_INSN: B:29:0x0155->B:30:0x0155 BREAK  A[LOOP:0: B:2:0x0073->B:37:0x0135], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0147  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x015a A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0143 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x00b3  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:28:0x00a7 -> B:5:0x00ab). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:32:0x0152 -> B:29:0x0155). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public ab(float r23, long r24, float r26, com.trossense.bm r27) {
        /*
            Method dump skipped, instructions count: 347
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.ab.<init>(float, long, float, com.trossense.bm):void");
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 97;
                    break;
                case 1:
                    i2 = 22;
                    break;
                case 2:
                    i2 = 89;
                    break;
                case 3:
                    i2 = 58;
                    break;
                case 4:
                    i2 = 99;
                    break;
                case 5:
                    i2 = 54;
                    break;
                default:
                    i2 = 96;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '`');
        }
        return charArray;
    }

    @Override // com.trossense.p
    public void a(float f, float f2, long j, long j2) {
        long j3 = 86544973032245L ^ j2;
        if (f2 >= this.b + 58.0f) {
            return;
        }
        if (f > (this.a + this.d) - 60.0f) {
            this.u = !this.u;
        } else {
            this.t.g(j3);
        }
    }

    @Override // com.trossense.p, com.trossense.aq
    public void a(long j) {
        long j2 = 39159558870452L ^ j;
        long j3 = 14605935197849L ^ j;
        long j4 = 89014590124158L ^ j;
        long j5 = j ^ 0;
        this.o.a(this.t.l() ? 1.0f : 0.0f, j2);
        this.p.a(this.u ? 1.0f : 0.0f, j2);
        for (ae aeVar : this.v) {
            aeVar.a(!aeVar.l().a(j3) && (this.u || !this.p.d()));
        }
        this.e = 58.0f;
        if (this.u || !this.p.d()) {
            float f = 0.0f;
            for (ae aeVar2 : this.v) {
                if (aeVar2.i()) {
                    aeVar2.b(j4, this.a, this.b + 58.0f + f);
                    aeVar2.a(j5);
                    f += aeVar2.g();
                }
            }
            if (f != 0.0f) {
                f += 15.0f;
            }
            this.e += f * this.p.b();
        }
    }

    @Override // com.trossense.p
    public void d(long j) {
        long j2;
        long j3 = j ^ 128968524833061L;
        long j4 = j3 >>> 8;
        int i = (int) ((j3 << 56) >>> 56);
        long j5 = j ^ 28834871580341L;
        long j6 = j ^ 50167032995699L;
        long j7 = j ^ 91228529291838L;
        long j8 = j ^ 61001158647622L;
        long j9 = j ^ 139128871043769L;
        long j10 = j ^ 0;
        da.b(j9, this.a, this.b, this.d, 58.0f, c9.a(w, Color.argb(38, 0, 0, 0), this.o.b()));
        int argb = Color.argb(((int) (this.o.b() * 51.2f)) + 127, 255, 255, 255);
        (this.t.l() ? q : r).b(this.t.d(j8), j6, this.a + 20.0f, this.b + r.a(58.0f), argb);
        float f = (this.a + this.d) - 38.0f;
        float a = this.b + s.a(58.0f);
        da.a(f, a, s.a(j7, "z"), j5, s.a(), this.p.b() * 180.0f);
        s.b("z", j6, f, a, argb);
        da.b(j4, (byte) i);
        da.b(j9, this.a, this.b + 58.0f, this.d, this.e - 58.0f, Color.argb(255, 32, 32, 32));
        for (ae aeVar : this.v) {
            if (aeVar.i()) {
                j2 = j10;
                aeVar.d(j2);
            } else {
                j2 = j10;
            }
            j10 = j2;
        }
    }
}
