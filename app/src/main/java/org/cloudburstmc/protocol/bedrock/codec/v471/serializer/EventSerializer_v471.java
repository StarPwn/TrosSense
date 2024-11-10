package org.cloudburstmc.protocol.bedrock.codec.v471.serializer;

import io.netty.buffer.ByteBuf;
import java.util.EnumMap;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v389.serializer.EventSerializer_v389;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.event.CodeBuilderActionEventData;
import org.cloudburstmc.protocol.bedrock.data.event.CodeBuilderScoreboardEventData;
import org.cloudburstmc.protocol.bedrock.data.event.CopperWaxedOrUnwaxedEventData;
import org.cloudburstmc.protocol.bedrock.data.event.EventData;
import org.cloudburstmc.protocol.bedrock.data.event.EventDataType;
import org.cloudburstmc.protocol.bedrock.data.event.PiglinBarterEventData;
import org.cloudburstmc.protocol.bedrock.data.event.SneakCloseToSculkSensorEventData;
import org.cloudburstmc.protocol.bedrock.data.event.StriderRiddenInLavaInOverworldEventData;
import org.cloudburstmc.protocol.bedrock.data.event.TargetBlockHitEventData;
import org.cloudburstmc.protocol.common.util.DefinitionUtils;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class EventSerializer_v471 extends EventSerializer_v389 {
    public static final EventSerializer_v471 INSTANCE = new EventSerializer_v471();

    /* JADX INFO: Access modifiers changed from: protected */
    public EventSerializer_v471() {
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.TARGET_BLOCK_HIT, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v471.serializer.EventSerializer_v471$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v471.this.readBlockHit((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.TARGET_BLOCK_HIT, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v471.serializer.EventSerializer_v471$$ExternalSyntheticLambda5
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v471.this.writeBlockHit((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.PIGLIN_BARTER, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v471.serializer.EventSerializer_v471$$ExternalSyntheticLambda6
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v471.this.readPiglinBarter((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.PIGLIN_BARTER, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v471.serializer.EventSerializer_v471$$ExternalSyntheticLambda7
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v471.this.writePiglinBarter((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.COPPER_WAXED_OR_UNWAXED, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v471.serializer.EventSerializer_v471$$ExternalSyntheticLambda8
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v471.this.readCopperWaxedUnwaxed((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.COPPER_WAXED_OR_UNWAXED, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v471.serializer.EventSerializer_v471$$ExternalSyntheticLambda9
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v471.this.writeCopperWaxedUnwaxed((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.CODE_BUILDER_ACTION, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v471.serializer.EventSerializer_v471$$ExternalSyntheticLambda10
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v471.this.readCodeBuilderAction((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.CODE_BUILDER_ACTION, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v471.serializer.EventSerializer_v471$$ExternalSyntheticLambda11
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v471.this.writeCodeBuilderAction((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.STRIDER_RIDDEN_IN_LAVA_IN_OVERWORLD, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v471.serializer.EventSerializer_v471$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                EventData eventData;
                eventData = StriderRiddenInLavaInOverworldEventData.INSTANCE;
                return eventData;
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.STRIDER_RIDDEN_IN_LAVA_IN_OVERWORLD, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v471.serializer.EventSerializer_v471$$ExternalSyntheticLambda2
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v471.lambda$new$1((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.SNEAK_CLOSE_TO_SCULK_SENSOR, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v471.serializer.EventSerializer_v471$$ExternalSyntheticLambda3
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                EventData eventData;
                eventData = SneakCloseToSculkSensorEventData.INSTANCE;
                return eventData;
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.SNEAK_CLOSE_TO_SCULK_SENSOR, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v471.serializer.EventSerializer_v471$$ExternalSyntheticLambda4
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v471.lambda$new$3((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$new$1(ByteBuf b, BedrockCodecHelper h, EventData e) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$new$3(ByteBuf b, BedrockCodecHelper h, EventData e) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public TargetBlockHitEventData readBlockHit(ByteBuf buffer, BedrockCodecHelper helper) {
        int redstoneLevel = VarInts.readInt(buffer);
        return new TargetBlockHitEventData(redstoneLevel);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeBlockHit(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        TargetBlockHitEventData event = (TargetBlockHitEventData) eventData;
        VarInts.writeInt(buffer, event.getRedstoneLevel());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PiglinBarterEventData readPiglinBarter(ByteBuf buffer, BedrockCodecHelper helper) {
        int runtimeId = VarInts.readInt(buffer);
        ItemDefinition itemDefinition = helper.getItemDefinitions().getDefinition(runtimeId);
        boolean targetingPlayer = buffer.readBoolean();
        return new PiglinBarterEventData(itemDefinition, targetingPlayer);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writePiglinBarter(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        PiglinBarterEventData event = (PiglinBarterEventData) eventData;
        VarInts.writeInt(buffer, event.getDefinition().getRuntimeId());
        buffer.writeBoolean(event.isTargetingPlayer());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CopperWaxedOrUnwaxedEventData readCopperWaxedUnwaxed(ByteBuf buffer, BedrockCodecHelper helper) {
        int runtimeId = VarInts.readInt(buffer);
        BlockDefinition blockDefinition = helper.getBlockDefinitions().getDefinition(runtimeId);
        return new CopperWaxedOrUnwaxedEventData(blockDefinition);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeCopperWaxedUnwaxed(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        CopperWaxedOrUnwaxedEventData event = (CopperWaxedOrUnwaxedEventData) eventData;
        VarInts.writeInt(buffer, ((BlockDefinition) DefinitionUtils.checkDefinition(helper.getBlockDefinitions(), event.getDefinition())).getRuntimeId());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CodeBuilderActionEventData readCodeBuilderAction(ByteBuf buffer, BedrockCodecHelper helper) {
        String action = helper.readString(buffer);
        return new CodeBuilderActionEventData(action);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeCodeBuilderAction(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        CodeBuilderActionEventData event = (CodeBuilderActionEventData) eventData;
        helper.writeString(buffer, event.getAction());
    }

    protected CodeBuilderScoreboardEventData readCodeBuilderScoreboard(ByteBuf buffer, BedrockCodecHelper helper) {
        String objectiveName = helper.readString(buffer);
        int score = VarInts.readInt(buffer);
        return new CodeBuilderScoreboardEventData(objectiveName, score);
    }

    protected void writeCodeBuilderScoreboard(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        CodeBuilderScoreboardEventData event = (CodeBuilderScoreboardEventData) eventData;
        helper.writeString(buffer, event.getObjectiveName());
        VarInts.writeInt(buffer, event.getScore());
    }
}
