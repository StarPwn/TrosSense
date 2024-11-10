package org.cloudburstmc.protocol.bedrock.codec.v448;

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
import org.cloudburstmc.protocol.bedrock.codec.v440.Bedrock_v440;
import org.cloudburstmc.protocol.bedrock.codec.v448.serializer.AvailableCommandsSerializer_v448;
import org.cloudburstmc.protocol.bedrock.codec.v448.serializer.NpcDialogueSerializer_v448;
import org.cloudburstmc.protocol.bedrock.codec.v448.serializer.NpcRequestSerializer_v448;
import org.cloudburstmc.protocol.bedrock.codec.v448.serializer.ResourcePacksInfoSerializer_v448;
import org.cloudburstmc.protocol.bedrock.codec.v448.serializer.SetTitleSerializer_v448;
import org.cloudburstmc.protocol.bedrock.codec.v448.serializer.SimulationTypeSerializer_v448;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.ParticleType;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventGenericPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.NpcDialoguePacket;
import org.cloudburstmc.protocol.bedrock.packet.NpcRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePacksInfoPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetTitlePacket;
import org.cloudburstmc.protocol.bedrock.packet.SimulationTypePacket;
import org.cloudburstmc.protocol.bedrock.transformer.FlagTransformer;
import org.cloudburstmc.protocol.bedrock.transformer.TypeMapTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v448 extends Bedrock_v440 {
    protected static final TypeMap<EntityFlag> ENTITY_FLAGS = Bedrock_v440.ENTITY_FLAGS.toBuilder().insert(98, (int) EntityFlag.IN_ASCENDABLE_BLOCK).insert(99, (int) EntityFlag.OVER_DESCENDABLE_BLOCK).build();
    protected static final TypeMap<ParticleType> PARTICLE_TYPES = Bedrock_v440.PARTICLE_TYPES.toBuilder().shift(9, 1).insert(9, (int) ParticleType.CANDLE_FLAME).build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v440.ENTITY_DATA.toBuilder().update(EntityDataTypes.FLAGS, new FlagTransformer(ENTITY_FLAGS, 0)).update(EntityDataTypes.FLAGS_2, new FlagTransformer(ENTITY_FLAGS, 1)).update(EntityDataTypes.AREA_EFFECT_CLOUD_PARTICLE, new TypeMapTransformer(PARTICLE_TYPES)).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v440.LEVEL_EVENTS.toBuilder().insert(16384, PARTICLE_TYPES).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v440.SOUND_EVENTS.toBuilder().replace(360, SoundEvent.CAKE_ADD_CANDLE).insert(361, (int) SoundEvent.EXTINGUISH_CANDLE).insert(362, (int) SoundEvent.AMBIENT_CANDLE).insert(363, (int) SoundEvent.UNDEFINED).build();
    public static final BedrockCodec CODEC = Bedrock_v440.CODEC.toBuilder().protocolVersion(448).minecraftVersion("1.17.10").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v448.Bedrock_v448$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v448.lambda$static$0();
        }
    }).updateSerializer(AvailableCommandsPacket.class, new AvailableCommandsSerializer_v448(COMMAND_PARAMS)).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).updateSerializer(LevelEventGenericPacket.class, new LevelEventGenericSerializer_v361(LEVEL_EVENTS)).updateSerializer(NpcRequestPacket.class, NpcRequestSerializer_v448.INSTANCE).updateSerializer(SetTitlePacket.class, SetTitleSerializer_v448.INSTANCE).updateSerializer(ResourcePacksInfoPacket.class, ResourcePacksInfoSerializer_v448.INSTANCE).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v448.Bedrock_v448$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SimulationTypePacket();
        }
    }, SimulationTypeSerializer_v448.INSTANCE, bl.b3).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v448.Bedrock_v448$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new NpcDialoguePacket();
        }
    }, NpcDialogueSerializer_v448.INSTANCE, bl.b4).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v448(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES);
    }
}
