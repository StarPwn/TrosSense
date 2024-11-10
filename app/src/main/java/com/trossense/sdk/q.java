package com.trossense.sdk;

import com.trossense.bl;
import com.trossense.dj;
import io.netty.buffer.ByteBuf;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.common.util.Int2ObjectBiMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes3.dex */
public class q implements BedrockPacketSerializer<n> {
    private static final Int2ObjectBiMap<a> a;
    private static final long b = dj.a(7207952753355586860L, -6637380005691911545L, MethodHandles.lookup().lookupClass()).a(97662034409329L);

    static {
        Int2ObjectBiMap<a> int2ObjectBiMap = new Int2ObjectBiMap<>();
        a = int2ObjectBiMap;
        int2ObjectBiMap.put(0, a.NO_ACTION);
        int2ObjectBiMap.put(1, a.SWING_ARM);
        int2ObjectBiMap.put(3, a.WAKE_UP);
        int2ObjectBiMap.put(4, a.CRITICAL_HIT);
        int2ObjectBiMap.put(5, a.MAGIC_CRITICAL_HIT);
        int2ObjectBiMap.put(128, a.ROW_RIGHT);
        int2ObjectBiMap.put(bl.bq, a.ROW_LEFT);
    }

    public void a(ByteBuf byteBuf, long j, BedrockCodecHelper bedrockCodecHelper, n nVar) {
        a b2 = nVar.b();
        VarInts.writeInt(byteBuf, a.get((Int2ObjectBiMap<a>) b2));
        VarInts.writeUnsignedLong(byteBuf, nVar.c());
        if (b2 == a.ROW_LEFT || b2 == a.ROW_RIGHT) {
            byteBuf.writeFloatLE(nVar.a());
        }
        if (nVar.b() == a.CRITICAL_HIT && nVar.d()) {
            VarInts.writeUnsignedLong(byteBuf, nVar.e());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x001e, code lost:            if (r4 == r6) goto L7;     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void b(io.netty.buffer.ByteBuf r3, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper r4, byte r5, int r6, int r7, com.trossense.sdk.n r8) {
        /*
            r2 = this;
            org.cloudburstmc.protocol.common.util.Int2ObjectBiMap<com.trossense.sdk.a> r4 = com.trossense.sdk.q.a
            int r6 = org.cloudburstmc.protocol.common.util.VarInts.readInt(r3)
            java.lang.Object r4 = r4.get(r6)
            com.trossense.sdk.a r4 = (com.trossense.sdk.a) r4
            r8.a(r4)
            long r0 = org.cloudburstmc.protocol.common.util.VarInts.readUnsignedLong(r3)
            r8.a(r0)
            com.trossense.sdk.a r6 = com.trossense.sdk.a.ROW_LEFT
            if (r4 == r6) goto L20
            com.trossense.sdk.a r6 = com.trossense.sdk.a.ROW_RIGHT
            if (r7 < 0) goto L2f
            if (r4 != r6) goto L27
        L20:
            float r4 = r3.readFloatLE()
            r8.a(r4)
        L27:
            if (r7 < 0) goto L31
            com.trossense.sdk.a r4 = r8.b()
            com.trossense.sdk.a r6 = com.trossense.sdk.a.CRITICAL_HIT
        L2f:
            if (r4 != r6) goto L4e
        L31:
            int r4 = r3.readableBytes()
            if (r7 < 0) goto L3c
            if (r4 == 0) goto L3b
            r4 = 1
            goto L3c
        L3b:
            r4 = 0
        L3c:
            r8.a(r4)
            if (r5 < 0) goto L47
            boolean r4 = r8.d()
            if (r4 == 0) goto L4e
        L47:
            long r3 = org.cloudburstmc.protocol.common.util.VarInts.readUnsignedLong(r3)
            r8.b(r3)
        L4e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.sdk.q.b(io.netty.buffer.ByteBuf, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper, byte, int, int, com.trossense.sdk.n):void");
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public /* bridge */ /* synthetic */ void deserialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, n nVar) {
        long j = (b ^ 136704006019804L) ^ 109668539932847L;
        b(byteBuf, bedrockCodecHelper, (byte) (j >>> 56), (int) ((j << 8) >>> 32), (int) ((j << 40) >>> 40), nVar);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public /* bridge */ /* synthetic */ void serialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, n nVar) {
        a(byteBuf, (b ^ 116507697451335L) ^ 78225151387123L, bedrockCodecHelper, nVar);
    }
}
