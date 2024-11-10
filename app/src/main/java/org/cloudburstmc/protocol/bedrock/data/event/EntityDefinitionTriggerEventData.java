package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public final class EntityDefinitionTriggerEventData implements EventData {
    private final String eventName;

    public EntityDefinitionTriggerEventData(String eventName) {
        this.eventName = eventName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EntityDefinitionTriggerEventData)) {
            return false;
        }
        EntityDefinitionTriggerEventData other = (EntityDefinitionTriggerEventData) o;
        Object this$eventName = getEventName();
        Object other$eventName = other.getEventName();
        return this$eventName != null ? this$eventName.equals(other$eventName) : other$eventName == null;
    }

    public int hashCode() {
        Object $eventName = getEventName();
        int result = (1 * 59) + ($eventName == null ? 43 : $eventName.hashCode());
        return result;
    }

    public String toString() {
        return "EntityDefinitionTriggerEventData(eventName=" + getEventName() + ")";
    }

    public String getEventName() {
        return this.eventName;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.ENTITY_DEFINITION_TRIGGER;
    }
}
