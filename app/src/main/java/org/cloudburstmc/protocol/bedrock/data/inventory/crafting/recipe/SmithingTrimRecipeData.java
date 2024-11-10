package org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe;

import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.CraftingDataType;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount;

/* loaded from: classes5.dex */
public class SmithingTrimRecipeData implements TaggedCraftingData, IdentifiableRecipeData, NetworkRecipeData {
    private final ItemDescriptorWithCount addition;
    private final ItemDescriptorWithCount base;
    private final String id;
    private final int netId;
    private final String tag;
    private final ItemDescriptorWithCount template;

    public String toString() {
        return "SmithingTrimRecipeData(id=" + getId() + ", base=" + getBase() + ", addition=" + getAddition() + ", template=" + getTemplate() + ", tag=" + getTag() + ", netId=" + getNetId() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof SmithingTrimRecipeData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SmithingTrimRecipeData)) {
            return false;
        }
        SmithingTrimRecipeData other = (SmithingTrimRecipeData) o;
        if (!other.canEqual(this) || getNetId() != other.getNetId()) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        if (this$id != null ? !this$id.equals(other$id) : other$id != null) {
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
        Object this$template = getTemplate();
        Object other$template = other.getTemplate();
        if (this$template != null ? !this$template.equals(other$template) : other$template != null) {
            return false;
        }
        Object this$tag = getTag();
        Object other$tag = other.getTag();
        return this$tag != null ? this$tag.equals(other$tag) : other$tag == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getNetId();
        Object $id = getId();
        int result2 = (result * 59) + ($id == null ? 43 : $id.hashCode());
        Object $base = getBase();
        int result3 = (result2 * 59) + ($base == null ? 43 : $base.hashCode());
        Object $addition = getAddition();
        int result4 = (result3 * 59) + ($addition == null ? 43 : $addition.hashCode());
        Object $template = getTemplate();
        int result5 = (result4 * 59) + ($template == null ? 43 : $template.hashCode());
        Object $tag = getTag();
        return (result5 * 59) + ($tag != null ? $tag.hashCode() : 43);
    }

    private SmithingTrimRecipeData(String id, ItemDescriptorWithCount base, ItemDescriptorWithCount addition, ItemDescriptorWithCount template, String tag, int netId) {
        this.id = id;
        this.base = base;
        this.addition = addition;
        this.template = template;
        this.tag = tag;
        this.netId = netId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.IdentifiableRecipeData
    public String getId() {
        return this.id;
    }

    public ItemDescriptorWithCount getBase() {
        return this.base;
    }

    public ItemDescriptorWithCount getAddition() {
        return this.addition;
    }

    public ItemDescriptorWithCount getTemplate() {
        return this.template;
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
        return CraftingDataType.SMITHING_TRIM;
    }

    public static SmithingTrimRecipeData of(String id, ItemDescriptorWithCount base, ItemDescriptorWithCount addition, ItemDescriptorWithCount template, String tag, int netId) {
        return new SmithingTrimRecipeData(id, base, addition, template, tag, netId);
    }
}
