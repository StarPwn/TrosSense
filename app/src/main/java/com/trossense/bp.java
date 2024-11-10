package com.trossense;

import com.trossense.clients.TrosSense;
import com.trossense.sdk.entity.type.EntityActor;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import com.trossense.sdk.math.Vec3f;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetEntityMotionPacket;

@cg(a = "InfiniteAura", b = b.Combat, c = "百米大刀", d = 54)
/* loaded from: classes3.dex */
public class bp extends bm {
    private static final long o = dj.a(2486631851791519696L, -1142753120797678823L, MethodHandles.lookup().lookupClass()).a(57257858624529L);
    private static final String[] p;
    private final cp j;
    private List<EntityActor> k;
    private c7 l;
    private c7 m;
    private com.trossense.sdk.ad n;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0044. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[4];
        String str = "按成\u0005\u007f,9Aa";
        int length = "按成\u0005\u007f,9Aa".length();
        char c = 2;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 76;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String b = b(i5, b(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = b;
                        i4 = i6 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = " \u0013ÜhymZt#\u001ej^z\u0015 4\u001epH|A'6\u0017eJv[`^U\u001d \u0014ÜhymZt#\u001ej^z\u0015 \u0016ÜhlkAf\u0013\u0010mCx\u001b)";
                        length = " \u0013ÜhymZt#\u001ej^z\u0015 4\u001epH|A'6\u0017eJv[`^U\u001d \u0014ÜhymZt#\u001ej^z\u0015 \u0016ÜhlkAf\u0013\u0010mCx\u001b)".length();
                        c = 31;
                        i2 = -1;
                        i3 = i;
                        i5 = 103;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = b;
                i2 = i6 + c;
                if (i2 >= length) {
                    p = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 103;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i4);
            i3 = i;
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public bp(long r12) {
        /*
            r11 = this;
            long r0 = com.trossense.bp.o
            long r12 = r12 ^ r0
            r0 = 36241287968442(0x20f6151876ba, double:1.7905575346247E-310)
            long r0 = r0 ^ r12
            r2 = 113181400626440(0x66f0192df508, double:5.5919041797719E-310)
            long r12 = r12 ^ r2
            r2 = 16
            long r4 = r12 >>> r2
            r2 = 48
            long r12 = r12 << r2
            long r12 = r12 >>> r2
            int r12 = (int) r12
            r11.<init>(r0)
            com.trossense.cp r13 = new com.trossense.cp
            short r6 = (short) r12
            java.lang.String[] r12 = com.trossense.bp.p
            r0 = 1
            r7 = r12[r0]
            r1 = 0
            r8 = r12[r1]
            java.lang.Boolean r10 = java.lang.Boolean.valueOf(r0)
            r3 = r13
            r9 = r11
            r3.<init>(r4, r6, r7, r8, r9, r10)
            r11.j = r13
            com.trossense.c7 r12 = new com.trossense.c7
            r12.<init>()
            r11.l = r12
            com.trossense.c7 r12 = new com.trossense.c7
            r12.<init>()
            r11.m = r12
            java.util.ArrayList r12 = new java.util.ArrayList
            r12.<init>()
            r11.k = r12
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.bp.<init>(long):void");
    }

    public static float a(Vec3f vec3f, Vec3f vec3f2) {
        return (float) Math.sqrt(Math.pow(vec3f.x - vec3f2.x, 2.0d) + Math.pow(vec3f.z - vec3f2.z, 2.0d));
    }

    private static Vec3f a(Vector3f vector3f) {
        return new Vec3f(vector3f.getX(), vector3f.getY(), vector3f.getZ());
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00ee  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void a(com.trossense.sdk.entity.type.EntityLocalPlayer r21, long r22) {
        /*
            Method dump skipped, instructions count: 252
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.bp.a(com.trossense.sdk.entity.type.EntityLocalPlayer, long):void");
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 96;
                    break;
                case 1:
                    i2 = 23;
                    break;
                case 2:
                    i2 = 28;
                    break;
                case 3:
                    i2 = 99;
                    break;
                case 4:
                    i2 = 74;
                    break;
                case 5:
                    i2 = 120;
                    break;
                default:
                    i2 = 82;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private void b(long j, EntityLocalPlayer entityLocalPlayer) {
        List<EntityActor> c = c8.c((j ^ o) ^ 88808105164310L, entityLocalPlayer, 100.0f);
        int[] o2 = br.o();
        this.k = c;
        Iterator<EntityActor> it2 = c.iterator();
        while (it2.hasNext()) {
            if (a6.b.containsKey(Long.valueOf(it2.next().C()))) {
                it2.remove();
            }
            if (o2 == null) {
                return;
            }
        }
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'R');
        }
        return charArray;
    }

    @bk
    public void a(ba baVar) {
        EntityLocalPlayer localPlayer;
        if (baVar.e() instanceof MovePlayerPacket) {
            EntityLocalPlayer localPlayer2 = TrosSense.INSTANCE.getLocalPlayer();
            MovePlayerPacket movePlayerPacket = (MovePlayerPacket) baVar.e();
            if (localPlayer2 != null && movePlayerPacket.getRuntimeEntityId() == localPlayer2.C()) {
                localPlayer2.a(p[2]);
                Vector3f position = ((MovePlayerPacket) baVar.e()).getPosition();
                if (this.n != null && a(new Vec3f(position.getX(), position.getY(), position.getZ()), a(this.n.getPosition())) < 10.0f) {
                    this.l.a();
                }
            }
        }
        if ((baVar.e() instanceof SetEntityMotionPacket) && (localPlayer = TrosSense.INSTANCE.getLocalPlayer()) != null && ((SetEntityMotionPacket) baVar.e()).getRuntimeEntityId() == localPlayer.C()) {
            baVar.c();
        }
    }

    @bk
    public void a(bb bbVar) {
        if (bbVar.e() instanceof com.trossense.sdk.ad) {
            this.n = (com.trossense.sdk.ad) bbVar.e();
        }
    }

    @bk(3)
    public void a(bg bgVar) {
        long j = o ^ 120008294989197L;
        long j2 = 92104146160098L ^ j;
        long j3 = j ^ 121866199521953L;
        EntityLocalPlayer a = bgVar.a();
        b(j2, a);
        if (this.k.isEmpty()) {
            return;
        }
        a(a, j3);
    }

    @Override // com.trossense.bm
    public void j(long j) {
        this.m.a();
        this.l.a();
        this.k = new ArrayList();
    }
}
