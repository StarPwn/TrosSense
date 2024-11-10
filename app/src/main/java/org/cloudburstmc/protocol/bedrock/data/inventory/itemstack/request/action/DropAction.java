package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequestSlotData;

/* loaded from: classes5.dex */
public final class DropAction implements ItemStackRequestAction {
    private final int count;
    private final boolean randomly;
    private final ItemStackRequestSlotData source;

    public DropAction(int count, ItemStackRequestSlotData source, boolean randomly) {
        this.count = count;
        this.source = source;
        this.randomly = randomly;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DropAction)) {
            return false;
        }
        DropAction other = (DropAction) o;
        if (getCount() != other.getCount() || isRandomly() != other.isRandomly()) {
            return false;
        }
        Object this$source = getSource();
        Object other$source = other.getSource();
        return this$source != null ? this$source.equals(other$source) : other$source == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getCount();
        int result2 = result * 59;
        int i = isRandomly() ? 79 : 97;
        Object $source = getSource();
        return ((result2 + i) * 59) + ($source == null ? 43 : $source.hashCode());
    }

    public String toString() {
        return "DropAction(count=" + getCount() + ", source=" + getSource() + ", randomly=" + isRandomly() + ")";
    }

    public int getCount() {
        return this.count;
    }

    public ItemStackRequestSlotData getSource() {
        return this.source;
    }

    public boolean isRandomly() {
        return this.randomly;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.DROP;
    }
}
