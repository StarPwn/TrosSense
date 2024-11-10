package org.cloudburstmc.protocol.bedrock.codec.v361.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureTemplateRequestOperation;
import org.cloudburstmc.protocol.bedrock.packet.StructureTemplateDataRequestPacket;

/* loaded from: classes5.dex */
public class StructureTemplateDataRequestSerializer_v361 implements BedrockPacketSerializer<StructureTemplateDataRequestPacket> {
    public static final StructureTemplateDataRequestSerializer_v361 INSTANCE = new StructureTemplateDataRequestSerializer_v361();

    protected StructureTemplateDataRequestSerializer_v361() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, StructureTemplateDataRequestPacket packet) {
        helper.writeString(buffer, packet.getName());
        helper.writeBlockPosition(buffer, packet.getPosition());
        helper.writeStructureSettings(buffer, packet.getSettings());
        buffer.writeByte(packet.getOperation().ordinal());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, StructureTemplateDataRequestPacket packet) {
        packet.setName(helper.readString(buffer));
        packet.setPosition(helper.readBlockPosition(buffer));
        packet.setSettings(helper.readStructureSettings(buffer));
        packet.setOperation(StructureTemplateRequestOperation.from(buffer.readByte()));
    }
}
