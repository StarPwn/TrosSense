package org.cloudburstmc.protocol.bedrock.data.event;

/* loaded from: classes5.dex */
public class SneakCloseToSculkSensorEventData implements EventData {
    public static final SneakCloseToSculkSensorEventData INSTANCE = new SneakCloseToSculkSensorEventData();

    private SneakCloseToSculkSensorEventData() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.SNEAK_CLOSE_TO_SCULK_SENSOR;
    }
}
