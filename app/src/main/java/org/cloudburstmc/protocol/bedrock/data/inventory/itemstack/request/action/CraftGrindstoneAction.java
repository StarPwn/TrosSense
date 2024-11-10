package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

/* loaded from: classes5.dex */
public final class CraftGrindstoneAction implements ItemStackRequestAction {
    private final int recipeNetworkId;
    private final int repairCost;

    public CraftGrindstoneAction(int recipeNetworkId, int repairCost) {
        this.recipeNetworkId = recipeNetworkId;
        this.repairCost = repairCost;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CraftGrindstoneAction)) {
            return false;
        }
        CraftGrindstoneAction other = (CraftGrindstoneAction) o;
        return getRecipeNetworkId() == other.getRecipeNetworkId() && getRepairCost() == other.getRepairCost();
    }

    public int hashCode() {
        int result = (1 * 59) + getRecipeNetworkId();
        return (result * 59) + getRepairCost();
    }

    public String toString() {
        return "CraftGrindstoneAction(recipeNetworkId=" + getRecipeNetworkId() + ", repairCost=" + getRepairCost() + ")";
    }

    public int getRecipeNetworkId() {
        return this.recipeNetworkId;
    }

    public int getRepairCost() {
        return this.repairCost;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.CRAFT_REPAIR_AND_DISENCHANT;
    }
}
