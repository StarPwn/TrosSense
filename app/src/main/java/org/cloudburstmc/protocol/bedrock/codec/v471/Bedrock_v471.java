package org.cloudburstmc.protocol.bedrock.codec.v471;

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
import org.cloudburstmc.protocol.bedrock.codec.v465.Bedrock_v465;
import org.cloudburstmc.protocol.bedrock.codec.v471.serializer.EventSerializer_v471;
import org.cloudburstmc.protocol.bedrock.codec.v471.serializer.PhotoInfoRequestSerializer_v471;
import org.cloudburstmc.protocol.bedrock.codec.v471.serializer.SubChunkRequestSerializer_v471;
import org.cloudburstmc.protocol.bedrock.codec.v471.serializer.SubChunkSerializer_v471;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.ParticleType;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.bedrock.packet.EventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventGenericPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.PhotoInfoRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.SubChunkPacket;
import org.cloudburstmc.protocol.bedrock.packet.SubChunkRequestPacket;
import org.cloudburstmc.protocol.bedrock.transformer.TypeMapTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v471 extends Bedrock_v465 {
    protected static final TypeMap<ParticleType> PARTICLE_TYPES = Bedrock_v465.PARTICLE_TYPES.toBuilder().insert(83, (int) ParticleType.SCULK_SOUL).build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v465.ENTITY_DATA.toBuilder().update(EntityDataTypes.AREA_EFFECT_CLOUD_PARTICLE, new TypeMapTransformer(PARTICLE_TYPES)).build();
    protected static final TypeMap<ItemStackRequestActionType> ITEM_STACK_REQUEST_TYPES = Bedrock_v465.ITEM_STACK_REQUEST_TYPES.toBuilder().shift(14, 2).insert(14, (int) ItemStackRequestActionType.CRAFT_REPAIR_AND_DISENCHANT).insert(15, (int) ItemStackRequestActionType.CRAFT_LOOM).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v465.LEVEL_EVENTS.toBuilder().insert(2036, (int) LevelEvent.SCULK_CATALYST_BLOOM).insert(16384, PARTICLE_TYPES).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v465.SOUND_EVENTS.toBuilder().insert(365, (int) SoundEvent.SCULK_CATALYST_BLOOM).build();
    public static final BedrockCodec CODEC = Bedrock_v465.CODEC.toBuilder().protocolVersion(471).minecraftVersion("1.17.40").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v471.Bedrock_v471$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v471.lambda$static$0();
        }
    }).updateSerializer(EventPacket.class, EventSerializer_v471.INSTANCE).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).updateSerializer(LevelEventGenericPacket.class, new LevelEventGenericSerializer_v361(LEVEL_EVENTS)).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v471.Bedrock_v471$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new PhotoInfoRequestPacket();
        }
    }, PhotoInfoRequestSerializer_v471.INSTANCE, bl.b8).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v471.Bedrock_v471$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SubChunkPacket();
        }
    }, SubChunkSerializer_v471.INSTANCE, bl.b9).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v471.Bedrock_v471$$ExternalSyntheticLambda3
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SubChunkRequestPacket();
        }
    }, SubChunkRequestSerializer_v471.INSTANCE, bl.b_).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v471(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES);
    }
}
