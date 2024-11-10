package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.event.AchievementAwardedEventData;
import org.cloudburstmc.protocol.bedrock.data.event.AgentCommandEventData;
import org.cloudburstmc.protocol.bedrock.data.event.AgentCreatedEventData;
import org.cloudburstmc.protocol.bedrock.data.event.AgentResult;
import org.cloudburstmc.protocol.bedrock.data.event.BossKilledEventData;
import org.cloudburstmc.protocol.bedrock.data.event.CauldronUsedEventData;
import org.cloudburstmc.protocol.bedrock.data.event.EntityInteractEventData;
import org.cloudburstmc.protocol.bedrock.data.event.EventData;
import org.cloudburstmc.protocol.bedrock.data.event.EventDataType;
import org.cloudburstmc.protocol.bedrock.data.event.FishBucketedEventData;
import org.cloudburstmc.protocol.bedrock.data.event.MobKilledEventData;
import org.cloudburstmc.protocol.bedrock.data.event.PatternRemovedEventData;
import org.cloudburstmc.protocol.bedrock.data.event.PlayerDiedEventData;
import org.cloudburstmc.protocol.bedrock.data.event.PortalBuiltEventData;
import org.cloudburstmc.protocol.bedrock.data.event.PortalUsedEventData;
import org.cloudburstmc.protocol.bedrock.data.event.SlashCommandExecutedEventData;
import org.cloudburstmc.protocol.bedrock.packet.EventPacket;
import org.cloudburstmc.protocol.common.util.Preconditions;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class EventSerializer_v291 implements BedrockPacketSerializer<EventPacket> {
    public static final EventSerializer_v291 INSTANCE = new EventSerializer_v291();
    protected static final EventDataType[] VALUES = EventDataType.values();
    protected final EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>> readers = new EnumMap<>(EventDataType.class);
    protected final EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>> writers = new EnumMap<>(EventDataType.class);

    /* JADX INFO: Access modifiers changed from: protected */
    public EventSerializer_v291() {
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.ACHIEVEMENT_AWARDED, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda11
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v291.this.readAchievementAwarded((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.ENTITY_INTERACT, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda3
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v291.this.readEntityInteract((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.PORTAL_BUILT, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda10
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v291.this.readPortalBuilt((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.PORTAL_USED, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda12
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v291.this.readPortalUsed((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.MOB_KILLED, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda13
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v291.this.readMobKilled((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.CAULDRON_USED, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda14
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v291.this.readCauldronUsed((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.PLAYER_DIED, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda15
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v291.this.readPlayerDied((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.BOSS_KILLED, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda16
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v291.this.readBossKilled((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.AGENT_COMMAND, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda17
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v291.this.readAgentCommand((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.AGENT_CREATED, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda18
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                EventData eventData;
                eventData = AgentCreatedEventData.INSTANCE;
                return eventData;
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.PATTERN_REMOVED, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda19
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v291.this.readPatternRemoved((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.SLASH_COMMAND_EXECUTED, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda20
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v291.this.readSlashCommandExecuted((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.FISH_BUCKETED, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda21
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v291.this.readFishBucketed((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.ACHIEVEMENT_AWARDED, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda22
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v291.this.writeAchievementAwarded((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.ENTITY_INTERACT, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda23
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v291.this.writeEntityInteract((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.PORTAL_BUILT, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda24
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v291.this.writePortalBuilt((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.PORTAL_USED, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda25
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v291.this.writePortalUsed((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.MOB_KILLED, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda26
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v291.this.writeMobKilled((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.CAULDRON_USED, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda1
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v291.this.writeCauldronUsed((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.PLAYER_DIED, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda2
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v291.this.writePlayerDied((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.BOSS_KILLED, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda4
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v291.this.writeBossKilled((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.AGENT_COMMAND, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda5
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v291.this.writeAgentCommand((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.AGENT_CREATED, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda6
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v291.lambda$new$1((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.PATTERN_REMOVED, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda7
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v291.this.writePatternRemoved((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.SLASH_COMMAND_EXECUTED, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda8
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v291.this.writeSlashCommandExecuted((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.FISH_BUCKETED, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291$$ExternalSyntheticLambda9
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v291.this.writeFishBucketed((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$new$1(ByteBuf buf, BedrockCodecHelper helper, EventData data) {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, EventPacket packet) {
        VarInts.writeLong(buffer, packet.getUniqueEntityId());
        EventData eventData = packet.getEventData();
        VarInts.writeInt(buffer, eventData.getType().ordinal());
        buffer.writeByte(packet.getUsePlayerId());
        TriConsumer<ByteBuf, BedrockCodecHelper, EventData> function = this.writers.get(eventData.getType());
        if (function == null) {
            throw new UnsupportedOperationException("Unknown event type " + eventData.getType());
        }
        function.accept(buffer, helper, eventData);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, EventPacket packet) {
        packet.setUniqueEntityId(VarInts.readLong(buffer));
        int eventId = VarInts.readInt(buffer);
        Preconditions.checkElementIndex(eventId, VALUES.length, "EventDataType");
        EventDataType type = VALUES[eventId];
        packet.setUsePlayerId(buffer.readByte());
        BiFunction<ByteBuf, BedrockCodecHelper, EventData> function = this.readers.get(type);
        if (function == null) {
            throw new UnsupportedOperationException("Unknown event type " + type);
        }
        packet.setEventData(function.apply(buffer, helper));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AchievementAwardedEventData readAchievementAwarded(ByteBuf buffer, BedrockCodecHelper helper) {
        int achievementId = VarInts.readInt(buffer);
        return new AchievementAwardedEventData(achievementId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeAchievementAwarded(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        AchievementAwardedEventData event = (AchievementAwardedEventData) eventData;
        VarInts.writeInt(buffer, event.getAchievementId());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public EntityInteractEventData readEntityInteract(ByteBuf buffer, BedrockCodecHelper helper) {
        int interactionType = VarInts.readInt(buffer);
        int interactionEntityType = VarInts.readInt(buffer);
        int entityVariant = VarInts.readInt(buffer);
        int entityColor = buffer.readUnsignedByte();
        return new EntityInteractEventData(interactionType, interactionEntityType, entityVariant, entityColor);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeEntityInteract(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        EntityInteractEventData event = (EntityInteractEventData) eventData;
        VarInts.writeInt(buffer, event.getInteractionType());
        VarInts.writeInt(buffer, event.getLegacyEntityTypeId());
        VarInts.writeInt(buffer, event.getVariant());
        buffer.writeByte(event.getPaletteColor());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PortalBuiltEventData readPortalBuilt(ByteBuf buffer, BedrockCodecHelper helper) {
        int dimensionId = VarInts.readInt(buffer);
        return new PortalBuiltEventData(dimensionId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writePortalBuilt(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        PortalBuiltEventData event = (PortalBuiltEventData) eventData;
        VarInts.writeInt(buffer, event.getDimensionId());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PortalUsedEventData readPortalUsed(ByteBuf buffer, BedrockCodecHelper helper) {
        int fromDimensionId = VarInts.readInt(buffer);
        int toDimensionId = VarInts.readInt(buffer);
        return new PortalUsedEventData(fromDimensionId, toDimensionId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writePortalUsed(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        PortalUsedEventData event = (PortalUsedEventData) eventData;
        VarInts.writeInt(buffer, event.getFromDimensionId());
        VarInts.writeInt(buffer, event.getToDimensionId());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MobKilledEventData readMobKilled(ByteBuf buffer, BedrockCodecHelper helper) {
        long killerUniqueEntityId = VarInts.readLong(buffer);
        long victimUniqueEntityId = VarInts.readLong(buffer);
        int entityDamageCause = VarInts.readInt(buffer);
        int villagerTradeTier = VarInts.readInt(buffer);
        String villagerDisplayName = helper.readString(buffer);
        return new MobKilledEventData(killerUniqueEntityId, victimUniqueEntityId, -1, entityDamageCause, villagerTradeTier, villagerDisplayName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeMobKilled(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        MobKilledEventData event = (MobKilledEventData) eventData;
        VarInts.writeLong(buffer, event.getKillerUniqueEntityId());
        VarInts.writeLong(buffer, event.getVictimUniqueEntityId());
        VarInts.writeInt(buffer, event.getEntityDamageCause());
        VarInts.writeInt(buffer, event.getVillagerTradeTier());
        helper.writeString(buffer, event.getVillagerDisplayName());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CauldronUsedEventData readCauldronUsed(ByteBuf buffer, BedrockCodecHelper helper) {
        int potionId = VarInts.readInt(buffer);
        int color = VarInts.readInt(buffer);
        int fillLevel = VarInts.readInt(buffer);
        return new CauldronUsedEventData(potionId, color, fillLevel);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeCauldronUsed(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        CauldronUsedEventData event = (CauldronUsedEventData) eventData;
        VarInts.writeUnsignedInt(buffer, event.getPotionId());
        VarInts.writeInt(buffer, event.getColor());
        VarInts.writeInt(buffer, event.getFillLevel());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PlayerDiedEventData readPlayerDied(ByteBuf buffer, BedrockCodecHelper helper) {
        int attackerEntityId = VarInts.readInt(buffer);
        int entityDamageCause = VarInts.readInt(buffer);
        return new PlayerDiedEventData(attackerEntityId, -1, entityDamageCause, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writePlayerDied(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        PlayerDiedEventData event = (PlayerDiedEventData) eventData;
        VarInts.writeInt(buffer, event.getAttackerEntityId());
        VarInts.writeInt(buffer, event.getEntityDamageCause());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BossKilledEventData readBossKilled(ByteBuf buffer, BedrockCodecHelper helper) {
        long bossUniqueEntityId = VarInts.readLong(buffer);
        int playerPartySize = VarInts.readInt(buffer);
        int interactionEntityType = VarInts.readInt(buffer);
        return new BossKilledEventData(bossUniqueEntityId, playerPartySize, interactionEntityType);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeBossKilled(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        BossKilledEventData event = (BossKilledEventData) eventData;
        VarInts.writeLong(buffer, event.getBossUniqueEntityId());
        VarInts.writeInt(buffer, event.getPlayerPartySize());
        VarInts.writeInt(buffer, event.getBossEntityType());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AgentCommandEventData readAgentCommand(ByteBuf buffer, BedrockCodecHelper helper) {
        AgentResult result = AgentResult.values()[VarInts.readInt(buffer)];
        int dataValue = VarInts.readInt(buffer);
        String command = helper.readString(buffer);
        String dataKey = helper.readString(buffer);
        String output = helper.readString(buffer);
        return new AgentCommandEventData(result, command, dataKey, dataValue, output);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeAgentCommand(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        AgentCommandEventData event = (AgentCommandEventData) eventData;
        VarInts.writeInt(buffer, event.getResult().ordinal());
        VarInts.writeInt(buffer, event.getDataValue());
        helper.writeString(buffer, event.getCommand());
        helper.writeString(buffer, event.getDataKey());
        helper.writeString(buffer, event.getOutput());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PatternRemovedEventData readPatternRemoved(ByteBuf buffer, BedrockCodecHelper helper) {
        int itemId = VarInts.readInt(buffer);
        int auxValue = VarInts.readInt(buffer);
        int patternsSize = VarInts.readInt(buffer);
        int patternIndex = VarInts.readInt(buffer);
        int patternColor = VarInts.readInt(buffer);
        return new PatternRemovedEventData(itemId, auxValue, patternsSize, patternIndex, patternColor);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writePatternRemoved(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        PatternRemovedEventData event = (PatternRemovedEventData) eventData;
        VarInts.writeInt(buffer, event.getItemId());
        VarInts.writeInt(buffer, event.getAuxValue());
        VarInts.writeInt(buffer, event.getPatternsSize());
        VarInts.writeInt(buffer, event.getPatternIndex());
        VarInts.writeInt(buffer, event.getPatternColor());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SlashCommandExecutedEventData readSlashCommandExecuted(ByteBuf buffer, BedrockCodecHelper helper) {
        int successCount = VarInts.readInt(buffer);
        VarInts.readInt(buffer);
        String commandName = helper.readString(buffer);
        List<String> outputMessages = Arrays.asList(helper.readString(buffer).split(";"));
        return new SlashCommandExecutedEventData(commandName, successCount, outputMessages);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeSlashCommandExecuted(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        SlashCommandExecutedEventData event = (SlashCommandExecutedEventData) eventData;
        VarInts.writeInt(buffer, event.getSuccessCount());
        List<String> outputMessages = event.getOutputMessages();
        VarInts.writeInt(buffer, outputMessages.size());
        helper.writeString(buffer, event.getCommandName());
        helper.writeString(buffer, EventSerializer_v291$$ExternalSyntheticBackport0.m(";", outputMessages));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FishBucketedEventData readFishBucketed(ByteBuf buffer, BedrockCodecHelper helper) {
        int pattern = VarInts.readInt(buffer);
        int preset = VarInts.readInt(buffer);
        int bucketedEntityType = VarInts.readInt(buffer);
        boolean isRelease = buffer.readBoolean();
        return new FishBucketedEventData(pattern, preset, bucketedEntityType, isRelease);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeFishBucketed(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        FishBucketedEventData event = (FishBucketedEventData) eventData;
        VarInts.writeInt(buffer, event.getPattern());
        VarInts.writeInt(buffer, event.getPreset());
        VarInts.writeInt(buffer, event.getBucketedEntityType());
        buffer.writeBoolean(event.isReleaseEvent());
    }
}
