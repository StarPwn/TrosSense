package org.cloudburstmc.protocol.bedrock.codec.v332;

import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v313.Bedrock_v313;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.AddPaintingSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.EventSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.LevelSoundEventSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.NetworkStackLatencySerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.ResourcePacksInfoSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.SpawnParticleEffectSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.StartGameSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.TextSerializer_v332;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.packet.AddPaintingPacket;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.bedrock.packet.EventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.NetworkStackLatencyPacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePacksInfoPacket;
import org.cloudburstmc.protocol.bedrock.packet.SpawnParticleEffectPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.bedrock.packet.TextPacket;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v332 extends Bedrock_v313 {
    protected static final TypeMap<CommandParam> COMMAND_PARAMS = Bedrock_v313.COMMAND_PARAMS.toBuilder().insert(15, (int) CommandParam.FILE_PATH).shift(26, 2).build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v313.ENTITY_DATA.toBuilder().insert(EntityDataTypes.AREA_EFFECT_CLOUD_DURATION, 94, EntityDataFormat.INT).insert(EntityDataTypes.AREA_EFFECT_CLOUD_SPAWN_TIME, 95, EntityDataFormat.INT).insert(EntityDataTypes.AREA_EFFECT_CLOUD_CHANGE_RATE, 96, EntityDataFormat.FLOAT).insert(EntityDataTypes.AREA_EFFECT_CLOUD_CHANGE_ON_PICKUP, 97, EntityDataFormat.FLOAT).insert(EntityDataTypes.AREA_EFFECT_CLOUD_PICKUP_COUNT, 98, EntityDataFormat.INT).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v313.LEVEL_EVENTS.toBuilder().insert(3512, (int) LevelEvent.CAULDRON_FILL_LAVA).insert(3513, (int) LevelEvent.CAULDRON_TAKE_LAVA).build();
    public static final BedrockCodec CODEC = Bedrock_v313.CODEC.toBuilder().protocolVersion(332).minecraftVersion("1.9.0").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v332.Bedrock_v332$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v332.lambda$static$0();
        }
    }).updateSerializer(ResourcePacksInfoPacket.class, ResourcePacksInfoSerializer_v332.INSTANCE).updateSerializer(TextPacket.class, TextSerializer_v332.INSTANCE).updateSerializer(StartGamePacket.class, StartGameSerializer_v332.INSTANCE).updateSerializer(AddPaintingPacket.class, AddPaintingSerializer_v332.INSTANCE).updateSerializer(EventPacket.class, EventSerializer_v332.INSTANCE).updateSerializer(NetworkStackLatencyPacket.class, NetworkStackLatencySerializer_v332.INSTANCE).updateSerializer(SpawnParticleEffectPacket.class, SpawnParticleEffectSerializer_v332.INSTANCE).updateSerializer(AvailableCommandsPacket.class, new AvailableCommandsSerializer_v291(COMMAND_PARAMS)).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v332.Bedrock_v332$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new LevelSoundEventPacket();
        }
    }, new LevelSoundEventSerializer_v332(SOUND_EVENTS), 123).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v332(ENTITY_DATA, GAME_RULE_TYPES);
    }
}
