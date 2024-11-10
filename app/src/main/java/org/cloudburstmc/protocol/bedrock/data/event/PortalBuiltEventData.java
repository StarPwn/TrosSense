package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class PortalBuiltEventData implements EventData {
    private final int dimensionId;

    public PortalBuiltEventData(int dimensionId) {
        this.dimensionId = dimensionId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PortalBuiltEventData)) {
            return false;
        }
        PortalBuiltEventData other = (PortalBuiltEventData) o;
        return getDimensionId() == other.getDimensionId();
    }

    public int hashCode() {
        int result = (1 * 59) + getDimensionId();
        return result;
    }

    public String toString() {
        return "PortalBuiltEventData(dimensionId=" + getDimensionId() + ")";
    }

    public int getDimensionId() {
        return this.dimensionId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.PORTAL_BUILT;
    }
}
