package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class MobKilledEventData implements EventData {
    private final int entityDamageCause;
    private final int killerEntityType;
    private final long killerUniqueEntityId;
    private final long victimUniqueEntityId;
    private String villagerDisplayName;
    private int villagerTradeTier;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MobKilledEventData)) {
            return false;
        }
        MobKilledEventData other = (MobKilledEventData) o;
        if (getKillerUniqueEntityId() != other.getKillerUniqueEntityId() || getVictimUniqueEntityId() != other.getVictimUniqueEntityId() || getKillerEntityType() != other.getKillerEntityType() || getEntityDamageCause() != other.getEntityDamageCause() || getVillagerTradeTier() != other.getVillagerTradeTier()) {
            return false;
        }
        Object this$villagerDisplayName = getVillagerDisplayName();
        Object other$villagerDisplayName = other.getVillagerDisplayName();
        return this$villagerDisplayName != null ? this$villagerDisplayName.equals(other$villagerDisplayName) : other$villagerDisplayName == null;
    }

    public int hashCode() {
        long $killerUniqueEntityId = getKillerUniqueEntityId();
        int result = (1 * 59) + ((int) (($killerUniqueEntityId >>> 32) ^ $killerUniqueEntityId));
        long $victimUniqueEntityId = getVictimUniqueEntityId();
        int result2 = (((((((result * 59) + ((int) (($victimUniqueEntityId >>> 32) ^ $victimUniqueEntityId))) * 59) + getKillerEntityType()) * 59) + getEntityDamageCause()) * 59) + getVillagerTradeTier();
        Object $villagerDisplayName = getVillagerDisplayName();
        return (result2 * 59) + ($villagerDisplayName == null ? 43 : $villagerDisplayName.hashCode());
    }

    public String toString() {
        return "MobKilledEventData(killerUniqueEntityId=" + getKillerUniqueEntityId() + ", victimUniqueEntityId=" + getVictimUniqueEntityId() + ", killerEntityType=" + getKillerEntityType() + ", entityDamageCause=" + getEntityDamageCause() + ", villagerTradeTier=" + getVillagerTradeTier() + ", villagerDisplayName=" + getVillagerDisplayName() + ")";
    }

    public MobKilledEventData(long killerUniqueEntityId, long victimUniqueEntityId, int killerEntityType, int entityDamageCause) {
        this.villagerTradeTier = -1;
        this.villagerDisplayName = "";
        this.killerUniqueEntityId = killerUniqueEntityId;
        this.victimUniqueEntityId = victimUniqueEntityId;
        this.killerEntityType = killerEntityType;
        this.entityDamageCause = entityDamageCause;
    }

    public MobKilledEventData(long killerUniqueEntityId, long victimUniqueEntityId, int killerEntityType, int entityDamageCause, int villagerTradeTier, String villagerDisplayName) {
        this.villagerTradeTier = -1;
        this.villagerDisplayName = "";
        this.killerUniqueEntityId = killerUniqueEntityId;
        this.victimUniqueEntityId = victimUniqueEntityId;
        this.killerEntityType = killerEntityType;
        this.entityDamageCause = entityDamageCause;
        this.villagerTradeTier = villagerTradeTier;
        this.villagerDisplayName = villagerDisplayName;
    }

    public long getKillerUniqueEntityId() {
        return this.killerUniqueEntityId;
    }

    public long getVictimUniqueEntityId() {
        return this.victimUniqueEntityId;
    }

    public int getKillerEntityType() {
        return this.killerEntityType;
    }

    public int getEntityDamageCause() {
        return this.entityDamageCause;
    }

    public int getVillagerTradeTier() {
        return this.villagerTradeTier;
    }

    public String getVillagerDisplayName() {
        return this.villagerDisplayName;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.MOB_KILLED;
    }
}
