package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class PatternRemovedEventData implements EventData {
    private final int auxValue;
    private final int itemId;
    private final int patternColor;
    private final int patternIndex;
    private final int patternsSize;

    public PatternRemovedEventData(int itemId, int auxValue, int patternsSize, int patternIndex, int patternColor) {
        this.itemId = itemId;
        this.auxValue = auxValue;
        this.patternsSize = patternsSize;
        this.patternIndex = patternIndex;
        this.patternColor = patternColor;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PatternRemovedEventData)) {
            return false;
        }
        PatternRemovedEventData other = (PatternRemovedEventData) o;
        return getItemId() == other.getItemId() && getAuxValue() == other.getAuxValue() && getPatternsSize() == other.getPatternsSize() && getPatternIndex() == other.getPatternIndex() && getPatternColor() == other.getPatternColor();
    }

    public int hashCode() {
        int result = (1 * 59) + getItemId();
        return (((((((result * 59) + getAuxValue()) * 59) + getPatternsSize()) * 59) + getPatternIndex()) * 59) + getPatternColor();
    }

    public String toString() {
        return "PatternRemovedEventData(itemId=" + getItemId() + ", auxValue=" + getAuxValue() + ", patternsSize=" + getPatternsSize() + ", patternIndex=" + getPatternIndex() + ", patternColor=" + getPatternColor() + ")";
    }

    public int getItemId() {
        return this.itemId;
    }

    public int getAuxValue() {
        return this.auxValue;
    }

    public int getPatternsSize() {
        return this.patternsSize;
    }

    public int getPatternIndex() {
        return this.patternIndex;
    }

    public int getPatternColor() {
        return this.patternColor;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.PATTERN_REMOVED;
    }
}
