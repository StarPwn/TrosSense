package org.cloudburstmc.protocol.bedrock.data.skin;

import java.util.List;

/* loaded from: classes5.dex */
public final class PersonaPieceTintData {
    private final List<String> colors;
    private final String type;

    public PersonaPieceTintData(String type, List<String> colors) {
        this.type = type;
        this.colors = colors;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PersonaPieceTintData)) {
            return false;
        }
        PersonaPieceTintData other = (PersonaPieceTintData) o;
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$colors = getColors();
        Object other$colors = other.getColors();
        return this$colors != null ? this$colors.equals(other$colors) : other$colors == null;
    }

    public int hashCode() {
        Object $type = getType();
        int result = (1 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $colors = getColors();
        return (result * 59) + ($colors != null ? $colors.hashCode() : 43);
    }

    public String toString() {
        return "PersonaPieceTintData(type=" + getType() + ", colors=" + getColors() + ")";
    }

    public String getType() {
        return this.type;
    }

    public List<String> getColors() {
        return this.colors;
    }
}
