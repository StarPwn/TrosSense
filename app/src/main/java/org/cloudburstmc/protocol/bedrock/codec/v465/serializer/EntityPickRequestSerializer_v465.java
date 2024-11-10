package org.cloudburstmc.protocol.bedrock.codec.v465.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EntityPickRequestSerializer_v291;
import org.cloudburstmc.protocol.bedrock.packet.EntityPickRequestPacket;

/* loaded from: classes5.dex */
public class EntityPickRequestSerializer_v465 extends EntityPickRequestSerializer_v291 {
    public static final EntityPickRequestSerializer_v465 INSTANCE = new EntityPickRequestSerializer_v465();

    protected EntityPickRequestSerializer_v465() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EntityPickRequestSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, EntityPickRequestPacket packet) {
        super.serialize(buffer, helper, packet);
        buffer.writeBoolean(packet.isWithData());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EntityPickRequestSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, EntityPickRequestPacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setWithData(buffer.readBoolean());
    }
}
