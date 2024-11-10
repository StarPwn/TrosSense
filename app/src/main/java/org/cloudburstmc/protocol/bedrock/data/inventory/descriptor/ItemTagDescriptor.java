package org.cloudburstmc.protocol.bedrock.data.inventory.descriptor;

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* loaded from: classes5.dex */
public final class ItemTagDescriptor implements ItemDescriptor {
    private final String itemTag;

    public ItemTagDescriptor(String itemTag) {
        this.itemTag = itemTag;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemTagDescriptor)) {
            return false;
        }
        ItemTagDescriptor other = (ItemTagDescriptor) o;
        Object this$itemTag = getItemTag();
        Object other$itemTag = other.getItemTag();
        return this$itemTag != null ? this$itemTag.equals(other$itemTag) : other$itemTag == null;
    }

    public int hashCode() {
        Object $itemTag = getItemTag();
        int result = (1 * 59) + ($itemTag == null ? 43 : $itemTag.hashCode());
        return result;
    }

    public String toString() {
        return "ItemTagDescriptor(itemTag=" + getItemTag() + ")";
    }

    public String getItemTag() {
        return this.itemTag;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptor
    public ItemDescriptorType getType() {
        return ItemDescriptorType.ITEM_TAG;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptor
    public ItemData.Builder toItem() {
        throw new UnsupportedOperationException();
    }
}
