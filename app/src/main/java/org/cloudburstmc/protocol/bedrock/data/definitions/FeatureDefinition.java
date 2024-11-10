package org.cloudburstmc.protocol.bedrock.data.definitions;

/* loaded from: classes5.dex */
public final class FeatureDefinition {
    private final String json;
    private final String name;

    public FeatureDefinition(String name, String json) {
        this.name = name;
        this.json = json;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FeatureDefinition)) {
            return false;
        }
        FeatureDefinition other = (FeatureDefinition) o;
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name != null ? !this$name.equals(other$name) : other$name != null) {
            return false;
        }
        Object this$json = getJson();
        Object other$json = other.getJson();
        return this$json != null ? this$json.equals(other$json) : other$json == null;
    }

    public int hashCode() {
        Object $name = getName();
        int result = (1 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $json = getJson();
        return (result * 59) + ($json != null ? $json.hashCode() : 43);
    }

    public String toString() {
        return "FeatureDefinition(name=" + getName() + ", json=" + getJson() + ")";
    }

    public String getName() {
        return this.name;
    }

    public String getJson() {
        return this.json;
    }
}
