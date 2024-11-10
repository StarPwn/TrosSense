package org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe;

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.CraftingDataType;
import org.cloudburstmc.protocol.common.util.Preconditions;

/* loaded from: classes5.dex */
public class FurnaceRecipeData implements TaggedCraftingData {
    private final int inputData;
    private final int inputId;
    private final ItemData result;
    private final String tag;
    private final CraftingDataType type;

    public String toString() {
        return "FurnaceRecipeData(type=" + getType() + ", inputId=" + getInputId() + ", inputData=" + getInputData() + ", result=" + getResult() + ", tag=" + getTag() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof FurnaceRecipeData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FurnaceRecipeData)) {
            return false;
        }
        FurnaceRecipeData other = (FurnaceRecipeData) o;
        if (!other.canEqual(this) || getInputId() != other.getInputId() || getInputData() != other.getInputData()) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$result = getResult();
        Object other$result = other.getResult();
        if (this$result != null ? !this$result.equals(other$result) : other$result != null) {
            return false;
        }
        Object this$tag = getTag();
        Object other$tag = other.getTag();
        return this$tag != null ? this$tag.equals(other$tag) : other$tag == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getInputId();
        int result2 = (result * 59) + getInputData();
        Object $type = getType();
        int result3 = (result2 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $result = getResult();
        int result4 = (result3 * 59) + ($result == null ? 43 : $result.hashCode());
        Object $tag = getTag();
        return (result4 * 59) + ($tag != null ? $tag.hashCode() : 43);
    }

    private FurnaceRecipeData(CraftingDataType type, int inputId, int inputData, ItemData result, String tag) {
        this.type = type;
        this.inputId = inputId;
        this.inputData = inputData;
        this.result = result;
        this.tag = tag;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.RecipeData
    public CraftingDataType getType() {
        return this.type;
    }

    public int getInputId() {
        return this.inputId;
    }

    public int getInputData() {
        return this.inputData;
    }

    public ItemData getResult() {
        return this.result;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.TaggedCraftingData
    public String getTag() {
        return this.tag;
    }

    public boolean hasData() {
        return this.type == CraftingDataType.FURNACE_DATA;
    }

    public static FurnaceRecipeData of(CraftingDataType type, int inputId, int inputData, ItemData result, String tag) {
        Preconditions.checkArgument(type == CraftingDataType.FURNACE || type == CraftingDataType.FURNACE_DATA, "type must be FURNACE or FURNACE_DATA");
        return new FurnaceRecipeData(type, inputId, inputData, result, tag);
    }

    public static FurnaceRecipeData of(int inputId, ItemData result, String tag) {
        return new FurnaceRecipeData(CraftingDataType.FURNACE, inputId, -1, result, tag);
    }

    public static FurnaceRecipeData of(int inputId, int inputData, ItemData result, String tag) {
        return new FurnaceRecipeData(CraftingDataType.FURNACE_DATA, inputId, inputData, result, tag);
    }
}
