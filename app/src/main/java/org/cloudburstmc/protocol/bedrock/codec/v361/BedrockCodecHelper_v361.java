package org.cloudburstmc.protocol.bedrock.codec.v361;

import io.netty.buffer.ByteBuf;
import java.util.Map;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v340.BedrockCodecHelper_v340;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataType;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureAnimationMode;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureMirror;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureRotation;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureSettings;
import org.cloudburstmc.protocol.bedrock.transformer.EntityDataTransformer;
import org.cloudburstmc.protocol.common.util.Preconditions;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v361 extends BedrockCodecHelper_v340 {
    public BedrockCodecHelper_v361(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes) {
        super(entityData, gameRulesTypes);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void readEntityData(ByteBuf buffer, EntityDataMap entityDataMap) {
        Object value;
        Preconditions.checkNotNull(entityDataMap, "entityDataDictionary");
        int length = VarInts.readUnsignedInt(buffer);
        for (int i = 0; i < length; i++) {
            int id = VarInts.readUnsignedInt(buffer);
            int formatId = VarInts.readUnsignedInt(buffer);
            EntityDataFormat format = EntityDataFormat.values()[formatId];
            switch (format) {
                case BYTE:
                    value = Byte.valueOf(buffer.readByte());
                    break;
                case SHORT:
                    value = Short.valueOf(buffer.readShortLE());
                    break;
                case INT:
                    value = Integer.valueOf(VarInts.readInt(buffer));
                    break;
                case FLOAT:
                    value = Float.valueOf(buffer.readFloatLE());
                    break;
                case STRING:
                    value = readString(buffer);
                    break;
                case NBT:
                    value = readTag(buffer);
                    break;
                case VECTOR3I:
                    value = readVector3i(buffer);
                    break;
                case LONG:
                    value = Long.valueOf(VarInts.readLong(buffer));
                    break;
                case VECTOR3F:
                    value = readVector3f(buffer);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown entity data type received");
            }
            EntityDataTypeMap.Definition<?>[] definitions = this.entityData.fromId(id, format);
            if (definitions != null) {
                for (EntityDataTypeMap.Definition<?> definition : definitions) {
                    EntityDataTransformer<Object, ?> transformer = definition.getTransformer();
                    Object transformedValue = transformer.deserialize(this, entityDataMap, value);
                    if (transformedValue != null) {
                        entityDataMap.put(definition.getType(), transformer.deserialize(this, entityDataMap, value));
                    }
                }
            } else {
                log.debug("Unknown entity data: {} type {} value {}", Integer.valueOf(id), format, value);
            }
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:7:0x0056. Please report as an issue. */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeEntityData(ByteBuf buffer, EntityDataMap entityDataMap) {
        Preconditions.checkNotNull(entityDataMap, "entityDataDictionary");
        VarInts.writeUnsignedInt(buffer, entityDataMap.size());
        for (Map.Entry<EntityDataType<?>, Object> entry : entityDataMap.entrySet()) {
            EntityDataTypeMap.Definition<?> definition = this.entityData.fromType(entry.getKey());
            VarInts.writeUnsignedInt(buffer, definition.getId());
            VarInts.writeUnsignedInt(buffer, definition.getFormat().ordinal());
            try {
                Object value = definition.getTransformer().serialize(this, entityDataMap, entry.getValue());
                switch (definition.getFormat()) {
                    case BYTE:
                        buffer.writeByte(((Byte) value).byteValue());
                    case SHORT:
                        buffer.writeShortLE(((Short) value).shortValue());
                    case INT:
                        VarInts.writeInt(buffer, ((Integer) value).intValue());
                    case FLOAT:
                        buffer.writeFloatLE(((Float) value).floatValue());
                    case STRING:
                        writeString(buffer, (String) value);
                    case NBT:
                        writeTag(buffer, value);
                    case VECTOR3I:
                        writeVector3i(buffer, (Vector3i) value);
                    case LONG:
                        VarInts.writeLong(buffer, ((Long) value).longValue());
                    case VECTOR3F:
                        writeVector3f(buffer, (Vector3f) value);
                    default:
                        throw new UnsupportedOperationException("Unknown entity data type " + definition.getFormat());
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to encode EntityData " + definition.getId() + " of " + definition.getType().getTypeName(), e);
            }
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public StructureSettings readStructureSettings(ByteBuf buffer) {
        String paletteName = readString(buffer);
        boolean ignoringEntities = buffer.readBoolean();
        boolean ignoringBlocks = buffer.readBoolean();
        Vector3i size = readBlockPosition(buffer);
        Vector3i offset = readBlockPosition(buffer);
        long lastEditedByEntityId = VarInts.readLong(buffer);
        StructureRotation rotation = StructureRotation.from(buffer.readByte());
        StructureMirror mirror = StructureMirror.from(buffer.readByte());
        float integrityValue = buffer.readFloatLE();
        int integritySeed = buffer.readIntLE();
        return new StructureSettings(paletteName, ignoringEntities, ignoringBlocks, true, size, offset, lastEditedByEntityId, rotation, mirror, StructureAnimationMode.NONE, 0.0f, integrityValue, integritySeed, Vector3f.ZERO);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeStructureSettings(ByteBuf buffer, StructureSettings settings) {
        writeString(buffer, settings.getPaletteName());
        buffer.writeBoolean(settings.isIgnoringEntities());
        buffer.writeBoolean(settings.isIgnoringBlocks());
        writeBlockPosition(buffer, settings.getSize());
        writeBlockPosition(buffer, settings.getOffset());
        VarInts.writeLong(buffer, settings.getLastEditedByEntityId());
        buffer.writeByte(settings.getRotation().ordinal());
        buffer.writeByte(settings.getMirror().ordinal());
        buffer.writeFloatLE(settings.getIntegrityValue());
        buffer.writeIntLE(settings.getIntegritySeed());
    }
}
