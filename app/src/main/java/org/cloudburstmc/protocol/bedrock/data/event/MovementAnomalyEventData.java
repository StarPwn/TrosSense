package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class MovementAnomalyEventData implements EventData {
    private final float averagePositionDelta;
    private final float cheatingScore;
    private final int eventType;
    private final float maxPositionDelta;
    private final float minPositionDelta;
    private final float totalPositionDelta;

    public MovementAnomalyEventData(int eventType, float cheatingScore, float averagePositionDelta, float totalPositionDelta, float minPositionDelta, float maxPositionDelta) {
        this.eventType = eventType;
        this.cheatingScore = cheatingScore;
        this.averagePositionDelta = averagePositionDelta;
        this.totalPositionDelta = totalPositionDelta;
        this.minPositionDelta = minPositionDelta;
        this.maxPositionDelta = maxPositionDelta;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MovementAnomalyEventData)) {
            return false;
        }
        MovementAnomalyEventData other = (MovementAnomalyEventData) o;
        return getEventType() == other.getEventType() && Float.compare(getCheatingScore(), other.getCheatingScore()) == 0 && Float.compare(getAveragePositionDelta(), other.getAveragePositionDelta()) == 0 && Float.compare(getTotalPositionDelta(), other.getTotalPositionDelta()) == 0 && Float.compare(getMinPositionDelta(), other.getMinPositionDelta()) == 0 && Float.compare(getMaxPositionDelta(), other.getMaxPositionDelta()) == 0;
    }

    public int hashCode() {
        int result = (1 * 59) + getEventType();
        return (((((((((result * 59) + Float.floatToIntBits(getCheatingScore())) * 59) + Float.floatToIntBits(getAveragePositionDelta())) * 59) + Float.floatToIntBits(getTotalPositionDelta())) * 59) + Float.floatToIntBits(getMinPositionDelta())) * 59) + Float.floatToIntBits(getMaxPositionDelta());
    }

    public String toString() {
        return "MovementAnomalyEventData(eventType=" + getEventType() + ", cheatingScore=" + getCheatingScore() + ", averagePositionDelta=" + getAveragePositionDelta() + ", totalPositionDelta=" + getTotalPositionDelta() + ", minPositionDelta=" + getMinPositionDelta() + ", maxPositionDelta=" + getMaxPositionDelta() + ")";
    }

    public int getEventType() {
        return this.eventType;
    }

    public float getCheatingScore() {
        return this.cheatingScore;
    }

    public float getAveragePositionDelta() {
        return this.averagePositionDelta;
    }

    public float getTotalPositionDelta() {
        return this.totalPositionDelta;
    }

    public float getMinPositionDelta() {
        return this.minPositionDelta;
    }

    public float getMaxPositionDelta() {
        return this.maxPositionDelta;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.MOVEMENT_ANOMALY;
    }
}
