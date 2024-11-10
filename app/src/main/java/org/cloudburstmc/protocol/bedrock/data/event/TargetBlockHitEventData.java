package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public class TargetBlockHitEventData implements EventData {
    private final int redstoneLevel;

    public TargetBlockHitEventData(int redstoneLevel) {
        this.redstoneLevel = redstoneLevel;
    }

    protected boolean canEqual(Object other) {
        return other instanceof TargetBlockHitEventData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TargetBlockHitEventData)) {
            return false;
        }
        TargetBlockHitEventData other = (TargetBlockHitEventData) o;
        return other.canEqual(this) && getRedstoneLevel() == other.getRedstoneLevel();
    }

    public int hashCode() {
        int result = (1 * 59) + getRedstoneLevel();
        return result;
    }

    public String toString() {
        return "TargetBlockHitEventData(redstoneLevel=" + getRedstoneLevel() + ")";
    }

    public int getRedstoneLevel() {
        return this.redstoneLevel;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.TARGET_BLOCK_HIT;
    }
}
