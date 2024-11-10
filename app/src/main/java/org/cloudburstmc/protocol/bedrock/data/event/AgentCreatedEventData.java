package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public class AgentCreatedEventData implements EventData {
    public static final AgentCreatedEventData INSTANCE = new AgentCreatedEventData();

    private AgentCreatedEventData() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.AGENT_CREATED;
    }
}
