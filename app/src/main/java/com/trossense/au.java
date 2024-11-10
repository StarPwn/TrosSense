package com.trossense;

/* loaded from: classes3.dex */
class au implements ax {
    final ad a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public au(ad adVar) {
        this.a = adVar;
    }

    /* JADX WARN: Type inference failed for: r5v4, types: [boolean, int] */
    @Override // com.trossense.ax
    public boolean a(long j, float f, float f2) {
        ?? r5 = ((f2 - ad.a(this.a)) > 70.0f ? 1 : ((f2 - ad.a(this.a)) == 70.0f ? 0 : -1));
        if (j <= 0) {
            return r5;
        }
        if (r5 <= 0) {
            return true;
        }
        ad.a(this.a, true);
        ad adVar = this.a;
        ad.a(adVar, ad.b(adVar, f2 - ad.b(adVar)));
        return false;
    }

    @Override // com.trossense.ax
    public void b(float f, int i, char c, int i2, float f2) {
        ad.a(this.a, false);
        ad.b(this.a, false);
    }

    /* JADX WARN: Type inference failed for: r6v2, types: [boolean, int] */
    @Override // com.trossense.ax
    public boolean c(long j, float f, float f2) {
        ad adVar = this.a;
        if (j > 0) {
            if (!ad.c(adVar)) {
                return true;
            }
            adVar = this.a;
        }
        float d = ad.d(adVar) - (f2 - ad.e(this.a));
        ad adVar2 = this.a;
        ad.b(adVar2, f2 - ad.f(adVar2));
        ?? r6 = (Math.abs((ad.g(this.a) + ad.h(this.a)) - f2) > 30.0f ? 1 : (Math.abs((ad.g(this.a) + ad.h(this.a)) - f2) == 30.0f ? 0 : -1));
        if (j <= 0) {
            return r6;
        }
        if (r6 <= 0) {
            return true;
        }
        ad.b(this.a, true);
        ad.c(this.a, d);
        return false;
    }
}
