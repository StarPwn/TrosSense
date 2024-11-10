package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventoryActionData;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventoryTransactionType;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.LegacySetItemSlotData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class InventoryTransactionPacket implements BedrockPacket {
    private int actionType;
    private BlockDefinition blockDefinition;
    private int blockFace;
    private Vector3i blockPosition;
    private Vector3f clickPosition;
    private Vector3f headPosition;
    private int hotbarSlot;
    private ItemData itemInHand;
    private int legacyRequestId;
    private Vector3f playerPosition;
    private long runtimeEntityId;
    private InventoryTransactionType transactionType;

    @Deprecated
    private boolean usingNetIds;
    private final List<LegacySetItemSlotData> legacySlots = new ObjectArrayList();
    private final List<InventoryActionData> actions = new ObjectArrayList();

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public void setBlockDefinition(BlockDefinition blockDefinition) {
        this.blockDefinition = blockDefinition;
    }

    public void setBlockFace(int blockFace) {
        this.blockFace = blockFace;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public void setClickPosition(Vector3f clickPosition) {
        this.clickPosition = clickPosition;
    }

    public void setHeadPosition(Vector3f headPosition) {
        this.headPosition = headPosition;
    }

    public void setHotbarSlot(int hotbarSlot) {
        this.hotbarSlot = hotbarSlot;
    }

    public void setItemInHand(ItemData itemInHand) {
        this.itemInHand = itemInHand;
    }

    public void setLegacyRequestId(int legacyRequestId) {
        this.legacyRequestId = legacyRequestId;
    }

    public void setPlayerPosition(Vector3f playerPosition) {
        this.playerPosition = playerPosition;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setTransactionType(InventoryTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Deprecated
    public void setUsingNetIds(boolean usingNetIds) {
        this.usingNetIds = usingNetIds;
    }

    protected boolean canEqual(Object other) {
        return other instanceof InventoryTransactionPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InventoryTransactionPacket)) {
            return false;
        }
        InventoryTransactionPacket other = (InventoryTransactionPacket) o;
        if (!other.canEqual(this) || this.legacyRequestId != other.legacyRequestId || this.actionType != other.actionType || this.runtimeEntityId != other.runtimeEntityId || this.blockFace != other.blockFace || this.hotbarSlot != other.hotbarSlot || this.usingNetIds != other.usingNetIds) {
            return false;
        }
        Object this$legacySlots = this.legacySlots;
        Object other$legacySlots = other.legacySlots;
        if (this$legacySlots != null ? !this$legacySlots.equals(other$legacySlots) : other$legacySlots != null) {
            return false;
        }
        Object this$actions = this.actions;
        Object other$actions = other.actions;
        if (this$actions != null ? !this$actions.equals(other$actions) : other$actions != null) {
            return false;
        }
        Object this$transactionType = this.transactionType;
        Object other$transactionType = other.transactionType;
        if (this$transactionType != null ? !this$transactionType.equals(other$transactionType) : other$transactionType != null) {
            return false;
        }
        Object this$blockPosition = this.blockPosition;
        Object other$blockPosition = other.blockPosition;
        if (this$blockPosition != null ? !this$blockPosition.equals(other$blockPosition) : other$blockPosition != null) {
            return false;
        }
        Object this$itemInHand = this.itemInHand;
        Object other$itemInHand = other.itemInHand;
        if (this$itemInHand != null ? !this$itemInHand.equals(other$itemInHand) : other$itemInHand != null) {
            return false;
        }
        Object this$playerPosition = this.playerPosition;
        Object other$playerPosition = other.playerPosition;
        if (this$playerPosition != null ? !this$playerPosition.equals(other$playerPosition) : other$playerPosition != null) {
            return false;
        }
        Object this$clickPosition = this.clickPosition;
        Object other$clickPosition = other.clickPosition;
        if (this$clickPosition == null) {
            if (other$clickPosition != null) {
                return false;
            }
        } else if (!this$clickPosition.equals(other$clickPosition)) {
            return false;
        }
        Object other$clickPosition2 = this.headPosition;
        Object other$playerPosition2 = other.headPosition;
        if (other$clickPosition2 == null) {
            if (other$playerPosition2 != null) {
                return false;
            }
        } else if (!other$clickPosition2.equals(other$playerPosition2)) {
            return false;
        }
        Object this$headPosition = this.blockDefinition;
        Object other$blockDefinition = other.blockDefinition;
        return this$headPosition == null ? other$blockDefinition == null : this$headPosition.equals(other$blockDefinition);
    }

    public int hashCode() {
        int result = (1 * 59) + this.legacyRequestId;
        int result2 = (result * 59) + this.actionType;
        long $runtimeEntityId = this.runtimeEntityId;
        int result3 = ((((((result2 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId))) * 59) + this.blockFace) * 59) + this.hotbarSlot) * 59;
        int i = this.usingNetIds ? 79 : 97;
        Object $legacySlots = this.legacySlots;
        int result4 = ((result3 + i) * 59) + ($legacySlots == null ? 43 : $legacySlots.hashCode());
        Object $actions = this.actions;
        int result5 = (result4 * 59) + ($actions == null ? 43 : $actions.hashCode());
        Object $transactionType = this.transactionType;
        int result6 = (result5 * 59) + ($transactionType == null ? 43 : $transactionType.hashCode());
        Object $blockPosition = this.blockPosition;
        int result7 = (result6 * 59) + ($blockPosition == null ? 43 : $blockPosition.hashCode());
        Object $itemInHand = this.itemInHand;
        int result8 = (result7 * 59) + ($itemInHand == null ? 43 : $itemInHand.hashCode());
        Object $playerPosition = this.playerPosition;
        int result9 = (result8 * 59) + ($playerPosition == null ? 43 : $playerPosition.hashCode());
        Object $clickPosition = this.clickPosition;
        int result10 = (result9 * 59) + ($clickPosition == null ? 43 : $clickPosition.hashCode());
        Object $headPosition = this.headPosition;
        int result11 = (result10 * 59) + ($headPosition == null ? 43 : $headPosition.hashCode());
        Object $blockDefinition = this.blockDefinition;
        return (result11 * 59) + ($blockDefinition != null ? $blockDefinition.hashCode() : 43);
    }

    public String toString() {
        return "InventoryTransactionPacket(legacyRequestId=" + this.legacyRequestId + ", legacySlots=" + this.legacySlots + ", actions=" + this.actions + ", transactionType=" + this.transactionType + ", actionType=" + this.actionType + ", runtimeEntityId=" + this.runtimeEntityId + ", blockPosition=" + this.blockPosition + ", blockFace=" + this.blockFace + ", hotbarSlot=" + this.hotbarSlot + ", itemInHand=" + this.itemInHand + ", playerPosition=" + this.playerPosition + ", clickPosition=" + this.clickPosition + ", headPosition=" + this.headPosition + ", usingNetIds=" + this.usingNetIds + ", blockDefinition=" + this.blockDefinition + ")";
    }

    public int getLegacyRequestId() {
        return this.legacyRequestId;
    }

    public List<LegacySetItemSlotData> getLegacySlots() {
        return this.legacySlots;
    }

    public List<InventoryActionData> getActions() {
        return this.actions;
    }

    public InventoryTransactionType getTransactionType() {
        return this.transactionType;
    }

    public int getActionType() {
        return this.actionType;
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public Vector3i getBlockPosition() {
        return this.blockPosition;
    }

    public int getBlockFace() {
        return this.blockFace;
    }

    public int getHotbarSlot() {
        return this.hotbarSlot;
    }

    public ItemData getItemInHand() {
        return this.itemInHand;
    }

    public Vector3f getPlayerPosition() {
        return this.playerPosition;
    }

    public Vector3f getClickPosition() {
        return this.clickPosition;
    }

    public Vector3f getHeadPosition() {
        return this.headPosition;
    }

    @Deprecated
    public boolean isUsingNetIds() {
        return this.usingNetIds;
    }

    public BlockDefinition getBlockDefinition() {
        return this.blockDefinition;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.INVENTORY_TRANSACTION;
    }
}
