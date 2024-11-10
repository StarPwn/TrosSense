package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public class StriderRiddenInLavaInOverworldEventData implements EventData {
    public static final StriderRiddenInLavaInOverworldEventData INSTANCE = new StriderRiddenInLavaInOverworldEventData();

    private StriderRiddenInLavaInOverworldEventData() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.STRIDER_RIDDEN_IN_LAVA_IN_OVERWORLD;
    }
}
