package org.cloudburstmc.protocol.bedrock.codec.v440;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v431.BedrockCodecHelper_v431;
import org.cloudburstmc.protocol.bedrock.data.GameRuleData;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureAnimationMode;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureMirror;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureRotation;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureSettings;
import org.cloudburstmc.protocol.common.util.Preconditions;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v440 extends BedrockCodecHelper_v431 {
    public BedrockCodecHelper_v440(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes, TypeMap<ItemStackRequestActionType> stackRequestActionTypes, TypeMap<ContainerSlotType> containerSlotTypes) {
        super(entityData, gameRulesTypes, stackRequestActionTypes, containerSlotTypes);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeGameRule(ByteBuf buffer, GameRuleData<?> gameRule) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(gameRule, "gameRule");
        Object value = gameRule.getValue();
        int id = this.gameRuleType.getId(value.getClass());
        writeString(buffer, gameRule.getName());
        buffer.writeBoolean(gameRule.isEditable());
        VarInts.writeUnsignedInt(buffer, id);
        switch (id) {
            case 1:
                buffer.writeBoolean(((Boolean) value).booleanValue());
                return;
            case 2:
                VarInts.writeUnsignedInt(buffer, ((Integer) value).intValue());
                return;
            case 3:
                buffer.writeFloatLE(((Float) value).floatValue());
                return;
            default:
                return;
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public GameRuleData<?> readGameRule(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        String name = readString(buffer);
        boolean editable = buffer.readBoolean();
        int type = VarInts.readUnsignedInt(buffer);
        switch (type) {
            case 1:
                return new GameRuleData<>(name, editable, Boolean.valueOf(buffer.readBoolean()));
            case 2:
                return new GameRuleData<>(name, editable, Integer.valueOf(VarInts.readUnsignedInt(buffer)));
            case 3:
                return new GameRuleData<>(name, editable, Float.valueOf(buffer.readFloatLE()));
            default:
                throw new IllegalStateException("Invalid gamerule type received");
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.BedrockCodecHelper_v388, org.cloudburstmc.protocol.bedrock.codec.v361.BedrockCodecHelper_v361, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public StructureSettings readStructureSettings(ByteBuf buffer) {
        String paletteName = readString(buffer);
        boolean ignoringEntities = buffer.readBoolean();
        boolean ignoringBlocks = buffer.readBoolean();
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
        return new StructureSettings(paletteName, ignoringEntities, ignoringBlocks, true, size, offset, lastEditedByEntityId, rotation, mirror, animationMode, animationSeconds, integrityValue, integritySeed, pivot);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.BedrockCodecHelper_v388, org.cloudburstmc.protocol.bedrock.codec.v361.BedrockCodecHelper_v361, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeStructureSettings(ByteBuf buffer, StructureSettings settings) {
        writeString(buffer, settings.getPaletteName());
        buffer.writeBoolean(settings.isIgnoringEntities());
        buffer.writeBoolean(settings.isIgnoringBlocks());
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
