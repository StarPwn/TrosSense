package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class CauldronUsedEventData implements EventData {
    private final int color;
    private final int fillLevel;
    private final int potionId;

    public CauldronUsedEventData(int potionId, int color, int fillLevel) {
        this.potionId = potionId;
        this.color = color;
        this.fillLevel = fillLevel;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CauldronUsedEventData)) {
            return false;
        }
        CauldronUsedEventData other = (CauldronUsedEventData) o;
        return getPotionId() == other.getPotionId() && getColor() == other.getColor() && getFillLevel() == other.getFillLevel();
    }

    public int hashCode() {
        int result = (1 * 59) + getPotionId();
        return (((result * 59) + getColor()) * 59) + getFillLevel();
    }

    public String toString() {
        return "CauldronUsedEventData(potionId=" + getPotionId() + ", color=" + getColor() + ", fillLevel=" + getFillLevel() + ")";
    }

    public int getPotionId() {
        return this.potionId;
    }

    public int getColor() {
        return this.color;
    }

    public int getFillLevel() {
        return this.fillLevel;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.CAULDRON_USED;
    }
}
