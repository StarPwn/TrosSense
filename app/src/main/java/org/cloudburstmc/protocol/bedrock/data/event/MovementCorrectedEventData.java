package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class MovementCorrectedEventData implements EventData {
    private final float cheatingScore;
    private final float distanceThreshold;
    private final int durationThreshold;
    private final float positionDelta;
    private final float scoreThreshold;

    public MovementCorrectedEventData(float positionDelta, float cheatingScore, float scoreThreshold, float distanceThreshold, int durationThreshold) {
        this.positionDelta = positionDelta;
        this.cheatingScore = cheatingScore;
        this.scoreThreshold = scoreThreshold;
        this.distanceThreshold = distanceThreshold;
        this.durationThreshold = durationThreshold;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MovementCorrectedEventData)) {
            return false;
        }
        MovementCorrectedEventData other = (MovementCorrectedEventData) o;
        return Float.compare(getPositionDelta(), other.getPositionDelta()) == 0 && Float.compare(getCheatingScore(), other.getCheatingScore()) == 0 && Float.compare(getScoreThreshold(), other.getScoreThreshold()) == 0 && Float.compare(getDistanceThreshold(), other.getDistanceThreshold()) == 0 && getDurationThreshold() == other.getDurationThreshold();
    }

    public int hashCode() {
        int result = (1 * 59) + Float.floatToIntBits(getPositionDelta());
        return (((((((result * 59) + Float.floatToIntBits(getCheatingScore())) * 59) + Float.floatToIntBits(getScoreThreshold())) * 59) + Float.floatToIntBits(getDistanceThreshold())) * 59) + getDurationThreshold();
    }

    public String toString() {
        return "MovementCorrectedEventData(positionDelta=" + getPositionDelta() + ", cheatingScore=" + getCheatingScore() + ", scoreThreshold=" + getScoreThreshold() + ", distanceThreshold=" + getDistanceThreshold() + ", durationThreshold=" + getDurationThreshold() + ")";
    }

    public float getPositionDelta() {
        return this.positionDelta;
    }

    public float getCheatingScore() {
        return this.cheatingScore;
    }

    public float getScoreThreshold() {
        return this.scoreThreshold;
    }

    public float getDistanceThreshold() {
        return this.distanceThreshold;
    }

    public int getDurationThreshold() {
        return this.durationThreshold;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.MOVEMENT_CORRECTED;
    }
}
