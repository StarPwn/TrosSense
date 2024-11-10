package org.cloudburstmc.protocol.bedrock.codec.v440;

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
import org.cloudburstmc.protocol.bedrock.codec.v431.Bedrock_v431;
import org.cloudburstmc.protocol.bedrock.codec.v440.serializer.AddVolumeEntitySerializer_v440;
import org.cloudburstmc.protocol.bedrock.codec.v440.serializer.RemoveVolumeEntitySerializer_v440;
import org.cloudburstmc.protocol.bedrock.codec.v440.serializer.StartGameSerializer_v440;
import org.cloudburstmc.protocol.bedrock.codec.v440.serializer.SyncEntityPropertySerializer_v440;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.ParticleType;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.AddVolumeEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventGenericPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.RemoveVolumeEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.bedrock.packet.SyncEntityPropertyPacket;
import org.cloudburstmc.protocol.bedrock.transformer.FlagTransformer;
import org.cloudburstmc.protocol.bedrock.transformer.TypeMapTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v440 extends Bedrock_v431 {
    protected static final TypeMap<EntityFlag> ENTITY_FLAGS = Bedrock_v431.ENTITY_FLAGS.toBuilder().insert(97, (int) EntityFlag.PLAYING_DEAD).build();
    protected static final TypeMap<ParticleType> PARTICLE_TYPES = Bedrock_v431.PARTICLE_TYPES.toBuilder().insert(73, (int) ParticleType.PORTAL_REVERSE).insert(74, (int) ParticleType.SNOWFLAKE).insert(75, (int) ParticleType.VIBRATION_SIGNAL).insert(76, (int) ParticleType.SCULK_SENSOR_REDSTONE).insert(77, (int) ParticleType.SPORE_BLOSSOM_SHOWER).insert(78, (int) ParticleType.SPORE_BLOSSOM_AMBIENT).insert(79, (int) ParticleType.WAX).insert(80, (int) ParticleType.ELECTRIC_SPARK).build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v431.ENTITY_DATA.toBuilder().update(EntityDataTypes.FLAGS, new FlagTransformer(ENTITY_FLAGS, 0)).update(EntityDataTypes.FLAGS_2, new FlagTransformer(ENTITY_FLAGS, 1)).update(EntityDataTypes.AREA_EFFECT_CLOUD_PARTICLE, new TypeMapTransformer(PARTICLE_TYPES)).shift(120, 1).insert(EntityDataTypes.UPDATE_PROPERTIES, 120, EntityDataFormat.NBT).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v431.LEVEL_EVENTS.toBuilder().insert(16384, PARTICLE_TYPES).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v431.SOUND_EVENTS.toBuilder().replace(339, SoundEvent.COPPER_WAX_ON).insert(340, (int) SoundEvent.COPPER_WAX_OFF).insert(341, (int) SoundEvent.SCRAPE).insert(342, (int) SoundEvent.PLAYER_HURT_DROWN).insert(343, (int) SoundEvent.PLAYER_HURT_ON_FIRE).insert(344, (int) SoundEvent.PLAYER_HURT_FREEZE).insert(345, (int) SoundEvent.USE_SPYGLASS).insert(346, (int) SoundEvent.STOP_USING_SPYGLASS).insert(347, (int) SoundEvent.AMETHYST_BLOCK_CHIME).insert(348, (int) SoundEvent.AMBIENT_SCREAMER).insert(349, (int) SoundEvent.HURT_SCREAMER).insert(350, (int) SoundEvent.DEATH_SCREAMER).insert(351, (int) SoundEvent.MILK_SCREAMER).insert(352, (int) SoundEvent.JUMP_TO_BLOCK).insert(353, (int) SoundEvent.PRE_RAM).insert(354, (int) SoundEvent.PRE_RAM_SCREAMER).insert(355, (int) SoundEvent.RAM_IMPACT).insert(356, (int) SoundEvent.RAM_IMPACT_SCREAMER).insert(357, (int) SoundEvent.SQUID_INK_SQUIRT).insert(358, (int) SoundEvent.GLOW_SQUID_INK_SQUIRT).insert(359, (int) SoundEvent.CONVERT_TO_STRAY).insert(360, (int) SoundEvent.UNDEFINED).build();
    public static final BedrockCodec CODEC = Bedrock_v431.CODEC.toBuilder().protocolVersion(440).minecraftVersion("1.17.0").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v440.Bedrock_v440$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v440.lambda$static$0();
        }
    }).updateSerializer(StartGamePacket.class, StartGameSerializer_v440.INSTANCE).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).updateSerializer(LevelEventGenericPacket.class, new LevelEventGenericSerializer_v361(LEVEL_EVENTS)).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v440.Bedrock_v440$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SyncEntityPropertyPacket();
        }
    }, SyncEntityPropertySerializer_v440.INSTANCE, bl.b0).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v440.Bedrock_v440$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new AddVolumeEntityPacket();
        }
    }, AddVolumeEntitySerializer_v440.INSTANCE, bl.b1).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v440.Bedrock_v440$$ExternalSyntheticLambda3
        @Override // java.util.function.Supplier
        public final Object get() {
            return new RemoveVolumeEntityPacket();
        }
    }, RemoveVolumeEntitySerializer_v440.INSTANCE, bl.b2).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v440(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES);
    }
}
