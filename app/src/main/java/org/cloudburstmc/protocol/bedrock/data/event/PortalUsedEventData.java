package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class PortalUsedEventData implements EventData {
    private final int fromDimensionId;
    private final int toDimensionId;

    public PortalUsedEventData(int fromDimensionId, int toDimensionId) {
        this.fromDimensionId = fromDimensionId;
        this.toDimensionId = toDimensionId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PortalUsedEventData)) {
            return false;
        }
        PortalUsedEventData other = (PortalUsedEventData) o;
        return getFromDimensionId() == other.getFromDimensionId() && getToDimensionId() == other.getToDimensionId();
    }

    public int hashCode() {
        int result = (1 * 59) + getFromDimensionId();
        return (result * 59) + getToDimensionId();
    }

    public String toString() {
        return "PortalUsedEventData(fromDimensionId=" + getFromDimensionId() + ", toDimensionId=" + getToDimensionId() + ")";
    }

    public int getFromDimensionId() {
        return this.fromDimensionId;
    }

    public int getToDimensionId() {
        return this.toDimensionId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.PORTAL_USED;
    }
}
