package com.trossense;

import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class c7 {
    private static final long b = dj.a(-2098016626214717010L, -2893419681997723606L, MethodHandles.lookup().lookupClass()).a(85083526487538L);
    private long a;

    public c7() {
        a();
    }

    public void a() {
        this.a = System.currentTimeMillis();
    }

    public void a(long j) {
        this.a = j;
    }

    public long b() {
        return this.a;
    }

    public boolean b(long j, byte b2, long j2) {
        return System.currentTimeMillis() - j2 >= this.a;
    }

    public long c() {
        return System.currentTimeMillis() - this.a;
    }
}
