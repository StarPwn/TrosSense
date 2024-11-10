package org.cloudburstmc.protocol.bedrock.data.command;

import java.util.Arrays;

/* loaded from: classes5.dex */
public class CommandOverloadData {
    private final boolean chaining;
    private final CommandParamData[] overloads;

    public CommandOverloadData(boolean chaining, CommandParamData[] overloads) {
        this.chaining = chaining;
        this.overloads = overloads;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CommandOverloadData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommandOverloadData)) {
            return false;
        }
        CommandOverloadData other = (CommandOverloadData) o;
        return other.canEqual(this) && isChaining() == other.isChaining() && Arrays.deepEquals(getOverloads(), other.getOverloads());
    }

    public int hashCode() {
        int result = (1 * 59) + (isChaining() ? 79 : 97);
        return (result * 59) + Arrays.deepHashCode(getOverloads());
    }

    public String toString() {
        return "CommandOverloadData(chaining=" + isChaining() + ", overloads=" + Arrays.deepToString(getOverloads()) + ")";
    }

    public boolean isChaining() {
        return this.chaining;
    }

    public CommandParamData[] getOverloads() {
        return this.overloads;
    }
}
