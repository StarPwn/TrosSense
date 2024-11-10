package com.trossense;

import com.trossense.sdk.PointerHolder;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import com.trossense.sdk.math.Vec2f;
import com.trossense.sdk.math.Vec3f;
import com.trossense.sdk.math.Vec3i;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class c6 {
    private static final long a = dj.a(-9179836494313882010L, -5712464222429948830L, MethodHandles.lookup().lookupClass()).a(260627255738953L);
    private static String b;

    static {
        if (b() != null) {
            b("laZSEb");
        }
    }

    public static float a(long j, float f, float f2, float f3) {
        float a2 = c2.a(f2 - f, (j ^ a) ^ 64754206544969L);
        if (a2 > f3) {
            a2 = f3;
        }
        float f4 = -f3;
        if (a2 < f4) {
            a2 = f4;
        }
        return f + a2;
    }

    public static Vec2f a(EntityLocalPlayer entityLocalPlayer, long j, Vec3f vec3f) {
        String b2 = b();
        Vec3f N = entityLocalPlayer.N();
        double d = vec3f.x - N.x;
        double d2 = vec3f.y - N.y;
        double d3 = vec3f.z - N.z;
        Vec2f vec2f = new Vec2f((float) (-((Math.atan2(d2, Math.sqrt((d * d) + (d3 * d3))) * 180.0d) / 3.141592653589793d)), ((float) ((Math.atan2(d3, d) * 180.0d) / 3.141592653589793d)) - 90.0f);
        if (b2 != null) {
            PointerHolder.b(new int[4]);
        }
        return vec2f;
    }

    public static Vec3i a(char c, EntityLocalPlayer entityLocalPlayer, short s, int i) {
        float a2 = c2.a(entityLocalPlayer.L().y, ((((c << 48) | ((s << 48) >>> 16)) | ((i << 32) >>> 32)) ^ a) ^ 84739554619202L) + 180.0f;
        if (a2 <= 325.0f) {
            int i2 = (a2 > 35.0f ? 1 : (a2 == 35.0f ? 0 : -1));
            if (c >= 0) {
                if (i2 >= 0) {
                    i2 = (a2 > 55.0f ? 1 : (a2 == 55.0f ? 0 : -1));
                }
            }
            if (s >= 0) {
                if (i2 < 0) {
                    return new Vec3i(1, 0, -1);
                }
                i2 = (a2 > 125.0f ? 1 : (a2 == 125.0f ? 0 : -1));
            }
            if (i < 0) {
                if (i2 < 0) {
                    return com.trossense.sdk.block.a.EAST.b();
                }
                i2 = (a2 > 145.0f ? 1 : (a2 == 145.0f ? 0 : -1));
            }
            if (c >= 0) {
                if (i2 < 0) {
                    return new Vec3i(1, 0, 1);
                }
                i2 = (a2 > 215.0f ? 1 : (a2 == 215.0f ? 0 : -1));
            }
            if (i <= 0) {
                if (i2 < 0) {
                    return com.trossense.sdk.block.a.SOUTH.b();
                }
                i2 = (a2 > 235.0f ? 1 : (a2 == 235.0f ? 0 : -1));
            }
            if (i < 0) {
                if (i2 < 0) {
                    return new Vec3i(-1, 0, 1);
                }
                i2 = (a2 > 305.0f ? 1 : (a2 == 305.0f ? 0 : -1));
            }
            return i2 < 0 ? com.trossense.sdk.block.a.WEST.b() : new Vec3i(-1, 0, -1);
        }
        return com.trossense.sdk.block.a.NORTH.b();
    }

    public static Vec2f b(long j, EntityLocalPlayer entityLocalPlayer, Vec3f vec3f) {
        Vec2f a2 = a(entityLocalPlayer, (j ^ a) ^ 52980331809401L, vec3f);
        Vec2f L = entityLocalPlayer.L();
        return new Vec2f(L.x - a2.x, a2.y - L.y);
    }

    public static String b() {
        return b;
    }

    public static void b(String str) {
        b = str;
    }
}
