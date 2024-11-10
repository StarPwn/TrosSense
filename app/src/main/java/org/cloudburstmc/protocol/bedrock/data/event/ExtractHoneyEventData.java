package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public class ExtractHoneyEventData implements EventData {
    public static final ExtractHoneyEventData INSTANCE = new ExtractHoneyEventData();

    private ExtractHoneyEventData() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.EXTRACT_HONEY;
    }
}
