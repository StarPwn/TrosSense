package com.trossense;

import com.trossense.sdk.math.Vec3f;
import java.lang.invoke.MethodHandles;

@cg(a = "NoWeb", b = b.Movement, c = "蜘蛛网无减速")
/* loaded from: classes3.dex */
public class b0 extends bm {
    private static final long j = dj.a(7456176398100354216L, 3333423451432701697L, MethodHandles.lookup().lookupClass()).a(50981332142341L);

    public b0(int i, short s, short s2) {
        super(((((s2 << 48) >>> 48) | ((i << 32) | ((s << 48) >>> 32))) ^ j) ^ 105858231209154L);
    }

    @bk
    public void a(bg bgVar) {
        bgVar.a().a(new Vec3f(0.0f, 0.0f, 0.0f));
    }
}
