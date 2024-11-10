package org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe;

import java.util.List;
import java.util.UUID;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.CraftingDataType;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount;
import org.cloudburstmc.protocol.common.util.Preconditions;

/* loaded from: classes5.dex */
public class ShapedRecipeData implements CraftingRecipeData {
    private final int height;
    private final String id;
    private final List<ItemDescriptorWithCount> ingredients;
    private final int netId;
    private final int priority;
    private final List<ItemData> results;
    private final String tag;
    private final CraftingDataType type;
    private final UUID uuid;
    private final int width;

    public String toString() {
        return "ShapedRecipeData(type=" + getType() + ", id=" + getId() + ", width=" + getWidth() + ", height=" + getHeight() + ", ingredients=" + getIngredients() + ", results=" + getResults() + ", uuid=" + getUuid() + ", tag=" + getTag() + ", priority=" + getPriority() + ", netId=" + getNetId() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof ShapedRecipeData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ShapedRecipeData)) {
            return false;
        }
        ShapedRecipeData other = (ShapedRecipeData) o;
        if (!other.canEqual(this) || getWidth() != other.getWidth() || getHeight() != other.getHeight() || getPriority() != other.getPriority() || getNetId() != other.getNetId()) {
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
        int result = (1 * 59) + getWidth();
        int result2 = (((((result * 59) + getHeight()) * 59) + getPriority()) * 59) + getNetId();
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

    private ShapedRecipeData(CraftingDataType type, String id, int width, int height, List<ItemDescriptorWithCount> ingredients, List<ItemData> results, UUID uuid, String tag, int priority, int netId) {
        this.type = type;
        this.id = id;
        this.width = width;
        this.height = height;
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

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
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

    public static ShapedRecipeData of(CraftingDataType type, String id, int width, int height, List<ItemDescriptorWithCount> ingredients, List<ItemData> results, UUID uuid, String tag, int priority, int netId) {
        Preconditions.checkArgument(type == CraftingDataType.SHAPED || type == CraftingDataType.SHAPED_CHEMISTRY, "type must be SHAPED or SHAPED_CHEMISTRY");
        return new ShapedRecipeData(type, id, width, height, ingredients, results, uuid, tag, priority, netId);
    }

    public static ShapedRecipeData shaped(String id, int width, int height, List<ItemDescriptorWithCount> ingredients, List<ItemData> results, UUID uuid, String tag, int priority, int netId) {
        return of(CraftingDataType.SHAPED, id, width, height, ingredients, results, uuid, tag, priority, netId);
    }

    public static ShapedRecipeData shapedChemistry(String id, int width, int height, List<ItemDescriptorWithCount> ingredients, List<ItemData> results, UUID uuid, String tag, int priority, int netId) {
        return of(CraftingDataType.SHAPED_CHEMISTRY, id, width, height, ingredients, results, uuid, tag, priority, netId);
    }
}
