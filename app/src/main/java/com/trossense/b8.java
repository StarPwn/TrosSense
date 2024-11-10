package com.trossense;

import com.trossense.sdk.entity.type.EntityLocalPlayer;
import java.lang.invoke.MethodHandles;

@cg(a = "NoSlow", b = b.Player, c = "用物品无减速")
/* loaded from: classes3.dex */
public class b8 extends bm {
    private static final long j = dj.a(-7652913124987752864L, 8906136966347403728L, MethodHandles.lookup().lookupClass()).a(229390548961616L);

    public b8(long j2) {
        super((j2 ^ j) ^ 72544542874115L);
    }

    @Override // com.trossense.bm
    public void j(long j2) {
        EntityLocalPlayer.f(true);
    }

    @Override // com.trossense.bm
    public void k(long j2) {
        EntityLocalPlayer.f(false);
    }
}
