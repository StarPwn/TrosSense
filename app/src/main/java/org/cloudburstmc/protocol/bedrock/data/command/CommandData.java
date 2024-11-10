package org.cloudburstmc.protocol.bedrock.data.command;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/* loaded from: classes5.dex */
public final class CommandData {
    private final CommandEnumData aliases;
    private final String description;
    private final Set<Flag> flags;
    private final String name;
    private final CommandOverloadData[] overloads;
    private final CommandPermission permission;
    private final List<ChainedSubCommandData> subcommands;

    /* loaded from: classes5.dex */
    public enum Flag {
        TEST_USAGE,
        HIDDEN_FROM_COMMAND_BLOCK,
        HIDDEN_FROM_PLAYER,
        HIDDEN_FROM_AUTOMATION,
        LOCAL_SYNC,
        EXECUTE_DISALLOWED,
        MESSAGE_TYPE,
        NOT_CHEAT,
        ASYNC
    }

    public CommandData(String name, String description, Set<Flag> flags, CommandPermission permission, CommandEnumData aliases, List<ChainedSubCommandData> subcommands, CommandOverloadData[] overloads) {
        this.name = name;
        this.description = description;
        this.flags = flags;
        this.permission = permission;
        this.aliases = aliases;
        this.subcommands = subcommands;
        this.overloads = overloads;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommandData)) {
            return false;
        }
        CommandData other = (CommandData) o;
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name != null ? !this$name.equals(other$name) : other$name != null) {
            return false;
        }
        Object this$description = getDescription();
        Object other$description = other.getDescription();
        if (this$description != null ? !this$description.equals(other$description) : other$description != null) {
            return false;
        }
        Object this$flags = getFlags();
        Object other$flags = other.getFlags();
        if (this$flags != null ? !this$flags.equals(other$flags) : other$flags != null) {
            return false;
        }
        Object this$permission = getPermission();
        Object other$permission = other.getPermission();
        if (this$permission != null ? !this$permission.equals(other$permission) : other$permission != null) {
            return false;
        }
        Object this$aliases = getAliases();
        Object other$aliases = other.getAliases();
        if (this$aliases != null ? !this$aliases.equals(other$aliases) : other$aliases != null) {
            return false;
        }
        Object this$subcommands = getSubcommands();
        Object other$subcommands = other.getSubcommands();
        if (this$subcommands != null ? this$subcommands.equals(other$subcommands) : other$subcommands == null) {
            return Arrays.deepEquals(getOverloads(), other.getOverloads());
        }
        return false;
    }

    public int hashCode() {
        Object $name = getName();
        int result = (1 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $description = getDescription();
        int result2 = (result * 59) + ($description == null ? 43 : $description.hashCode());
        Object $flags = getFlags();
        int result3 = (result2 * 59) + ($flags == null ? 43 : $flags.hashCode());
        Object $permission = getPermission();
        int result4 = (result3 * 59) + ($permission == null ? 43 : $permission.hashCode());
        Object $aliases = getAliases();
        int result5 = (result4 * 59) + ($aliases == null ? 43 : $aliases.hashCode());
        Object $subcommands = getSubcommands();
        return (((result5 * 59) + ($subcommands != null ? $subcommands.hashCode() : 43)) * 59) + Arrays.deepHashCode(getOverloads());
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Set<Flag> getFlags() {
        return this.flags;
    }

    public CommandPermission getPermission() {
        return this.permission;
    }

    public CommandEnumData getAliases() {
        return this.aliases;
    }

    public List<ChainedSubCommandData> getSubcommands() {
        return this.subcommands;
    }

    public CommandOverloadData[] getOverloads() {
        return this.overloads;
    }

    public String toString() {
        StringBuilder overloads = new StringBuilder("[\r\n");
        for (CommandOverloadData overload : this.overloads) {
            overloads.append("    [\r\n");
            overloads.append("       chaining=").append(overload.isChaining()).append("\r\n");
            for (CommandParamData parameter : overload.getOverloads()) {
                overloads.append("       ").append(parameter).append("\r\n");
            }
            overloads.append("    ]\r\n");
        }
        overloads.append("]\r\n");
        StringBuilder builder = new StringBuilder("CommandData(\r\n");
        List<?> objects = Arrays.asList("name=" + this.name, "description=" + this.description, "flags=" + Arrays.toString(this.flags.toArray()), "permission=" + this.permission, "aliases=" + this.aliases, "subcommands=" + Arrays.toString(this.subcommands.toArray()), "overloads=" + ((Object) overloads));
        for (Object object : objects) {
            builder.append("    ").append(Objects.toString(object).replaceAll("\r\n", "\r\n    ")).append("\r\n");
        }
        return builder.append(")").toString();
    }
}
