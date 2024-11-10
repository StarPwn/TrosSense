package org.cloudburstmc.protocol.bedrock.codec.v554.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.StructureBlockUpdateSerializer_v388;
import org.cloudburstmc.protocol.bedrock.packet.StructureBlockUpdatePacket;

/* loaded from: classes5.dex */
public class StructureBlockUpdateSerializer_v554 extends StructureBlockUpdateSerializer_v388 {
    @Override // org.cloudburstmc.protocol.bedrock.codec.v361.serializer.StructureBlockUpdateSerializer_v361, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, StructureBlockUpdatePacket packet) {
        super.serialize(buffer, helper, packet);
        buffer.writeBoolean(packet.isWaterlogged());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v361.serializer.StructureBlockUpdateSerializer_v361, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, StructureBlockUpdatePacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setWaterlogged(buffer.readBoolean());
    }
}
