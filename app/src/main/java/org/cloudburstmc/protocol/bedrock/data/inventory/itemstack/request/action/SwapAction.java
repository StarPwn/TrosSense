package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequestSlotData;

/* loaded from: classes5.dex */
public final class SwapAction implements ItemStackRequestAction {
    private final ItemStackRequestSlotData destination;
    private final ItemStackRequestSlotData source;

    public SwapAction(ItemStackRequestSlotData source, ItemStackRequestSlotData destination) {
        this.source = source;
        this.destination = destination;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SwapAction)) {
            return false;
        }
        SwapAction other = (SwapAction) o;
        Object this$source = getSource();
        Object other$source = other.getSource();
        if (this$source != null ? !this$source.equals(other$source) : other$source != null) {
            return false;
        }
        Object this$destination = getDestination();
        Object other$destination = other.getDestination();
        return this$destination != null ? this$destination.equals(other$destination) : other$destination == null;
    }

    public int hashCode() {
        Object $source = getSource();
        int result = (1 * 59) + ($source == null ? 43 : $source.hashCode());
        Object $destination = getDestination();
        return (result * 59) + ($destination != null ? $destination.hashCode() : 43);
    }

    public String toString() {
        return "SwapAction(source=" + getSource() + ", destination=" + getDestination() + ")";
    }

    public ItemStackRequestSlotData getSource() {
        return this.source;
    }

    public ItemStackRequestSlotData getDestination() {
        return this.destination;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.SWAP;
    }
}
