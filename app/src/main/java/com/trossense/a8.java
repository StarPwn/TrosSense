package com.trossense;

import com.trossense.clients.TrosSense;
import com.trossense.sdk.PointerHolder;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import com.trossense.sdk.math.Vec2f;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket;

/* loaded from: classes3.dex */
public class a8 {
    private static Vec2f a;
    private static Vec2f b;
    private static Vec2f c;
    private static Vec2f d;
    private static boolean f;
    private static float g;
    private static boolean h;
    private static boolean i;
    private static boolean j;
    private static final long k = dj.a(-3290780100469624206L, -4796376103979256388L, MethodHandles.lookup().lookupClass()).a(34037462323635L);
    private static Vec2f e = new Vec2f(0.0f, 0.0f);

    static {
        b(false);
    }

    public static Vec2f a() {
        return e;
    }

    public static void a(Vec2f vec2f, float f2) {
        a(vec2f, f2, false);
    }

    public static void a(Vec2f vec2f, float f2, boolean z) {
        c = vec2f;
        g = f2;
        i = z;
        f = true;
        h = false;
    }

    public static void b(boolean z) {
        j = z;
    }

    public static boolean b() {
        return j;
    }

    public static boolean c() {
        return !b();
    }

    @bk(0)
    public void a(bb bbVar) {
        boolean c2 = c();
        if (bbVar.e() instanceof MovePlayerPacket) {
            MovePlayerPacket movePlayerPacket = (MovePlayerPacket) bbVar.e();
            if (f) {
                movePlayerPacket.setRotation(Vector3f.from(a.x, a.y, a.y));
            }
            if (c2) {
                return;
            }
        }
        if (bbVar.e() instanceof com.trossense.sdk.ad) {
            com.trossense.sdk.ad adVar = (com.trossense.sdk.ad) bbVar.e();
            if (f) {
                adVar.setRotation(Vector3f.from(a.x, a.y, a.y));
            }
        }
    }

    @bk(0)
    public void a(bf bfVar) {
        EntityLocalPlayer localPlayer = TrosSense.INSTANCE.getLocalPlayer();
        if (localPlayer == null || !f) {
            return;
        }
        b = localPlayer.L();
        localPlayer.e(a.y);
        localPlayer.c(a.y);
        localPlayer.a(new Vec2f(a.x, a.y));
    }

    @bk(0)
    public void a(bg bgVar) {
        long j2 = k ^ 20520800163558L;
        long j3 = 29221066503520L ^ j2;
        long j4 = j2 ^ 96464605525824L;
        EntityLocalPlayer a2 = bgVar.a();
        boolean c2 = c();
        Vec2f L = a2.L();
        boolean z = f;
        if (!z || a == null || c == null) {
            d = L;
            c = L;
            a = L;
        }
        if (z) {
            Vec2f vec2f = b;
            if (vec2f != null && !i) {
                a2.a(vec2f);
            }
            Vec2f vec2f2 = a;
            Vec2f vec2f3 = new Vec2f(c6.a(j4, vec2f2.x, c.x + ((float) c3.a(-0.5d, j3, 0.5d)), g), c6.a(j4, vec2f2.y, c.y + ((float) c3.a(-0.5d, j3, 0.5d)), g));
            a = vec2f3;
            if (Math.abs((vec2f3.x - c.x) % 360.0f) >= 1.0f || Math.abs((a.y - c.y) % 360.0f) >= 1.0f) {
                return;
            }
            if (!h) {
                a(d, g, i);
                h = true;
                if (c2) {
                    return;
                } else {
                    PointerHolder.b(new int[5]);
                }
            }
            f = false;
        }
    }

    @bk(5)
    public void b(bb bbVar) {
        if (bbVar.e() instanceof MovePlayerPacket) {
            MovePlayerPacket movePlayerPacket = (MovePlayerPacket) bbVar.e();
            e = new Vec2f(movePlayerPacket.getRotation().getX(), movePlayerPacket.getRotation().getY());
        }
    }
}
