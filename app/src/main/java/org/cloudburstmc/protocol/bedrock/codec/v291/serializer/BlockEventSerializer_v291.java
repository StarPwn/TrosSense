package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.BlockEventPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class BlockEventSerializer_v291 implements BedrockPacketSerializer<BlockEventPacket> {
    public static final BlockEventSerializer_v291 INSTANCE = new BlockEventSerializer_v291();

    protected BlockEventSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, BlockEventPacket packet) {
        helper.writeBlockPosition(buffer, packet.getBlockPosition());
        VarInts.writeInt(buffer, packet.getEventType());
        VarInts.writeInt(buffer, packet.getEventData());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, BlockEventPacket packet) {
        packet.setBlockPosition(helper.readBlockPosition(buffer));
        packet.setEventType(VarInts.readInt(buffer));
        packet.setEventData(VarInts.readInt(buffer));
    }
}
