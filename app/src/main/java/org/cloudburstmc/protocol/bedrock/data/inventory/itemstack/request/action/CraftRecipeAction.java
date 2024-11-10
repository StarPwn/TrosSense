package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

/* loaded from: classes5.dex */
public final class CraftRecipeAction implements RecipeItemStackRequestAction {
    private final int recipeNetworkId;

    public CraftRecipeAction(int recipeNetworkId) {
        this.recipeNetworkId = recipeNetworkId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CraftRecipeAction)) {
            return false;
        }
        CraftRecipeAction other = (CraftRecipeAction) o;
        return getRecipeNetworkId() == other.getRecipeNetworkId();
    }

    public int hashCode() {
        int result = (1 * 59) + getRecipeNetworkId();
        return result;
    }

    public String toString() {
        return "CraftRecipeAction(recipeNetworkId=" + getRecipeNetworkId() + ")";
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.RecipeItemStackRequestAction
    public int getRecipeNetworkId() {
        return this.recipeNetworkId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.CRAFT_RECIPE;
    }
}
