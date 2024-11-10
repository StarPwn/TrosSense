package org.cloudburstmc.protocol.bedrock.data.command;

import java.util.EnumSet;
import java.util.Set;

/* loaded from: classes5.dex */
public class CommandParamData {
    private CommandEnumData enumData;
    private String name;
    private boolean optional;
    private final Set<CommandParamOption> options = EnumSet.noneOf(CommandParamOption.class);
    private String postfix;
    private CommandParam type;

    protected boolean canEqual(Object other) {
        return other instanceof CommandParamData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommandParamData)) {
            return false;
        }
        CommandParamData other = (CommandParamData) o;
        if (!other.canEqual(this) || isOptional() != other.isOptional()) {
            return false;
        }
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name != null ? !this$name.equals(other$name) : other$name != null) {
            return false;
        }
        Object this$enumData = getEnumData();
        Object other$enumData = other.getEnumData();
        if (this$enumData != null ? !this$enumData.equals(other$enumData) : other$enumData != null) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$postfix = getPostfix();
        Object other$postfix = other.getPostfix();
        if (this$postfix != null ? !this$postfix.equals(other$postfix) : other$postfix != null) {
            return false;
        }
        Object this$options = getOptions();
        Object other$options = other.getOptions();
        return this$options != null ? this$options.equals(other$options) : other$options == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (isOptional() ? 79 : 97);
        Object $name = getName();
        int result2 = (result * 59) + ($name == null ? 43 : $name.hashCode());
        Object $enumData = getEnumData();
        int result3 = (result2 * 59) + ($enumData == null ? 43 : $enumData.hashCode());
        Object $type = getType();
        int result4 = (result3 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $postfix = getPostfix();
        int result5 = (result4 * 59) + ($postfix == null ? 43 : $postfix.hashCode());
        Object $options = getOptions();
        return (result5 * 59) + ($options != null ? $options.hashCode() : 43);
    }

    public void setEnumData(CommandEnumData enumData) {
        this.enumData = enumData;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    public void setType(CommandParam type) {
        this.type = type;
    }

    public String toString() {
        return "CommandParamData(name=" + getName() + ", optional=" + isOptional() + ", enumData=" + getEnumData() + ", type=" + getType() + ", postfix=" + getPostfix() + ", options=" + getOptions() + ")";
    }

    public String getName() {
        return this.name;
    }

    public boolean isOptional() {
        return this.optional;
    }

    public CommandEnumData getEnumData() {
        return this.enumData;
    }

    public CommandParam getType() {
        return this.type;
    }

    public String getPostfix() {
        return this.postfix;
    }

    public Set<CommandParamOption> getOptions() {
        return this.options;
    }
}
