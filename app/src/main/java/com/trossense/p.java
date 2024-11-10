package com.trossense;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BooleanSupplier;

/* loaded from: classes3.dex */
public class p implements aq {
    private static final long F = dj.a(-6360713210102912689L, 3601528385800299437L, MethodHandles.lookup().lookupClass()).a(88303361680056L);
    private static boolean n;
    protected float a;
    protected float b;
    protected float d;
    protected float e;
    protected boolean f;
    private long g;
    protected boolean h;
    protected boolean i;
    protected float j;
    protected float k;
    protected BooleanSupplier c = new ao(this);
    private List<ax> m = new ArrayList();
    protected List<p> l = new ArrayList();

    static {
        if (k()) {
            b(true);
        }
    }

    public p(float f, float f2) {
        this.f = false;
        this.h = false;
        this.i = false;
        this.a = f;
        this.b = f2;
        this.f = true;
        this.h = false;
        this.i = false;
        c();
        a(new ap(this));
    }

    public static void b(boolean z) {
        n = z;
    }

    public static boolean j() {
        return n;
    }

    public static boolean k() {
        return !j();
    }

    public p a(BooleanSupplier booleanSupplier) {
        this.c = booleanSupplier;
        return this;
    }

    public void a(float f) {
        this.e = f;
    }

    public void a(float f, float f2, long j, long j2) {
    }

    @Override // com.trossense.aq
    public void a(long j) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:11:0x00f1  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x011a  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x007d A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00de A[LOOP:1: B:25:0x007d->B:36:0x00de, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x013c A[LOOP:0: B:6:0x005d->B:51:0x013c, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0116  */
    /* JADX WARN: Type inference failed for: r0v9, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r21v1, types: [java.util.Iterator] */
    /* JADX WARN: Type inference failed for: r21v2, types: [java.util.Iterator] */
    /* JADX WARN: Type inference failed for: r21v4 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:43:0x009c -> B:26:0x00a5). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:44:0x00e5 -> B:9:0x00e9). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void a(long r35, float r37, float r38, boolean r39) {
        /*
            Method dump skipped, instructions count: 327
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.p.a(long, float, float, boolean):void");
    }

    public void a(ax axVar) {
        this.m.add(axVar);
    }

    public void a(p pVar) {
        this.l.add(pVar);
    }

    public void a(boolean z) {
        this.f = z;
    }

    public boolean a(long j, float f, float f2) {
        return false;
    }

    public void b(float f) {
        this.d = f;
    }

    @Override // com.trossense.aq
    public final void b(long j) {
        long j2 = 962850386263L ^ j;
        long j3 = j ^ 74030066139384L;
        if (!this.f || this.c.getAsBoolean()) {
            return;
        }
        a(j3);
        d(j2);
    }

    public void b(long j, float f, float f2) {
        this.a = f;
        this.b = f2;
    }

    protected boolean b(float f, float f2, long j) {
        return false;
    }

    protected void c() {
    }

    public void c(float f) {
        this.a = f;
    }

    public void c(float f, float f2) {
        this.d = f;
        this.e = f2;
    }

    public void d(float f) {
        this.b = f;
    }

    public void d(long j) {
    }

    public final boolean d(float f, long j, float f2) {
        long j2 = j ^ F;
        long j3 = 74477492153666L ^ j2;
        long j4 = 10415815833773L ^ j2;
        long j5 = j2 ^ 40654078683538L;
        if (f(f, f2, j4) && this.f && !this.c.getAsBoolean()) {
            this.h = true;
            this.g = System.currentTimeMillis();
            Iterator<p> it2 = this.l.iterator();
            while (it2.hasNext()) {
                if (!it2.next().d(f, j5, f2)) {
                    return false;
                }
            }
            Iterator<ax> it3 = this.m.iterator();
            while (it3.hasNext()) {
                if (!it3.next().a(j3, f, f2)) {
                    return false;
                }
            }
        }
        return true;
    }

    public float e() {
        return this.a;
    }

    public final boolean e(long j, float f, float f2) {
        long j2 = j ^ F;
        long j3 = 91358558781144L ^ j2;
        long j4 = j2 ^ 40654078683538L;
        if (!this.h) {
            return true;
        }
        Iterator<p> it2 = this.l.iterator();
        while (it2.hasNext()) {
            if (!it2.next().e(j4, f, f2)) {
                return false;
            }
        }
        Iterator<ax> it3 = this.m.iterator();
        while (it3.hasNext()) {
            if (!it3.next().c(j3, f, f2)) {
                return false;
            }
        }
        return true;
    }

    public float f() {
        return this.d;
    }

    public boolean f(float f, float f2, long j) {
        float f3 = this.a;
        if (f >= f3 && f <= f3 + this.d) {
            float f4 = this.b;
            if (f2 >= f4 && f2 <= f4 + this.e) {
                return true;
            }
        }
        return false;
    }

    public float g() {
        return this.e;
    }

    public float h() {
        return this.b;
    }

    public boolean i() {
        return this.f;
    }
}
