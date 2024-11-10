package org.cloudburstmc.protocol.bedrock.codec.v475.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v465.serializer.StartGameSerializer_v465;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;

/* loaded from: classes5.dex */
public class StartGameSerializer_v475 extends StartGameSerializer_v465 {
    public static final StartGameSerializer_v475 INSTANCE = new StartGameSerializer_v475();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v440.serializer.StartGameSerializer_v440, org.cloudburstmc.protocol.bedrock.codec.v428.serializer.StartGameSerializer_v428, org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        super.serialize(buffer, helper, packet);
        buffer.writeLongLE(packet.getBlockRegistryChecksum());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v440.serializer.StartGameSerializer_v440, org.cloudburstmc.protocol.bedrock.codec.v428.serializer.StartGameSerializer_v428, org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setBlockRegistryChecksum(buffer.readLongLE());
    }
}
