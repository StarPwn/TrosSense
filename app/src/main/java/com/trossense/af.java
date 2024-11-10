package com.trossense;

import android.graphics.Color;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class af extends ae {
    private static az q = null;
    private static final float s = 60.0f;
    private static final float t = 30.0f;
    private static final long v;
    private cp r;
    public C0039r u;

    static {
        long a = dj.a(7768710550349804310L, -1730969795264682051L, MethodHandles.lookup().lookupClass()).a(254645664384809L);
        v = a;
        q = a1.a(a(24, a(",\u0002\u0014x%j\\!I\u000em ")), 128, (a ^ 115966755988782L) ^ 111485153346371L);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public af(cp cpVar, long j) {
        super(0.0f, 0.0f, cpVar);
        long j2 = (j ^ v) ^ 54274034859107L;
        this.d = 360.0f;
        this.e = 70.0f;
        this.r = cpVar;
        C0039r c0039r = new C0039r((char) (j2 >>> 48), this, s, t, cpVar, (int) ((j2 << 16) >>> 32), (short) ((j2 << 48) >>> 48));
        this.u = c0039r;
        a(c0039r);
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 64;
                    break;
                case 1:
                    i2 = 127;
                    break;
                case 2:
                    i2 = 98;
                    break;
                case 3:
                    i2 = 1;
                    break;
                case 4:
                    i2 = 94;
                    break;
                case 5:
                    i2 = 27;
                    break;
                default:
                    i2 = 48;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '0');
        }
        return charArray;
    }

    @Override // com.trossense.p, com.trossense.aq
    public void a(long j) {
        this.u.b(j ^ 89014590124158L, ((this.a + this.d) - 10.0f) - s, (this.b + (this.e / 2.0f)) - 15.0f);
    }

    @Override // com.trossense.p
    public void d(long j) {
        long j2 = j ^ 121281231864540L;
        q.a(this.r.c(37087146222550L ^ j), j2, this.a + 10.0f, this.b, this.e, 0.25f, Color.argb(((int) (this.u.o.b() * 128.0f)) + 127, 255, 255, 255));
        this.u.d(j ^ 0);
    }
}
