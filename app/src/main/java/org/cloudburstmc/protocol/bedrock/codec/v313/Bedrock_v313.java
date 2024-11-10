package org.cloudburstmc.protocol.bedrock.codec.v313;

import com.trossense.bl;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EntityEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelSoundEvent1Serializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.AddEntitySerializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.AvailableEntityIdentifiersSerializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.BiomeDefinitionListSerializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.LevelSoundEvent2Serializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.NetworkChunkPublisherUpdateSerializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.ResourcePackStackSerializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.SpawnParticleEffectSerializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.StartGameSerializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.UpdateTradeSerializer_v313;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.ParticleType;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityEventType;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.AddEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.bedrock.packet.AvailableEntityIdentifiersPacket;
import org.cloudburstmc.protocol.bedrock.packet.BiomeDefinitionListPacket;
import org.cloudburstmc.protocol.bedrock.packet.EntityEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.NetworkChunkPublisherUpdatePacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackStackPacket;
import org.cloudburstmc.protocol.bedrock.packet.SpawnParticleEffectPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateTradePacket;
import org.cloudburstmc.protocol.bedrock.transformer.FlagTransformer;
import org.cloudburstmc.protocol.bedrock.transformer.TypeMapTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v313 extends Bedrock_v291 {
    protected static final TypeMap<EntityFlag> ENTITY_FLAGS = Bedrock_v291.ENTITY_FLAGS.toBuilder().insert(61, (int) EntityFlag.TRANSITION_SITTING).insert(62, (int) EntityFlag.EATING).insert(63, (int) EntityFlag.LAYING_DOWN).insert(64, (int) EntityFlag.SNEEZING).insert(65, (int) EntityFlag.TRUSTING).insert(66, (int) EntityFlag.ROLLING).insert(67, (int) EntityFlag.SCARED).insert(68, (int) EntityFlag.IN_SCAFFOLDING).insert(69, (int) EntityFlag.OVER_SCAFFOLDING).insert(70, (int) EntityFlag.DESCEND_THROUGH_BLOCK).build();
    protected static final TypeMap<ParticleType> PARTICLE_TYPES = Bedrock_v291.PARTICLE_TYPES.toBuilder().insert(45, (int) ParticleType.FIREWORKS_STARTER).insert(46, (int) ParticleType.FIREWORKS).insert(47, (int) ParticleType.FIREWORKS_OVERLAY).insert(48, (int) ParticleType.BALLOON_GAS).insert(49, (int) ParticleType.COLORED_FLAME).insert(50, (int) ParticleType.SPARKLER).insert(51, (int) ParticleType.CONDUIT).insert(52, (int) ParticleType.BUBBLE_COLUMN_UP).insert(53, (int) ParticleType.BUBBLE_COLUMN_DOWN).insert(54, (int) ParticleType.SNEEZE).build();
    protected static final TypeMap<CommandParam> COMMAND_PARAMS = Bedrock_v291.COMMAND_PARAMS.toBuilder().shift(24, 2).build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v291.ENTITY_DATA.toBuilder().update(EntityDataTypes.FLAGS, new FlagTransformer(ENTITY_FLAGS, 0)).update(EntityDataTypes.AREA_EFFECT_CLOUD_PARTICLE, new TypeMapTransformer(PARTICLE_TYPES)).insert(EntityDataTypes.SITTING_AMOUNT, 88, EntityDataFormat.FLOAT).insert(EntityDataTypes.SITTING_AMOUNT_PREVIOUS, 89, EntityDataFormat.FLOAT).insert(EntityDataTypes.EATING_COUNTER, 90, EntityDataFormat.INT).insert(EntityDataTypes.FLAGS_2, 91, EntityDataFormat.LONG, new FlagTransformer(ENTITY_FLAGS, 1)).insert(EntityDataTypes.LAYING_AMOUNT, 92, EntityDataFormat.FLOAT).insert(EntityDataTypes.LAYING_AMOUNT_PREVIOUS, 93, EntityDataFormat.FLOAT).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v291.SOUND_EVENTS.toBuilder().replace(bl.da, SoundEvent.BLOCK_BAMBOO_SAPLING_PLACE).insert(bl.db, (int) SoundEvent.PRE_SNEEZE).insert(bl.dc, (int) SoundEvent.SNEEZE).insert(bl.dd, (int) SoundEvent.AMBIENT_TAME).insert(bl.de, (int) SoundEvent.SCARED).insert(bl.df, (int) SoundEvent.BLOCK_SCAFFOLDING_CLIMB).insert(bl.dg, (int) SoundEvent.CROSSBOW_LOADING_START).insert(bl.dh, (int) SoundEvent.CROSSBOW_LOADING_MIDDLE).insert(bl.di, (int) SoundEvent.CROSSBOW_LOADING_END).insert(bl.dj, (int) SoundEvent.CROSSBOW_SHOOT).insert(bl.dk, (int) SoundEvent.CROSSBOW_QUICK_CHARGE_START).insert(250, (int) SoundEvent.CROSSBOW_QUICK_CHARGE_MIDDLE).insert(bl.dm, (int) SoundEvent.CROSSBOW_QUICK_CHARGE_END).insert(bl.dn, (int) SoundEvent.AMBIENT_AGGRESSIVE).insert(bl.dp, (int) SoundEvent.AMBIENT_WORRIED).insert(254, (int) SoundEvent.CANT_BREED).insert(255, (int) SoundEvent.UNDEFINED).build();
    protected static final TypeMap<EntityEventType> ENTITY_EVENTS = Bedrock_v291.ENTITY_EVENTS.toBuilder().insert(73, (int) EntityEventType.SUMMON_AGENT).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v291.LEVEL_EVENTS.toBuilder().insert(3511, (int) LevelEvent.AGENT_SPAWN_EFFECT).insert(16384, PARTICLE_TYPES).build();
    public static final BedrockCodec CODEC = Bedrock_v291.CODEC.toBuilder().protocolVersion(bl.em).minecraftVersion("1.8.0").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v313.Bedrock_v313$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v313.lambda$static$0();
        }
    }).updateSerializer(ResourcePackStackPacket.class, ResourcePackStackSerializer_v313.INSTANCE).updateSerializer(StartGamePacket.class, StartGameSerializer_v313.INSTANCE).updateSerializer(AddEntityPacket.class, AddEntitySerializer_v313.INSTANCE).updateSerializer(UpdateTradePacket.class, UpdateTradeSerializer_v313.INSTANCE).updateSerializer(AvailableCommandsPacket.class, new AvailableCommandsSerializer_v291(COMMAND_PARAMS)).updateSerializer(EntityEventPacket.class, new EntityEventSerializer_v291(ENTITY_EVENTS)).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v313.Bedrock_v313$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SpawnParticleEffectPacket();
        }
    }, SpawnParticleEffectSerializer_v313.INSTANCE, 118).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v313.Bedrock_v313$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new AvailableEntityIdentifiersPacket();
        }
    }, AvailableEntityIdentifiersSerializer_v313.INSTANCE, 119).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v313.Bedrock_v313$$ExternalSyntheticLambda3
        @Override // java.util.function.Supplier
        public final Object get() {
            return new LevelSoundEvent2Packet();
        }
    }, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS), 120).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v313.Bedrock_v313$$ExternalSyntheticLambda4
        @Override // java.util.function.Supplier
        public final Object get() {
            return new NetworkChunkPublisherUpdatePacket();
        }
    }, NetworkChunkPublisherUpdateSerializer_v313.INSTANCE, 121).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v313.Bedrock_v313$$ExternalSyntheticLambda5
        @Override // java.util.function.Supplier
        public final Object get() {
            return new BiomeDefinitionListPacket();
        }
    }, BiomeDefinitionListSerializer_v313.INSTANCE, 122).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v313(ENTITY_DATA, GAME_RULE_TYPES);
    }
}
