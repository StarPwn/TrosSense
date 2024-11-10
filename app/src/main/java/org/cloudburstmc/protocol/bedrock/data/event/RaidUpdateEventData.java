package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class RaidUpdateEventData implements EventData {
    private final int currentWave;
    private final int totalWaves;
    private final boolean winner;

    public RaidUpdateEventData(int currentWave, int totalWaves, boolean winner) {
        this.currentWave = currentWave;
        this.totalWaves = totalWaves;
        this.winner = winner;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RaidUpdateEventData)) {
            return false;
        }
        RaidUpdateEventData other = (RaidUpdateEventData) o;
        return getCurrentWave() == other.getCurrentWave() && getTotalWaves() == other.getTotalWaves() && isWinner() == other.isWinner();
    }

    public int hashCode() {
        int result = (1 * 59) + getCurrentWave();
        return (((result * 59) + getTotalWaves()) * 59) + (isWinner() ? 79 : 97);
    }

    public String toString() {
        return "RaidUpdateEventData(currentWave=" + getCurrentWave() + ", totalWaves=" + getTotalWaves() + ", winner=" + isWinner() + ")";
    }

    public int getCurrentWave() {
        return this.currentWave;
    }

    public int getTotalWaves() {
        return this.totalWaves;
    }

    public boolean isWinner() {
        return this.winner;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.RAID_UPDATE;
    }
}
