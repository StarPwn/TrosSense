package com.trossense.sdk;

import com.trossense.dj;
import io.netty.buffer.ByteBuf;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes3.dex */
public class s implements BedrockPacketSerializer<j> {
    private static final long b = dj.a(4429555186767410670L, -5896884049146433686L, MethodHandles.lookup().lookupClass()).a(249091721842294L);
    public static final s a = new s();

    /* JADX WARN: Code restructure failed: missing block: B:49:0x0037, code lost:            if (r7 >= 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x002b, code lost:            r1 = r2;     */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:34:0x0037 -> B:7:0x003b). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(long r7, io.netty.buffer.ByteBuf r9, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper r10, com.trossense.sdk.j r11) {
        /*
            r6 = this;
            long r0 = com.trossense.sdk.s.b
            long r7 = r7 ^ r0
            java.lang.String[] r0 = com.trossense.sdk.x.b()
            java.util.List r1 = r11.a()
            int r1 = r1.size()
            org.cloudburstmc.protocol.common.util.VarInts.writeUnsignedInt(r9, r1)
            java.util.List r1 = r11.a()
            java.util.Iterator r1 = r1.iterator()
        L1a:
            boolean r2 = r1.hasNext()
            r3 = 0
            if (r2 == 0) goto L2d
            int r2 = (r7 > r3 ? 1 : (r7 == r3 ? 0 : -1))
            if (r2 <= 0) goto L2b
            java.lang.Object r2 = r1.next()
            goto L3b
        L2b:
            r2 = r1
            goto L57
        L2d:
            java.util.List r2 = r11.a()
            java.util.Iterator r2 = r2.iterator()
            int r5 = (r7 > r3 ? 1 : (r7 == r3 ? 0 : -1))
            if (r5 < 0) goto L3b
        L39:
            r1 = r2
            goto L2b
        L3b:
            com.trossense.sdk.k r2 = (com.trossense.sdk.k) r2
            boolean r5 = r2.a()
            r9.writeBoolean(r5)
            java.util.UUID r5 = r2.b()
            r10.writeUuid(r9, r5)
            byte[] r2 = r2.c()
            r10.writeByteArray(r9, r2)
            if (r0 != 0) goto L2b
            if (r0 == 0) goto L1a
            goto L2d
        L57:
            int r5 = (r7 > r3 ? 1 : (r7 == r3 ? 0 : -1))
            if (r5 <= 0) goto L7e
            boolean r1 = r1.hasNext()
            if (r1 == 0) goto L74
            if (r5 < 0) goto L7f
            java.lang.Object r1 = r2.next()
            com.trossense.sdk.k r1 = (com.trossense.sdk.k) r1
            java.lang.String r1 = r1.d()
            r10.writeString(r9, r1)
            if (r0 != 0) goto L7f
            if (r0 == 0) goto L39
        L74:
            java.util.List r1 = r11.a()
            java.util.Iterator r1 = r1.iterator()
            if (r5 <= 0) goto L57
        L7e:
            r2 = r1
        L7f:
            int r11 = (r7 > r3 ? 1 : (r7 == r3 ? 0 : -1))
            if (r11 < 0) goto L8e
            boolean r11 = r2.hasNext()
            if (r11 == 0) goto L9a
            java.lang.Object r11 = r2.next()
            goto L8f
        L8e:
            r11 = r2
        L8f:
            com.trossense.sdk.k r11 = (com.trossense.sdk.k) r11
            java.lang.String r11 = r11.e()
            r10.writeString(r9, r11)
            if (r0 == 0) goto L7f
        L9a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.sdk.s.a(long, io.netty.buffer.ByteBuf, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper, com.trossense.sdk.j):void");
    }

    public void b(long j, ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, j jVar) {
        int readUnsignedInt = VarInts.readUnsignedInt(byteBuf);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < readUnsignedInt; i++) {
            k kVar = new k();
            kVar.a(byteBuf.readBoolean());
            kVar.a(bedrockCodecHelper.readUuid(byteBuf));
            kVar.a(bedrockCodecHelper.readByteArray(byteBuf));
            arrayList.add(kVar);
        }
        if (byteBuf.isReadable()) {
            for (int i2 = 0; i2 < readUnsignedInt; i2++) {
                arrayList.get(i2).a(bedrockCodecHelper.readString(byteBuf));
            }
        }
        if (byteBuf.isReadable()) {
            for (int i3 = 0; i3 < readUnsignedInt; i3++) {
                arrayList.get(i3).b(bedrockCodecHelper.readString(byteBuf));
            }
        }
        jVar.a((List<k>) arrayList);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public /* bridge */ /* synthetic */ void deserialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, j jVar) {
        b((b ^ 86535768573892L) ^ 114287128419634L, byteBuf, bedrockCodecHelper, jVar);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public /* bridge */ /* synthetic */ void serialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, j jVar) {
        a((b ^ 81296338443757L) ^ 16021682052488L, byteBuf, bedrockCodecHelper, jVar);
    }
}
