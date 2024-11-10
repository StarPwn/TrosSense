package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.PlayerHotbarPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class PlayerHotbarSerializer_v291 implements BedrockPacketSerializer<PlayerHotbarPacket> {
    public static final PlayerHotbarSerializer_v291 INSTANCE = new PlayerHotbarSerializer_v291();

    protected PlayerHotbarSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, PlayerHotbarPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getSelectedHotbarSlot());
        buffer.writeByte(packet.getContainerId());
        buffer.writeBoolean(packet.isSelectHotbarSlot());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, PlayerHotbarPacket packet) {
        packet.setSelectedHotbarSlot(VarInts.readUnsignedInt(buffer));
        packet.setContainerId(buffer.readUnsignedByte());
        packet.setSelectHotbarSlot(buffer.readBoolean());
    }
}
