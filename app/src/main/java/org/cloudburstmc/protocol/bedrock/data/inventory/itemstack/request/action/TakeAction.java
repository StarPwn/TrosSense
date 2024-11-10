package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequestSlotData;

/* loaded from: classes5.dex */
public final class TakeAction implements TransferItemStackRequestAction {
    private final int count;
    private final ItemStackRequestSlotData destination;
    private final ItemStackRequestSlotData source;

    public TakeAction(int count, ItemStackRequestSlotData source, ItemStackRequestSlotData destination) {
        this.count = count;
        this.source = source;
        this.destination = destination;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TakeAction)) {
            return false;
        }
        TakeAction other = (TakeAction) o;
        if (getCount() != other.getCount()) {
            return false;
        }
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
        int result = (1 * 59) + getCount();
        Object $source = getSource();
        int result2 = (result * 59) + ($source == null ? 43 : $source.hashCode());
        Object $destination = getDestination();
        return (result2 * 59) + ($destination != null ? $destination.hashCode() : 43);
    }

    public String toString() {
        return "TakeAction(count=" + getCount() + ", source=" + getSource() + ", destination=" + getDestination() + ")";
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.TransferItemStackRequestAction
    public int getCount() {
        return this.count;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.TransferItemStackRequestAction
    public ItemStackRequestSlotData getSource() {
        return this.source;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.TransferItemStackRequestAction
    public ItemStackRequestSlotData getDestination() {
        return this.destination;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.TAKE;
    }
}
