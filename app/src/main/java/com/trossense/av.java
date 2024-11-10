package com.trossense;

/* loaded from: classes3.dex */
class av implements ax {
    final x a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public av(x xVar) {
        this.a = xVar;
    }

    @Override // com.trossense.ax
    public boolean a(long j, float f, float f2) {
        x.a(this.a, f);
        x.b(this.a, f2);
        return true;
    }

    @Override // com.trossense.ax
    public void b(float f, int i, char c, int i2, float f2) {
        boolean z;
        if (Math.abs(x.a(this.a) - f) <= 20.0f) {
            x xVar = this.a;
            if (i2 >= 0) {
                if (Math.abs(x.b(xVar) - f2) > 20.0f) {
                    return;
                } else {
                    xVar = this.a;
                }
            }
            boolean c2 = x.c(this.a);
            if (i >= 0) {
                if (c2) {
                    z = false;
                    x.a(xVar, z);
                }
                c2 = true;
            }
            z = c2;
            x.a(xVar, z);
        }
    }

    @Override // com.trossense.ax
    public boolean c(long j, float f, float f2) {
        return true;
    }
}
