package org.cloudburstmc.protocol.bedrock.codec.v340;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import java.io.IOException;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtUtils;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v332.BedrockCodecHelper_v332;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v340 extends BedrockCodecHelper_v332 {
    protected static final String BLOCKING_ID = "minecraft:shield";

    public BedrockCodecHelper_v340(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes) {
        super(entityData, gameRulesTypes);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v332.BedrockCodecHelper_v332, org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemData readItem(ByteBuf buffer) {
        NBTInputStream reader;
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
        int nbtSize = buffer.readShortLE();
        NbtMap compoundTag = null;
        if (nbtSize > 0) {
            try {
                reader = NbtUtils.createReaderLE(new ByteBufInputStream(buffer.readSlice(nbtSize)));
                try {
                    compoundTag = (NbtMap) reader.readTag();
                    if (reader != null) {
                        reader.close();
                    }
                } finally {
                }
            } catch (IOException e) {
                throw new IllegalStateException("Unable to load NBT data", e);
            }
        } else if (nbtSize == -1) {
            int tagCount = buffer.readUnsignedByte();
            if (tagCount != 1) {
                throw new IllegalArgumentException("Expected 1 tag but got " + tagCount);
            }
            try {
                reader = NbtUtils.createNetworkReader(new ByteBufInputStream(buffer));
                try {
                    compoundTag = (NbtMap) reader.readTag();
                    if (reader != null) {
                        reader.close();
                    }
                } finally {
                    try {
                        throw th;
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (Throwable th) {
                                th.addSuppressed(th);
                            }
                        }
                    }
                }
            } catch (IOException e2) {
                throw new IllegalStateException("Unable to load NBT data", e2);
            }
        }
        String[] canPlace = new String[VarInts.readInt(buffer)];
        for (int i = 0; i < canPlace.length; i++) {
            canPlace[i] = readString(buffer);
        }
        int i2 = VarInts.readInt(buffer);
        String[] canBreak = new String[i2];
        for (int i3 = 0; i3 < canBreak.length; i3++) {
            canBreak[i3] = readString(buffer);
        }
        long blockingTicks = 0;
        if (definition != null && BLOCKING_ID.equals(definition.getIdentifier())) {
            blockingTicks = VarInts.readLong(buffer);
        }
        return ItemData.builder().definition(definition).damage(damage).count(count).tag(compoundTag).canPlace(canPlace).canBreak(canBreak).blockingTicks(blockingTicks).build();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v332.BedrockCodecHelper_v332, org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeItem(ByteBuf buffer, ItemData item) {
        super.writeItem(buffer, item);
        ItemDefinition definition = item.getDefinition();
        if (definition != null && BLOCKING_ID.equals(definition.getIdentifier())) {
            VarInts.writeLong(buffer, item.getBlockingTicks());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void readItemUse(ByteBuf buffer, InventoryTransactionPacket packet) {
        super.readItemUse(buffer, packet);
        packet.setBlockDefinition(this.blockDefinitions.getDefinition(VarInts.readUnsignedInt(buffer)));
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeItemUse(ByteBuf buffer, InventoryTransactionPacket packet) {
        super.writeItemUse(buffer, packet);
        VarInts.writeUnsignedInt(buffer, packet.getBlockDefinition().getRuntimeId());
    }
}
