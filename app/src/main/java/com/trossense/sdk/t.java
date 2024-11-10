package com.trossense.sdk;

import com.trossense.dj;
import io.netty.buffer.ByteBuf;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes3.dex */
public class t implements BedrockPacketSerializer<p> {
    private static final long a = dj.a(-6031591452086721947L, -2742253104054844813L, MethodHandles.lookup().lookupClass()).a(210236483484742L);

    public void a(ByteBuf byteBuf, long j, BedrockCodecHelper bedrockCodecHelper, p pVar) {
        byteBuf.writeByte(pVar.a());
        byteBuf.writeByte(pVar.b().getId());
        bedrockCodecHelper.writeBlockPosition(byteBuf, pVar.c());
        VarInts.writeLong(byteBuf, pVar.d());
        if (pVar.e()) {
            byteBuf.writeBoolean(pVar.f());
        }
    }

    public void b(long j, ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, p pVar) {
        long j2 = j ^ a;
        pVar.a(byteBuf.readByte());
        pVar.a(ContainerType.from(byteBuf.readByte()));
        pVar.a(bedrockCodecHelper.readBlockPosition(byteBuf));
        pVar.a(VarInts.readLong(byteBuf));
        pVar.a(byteBuf.readableBytes() != 0);
        if (j2 <= 0 || pVar.e()) {
            pVar.b(byteBuf.readBoolean());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public /* bridge */ /* synthetic */ void deserialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, p pVar) {
        b((a ^ 81470373414467L) ^ 29427387099730L, byteBuf, bedrockCodecHelper, pVar);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public /* bridge */ /* synthetic */ void serialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, p pVar) {
        a(byteBuf, (a ^ 106006992726307L) ^ 10390942378232L, bedrockCodecHelper, pVar);
    }
}
