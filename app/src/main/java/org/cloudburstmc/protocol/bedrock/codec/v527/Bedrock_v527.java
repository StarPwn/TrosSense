package org.cloudburstmc.protocol.bedrock.codec.v527;

import com.trossense.bl;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelSoundEvent1Serializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.LevelSoundEvent2Serializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.LevelSoundEventSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.LevelEventGenericSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v448.serializer.AvailableCommandsSerializer_v448;
import org.cloudburstmc.protocol.bedrock.codec.v503.BedrockCodecHelper_v503;
import org.cloudburstmc.protocol.bedrock.codec.v503.Bedrock_v503;
import org.cloudburstmc.protocol.bedrock.codec.v527.serializer.LessonProgressSerializer_v527;
import org.cloudburstmc.protocol.bedrock.codec.v527.serializer.PlayerActionSerializer_v527;
import org.cloudburstmc.protocol.bedrock.codec.v527.serializer.PlayerAuthInputSerializer_v527;
import org.cloudburstmc.protocol.bedrock.codec.v527.serializer.RequestAbilitySerializer_v527;
import org.cloudburstmc.protocol.bedrock.codec.v527.serializer.RequestPermissionsSerializer_v527;
import org.cloudburstmc.protocol.bedrock.codec.v527.serializer.StartGameSerializer_v527;
import org.cloudburstmc.protocol.bedrock.codec.v527.serializer.ToastRequestSerializer_v527;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.ParticleType;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.bedrock.packet.LessonProgressPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventGenericPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerActionPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.cloudburstmc.protocol.bedrock.packet.RequestAbilityPacket;
import org.cloudburstmc.protocol.bedrock.packet.RequestPermissionsPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.bedrock.packet.ToastRequestPacket;
import org.cloudburstmc.protocol.bedrock.transformer.BooleanTransformer;
import org.cloudburstmc.protocol.bedrock.transformer.FlagTransformer;
import org.cloudburstmc.protocol.bedrock.transformer.TypeMapTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v527 extends Bedrock_v503 {
    protected static final TypeMap<EntityFlag> ENTITY_FLAGS = Bedrock_v503.ENTITY_FLAGS.toBuilder().insert(106, (int) EntityFlag.SONIC_BOOM).build();
    protected static final TypeMap<ParticleType> PARTICLE_TYPES = Bedrock_v503.PARTICLE_TYPES.toBuilder().insert(84, (int) ParticleType.SONIC_EXPLOSION).build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v503.ENTITY_DATA.toBuilder().update(EntityDataTypes.FLAGS, new FlagTransformer(ENTITY_FLAGS, 0)).update(EntityDataTypes.FLAGS_2, new FlagTransformer(ENTITY_FLAGS, 1)).update(EntityDataTypes.AREA_EFFECT_CLOUD_PARTICLE, new TypeMapTransformer(PARTICLE_TYPES)).insert(EntityDataTypes.PLAYER_LAST_DEATH_POS, 128, EntityDataFormat.VECTOR3I).insert(EntityDataTypes.PLAYER_LAST_DEATH_DIMENSION, bl.bq, EntityDataFormat.INT).insert(EntityDataTypes.PLAYER_HAS_DIED, bl.br, EntityDataFormat.BYTE, BooleanTransformer.INSTANCE).build();
    protected static final TypeMap<CommandParam> COMMAND_PARAMS = Bedrock_v503.COMMAND_PARAMS.toBuilder().shift(7, 1).insert(7, (int) CommandParam.COMPARE_OPERATOR).insert(23, (int) CommandParam.INT_RANGE).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v503.LEVEL_EVENTS.toBuilder().insert(2039, (int) LevelEvent.SONIC_EXPLOSION).insert(16384, PARTICLE_TYPES).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v503.SOUND_EVENTS.toBuilder().remove(423).insert(426, (int) SoundEvent.IMITATE_WARDEN).insert(428, (int) SoundEvent.ITEM_GIVEN).insert(429, (int) SoundEvent.ITEM_TAKEN).insert(430, (int) SoundEvent.DISAPPEARED).insert(431, (int) SoundEvent.REAPPEARED).insert(433, (int) SoundEvent.FROGSPAWN_HATCHED).insert(434, (int) SoundEvent.LAY_SPAWN).insert(435, (int) SoundEvent.FROGSPAWN_BREAK).insert(436, (int) SoundEvent.SONIC_BOOM).insert(437, (int) SoundEvent.SONIC_CHARGE).insert(438, (int) SoundEvent.ITEM_THROWN).insert(439, (int) SoundEvent.RECORD_5).insert(440, (int) SoundEvent.CONVERT_TO_FROG).insert(441, (int) SoundEvent.UNDEFINED).build();
    public static final BedrockCodec CODEC = Bedrock_v503.CODEC.toBuilder().protocolVersion(527).minecraftVersion("1.19.0").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v527.Bedrock_v527$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v527.lambda$static$0();
        }
    }).updateSerializer(StartGamePacket.class, new StartGameSerializer_v527()).updateSerializer(AvailableCommandsPacket.class, new AvailableCommandsSerializer_v448(COMMAND_PARAMS)).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).updateSerializer(LevelEventGenericPacket.class, new LevelEventGenericSerializer_v361(LEVEL_EVENTS)).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).updateSerializer(PlayerActionPacket.class, new PlayerActionSerializer_v527()).updateSerializer(PlayerAuthInputPacket.class, new PlayerAuthInputSerializer_v527()).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v527.Bedrock_v527$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new LessonProgressPacket();
        }
    }, new LessonProgressSerializer_v527(), bl.ch).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v527.Bedrock_v527$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new RequestAbilityPacket();
        }
    }, new RequestAbilitySerializer_v527(), bl.ci).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v527.Bedrock_v527$$ExternalSyntheticLambda3
        @Override // java.util.function.Supplier
        public final Object get() {
            return new RequestPermissionsPacket();
        }
    }, new RequestPermissionsSerializer_v527(), bl.cj).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v527.Bedrock_v527$$ExternalSyntheticLambda4
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ToastRequestPacket();
        }
    }, new ToastRequestSerializer_v527(), bl.ck).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v503(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES);
    }
}
