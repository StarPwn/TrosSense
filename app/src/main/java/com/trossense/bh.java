package com.trossense;

import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/* loaded from: classes3.dex */
public class bh {
    private static final long e = dj.a(-4398176239906031375L, -8964405157344138491L, MethodHandles.lookup().lookupClass()).a(1669861658850L);
    private final Method a;
    private final Class<? extends a9> b;
    private final Object c;
    private final int d;

    public bh(Method method, Class cls, Object obj, long j, int i) {
        this.a = method;
        a9.b();
        this.b = cls;
        this.c = obj;
        this.d = i;
        if (PointerHolder.s() == null) {
            a9.b("wmNgo");
        }
    }

    public Method a() {
        return this.a;
    }

    public Class<? extends a9> b() {
        return this.b;
    }

    public Object c() {
        return this.c;
    }

    public int d() {
        return this.d;
    }
}
