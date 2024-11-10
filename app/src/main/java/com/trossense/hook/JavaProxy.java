package com.trossense.hook;

import com.trossense.a9;
import com.trossense.ba;
import com.trossense.bb;
import com.trossense.bc;
import com.trossense.bd;
import com.trossense.be;
import com.trossense.bf;
import com.trossense.bg;
import com.trossense.bm;
import com.trossense.ch;
import com.trossense.clients.TrosSense;
import com.trossense.dj;
import com.trossense.f;
import com.trossense.sdk.InstanceGenerator;
import com.trossense.sdk.PointerHolder;
import com.trossense.sdk.block.Block;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import com.trossense.sdk.math.Vec3i;
import com.trossense.sdk.render.ScreenView;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketType;

/* loaded from: classes3.dex */
public class JavaProxy {
    private static final long b = dj.a(2241795502956072872L, 4495299003792457972L, MethodHandles.lookup().lookupClass()).a(190135025874482L);
    private static boolean a = true;

    public static float a(long j, long j2, int i, long j3, int i2, int i3, float f) {
        long j4 = (b ^ j3) ^ 72883035309440L;
        Block block = new Block(j2, new Vec3i(i, i2, i3));
        EntityLocalPlayer entityLocalPlayer = new EntityLocalPlayer(j);
        HopeHookEntrance.b();
        bc bcVar = new bc(block, entityLocalPlayer, f, j4);
        TrosSense.INSTANCE.d().a((a9) bcVar);
        float d = bcVar.d();
        if (PointerHolder.s() == null) {
            HopeHookEntrance.b(new int[2]);
        }
        return d;
    }

    public static void a() {
        TrosSense.INSTANCE.d().a((a9) new bf());
    }

    public static void a(int i, int i2, long j) {
        long j2 = (j ^ b) ^ 22611985091448L;
        int i3 = (int) (j2 >>> 48);
        int i4 = (int) ((j2 << 16) >>> 32);
        int i5 = (int) ((j2 << 48) >>> 48);
        int[] b2 = HopeHookEntrance.b();
        ch f = TrosSense.INSTANCE.f();
        if (f == null || i2 != 0) {
            return;
        }
        Iterator<bm> it2 = f.c().iterator();
        while (it2.hasNext()) {
            it2.next().a((short) i3, i4, (short) i5, i);
            if (b2 != null) {
                return;
            }
        }
    }

    public static void a(long j, long j2) {
    }

    public static boolean a(int i, long j, float f, float f2, int i2) {
        return f.a(i, (char) (r10 >>> 48), f, (((j ^ b) ^ 117691104339304L) << 16) >>> 16, f2, i2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static byte[] a(byte[] bArr, long j) {
        long j2 = j ^ b;
        long j3 = 94490827892583L ^ j2;
        BedrockPacket a2 = InstanceGenerator.a(bArr);
        if (!f.h) {
            f.h = true;
        }
        bb bbVar = new bb(a2);
        TrosSense.INSTANCE.d().a((a9) bbVar);
        boolean a3 = bbVar.a();
        int i = a3;
        if (j2 > 0) {
            if (a3 == 0) {
                try {
                    return InstanceGenerator.a(j3, a2);
                } catch (Exception e) {
                    return bArr;
                }
            }
            i = 0;
        }
        return new byte[i];
    }

    public static void b() {
        TrosSense.INSTANCE.d().a((a9) new bd());
    }

    public static void b(long j, long j2) {
        long j3 = (j2 ^ b) ^ 12081125440255L;
        TrosSense.INSTANCE.d().a((a9) new bg(new EntityLocalPlayer(j), j3));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static byte[] b(byte[] bArr, long j) {
        long j2 = j ^ b;
        long j3 = 116080949140724L ^ j2;
        if (!f.h) {
            f.h = true;
        }
        BedrockPacket a2 = InstanceGenerator.a(bArr);
        if (a2.getPacketType() == BedrockPacketType.AVAILABLE_COMMANDS) {
            return bArr;
        }
        ba baVar = new ba(a2);
        TrosSense.INSTANCE.d().a((a9) baVar);
        boolean a3 = baVar.a();
        int i = a3;
        if (j2 >= 0) {
            if (a3 == 0) {
                return InstanceGenerator.a(j3, a2);
            }
            i = 0;
        }
        return new byte[i];
    }

    public static void c(long j) {
        TrosSense.INSTANCE.d().a((a9) new be(new ScreenView(j)));
    }

    public static void d(long j) {
    }
}
