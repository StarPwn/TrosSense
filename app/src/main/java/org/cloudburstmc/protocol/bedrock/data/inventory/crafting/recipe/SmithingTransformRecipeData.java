package org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe;

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.CraftingDataType;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount;

/* loaded from: classes5.dex */
public class SmithingTransformRecipeData implements TaggedCraftingData, IdentifiableRecipeData, NetworkRecipeData {
    private final ItemDescriptorWithCount addition;
    private final ItemDescriptorWithCount base;
    private final String id;
    private final int netId;
    private final ItemData result;
    private final String tag;
    private final ItemDescriptorWithCount template;

    public String toString() {
        return "SmithingTransformRecipeData(id=" + getId() + ", template=" + getTemplate() + ", base=" + getBase() + ", addition=" + getAddition() + ", result=" + getResult() + ", tag=" + getTag() + ", netId=" + getNetId() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof SmithingTransformRecipeData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SmithingTransformRecipeData)) {
            return false;
        }
        SmithingTransformRecipeData other = (SmithingTransformRecipeData) o;
        if (!other.canEqual(this) || getNetId() != other.getNetId()) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        if (this$id != null ? !this$id.equals(other$id) : other$id != null) {
            return false;
        }
        Object this$template = getTemplate();
        Object other$template = other.getTemplate();
        if (this$template != null ? !this$template.equals(other$template) : other$template != null) {
            return false;
        }
        Object this$base = getBase();
        Object other$base = other.getBase();
        if (this$base != null ? !this$base.equals(other$base) : other$base != null) {
            return false;
        }
        Object this$addition = getAddition();
        Object other$addition = other.getAddition();
        if (this$addition != null ? !this$addition.equals(other$addition) : other$addition != null) {
            return false;
        }
        Object this$result = getResult();
        Object other$result = other.getResult();
        if (this$result != null ? !this$result.equals(other$result) : other$result != null) {
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
        int result = (1 * 59) + getNetId();
        Object $id = getId();
        int result2 = (result * 59) + ($id == null ? 43 : $id.hashCode());
        Object $template = getTemplate();
        int result3 = (result2 * 59) + ($template == null ? 43 : $template.hashCode());
        Object $base = getBase();
        int result4 = (result3 * 59) + ($base == null ? 43 : $base.hashCode());
        Object $addition = getAddition();
        int result5 = (result4 * 59) + ($addition == null ? 43 : $addition.hashCode());
        Object $result = getResult();
        int result6 = (result5 * 59) + ($result == null ? 43 : $result.hashCode());
        Object $tag = getTag();
        return (result6 * 59) + ($tag != null ? $tag.hashCode() : 43);
    }

    private SmithingTransformRecipeData(String id, ItemDescriptorWithCount template, ItemDescriptorWithCount base, ItemDescriptorWithCount addition, ItemData result, String tag, int netId) {
        this.id = id;
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.result = result;
        this.tag = tag;
        this.netId = netId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.IdentifiableRecipeData
    public String getId() {
        return this.id;
    }

    public ItemDescriptorWithCount getTemplate() {
        return this.template;
    }

    public ItemDescriptorWithCount getBase() {
        return this.base;
    }

    public ItemDescriptorWithCount getAddition() {
        return this.addition;
    }

    public ItemData getResult() {
        return this.result;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.TaggedCraftingData
    public String getTag() {
        return this.tag;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.NetworkRecipeData
    public int getNetId() {
        return this.netId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.RecipeData
    public CraftingDataType getType() {
        return CraftingDataType.SMITHING_TRANSFORM;
    }

    public static SmithingTransformRecipeData of(String id, ItemDescriptorWithCount template, ItemDescriptorWithCount base, ItemDescriptorWithCount addition, ItemData result, String tag, int netId) {
        return new SmithingTransformRecipeData(id, template, base, addition, result, tag, netId);
    }

    public static SmithingTransformRecipeData of(String id, ItemDescriptorWithCount base, ItemDescriptorWithCount addition, ItemData result, String tag, int netId) {
        return new SmithingTransformRecipeData(id, null, base, addition, result, tag, netId);
    }
}
