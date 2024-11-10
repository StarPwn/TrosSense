package org.cloudburstmc.protocol.bedrock.data;

import org.cloudburstmc.protocol.common.util.Preconditions;

/* loaded from: classes5.dex */
public class ScoreInfo {
    private final long entityId;
    private final String name;
    private final String objectiveId;
    private final int score;
    private final long scoreboardId;
    private final ScorerType type;

    /* loaded from: classes5.dex */
    public enum ScorerType {
        INVALID,
        PLAYER,
        ENTITY,
        FAKE
    }

    protected boolean canEqual(Object other) {
        return other instanceof ScoreInfo;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ScoreInfo)) {
            return false;
        }
        ScoreInfo other = (ScoreInfo) o;
        if (!other.canEqual(this) || getScoreboardId() != other.getScoreboardId() || getScore() != other.getScore() || getEntityId() != other.getEntityId()) {
            return false;
        }
        Object this$objectiveId = getObjectiveId();
        Object other$objectiveId = other.getObjectiveId();
        if (this$objectiveId != null ? !this$objectiveId.equals(other$objectiveId) : other$objectiveId != null) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$name = getName();
        Object other$name = other.getName();
        return this$name != null ? this$name.equals(other$name) : other$name == null;
    }

    public int hashCode() {
        long $scoreboardId = getScoreboardId();
        int result = (1 * 59) + ((int) (($scoreboardId >>> 32) ^ $scoreboardId));
        int result2 = (result * 59) + getScore();
        long $entityId = getEntityId();
        int result3 = (result2 * 59) + ((int) (($entityId >>> 32) ^ $entityId));
        Object $objectiveId = getObjectiveId();
        int result4 = (result3 * 59) + ($objectiveId == null ? 43 : $objectiveId.hashCode());
        Object $type = getType();
        int result5 = (result4 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $name = getName();
        return (result5 * 59) + ($name != null ? $name.hashCode() : 43);
    }

    public String toString() {
        return "ScoreInfo(scoreboardId=" + getScoreboardId() + ", objectiveId=" + getObjectiveId() + ", score=" + getScore() + ", type=" + getType() + ", name=" + getName() + ", entityId=" + getEntityId() + ")";
    }

    public long getScoreboardId() {
        return this.scoreboardId;
    }

    public String getObjectiveId() {
        return this.objectiveId;
    }

    public int getScore() {
        return this.score;
    }

    public ScorerType getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public long getEntityId() {
        return this.entityId;
    }

    public ScoreInfo(long scoreboardId, String objectiveId, int score) {
        this.scoreboardId = scoreboardId;
        this.objectiveId = objectiveId;
        this.score = score;
        this.type = ScorerType.INVALID;
        this.name = null;
        this.entityId = -1L;
    }

    public ScoreInfo(long scoreboardId, String objectiveId, int score, String name) {
        this.scoreboardId = scoreboardId;
        this.objectiveId = objectiveId;
        this.score = score;
        this.type = ScorerType.FAKE;
        this.name = name;
        this.entityId = -1L;
    }

    public ScoreInfo(long scoreboardId, String objectiveId, int score, ScorerType type, long entityId) {
        Preconditions.checkArgument(type == ScorerType.ENTITY || type == ScorerType.PLAYER, "Must be player or entity");
        this.scoreboardId = scoreboardId;
        this.objectiveId = objectiveId;
        this.score = score;
        this.type = type;
        this.entityId = entityId;
        this.name = null;
    }
}
