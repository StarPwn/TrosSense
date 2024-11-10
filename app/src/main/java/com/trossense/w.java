package com.trossense;

import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class w extends q {
    private static boolean p;
    private static final long v = dj.a(6351537840873131732L, -4370611919136097548L, MethodHandles.lookup().lookupClass()).a(74158054552827L);
    private String o;

    static {
        if (m()) {
            return;
        }
        c(true);
    }

    public w(float f, float f2, float f3, long j, float f4, String str) {
        super((v ^ j) ^ 106987863713577L, f, f2, f3, f4);
        this.o = str;
    }

    public static void c(boolean z) {
        p = z;
    }

    public static boolean l() {
        return p;
    }

    public static boolean m() {
        return !l();
    }

    @Override // com.trossense.p
    public boolean a(long j, float f, float f2) {
        return true;
    }

    @Override // com.trossense.p
    public void d(long j) {
        da.a(this.o, this.a, this.b, this.d, j ^ 34289131986434L, this.e);
    }
}
