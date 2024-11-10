package com.trossense.sdk;

import com.trossense.ct;
import com.trossense.dj;
import com.trossense.hook.HopeHookEntrance;
import com.trossense.sdk.entity.type.EntityActor;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantLock;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.UnknownPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes3.dex */
public class InstanceGenerator {
    private static h a;
    private static d b;
    private static BedrockCodecHelper c;
    private static BedrockCodec d;
    private static int e;
    private static int f;
    private static ByteBuf g;
    private static final ReentrantLock h;
    private static final long i;
    private static final String[] j;

    static {
        long a2 = dj.a(-1846880253461800082L, -6739687513165530944L, MethodHandles.lookup().lookupClass()).a(277390134867874L);
        i = a2;
        long j2 = a2 ^ 75685545973117L;
        long j3 = 44381013502201L ^ j2;
        int i2 = (int) (j3 >>> 48);
        int i3 = (int) ((j3 << 16) >>> 48);
        int i4 = (int) ((j3 << 32) >>> 32);
        long j4 = j2 ^ 27248596430593L;
        String[] strArr = new String[3];
        int length = "bfHf_>\u0018i{\u001cjH)\u0014\u0018n|UfH?VajRtS>\u0012 mJl_'WknR\u0018n|UfH?VajRtS>\u0012 fRfQb\u0013|`H".length();
        char c2 = 14;
        int i5 = -1;
        int i6 = 0;
        while (true) {
            int i7 = i5 + 1;
            int i8 = c2 + i7;
            int i9 = i6 + 1;
            strArr[i6] = a(109, a("bfHf_>\u0018i{\u001cjH)\u0014\u0018n|UfH?VajRtS>\u0012 mJl_'WknR\u0018n|UfH?VajRtS>\u0012 fRfQb\u0013|`H".substring(i7, i8)));
            if (i8 >= length) {
                j = strArr;
                e = 0;
                f = 0;
                h = new ReentrantLock();
                g = ByteBufAllocator.DEFAULT.buffer(10485760);
                BedrockCodec bedrockCodec = ab.a;
                d = bedrockCodec;
                c = bedrockCodec.createHelper();
                a = new h(j4, new InputStreamReader(HopeHookEntrance.class.getClassLoader().getResourceAsStream(strArr[2]), StandardCharsets.UTF_8));
                b = new d((short) i2, HopeHookEntrance.class.getClassLoader().getResourceAsStream(strArr[1]), (short) i3, i4);
                c.setItemDefinitions(a);
                c.setBlockDefinitions(b);
                return;
            }
            i6 = i9;
            i5 = i8;
            c2 = "bfHf_>\u0018i{\u001cjH)\u0014\u0018n|UfH?VajRtS>\u0012 mJl_'WknR\u0018n|UfH?VajRtS>\u0012 fRfQb\u0013|`H".charAt(i8);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0039 A[EDGE_INSN: B:16:0x0039->B:9:0x0039 BREAK  A[LOOP:0: B:2:0x0010->B:7:?], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0049 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:? A[LOOP:0: B:2:0x0010->B:7:?, LOOP_END, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:12:0x0037 -> B:5:0x0049). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static io.netty.buffer.ByteBuf a(long r8, java.lang.String r10) {
        /*
            long r0 = com.trossense.sdk.InstanceGenerator.i
            long r8 = r8 ^ r0
            int r0 = r10.length()
            java.lang.String r1 = com.trossense.ct.l()
            int r2 = r0 / 2
            byte[] r2 = new byte[r2]
            r3 = 0
        L10:
            if (r3 >= r0) goto L33
            if (r1 == 0) goto L39
            int r4 = r3 / 2
            char r5 = r10.charAt(r3)
            r6 = 16
            int r5 = java.lang.Character.digit(r5, r6)
            int r5 = r5 << 4
            int r7 = r3 + 1
            char r7 = r10.charAt(r7)
            int r6 = java.lang.Character.digit(r7, r6)
            int r5 = r5 + r6
            byte r5 = (byte) r5
            r2[r4] = r5
            int r3 = r3 + 2
            goto L49
        L33:
            r4 = 0
            int r4 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1))
            if (r4 <= 0) goto L49
        L39:
            io.netty.buffer.ByteBuf r8 = io.netty.buffer.Unpooled.wrappedBuffer(r2)
            int[] r9 = com.trossense.sdk.PointerHolder.s()
            if (r9 != 0) goto L48
            java.lang.String r9 = "RqlYUc"
            com.trossense.ct.b(r9)
        L48:
            return r8
        L49:
            if (r1 != 0) goto L10
            goto L33
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.sdk.InstanceGenerator.a(long, java.lang.String):io.netty.buffer.ByteBuf");
    }

    private static String a(int i2, char[] cArr) {
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            int i4 = 98;
            switch (i3 % 7) {
                case 0:
                case 1:
                    break;
                case 2:
                    i4 = 75;
                    break;
                case 3:
                    i4 = 110;
                    break;
                case 4:
                    i4 = 81;
                    break;
                case 5:
                    i4 = 33;
                    break;
                default:
                    i4 = 20;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i4));
        }
        return new String(cArr).intern();
    }

    public static BedrockPacket a(byte[] bArr) {
        ByteBuf wrappedBuffer = Unpooled.wrappedBuffer(bArr);
        int readUnsignedInt = VarInts.readUnsignedInt(wrappedBuffer);
        e = (readUnsignedInt >> 10) & 3;
        f = (readUnsignedInt >> 12) & 3;
        return d.tryDecode(c, wrappedBuffer, readUnsignedInt & 1023);
    }

    public static void a() {
    }

    public static byte[] a(long j2, BedrockPacket bedrockPacket) {
        int id;
        long j3 = j2 ^ i;
        String l = ct.l();
        ReentrantLock reentrantLock = h;
        reentrantLock.lock();
        if (bedrockPacket instanceof UnknownPacket) {
            id = ((UnknownPacket) bedrockPacket).getPacketId();
            if (j3 >= 0) {
                if (l == null) {
                    PointerHolder.b(new int[1]);
                }
                VarInts.writeUnsignedInt(g, (id & 1023) | id | ((e & 3) << 10) | ((f & 3) << 12));
                d.tryEncode(c, g, bedrockPacket);
            }
            byte[] bArr = new byte[g.readableBytes()];
            g.readBytes(bArr);
            g.clear();
            reentrantLock.unlock();
            return bArr;
        }
        id = d.getPacketDefinition(bedrockPacket.getClass()).getId();
        VarInts.writeUnsignedInt(g, (id & 1023) | id | ((e & 3) << 10) | ((f & 3) << 12));
        d.tryEncode(c, g, bedrockPacket);
        byte[] bArr2 = new byte[g.readableBytes()];
        g.readBytes(bArr2);
        g.clear();
        reentrantLock.unlock();
        return bArr2;
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 20);
        }
        return charArray;
    }

    public static int b(int i2, short s, char c2, BedrockPacket bedrockPacket) {
        return bedrockPacket instanceof UnknownPacket ? ((UnknownPacket) bedrockPacket).getPacketId() : d.getPacketDefinition(bedrockPacket.getClass()).getId();
    }

    public static EntityActor generatorEntity(long j2) {
        if (j2 == 0) {
            return null;
        }
        EntityActor entityActor = new EntityActor(j2);
        return entityActor.d() == 319 ? entityActor.U() ? new EntityLocalPlayer(j2) : new com.trossense.sdk.entity.type.c(j2) : entityActor.z().equals(j[0]) ? new com.trossense.sdk.entity.type.a(j2) : entityActor;
    }

    public static ItemData generatorItemData(byte[] bArr) {
        return c.readItem(Unpooled.wrappedBuffer(bArr));
    }

    public static EntityActor generatorPlayer(long j2) {
        if (j2 == 0) {
            return null;
        }
        return new EntityActor(j2).U() ? new EntityLocalPlayer(j2) : new com.trossense.sdk.entity.type.c(j2);
    }
}
