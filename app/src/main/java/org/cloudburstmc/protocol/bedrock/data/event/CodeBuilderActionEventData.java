package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public class CodeBuilderActionEventData implements EventData {
    private final String action;

    public CodeBuilderActionEventData(String action) {
        this.action = action;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CodeBuilderActionEventData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CodeBuilderActionEventData)) {
            return false;
        }
        CodeBuilderActionEventData other = (CodeBuilderActionEventData) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$action = getAction();
        Object other$action = other.getAction();
        return this$action != null ? this$action.equals(other$action) : other$action == null;
    }

    public int hashCode() {
        Object $action = getAction();
        int result = (1 * 59) + ($action == null ? 43 : $action.hashCode());
        return result;
    }

    public String toString() {
        return "CodeBuilderActionEventData(action=" + getAction() + ")";
    }

    public String getAction() {
        return this.action;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.CODE_BUILDER_ACTION;
    }
}
