package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class MobBornEventData implements EventData {
    private final int color;
    private final int entityType;
    private final int variant;

    public MobBornEventData(int entityType, int variant, int color) {
        this.entityType = entityType;
        this.variant = variant;
        this.color = color;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MobBornEventData)) {
            return false;
        }
        MobBornEventData other = (MobBornEventData) o;
        return getEntityType() == other.getEntityType() && getVariant() == other.getVariant() && getColor() == other.getColor();
    }

    public int hashCode() {
        int result = (1 * 59) + getEntityType();
        return (((result * 59) + getVariant()) * 59) + getColor();
    }

    public String toString() {
        return "MobBornEventData(entityType=" + getEntityType() + ", variant=" + getVariant() + ", color=" + getColor() + ")";
    }

    public int getEntityType() {
        return this.entityType;
    }

    public int getVariant() {
        return this.variant;
    }

    public int getColor() {
        return this.color;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.MOB_BORN;
    }
}
