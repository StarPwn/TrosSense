package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class PetDiedEventData implements EventData {
    private final int entityDamageCause;
    private final long killerUniqueEntityId;
    private final boolean ownerKilled;
    private final int petEntityType;
    private final long petUniqueEntityId;

    public PetDiedEventData(boolean ownerKilled, long killerUniqueEntityId, long petUniqueEntityId, int entityDamageCause, int petEntityType) {
        this.ownerKilled = ownerKilled;
        this.killerUniqueEntityId = killerUniqueEntityId;
        this.petUniqueEntityId = petUniqueEntityId;
        this.entityDamageCause = entityDamageCause;
        this.petEntityType = petEntityType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PetDiedEventData)) {
            return false;
        }
        PetDiedEventData other = (PetDiedEventData) o;
        return isOwnerKilled() == other.isOwnerKilled() && getKillerUniqueEntityId() == other.getKillerUniqueEntityId() && getPetUniqueEntityId() == other.getPetUniqueEntityId() && getEntityDamageCause() == other.getEntityDamageCause() && getPetEntityType() == other.getPetEntityType();
    }

    public int hashCode() {
        int result = (1 * 59) + (isOwnerKilled() ? 79 : 97);
        long $killerUniqueEntityId = getKillerUniqueEntityId();
        long $petUniqueEntityId = getPetUniqueEntityId();
        return (((((((result * 59) + ((int) (($killerUniqueEntityId >>> 32) ^ $killerUniqueEntityId))) * 59) + ((int) (($petUniqueEntityId >>> 32) ^ $petUniqueEntityId))) * 59) + getEntityDamageCause()) * 59) + getPetEntityType();
    }

    public String toString() {
        return "PetDiedEventData(ownerKilled=" + isOwnerKilled() + ", killerUniqueEntityId=" + getKillerUniqueEntityId() + ", petUniqueEntityId=" + getPetUniqueEntityId() + ", entityDamageCause=" + getEntityDamageCause() + ", petEntityType=" + getPetEntityType() + ")";
    }

    public boolean isOwnerKilled() {
        return this.ownerKilled;
    }

    public long getKillerUniqueEntityId() {
        return this.killerUniqueEntityId;
    }

    public long getPetUniqueEntityId() {
        return this.petUniqueEntityId;
    }

    public int getEntityDamageCause() {
        return this.entityDamageCause;
    }

    public int getPetEntityType() {
        return this.petEntityType;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.PET_DIED;
    }
}
