package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount;

/* loaded from: classes5.dex */
public final class AutoCraftRecipeAction implements RecipeItemStackRequestAction {
    private final List<ItemDescriptorWithCount> ingredients;
    private final int recipeNetworkId;
    private final int timesCrafted;

    public AutoCraftRecipeAction(int recipeNetworkId, int timesCrafted, List<ItemDescriptorWithCount> ingredients) {
        this.recipeNetworkId = recipeNetworkId;
        this.timesCrafted = timesCrafted;
        this.ingredients = ingredients;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AutoCraftRecipeAction)) {
            return false;
        }
        AutoCraftRecipeAction other = (AutoCraftRecipeAction) o;
        if (getRecipeNetworkId() != other.getRecipeNetworkId() || getTimesCrafted() != other.getTimesCrafted()) {
            return false;
        }
        Object this$ingredients = getIngredients();
        Object other$ingredients = other.getIngredients();
        return this$ingredients != null ? this$ingredients.equals(other$ingredients) : other$ingredients == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getRecipeNetworkId();
        int result2 = (result * 59) + getTimesCrafted();
        Object $ingredients = getIngredients();
        return (result2 * 59) + ($ingredients == null ? 43 : $ingredients.hashCode());
    }

    public String toString() {
        return "AutoCraftRecipeAction(recipeNetworkId=" + getRecipeNetworkId() + ", timesCrafted=" + getTimesCrafted() + ", ingredients=" + getIngredients() + ")";
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.RecipeItemStackRequestAction
    public int getRecipeNetworkId() {
        return this.recipeNetworkId;
    }

    public int getTimesCrafted() {
        return this.timesCrafted;
    }

    public List<ItemDescriptorWithCount> getIngredients() {
        return this.ingredients;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.CRAFT_RECIPE_AUTO;
    }
}
