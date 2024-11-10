package com.trossense;

import com.trossense.sdk.PointerHolder;

/* loaded from: classes3.dex */
class ap implements ax {
    final p a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ap(p pVar) {
        this.a = pVar;
    }

    @Override // com.trossense.ax
    public boolean a(long j, float f, float f2) {
        boolean k = p.k();
        if (!this.a.a(55388084122575L ^ j, f, f2)) {
            if (PointerHolder.s() == null) {
                if (j >= 0) {
                    k = !k;
                }
                p.b(k);
            }
            return true;
        }
        this.a.i = true;
        p pVar = this.a;
        pVar.j = f - pVar.a;
        p pVar2 = this.a;
        pVar2.k = f2 - pVar2.b;
        return false;
    }

    @Override // com.trossense.ax
    public void b(float f, int i, char c, int i2, float f2) {
        if (this.a.i) {
            this.a.i = false;
        }
    }

    @Override // com.trossense.ax
    public boolean c(long j, float f, float f2) {
        long j2 = j ^ 111926181984753L;
        if (!this.a.i) {
            return true;
        }
        p pVar = this.a;
        pVar.b(j2, f - pVar.j, f2 - this.a.k);
        return false;
    }
}
