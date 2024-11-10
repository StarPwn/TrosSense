package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class AgentCommandEventData implements EventData {
    private final String command;
    private final String dataKey;
    private final int dataValue;
    private final String output;
    private final AgentResult result;

    public AgentCommandEventData(AgentResult result, String command, String dataKey, int dataValue, String output) {
        this.result = result;
        this.command = command;
        this.dataKey = dataKey;
        this.dataValue = dataValue;
        this.output = output;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AgentCommandEventData)) {
            return false;
        }
        AgentCommandEventData other = (AgentCommandEventData) o;
        if (getDataValue() != other.getDataValue()) {
            return false;
        }
        Object this$result = getResult();
        Object other$result = other.getResult();
        if (this$result != null ? !this$result.equals(other$result) : other$result != null) {
            return false;
        }
        Object this$command = getCommand();
        Object other$command = other.getCommand();
        if (this$command != null ? !this$command.equals(other$command) : other$command != null) {
            return false;
        }
        Object this$dataKey = getDataKey();
        Object other$dataKey = other.getDataKey();
        if (this$dataKey != null ? !this$dataKey.equals(other$dataKey) : other$dataKey != null) {
            return false;
        }
        Object this$output = getOutput();
        Object other$output = other.getOutput();
        return this$output != null ? this$output.equals(other$output) : other$output == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getDataValue();
        Object $result = getResult();
        int result2 = (result * 59) + ($result == null ? 43 : $result.hashCode());
        Object $command = getCommand();
        int result3 = (result2 * 59) + ($command == null ? 43 : $command.hashCode());
        Object $dataKey = getDataKey();
        int result4 = (result3 * 59) + ($dataKey == null ? 43 : $dataKey.hashCode());
        Object $output = getOutput();
        return (result4 * 59) + ($output != null ? $output.hashCode() : 43);
    }

    public String toString() {
        return "AgentCommandEventData(result=" + getResult() + ", command=" + getCommand() + ", dataKey=" + getDataKey() + ", dataValue=" + getDataValue() + ", output=" + getOutput() + ")";
    }

    public AgentResult getResult() {
        return this.result;
    }

    public String getCommand() {
        return this.command;
    }

    public String getDataKey() {
        return this.dataKey;
    }

    public int getDataValue() {
        return this.dataValue;
    }

    public String getOutput() {
        return this.output;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.AGENT_COMMAND;
    }
}
