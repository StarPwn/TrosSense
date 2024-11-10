package org.cloudburstmc.protocol.bedrock.data.inventory.crafting;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;

/* loaded from: classes5.dex */
public final class MaterialReducer {
    private final int inputId;
    private final Object2IntMap<ItemDefinition> itemCounts;

    public MaterialReducer(int inputId, Object2IntMap<ItemDefinition> itemCounts) {
        this.inputId = inputId;
        this.itemCounts = itemCounts;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MaterialReducer)) {
            return false;
        }
        MaterialReducer other = (MaterialReducer) o;
        if (getInputId() != other.getInputId()) {
            return false;
        }
        Object this$itemCounts = getItemCounts();
        Object other$itemCounts = other.getItemCounts();
        return this$itemCounts != null ? this$itemCounts.equals(other$itemCounts) : other$itemCounts == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getInputId();
        Object $itemCounts = getItemCounts();
        return (result * 59) + ($itemCounts == null ? 43 : $itemCounts.hashCode());
    }

    public String toString() {
        return "MaterialReducer(inputId=" + getInputId() + ", itemCounts=" + getItemCounts() + ")";
    }

    public int getInputId() {
        return this.inputId;
    }

    public Object2IntMap<ItemDefinition> getItemCounts() {
        return this.itemCounts;
    }
}
