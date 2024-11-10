package org.cloudburstmc.protocol.bedrock.codec.v361;

import com.trossense.bl;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v340.Bedrock_v340$$ExternalSyntheticLambda1;
import org.cloudburstmc.protocol.bedrock.codec.v340.Bedrock_v340$$ExternalSyntheticLambda2;
import org.cloudburstmc.protocol.bedrock.codec.v354.Bedrock_v354;
import org.cloudburstmc.protocol.bedrock.codec.v354.serializer.LecternUpdateSerializer_v354;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.AddPaintingSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.ClientCacheBlobStatusSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.ClientCacheMissResponseSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.ClientCacheStatusSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.CommandBlockUpdateSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.CraftingDataSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.LevelChunkSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.LevelEventGenericSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.ResourcePackDataInfoSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.StartGameSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.StructureBlockUpdateSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.StructureTemplateDataRequestSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.StructureTemplateDataResponseSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.UpdateBlockPropertiesSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.VideoStreamConnectSerializer_v361;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.ParticleType;
import org.cloudburstmc.protocol.bedrock.data.ResourcePackType;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.AddHangingEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.AddPaintingPacket;
import org.cloudburstmc.protocol.bedrock.packet.ClientCacheBlobStatusPacket;
import org.cloudburstmc.protocol.bedrock.packet.ClientCacheMissResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.ClientCacheStatusPacket;
import org.cloudburstmc.protocol.bedrock.packet.CommandBlockUpdatePacket;
import org.cloudburstmc.protocol.bedrock.packet.CraftingDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.LecternUpdatePacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventGenericPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackDataInfoPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.bedrock.packet.StructureBlockUpdatePacket;
import org.cloudburstmc.protocol.bedrock.packet.StructureTemplateDataRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.StructureTemplateDataResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPropertiesPacket;
import org.cloudburstmc.protocol.bedrock.packet.VideoStreamConnectPacket;
import org.cloudburstmc.protocol.bedrock.transformer.BooleanTransformer;
import org.cloudburstmc.protocol.bedrock.transformer.FlagTransformer;
import org.cloudburstmc.protocol.bedrock.transformer.TypeMapTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v361 extends Bedrock_v354 {
    protected static final TypeMap<EntityFlag> ENTITY_FLAGS = Bedrock_v354.ENTITY_FLAGS.toBuilder().insert(87, (int) EntityFlag.HIDDEN_WHEN_INVISIBLE).build();
    protected static final TypeMap<ParticleType> PARTICLE_TYPES = Bedrock_v354.PARTICLE_TYPES.toBuilder().shift(2, 1).insert(2, (int) ParticleType.BUBBLE_MANUAL).shift(22, 1).insert(22, (int) ParticleType.MOB_PORTAL).shift(24, 1).insert(24, (int) ParticleType.WATER_SPLASH_MANUAL).build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v354.ENTITY_DATA.toBuilder().update(EntityDataTypes.FLAGS, new FlagTransformer(ENTITY_FLAGS, 0)).update(EntityDataTypes.FLAGS_2, new FlagTransformer(ENTITY_FLAGS, 1)).update(EntityDataTypes.AREA_EFFECT_CLOUD_PARTICLE, new TypeMapTransformer(PARTICLE_TYPES)).replace(EntityDataTypes.NPC_DATA, 40, EntityDataFormat.STRING).insert(EntityDataTypes.SKIN_ID, 103, EntityDataFormat.INT).insert(EntityDataTypes.SPAWNING_FRAMES, 104, EntityDataFormat.INT).insert(EntityDataTypes.COMMAND_BLOCK_TICK_DELAY, 105, EntityDataFormat.INT).insert(EntityDataTypes.COMMAND_BLOCK_EXECUTE_ON_FIRST_TICK, 106, EntityDataFormat.BYTE, BooleanTransformer.INSTANCE).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v354.LEVEL_EVENTS.toBuilder().insert(2023, (int) LevelEvent.PARTICLE_TELEPORT_TRAIL).insert(16384, PARTICLE_TYPES).build();
    protected static final TypeMap<ResourcePackType> RESOURCE_PACK_TYPES = TypeMap.builder(ResourcePackType.class).insert(0, (int) ResourcePackType.INVALID).insert(1, (int) ResourcePackType.RESOURCES).insert(2, (int) ResourcePackType.DATA_ADD_ON).insert(3, (int) ResourcePackType.WORLD_TEMPLATE).insert(4, (int) ResourcePackType.ADDON).insert(5, (int) ResourcePackType.SKINS).insert(6, (int) ResourcePackType.CACHED).insert(7, (int) ResourcePackType.COPY_PROTECTED).build();
    public static BedrockCodec CODEC = Bedrock_v354.CODEC.toBuilder().protocolVersion(361).minecraftVersion("1.12.0").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v361.Bedrock_v361$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v361.lambda$static$0();
        }
    }).deregisterPacket(AddHangingEntityPacket.class).deregisterPacket(LecternUpdatePacket.class).deregisterPacket(VideoStreamConnectPacket.class).updateSerializer(StartGamePacket.class, StartGameSerializer_v361.INSTANCE).updateSerializer(AddPaintingPacket.class, AddPaintingSerializer_v361.INSTANCE).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).updateSerializer(CraftingDataPacket.class, CraftingDataSerializer_v361.INSTANCE).updateSerializer(LevelChunkPacket.class, LevelChunkSerializer_v361.INSTANCE).updateSerializer(CommandBlockUpdatePacket.class, CommandBlockUpdateSerializer_v361.INSTANCE).updateSerializer(ResourcePackDataInfoPacket.class, new ResourcePackDataInfoSerializer_v361(RESOURCE_PACK_TYPES)).updateSerializer(StructureBlockUpdatePacket.class, StructureBlockUpdateSerializer_v361.INSTANCE).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v361.Bedrock_v361$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new LevelEventGenericPacket();
        }
    }, new LevelEventGenericSerializer_v361(LEVEL_EVENTS), 124).registerPacket(new Bedrock_v340$$ExternalSyntheticLambda1(), LecternUpdateSerializer_v354.INSTANCE, bl.bm).registerPacket(new Bedrock_v340$$ExternalSyntheticLambda2(), VideoStreamConnectSerializer_v361.INSTANCE, 126).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v361.Bedrock_v361$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ClientCacheStatusPacket();
        }
    }, ClientCacheStatusSerializer_v361.INSTANCE, bl.bq).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v361.Bedrock_v361$$ExternalSyntheticLambda3
        @Override // java.util.function.Supplier
        public final Object get() {
            return new StructureTemplateDataRequestPacket();
        }
    }, StructureTemplateDataRequestSerializer_v361.INSTANCE, bl.bt).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v361.Bedrock_v361$$ExternalSyntheticLambda4
        @Override // java.util.function.Supplier
        public final Object get() {
            return new StructureTemplateDataResponsePacket();
        }
    }, StructureTemplateDataResponseSerializer_v361.INSTANCE, bl.bu).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v361.Bedrock_v361$$ExternalSyntheticLambda5
        @Override // java.util.function.Supplier
        public final Object get() {
            return new UpdateBlockPropertiesPacket();
        }
    }, UpdateBlockPropertiesSerializer_v361.INSTANCE, bl.bv).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v361.Bedrock_v361$$ExternalSyntheticLambda6
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ClientCacheBlobStatusPacket();
        }
    }, ClientCacheBlobStatusSerializer_v361.INSTANCE, bl.bw).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v361.Bedrock_v361$$ExternalSyntheticLambda7
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ClientCacheMissResponsePacket();
        }
    }, ClientCacheMissResponseSerializer_v361.INSTANCE, bl.bx).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v361(ENTITY_DATA, GAME_RULE_TYPES);
    }
}
