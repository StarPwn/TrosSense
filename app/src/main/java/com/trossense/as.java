package com.trossense;

import androidx.core.math.MathUtils;

/* loaded from: classes3.dex */
class as implements ax {
    final ag a;
    final ag b;
    final z c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public as(z zVar, ag agVar, ag agVar2) {
        this.c = zVar;
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
        boolean i = ag.b(this.b).i();
        if (j < 0) {
            return i;
        }
        if (i) {
            return true;
        }
        ag.b(this.b).d((MathUtils.clamp(f, z.a(this.c), z.b(this.c) + z.c(this.c)) - z.d(this.c)) / z.e(this.c));
        ag.b(this.b).c(1.0f - ((MathUtils.clamp(f2, z.f(this.c), z.g(this.c) + z.h(this.c)) - z.i(this.c)) / z.j(this.c)));
        return false;
    }
}
