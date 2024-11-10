package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class EntityInteractEventData implements EventData {
    private final int interactionType;
    private final int legacyEntityTypeId;
    private final int paletteColor;
    private final int variant;

    public EntityInteractEventData(int interactionType, int legacyEntityTypeId, int variant, int paletteColor) {
        this.interactionType = interactionType;
        this.legacyEntityTypeId = legacyEntityTypeId;
        this.variant = variant;
        this.paletteColor = paletteColor;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EntityInteractEventData)) {
            return false;
        }
        EntityInteractEventData other = (EntityInteractEventData) o;
        return getInteractionType() == other.getInteractionType() && getLegacyEntityTypeId() == other.getLegacyEntityTypeId() && getVariant() == other.getVariant() && getPaletteColor() == other.getPaletteColor();
    }

    public int hashCode() {
        int result = (1 * 59) + getInteractionType();
        return (((((result * 59) + getLegacyEntityTypeId()) * 59) + getVariant()) * 59) + getPaletteColor();
    }

    public String toString() {
        return "EntityInteractEventData(interactionType=" + getInteractionType() + ", legacyEntityTypeId=" + getLegacyEntityTypeId() + ", variant=" + getVariant() + ", paletteColor=" + getPaletteColor() + ")";
    }

    public int getInteractionType() {
        return this.interactionType;
    }

    public int getLegacyEntityTypeId() {
        return this.legacyEntityTypeId;
    }

    public int getVariant() {
        return this.variant;
    }

    public int getPaletteColor() {
        return this.paletteColor;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.ENTITY_INTERACT;
    }
}
