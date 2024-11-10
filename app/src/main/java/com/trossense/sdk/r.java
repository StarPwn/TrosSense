package com.trossense.sdk;

import com.trossense.dj;
import io.netty.buffer.ByteBuf;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes3.dex */
public class r implements BedrockPacketSerializer<o> {
    private static final long a = dj.a(6761533337391095897L, 61893720765256628L, MethodHandles.lookup().lookupClass()).a(246706308895687L);

    public void a(ByteBuf byteBuf, long j, BedrockCodecHelper bedrockCodecHelper, o oVar) {
        bedrockCodecHelper.writeString(byteBuf, oVar.a());
        bedrockCodecHelper.writeCommandOrigin(byteBuf, oVar.b());
        byteBuf.writeBoolean(oVar.c());
        VarInts.writeInt(byteBuf, oVar.d());
        if (oVar.e()) {
            byteBuf.writeBoolean(oVar.f());
        }
    }

    public void b(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, long j, o oVar) {
        long j2 = j ^ a;
        oVar.a(bedrockCodecHelper.readString(byteBuf));
        oVar.a(bedrockCodecHelper.readCommandOrigin(byteBuf));
        oVar.a(byteBuf.readBoolean());
        oVar.a(VarInts.readInt(byteBuf));
        oVar.b(byteBuf.readableBytes() != 0);
        if (j2 < 0 || oVar.e()) {
            oVar.c(byteBuf.readBoolean());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public /* bridge */ /* synthetic */ void deserialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, o oVar) {
        b(byteBuf, bedrockCodecHelper, (a ^ 31096015908368L) ^ 89380251853140L, oVar);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public /* bridge */ /* synthetic */ void serialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, o oVar) {
        a(byteBuf, (a ^ 127862608664507L) ^ 98525958421082L, bedrockCodecHelper, oVar);
    }
}
