package com.trossense;

import android.graphics.Color;
import androidx.core.view.ViewCompat;
import com.google.common.base.Ascii;
import com.trossense.clients.TrosSense;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes3.dex */
public class ad extends p {
    private static final float C = 30.0f;
    private static final long G;
    private static az o = null;
    private static az p = null;
    private static final float q = 70.0f;
    public static final float r = 380.0f;
    private static final float s = 800.0f;
    public static final int t;
    public static final int u;
    private float A;
    private float B;
    private float D;
    private float E;
    private String v;
    private String w;
    private List<ab> x;
    private boolean y;
    private boolean z;

    static {
        long a = dj.a(9009806716588971965L, -8206419076538515614L, MethodHandles.lookup().lookupClass()).a(10170849713862L);
        G = a;
        long j = (a ^ 23755648163095L) ^ 12975499914221L;
        String[] strArr = new String[2];
        int length = "YX`1p\u0018jV\u0011D^a>=\u0005jI\u0016m02\b0DOi".length();
        char c = '\b';
        int i = -1;
        int i2 = 0;
        while (true) {
            int i3 = i + 1;
            int i4 = c + i3;
            int i5 = i2 + 1;
            strArr[i2] = a(97, a("YX`1p\u0018jV\u0011D^a>=\u0005jI\u0016m02\b0DOi".substring(i3, i4)));
            if (i4 >= length) {
                o = a1.a(strArr[1], 45, j);
                p = a1.a(strArr[0], 40, j);
                t = Color.rgb(bl.c8, bl.bu, bl.cH);
                u = Color.rgb(28, bl.b2, bl.cU);
                return;
            }
            i2 = i5;
            i = i4;
            c = "YX`1p\u0018jV\u0011D^a>=\u0005jI\u0016m02\b0DOi".charAt(i4);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ad(long j, float f, float f2, String str, String str2, b bVar) {
        super(f, f2);
        long j2 = j ^ G;
        long j3 = 37507664923616L ^ j2;
        this.v = str;
        this.w = str2;
        this.d = 380.0f;
        this.x = new ArrayList();
        Iterator it2 = TrosSense.INSTANCE.f().a(bVar, j2 ^ 83308551888948L).iterator();
        while (it2.hasNext()) {
            ab abVar = new ab(0.0f, j3, 0.0f, (bm) it2.next());
            a(abVar);
            this.x.add(abVar);
        }
        a(new au(this));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float a(ad adVar) {
        return adVar.b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float a(ad adVar, float f) {
        adVar.A = f;
        return f;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 81;
                    break;
                case 1:
                    i2 = 90;
                    break;
                case 2:
                    i2 = 110;
                    break;
                case 3:
                    i2 = 62;
                    break;
                case 4:
                    i2 = 63;
                    break;
                case 5:
                    i2 = 13;
                    break;
                default:
                    i2 = 127;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(ad adVar, boolean z) {
        adVar.y = z;
        return z;
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ Ascii.MAX);
        }
        return charArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float b(ad adVar) {
        return adVar.b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float b(ad adVar, float f) {
        adVar.B = f;
        return f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean b(ad adVar, boolean z) {
        adVar.z = z;
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float c(ad adVar, float f) {
        float f2 = adVar.D + f;
        adVar.D = f2;
        return f2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean c(ad adVar) {
        return adVar.y;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float d(ad adVar) {
        return adVar.B;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float e(ad adVar) {
        return adVar.b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float f(ad adVar) {
        return adVar.b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float g(ad adVar) {
        return adVar.A;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float h(ad adVar) {
        return adVar.b;
    }

    @Override // com.trossense.p, com.trossense.aq
    public void a(long j) {
        long j2 = j ^ 89014590124158L;
        long j3 = j ^ 0;
        String[] m = ae.m();
        this.D = Math.min(Math.max(0.0f, this.D), Math.max(this.E - s, 0.0f));
        this.E = 0.0f;
        for (ab abVar : this.x) {
            abVar.b(j2, this.a + ((this.d - 360.0f) / 2.0f), ((this.b + this.E) - this.D) + q);
            abVar.a(j3);
            this.E += abVar.g();
            if (j >= 0 && m == null) {
                return;
            }
            if (m == null) {
                break;
            }
        }
        this.e = Math.max(q, Math.min(this.E - this.D, s) + q);
    }

    @Override // com.trossense.p
    public boolean a(long j, float f, float f2) {
        return f2 - this.b < q;
    }

    @Override // com.trossense.p
    protected boolean b(float f, float f2, long j) {
        return this.z;
    }

    @Override // com.trossense.p
    public void d(long j) {
        long j2 = j ^ 0;
        long j3 = j ^ 91228529291838L;
        da.a(this.a, this.b, this.d, this.e, 20.0f, j ^ 84803153043256L, t, u);
        float a = o.a(j3, this.v + " ") / 2.0f;
        o.b(this.v, j ^ 50167032995699L, ((this.a + 190.0f) - a) - (p.a(j3, this.w) / 2.0f), this.b + o.a(q), -1);
        p.a(this.w, this.a + 190.0f + a, this.b + p.a(q), -1, j ^ 75602871466904L);
        dd.a();
        dd.b();
        da.b(j ^ 139128871043769L, this.a + ((this.d - 360.0f) / 2.0f), this.b + q, 360.0f, 50.0f, ViewCompat.MEASURED_STATE_MASK);
        String[] m = ae.m();
        da.a(((this.d - 360.0f) / 2.0f) + this.a, this.b + q + 25.0f, j ^ 111589528524585L, 360.0f, (this.e - q) - 35.0f, 12.0f, ViewCompat.MEASURED_STATE_MASK);
        dd.c();
        Iterator<ab> it2 = this.x.iterator();
        while (it2.hasNext()) {
            it2.next().d(j2);
            if (j >= 0 && m == null) {
                return;
            }
            if (m == null) {
                break;
            }
        }
        dd.d();
    }
}
