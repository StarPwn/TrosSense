package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequestSlotData;

/* loaded from: classes5.dex */
public final class DestroyAction implements ItemStackRequestAction {
    private final int count;
    private final ItemStackRequestSlotData source;

    public DestroyAction(int count, ItemStackRequestSlotData source) {
        this.count = count;
        this.source = source;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DestroyAction)) {
            return false;
        }
        DestroyAction other = (DestroyAction) o;
        if (getCount() != other.getCount()) {
            return false;
        }
        Object this$source = getSource();
        Object other$source = other.getSource();
        return this$source != null ? this$source.equals(other$source) : other$source == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getCount();
        Object $source = getSource();
        return (result * 59) + ($source == null ? 43 : $source.hashCode());
    }

    public String toString() {
        return "DestroyAction(count=" + getCount() + ", source=" + getSource() + ")";
    }

    public int getCount() {
        return this.count;
    }

    public ItemStackRequestSlotData getSource() {
        return this.source;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.DESTROY;
    }
}
