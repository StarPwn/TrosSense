package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class BellUsedEventData implements EventData {
    private final int itemId;

    public BellUsedEventData(int itemId) {
        this.itemId = itemId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BellUsedEventData)) {
            return false;
        }
        BellUsedEventData other = (BellUsedEventData) o;
        return getItemId() == other.getItemId();
    }

    public int hashCode() {
        int result = (1 * 59) + getItemId();
        return result;
    }

    public String toString() {
        return "BellUsedEventData(itemId=" + getItemId() + ")";
    }

    public int getItemId() {
        return this.itemId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.BELL_USED;
    }
}
