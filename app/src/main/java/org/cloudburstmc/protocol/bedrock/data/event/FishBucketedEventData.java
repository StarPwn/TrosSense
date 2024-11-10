package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class FishBucketedEventData implements EventData {
    private final int bucketedEntityType;
    private final int pattern;
    private final int preset;
    private final boolean releaseEvent;

    public FishBucketedEventData(int pattern, int preset, int bucketedEntityType, boolean releaseEvent) {
        this.pattern = pattern;
        this.preset = preset;
        this.bucketedEntityType = bucketedEntityType;
        this.releaseEvent = releaseEvent;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FishBucketedEventData)) {
            return false;
        }
        FishBucketedEventData other = (FishBucketedEventData) o;
        return getPattern() == other.getPattern() && getPreset() == other.getPreset() && getBucketedEntityType() == other.getBucketedEntityType() && isReleaseEvent() == other.isReleaseEvent();
    }

    public int hashCode() {
        int result = (1 * 59) + getPattern();
        return (((((result * 59) + getPreset()) * 59) + getBucketedEntityType()) * 59) + (isReleaseEvent() ? 79 : 97);
    }

    public String toString() {
        return "FishBucketedEventData(pattern=" + getPattern() + ", preset=" + getPreset() + ", bucketedEntityType=" + getBucketedEntityType() + ", releaseEvent=" + isReleaseEvent() + ")";
    }

    public int getPattern() {
        return this.pattern;
    }

    public int getPreset() {
        return this.preset;
    }

    public int getBucketedEntityType() {
        return this.bucketedEntityType;
    }

    public boolean isReleaseEvent() {
        return this.releaseEvent;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.FISH_BUCKETED;
    }
}
