package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.SetEntityLinkPacket;

/* loaded from: classes5.dex */
public class SetEntityLinkSerializer_v291 implements BedrockPacketSerializer<SetEntityLinkPacket> {
    public static final SetEntityLinkSerializer_v291 INSTANCE = new SetEntityLinkSerializer_v291();

    protected SetEntityLinkSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, SetEntityLinkPacket packet) {
        helper.writeEntityLink(buffer, packet.getEntityLink());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, SetEntityLinkPacket packet) {
        packet.setEntityLink(helper.readEntityLink(buffer));
    }
}
