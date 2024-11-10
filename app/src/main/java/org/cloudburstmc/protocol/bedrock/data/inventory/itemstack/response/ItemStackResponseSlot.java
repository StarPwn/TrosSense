package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response;

/* loaded from: classes5.dex */
public final class ItemStackResponseSlot {
    private final int count;
    private final String customName;
    private final int durabilityCorrection;
    private final int hotbarSlot;
    private final int slot;
    private final int stackNetworkId;

    public ItemStackResponseSlot(int slot, int hotbarSlot, int count, int stackNetworkId, String customName, int durabilityCorrection) {
        if (customName == null) {
            throw new NullPointerException("customName is marked non-null but is null");
        }
        this.slot = slot;
        this.hotbarSlot = hotbarSlot;
        this.count = count;
        this.stackNetworkId = stackNetworkId;
        this.customName = customName;
        this.durabilityCorrection = durabilityCorrection;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemStackResponseSlot)) {
            return false;
        }
        ItemStackResponseSlot other = (ItemStackResponseSlot) o;
        if (getSlot() != other.getSlot() || getHotbarSlot() != other.getHotbarSlot() || getCount() != other.getCount() || getStackNetworkId() != other.getStackNetworkId() || getDurabilityCorrection() != other.getDurabilityCorrection()) {
            return false;
        }
        Object this$customName = getCustomName();
        Object other$customName = other.getCustomName();
        return this$customName != null ? this$customName.equals(other$customName) : other$customName == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getSlot();
        int result2 = (((((((result * 59) + getHotbarSlot()) * 59) + getCount()) * 59) + getStackNetworkId()) * 59) + getDurabilityCorrection();
        Object $customName = getCustomName();
        return (result2 * 59) + ($customName == null ? 43 : $customName.hashCode());
    }

    public String toString() {
        return "ItemStackResponseSlot(slot=" + getSlot() + ", hotbarSlot=" + getHotbarSlot() + ", count=" + getCount() + ", stackNetworkId=" + getStackNetworkId() + ", customName=" + getCustomName() + ", durabilityCorrection=" + getDurabilityCorrection() + ")";
    }

    public int getSlot() {
        return this.slot;
    }

    public int getHotbarSlot() {
        return this.hotbarSlot;
    }

    public int getCount() {
        return this.count;
    }

    public int getStackNetworkId() {
        return this.stackNetworkId;
    }

    public String getCustomName() {
        return this.customName;
    }

    public int getDurabilityCorrection() {
        return this.durabilityCorrection;
    }
}
