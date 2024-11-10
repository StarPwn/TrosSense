package org.cloudburstmc.protocol.bedrock.data.event;

import java.util.List;

/* loaded from: classes5.dex */
public final class SlashCommandExecutedEventData implements EventData {
    private final String commandName;
    private final List<String> outputMessages;
    private final int successCount;

    public SlashCommandExecutedEventData(String commandName, int successCount, List<String> outputMessages) {
        this.commandName = commandName;
        this.successCount = successCount;
        this.outputMessages = outputMessages;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SlashCommandExecutedEventData)) {
            return false;
        }
        SlashCommandExecutedEventData other = (SlashCommandExecutedEventData) o;
        if (getSuccessCount() != other.getSuccessCount()) {
            return false;
        }
        Object this$commandName = getCommandName();
        Object other$commandName = other.getCommandName();
        if (this$commandName != null ? !this$commandName.equals(other$commandName) : other$commandName != null) {
            return false;
        }
        Object this$outputMessages = getOutputMessages();
        Object other$outputMessages = other.getOutputMessages();
        return this$outputMessages != null ? this$outputMessages.equals(other$outputMessages) : other$outputMessages == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getSuccessCount();
        Object $commandName = getCommandName();
        int result2 = (result * 59) + ($commandName == null ? 43 : $commandName.hashCode());
        Object $outputMessages = getOutputMessages();
        return (result2 * 59) + ($outputMessages != null ? $outputMessages.hashCode() : 43);
    }

    public String toString() {
        return "SlashCommandExecutedEventData(commandName=" + getCommandName() + ", successCount=" + getSuccessCount() + ", outputMessages=" + getOutputMessages() + ")";
    }

    public String getCommandName() {
        return this.commandName;
    }

    public int getSuccessCount() {
        return this.successCount;
    }

    public List<String> getOutputMessages() {
        return this.outputMessages;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.SLASH_COMMAND_EXECUTED;
    }
}
