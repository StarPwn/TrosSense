package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response;

import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;

/* loaded from: classes5.dex */
public final class ItemStackResponseContainer {
    private final ContainerSlotType container;
    private final List<ItemStackResponseSlot> items;

    public ItemStackResponseContainer(ContainerSlotType container, List<ItemStackResponseSlot> items) {
        this.container = container;
        this.items = items;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemStackResponseContainer)) {
            return false;
        }
        ItemStackResponseContainer other = (ItemStackResponseContainer) o;
        Object this$container = getContainer();
        Object other$container = other.getContainer();
        if (this$container != null ? !this$container.equals(other$container) : other$container != null) {
            return false;
        }
        Object this$items = getItems();
        Object other$items = other.getItems();
        return this$items != null ? this$items.equals(other$items) : other$items == null;
    }

    public int hashCode() {
        Object $container = getContainer();
        int result = (1 * 59) + ($container == null ? 43 : $container.hashCode());
        Object $items = getItems();
        return (result * 59) + ($items != null ? $items.hashCode() : 43);
    }

    public String toString() {
        return "ItemStackResponseContainer(container=" + getContainer() + ", items=" + getItems() + ")";
    }

    public ContainerSlotType getContainer() {
        return this.container;
    }

    public List<ItemStackResponseSlot> getItems() {
        return this.items;
    }
}
