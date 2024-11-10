package com.trossense;

import com.trossense.clients.TrosSense;
import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes3.dex */
public class cm {
    private static final long b = dj.a(-794910028531171438L, -4516313745240606796L, MethodHandles.lookup().lookupClass()).a(201845124316817L);
    private static final CopyOnWriteArrayList<cl> a = new CopyOnWriteArrayList<>();

    public static CopyOnWriteArrayList<cl> a() {
        return a;
    }

    public static void a(long j, c cVar, String str, String str2) {
        boolean z;
        long j2 = j ^ b;
        boolean c = c.c();
        a(new cl(cVar, j2 ^ 14825919911181L, str, str2), 47302533163656L ^ j2);
        if (PointerHolder.s() == null) {
            if (j2 > 0) {
                if (!c) {
                    z = true;
                    c.b(z);
                }
                c = false;
            }
            z = c;
            c.b(z);
        }
    }

    private static void a(cl clVar, long j) {
        long j2 = j ^ b;
        bm a2 = TrosSense.INSTANCE.f().a((Class<bm>) b_.class);
        if (a2 != null) {
            boolean l = a2.l();
            if (j2 <= 0 || !l) {
                return;
            }
            a.add(clVar);
        }
    }
}
