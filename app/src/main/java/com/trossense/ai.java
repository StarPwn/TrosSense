package com.trossense;

import android.graphics.Color;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes3.dex */
public class ai extends ae {
    private static az q;
    private static final long t;
    private cs r;
    private List<ac> s;

    static {
        long a = dj.a(-1030257459397360197L, -2091784213487534116L, MethodHandles.lookup().lookupClass()).a(166948319800919L);
        t = a;
        q = a1.a(a(61, a("\u001bmX?6BA\u0016&B*3")), 56, (a ^ 91766731743057L) ^ 96871154847310L);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ai(cs csVar, long j) {
        super(0.0f, 0.0f, csVar);
        long j2 = (j ^ t) ^ 49373749970606L;
        this.r = csVar;
        this.d = 360.0f;
        n(j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static cs a(ai aiVar) {
        return aiVar.r;
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
                    i2 = 53;
                    break;
                case 2:
                    i2 = 11;
                    break;
                case 3:
                    i2 = 99;
                    break;
                case 4:
                    i2 = 104;
                    break;
                case 5:
                    i2 = 22;
                    break;
                default:
                    i2 = 8;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '\b');
        }
        return charArray;
    }

    private void n(long j) {
        long j2 = j ^ t;
        long j3 = 3519720747159L ^ j2;
        this.s = new ArrayList();
        Set<cu> keySet = this.r.e().keySet();
        int i = 0;
        while (i < keySet.size()) {
            ac acVar = new ac(this, this, i, j3);
            a(acVar);
            this.s.add(acVar);
            i++;
            if (j2 < 0) {
                return;
            }
        }
    }

    private void o(long j) {
        long j2 = (j ^ t) ^ 41890201836282L;
        if (this.s.size() != this.r.e().keySet().size()) {
            n(j2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static az p() {
        return q;
    }

    @Override // com.trossense.p, com.trossense.aq
    public void a(long j) {
        long j2 = j ^ 89014590124158L;
        long j3 = j ^ 0;
        this.e = 40.0f;
        float f = this.a + 20.0f;
        String[] m = ae.m();
        o(j ^ 132148334636783L);
        float f2 = 50.0f;
        for (ac acVar : this.s) {
            acVar.a(j3);
            if (m == null) {
                return;
            }
            float f3 = acVar.f() + f;
            float f4 = (this.a + this.d) - 10.0f;
            if (j >= 0) {
                if (f3 > f4) {
                    f = this.a + 20.0f;
                    f2 += acVar.g() + 5.0f;
                }
                acVar.b(j2, f, this.b + f2);
                f4 = 5.0f + acVar.f();
                f3 = f;
            }
            f = f3 + f4;
            if (m == null) {
                break;
            }
        }
        this.e += f2 + 7.0f;
    }

    @Override // com.trossense.p
    public void d(long j) {
        long j2 = j ^ 0;
        long j3 = j ^ 111589528524585L;
        q.a(this.r.c(j ^ 37087146222550L), j ^ 121281231864540L, this.a + 20.0f, this.b + 15.0f, 5.0f, 0.5f, -1);
        da.a(10.0f + this.a, this.b + 40.0f, j3, this.d - 20.0f, this.e - 40.0f, 15.0f, c9.a(Color.rgb(64, 64, 64), ad.u, this.r.i().size() / this.r.e().size()));
        String[] m = ae.m();
        da.a(13.0f + this.a, 43.0f + this.b, j3, this.d - 26.0f, this.e - 46.0f, 15.0f, Color.rgb(45, 45, 45));
        Iterator<ac> it2 = this.s.iterator();
        while (it2.hasNext()) {
            it2.next().d(j2);
            if (m == null) {
                return;
            }
        }
    }
}
