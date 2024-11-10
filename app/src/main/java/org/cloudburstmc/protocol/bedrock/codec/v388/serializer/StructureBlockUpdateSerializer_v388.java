package org.cloudburstmc.protocol.bedrock.codec.v388.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.StructureBlockUpdateSerializer_v361;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureBlockType;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureEditorData;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureRedstoneSaveMode;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureSettings;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class StructureBlockUpdateSerializer_v388 extends StructureBlockUpdateSerializer_v361 {
    public static final StructureBlockUpdateSerializer_v388 INSTANCE = new StructureBlockUpdateSerializer_v388();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v361.serializer.StructureBlockUpdateSerializer_v361
    protected StructureEditorData readEditorData(ByteBuf buffer, BedrockCodecHelper helper) {
        String name = helper.readString(buffer);
        String dataField = helper.readString(buffer);
        boolean includingPlayers = buffer.readBoolean();
        boolean boundingBoxVisible = buffer.readBoolean();
        StructureBlockType type = StructureBlockType.from(VarInts.readInt(buffer));
        StructureSettings settings = helper.readStructureSettings(buffer);
        StructureRedstoneSaveMode redstoneSaveMode = StructureRedstoneSaveMode.from(VarInts.readInt(buffer));
        return new StructureEditorData(name, dataField, includingPlayers, boundingBoxVisible, type, settings, redstoneSaveMode);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v361.serializer.StructureBlockUpdateSerializer_v361
    public void writeEditorData(ByteBuf buffer, BedrockCodecHelper helper, StructureEditorData data) {
        super.writeEditorData(buffer, helper, data);
        VarInts.writeInt(buffer, data.getRedstoneSaveMode().ordinal());
    }
}
