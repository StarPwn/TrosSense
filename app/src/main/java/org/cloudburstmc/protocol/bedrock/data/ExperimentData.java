package org.cloudburstmc.protocol.bedrock.data;

/* loaded from: classes5.dex */
public final class ExperimentData {
    private final boolean enabled;
    private final String name;

    public ExperimentData(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ExperimentData)) {
            return false;
        }
        ExperimentData other = (ExperimentData) o;
        if (isEnabled() != other.isEnabled()) {
            return false;
        }
        Object this$name = getName();
        Object other$name = other.getName();
        return this$name != null ? this$name.equals(other$name) : other$name == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (isEnabled() ? 79 : 97);
        Object $name = getName();
        return (result * 59) + ($name == null ? 43 : $name.hashCode());
    }

    public String toString() {
        return "ExperimentData(name=" + getName() + ", enabled=" + isEnabled() + ")";
    }

    public String getName() {
        return this.name;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}
