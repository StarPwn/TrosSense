package org.cloudburstmc.protocol.bedrock.codec.v589.serializer;

import io.netty.buffer.ByteBuf;
import java.util.EnumMap;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v471.serializer.EventSerializer_v471;
import org.cloudburstmc.protocol.bedrock.data.event.CarefulRestorationEventData;
import org.cloudburstmc.protocol.bedrock.data.event.EventData;
import org.cloudburstmc.protocol.bedrock.data.event.EventDataType;
import org.cloudburstmc.protocol.common.util.TriConsumer;

/* loaded from: classes5.dex */
public class EventSerializer_v589 extends EventSerializer_v471 {
    public static final EventSerializer_v589 INSTANCE = new EventSerializer_v589();

    public EventSerializer_v589() {
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.CAREFUL_RESTORATION, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v589.serializer.EventSerializer_v589$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                EventData eventData;
                eventData = CarefulRestorationEventData.INSTANCE;
                return eventData;
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.CAREFUL_RESTORATION, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v589.serializer.EventSerializer_v589$$ExternalSyntheticLambda1
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v589.lambda$new$1((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$new$1(ByteBuf b, BedrockCodecHelper h, EventData e) {
    }
}
