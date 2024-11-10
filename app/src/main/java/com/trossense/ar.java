package com.trossense;

import androidx.core.math.MathUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class ar implements ax {
    final ag a;
    final ag b;
    final s c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ar(s sVar, ag agVar, ag agVar2) {
        this.c = sVar;
        this.a = agVar;
        this.b = agVar2;
    }

    @Override // com.trossense.ax
    public boolean a(long j, float f, float f2) {
        c(j ^ 66898845766908L, f, f2);
        return false;
    }

    @Override // com.trossense.ax
    public void b(float f, int i, char c, int i2, float f2) {
    }

    @Override // com.trossense.ax
    public boolean c(long j, float f, float f2) {
        ag.b(this.b).a((MathUtils.clamp(f, s.a(this.c), s.b(this.c) + s.c(this.c)) - s.d(this.c)) / s.e(this.c));
        return false;
    }
}
