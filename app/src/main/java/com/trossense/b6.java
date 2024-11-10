package com.trossense;

import com.trossense.sdk.entity.type.EntityLocalPlayer;
import java.lang.invoke.MethodHandles;

@cg(a = "InvMove", b = b.Player, c = "物品移动")
/* loaded from: classes3.dex */
public class b6 extends bm {
    private static final long j = dj.a(6915377989199073881L, -1533091437331628490L, MethodHandles.lookup().lookupClass()).a(222241526407156L);

    public b6(char c, long j2) {
        super(((((j2 << 16) >>> 16) | (c << 48)) ^ j) ^ 55909321746332L);
    }

    @Override // com.trossense.bm
    public void j(long j2) {
        EntityLocalPlayer.g(true);
    }

    @Override // com.trossense.bm
    public void k(long j2) {
        EntityLocalPlayer.g(false);
    }
}
