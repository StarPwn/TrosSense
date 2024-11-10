package com.trossense;

import com.trossense.sdk.PointerHolder;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class bg extends a9 {
    private static int c;
    private static final long d = dj.a(-5538750040608836920L, 88393749325651596L, MethodHandles.lookup().lookupClass()).a(21787243823276L);
    private EntityLocalPlayer a;

    static {
        if (c() == 0) {
            b(3);
        }
    }

    public bg(EntityLocalPlayer entityLocalPlayer, long j) {
        int c2 = c();
        this.a = entityLocalPlayer;
        if (PointerHolder.s() == null) {
            b(c2 + 1);
        }
    }

    public static void b(int i) {
        c = i;
    }

    public static int c() {
        return c;
    }

    public static int d() {
        return c() == 0 ? 8 : 0;
    }

    public EntityLocalPlayer a() {
        return this.a;
    }
}
