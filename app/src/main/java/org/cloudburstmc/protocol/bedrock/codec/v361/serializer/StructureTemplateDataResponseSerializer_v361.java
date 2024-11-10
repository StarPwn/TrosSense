package org.cloudburstmc.protocol.bedrock.codec.v361.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.StructureTemplateDataResponsePacket;

/* loaded from: classes5.dex */
public class StructureTemplateDataResponseSerializer_v361 implements BedrockPacketSerializer<StructureTemplateDataResponsePacket> {
    public static final StructureTemplateDataResponseSerializer_v361 INSTANCE = new StructureTemplateDataResponseSerializer_v361();

    protected StructureTemplateDataResponseSerializer_v361() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, StructureTemplateDataResponsePacket packet) {
        helper.writeString(buffer, packet.getName());
        boolean save = packet.isSave();
        buffer.writeBoolean(save);
        if (save) {
            helper.writeTag(buffer, packet.getTag());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, StructureTemplateDataResponsePacket packet) {
        packet.setName(helper.readString(buffer));
        boolean save = buffer.readBoolean();
        packet.setSave(save);
        if (save) {
            packet.setTag((NbtMap) helper.readTag(buffer, NbtMap.class));
        }
    }
}
