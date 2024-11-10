package com.trossense;

import androidx.core.view.ViewCompat;
import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class ak extends p {
    private static az s;
    private static final long t;
    private bm o;
    private float p;
    private float q;
    private df r;

    static {
        long a = dj.a(-6875303864144966831L, 2952819032155225183L, MethodHandles.lookup().lookupClass()).a(256985659512239L);
        t = a;
        s = a1.a(a(7, a("PtGQ'^:]?]D\"")), 128, (a ^ 126617105428551L) ^ 108404888292710L);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ak(float f, long j, float f2, bm bmVar) {
        super(f, f2);
        long j2 = (j ^ t) ^ 19005574969159L;
        this.r = new df(d.Decelerate, 250L, j2);
        this.o = bmVar;
        this.e = 80.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float a(ak akVar) {
        return akVar.p;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float a(ak akVar, float f) {
        akVar.p = f;
        return f;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 35;
                    break;
                case 1:
                    i2 = 22;
                    break;
                case 2:
                    i2 = 46;
                    break;
                case 3:
                    i2 = 55;
                    break;
                case 4:
                    i2 = 67;
                    break;
                case 5:
                    i2 = 48;
                    break;
                default:
                    i2 = 73;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'I');
        }
        return charArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float b(ak akVar) {
        return akVar.q;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float b(ak akVar, float f) {
        akVar.q = f;
        return f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static bm c(ak akVar) {
        return akVar.o;
    }

    @Override // com.trossense.p, com.trossense.aq
    public void a(long j) {
        this.d = (s.a(19020623741841L ^ j, this.o.d(j ^ 128430963502825L)) / 4.0f) + 20.0f;
    }

    @Override // com.trossense.p
    public boolean a(long j, float f, float f2) {
        return true;
    }

    @Override // com.trossense.p
    protected void c() {
        a(new aw(this));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.trossense.p
    public void d(long j) {
        long j2 = j ^ 105732733789211L;
        long j3 = j ^ 111589528524585L;
        long j4 = j ^ 124618597675762L;
        long j5 = j ^ 83447201937396L;
        long j6 = j ^ 61001158647622L;
        boolean m = w.m();
        this.r.a(this.o.l() ? 1.0f : 0.0f, j2);
        int a = c9.a(-1, ad.t, this.r.b());
        int a2 = c9.a(-1, ad.u, this.r.b());
        da.a(this.a, this.b, this.d, this.e, 15.0f, j4, a, a2, false);
        da.a(this.a + 2.0f, this.b + 2.0f, j3, this.d - 4.0f, this.e - 4.0f, 15.0f, ViewCompat.MEASURED_STATE_MASK);
        s.a(this.o.d(j6), this.a + 10.0f, this.b, this.e, j5, 0.25f, a, a2, false, false);
        int i = m;
        if (j >= 0) {
            if (m != 0) {
                return;
            } else {
                i = 4;
            }
        }
        PointerHolder.b(new int[i]);
    }
}
