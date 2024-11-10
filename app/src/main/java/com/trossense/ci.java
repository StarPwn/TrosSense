package com.trossense;

import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
class ci {
    private static final long d = dj.a(7633476443505919951L, 7451260306894548096L, MethodHandles.lookup().lookupClass()).a(274072531175962L);
    private int a;
    private float b;
    final bw c;

    public ci(bw bwVar, int i, long j, float f) {
        this.c = bwVar;
        bt.o();
        this.a = i;
        this.b = f;
        if (PointerHolder.s() == null) {
            bt.a(new String[2]);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(ci ciVar) {
        return ciVar.a;
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
