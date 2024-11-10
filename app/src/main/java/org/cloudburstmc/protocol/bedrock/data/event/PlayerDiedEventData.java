package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class PlayerDiedEventData implements EventData {
    private final int attackerEntityId;
    private final int attackerVariant;
    private final int entityDamageCause;
    private final boolean inRaid;

    public PlayerDiedEventData(int attackerEntityId, int attackerVariant, int entityDamageCause, boolean inRaid) {
        this.attackerEntityId = attackerEntityId;
        this.attackerVariant = attackerVariant;
        this.entityDamageCause = entityDamageCause;
        this.inRaid = inRaid;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerDiedEventData)) {
            return false;
        }
        PlayerDiedEventData other = (PlayerDiedEventData) o;
        return getAttackerEntityId() == other.getAttackerEntityId() && getAttackerVariant() == other.getAttackerVariant() && getEntityDamageCause() == other.getEntityDamageCause() && isInRaid() == other.isInRaid();
    }

    public int hashCode() {
        int result = (1 * 59) + getAttackerEntityId();
        return (((((result * 59) + getAttackerVariant()) * 59) + getEntityDamageCause()) * 59) + (isInRaid() ? 79 : 97);
    }

    public String toString() {
        return "PlayerDiedEventData(attackerEntityId=" + getAttackerEntityId() + ", attackerVariant=" + getAttackerVariant() + ", entityDamageCause=" + getEntityDamageCause() + ", inRaid=" + isInRaid() + ")";
    }

    public int getAttackerEntityId() {
        return this.attackerEntityId;
    }

    public int getAttackerVariant() {
        return this.attackerVariant;
    }

    public int getEntityDamageCause() {
        return this.entityDamageCause;
    }

    public boolean isInRaid() {
        return this.inRaid;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.PLAYER_DIED;
    }
}
