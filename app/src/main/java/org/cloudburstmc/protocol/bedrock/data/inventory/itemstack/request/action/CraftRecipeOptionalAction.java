package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

/* loaded from: classes5.dex */
public final class CraftRecipeOptionalAction implements ItemStackRequestAction {
    private final int filteredStringIndex;
    private final int recipeNetworkId;

    public CraftRecipeOptionalAction(int recipeNetworkId, int filteredStringIndex) {
        this.recipeNetworkId = recipeNetworkId;
        this.filteredStringIndex = filteredStringIndex;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CraftRecipeOptionalAction)) {
            return false;
        }
        CraftRecipeOptionalAction other = (CraftRecipeOptionalAction) o;
        return getRecipeNetworkId() == other.getRecipeNetworkId() && getFilteredStringIndex() == other.getFilteredStringIndex();
    }

    public int hashCode() {
        int result = (1 * 59) + getRecipeNetworkId();
        return (result * 59) + getFilteredStringIndex();
    }

    public String toString() {
        return "CraftRecipeOptionalAction(recipeNetworkId=" + getRecipeNetworkId() + ", filteredStringIndex=" + getFilteredStringIndex() + ")";
    }

    public int getRecipeNetworkId() {
        return this.recipeNetworkId;
    }

    public int getFilteredStringIndex() {
        return this.filteredStringIndex;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.CRAFT_RECIPE_OPTIONAL;
    }
}
