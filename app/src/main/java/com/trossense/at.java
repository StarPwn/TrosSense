package com.trossense;

import androidx.core.math.MathUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class at implements ax {
    final aj a;
    final aj b;
    final v c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public at(v vVar, aj ajVar, aj ajVar2) {
        this.c = vVar;
        this.a = ajVar;
        this.b = ajVar2;
    }

    @Override // com.trossense.ax
    public boolean a(long j, float f, float f2) {
        this.c.r = true;
        c(j ^ 66898845766908L, f, f2);
        return false;
    }

    @Override // com.trossense.ax
    public void b(float f, int i, char c, int i2, float f2) {
        this.c.r = false;
    }

    @Override // com.trossense.ax
    public boolean c(long j, float f, float f2) {
        float clamp = (MathUtils.clamp(f, v.b(this.c), v.c(this.c) + v.d(this.c)) - v.e(this.c)) / v.f(this.c);
        aj.b(this.b).a(Float.valueOf((clamp * (aj.b(this.b).j().floatValue() - aj.b(this.b).k().floatValue())) + aj.b(this.b).k().floatValue()), j ^ 33905699364051L);
        return false;
    }
}
