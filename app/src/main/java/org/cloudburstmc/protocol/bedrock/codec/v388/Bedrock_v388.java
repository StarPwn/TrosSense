package org.cloudburstmc.protocol.bedrock.codec.v388;

import com.trossense.bl;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EntityEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelSoundEvent1Serializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.LevelSoundEvent2Serializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.LevelSoundEventSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v361.Bedrock_v361;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.LevelEventGenericSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.ResourcePackDataInfoSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AddPlayerSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AnvilDamageSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AvailableCommandsSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.CompletedUsingItemSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.CraftingDataSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.EducationSettingsSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.EmoteSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.EventSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.InteractSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.MoveEntityDeltaSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.MultiplayerSettingsSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.NetworkSettingsSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.PlayerAuthInputSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.PlayerListSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.PlayerSkinSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.ResourcePackChunkDataSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.ResourcePackStackSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.RespawnSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.SettingsCommandSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.StartGameSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.StructureBlockUpdateSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.StructureTemplateDataResponseSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.TickSyncSerializer_v388;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.ResourcePackType;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityEventType;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.AddPlayerPacket;
import org.cloudburstmc.protocol.bedrock.packet.AnvilDamagePacket;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.bedrock.packet.CompletedUsingItemPacket;
import org.cloudburstmc.protocol.bedrock.packet.CraftingDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.EducationSettingsPacket;
import org.cloudburstmc.protocol.bedrock.packet.EmotePacket;
import org.cloudburstmc.protocol.bedrock.packet.EntityEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.EventPacket;
import org.cloudburstmc.protocol.bedrock.packet.ExplodePacket;
import org.cloudburstmc.protocol.bedrock.packet.InteractPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventGenericPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.MoveEntityDeltaPacket;
import org.cloudburstmc.protocol.bedrock.packet.MultiplayerSettingsPacket;
import org.cloudburstmc.protocol.bedrock.packet.NetworkSettingsPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerListPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerSkinPacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackChunkDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackDataInfoPacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackStackPacket;
import org.cloudburstmc.protocol.bedrock.packet.RespawnPacket;
import org.cloudburstmc.protocol.bedrock.packet.SettingsCommandPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.bedrock.packet.StructureBlockUpdatePacket;
import org.cloudburstmc.protocol.bedrock.packet.StructureTemplateDataResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.TickSyncPacket;
import org.cloudburstmc.protocol.bedrock.transformer.BooleanTransformer;
import org.cloudburstmc.protocol.bedrock.transformer.FlagTransformer;
import org.cloudburstmc.protocol.bedrock.transformer.TypeMapTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v388 extends Bedrock_v361 {
    protected static final TypeMap<EntityFlag> ENTITY_FLAGS = Bedrock_v361.ENTITY_FLAGS.toBuilder().insert(88, (int) EntityFlag.IS_IN_UI).insert(89, (int) EntityFlag.STALKING).insert(90, (int) EntityFlag.EMOTING).insert(91, (int) EntityFlag.CELEBRATING).build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v361.ENTITY_DATA.toBuilder().update(EntityDataTypes.FLAGS, new FlagTransformer(ENTITY_FLAGS, 0)).update(EntityDataTypes.FLAGS_2, new FlagTransformer(ENTITY_FLAGS, 1)).update(EntityDataTypes.AREA_EFFECT_CLOUD_PARTICLE, new TypeMapTransformer(PARTICLE_TYPES)).insert(EntityDataTypes.AMBIENT_SOUND_INTERVAL, 107, EntityDataFormat.FLOAT).insert(EntityDataTypes.AMBIENT_SOUND_INTERVAL_RANGE, 108, EntityDataFormat.FLOAT).insert(EntityDataTypes.AMBIENT_SOUND_EVENT_NAME, 109, EntityDataFormat.STRING).insert(EntityDataTypes.FALL_DAMAGE_MULTIPLIER, 110, EntityDataFormat.FLOAT).insert(EntityDataTypes.NAME_RAW_TEXT, 111, EntityDataFormat.STRING).insert(EntityDataTypes.CAN_RIDE_TARGET, 112, EntityDataFormat.BYTE, BooleanTransformer.INSTANCE).build();
    protected static final TypeMap<CommandParam> COMMAND_PARAMS = Bedrock_v361.COMMAND_PARAMS.toBuilder().shift(27, 2).shift(31, 7).insert(37, (int) CommandParam.BLOCK_POSITION).shift(46, 1).build();
    protected static final TypeMap<EntityEventType> ENTITY_EVENTS = Bedrock_v361.ENTITY_EVENTS.toBuilder().insert(74, (int) EntityEventType.FINISHED_CHARGING_ITEM).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v361.LEVEL_EVENTS.toBuilder().insert(2024, (int) LevelEvent.PARTICLE_POINT_CLOUD).insert(2025, (int) LevelEvent.PARTICLE_EXPLOSION).insert(2026, (int) LevelEvent.PARTICLE_BLOCK_EXPLOSION).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v361.SOUND_EVENTS.toBuilder().replace(bl.dK, SoundEvent.AMBIENT_IN_RAID).insert(bl.dL, (int) SoundEvent.UI_CARTOGRAPHY_TABLE_USE).insert(bl.dM, (int) SoundEvent.UI_STONECUTTER_USE).insert(bl.dN, (int) SoundEvent.UI_LOOM_USE).insert(bl.dO, (int) SoundEvent.SMOKER_USE).insert(bl.dP, (int) SoundEvent.BLAST_FURNACE_USE).insert(bl.dQ, (int) SoundEvent.SMITHING_TABLE_USE).insert(bl.dR, (int) SoundEvent.SCREECH).insert(bl.dS, (int) SoundEvent.SLEEP).insert(bl.dT, (int) SoundEvent.FURNACE_USE).insert(bl.dU, (int) SoundEvent.MOOSHROOM_CONVERT).insert(bl.dV, (int) SoundEvent.MILK_SUSPICIOUSLY).insert(bl.dW, (int) SoundEvent.CELEBRATE).insert(bl.dX, (int) SoundEvent.UNDEFINED).build();
    protected static final TypeMap<ResourcePackType> RESOURCE_PACK_TYPES = Bedrock_v361.RESOURCE_PACK_TYPES.toBuilder().replace(1, ResourcePackType.ADDON).replace(2, ResourcePackType.CACHED).replace(3, ResourcePackType.COPY_PROTECTED).replace(4, ResourcePackType.DATA_ADD_ON).replace(5, ResourcePackType.PERSONA_PIECE).replace(6, ResourcePackType.RESOURCES).replace(7, ResourcePackType.SKINS).insert(8, (int) ResourcePackType.WORLD_TEMPLATE).build();
    public static final BedrockCodec CODEC = Bedrock_v361.CODEC.toBuilder().protocolVersion(388).minecraftVersion("1.13.0").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.Bedrock_v388$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v388.lambda$static$0();
        }
    }).deregisterPacket(ExplodePacket.class).updateSerializer(ResourcePackDataInfoPacket.class, new ResourcePackDataInfoSerializer_v361(RESOURCE_PACK_TYPES)).updateSerializer(ResourcePackStackPacket.class, ResourcePackStackSerializer_v388.INSTANCE).updateSerializer(StartGamePacket.class, StartGameSerializer_v388.INSTANCE).updateSerializer(AddPlayerPacket.class, AddPlayerSerializer_v388.INSTANCE).updateSerializer(InteractPacket.class, InteractSerializer_v388.INSTANCE).updateSerializer(RespawnPacket.class, RespawnSerializer_v388.INSTANCE).updateSerializer(CraftingDataPacket.class, CraftingDataSerializer_v388.INSTANCE).updateSerializer(PlayerListPacket.class, PlayerListSerializer_v388.INSTANCE).updateSerializer(EventPacket.class, EventSerializer_v388.INSTANCE).updateSerializer(AvailableCommandsPacket.class, new AvailableCommandsSerializer_v388(COMMAND_PARAMS)).updateSerializer(ResourcePackChunkDataPacket.class, ResourcePackChunkDataSerializer_v388.INSTANCE).updateSerializer(StructureBlockUpdatePacket.class, StructureBlockUpdateSerializer_v388.INSTANCE).updateSerializer(PlayerSkinPacket.class, PlayerSkinSerializer_v388.INSTANCE).updateSerializer(MoveEntityDeltaPacket.class, MoveEntityDeltaSerializer_v388.INSTANCE).updateSerializer(StructureTemplateDataResponsePacket.class, StructureTemplateDataResponseSerializer_v388.INSTANCE).updateSerializer(EntityEventPacket.class, new EntityEventSerializer_v291(ENTITY_EVENTS)).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).updateSerializer(LevelEventGenericPacket.class, new LevelEventGenericSerializer_v361(LEVEL_EVENTS)).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.Bedrock_v388$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new TickSyncPacket();
        }
    }, TickSyncSerializer_v388.INSTANCE, 23).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.Bedrock_v388$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new EducationSettingsPacket();
        }
    }, EducationSettingsSerializer_v388.INSTANCE, bl.by).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.Bedrock_v388$$ExternalSyntheticLambda3
        @Override // java.util.function.Supplier
        public final Object get() {
            return new EmotePacket();
        }
    }, EmoteSerializer_v388.INSTANCE, bl.bz).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.Bedrock_v388$$ExternalSyntheticLambda4
        @Override // java.util.function.Supplier
        public final Object get() {
            return new MultiplayerSettingsPacket();
        }
    }, MultiplayerSettingsSerializer_v388.INSTANCE, bl.bA).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.Bedrock_v388$$ExternalSyntheticLambda5
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SettingsCommandPacket();
        }
    }, SettingsCommandSerializer_v388.INSTANCE, bl.bB).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.Bedrock_v388$$ExternalSyntheticLambda6
        @Override // java.util.function.Supplier
        public final Object get() {
            return new AnvilDamagePacket();
        }
    }, AnvilDamageSerializer_v388.INSTANCE, bl.bC).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.Bedrock_v388$$ExternalSyntheticLambda7
        @Override // java.util.function.Supplier
        public final Object get() {
            return new CompletedUsingItemPacket();
        }
    }, CompletedUsingItemSerializer_v388.INSTANCE, bl.bD).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.Bedrock_v388$$ExternalSyntheticLambda8
        @Override // java.util.function.Supplier
        public final Object get() {
            return new NetworkSettingsPacket();
        }
    }, NetworkSettingsSerializer_v388.INSTANCE, bl.bE).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.Bedrock_v388$$ExternalSyntheticLambda9
        @Override // java.util.function.Supplier
        public final Object get() {
            return new PlayerAuthInputPacket();
        }
    }, PlayerAuthInputSerializer_v388.INSTANCE, bl.bF).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v388(ENTITY_DATA, GAME_RULE_TYPES);
    }
}
