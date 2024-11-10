package com.trossense;

import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class q extends p {
    private static final long t = dj.a(-3264839618498148322L, 606370053612555030L, MethodHandles.lookup().lookupClass()).a(65394479755449L);

    public q(long j, float f, float f2, float f3, float f4) {
        super(f, f2);
        boolean k = p.k();
        this.d = f3;
        this.e = f4;
        if (k) {
            PointerHolder.b(new int[2]);
        }
    }

    @Override // com.trossense.p
    public void a(float f) {
        this.e = f;
    }

    @Override // com.trossense.p
    public void b(float f) {
        this.d = f;
    }

    @Override // com.trossense.p
    public float f() {
        return this.d;
    }

    @Override // com.trossense.p
    public float g() {
        return this.e;
    }
}
