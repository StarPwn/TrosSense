package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.BlockPickRequestPacket;

/* loaded from: classes5.dex */
public class BlockPickRequestSerializer_v291 implements BedrockPacketSerializer<BlockPickRequestPacket> {
    public static final BlockPickRequestSerializer_v291 INSTANCE = new BlockPickRequestSerializer_v291();

    protected BlockPickRequestSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, BlockPickRequestPacket packet) {
        helper.writeVector3i(buffer, packet.getBlockPosition());
        buffer.writeBoolean(packet.isAddUserData());
        buffer.writeByte(packet.getHotbarSlot());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, BlockPickRequestPacket packet) {
        packet.setBlockPosition(helper.readVector3i(buffer));
        packet.setAddUserData(buffer.readBoolean());
        packet.setHotbarSlot(buffer.readUnsignedByte());
    }
}
