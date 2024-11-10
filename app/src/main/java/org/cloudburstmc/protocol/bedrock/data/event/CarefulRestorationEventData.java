package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public class CarefulRestorationEventData implements EventData {
    public static final CarefulRestorationEventData INSTANCE = new CarefulRestorationEventData();

    private CarefulRestorationEventData() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.CAREFUL_RESTORATION;
    }
}
