package org.cloudburstmc.protocol.bedrock.data.inventory.transaction;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* loaded from: classes5.dex */
public class ItemUseTransaction {
    private int actionType;
    private BlockDefinition blockDefinition;
    private int blockFace;
    private Vector3i blockPosition;
    private Vector3f clickPosition;
    private int hotbarSlot;
    private ItemData itemInHand;
    private int legacyRequestId;
    private Vector3f playerPosition;
    private boolean usingNetIds;
    private final List<LegacySetItemSlotData> legacySlots = new ObjectArrayList();
    private final List<InventoryActionData> actions = new ObjectArrayList();

    protected boolean canEqual(Object other) {
        return other instanceof ItemUseTransaction;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemUseTransaction)) {
            return false;
        }
        ItemUseTransaction other = (ItemUseTransaction) o;
        if (!other.canEqual(this) || getLegacyRequestId() != other.getLegacyRequestId() || isUsingNetIds() != other.isUsingNetIds() || getActionType() != other.getActionType() || getBlockFace() != other.getBlockFace() || getHotbarSlot() != other.getHotbarSlot()) {
            return false;
        }
        Object this$legacySlots = getLegacySlots();
        Object other$legacySlots = other.getLegacySlots();
        if (this$legacySlots != null ? !this$legacySlots.equals(other$legacySlots) : other$legacySlots != null) {
            return false;
        }
        Object this$actions = getActions();
        Object other$actions = other.getActions();
        if (this$actions != null ? !this$actions.equals(other$actions) : other$actions != null) {
            return false;
        }
        Object this$blockPosition = getBlockPosition();
        Object other$blockPosition = other.getBlockPosition();
        if (this$blockPosition != null ? !this$blockPosition.equals(other$blockPosition) : other$blockPosition != null) {
            return false;
        }
        Object this$itemInHand = getItemInHand();
        Object other$itemInHand = other.getItemInHand();
        if (this$itemInHand != null ? !this$itemInHand.equals(other$itemInHand) : other$itemInHand != null) {
            return false;
        }
        Object this$playerPosition = getPlayerPosition();
        Object other$playerPosition = other.getPlayerPosition();
        if (this$playerPosition != null ? !this$playerPosition.equals(other$playerPosition) : other$playerPosition != null) {
            return false;
        }
        Object this$clickPosition = getClickPosition();
        Object other$clickPosition = other.getClickPosition();
        if (this$clickPosition != null ? !this$clickPosition.equals(other$clickPosition) : other$clickPosition != null) {
            return false;
        }
        Object this$blockDefinition = getBlockDefinition();
        Object other$blockDefinition = other.getBlockDefinition();
        return this$blockDefinition == null ? other$blockDefinition == null : this$blockDefinition.equals(other$blockDefinition);
    }

    public int hashCode() {
        int result = (1 * 59) + getLegacyRequestId();
        int result2 = (((((((result * 59) + (isUsingNetIds() ? 79 : 97)) * 59) + getActionType()) * 59) + getBlockFace()) * 59) + getHotbarSlot();
        Object $legacySlots = getLegacySlots();
        int result3 = (result2 * 59) + ($legacySlots == null ? 43 : $legacySlots.hashCode());
        Object $actions = getActions();
        int result4 = (result3 * 59) + ($actions == null ? 43 : $actions.hashCode());
        Object $blockPosition = getBlockPosition();
        int result5 = (result4 * 59) + ($blockPosition == null ? 43 : $blockPosition.hashCode());
        Object $itemInHand = getItemInHand();
        int result6 = (result5 * 59) + ($itemInHand == null ? 43 : $itemInHand.hashCode());
        Object $playerPosition = getPlayerPosition();
        int result7 = (result6 * 59) + ($playerPosition == null ? 43 : $playerPosition.hashCode());
        Object $clickPosition = getClickPosition();
        int result8 = (result7 * 59) + ($clickPosition == null ? 43 : $clickPosition.hashCode());
        Object $blockDefinition = getBlockDefinition();
        return (result8 * 59) + ($blockDefinition != null ? $blockDefinition.hashCode() : 43);
    }

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

    public void setUsingNetIds(boolean usingNetIds) {
        this.usingNetIds = usingNetIds;
    }

    public String toString() {
        return "ItemUseTransaction(legacyRequestId=" + getLegacyRequestId() + ", legacySlots=" + getLegacySlots() + ", usingNetIds=" + isUsingNetIds() + ", actions=" + getActions() + ", actionType=" + getActionType() + ", blockPosition=" + getBlockPosition() + ", blockFace=" + getBlockFace() + ", hotbarSlot=" + getHotbarSlot() + ", itemInHand=" + getItemInHand() + ", playerPosition=" + getPlayerPosition() + ", clickPosition=" + getClickPosition() + ", blockDefinition=" + getBlockDefinition() + ")";
    }

    public int getLegacyRequestId() {
        return this.legacyRequestId;
    }

    public List<LegacySetItemSlotData> getLegacySlots() {
        return this.legacySlots;
    }

    public boolean isUsingNetIds() {
        return this.usingNetIds;
    }

    public List<InventoryActionData> getActions() {
        return this.actions;
    }

    public int getActionType() {
        return this.actionType;
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

    public BlockDefinition getBlockDefinition() {
        return this.blockDefinition;
    }
}
