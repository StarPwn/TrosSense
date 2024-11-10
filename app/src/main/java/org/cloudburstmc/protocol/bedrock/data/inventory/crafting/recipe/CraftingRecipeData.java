package org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe;

import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount;

/* loaded from: classes5.dex */
public interface CraftingRecipeData extends TaggedCraftingData, UniqueCraftingData, IdentifiableRecipeData {
    List<ItemDescriptorWithCount> getIngredients();

    int getPriority();

    List<ItemData> getResults();
}
