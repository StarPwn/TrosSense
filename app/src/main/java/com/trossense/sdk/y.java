package com.trossense.sdk;

import com.trossense.dj;
import io.netty.buffer.ByteBuf;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v557.serializer.AddEntitySerializer_v557;
import org.cloudburstmc.protocol.bedrock.packet.AddEntityPacket;

/* loaded from: classes3.dex */
public class y extends AddEntitySerializer_v557 {
    private static final long a = dj.a(-6364422276104540439L, 7675742817184208274L, MethodHandles.lookup().lookupClass()).a(53997167878898L);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v557.serializer.AddEntitySerializer_v557, org.cloudburstmc.protocol.bedrock.codec.v534.serializer.AddEntitySerializer_v534, org.cloudburstmc.protocol.bedrock.codec.v313.serializer.AddEntitySerializer_v313, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddEntitySerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, AddEntityPacket addEntityPacket) {
        super.deserialize(byteBuf, bedrockCodecHelper, addEntityPacket);
        ac acVar = (ac) addEntityPacket;
        acVar.a(byteBuf.readableBytes() != 0);
        if (acVar.a()) {
            acVar.a(bedrockCodecHelper.readString(byteBuf));
            acVar.b(bedrockCodecHelper.readString(byteBuf));
            acVar.c(bedrockCodecHelper.readString(byteBuf));
            acVar.b(byteBuf.readBoolean());
            acVar.c(byteBuf.readBoolean());
            acVar.d(byteBuf.readBoolean());
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v557.serializer.AddEntitySerializer_v557, org.cloudburstmc.protocol.bedrock.codec.v534.serializer.AddEntitySerializer_v534, org.cloudburstmc.protocol.bedrock.codec.v313.serializer.AddEntitySerializer_v313, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddEntitySerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, AddEntityPacket addEntityPacket) {
        super.serialize(byteBuf, bedrockCodecHelper, addEntityPacket);
        ac acVar = (ac) addEntityPacket;
        if (acVar.a()) {
            bedrockCodecHelper.writeString(byteBuf, acVar.b());
            bedrockCodecHelper.writeString(byteBuf, acVar.c());
            bedrockCodecHelper.writeString(byteBuf, acVar.d());
            byteBuf.writeBoolean(acVar.e());
            byteBuf.writeBoolean(acVar.f());
            byteBuf.writeBoolean(acVar.g());
        }
    }
}
