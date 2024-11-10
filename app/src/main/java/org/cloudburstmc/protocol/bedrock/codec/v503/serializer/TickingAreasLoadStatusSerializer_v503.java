package org.cloudburstmc.protocol.bedrock.codec.v503.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.TickingAreasLoadStatusPacket;

/* loaded from: classes5.dex */
public class TickingAreasLoadStatusSerializer_v503 implements BedrockPacketSerializer<TickingAreasLoadStatusPacket> {
    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, TickingAreasLoadStatusPacket packet) {
        buffer.writeBoolean(packet.isWaitingForPreload());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, TickingAreasLoadStatusPacket packet) {
        packet.setWaitingForPreload(buffer.readBoolean());
    }
}
