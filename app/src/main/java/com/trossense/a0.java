package com.trossense;

import android.graphics.Bitmap;
import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
final class a0 {
    private static final long d = dj.a(-8501893902283557335L, -6897851500885263099L, MethodHandles.lookup().lookupClass()).a(230885652405452L);
    public final int a;
    public final float b;
    final az c;

    public a0(az azVar, Bitmap bitmap, float f) {
        this.c = azVar;
        this.a = de.a(bitmap);
        this.b = f;
    }

    public void a(float f, float f2, int i, long j) {
        long j2 = (j ^ d) ^ 59049314614132L;
        az.b();
        da.a(this.a, f, f2, az.a(this.c), az.a(this.c), i, j2);
        if (PointerHolder.s() == null) {
            az.b(new int[3]);
        }
    }

    public void a(long j, float f, float f2, int i, int i2, boolean z, char c) {
        long j2 = (((j << 16) | ((c << 48) >>> 48)) ^ d) ^ 63177897903503L;
        int[] b = az.b();
        da.a(this.a, f, f2, az.a(this.c), az.a(this.c), i, i2, z, j2);
        if (b == null) {
            PointerHolder.b(new int[3]);
        }
    }
}
