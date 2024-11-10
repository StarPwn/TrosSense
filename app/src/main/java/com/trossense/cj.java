package com.trossense;

import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
class cj {
    private static final long d = dj.a(-7319164755471711824L, -8216635932719939764L, MethodHandles.lookup().lookupClass()).a(124270780888499L);
    private int a;
    private float b;
    final b4 c;

    public cj(b4 b4Var, int i, float f, long j) {
        this.c = b4Var;
        b5.n();
        this.a = i;
        this.b = f;
        if (PointerHolder.s() == null) {
            b5.b(new int[5]);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(cj cjVar) {
        return cjVar.a;
    }

    public int a() {
        return this.a;
    }

    public void a(float f) {
        this.b = f;
    }

    public void a(int i) {
        this.a = i;
    }

    public float b() {
        return this.b;
    }
}
