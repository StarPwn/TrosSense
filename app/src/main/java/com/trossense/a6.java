package com.trossense;

import com.trossense.clients.TrosSense;
import com.trossense.sdk.entity.type.EntityActor;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import com.trossense.sdk.level.Level;
import com.trossense.sdk.math.Vec3f;
import com.trossense.sdk.math.Vec3i;
import io.netty.util.internal.StringUtil;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.AdventureSettingsPacket;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetDifficultyPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetEntityDataPacket;

/* loaded from: classes3.dex */
public class a6 {
    private static com.trossense.sdk.ad a = null;
    public static HashMap<Long, Integer> b = null;
    public static Vector3f c = null;
    public static final float d = 0.25f;
    public static final float e = 0.34f;
    public static final float f = 299.0f;
    public static final float g = 9999.0f;
    private static final float h = 1.62f;
    public static final double i = 5.0d;
    private static final long j = dj.a(-6559524916680474625L, -4025502335906240920L, MethodHandles.lookup().lookupClass()).a(2909911946719L);
    private static final String[] k;

    static {
        String[] strArr = new String[2];
        int length = "ZAoT\u00036EQ\\;P\t6\rZAoT\u00036EQ\\;P\t6".length();
        int i2 = 0;
        char c2 = StringUtil.CARRIAGE_RETURN;
        int i3 = -1;
        while (true) {
            int i4 = i3 + 1;
            int i5 = c2 + i4;
            int i6 = i2 + 1;
            strArr[i2] = a(27, a("ZAoT\u00036EQ\\;P\t6\rZAoT\u00036EQ\\;P\t6".substring(i4, i5)));
            if (i5 >= length) {
                k = strArr;
                b = new HashMap<>();
                return;
            } else {
                i3 = i5;
                c2 = "ZAoT\u00036EQ\\;P\t6\rZAoT\u00036EQ\\;P\t6".charAt(i5);
                i2 = i6;
            }
        }
    }

    private static double a(Vec3i vec3i, Vec3f vec3f) {
        double d2 = (vec3i.x - vec3f.x) + 0.5d;
        double d3 = (vec3i.y - vec3f.y) + h + 1.0E-4d;
        double d4 = (vec3i.z - vec3f.z) + 0.5d;
        return Math.sqrt((d2 * d2) + (d3 * d3) + (d4 * d4));
    }

    public static float a() {
        return a.getPosition().getY();
    }

    public static float a(long j2, float f2) {
        float f3 = f2 % 0.015625f;
        float f4 = 0.010617747f;
        if (f3 >= 0.010617747f) {
            f4 = 0.010637746f;
            if (f3 <= 0.010637746f) {
                return f2;
            }
        }
        return f2 + (f4 - f3);
    }

    public static float a(Vector3f vector3f, Vector3f vector3f2) {
        return (float) Math.sqrt(Math.pow(vector3f.getX() - vector3f2.getX(), 2.0d) + Math.pow(vector3f.getZ() - vector3f2.getZ(), 2.0d));
    }

    private static String a(int i2, char[] cArr) {
        int i3;
        int length = cArr.length;
        for (int i4 = 0; length > i4; i4++) {
            char c2 = cArr[i4];
            switch (i4 % 7) {
                case 0:
                    i3 = 44;
                    break;
                case 1:
                    i3 = 51;
                    break;
                case 2:
                    i3 = 26;
                    break;
                case 3:
                    i3 = 42;
                    break;
                case 4:
                    i3 = 123;
                    break;
                case 5:
                    i3 = 95;
                    break;
                default:
                    i3 = 63;
                    break;
            }
            cArr[i4] = (char) (c2 ^ (i3 ^ i2));
        }
        return new String(cArr).intern();
    }

    public static Vector2f a(Vec3f vec3f, Vec3f vec3f2) {
        double d2 = vec3f2.x - vec3f.x;
        double d3 = vec3f2.y - vec3f.y;
        double d4 = vec3f2.z - vec3f.z;
        return Vector2f.from((float) (-((Math.atan2(d3, Math.sqrt((d2 * d2) + (d4 * d4))) * 180.0d) / 3.141592653589793d)), ((float) ((Math.atan2(d4, d2) * 180.0d) / 3.141592653589793d)) - 90.0f);
    }

    private static Vector3f a(EntityLocalPlayer entityLocalPlayer, long j2, EntityActor entityActor) {
        Vec3f N = entityActor.N();
        Vector3i from = Vector3i.from(Math.floor(N.x), N.y - h, Math.floor(N.z));
        for (int i2 = 0; i2 > -3; i2--) {
            if (!entityLocalPlayer.W().d(new Vec3i(from.getX(), from.getY() + i2, from.getZ())).t()) {
                return Vector3f.from(N.x, 1.6200000047683716d + entityLocalPlayer.W().c(new Vec3i(from.getX(), from.getY() + i2, from.getZ())).e().maxY, N.z);
            }
        }
        return null;
    }

    public static void a(int i2, char c2, EntityLocalPlayer entityLocalPlayer, Vector2f vector2f, char c3) {
        long j2 = ((((c3 << 48) >>> 48) | ((i2 << 32) | ((c2 << 48) >>> 32))) ^ j) ^ 25816975982566L;
        a(entityLocalPlayer);
        Vector3f position = a.getPosition();
        a.setDelta(Vector3f.from(vector2f.getX(), -0.25f, vector2f.getY()));
        a.setPosition(Vector3f.from(position.getX() + vector2f.getX(), position.getY() - 0.25f, position.getZ() + vector2f.getY()));
        entityLocalPlayer.d(a, j2);
    }

    public static void a(long j2, EntityLocalPlayer entityLocalPlayer, float f2) {
        long j3 = (j2 ^ j) ^ 38051936698492L;
        a(entityLocalPlayer);
        Vector3f position = a.getPosition();
        a.setDelta(Vector3f.from(0.0f, f2 - position.getY(), 0.0f));
        a.setPosition(Vector3f.from(position.getX(), f2, position.getZ()));
        entityLocalPlayer.d(a, j3);
    }

    public static void a(EntityLocalPlayer entityLocalPlayer) {
        long b2 = entityLocalPlayer.W().b();
        a.setTick(b2);
        entityLocalPlayer.W().a(b2 + 1);
    }

    public static void a(EntityLocalPlayer entityLocalPlayer, Vector3f vector3f, long j2, Vector2f vector2f) {
        long j3 = (j2 ^ j) ^ 81096631305893L;
        a(entityLocalPlayer);
        Vector3f position = a.getPosition();
        a.setDelta(Vector3f.from(vector3f.getX() - position.getX(), vector3f.getY() - position.getY(), vector3f.getY() - position.getY()));
        a.setPosition(vector3f);
        if (vector2f != null) {
            a.setRotation(Vector3f.from(vector2f.getX(), vector2f.getY(), vector2f.getY()));
        }
        entityLocalPlayer.d(a, j3);
    }

    public static boolean a(long j2, EntityLocalPlayer entityLocalPlayer, Vector3f vector3f) {
        long j3 = (j2 ^ j) ^ 42998716280228L;
        int i2 = (int) (j3 >>> 32);
        int i3 = (int) ((j3 << 32) >>> 48);
        int i4 = (int) ((j3 << 48) >>> 48);
        com.trossense.sdk.ad adVar = a;
        if (adVar == null) {
            return false;
        }
        Vector3f position = adVar.getPosition();
        Vector2f from = Vector2f.from(position.getX(), position.getZ());
        Vector2f from2 = Vector2f.from(vector3f.getX(), vector3f.getZ());
        if (from.equals(from2)) {
            return true;
        }
        float distance = from.distance(from2);
        Vector2f normalize = from2.sub(from).normalize();
        Vector2f from3 = Vector2f.from(normalize.getX() * 0.34f, normalize.getY() * 0.34f);
        if (distance > ((position.getY() - Math.max(299.0f, vector3f.getY())) / 0.25f) * 0.34f) {
            return false;
        }
        for (int i5 = 0; i5 < distance / 0.34f; i5++) {
            a(i2, (char) i3, entityLocalPlayer, from3, (char) i4);
        }
        return true;
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '?');
        }
        return charArray;
    }

    public static Vector3f b() {
        return a.getPosition();
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x00e2, code lost:            if (r0 < 0) goto L31;     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00e4, code lost:            return null;     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00f4, code lost:            return org.cloudburstmc.math.vector.Vector3f.from(r12.x + 0.5f, r9, r12.z + 0.5f);     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x00d9 A[LOOP:0: B:2:0x003c->B:18:0x00d9, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00e0 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0065  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:32:0x0056 -> B:5:0x0057). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static org.cloudburstmc.math.vector.Vector3f b(long r20, com.trossense.sdk.entity.type.EntityLocalPlayer r22, com.trossense.sdk.entity.type.EntityActor r23) {
        /*
            Method dump skipped, instructions count: 245
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.a6.b(long, com.trossense.sdk.entity.type.EntityLocalPlayer, com.trossense.sdk.entity.type.EntityActor):org.cloudburstmc.math.vector.Vector3f");
    }

    public static boolean b(long j2, EntityLocalPlayer entityLocalPlayer, Vector3f vector3f) {
        return b(entityLocalPlayer, (char) (r8 >>> 48), vector3f, null, (((j2 ^ j) ^ 32014183539548L) << 16) >>> 16);
    }

    public static boolean b(EntityLocalPlayer entityLocalPlayer, char c2, Vector3f vector3f, Vector2f vector2f, long j2) {
        long j3 = ((c2 << 48) | ((j2 << 16) >>> 16)) ^ j;
        long j4 = 89406349695571L ^ j3;
        long j5 = 110715032823241L ^ j3;
        int i2 = (int) (j5 >>> 32);
        int i3 = (int) ((j5 << 32) >>> 48);
        int i4 = (int) ((j5 << 48) >>> 48);
        long j6 = j3 ^ 63818490944650L;
        boolean b2 = a8.b();
        com.trossense.sdk.ad adVar = a;
        if (adVar == null) {
            return false;
        }
        Vector3f position = adVar.getPosition();
        Vector2f from = Vector2f.from(position.getX(), position.getZ());
        Vector2f from2 = Vector2f.from(vector3f.getX(), vector3f.getZ());
        if (from.equals(from2)) {
            a(entityLocalPlayer, vector3f, j6, vector2f);
            return true;
        }
        float distance = from.distance(from2);
        Vector2f normalize = from2.sub(from).normalize();
        Vector2f from3 = Vector2f.from(normalize.getX() * 0.34f, normalize.getY() * 0.34f);
        if (distance > ((9999.0f - Math.max(299.0f, vector3f.getY())) / 0.25f) * 0.34f) {
            return false;
        }
        a(j4, entityLocalPlayer, 9999.0f);
        int i5 = 0;
        while (i5 < distance / 0.34f) {
            a(i2, (char) i3, entityLocalPlayer, from3, (char) i4);
            i5++;
            if (b2) {
                return true;
            }
            if (b2) {
                break;
            }
        }
        a(entityLocalPlayer, vector3f, j6, null);
        entityLocalPlayer.a(new Vec3i((int) vector3f.getX(), (int) vector3f.getY(), (int) vector3f.getZ()), 1, false);
        return true;
    }

    private static boolean b(short s, int i2, EntityLocalPlayer entityLocalPlayer, short s2) {
        Vector3f vector3f;
        long j2 = ((((s2 << 48) >>> 48) | ((s << 48) | ((i2 << 32) >>> 16))) ^ j) ^ 92664978533162L;
        Vector3f position = a.getPosition();
        Vector3i from = Vector3i.from(position.getFloorX(), (int) (position.getY() - h), position.getFloorZ());
        int y = from.getY();
        while (true) {
            if (y <= 0) {
                vector3f = null;
                break;
            }
            if (!entityLocalPlayer.W().d(new Vec3i(from.getX(), y, from.getZ())).t()) {
                vector3f = Vector3f.from(position.getX(), 1.6200000047683716d + entityLocalPlayer.W().c(new Vec3i(from.getX(), y, from.getZ())).e().maxY, position.getZ());
                break;
            }
            y--;
        }
        if (vector3f == null) {
            return false;
        }
        a(entityLocalPlayer, vector3f, j2, null);
        return true;
    }

    public static boolean c(long j2, EntityLocalPlayer entityLocalPlayer) {
        Vector3f vector3f;
        long j3 = (j2 ^ j) ^ 95087490794149L;
        Vector3f position = a.getPosition();
        Vector3i from = Vector3i.from(position.getFloorX(), (int) (position.getY() - h), position.getFloorZ());
        int y = from.getY();
        while (true) {
            if (y <= 0) {
                vector3f = null;
                break;
            }
            if (!entityLocalPlayer.W().d(new Vec3i(from.getX(), y, from.getZ())).t()) {
                vector3f = Vector3f.from(position.getX(), 1.6200000047683716d + entityLocalPlayer.W().c(new Vec3i(from.getX(), y, from.getZ())).e().maxY, position.getZ());
                break;
            }
            y--;
        }
        if (vector3f == null) {
            return false;
        }
        a(entityLocalPlayer, vector3f, j3, null);
        return true;
    }

    public static boolean c(long j2, EntityLocalPlayer entityLocalPlayer, EntityActor entityActor) {
        long j3 = j2 ^ j;
        long j4 = 82089851869167L ^ j3;
        long j5 = 96167338600837L ^ j3;
        long j6 = j3 ^ 100696955918284L;
        int i2 = (int) (j6 >>> 48);
        long j7 = (j6 << 16) >>> 16;
        if (a == null) {
            return false;
        }
        Vector3f a2 = a(entityLocalPlayer, j5, entityActor);
        if (a2 == null && (a2 = b(j4, entityLocalPlayer, entityActor)) == null) {
            return false;
        }
        Vector3f vector3f = a2;
        return b(entityLocalPlayer, (char) i2, vector3f, a(new Vec3f(vector3f.getX(), vector3f.getY(), vector3f.getZ()), entityActor.N()), j7);
    }

    @bk
    public void a(ba baVar) {
        if ((baVar.e() instanceof SetDifficultyPacket) || (baVar.e() instanceof AdventureSettingsPacket)) {
            c = null;
        }
    }

    @bk
    public void a(bb bbVar) {
        EntityLocalPlayer localPlayer;
        com.trossense.sdk.ad adVar;
        long j2 = j ^ 66098779535490L;
        long j3 = 58100024012136L ^ j2;
        long j4 = j2 ^ 22066511435995L;
        if (bbVar.e() instanceof InventoryTransactionPacket) {
            if (((bx.m == null || !bx.m.l()) && (b5.j == null || !b5.j.l())) || (localPlayer = TrosSense.INSTANCE.getLocalPlayer()) == null || (adVar = a) == null || adVar.getPosition().getY() <= 800.0f) {
                return;
            }
            Vector3f position = a.getPosition();
            a(localPlayer, Vector3f.from(position.getX(), localPlayer.N().y, position.getZ()), j4, null);
            bbVar.c();
            localPlayer.d(bbVar.e(), j3);
            a(localPlayer, position, j4, null);
        }
    }

    @bk
    public void a(bg bgVar) {
        EntityLocalPlayer a2 = bgVar.a();
        Level W = a2.W();
        Vec3f N = a2.N();
        Vector3i from = Vector3i.from(Math.floor(N.x), (N.y - h) - 0.01f, N.z);
        if (W.d(new Vec3i(from.getX(), from.getY(), from.getZ())).t()) {
            return;
        }
        c = Vector3f.from(N.x, N.y, N.z);
    }

    @bk
    public void b(ba baVar) {
        EntityLocalPlayer localPlayer;
        long j2 = (j ^ 89470170032272L) ^ 133399358900425L;
        if (!(baVar.e() instanceof MovePlayerPacket) || (localPlayer = TrosSense.INSTANCE.getLocalPlayer()) == null) {
            return;
        }
        MovePlayerPacket movePlayerPacket = (MovePlayerPacket) baVar.e();
        Vector3f position = movePlayerPacket.getPosition();
        if (position.getY() <= 800.0f || localPlayer.C() != movePlayerPacket.getRuntimeEntityId()) {
            return;
        }
        a(localPlayer, position, j2, null);
        movePlayerPacket.setPosition(Vector3f.from(position.getX(), localPlayer.N().y, position.getZ()));
    }

    @bk(4)
    public void b(bb bbVar) {
        if (bbVar.e() instanceof com.trossense.sdk.ad) {
            a = (com.trossense.sdk.ad) bbVar.e();
        }
    }

    @bk
    public void b(bg bgVar) {
        bgVar.a();
        Iterator<Map.Entry<Long, Integer>> it2 = b.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry<Long, Integer> next = it2.next();
            Long key = next.getKey();
            int intValue = next.getValue().intValue();
            if (intValue > 160) {
                it2.remove();
            } else {
                b.put(key, Integer.valueOf(intValue + 1));
            }
        }
    }

    @bk
    public void c(ba baVar) {
        if (baVar.e() instanceof SetEntityDataPacket) {
            SetEntityDataPacket setEntityDataPacket = (SetEntityDataPacket) baVar.e();
            if (setEntityDataPacket.getMetadata().getFlags().contains(EntityFlag.INVISIBLE)) {
                b.put(Long.valueOf(setEntityDataPacket.getRuntimeEntityId()), 0);
            }
        }
    }
}
