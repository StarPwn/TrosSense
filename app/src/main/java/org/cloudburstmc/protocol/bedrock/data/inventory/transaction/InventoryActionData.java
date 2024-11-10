package org.cloudburstmc.protocol.bedrock.data.inventory.transaction;

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* loaded from: classes5.dex */
public final class InventoryActionData {
    private final ItemData fromItem;
    private final int slot;
    private final InventorySource source;
    private final int stackNetworkId;
    private final ItemData toItem;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InventoryActionData)) {
            return false;
        }
        InventoryActionData other = (InventoryActionData) o;
        if (getSlot() != other.getSlot() || getStackNetworkId() != other.getStackNetworkId()) {
            return false;
        }
        Object this$source = getSource();
        Object other$source = other.getSource();
        if (this$source != null ? !this$source.equals(other$source) : other$source != null) {
            return false;
        }
        Object this$fromItem = getFromItem();
        Object other$fromItem = other.getFromItem();
        if (this$fromItem != null ? !this$fromItem.equals(other$fromItem) : other$fromItem != null) {
            return false;
        }
        Object this$toItem = getToItem();
        Object other$toItem = other.getToItem();
        return this$toItem != null ? this$toItem.equals(other$toItem) : other$toItem == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getSlot();
        int result2 = (result * 59) + getStackNetworkId();
        Object $source = getSource();
        int result3 = (result2 * 59) + ($source == null ? 43 : $source.hashCode());
        Object $fromItem = getFromItem();
        int result4 = (result3 * 59) + ($fromItem == null ? 43 : $fromItem.hashCode());
        Object $toItem = getToItem();
        return (result4 * 59) + ($toItem != null ? $toItem.hashCode() : 43);
    }

    public String toString() {
        return "InventoryActionData(source=" + getSource() + ", slot=" + getSlot() + ", fromItem=" + getFromItem() + ", toItem=" + getToItem() + ", stackNetworkId=" + getStackNetworkId() + ")";
    }

    public InventoryActionData(InventorySource source, int slot, ItemData fromItem, ItemData toItem, int stackNetworkId) {
        this.source = source;
        this.slot = slot;
        this.fromItem = fromItem;
        this.toItem = toItem;
        this.stackNetworkId = stackNetworkId;
    }

    public InventorySource getSource() {
        return this.source;
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemData getFromItem() {
        return this.fromItem;
    }

    public ItemData getToItem() {
        return this.toItem;
    }

    public int getStackNetworkId() {
        return this.stackNetworkId;
    }

    public InventoryActionData(InventorySource source, int slot, ItemData fromItem, ItemData toItem) {
        this(source, slot, fromItem, toItem, 0);
    }

    public InventoryActionData reverse() {
        return new InventoryActionData(this.source, this.slot, this.toItem, this.fromItem, this.stackNetworkId);
    }
}
