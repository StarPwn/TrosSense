package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public class CodeBuilderScoreboardEventData implements EventData {
    private final String objectiveName;
    private final int score;

    public CodeBuilderScoreboardEventData(String objectiveName, int score) {
        this.objectiveName = objectiveName;
        this.score = score;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CodeBuilderScoreboardEventData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CodeBuilderScoreboardEventData)) {
            return false;
        }
        CodeBuilderScoreboardEventData other = (CodeBuilderScoreboardEventData) o;
        if (!other.canEqual(this) || getScore() != other.getScore()) {
            return false;
        }
        Object this$objectiveName = getObjectiveName();
        Object other$objectiveName = other.getObjectiveName();
        return this$objectiveName != null ? this$objectiveName.equals(other$objectiveName) : other$objectiveName == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getScore();
        Object $objectiveName = getObjectiveName();
        return (result * 59) + ($objectiveName == null ? 43 : $objectiveName.hashCode());
    }

    public String toString() {
        return "CodeBuilderScoreboardEventData(objectiveName=" + getObjectiveName() + ", score=" + getScore() + ")";
    }

    public String getObjectiveName() {
        return this.objectiveName;
    }

    public int getScore() {
        return this.score;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.CODE_BUILDER_SCOREBOARD;
    }
}
