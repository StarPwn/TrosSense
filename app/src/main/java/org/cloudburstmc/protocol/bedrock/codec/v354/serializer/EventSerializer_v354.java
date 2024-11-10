package org.cloudburstmc.protocol.bedrock.codec.v354.serializer;

import io.netty.buffer.ByteBuf;
import java.util.EnumMap;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v340.serializer.EventSerializer_v340;
import org.cloudburstmc.protocol.bedrock.data.BlockInteractionType;
import org.cloudburstmc.protocol.bedrock.data.event.BellUsedEventData;
import org.cloudburstmc.protocol.bedrock.data.event.CauldronInteractEventData;
import org.cloudburstmc.protocol.bedrock.data.event.ComposterInteractEventData;
import org.cloudburstmc.protocol.bedrock.data.event.EventData;
import org.cloudburstmc.protocol.bedrock.data.event.EventDataType;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class EventSerializer_v354 extends EventSerializer_v340 {
    public static final EventSerializer_v354 INSTANCE = new EventSerializer_v354();

    /* JADX INFO: Access modifiers changed from: protected */
    public EventSerializer_v354() {
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.CAULDRON_INTERACT, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v354.serializer.EventSerializer_v354$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v354.this.readCauldronInteract((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.COMPOSTER_INTERACT, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v354.serializer.EventSerializer_v354$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v354.this.readComposterInteract((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.BELL_USED, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v354.serializer.EventSerializer_v354$$ExternalSyntheticLambda2
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v354.this.readBellUsed((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.CAULDRON_INTERACT, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v354.serializer.EventSerializer_v354$$ExternalSyntheticLambda3
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v354.this.writeCauldronInteract((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.COMPOSTER_INTERACT, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v354.serializer.EventSerializer_v354$$ExternalSyntheticLambda4
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v354.this.writeComposterInteract((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.BELL_USED, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v354.serializer.EventSerializer_v354$$ExternalSyntheticLambda5
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v354.this.writeBellUsed((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CauldronInteractEventData readCauldronInteract(ByteBuf buffer, BedrockCodecHelper helper) {
        BlockInteractionType type = BlockInteractionType.values()[VarInts.readInt(buffer)];
        int itemId = VarInts.readInt(buffer);
        return new CauldronInteractEventData(type, itemId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeCauldronInteract(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        CauldronInteractEventData event = (CauldronInteractEventData) eventData;
        VarInts.writeInt(buffer, event.getBlockInteractionType().ordinal());
        VarInts.writeInt(buffer, event.getItemId());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ComposterInteractEventData readComposterInteract(ByteBuf buffer, BedrockCodecHelper helper) {
        BlockInteractionType type = BlockInteractionType.values()[VarInts.readInt(buffer)];
        int itemId = VarInts.readInt(buffer);
        return new ComposterInteractEventData(type, itemId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeComposterInteract(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        ComposterInteractEventData event = (ComposterInteractEventData) eventData;
        VarInts.writeInt(buffer, event.getBlockInteractionType().ordinal());
        VarInts.writeInt(buffer, event.getItemId());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BellUsedEventData readBellUsed(ByteBuf buffer, BedrockCodecHelper helper) {
        int itemId = VarInts.readInt(buffer);
        return new BellUsedEventData(itemId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeBellUsed(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        BellUsedEventData event = (BellUsedEventData) eventData;
        VarInts.writeInt(buffer, event.getItemId());
    }
}
