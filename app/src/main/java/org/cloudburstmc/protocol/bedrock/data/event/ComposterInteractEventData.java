package org.cloudburstmc.protocol.bedrock.data.event;

import org.cloudburstmc.protocol.bedrock.data.BlockInteractionType;

/* loaded from: classes5.dex */
public final class ComposterInteractEventData implements EventData {
    private final BlockInteractionType blockInteractionType;
    private final int itemId;

    public ComposterInteractEventData(BlockInteractionType blockInteractionType, int itemId) {
        this.blockInteractionType = blockInteractionType;
        this.itemId = itemId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ComposterInteractEventData)) {
            return false;
        }
        ComposterInteractEventData other = (ComposterInteractEventData) o;
        if (getItemId() != other.getItemId()) {
            return false;
        }
        Object this$blockInteractionType = getBlockInteractionType();
        Object other$blockInteractionType = other.getBlockInteractionType();
        return this$blockInteractionType != null ? this$blockInteractionType.equals(other$blockInteractionType) : other$blockInteractionType == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getItemId();
        Object $blockInteractionType = getBlockInteractionType();
        return (result * 59) + ($blockInteractionType == null ? 43 : $blockInteractionType.hashCode());
    }

    public String toString() {
        return "ComposterInteractEventData(blockInteractionType=" + getBlockInteractionType() + ", itemId=" + getItemId() + ")";
    }

    public BlockInteractionType getBlockInteractionType() {
        return this.blockInteractionType;
    }

    public int getItemId() {
        return this.itemId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.COMPOSTER_INTERACT;
    }
}
