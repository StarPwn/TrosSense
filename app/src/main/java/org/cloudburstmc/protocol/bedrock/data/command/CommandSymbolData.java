package org.cloudburstmc.protocol.bedrock.data.command;

import org.cloudburstmc.protocol.common.util.Preconditions;

/* loaded from: classes5.dex */
public final class CommandSymbolData {
    private static final int ARG_FLAG_ENUM = 2097152;
    private static final int ARG_FLAG_POSTFIX = 16777216;
    private static final int ARG_FLAG_SOFT_ENUM = 67108864;
    private static final int ARG_FLAG_VALID = 1048576;
    private final boolean commandEnum;
    private final boolean postfix;
    private final boolean softEnum;
    private final int value;

    public CommandSymbolData(int value, boolean commandEnum, boolean softEnum, boolean postfix) {
        this.value = value;
        this.commandEnum = commandEnum;
        this.softEnum = softEnum;
        this.postfix = postfix;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommandSymbolData)) {
            return false;
        }
        CommandSymbolData other = (CommandSymbolData) o;
        return getValue() == other.getValue() && isCommandEnum() == other.isCommandEnum() && isSoftEnum() == other.isSoftEnum() && isPostfix() == other.isPostfix();
    }

    public int hashCode() {
        int result = (1 * 59) + getValue();
        return (((((result * 59) + (isCommandEnum() ? 79 : 97)) * 59) + (isSoftEnum() ? 79 : 97)) * 59) + (isPostfix() ? 79 : 97);
    }

    public String toString() {
        return "CommandSymbolData(value=" + getValue() + ", commandEnum=" + isCommandEnum() + ", softEnum=" + isSoftEnum() + ", postfix=" + isPostfix() + ")";
    }

    public int getValue() {
        return this.value;
    }

    public boolean isCommandEnum() {
        return this.commandEnum;
    }

    public boolean isSoftEnum() {
        return this.softEnum;
    }

    public boolean isPostfix() {
        return this.postfix;
    }

    public static CommandSymbolData deserialize(int type) {
        int value = 65535 & type;
        boolean z = true;
        boolean commandEnum = (2097152 & type) != 0;
        boolean softEnum = (67108864 & type) != 0;
        boolean postfix = (16777216 & type) != 0;
        if (!postfix && (1048576 & type) == 0) {
            z = false;
        }
        Preconditions.checkArgument(z, "Invalid command param type: " + type);
        return new CommandSymbolData(value, commandEnum, softEnum, postfix);
    }

    public int serialize() {
        int value = this.value;
        if (this.commandEnum) {
            value |= 2097152;
        }
        if (this.softEnum) {
            value |= 67108864;
        }
        if (this.postfix) {
            return value | 16777216;
        }
        return value | 1048576;
    }
}
