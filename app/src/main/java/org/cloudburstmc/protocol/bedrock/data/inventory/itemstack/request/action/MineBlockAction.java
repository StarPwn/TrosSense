package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

/* loaded from: classes5.dex */
public final class MineBlockAction implements ItemStackRequestAction {
    private final int hotbarSlot;
    private final int predictedDurability;
    private final int stackNetworkId;

    public MineBlockAction(int hotbarSlot, int predictedDurability, int stackNetworkId) {
        this.hotbarSlot = hotbarSlot;
        this.predictedDurability = predictedDurability;
        this.stackNetworkId = stackNetworkId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MineBlockAction)) {
            return false;
        }
        MineBlockAction other = (MineBlockAction) o;
        return getHotbarSlot() == other.getHotbarSlot() && getPredictedDurability() == other.getPredictedDurability() && getStackNetworkId() == other.getStackNetworkId();
    }

    public int hashCode() {
        int result = (1 * 59) + getHotbarSlot();
        return (((result * 59) + getPredictedDurability()) * 59) + getStackNetworkId();
    }

    public String toString() {
        return "MineBlockAction(hotbarSlot=" + getHotbarSlot() + ", predictedDurability=" + getPredictedDurability() + ", stackNetworkId=" + getStackNetworkId() + ")";
    }

    public int getHotbarSlot() {
        return this.hotbarSlot;
    }

    public int getPredictedDurability() {
        return this.predictedDurability;
    }

    public int getStackNetworkId() {
        return this.stackNetworkId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.MINE_BLOCK;
    }
}
