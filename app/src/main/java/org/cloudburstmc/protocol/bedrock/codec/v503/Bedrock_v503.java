package org.cloudburstmc.protocol.bedrock.codec.v503;

import com.trossense.bl;
import java.util.function.Supplier;
import okhttp3.internal.http.StatusLine;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EntityEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelSoundEvent1Serializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.LevelSoundEvent2Serializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.LevelSoundEventSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.LevelEventGenericSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v448.serializer.AvailableCommandsSerializer_v448;
import org.cloudburstmc.protocol.bedrock.codec.v486.Bedrock_v486;
import org.cloudburstmc.protocol.bedrock.codec.v503.serializer.AddPlayerSerializer_v503;
import org.cloudburstmc.protocol.bedrock.codec.v503.serializer.AddVolumeEntitySerializer_v503;
import org.cloudburstmc.protocol.bedrock.codec.v503.serializer.AgentActionEventSerializer_v503;
import org.cloudburstmc.protocol.bedrock.codec.v503.serializer.ChangeMobPropertySerializer_v503;
import org.cloudburstmc.protocol.bedrock.codec.v503.serializer.DimensionDataSerializer_v503;
import org.cloudburstmc.protocol.bedrock.codec.v503.serializer.RemoveVolumeEntitySerializer_v503;
import org.cloudburstmc.protocol.bedrock.codec.v503.serializer.SpawnParticleEffectSerializer_v503;
import org.cloudburstmc.protocol.bedrock.codec.v503.serializer.StartGameSerializer_v503;
import org.cloudburstmc.protocol.bedrock.codec.v503.serializer.TickingAreasLoadStatusSerializer_v503;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityEventType;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.AddPlayerPacket;
import org.cloudburstmc.protocol.bedrock.packet.AddVolumeEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.AgentActionEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.bedrock.packet.ChangeMobPropertyPacket;
import org.cloudburstmc.protocol.bedrock.packet.DimensionDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.EntityEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventGenericPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.RemoveVolumeEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.SpawnParticleEffectPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.bedrock.packet.TickingAreasLoadStatusPacket;
import org.cloudburstmc.protocol.bedrock.transformer.FlagTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v503 extends Bedrock_v486 {
    protected static final TypeMap<EntityFlag> ENTITY_FLAGS = Bedrock_v486.ENTITY_FLAGS.toBuilder().insert(102, (int) EntityFlag.JUMP_GOAL_JUMP).insert(103, (int) EntityFlag.EMERGING).insert(104, (int) EntityFlag.SNIFFING).insert(105, (int) EntityFlag.DIGGING).build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v486.ENTITY_DATA.toBuilder().update(EntityDataTypes.FLAGS, new FlagTransformer(ENTITY_FLAGS, 0)).update(EntityDataTypes.FLAGS_2, new FlagTransformer(ENTITY_FLAGS, 1)).insert(EntityDataTypes.MOVEMENT_SOUND_DISTANCE_OFFSET, bl.bm, EntityDataFormat.FLOAT).insert(EntityDataTypes.HEARTBEAT_INTERVAL_TICKS, 126, EntityDataFormat.INT).insert(EntityDataTypes.HEARTBEAT_SOUND_EVENT, 127, EntityDataFormat.INT).build();
    protected static final TypeMap<EntityEventType> ENTITY_EVENTS = Bedrock_v486.ENTITY_EVENTS.toBuilder().insert(77, (int) EntityEventType.VIBRATION_DETECTED).build();
    protected static final TypeMap<CommandParam> COMMAND_PARAMS = Bedrock_v486.COMMAND_PARAMS.toBuilder().shift(32, 6).insert(37, (int) CommandParam.EQUIPMENT_SLOTS).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v486.LEVEL_EVENTS.toBuilder().insert(2037, (int) LevelEvent.SCULK_CHARGE).insert(2038, (int) LevelEvent.SCULK_CHARGE_POP).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v486.SOUND_EVENTS.toBuilder().replace(375, SoundEvent.LISTENING).insert(376, (int) SoundEvent.HEARTBEAT).insert(377, (int) SoundEvent.HORN_BREAK).insert(378, (int) SoundEvent.SCULK_PLACE).insert(379, (int) SoundEvent.SCULK_SPREAD).insert(380, (int) SoundEvent.SCULK_CHARGE).insert(381, (int) SoundEvent.SCULK_SENSOR_PLACE).insert(382, (int) SoundEvent.SCULK_SHRIEKER_PLACE).insert(383, (int) SoundEvent.GOAT_CALL_0).insert(384, (int) SoundEvent.GOAT_CALL_1).insert(385, (int) SoundEvent.GOAT_CALL_2).insert(386, (int) SoundEvent.GOAT_CALL_3).insert(387, (int) SoundEvent.GOAT_CALL_4).insert(388, (int) SoundEvent.GOAT_CALL_5).insert(389, (int) SoundEvent.GOAT_CALL_6).insert(390, (int) SoundEvent.GOAT_CALL_7).insert(391, (int) SoundEvent.GOAT_CALL_8).insert(392, (int) SoundEvent.GOAT_CALL_9).insert(393, (int) SoundEvent.GOAT_HARMONY_0).insert(394, (int) SoundEvent.GOAT_HARMONY_1).insert(395, (int) SoundEvent.GOAT_HARMONY_2).insert(396, (int) SoundEvent.GOAT_HARMONY_3).insert(397, (int) SoundEvent.GOAT_HARMONY_4).insert(398, (int) SoundEvent.GOAT_HARMONY_5).insert(399, (int) SoundEvent.GOAT_HARMONY_6).insert(400, (int) SoundEvent.GOAT_HARMONY_7).insert(401, (int) SoundEvent.GOAT_HARMONY_8).insert(402, (int) SoundEvent.GOAT_HARMONY_9).insert(403, (int) SoundEvent.GOAT_MELODY_0).insert(404, (int) SoundEvent.GOAT_MELODY_1).insert(405, (int) SoundEvent.GOAT_MELODY_2).insert(406, (int) SoundEvent.GOAT_MELODY_3).insert(407, (int) SoundEvent.GOAT_MELODY_4).insert(408, (int) SoundEvent.GOAT_MELODY_5).insert(409, (int) SoundEvent.GOAT_MELODY_6).insert(410, (int) SoundEvent.GOAT_MELODY_7).insert(411, (int) SoundEvent.GOAT_MELODY_8).insert(412, (int) SoundEvent.GOAT_MELODY_9).insert(413, (int) SoundEvent.GOAT_BASS_0).insert(414, (int) SoundEvent.GOAT_BASS_1).insert(415, (int) SoundEvent.GOAT_BASS_2).insert(416, (int) SoundEvent.GOAT_BASS_3).insert(417, (int) SoundEvent.GOAT_BASS_4).insert(418, (int) SoundEvent.GOAT_BASS_5).insert(419, (int) SoundEvent.GOAT_BASS_6).insert(420, (int) SoundEvent.GOAT_BASS_7).insert(StatusLine.HTTP_MISDIRECTED_REQUEST, (int) SoundEvent.GOAT_BASS_8).insert(422, (int) SoundEvent.GOAT_BASS_9).insert(423, (int) SoundEvent.UNDEFINED).build();
    public static final BedrockCodec CODEC = Bedrock_v486.CODEC.toBuilder().protocolVersion(503).minecraftVersion("1.18.30").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v503.Bedrock_v503$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v503.lambda$static$0();
        }
    }).updateSerializer(StartGamePacket.class, new StartGameSerializer_v503()).updateSerializer(AddPlayerPacket.class, new AddPlayerSerializer_v503()).updateSerializer(AvailableCommandsPacket.class, new AvailableCommandsSerializer_v448(COMMAND_PARAMS)).updateSerializer(EntityEventPacket.class, new EntityEventSerializer_v291(ENTITY_EVENTS)).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).updateSerializer(LevelEventGenericPacket.class, new LevelEventGenericSerializer_v361(LEVEL_EVENTS)).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).updateSerializer(SpawnParticleEffectPacket.class, new SpawnParticleEffectSerializer_v503()).updateSerializer(AddVolumeEntityPacket.class, new AddVolumeEntitySerializer_v503()).updateSerializer(RemoveVolumeEntityPacket.class, new RemoveVolumeEntitySerializer_v503()).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v503.Bedrock_v503$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new TickingAreasLoadStatusPacket();
        }
    }, new TickingAreasLoadStatusSerializer_v503(), bl.cd).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v503.Bedrock_v503$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new DimensionDataPacket();
        }
    }, new DimensionDataSerializer_v503(), bl.ce).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v503.Bedrock_v503$$ExternalSyntheticLambda3
        @Override // java.util.function.Supplier
        public final Object get() {
            return new AgentActionEventPacket();
        }
    }, new AgentActionEventSerializer_v503(), bl.cf).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v503.Bedrock_v503$$ExternalSyntheticLambda4
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ChangeMobPropertyPacket();
        }
    }, new ChangeMobPropertySerializer_v503(), bl.cg).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v503(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES);
    }
}
