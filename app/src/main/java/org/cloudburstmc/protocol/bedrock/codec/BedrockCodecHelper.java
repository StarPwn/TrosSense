package org.cloudburstmc.protocol.bedrock.codec;

import io.netty.buffer.ByteBuf;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtType;
import org.cloudburstmc.protocol.bedrock.data.ExperimentData;
import org.cloudburstmc.protocol.bedrock.data.GameRuleData;
import org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder;
import org.cloudburstmc.protocol.bedrock.data.command.CommandEnumData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOriginData;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityLinkData;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityProperties;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequest;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventoryActionData;
import org.cloudburstmc.protocol.bedrock.data.skin.SerializedSkin;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureSettings;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;
import org.cloudburstmc.protocol.common.DefinitionRegistry;
import org.cloudburstmc.protocol.common.NamedDefinition;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public interface BedrockCodecHelper {
    DefinitionRegistry<BlockDefinition> getBlockDefinitions();

    DefinitionRegistry<NamedDefinition> getCameraPresetDefinitions();

    DefinitionRegistry<ItemDefinition> getItemDefinitions();

    <T> void readArray(ByteBuf byteBuf, Collection<T> collection, Function<ByteBuf, T> function);

    <T> void readArray(ByteBuf byteBuf, Collection<T> collection, ToLongFunction<ByteBuf> toLongFunction, BiFunction<ByteBuf, BedrockCodecHelper, T> biFunction);

    <T> T[] readArray(ByteBuf byteBuf, T[] tArr, BiFunction<ByteBuf, BedrockCodecHelper, T> biFunction);

    <T> T[] readArray(ByteBuf byteBuf, T[] tArr, Function<ByteBuf, T> function);

    Vector3i readBlockPosition(ByteBuf byteBuf);

    float readByteAngle(ByteBuf byteBuf);

    byte[] readByteArray(ByteBuf byteBuf);

    ByteBuf readByteBuf(ByteBuf byteBuf);

    CommandEnumData readCommandEnum(ByteBuf byteBuf, boolean z);

    CommandOriginData readCommandOrigin(ByteBuf byteBuf);

    ContainerSlotType readContainerSlotType(ByteBuf byteBuf);

    void readEntityData(ByteBuf byteBuf, EntityDataMap entityDataMap);

    EntityLinkData readEntityLink(ByteBuf byteBuf);

    void readEntityProperties(ByteBuf byteBuf, EntityProperties entityProperties);

    void readExperiments(ByteBuf byteBuf, List<ExperimentData> list);

    GameRuleData<?> readGameRule(ByteBuf byteBuf);

    ItemDescriptorWithCount readIngredient(ByteBuf byteBuf);

    boolean readInventoryActions(ByteBuf byteBuf, List<InventoryActionData> list);

    ItemData readItem(ByteBuf byteBuf);

    ItemData readItemInstance(ByteBuf byteBuf);

    ItemStackRequest readItemStackRequest(ByteBuf byteBuf);

    void readItemUse(ByteBuf byteBuf, InventoryTransactionPacket inventoryTransactionPacket);

    ItemData readNetItem(ByteBuf byteBuf);

    <O> O readOptional(ByteBuf byteBuf, O o, Function<ByteBuf, O> function);

    void readPlayerAbilities(ByteBuf byteBuf, PlayerAbilityHolder playerAbilityHolder);

    SerializedSkin readSkin(ByteBuf byteBuf);

    String readString(ByteBuf byteBuf);

    StructureSettings readStructureSettings(ByteBuf byteBuf);

    <T> T readTag(ByteBuf byteBuf, Class<T> cls);

    <T> T readTagLE(ByteBuf byteBuf, Class<T> cls);

    <T> T readTagValue(ByteBuf byteBuf, NbtType<T> nbtType);

    UUID readUuid(ByteBuf byteBuf);

    Vector2f readVector2f(ByteBuf byteBuf);

    Vector3f readVector3f(ByteBuf byteBuf);

    Vector3i readVector3i(ByteBuf byteBuf);

    void setBlockDefinitions(DefinitionRegistry<BlockDefinition> definitionRegistry);

    void setCameraPresetDefinitions(DefinitionRegistry<NamedDefinition> definitionRegistry);

    void setItemDefinitions(DefinitionRegistry<ItemDefinition> definitionRegistry);

    <T> void writeArray(ByteBuf byteBuf, Collection<T> collection, BiConsumer<ByteBuf, T> biConsumer);

    <T> void writeArray(ByteBuf byteBuf, Collection<T> collection, ObjIntConsumer<ByteBuf> objIntConsumer, TriConsumer<ByteBuf, BedrockCodecHelper, T> triConsumer);

    <T> void writeArray(ByteBuf byteBuf, T[] tArr, BiConsumer<ByteBuf, T> biConsumer);

    <T> void writeArray(ByteBuf byteBuf, T[] tArr, TriConsumer<ByteBuf, BedrockCodecHelper, T> triConsumer);

    void writeBlockPosition(ByteBuf byteBuf, Vector3i vector3i);

    void writeByteAngle(ByteBuf byteBuf, float f);

    void writeByteArray(ByteBuf byteBuf, byte[] bArr);

    void writeByteBuf(ByteBuf byteBuf, ByteBuf byteBuf2);

    void writeCommandEnum(ByteBuf byteBuf, CommandEnumData commandEnumData);

    void writeCommandOrigin(ByteBuf byteBuf, CommandOriginData commandOriginData);

    void writeContainerSlotType(ByteBuf byteBuf, ContainerSlotType containerSlotType);

    void writeEntityData(ByteBuf byteBuf, EntityDataMap entityDataMap);

    void writeEntityLink(ByteBuf byteBuf, EntityLinkData entityLinkData);

    void writeEntityProperties(ByteBuf byteBuf, EntityProperties entityProperties);

    void writeExperiments(ByteBuf byteBuf, List<ExperimentData> list);

    void writeGameRule(ByteBuf byteBuf, GameRuleData<?> gameRuleData);

    void writeIngredient(ByteBuf byteBuf, ItemDescriptorWithCount itemDescriptorWithCount);

    void writeInventoryActions(ByteBuf byteBuf, List<InventoryActionData> list, boolean z);

    void writeItem(ByteBuf byteBuf, ItemData itemData);

    void writeItemInstance(ByteBuf byteBuf, ItemData itemData);

    void writeItemStackRequest(ByteBuf byteBuf, ItemStackRequest itemStackRequest);

    void writeItemUse(ByteBuf byteBuf, InventoryTransactionPacket inventoryTransactionPacket);

    void writeNetItem(ByteBuf byteBuf, ItemData itemData);

    <T> void writeOptional(ByteBuf byteBuf, Predicate<T> predicate, T t, BiConsumer<ByteBuf, T> biConsumer);

    <T> void writeOptionalNull(ByteBuf byteBuf, T t, BiConsumer<ByteBuf, T> biConsumer);

    void writePlayerAbilities(ByteBuf byteBuf, PlayerAbilityHolder playerAbilityHolder);

    void writeSkin(ByteBuf byteBuf, SerializedSkin serializedSkin);

    void writeString(ByteBuf byteBuf, String str);

    void writeStructureSettings(ByteBuf byteBuf, StructureSettings structureSettings);

    void writeTag(ByteBuf byteBuf, Object obj);

    void writeTagLE(ByteBuf byteBuf, Object obj);

    void writeTagValue(ByteBuf byteBuf, Object obj);

    void writeUuid(ByteBuf byteBuf, UUID uuid);

    void writeVector2f(ByteBuf byteBuf, Vector2f vector2f);

    void writeVector3f(ByteBuf byteBuf, Vector3f vector3f);

    void writeVector3i(ByteBuf byteBuf, Vector3i vector3i);

    default <T> void readArray(ByteBuf buffer, Collection<T> array, BiFunction<ByteBuf, BedrockCodecHelper, T> function) {
        readArray(buffer, array, new ToLongFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.ToLongFunction
            public final long applyAsLong(Object obj) {
                return VarInts.readUnsignedInt((ByteBuf) obj);
            }
        }, function);
    }

    default <T> void writeArray(ByteBuf buffer, Collection<T> array, TriConsumer<ByteBuf, BedrockCodecHelper, T> consumer) {
        writeArray(buffer, array, new ObjIntConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper$$ExternalSyntheticLambda1
            @Override // java.util.function.ObjIntConsumer
            public final void accept(Object obj, int i) {
                VarInts.writeUnsignedInt((ByteBuf) obj, i);
            }
        }, consumer);
    }

    default Object readTag(ByteBuf buffer) {
        return readTag(buffer, Object.class);
    }

    default Object readTagLE(ByteBuf buffer) {
        return readTag(buffer, Object.class);
    }
}
