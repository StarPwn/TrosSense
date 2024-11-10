package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request;

import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;

/* loaded from: classes5.dex */
public final class ItemStackRequestSlotData {
    private final ContainerSlotType container;
    private final int slot;
    private final int stackNetworkId;

    public ItemStackRequestSlotData(ContainerSlotType container, int slot, int stackNetworkId) {
        this.container = container;
        this.slot = slot;
        this.stackNetworkId = stackNetworkId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemStackRequestSlotData)) {
            return false;
        }
        ItemStackRequestSlotData other = (ItemStackRequestSlotData) o;
        if (getSlot() != other.getSlot() || getStackNetworkId() != other.getStackNetworkId()) {
            return false;
        }
        Object this$container = getContainer();
        Object other$container = other.getContainer();
        return this$container != null ? this$container.equals(other$container) : other$container == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getSlot();
        int result2 = (result * 59) + getStackNetworkId();
        Object $container = getContainer();
        return (result2 * 59) + ($container == null ? 43 : $container.hashCode());
    }

    public String toString() {
        return "ItemStackRequestSlotData(container=" + getContainer() + ", slot=" + getSlot() + ", stackNetworkId=" + getStackNetworkId() + ")";
    }

    public ContainerSlotType getContainer() {
        return this.container;
    }

    public int getSlot() {
        return this.slot;
    }

    public int getStackNetworkId() {
        return this.stackNetworkId;
    }
}
