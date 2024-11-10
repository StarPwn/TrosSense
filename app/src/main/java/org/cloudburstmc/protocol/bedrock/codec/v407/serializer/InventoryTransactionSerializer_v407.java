package org.cloudburstmc.protocol.bedrock.codec.v407.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.InventoryTransactionSerializer_v291;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventoryTransactionType;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.LegacySetItemSlotData;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class InventoryTransactionSerializer_v407 extends InventoryTransactionSerializer_v291 {
    public static final InventoryTransactionSerializer_v407 INSTANCE = new InventoryTransactionSerializer_v407();

    protected InventoryTransactionSerializer_v407() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.InventoryTransactionSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, InventoryTransactionPacket packet) {
        int legacyRequestId = packet.getLegacyRequestId();
        VarInts.writeInt(buffer, legacyRequestId);
        if (legacyRequestId < -1 && (legacyRequestId & 1) == 0) {
            helper.writeArray(buffer, packet.getLegacySlots(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.InventoryTransactionSerializer_v407$$ExternalSyntheticLambda0
                @Override // org.cloudburstmc.protocol.common.util.TriConsumer
                public final void accept(Object obj, Object obj2, Object obj3) {
                    InventoryTransactionSerializer_v407.lambda$serialize$0((ByteBuf) obj, (BedrockCodecHelper) obj2, (LegacySetItemSlotData) obj3);
                }
            });
        }
        InventoryTransactionType transactionType = packet.getTransactionType();
        VarInts.writeUnsignedInt(buffer, transactionType.ordinal());
        helper.writeInventoryActions(buffer, packet.getActions(), packet.isUsingNetIds());
        switch (transactionType) {
            case ITEM_USE:
                helper.writeItemUse(buffer, packet);
                return;
            case ITEM_USE_ON_ENTITY:
                writeItemUseOnEntity(buffer, helper, packet);
                return;
            case ITEM_RELEASE:
                writeItemRelease(buffer, helper, packet);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$serialize$0(ByteBuf buf, BedrockCodecHelper packetHelper, LegacySetItemSlotData data) {
        buf.writeByte(data.getContainerId());
        packetHelper.writeByteArray(buf, data.getSlots());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.InventoryTransactionSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, InventoryTransactionPacket packet) {
        int legacyRequestId = VarInts.readInt(buffer);
        packet.setLegacyRequestId(legacyRequestId);
        if (legacyRequestId < -1 && (legacyRequestId & 1) == 0) {
            helper.readArray(buffer, packet.getLegacySlots(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.InventoryTransactionSerializer_v407$$ExternalSyntheticLambda1
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    return InventoryTransactionSerializer_v407.lambda$deserialize$1((ByteBuf) obj, (BedrockCodecHelper) obj2);
                }
            });
        }
        InventoryTransactionType transactionType = InventoryTransactionType.values()[VarInts.readUnsignedInt(buffer)];
        packet.setTransactionType(transactionType);
        packet.setUsingNetIds(helper.readInventoryActions(buffer, packet.getActions()));
        switch (transactionType) {
            case ITEM_USE:
                helper.readItemUse(buffer, packet);
                return;
            case ITEM_USE_ON_ENTITY:
                readItemUseOnEntity(buffer, helper, packet);
                return;
            case ITEM_RELEASE:
                readItemRelease(buffer, helper, packet);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ LegacySetItemSlotData lambda$deserialize$1(ByteBuf buf, BedrockCodecHelper packetHelper) {
        byte containerId = buf.readByte();
        byte[] slots = packetHelper.readByteArray(buf);
        return new LegacySetItemSlotData(containerId, slots);
    }
}
