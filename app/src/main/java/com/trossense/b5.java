package com.trossense;

import java.lang.invoke.MethodHandles;
import org.cloudburstmc.math.vector.Vector3f;

@cg(a = "GodMode", b = b.Player, c = "上帝模式")
/* loaded from: classes3.dex */
public class b5 extends bm {
    public static b5 j;
    private static int[] k;
    private static final long l = dj.a(3702938130345368244L, 3551673404576014503L, MethodHandles.lookup().lookupClass()).a(83752753644623L);

    static {
        if (n() == null) {
            b(new int[5]);
        }
    }

    public b5(long j2) {
        super((j2 ^ l) ^ 79930835302649L);
        j = this;
    }

    public static void b(int[] iArr) {
        k = iArr;
    }

    public static int[] n() {
        return k;
    }

    @bk
    public void a(bb bbVar) {
        if (bbVar.e() instanceof com.trossense.sdk.ad) {
            com.trossense.sdk.ad adVar = (com.trossense.sdk.ad) bbVar.e();
            if (adVar.getTick() % 2 == 0) {
                Vector3f position = adVar.getPosition();
                adVar.setPosition(Vector3f.from(position.getX(), 900.0f, position.getZ()));
            }
        }
    }
}
