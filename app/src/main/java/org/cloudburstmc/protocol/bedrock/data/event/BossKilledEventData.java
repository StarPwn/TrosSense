package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class BossKilledEventData implements EventData {
    private final int bossEntityType;
    private final long bossUniqueEntityId;
    private final int playerPartySize;

    public BossKilledEventData(long bossUniqueEntityId, int playerPartySize, int bossEntityType) {
        this.bossUniqueEntityId = bossUniqueEntityId;
        this.playerPartySize = playerPartySize;
        this.bossEntityType = bossEntityType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BossKilledEventData)) {
            return false;
        }
        BossKilledEventData other = (BossKilledEventData) o;
        return getBossUniqueEntityId() == other.getBossUniqueEntityId() && getPlayerPartySize() == other.getPlayerPartySize() && getBossEntityType() == other.getBossEntityType();
    }

    public int hashCode() {
        long $bossUniqueEntityId = getBossUniqueEntityId();
        int result = (1 * 59) + ((int) (($bossUniqueEntityId >>> 32) ^ $bossUniqueEntityId));
        return (((result * 59) + getPlayerPartySize()) * 59) + getBossEntityType();
    }

    public String toString() {
        return "BossKilledEventData(bossUniqueEntityId=" + getBossUniqueEntityId() + ", playerPartySize=" + getPlayerPartySize() + ", bossEntityType=" + getBossEntityType() + ")";
    }

    public long getBossUniqueEntityId() {
        return this.bossUniqueEntityId;
    }

    public int getPlayerPartySize() {
        return this.playerPartySize;
    }

    public int getBossEntityType() {
        return this.bossEntityType;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.BOSS_KILLED;
    }
}
