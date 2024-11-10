package com.trossense;

/* loaded from: classes3.dex */
class aw implements ax {
    final ak a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public aw(ak akVar) {
        this.a = akVar;
    }

    @Override // com.trossense.ax
    public boolean a(long j, float f, float f2) {
        ak.a(this.a, f);
        ak.b(this.a, f2);
        return true;
    }

    @Override // com.trossense.ax
    public void b(float f, int i, char c, int i2, float f2) {
        long j = (((i << 32) | ((c << 48) >>> 32)) | ((i2 << 48) >>> 48)) ^ 108522209592926L;
        if (Math.abs(ak.a(this.a) - f) <= 20.0f) {
            ak akVar = this.a;
            if (c > 0) {
                if (Math.abs(ak.b(akVar) - f2) > 20.0f) {
                    return;
                } else {
                    akVar = this.a;
                }
            }
            ak.c(akVar).g(j);
        }
    }

    @Override // com.trossense.ax
    public boolean c(long j, float f, float f2) {
        return true;
    }
}
