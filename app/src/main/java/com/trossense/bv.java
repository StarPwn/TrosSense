package com.trossense;

import com.trossense.clients.TrosSense;
import java.lang.invoke.MethodHandles;

@cg(a = "Chinese", b = b.Misc, c = "中文模式")
/* loaded from: classes3.dex */
public class bv extends bm {
    private static final long j = dj.a(2663853285205462756L, -6461218087514091612L, MethodHandles.lookup().lookupClass()).a(96064041930437L);

    public bv(long j2) {
        super((j2 ^ j) ^ 87926856935050L);
    }

    @Override // com.trossense.bm
    public void j(long j2) {
        TrosSense.INSTANCE.isEnglishLanguage = false;
    }

    @Override // com.trossense.bm
    public void k(long j2) {
        TrosSense.INSTANCE.isEnglishLanguage = true;
    }
}
