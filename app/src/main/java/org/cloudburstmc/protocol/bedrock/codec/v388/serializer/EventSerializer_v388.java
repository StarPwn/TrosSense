package org.cloudburstmc.protocol.bedrock.codec.v388.serializer;

import io.netty.buffer.ByteBuf;
import java.util.EnumMap;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v354.serializer.EventSerializer_v354;
import org.cloudburstmc.protocol.bedrock.data.event.EntityDefinitionTriggerEventData;
import org.cloudburstmc.protocol.bedrock.data.event.EventData;
import org.cloudburstmc.protocol.bedrock.data.event.EventDataType;
import org.cloudburstmc.protocol.bedrock.data.event.MobKilledEventData;
import org.cloudburstmc.protocol.bedrock.data.event.MovementAnomalyEventData;
import org.cloudburstmc.protocol.bedrock.data.event.MovementCorrectedEventData;
import org.cloudburstmc.protocol.bedrock.data.event.RaidUpdateEventData;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class EventSerializer_v388 extends EventSerializer_v354 {
    public static final EventSerializer_v388 INSTANCE = new EventSerializer_v388();

    /* JADX INFO: Access modifiers changed from: protected */
    public EventSerializer_v388() {
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.ENTITY_DEFINITION_TRIGGER, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.EventSerializer_v388$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v388.this.readEntityDefinitionTrigger((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.RAID_UPDATE, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.EventSerializer_v388$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v388.this.readRaidUpdate((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.MOVEMENT_ANOMALY, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.EventSerializer_v388$$ExternalSyntheticLambda2
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v388.this.readMovementAnomaly((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.MOVEMENT_CORRECTED, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.EventSerializer_v388$$ExternalSyntheticLambda3
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v388.this.readMovementCorrected((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.ENTITY_DEFINITION_TRIGGER, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.EventSerializer_v388$$ExternalSyntheticLambda4
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v388.this.writeEntityDefinitionTrigger((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.RAID_UPDATE, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.EventSerializer_v388$$ExternalSyntheticLambda5
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v388.this.writeRaidUpdate((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.MOVEMENT_ANOMALY, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.EventSerializer_v388$$ExternalSyntheticLambda6
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v388.this.writeMovementAnomaly((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.MOVEMENT_CORRECTED, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.EventSerializer_v388$$ExternalSyntheticLambda7
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v388.this.writeMovementCorrected((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291
    public MobKilledEventData readMobKilled(ByteBuf buffer, BedrockCodecHelper helper) {
        long killerUniqueEntityId = VarInts.readLong(buffer);
        long victimUniqueEntityId = VarInts.readLong(buffer);
        int killerEntityType = VarInts.readInt(buffer);
        int entityDamageCause = VarInts.readInt(buffer);
        int villagerTradeTier = VarInts.readInt(buffer);
        String villagerDisplayName = helper.readString(buffer);
        return new MobKilledEventData(killerUniqueEntityId, victimUniqueEntityId, killerEntityType, entityDamageCause, villagerTradeTier, villagerDisplayName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291
    public void writeMobKilled(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        MobKilledEventData event = (MobKilledEventData) eventData;
        VarInts.writeLong(buffer, event.getKillerUniqueEntityId());
        VarInts.writeLong(buffer, event.getVictimUniqueEntityId());
        VarInts.writeInt(buffer, event.getKillerEntityType());
        VarInts.writeInt(buffer, event.getEntityDamageCause());
        VarInts.writeInt(buffer, event.getVillagerTradeTier());
        helper.writeString(buffer, event.getVillagerDisplayName());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public EntityDefinitionTriggerEventData readEntityDefinitionTrigger(ByteBuf buffer, BedrockCodecHelper helper) {
        String eventName = helper.readString(buffer);
        return new EntityDefinitionTriggerEventData(eventName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeEntityDefinitionTrigger(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        EntityDefinitionTriggerEventData event = (EntityDefinitionTriggerEventData) eventData;
        helper.writeString(buffer, event.getEventName());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RaidUpdateEventData readRaidUpdate(ByteBuf buffer, BedrockCodecHelper helper) {
        int currentRaidWave = VarInts.readInt(buffer);
        int totalRaidWaves = VarInts.readInt(buffer);
        boolean wonRaid = buffer.readBoolean();
        return new RaidUpdateEventData(currentRaidWave, totalRaidWaves, wonRaid);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeRaidUpdate(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        RaidUpdateEventData event = (RaidUpdateEventData) eventData;
        VarInts.writeInt(buffer, event.getCurrentWave());
        VarInts.writeInt(buffer, event.getTotalWaves());
        buffer.writeBoolean(event.isWinner());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MovementAnomalyEventData readMovementAnomaly(ByteBuf buffer, BedrockCodecHelper helper) {
        byte eventType = buffer.readByte();
        float cheatingScore = buffer.readFloatLE();
        float averagePositionDelta = buffer.readFloatLE();
        float totalPositionDelta = buffer.readFloatLE();
        float minPositionDelta = buffer.readFloatLE();
        float maxPositionDelta = buffer.readFloatLE();
        return new MovementAnomalyEventData(eventType, cheatingScore, averagePositionDelta, totalPositionDelta, minPositionDelta, maxPositionDelta);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeMovementAnomaly(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        MovementAnomalyEventData event = (MovementAnomalyEventData) eventData;
        buffer.writeByte(event.getEventType());
        buffer.writeFloatLE(event.getCheatingScore());
        buffer.writeFloatLE(event.getAveragePositionDelta());
        buffer.writeFloatLE(event.getTotalPositionDelta());
        buffer.writeFloatLE(event.getMinPositionDelta());
        buffer.writeFloatLE(event.getMaxPositionDelta());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MovementCorrectedEventData readMovementCorrected(ByteBuf buffer, BedrockCodecHelper helper) {
        float positionDelta = buffer.readFloatLE();
        float cheatingScore = buffer.readFloatLE();
        float scoreThreshold = buffer.readFloatLE();
        float distanceThreshold = buffer.readFloatLE();
        int durationThreshold = VarInts.readInt(buffer);
        return new MovementCorrectedEventData(positionDelta, cheatingScore, scoreThreshold, distanceThreshold, durationThreshold);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeMovementCorrected(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        MovementCorrectedEventData event = (MovementCorrectedEventData) eventData;
        buffer.writeFloatLE(event.getPositionDelta());
        buffer.writeFloatLE(event.getCheatingScore());
        buffer.writeFloatLE(event.getScoreThreshold());
        buffer.writeFloatLE(event.getDistanceThreshold());
        VarInts.writeInt(buffer, event.getDurationThreshold());
    }
}
