package org.cloudburstmc.protocol.bedrock.codec.v291;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtUtils;
import org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.data.GameRuleData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandEnumConstraint;
import org.cloudburstmc.protocol.bedrock.data.command.CommandEnumData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOriginData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOriginType;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataType;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityLinkData;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.transformer.EntityDataTransformer;
import org.cloudburstmc.protocol.common.util.Preconditions;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;
import org.cloudburstmc.protocol.common.util.stream.LittleEndianByteBufOutputStream;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v291 extends BaseBedrockCodecHelper {
    public BedrockCodecHelper_v291(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes) {
        super(entityData, gameRulesTypes);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public EntityLinkData readEntityLink(ByteBuf buffer) {
        long from = VarInts.readLong(buffer);
        long to = VarInts.readLong(buffer);
        int type = buffer.readUnsignedByte();
        boolean immediate = buffer.readBoolean();
        return new EntityLinkData(from, to, EntityLinkData.Type.values()[type], immediate);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeEntityLink(ByteBuf buffer, EntityLinkData entityLink) {
        Preconditions.checkNotNull(entityLink, "entityLink");
        VarInts.writeLong(buffer, entityLink.getFrom());
        VarInts.writeLong(buffer, entityLink.getTo());
        buffer.writeByte(entityLink.getType().ordinal());
        buffer.writeBoolean(entityLink.isImmediate());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemData readNetItem(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeNetItem(ByteBuf buffer, ItemData item) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemData readItem(ByteBuf buffer) {
        int runtimeId = VarInts.readInt(buffer);
        if (runtimeId == 0) {
            return ItemData.AIR;
        }
        ItemDefinition definition = this.itemDefinitions.getDefinition(runtimeId);
        int aux = VarInts.readInt(buffer);
        int damage = (short) (aux >> 8);
        if (damage == 32767) {
            damage = -1;
        }
        int count = aux & 255;
        short nbtSize = buffer.readShortLE();
        NbtMap compoundTag = null;
        if (nbtSize > 0) {
            try {
                NBTInputStream reader = NbtUtils.createReaderLE(new ByteBufInputStream(buffer.readSlice(nbtSize)));
                try {
                    Object tag = reader.readTag();
                    if (tag instanceof NbtMap) {
                        compoundTag = (NbtMap) tag;
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } finally {
                }
            } catch (IOException e) {
                throw new IllegalStateException("Unable to load NBT data", e);
            }
        }
        String[] canPlace = (String[]) readArray(buffer, new String[0], new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper_v291.this.readString((ByteBuf) obj);
            }
        });
        String[] canBreak = (String[]) readArray(buffer, new String[0], new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper_v291.this.readString((ByteBuf) obj);
            }
        });
        return ItemData.builder().definition(definition).damage(damage).count(count).tag(compoundTag).canPlace(canPlace).canBreak(canBreak).build();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeItem(ByteBuf buffer, ItemData item) {
        Preconditions.checkNotNull(item, "item");
        ItemDefinition definition = item.getDefinition();
        if (isAir(definition)) {
            buffer.writeByte(0);
            return;
        }
        VarInts.writeInt(buffer, definition.getRuntimeId());
        int damage = item.getDamage();
        if (damage == -1) {
            damage = 32767;
        }
        VarInts.writeInt(buffer, (damage << 8) | (item.getCount() & 255));
        int sizeIndex = buffer.writerIndex();
        buffer.writeShortLE(0);
        if (item.getTag() != null) {
            int afterSizeIndex = buffer.writerIndex();
            try {
                NBTOutputStream stream = new NBTOutputStream(new LittleEndianByteBufOutputStream(buffer));
                try {
                    stream.writeTag(item.getTag());
                    stream.close();
                    buffer.setShortLE(sizeIndex, buffer.writerIndex() - afterSizeIndex);
                } finally {
                }
            } catch (IOException e) {
                throw new IllegalStateException("Unable to save NBT data", e);
            }
        }
        writeArray(buffer, item.getCanPlace(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper_v291.this.writeString((ByteBuf) obj, (String) obj2);
            }
        });
        writeArray(buffer, item.getCanBreak(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper_v291.this.writeString((ByteBuf) obj, (String) obj2);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemData readItemInstance(ByteBuf buffer) {
        return readItem(buffer);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeItemInstance(ByteBuf buffer, ItemData item) {
        writeItem(buffer, item);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public CommandOriginData readCommandOrigin(ByteBuf buffer) {
        long varLong;
        CommandOriginType origin = CommandOriginType.values()[VarInts.readUnsignedInt(buffer)];
        UUID uuid = readUuid(buffer);
        String requestId = readString(buffer);
        if (origin != CommandOriginType.DEV_CONSOLE && origin != CommandOriginType.TEST) {
            varLong = -1;
        } else {
            long varLong2 = VarInts.readLong(buffer);
            varLong = varLong2;
        }
        return new CommandOriginData(origin, uuid, requestId, varLong);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeCommandOrigin(ByteBuf buffer, CommandOriginData originData) {
        Preconditions.checkNotNull(originData, "commandOriginData");
        VarInts.writeUnsignedInt(buffer, originData.getOrigin().ordinal());
        writeUuid(buffer, originData.getUuid());
        writeString(buffer, originData.getRequestId());
        if (originData.getOrigin() == CommandOriginType.DEV_CONSOLE || originData.getOrigin() == CommandOriginType.TEST) {
            VarInts.writeLong(buffer, originData.getEvent());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public GameRuleData<?> readGameRule(ByteBuf buffer) {
        String name = readString(buffer);
        int type = VarInts.readUnsignedInt(buffer);
        switch (type) {
            case 1:
                return new GameRuleData<>(name, Boolean.valueOf(buffer.readBoolean()));
            case 2:
                return new GameRuleData<>(name, Integer.valueOf(VarInts.readUnsignedInt(buffer)));
            case 3:
                return new GameRuleData<>(name, Float.valueOf(buffer.readFloatLE()));
            default:
                throw new IllegalStateException("Invalid gamerule type received");
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeGameRule(ByteBuf buffer, GameRuleData<?> gameRule) {
        Preconditions.checkNotNull(gameRule, "gameRule");
        Object value = gameRule.getValue();
        int type = this.gameRuleType.getId(value.getClass());
        writeString(buffer, gameRule.getName());
        VarInts.writeUnsignedInt(buffer, type);
        switch (type) {
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

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void readEntityData(ByteBuf buffer, EntityDataMap entityDataMap) {
        Object value;
        Preconditions.checkNotNull(entityDataMap, "entityDataDictionary");
        int length = VarInts.readUnsignedInt(buffer);
        for (int i = 0; i < length; i++) {
            int id = VarInts.readUnsignedInt(buffer);
            EntityDataFormat format = EntityDataFormat.values()[VarInts.readUnsignedInt(buffer)];
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
                    value = readItem(buffer).getTag();
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
                    throw new UnsupportedOperationException("Unknown entity data type received");
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
    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
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
                        writeItem(buffer, ItemData.builder().definition(ItemDefinition.LEGACY_FIREWORK).damage(0).count(1).tag((NbtMap) value).build());
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

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public CommandEnumData readCommandEnum(ByteBuf buffer, boolean soft) {
        String name = readString(buffer);
        int count = VarInts.readUnsignedInt(buffer);
        LinkedHashMap<String, Set<CommandEnumConstraint>> values = new LinkedHashMap<>();
        for (int i = 0; i < count; i++) {
            values.put(readString(buffer), Collections.emptySet());
        }
        return new CommandEnumData(name, values, soft);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeCommandEnum(ByteBuf buffer, CommandEnumData enumData) {
        Preconditions.checkNotNull(enumData, "enumData");
        writeString(buffer, enumData.getName());
        Set<String> values = enumData.getValues().keySet();
        VarInts.writeUnsignedInt(buffer, values.size());
        for (String value : values) {
            writeString(buffer, value);
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <O> O readOptional(ByteBuf buffer, O emptyValue, Function<ByteBuf, O> function) {
        if (buffer.readBoolean()) {
            return function.apply(buffer);
        }
        return emptyValue;
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <T> void writeOptional(ByteBuf buffer, Predicate<T> isPresent, T object, BiConsumer<ByteBuf, T> consumer) {
        Preconditions.checkNotNull(consumer, "read consumer");
        boolean exists = isPresent.test(object);
        buffer.writeBoolean(exists);
        if (exists) {
            consumer.accept(buffer, object);
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <T> void writeOptionalNull(ByteBuf buffer, T object, BiConsumer<ByteBuf, T> consumer) {
        writeOptional(buffer, new Predicate() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean nonNull;
                nonNull = Objects.nonNull(obj);
                return nonNull;
            }
        }, object, consumer);
    }
}
