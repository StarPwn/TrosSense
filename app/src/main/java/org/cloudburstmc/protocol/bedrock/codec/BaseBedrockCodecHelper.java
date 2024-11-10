package org.cloudburstmc.protocol.bedrock.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.ToLongFunction;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.nbt.NbtType;
import org.cloudburstmc.nbt.NbtUtils;
import org.cloudburstmc.protocol.bedrock.data.ExperimentData;
import org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityProperties;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequest;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventoryActionData;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventorySource;
import org.cloudburstmc.protocol.bedrock.data.skin.AnimationData;
import org.cloudburstmc.protocol.bedrock.data.skin.ImageData;
import org.cloudburstmc.protocol.bedrock.data.skin.SerializedSkin;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureSettings;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;
import org.cloudburstmc.protocol.common.DefinitionRegistry;
import org.cloudburstmc.protocol.common.NamedDefinition;
import org.cloudburstmc.protocol.common.util.Preconditions;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public abstract class BaseBedrockCodecHelper implements BedrockCodecHelper {
    protected static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) BaseBedrockCodecHelper.class);
    protected DefinitionRegistry<BlockDefinition> blockDefinitions;
    protected final EntityDataTypeMap entityData;
    protected final TypeMap<Class<?>> gameRuleType;
    protected DefinitionRegistry<ItemDefinition> itemDefinitions;

    /* JADX INFO: Access modifiers changed from: protected */
    public BaseBedrockCodecHelper(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRuleType) {
        this.entityData = entityData;
        this.gameRuleType = gameRuleType;
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public DefinitionRegistry<ItemDefinition> getItemDefinitions() {
        return this.itemDefinitions;
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void setItemDefinitions(DefinitionRegistry<ItemDefinition> itemDefinitions) {
        this.itemDefinitions = itemDefinitions;
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public DefinitionRegistry<BlockDefinition> getBlockDefinitions() {
        return this.blockDefinitions;
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void setBlockDefinitions(DefinitionRegistry<BlockDefinition> blockDefinitions) {
        this.blockDefinitions = blockDefinitions;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean isAir(ItemDefinition definition) {
        return definition == null || "minecraft:air".equals(definition.getIdentifier());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public byte[] readByteArray(ByteBuf buffer) {
        int length = VarInts.readUnsignedInt(buffer);
        Preconditions.checkArgument(buffer.isReadable(length), "Tried to read %s bytes but only has %s readable", length, buffer.readableBytes());
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return bytes;
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeByteArray(ByteBuf buffer, byte[] bytes) {
        Preconditions.checkNotNull(bytes, "bytes");
        VarInts.writeUnsignedInt(buffer, bytes.length);
        buffer.writeBytes(bytes);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ByteBuf readByteBuf(ByteBuf buffer) {
        int length = VarInts.readUnsignedInt(buffer);
        return buffer.readRetainedSlice(length);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeByteBuf(ByteBuf buffer, ByteBuf toWrite) {
        Preconditions.checkNotNull(toWrite, "toWrite");
        VarInts.writeUnsignedInt(buffer, toWrite.readableBytes());
        buffer.writeBytes(toWrite, toWrite.readerIndex(), toWrite.writerIndex());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public String readString(ByteBuf buffer) {
        int length = VarInts.readUnsignedInt(buffer);
        return (String) buffer.readCharSequence(length, StandardCharsets.UTF_8);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeString(ByteBuf buffer, String string) {
        Preconditions.checkNotNull(string, "string");
        VarInts.writeUnsignedInt(buffer, ByteBufUtil.utf8Bytes(string));
        buffer.writeCharSequence(string, StandardCharsets.UTF_8);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public UUID readUuid(ByteBuf buffer) {
        return new UUID(buffer.readLongLE(), buffer.readLongLE());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeUuid(ByteBuf buffer, UUID uuid) {
        Preconditions.checkNotNull(uuid, "uuid");
        buffer.writeLongLE(uuid.getMostSignificantBits());
        buffer.writeLongLE(uuid.getLeastSignificantBits());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public Vector3f readVector3f(ByteBuf buffer) {
        float x = buffer.readFloatLE();
        float y = buffer.readFloatLE();
        float z = buffer.readFloatLE();
        return Vector3f.from(x, y, z);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeVector3f(ByteBuf buffer, Vector3f vector3f) {
        Preconditions.checkNotNull(vector3f, "vector3f");
        buffer.writeFloatLE(vector3f.getX());
        buffer.writeFloatLE(vector3f.getY());
        buffer.writeFloatLE(vector3f.getZ());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public Vector2f readVector2f(ByteBuf buffer) {
        float x = buffer.readFloatLE();
        float y = buffer.readFloatLE();
        return Vector2f.from(x, y);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeVector2f(ByteBuf buffer, Vector2f vector2f) {
        Preconditions.checkNotNull(vector2f, "vector2f");
        buffer.writeFloatLE(vector2f.getX());
        buffer.writeFloatLE(vector2f.getY());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public Vector3i readVector3i(ByteBuf buffer) {
        int x = VarInts.readInt(buffer);
        int y = VarInts.readInt(buffer);
        int z = VarInts.readInt(buffer);
        return Vector3i.from(x, y, z);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeVector3i(ByteBuf buffer, Vector3i vector3i) {
        Preconditions.checkNotNull(vector3i, "vector3i");
        VarInts.writeInt(buffer, vector3i.getX());
        VarInts.writeInt(buffer, vector3i.getY());
        VarInts.writeInt(buffer, vector3i.getZ());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public float readByteAngle(ByteBuf buffer) {
        return buffer.readByte() * 1.40625f;
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeByteAngle(ByteBuf buffer, float angle) {
        buffer.writeByte((byte) (angle / 1.40625f));
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public Vector3i readBlockPosition(ByteBuf buffer) {
        int x = VarInts.readInt(buffer);
        int y = VarInts.readUnsignedInt(buffer);
        int z = VarInts.readInt(buffer);
        return Vector3i.from(x, y, z);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeBlockPosition(ByteBuf buffer, Vector3i blockPosition) {
        Preconditions.checkNotNull(blockPosition, "blockPosition");
        VarInts.writeInt(buffer, blockPosition.getX());
        VarInts.writeUnsignedInt(buffer, blockPosition.getY());
        VarInts.writeInt(buffer, blockPosition.getZ());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <T> void readArray(ByteBuf buffer, Collection<T> array, ToLongFunction<ByteBuf> lengthReader, BiFunction<ByteBuf, BedrockCodecHelper, T> function) {
        long length = lengthReader.applyAsLong(buffer);
        for (int i = 0; i < length; i++) {
            array.add(function.apply(buffer, this));
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <T> void writeArray(ByteBuf buffer, Collection<T> array, ObjIntConsumer<ByteBuf> lengthWriter, TriConsumer<ByteBuf, BedrockCodecHelper, T> consumer) {
        lengthWriter.accept(buffer, array.size());
        for (T val : array) {
            consumer.accept(buffer, this, val);
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <T> T[] readArray(ByteBuf byteBuf, T[] tArr, BiFunction<ByteBuf, BedrockCodecHelper, T> biFunction) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        readArray(byteBuf, objectArrayList, biFunction);
        return (T[]) objectArrayList.toArray(tArr);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <T> void writeArray(ByteBuf buffer, T[] array, TriConsumer<ByteBuf, BedrockCodecHelper, T> consumer) {
        VarInts.writeUnsignedInt(buffer, array.length);
        for (T val : array) {
            consumer.accept(buffer, this, val);
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <T> void readArray(ByteBuf buffer, Collection<T> array, Function<ByteBuf, T> function) {
        int length = VarInts.readUnsignedInt(buffer);
        for (int i = 0; i < length; i++) {
            array.add(function.apply(buffer));
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <T> void writeArray(ByteBuf buffer, Collection<T> array, BiConsumer<ByteBuf, T> biConsumer) {
        VarInts.writeUnsignedInt(buffer, array.size());
        for (T val : array) {
            biConsumer.accept(buffer, val);
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <T> T[] readArray(ByteBuf byteBuf, T[] tArr, Function<ByteBuf, T> function) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        readArray(byteBuf, objectArrayList, function);
        return (T[]) objectArrayList.toArray(tArr);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <T> void writeArray(ByteBuf buffer, T[] array, BiConsumer<ByteBuf, T> biConsumer) {
        VarInts.writeUnsignedInt(buffer, array.length);
        for (T val : array) {
            biConsumer.accept(buffer, val);
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <T> T readTag(ByteBuf byteBuf, Class<T> cls) {
        try {
            NBTInputStream createNetworkReader = NbtUtils.createNetworkReader(new ByteBufInputStream(byteBuf));
            try {
                T t = (T) createNetworkReader.readTag();
                Preconditions.checkArgument(cls.isInstance(t), "Expected tag of %s type but received %s", cls, t.getClass());
                if (createNetworkReader != null) {
                    createNetworkReader.close();
                }
                return t;
            } finally {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeTag(ByteBuf buffer, Object tag) {
        try {
            NBTOutputStream writer = NbtUtils.createNetworkWriter(new ByteBufOutputStream(buffer));
            try {
                writer.writeTag(tag);
                if (writer != null) {
                    writer.close();
                }
            } finally {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <T> T readTagLE(ByteBuf byteBuf, Class<T> cls) {
        try {
            NBTInputStream createReaderLE = NbtUtils.createReaderLE(new ByteBufInputStream(byteBuf));
            try {
                Object readTag = createReaderLE.readTag();
                Preconditions.checkArgument(cls.isInstance(readTag), "Expected tag of %s type but received %s", cls, readTag.getClass());
                T t = (T) createReaderLE.readTag();
                if (createReaderLE != null) {
                    createReaderLE.close();
                }
                return t;
            } finally {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeTagLE(ByteBuf buffer, Object tag) {
        try {
            NBTOutputStream writer = NbtUtils.createWriterLE(new ByteBufOutputStream(buffer));
            try {
                writer.writeTag(tag);
                if (writer != null) {
                    writer.close();
                }
            } finally {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <T> T readTagValue(ByteBuf byteBuf, NbtType<T> nbtType) {
        try {
            NBTInputStream createNetworkReader = NbtUtils.createNetworkReader(new ByteBufInputStream(byteBuf));
            try {
                T t = (T) createNetworkReader.readValue(nbtType);
                if (createNetworkReader != null) {
                    createNetworkReader.close();
                }
                return t;
            } finally {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeTagValue(ByteBuf buffer, Object tag) {
        try {
            NBTOutputStream writer = NbtUtils.createNetworkWriter(new ByteBufOutputStream(buffer));
            try {
                writer.writeValue(tag);
                if (writer != null) {
                    writer.close();
                }
            } finally {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void readItemUse(ByteBuf buffer, InventoryTransactionPacket packet) {
        packet.setActionType(VarInts.readUnsignedInt(buffer));
        packet.setBlockPosition(readBlockPosition(buffer));
        packet.setBlockFace(VarInts.readInt(buffer));
        packet.setHotbarSlot(VarInts.readInt(buffer));
        packet.setItemInHand(readItem(buffer));
        packet.setPlayerPosition(readVector3f(buffer));
        packet.setClickPosition(readVector3f(buffer));
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeItemUse(ByteBuf buffer, InventoryTransactionPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getActionType());
        writeBlockPosition(buffer, packet.getBlockPosition());
        VarInts.writeInt(buffer, packet.getBlockFace());
        VarInts.writeInt(buffer, packet.getHotbarSlot());
        writeItem(buffer, packet.getItemInHand());
        writeVector3f(buffer, packet.getPlayerPosition());
        writeVector3f(buffer, packet.getClickPosition());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public boolean readInventoryActions(ByteBuf buffer, List<InventoryActionData> actions) {
        readArray(buffer, actions, new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return BaseBedrockCodecHelper.this.m2051x2ba76963((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$readInventoryActions$0$org-cloudburstmc-protocol-bedrock-codec-BaseBedrockCodecHelper, reason: not valid java name */
    public /* synthetic */ InventoryActionData m2051x2ba76963(ByteBuf buf, BedrockCodecHelper helper) {
        InventorySource source = readSource(buf);
        int slot = VarInts.readUnsignedInt(buf);
        ItemData fromItem = helper.readItem(buf);
        ItemData toItem = helper.readItem(buf);
        return new InventoryActionData(source, slot, fromItem, toItem);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeInventoryActions(ByteBuf buffer, List<InventoryActionData> actions, boolean hasNetworkIds) {
        writeArray(buffer, actions, new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper$$ExternalSyntheticLambda0
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                BaseBedrockCodecHelper.this.m2052x1f4818b3((ByteBuf) obj, (BedrockCodecHelper) obj2, (InventoryActionData) obj3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$writeInventoryActions$1$org-cloudburstmc-protocol-bedrock-codec-BaseBedrockCodecHelper, reason: not valid java name */
    public /* synthetic */ void m2052x1f4818b3(ByteBuf buf, BedrockCodecHelper helper, InventoryActionData action) {
        writeSource(buf, action.getSource());
        VarInts.writeUnsignedInt(buf, action.getSlot());
        helper.writeItem(buf, action.getFromItem());
        helper.writeItem(buf, action.getToItem());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public InventorySource readSource(ByteBuf buffer) {
        InventorySource.Type type = InventorySource.Type.byId(VarInts.readUnsignedInt(buffer));
        switch (type) {
            case CONTAINER:
                int containerId = VarInts.readInt(buffer);
                return InventorySource.fromContainerWindowId(containerId);
            case GLOBAL:
                return InventorySource.fromGlobalInventory();
            case WORLD_INTERACTION:
                InventorySource.Flag flag = InventorySource.Flag.values()[VarInts.readUnsignedInt(buffer)];
                return InventorySource.fromWorldInteraction(flag);
            case CREATIVE:
                return InventorySource.fromCreativeInventory();
            case NON_IMPLEMENTED_TODO:
                int containerId2 = VarInts.readInt(buffer);
                return InventorySource.fromNonImplementedTodo(containerId2);
            default:
                return InventorySource.fromInvalid();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeSource(ByteBuf buffer, InventorySource inventorySource) {
        Objects.requireNonNull(inventorySource, "InventorySource was null");
        VarInts.writeUnsignedInt(buffer, inventorySource.getType().id());
        switch (inventorySource.getType()) {
            case CONTAINER:
            case NON_IMPLEMENTED_TODO:
            case UNTRACKED_INTERACTION_UI:
                VarInts.writeInt(buffer, inventorySource.getContainerId());
                return;
            case GLOBAL:
            case CREATIVE:
            default:
                return;
            case WORLD_INTERACTION:
                VarInts.writeUnsignedInt(buffer, inventorySource.getFlag().ordinal());
                return;
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void readExperiments(ByteBuf buffer, List<ExperimentData> experiments) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeExperiments(ByteBuf buffer, List<ExperimentData> experiments) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemStackRequest readItemStackRequest(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeItemStackRequest(ByteBuf buffer, ItemStackRequest request) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public StructureSettings readStructureSettings(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeStructureSettings(ByteBuf buffer, StructureSettings settings) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public SerializedSkin readSkin(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeSkin(ByteBuf buffer, SerializedSkin skin) {
        throw new UnsupportedOperationException();
    }

    public AnimationData readAnimationData(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    protected void writeAnimationData(ByteBuf buffer, AnimationData animation) {
        throw new UnsupportedOperationException();
    }

    protected ImageData readImage(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    protected void writeImage(ByteBuf buffer, ImageData image) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void readEntityProperties(ByteBuf buffer, EntityProperties properties) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeEntityProperties(ByteBuf buffer, EntityProperties properties) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemDescriptorWithCount readIngredient(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeIngredient(ByteBuf buffer, ItemDescriptorWithCount ingredient) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ContainerSlotType readContainerSlotType(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeContainerSlotType(ByteBuf buffer, ContainerSlotType slotType) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writePlayerAbilities(ByteBuf buffer, PlayerAbilityHolder abilityHolder) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void readPlayerAbilities(ByteBuf buffer, PlayerAbilityHolder abilityHolder) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public DefinitionRegistry<NamedDefinition> getCameraPresetDefinitions() {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void setCameraPresetDefinitions(DefinitionRegistry<NamedDefinition> registry) {
        throw new UnsupportedOperationException();
    }
}
