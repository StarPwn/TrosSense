package org.cloudburstmc.protocol.bedrock.data;

/* loaded from: classes5.dex */
public class TrimPattern {
    private final String itemName;
    private final String patternId;

    public TrimPattern(String itemName, String patternId) {
        this.itemName = itemName;
        this.patternId = patternId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof TrimPattern;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TrimPattern)) {
            return false;
        }
        TrimPattern other = (TrimPattern) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$itemName = getItemName();
        Object other$itemName = other.getItemName();
        if (this$itemName != null ? !this$itemName.equals(other$itemName) : other$itemName != null) {
            return false;
        }
        Object this$patternId = getPatternId();
        Object other$patternId = other.getPatternId();
        return this$patternId != null ? this$patternId.equals(other$patternId) : other$patternId == null;
    }

    public int hashCode() {
        Object $itemName = getItemName();
        int result = (1 * 59) + ($itemName == null ? 43 : $itemName.hashCode());
        Object $patternId = getPatternId();
        return (result * 59) + ($patternId != null ? $patternId.hashCode() : 43);
    }

    public String toString() {
        return "TrimPattern(itemName=" + getItemName() + ", patternId=" + getPatternId() + ")";
    }

    public String getItemName() {
        return this.itemName;
    }

    public String getPatternId() {
        return this.patternId;
    }
}
