package com.trossense;

import android.graphics.Color;
import java.lang.invoke.MethodHandles;
import java.util.Map;

/* loaded from: classes3.dex */
public class ac extends p {
    private static final long s = dj.a(6885904541236129382L, 8432392592531137607L, MethodHandles.lookup().lookupClass()).a(88782602127995L);
    private df o;
    private ai p;
    private int q;
    final ai r;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ac(ai aiVar, ai aiVar2, int i, long j) {
        super(0.0f, 0.0f);
        long j2 = j ^ s;
        long j3 = 58790566352773L ^ j2;
        this.r = aiVar;
        this.o = new df(d.Decelerate, 250L, j2 ^ 46464980445079L);
        this.p = aiVar2;
        this.q = i;
        this.e = (ai.p().a() / 2.0f) + 5.0f;
        this.o.a(ai.a(aiVar2).b(i).booleanValue() ? 1.0f : 0.0f, j3);
        this.o.e();
    }

    @Override // com.trossense.p
    public void a(float f, float f2, long j, long j2) {
        Map<cu, Boolean> e = ai.a(this.p).e();
        cu a = ai.a(this.p).a(this.q);
        boolean booleanValue = ai.a(this.p).b(this.q).booleanValue();
        if (j2 >= 0) {
            booleanValue = !booleanValue;
        }
        e.put(a, Boolean.valueOf(booleanValue));
        ai.a(this.p).j();
    }

    @Override // com.trossense.p, com.trossense.aq
    public void a(long j) {
        long j2 = 19020623741841L ^ j;
        long j3 = j ^ 32731980813556L;
        this.d = (ai.p().a(j2, ai.a(this.p).a(this.q).a((int) (j3 >>> 32), (short) ((j3 << 32) >>> 48), (int) ((j3 << 48) >>> 48))) / 2.0f) + 20.0f;
    }

    @Override // com.trossense.p
    public void d(long j) {
        long j2 = j ^ 111589528524585L;
        long j3 = j ^ 121281231864540L;
        long j4 = j ^ 103840959931739L;
        int i = (int) (j4 >>> 32);
        int i2 = (int) ((j4 << 32) >>> 48);
        int i3 = (int) ((j4 << 48) >>> 48);
        this.o.a(ai.a(this.p).b(this.q).booleanValue() ? 1.0f : 0.0f, j ^ 105732733789211L);
        da.a(this.a, this.b, j2, this.d, this.e, 15.0f, c9.a(Color.rgb(64, 64, 64), ad.u, this.o.b()));
        ai.p().a(ai.a(this.p).a(this.q).a(i, (short) i2, i3), j3, this.a + 10.0f, this.b, this.e, 0.5f, Color.argb(((int) (this.o.b() * 128.0f)) + 127, 255, 255, 255));
    }
}
