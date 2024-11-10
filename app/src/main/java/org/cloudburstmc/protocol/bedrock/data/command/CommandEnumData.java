package org.cloudburstmc.protocol.bedrock.data.command;

import java.util.Map;
import java.util.Set;

/* loaded from: classes5.dex */
public final class CommandEnumData {
    private final boolean isSoft;
    private final String name;
    private final Map<String, Set<CommandEnumConstraint>> values;

    public CommandEnumData(String name, Map<String, Set<CommandEnumConstraint>> values, boolean isSoft) {
        this.name = name;
        this.values = values;
        this.isSoft = isSoft;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommandEnumData)) {
            return false;
        }
        CommandEnumData other = (CommandEnumData) o;
        if (isSoft() != other.isSoft()) {
            return false;
        }
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name != null ? !this$name.equals(other$name) : other$name != null) {
            return false;
        }
        Object this$values = getValues();
        Object other$values = other.getValues();
        return this$values != null ? this$values.equals(other$values) : other$values == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (isSoft() ? 79 : 97);
        Object $name = getName();
        int result2 = (result * 59) + ($name == null ? 43 : $name.hashCode());
        Object $values = getValues();
        return (result2 * 59) + ($values != null ? $values.hashCode() : 43);
    }

    public String toString() {
        return "CommandEnumData(name=" + getName() + ", values=" + getValues() + ", isSoft=" + isSoft() + ")";
    }

    public String getName() {
        return this.name;
    }

    public Map<String, Set<CommandEnumConstraint>> getValues() {
        return this.values;
    }

    public boolean isSoft() {
        return this.isSoft;
    }
}
