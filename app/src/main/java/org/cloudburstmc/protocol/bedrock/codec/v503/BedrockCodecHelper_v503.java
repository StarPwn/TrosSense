package org.cloudburstmc.protocol.bedrock.codec.v503;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v471.BedrockCodecHelper_v471;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureAnimationMode;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureMirror;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureRotation;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureSettings;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v503 extends BedrockCodecHelper_v471 {
    public BedrockCodecHelper_v503(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes, TypeMap<ItemStackRequestActionType> stackRequestActionTypes, TypeMap<ContainerSlotType> containerSlotTypes) {
        super(entityData, gameRulesTypes, stackRequestActionTypes, containerSlotTypes);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v440.BedrockCodecHelper_v440, org.cloudburstmc.protocol.bedrock.codec.v388.BedrockCodecHelper_v388, org.cloudburstmc.protocol.bedrock.codec.v361.BedrockCodecHelper_v361, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public StructureSettings readStructureSettings(ByteBuf buffer) {
        String paletteName = readString(buffer);
        boolean ignoringEntities = buffer.readBoolean();
        boolean ignoringBlocks = buffer.readBoolean();
        boolean nonTickingPlayersAndTickingAreasEnabled = buffer.readBoolean();
        Vector3i size = readBlockPosition(buffer);
        Vector3i offset = readBlockPosition(buffer);
        long lastEditedByEntityId = VarInts.readLong(buffer);
        StructureRotation rotation = StructureRotation.from(buffer.readByte());
        StructureMirror mirror = StructureMirror.from(buffer.readByte());
        StructureAnimationMode animationMode = StructureAnimationMode.from(buffer.readUnsignedByte());
        float animationSeconds = buffer.readFloatLE();
        float integrityValue = buffer.readFloatLE();
        int integritySeed = buffer.readIntLE();
        Vector3f pivot = readVector3f(buffer);
        return new StructureSettings(paletteName, ignoringEntities, ignoringBlocks, nonTickingPlayersAndTickingAreasEnabled, size, offset, lastEditedByEntityId, rotation, mirror, animationMode, animationSeconds, integrityValue, integritySeed, pivot);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v440.BedrockCodecHelper_v440, org.cloudburstmc.protocol.bedrock.codec.v388.BedrockCodecHelper_v388, org.cloudburstmc.protocol.bedrock.codec.v361.BedrockCodecHelper_v361, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeStructureSettings(ByteBuf buffer, StructureSettings settings) {
        writeString(buffer, settings.getPaletteName());
        buffer.writeBoolean(settings.isIgnoringEntities());
        buffer.writeBoolean(settings.isIgnoringBlocks());
        buffer.writeBoolean(settings.isNonTickingPlayersAndTickingAreasEnabled());
        writeBlockPosition(buffer, settings.getSize());
        writeBlockPosition(buffer, settings.getOffset());
        VarInts.writeLong(buffer, settings.getLastEditedByEntityId());
        buffer.writeByte(settings.getRotation().ordinal());
        buffer.writeByte(settings.getMirror().ordinal());
        buffer.writeByte(settings.getAnimationMode().ordinal());
        buffer.writeFloatLE(settings.getAnimationSeconds());
        buffer.writeFloatLE(settings.getIntegrityValue());
        buffer.writeIntLE(settings.getIntegritySeed());
        writeVector3f(buffer, settings.getPivot());
    }
}
