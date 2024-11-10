package com.trossense;

import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class cl {
    private static final long e = dj.a(-2379642278027278559L, -5220539755762342836L, MethodHandles.lookup().lookupClass()).a(41882127120990L);
    private final c a;
    private final String b;
    private final String c;
    private final df d;

    public cl(c cVar, long j, String str, String str2) {
        long j2 = j ^ e;
        long j3 = 19362677204225L ^ j2;
        this.a = cVar;
        this.b = str;
        boolean c = c.c();
        this.c = str2;
        df dfVar = new df(d.Decelerate, 250L, j2 ^ 16108258759955L);
        this.d = dfVar;
        dfVar.a(1.0f, j3);
        if (c) {
            return;
        }
        PointerHolder.b(new int[4]);
    }

    public df a() {
        return this.d;
    }

    public String b() {
        return this.b;
    }

    public String c() {
        return this.c;
    }

    public c d() {
        return this.a;
    }
}
