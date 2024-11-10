package org.cloudburstmc.protocol.bedrock.codec.v361.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureBlockType;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureEditorData;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureRedstoneSaveMode;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureSettings;
import org.cloudburstmc.protocol.bedrock.packet.StructureBlockUpdatePacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class StructureBlockUpdateSerializer_v361 implements BedrockPacketSerializer<StructureBlockUpdatePacket> {
    public static final StructureBlockUpdateSerializer_v361 INSTANCE = new StructureBlockUpdateSerializer_v361();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, StructureBlockUpdatePacket packet) {
        helper.writeBlockPosition(buffer, packet.getBlockPosition());
        writeEditorData(buffer, helper, packet.getEditorData());
        buffer.writeBoolean(packet.isPowered());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, StructureBlockUpdatePacket packet) {
        packet.setBlockPosition(helper.readBlockPosition(buffer));
        packet.setEditorData(readEditorData(buffer, helper));
        packet.setPowered(buffer.readBoolean());
    }

    protected StructureEditorData readEditorData(ByteBuf buffer, BedrockCodecHelper helper) {
        String name = helper.readString(buffer);
        String dataField = helper.readString(buffer);
        boolean includingPlayers = buffer.readBoolean();
        boolean boundingBoxVisible = buffer.readBoolean();
        StructureBlockType type = StructureBlockType.from(VarInts.readInt(buffer));
        StructureSettings settings = helper.readStructureSettings(buffer);
        return new StructureEditorData(name, dataField, includingPlayers, boundingBoxVisible, type, settings, StructureRedstoneSaveMode.SAVES_TO_DISK);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeEditorData(ByteBuf buffer, BedrockCodecHelper helper, StructureEditorData data) {
        helper.writeString(buffer, data.getName());
        helper.writeString(buffer, data.getDataField());
        buffer.writeBoolean(data.isIncludingPlayers());
        buffer.writeBoolean(data.isBoundingBoxVisible());
        VarInts.writeInt(buffer, data.getType().ordinal());
        helper.writeStructureSettings(buffer, data.getSettings());
    }
}
