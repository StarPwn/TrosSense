package org.cloudburstmc.protocol.bedrock.codec.v630.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ToggleCrafterSlotRequestPacket;

/* loaded from: classes5.dex */
public class ToggleCrafterSlotRequestSerializer_v630 implements BedrockPacketSerializer<ToggleCrafterSlotRequestPacket> {
    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ToggleCrafterSlotRequestPacket packet) {
        buffer.writeIntLE(packet.getBlockPosition().getX());
        buffer.writeIntLE(packet.getBlockPosition().getY());
        buffer.writeIntLE(packet.getBlockPosition().getZ());
        buffer.writeByte(packet.getSlot());
        buffer.writeBoolean(packet.isDisabled());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ToggleCrafterSlotRequestPacket packet) {
        int x = buffer.readIntLE();
        int y = buffer.readIntLE();
        int z = buffer.readIntLE();
        packet.setBlockPosition(Vector3i.from(x, y, z));
        packet.setSlot(buffer.readByte());
        packet.setDisabled(buffer.readBoolean());
    }
}
