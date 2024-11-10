package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class AchievementAwardedEventData implements EventData {
    private final int achievementId;

    public AchievementAwardedEventData(int achievementId) {
        this.achievementId = achievementId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AchievementAwardedEventData)) {
            return false;
        }
        AchievementAwardedEventData other = (AchievementAwardedEventData) o;
        return getAchievementId() == other.getAchievementId();
    }

    public int hashCode() {
        int result = (1 * 59) + getAchievementId();
        return result;
    }

    public String toString() {
        return "AchievementAwardedEventData(achievementId=" + getAchievementId() + ")";
    }

    public int getAchievementId() {
        return this.achievementId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.ACHIEVEMENT_AWARDED;
    }
}
