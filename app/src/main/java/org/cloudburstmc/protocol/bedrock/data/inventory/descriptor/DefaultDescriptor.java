package org.cloudburstmc.protocol.bedrock.data.inventory.descriptor;

import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* loaded from: classes5.dex */
public final class DefaultDescriptor implements ItemDescriptor {
    private final int auxValue;
    private final ItemDefinition itemId;

    public DefaultDescriptor(ItemDefinition itemId, int auxValue) {
        this.itemId = itemId;
        this.auxValue = auxValue;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DefaultDescriptor)) {
            return false;
        }
        DefaultDescriptor other = (DefaultDescriptor) o;
        if (getAuxValue() != other.getAuxValue()) {
            return false;
        }
        Object this$itemId = getItemId();
        Object other$itemId = other.getItemId();
        return this$itemId != null ? this$itemId.equals(other$itemId) : other$itemId == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getAuxValue();
        Object $itemId = getItemId();
        return (result * 59) + ($itemId == null ? 43 : $itemId.hashCode());
    }

    public String toString() {
        return "DefaultDescriptor(itemId=" + getItemId() + ", auxValue=" + getAuxValue() + ")";
    }

    public ItemDefinition getItemId() {
        return this.itemId;
    }

    public int getAuxValue() {
        return this.auxValue;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptor
    public ItemDescriptorType getType() {
        return ItemDescriptorType.DEFAULT;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptor
    public ItemData.Builder toItem() {
        return ItemData.builder().definition(this.itemId).damage(this.auxValue);
    }
}
