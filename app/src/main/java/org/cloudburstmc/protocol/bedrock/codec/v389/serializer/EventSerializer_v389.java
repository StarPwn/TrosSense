package org.cloudburstmc.protocol.bedrock.codec.v389.serializer;

import io.netty.buffer.ByteBuf;
import java.util.EnumMap;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.EventSerializer_v388;
import org.cloudburstmc.protocol.bedrock.data.event.EventData;
import org.cloudburstmc.protocol.bedrock.data.event.EventDataType;
import org.cloudburstmc.protocol.bedrock.data.event.ExtractHoneyEventData;
import org.cloudburstmc.protocol.bedrock.data.event.PlayerDiedEventData;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class EventSerializer_v389 extends EventSerializer_v388 {
    public static final EventSerializer_v389 INSTANCE = new EventSerializer_v389();

    /* JADX INFO: Access modifiers changed from: protected */
    public EventSerializer_v389() {
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.EXTRACT_HONEY, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v389.serializer.EventSerializer_v389$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                EventData eventData;
                eventData = ExtractHoneyEventData.INSTANCE;
                return eventData;
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.EXTRACT_HONEY, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v389.serializer.EventSerializer_v389$$ExternalSyntheticLambda1
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v389.lambda$new$1((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.PLAYER_DIED, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v389.serializer.EventSerializer_v389$$ExternalSyntheticLambda2
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v389.this.writePlayerDied((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.PLAYER_DIED, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v389.serializer.EventSerializer_v389$$ExternalSyntheticLambda3
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v389.this.readPlayerDied((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$new$1(ByteBuf b, BedrockCodecHelper h, EventData e) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291
    public PlayerDiedEventData readPlayerDied(ByteBuf buffer, BedrockCodecHelper helper) {
        int attackerEntityId = VarInts.readInt(buffer);
        int attackerVariant = VarInts.readInt(buffer);
        int entityDamageCause = VarInts.readInt(buffer);
        boolean inRaid = buffer.readBoolean();
        return new PlayerDiedEventData(attackerEntityId, attackerVariant, entityDamageCause, inRaid);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291
    public void writePlayerDied(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        PlayerDiedEventData event = (PlayerDiedEventData) eventData;
        VarInts.writeInt(buffer, event.getAttackerEntityId());
        VarInts.writeInt(buffer, event.getAttackerVariant());
        VarInts.writeInt(buffer, event.getEntityDamageCause());
        buffer.writeBoolean(event.isInRaid());
    }
}
