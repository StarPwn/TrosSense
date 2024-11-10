package org.cloudburstmc.protocol.bedrock.data.command;

import java.util.Arrays;

/* loaded from: classes5.dex */
public final class CommandOutputMessage {
    private final boolean internal;
    private final String messageId;
    private final String[] parameters;

    public CommandOutputMessage(boolean internal, String messageId, String[] parameters) {
        if (messageId == null) {
            throw new NullPointerException("messageId is marked non-null but is null");
        }
        if (parameters == null) {
            throw new NullPointerException("parameters is marked non-null but is null");
        }
        this.internal = internal;
        this.messageId = messageId;
        this.parameters = parameters;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommandOutputMessage)) {
            return false;
        }
        CommandOutputMessage other = (CommandOutputMessage) o;
        if (isInternal() != other.isInternal()) {
            return false;
        }
        Object this$messageId = getMessageId();
        Object other$messageId = other.getMessageId();
        if (this$messageId != null ? this$messageId.equals(other$messageId) : other$messageId == null) {
            return Arrays.deepEquals(getParameters(), other.getParameters());
        }
        return false;
    }

    public int hashCode() {
        int result = (1 * 59) + (isInternal() ? 79 : 97);
        Object $messageId = getMessageId();
        return (((result * 59) + ($messageId == null ? 43 : $messageId.hashCode())) * 59) + Arrays.deepHashCode(getParameters());
    }

    public String toString() {
        return "CommandOutputMessage(internal=" + isInternal() + ", messageId=" + getMessageId() + ", parameters=" + Arrays.deepToString(getParameters()) + ")";
    }

    public boolean isInternal() {
        return this.internal;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public String[] getParameters() {
        return this.parameters;
    }
}
