package org.cloudburstmc.protocol.bedrock.codec.v407;

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
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.LevelEventGenericSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v390.Bedrock_v390;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.CodeBuilderSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.CraftingDataSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.CreativeContentSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.DebugInfoSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.EducationSettingsSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.EmoteListSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.HurtArmorSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.InventoryContentSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.InventorySlotSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.InventoryTransactionSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.ItemStackRequestSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.ItemStackResponseSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.PacketViolationWarningSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.PlayerArmorDamageSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.PlayerEnchantOptionsSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.PositionTrackingDBClientRequestSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.PositionTrackingDBServerBroadcastSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.SetSpawnPositionSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.StartGameSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.UpdatePlayerGameTypeSerializer_v407;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.ParticleType;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityEventType;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.bedrock.packet.CodeBuilderPacket;
import org.cloudburstmc.protocol.bedrock.packet.CraftingDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.CreativeContentPacket;
import org.cloudburstmc.protocol.bedrock.packet.DebugInfoPacket;
import org.cloudburstmc.protocol.bedrock.packet.EducationSettingsPacket;
import org.cloudburstmc.protocol.bedrock.packet.EmoteListPacket;
import org.cloudburstmc.protocol.bedrock.packet.EntityEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.HurtArmorPacket;
import org.cloudburstmc.protocol.bedrock.packet.InventoryContentPacket;
import org.cloudburstmc.protocol.bedrock.packet.InventorySlotPacket;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;
import org.cloudburstmc.protocol.bedrock.packet.ItemStackRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ItemStackResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventGenericPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.PacketViolationWarningPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerArmorDamagePacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerEnchantOptionsPacket;
import org.cloudburstmc.protocol.bedrock.packet.PositionTrackingDBClientRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.PositionTrackingDBServerBroadcastPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetSpawnPositionPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdatePlayerGameTypePacket;
import org.cloudburstmc.protocol.bedrock.packet.VideoStreamConnectPacket;
import org.cloudburstmc.protocol.bedrock.transformer.BooleanTransformer;
import org.cloudburstmc.protocol.bedrock.transformer.FlagTransformer;
import org.cloudburstmc.protocol.bedrock.transformer.TypeMapTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v407 extends Bedrock_v390 {
    protected static final TypeMap<EntityFlag> ENTITY_FLAGS = Bedrock_v390.ENTITY_FLAGS.toBuilder().shift(86, 1).insert(86, (int) EntityFlag.IS_AVOIDING_BLOCK).shift(93, 2).insert(93, (int) EntityFlag.ADMIRING).insert(94, (int) EntityFlag.CELEBRATING_SPECIAL).build();
    protected static final TypeMap<ParticleType> PARTICLE_TYPES = Bedrock_v390.PARTICLE_TYPES.toBuilder().insert(68, (int) ParticleType.BLUE_FLAME).insert(69, (int) ParticleType.SOUL).insert(70, (int) ParticleType.OBSIDIAN_TEAR).build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v390.ENTITY_DATA.toBuilder().update(EntityDataTypes.FLAGS, new FlagTransformer(ENTITY_FLAGS, 0)).update(EntityDataTypes.FLAGS_2, new FlagTransformer(ENTITY_FLAGS, 1)).update(EntityDataTypes.AREA_EFFECT_CLOUD_PARTICLE, new TypeMapTransformer(PARTICLE_TYPES)).insert(EntityDataTypes.LOW_TIER_CURED_TRADE_DISCOUNT, 113, EntityDataFormat.INT).insert(EntityDataTypes.HIGH_TIER_CURED_TRADE_DISCOUNT, 114, EntityDataFormat.INT).insert(EntityDataTypes.NEARBY_CURED_TRADE_DISCOUNT, 115, EntityDataFormat.INT).insert(EntityDataTypes.NEARBY_CURED_DISCOUNT_TIME_STAMP, 116, EntityDataFormat.INT).insert(EntityDataTypes.HITBOX, 117, EntityDataFormat.NBT).insert(EntityDataTypes.IS_BUOYANT, 118, EntityDataFormat.BYTE, BooleanTransformer.INSTANCE).insert(EntityDataTypes.BUOYANCY_DATA, 119, EntityDataFormat.STRING).build();
    protected static final TypeMap<EntityEventType> ENTITY_EVENTS = Bedrock_v390.ENTITY_EVENTS.toBuilder().insert(75, (int) EntityEventType.LANDED_ON_GROUND).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v390.LEVEL_EVENTS.toBuilder().insert(1050, (int) LevelEvent.SOUND_CAMERA).insert(3600, (int) LevelEvent.BLOCK_START_BREAK).insert(3601, (int) LevelEvent.BLOCK_STOP_BREAK).insert(3602, (int) LevelEvent.BLOCK_UPDATE_BREAK).insert(4000, (int) LevelEvent.SET_DATA).insert(9800, (int) LevelEvent.ALL_PLAYERS_SLEEPING).insert(16384, PARTICLE_TYPES).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v390.SOUND_EVENTS.toBuilder().replace(bl.dX, SoundEvent.JUMP_PREVENT).insert(bl.dY, (int) SoundEvent.AMBIENT_POLLINATE).insert(bl.dZ, (int) SoundEvent.BEEHIVE_DRIP).insert(bl.d0, (int) SoundEvent.BEEHIVE_ENTER).insert(bl.d1, (int) SoundEvent.BEEHIVE_EXIT).insert(bl.d2, (int) SoundEvent.BEEHIVE_WORK).insert(bl.d3, (int) SoundEvent.BEEHIVE_SHEAR).insert(bl.d4, (int) SoundEvent.HONEYBOTTLE_DRINK).insert(bl.d5, (int) SoundEvent.AMBIENT_CAVE).insert(bl.d6, (int) SoundEvent.RETREAT).insert(bl.d7, (int) SoundEvent.CONVERT_TO_ZOMBIFIED).insert(bl.d8, (int) SoundEvent.ADMIRE).insert(bl.d9, (int) SoundEvent.STEP_LAVA).insert(300, (int) SoundEvent.TEMPT).insert(bl.ea, (int) SoundEvent.PANIC).insert(bl.eb, (int) SoundEvent.ANGRY).insert(bl.ec, (int) SoundEvent.AMBIENT_WARPED_FOREST).insert(bl.ed, (int) SoundEvent.AMBIENT_SOULSAND_VALLEY).insert(bl.ee, (int) SoundEvent.AMBIENT_NETHER_WASTES).insert(bl.ef, (int) SoundEvent.AMBIENT_BASALT_DELTAS).insert(307, (int) SoundEvent.AMBIENT_CRIMSON_FOREST).insert(308, (int) SoundEvent.RESPAWN_ANCHOR_CHARGE).insert(bl.ei, (int) SoundEvent.RESPAWN_ANCHOR_DEPLETE).insert(bl.ej, (int) SoundEvent.RESPAWN_ANCHOR_SET_SPAWN).insert(bl.ek, (int) SoundEvent.RESPAWN_ANCHOR_AMBIENT).insert(bl.el, (int) SoundEvent.SOUL_ESCAPE_QUIET).insert(bl.em, (int) SoundEvent.SOUL_ESCAPE_LOUD).insert(bl.en, (int) SoundEvent.RECORD_PIGSTEP).insert(bl.eo, (int) SoundEvent.LINK_COMPASS_TO_LODESTONE).insert(bl.ep, (int) SoundEvent.USE_SMITHING_TABLE).insert(317, (int) SoundEvent.UNDEFINED).build();
    protected static final TypeMap<ItemStackRequestActionType> ITEM_STACK_REQUEST_TYPES = TypeMap.builder(ItemStackRequestActionType.class).insert(0, (int) ItemStackRequestActionType.TAKE).insert(1, (int) ItemStackRequestActionType.PLACE).insert(2, (int) ItemStackRequestActionType.SWAP).insert(3, (int) ItemStackRequestActionType.DROP).insert(4, (int) ItemStackRequestActionType.DESTROY).insert(5, (int) ItemStackRequestActionType.CONSUME).insert(6, (int) ItemStackRequestActionType.CREATE).insert(7, (int) ItemStackRequestActionType.LAB_TABLE_COMBINE).insert(8, (int) ItemStackRequestActionType.BEACON_PAYMENT).insert(9, (int) ItemStackRequestActionType.CRAFT_RECIPE).insert(10, (int) ItemStackRequestActionType.CRAFT_RECIPE_AUTO).insert(11, (int) ItemStackRequestActionType.CRAFT_CREATIVE).insert(12, (int) ItemStackRequestActionType.CRAFT_NON_IMPLEMENTED_DEPRECATED).insert(13, (int) ItemStackRequestActionType.CRAFT_RESULTS_DEPRECATED).build();
    protected static final TypeMap<ContainerSlotType> CONTAINER_SLOT_TYPES = TypeMap.builder(ContainerSlotType.class).insert(0, (int) ContainerSlotType.ANVIL_INPUT).insert(1, (int) ContainerSlotType.ANVIL_MATERIAL).insert(2, (int) ContainerSlotType.ANVIL_RESULT).insert(3, (int) ContainerSlotType.SMITHING_TABLE_INPUT).insert(4, (int) ContainerSlotType.SMITHING_TABLE_MATERIAL).insert(5, (int) ContainerSlotType.SMITHING_TABLE_RESULT).insert(6, (int) ContainerSlotType.ARMOR).insert(7, (int) ContainerSlotType.LEVEL_ENTITY).insert(8, (int) ContainerSlotType.BEACON_PAYMENT).insert(9, (int) ContainerSlotType.BREWING_INPUT).insert(10, (int) ContainerSlotType.BREWING_RESULT).insert(11, (int) ContainerSlotType.BREWING_FUEL).insert(12, (int) ContainerSlotType.HOTBAR_AND_INVENTORY).insert(13, (int) ContainerSlotType.CRAFTING_INPUT).insert(14, (int) ContainerSlotType.CRAFTING_OUTPUT).insert(15, (int) ContainerSlotType.RECIPE_CONSTRUCTION).insert(16, (int) ContainerSlotType.RECIPE_NATURE).insert(17, (int) ContainerSlotType.RECIPE_ITEMS).insert(18, (int) ContainerSlotType.RECIPE_SEARCH).insert(19, (int) ContainerSlotType.RECIPE_SEARCH_BAR).insert(20, (int) ContainerSlotType.RECIPE_EQUIPMENT).insert(21, (int) ContainerSlotType.ENCHANTING_INPUT).insert(22, (int) ContainerSlotType.ENCHANTING_MATERIAL).insert(23, (int) ContainerSlotType.FURNACE_FUEL).insert(24, (int) ContainerSlotType.FURNACE_INGREDIENT).insert(25, (int) ContainerSlotType.FURNACE_RESULT).insert(26, (int) ContainerSlotType.HORSE_EQUIP).insert(27, (int) ContainerSlotType.HOTBAR).insert(28, (int) ContainerSlotType.INVENTORY).insert(29, (int) ContainerSlotType.SHULKER_BOX).insert(30, (int) ContainerSlotType.TRADE_INGREDIENT_1).insert(31, (int) ContainerSlotType.TRADE_INGREDIENT_2).insert(32, (int) ContainerSlotType.TRADE_RESULT).insert(33, (int) ContainerSlotType.OFFHAND).insert(34, (int) ContainerSlotType.COMPOUND_CREATOR_INPUT).insert(35, (int) ContainerSlotType.COMPOUND_CREATOR_OUTPUT).insert(36, (int) ContainerSlotType.ELEMENT_CONSTRUCTOR_OUTPUT).insert(37, (int) ContainerSlotType.MATERIAL_REDUCER_INPUT).insert(38, (int) ContainerSlotType.MATERIAL_REDUCER_OUTPUT).insert(39, (int) ContainerSlotType.LAB_TABLE_INPUT).insert(40, (int) ContainerSlotType.LOOM_INPUT).insert(41, (int) ContainerSlotType.LOOM_DYE).insert(42, (int) ContainerSlotType.LOOM_MATERIAL).insert(43, (int) ContainerSlotType.LOOM_RESULT).insert(44, (int) ContainerSlotType.BLAST_FURNACE_INGREDIENT).insert(45, (int) ContainerSlotType.SMOKER_INGREDIENT).insert(46, (int) ContainerSlotType.TRADE2_INGREDIENT_1).insert(47, (int) ContainerSlotType.TRADE2_INGREDIENT_2).insert(48, (int) ContainerSlotType.TRADE2_RESULT).insert(49, (int) ContainerSlotType.GRINDSTONE_INPUT).insert(50, (int) ContainerSlotType.GRINDSTONE_ADDITIONAL).insert(51, (int) ContainerSlotType.GRINDSTONE_RESULT).insert(52, (int) ContainerSlotType.STONECUTTER_INPUT).insert(53, (int) ContainerSlotType.STONECUTTER_RESULT).insert(54, (int) ContainerSlotType.CARTOGRAPHY_INPUT).insert(55, (int) ContainerSlotType.CARTOGRAPHY_ADDITIONAL).insert(56, (int) ContainerSlotType.CARTOGRAPHY_RESULT).insert(57, (int) ContainerSlotType.BARREL).insert(58, (int) ContainerSlotType.CURSOR).insert(59, (int) ContainerSlotType.CREATED_OUTPUT).build();
    public static BedrockCodec CODEC = Bedrock_v390.CODEC.toBuilder().protocolVersion(407).minecraftVersion("1.16.0").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.Bedrock_v407$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v407.lambda$static$0();
        }
    }).deregisterPacket(VideoStreamConnectPacket.class).updateSerializer(StartGamePacket.class, StartGameSerializer_v407.INSTANCE).updateSerializer(InventoryTransactionPacket.class, InventoryTransactionSerializer_v407.INSTANCE).updateSerializer(HurtArmorPacket.class, HurtArmorSerializer_v407.INSTANCE).updateSerializer(SetSpawnPositionPacket.class, SetSpawnPositionSerializer_v407.INSTANCE).updateSerializer(InventoryContentPacket.class, InventoryContentSerializer_v407.INSTANCE).updateSerializer(InventorySlotPacket.class, InventorySlotSerializer_v407.INSTANCE).updateSerializer(CraftingDataPacket.class, CraftingDataSerializer_v407.INSTANCE).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).updateSerializer(EntityEventPacket.class, new EntityEventSerializer_v291(ENTITY_EVENTS)).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).updateSerializer(LevelEventGenericPacket.class, new LevelEventGenericSerializer_v361(LEVEL_EVENTS)).updateSerializer(EducationSettingsPacket.class, EducationSettingsSerializer_v407.INSTANCE).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.Bedrock_v407$$ExternalSyntheticLambda7
        @Override // java.util.function.Supplier
        public final Object get() {
            return new CreativeContentPacket();
        }
    }, CreativeContentSerializer_v407.INSTANCE, bl.bG).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.Bedrock_v407$$ExternalSyntheticLambda8
        @Override // java.util.function.Supplier
        public final Object get() {
            return new PlayerEnchantOptionsPacket();
        }
    }, PlayerEnchantOptionsSerializer_v407.INSTANCE, bl.bH).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.Bedrock_v407$$ExternalSyntheticLambda9
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ItemStackRequestPacket();
        }
    }, ItemStackRequestSerializer_v407.INSTANCE, bl.bI).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.Bedrock_v407$$ExternalSyntheticLambda10
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ItemStackResponsePacket();
        }
    }, ItemStackResponseSerializer_v407.INSTANCE, bl.bJ).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.Bedrock_v407$$ExternalSyntheticLambda11
        @Override // java.util.function.Supplier
        public final Object get() {
            return new PlayerArmorDamagePacket();
        }
    }, PlayerArmorDamageSerializer_v407.INSTANCE, bl.bK).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.Bedrock_v407$$ExternalSyntheticLambda12
        @Override // java.util.function.Supplier
        public final Object get() {
            return new CodeBuilderPacket();
        }
    }, CodeBuilderSerializer_v407.INSTANCE, bl.bL).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.Bedrock_v407$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new UpdatePlayerGameTypePacket();
        }
    }, UpdatePlayerGameTypeSerializer_v407.INSTANCE, bl.bM).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.Bedrock_v407$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new EmoteListPacket();
        }
    }, EmoteListSerializer_v407.INSTANCE, bl.bN).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.Bedrock_v407$$ExternalSyntheticLambda3
        @Override // java.util.function.Supplier
        public final Object get() {
            return new PositionTrackingDBServerBroadcastPacket();
        }
    }, PositionTrackingDBServerBroadcastSerializer_v407.INSTANCE, bl.bO).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.Bedrock_v407$$ExternalSyntheticLambda4
        @Override // java.util.function.Supplier
        public final Object get() {
            return new PositionTrackingDBClientRequestPacket();
        }
    }, PositionTrackingDBClientRequestSerializer_v407.INSTANCE, bl.bP).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.Bedrock_v407$$ExternalSyntheticLambda5
        @Override // java.util.function.Supplier
        public final Object get() {
            return new DebugInfoPacket();
        }
    }, DebugInfoSerializer_v407.INSTANCE, bl.bQ).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.Bedrock_v407$$ExternalSyntheticLambda6
        @Override // java.util.function.Supplier
        public final Object get() {
            return new PacketViolationWarningPacket();
        }
    }, PacketViolationWarningSerializer_v407.INSTANCE, bl.bR).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v407(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES);
    }
}
