package org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe;

import java.util.List;
import java.util.UUID;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.CraftingDataType;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount;
import org.cloudburstmc.protocol.common.util.Preconditions;

/* loaded from: classes5.dex */
public class ShapelessRecipeData implements CraftingRecipeData {
    private final String id;
    private final List<ItemDescriptorWithCount> ingredients;
    private final int netId;
    private final int priority;
    private final List<ItemData> results;
    private final String tag;
    private final CraftingDataType type;
    private final UUID uuid;

    public String toString() {
        return "ShapelessRecipeData(type=" + getType() + ", id=" + getId() + ", ingredients=" + getIngredients() + ", results=" + getResults() + ", uuid=" + getUuid() + ", tag=" + getTag() + ", priority=" + getPriority() + ", netId=" + getNetId() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof ShapelessRecipeData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ShapelessRecipeData)) {
            return false;
        }
        ShapelessRecipeData other = (ShapelessRecipeData) o;
        if (!other.canEqual(this) || getPriority() != other.getPriority() || getNetId() != other.getNetId()) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        if (this$id != null ? !this$id.equals(other$id) : other$id != null) {
            return false;
        }
        Object this$ingredients = getIngredients();
        Object other$ingredients = other.getIngredients();
        if (this$ingredients != null ? !this$ingredients.equals(other$ingredients) : other$ingredients != null) {
            return false;
        }
        Object this$results = getResults();
        Object other$results = other.getResults();
        if (this$results != null ? !this$results.equals(other$results) : other$results != null) {
            return false;
        }
        Object this$uuid = getUuid();
        Object other$uuid = other.getUuid();
        if (this$uuid != null ? !this$uuid.equals(other$uuid) : other$uuid != null) {
            return false;
        }
        Object this$tag = getTag();
        Object other$tag = other.getTag();
        if (this$tag == null) {
            if (other$tag == null) {
                return true;
            }
        } else if (this$tag.equals(other$tag)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = (1 * 59) + getPriority();
        int result2 = (result * 59) + getNetId();
        Object $type = getType();
        int result3 = (result2 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $id = getId();
        int result4 = (result3 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $ingredients = getIngredients();
        int result5 = (result4 * 59) + ($ingredients == null ? 43 : $ingredients.hashCode());
        Object $results = getResults();
        int result6 = (result5 * 59) + ($results == null ? 43 : $results.hashCode());
        Object $uuid = getUuid();
        int result7 = (result6 * 59) + ($uuid == null ? 43 : $uuid.hashCode());
        Object $tag = getTag();
        return (result7 * 59) + ($tag != null ? $tag.hashCode() : 43);
    }

    private ShapelessRecipeData(CraftingDataType type, String id, List<ItemDescriptorWithCount> ingredients, List<ItemData> results, UUID uuid, String tag, int priority, int netId) {
        this.type = type;
        this.id = id;
        this.ingredients = ingredients;
        this.results = results;
        this.uuid = uuid;
        this.tag = tag;
        this.priority = priority;
        this.netId = netId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.RecipeData
    public CraftingDataType getType() {
        return this.type;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.IdentifiableRecipeData
    public String getId() {
        return this.id;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.CraftingRecipeData
    public List<ItemDescriptorWithCount> getIngredients() {
        return this.ingredients;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.CraftingRecipeData
    public List<ItemData> getResults() {
        return this.results;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.UniqueCraftingData
    public UUID getUuid() {
        return this.uuid;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.TaggedCraftingData
    public String getTag() {
        return this.tag;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.CraftingRecipeData
    public int getPriority() {
        return this.priority;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.NetworkRecipeData
    public int getNetId() {
        return this.netId;
    }

    public static ShapelessRecipeData of(CraftingDataType type, String id, List<ItemDescriptorWithCount> ingredients, List<ItemData> results, UUID uuid, String tag, int priority, int netId) {
        Preconditions.checkArgument(type == CraftingDataType.SHAPELESS || type == CraftingDataType.SHAPELESS_CHEMISTRY || type == CraftingDataType.SHULKER_BOX, "type must be SHAPELESS, SHAPELESS_CHEMISTRY or SHULKER_BOX");
        return new ShapelessRecipeData(type, id, ingredients, results, uuid, tag, priority, netId);
    }

    public static ShapelessRecipeData shapeless(String id, List<ItemDescriptorWithCount> ingredients, List<ItemData> results, UUID uuid, String tag, int priority, int netId) {
        return of(CraftingDataType.SHAPELESS, id, ingredients, results, uuid, tag, priority, netId);
    }

    public static ShapelessRecipeData shapelessChemistry(String id, List<ItemDescriptorWithCount> ingredients, List<ItemData> results, UUID uuid, String tag, int priority, int netId) {
        return of(CraftingDataType.SHAPELESS_CHEMISTRY, id, ingredients, results, uuid, tag, priority, netId);
    }

    public static ShapelessRecipeData shulkerBox(String id, List<ItemDescriptorWithCount> ingredients, List<ItemData> results, UUID uuid, String tag, int priority, int netId) {
        return of(CraftingDataType.SHULKER_BOX, id, ingredients, results, uuid, tag, priority, netId);
    }
}
