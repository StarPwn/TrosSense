package org.cloudburstmc.protocol.bedrock.codec.v291;

import androidx.core.view.PointerIconCompat;
import com.trossense.bl;
import java.util.function.Supplier;
import okhttp3.internal.ws.WebSocketProtocol;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.compat.BedrockCompat$$ExternalSyntheticLambda1;
import org.cloudburstmc.protocol.bedrock.codec.compat.BedrockCompat$$ExternalSyntheticLambda2;
import org.cloudburstmc.protocol.bedrock.codec.compat.BedrockCompat$$ExternalSyntheticLambda3;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddBehaviorTreeSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddEntitySerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddHangingEntitySerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddItemEntitySerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddPaintingSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddPlayerSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AdventureSettingsSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AnimateSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AutomationClientConnectSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.BlockEntityDataSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.BlockEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.BlockPickRequestSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.BookEditSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.BossEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CameraSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ChangeDimensionSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ChunkRadiusUpdatedSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ClientToServerHandshakeSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ClientboundMapItemDataSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CommandBlockUpdateSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CommandOutputSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CommandRequestSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ContainerCloseSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ContainerOpenSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ContainerSetDataSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.DisconnectSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EntityEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EntityFallSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EntityPickRequestSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ExplodeSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.FullChunkDataSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.GameRulesChangedSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.GuiDataPickItemSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.HurtArmorSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.InteractSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.InventoryContentSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.InventorySlotSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.InventoryTransactionSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ItemFrameDropItemSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LabTableSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelSoundEvent1Serializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LoginSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MapInfoRequestSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MobArmorEquipmentSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MobEffectSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MobEquipmentSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ModalFormRequestSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ModalFormResponseSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityAbsoluteSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityDeltaSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MovePlayerSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.NetworkStackLatencySerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.NpcRequestSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.PhotoTransferSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.PlaySoundSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.PlayStatusSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.PlayerActionSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.PlayerHotbarSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.PlayerInputSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.PlayerListSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.PlayerSkinSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.PurchaseReceiptSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.RemoveEntitySerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.RemoveObjectiveSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.RequestChunkRadiusSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePackChunkDataSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePackChunkRequestSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePackClientResponseSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePackDataInfoSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePackStackSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePacksInfoSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.RespawnSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.RiderJumpSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ScriptCustomEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ServerSettingsRequestSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ServerSettingsResponseSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ServerToClientHandshakeSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetCommandsEnabledSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetDefaultGameTypeSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetDifficultySerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetDisplayObjectiveSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetEntityDataSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetEntityLinkSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetEntityMotionSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetHealthSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetLastHurtBySerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetLocalPlayerAsInitializedSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetPlayerGameTypeSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetScoreSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetScoreboardIdentitySerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetSpawnPositionSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetTimeSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetTitleSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ShowCreditsSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ShowProfileSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ShowStoreOfferSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SimpleEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SpawnExperienceOrbSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.StartGameSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.StopSoundSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.StructureBlockUpdateSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SubClientLoginSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.TakeItemEntitySerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.TextSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.TransferSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.UpdateAttributesSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.UpdateBlockSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.UpdateBlockSyncedSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.UpdateEquipSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.UpdateSoftEnumSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.UpdateTradeSerializer_v291;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.ParticleType;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityEventType;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.AddBehaviorTreePacket;
import org.cloudburstmc.protocol.bedrock.packet.AddEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.AddHangingEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.AddItemEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.AddPaintingPacket;
import org.cloudburstmc.protocol.bedrock.packet.AddPlayerPacket;
import org.cloudburstmc.protocol.bedrock.packet.AdventureSettingsPacket;
import org.cloudburstmc.protocol.bedrock.packet.AnimatePacket;
import org.cloudburstmc.protocol.bedrock.packet.AutomationClientConnectPacket;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.bedrock.packet.BlockEntityDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.BlockEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.BlockPickRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.BookEditPacket;
import org.cloudburstmc.protocol.bedrock.packet.BossEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.CameraPacket;
import org.cloudburstmc.protocol.bedrock.packet.ChangeDimensionPacket;
import org.cloudburstmc.protocol.bedrock.packet.ChunkRadiusUpdatedPacket;
import org.cloudburstmc.protocol.bedrock.packet.ClientToServerHandshakePacket;
import org.cloudburstmc.protocol.bedrock.packet.ClientboundMapItemDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.CommandBlockUpdatePacket;
import org.cloudburstmc.protocol.bedrock.packet.CommandOutputPacket;
import org.cloudburstmc.protocol.bedrock.packet.CommandRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ContainerClosePacket;
import org.cloudburstmc.protocol.bedrock.packet.ContainerOpenPacket;
import org.cloudburstmc.protocol.bedrock.packet.ContainerSetDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.CraftingDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.CraftingEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.EntityEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.EntityFallPacket;
import org.cloudburstmc.protocol.bedrock.packet.EntityPickRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.EventPacket;
import org.cloudburstmc.protocol.bedrock.packet.ExplodePacket;
import org.cloudburstmc.protocol.bedrock.packet.GameRulesChangedPacket;
import org.cloudburstmc.protocol.bedrock.packet.GuiDataPickItemPacket;
import org.cloudburstmc.protocol.bedrock.packet.HurtArmorPacket;
import org.cloudburstmc.protocol.bedrock.packet.InteractPacket;
import org.cloudburstmc.protocol.bedrock.packet.InventoryContentPacket;
import org.cloudburstmc.protocol.bedrock.packet.InventorySlotPacket;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;
import org.cloudburstmc.protocol.bedrock.packet.ItemFrameDropItemPacket;
import org.cloudburstmc.protocol.bedrock.packet.LabTablePacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.MapInfoRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.MobArmorEquipmentPacket;
import org.cloudburstmc.protocol.bedrock.packet.MobEffectPacket;
import org.cloudburstmc.protocol.bedrock.packet.MobEquipmentPacket;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.MoveEntityAbsolutePacket;
import org.cloudburstmc.protocol.bedrock.packet.MoveEntityDeltaPacket;
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket;
import org.cloudburstmc.protocol.bedrock.packet.NetworkStackLatencyPacket;
import org.cloudburstmc.protocol.bedrock.packet.NpcRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.PhotoTransferPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlaySoundPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerActionPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerHotbarPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerInputPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerListPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerSkinPacket;
import org.cloudburstmc.protocol.bedrock.packet.PurchaseReceiptPacket;
import org.cloudburstmc.protocol.bedrock.packet.RemoveEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.RemoveObjectivePacket;
import org.cloudburstmc.protocol.bedrock.packet.RequestChunkRadiusPacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackChunkDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackChunkRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackClientResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackDataInfoPacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackStackPacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePacksInfoPacket;
import org.cloudburstmc.protocol.bedrock.packet.RespawnPacket;
import org.cloudburstmc.protocol.bedrock.packet.RiderJumpPacket;
import org.cloudburstmc.protocol.bedrock.packet.ScriptCustomEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerToClientHandshakePacket;
import org.cloudburstmc.protocol.bedrock.packet.SetCommandsEnabledPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetDefaultGameTypePacket;
import org.cloudburstmc.protocol.bedrock.packet.SetDifficultyPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetDisplayObjectivePacket;
import org.cloudburstmc.protocol.bedrock.packet.SetEntityDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetEntityLinkPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetEntityMotionPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetHealthPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetLastHurtByPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetLocalPlayerAsInitializedPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetPlayerGameTypePacket;
import org.cloudburstmc.protocol.bedrock.packet.SetScorePacket;
import org.cloudburstmc.protocol.bedrock.packet.SetScoreboardIdentityPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetSpawnPositionPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetTimePacket;
import org.cloudburstmc.protocol.bedrock.packet.SetTitlePacket;
import org.cloudburstmc.protocol.bedrock.packet.ShowCreditsPacket;
import org.cloudburstmc.protocol.bedrock.packet.ShowProfilePacket;
import org.cloudburstmc.protocol.bedrock.packet.ShowStoreOfferPacket;
import org.cloudburstmc.protocol.bedrock.packet.SimpleEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.SpawnExperienceOrbPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.bedrock.packet.StopSoundPacket;
import org.cloudburstmc.protocol.bedrock.packet.StructureBlockUpdatePacket;
import org.cloudburstmc.protocol.bedrock.packet.SubClientLoginPacket;
import org.cloudburstmc.protocol.bedrock.packet.TakeItemEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.TextPacket;
import org.cloudburstmc.protocol.bedrock.packet.TransferPacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateAttributesPacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateBlockSyncedPacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateEquipPacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateSoftEnumPacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateTradePacket;
import org.cloudburstmc.protocol.bedrock.transformer.BlockDefinitionTransformer;
import org.cloudburstmc.protocol.bedrock.transformer.BooleanTransformer;
import org.cloudburstmc.protocol.bedrock.transformer.FlagTransformer;
import org.cloudburstmc.protocol.bedrock.transformer.TypeMapTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v291 {
    protected static final int LEVEL_EVENT_PARTICLE_TYPE = 16384;
    protected static final int LEVEL_EVENT_SOUND = 1000;
    protected static final int LEVEL_EVENT_WORLD = 3000;
    protected static final TypeMap<EntityFlag> ENTITY_FLAGS = TypeMap.builder(EntityFlag.class).insert(0, (int) EntityFlag.ON_FIRE).insert(1, (int) EntityFlag.SNEAKING).insert(2, (int) EntityFlag.RIDING).insert(3, (int) EntityFlag.SPRINTING).insert(4, (int) EntityFlag.USING_ITEM).insert(5, (int) EntityFlag.INVISIBLE).insert(6, (int) EntityFlag.TEMPTED).insert(7, (int) EntityFlag.IN_LOVE).insert(8, (int) EntityFlag.SADDLED).insert(9, (int) EntityFlag.POWERED).insert(10, (int) EntityFlag.IGNITED).insert(11, (int) EntityFlag.BABY).insert(12, (int) EntityFlag.CONVERTING).insert(13, (int) EntityFlag.CRITICAL).insert(14, (int) EntityFlag.CAN_SHOW_NAME).insert(15, (int) EntityFlag.ALWAYS_SHOW_NAME).insert(16, (int) EntityFlag.NO_AI).insert(17, (int) EntityFlag.SILENT).insert(18, (int) EntityFlag.WALL_CLIMBING).insert(19, (int) EntityFlag.CAN_CLIMB).insert(20, (int) EntityFlag.CAN_SWIM).insert(21, (int) EntityFlag.CAN_FLY).insert(22, (int) EntityFlag.CAN_WALK).insert(23, (int) EntityFlag.RESTING).insert(24, (int) EntityFlag.SITTING).insert(25, (int) EntityFlag.ANGRY).insert(26, (int) EntityFlag.INTERESTED).insert(27, (int) EntityFlag.CHARGED).insert(28, (int) EntityFlag.TAMED).insert(29, (int) EntityFlag.ORPHANED).insert(30, (int) EntityFlag.LEASHED).insert(31, (int) EntityFlag.SHEARED).insert(32, (int) EntityFlag.GLIDING).insert(33, (int) EntityFlag.ELDER).insert(34, (int) EntityFlag.MOVING).insert(35, (int) EntityFlag.BREATHING).insert(36, (int) EntityFlag.CHESTED).insert(37, (int) EntityFlag.STACKABLE).insert(38, (int) EntityFlag.SHOW_BOTTOM).insert(39, (int) EntityFlag.STANDING).insert(40, (int) EntityFlag.SHAKING).insert(41, (int) EntityFlag.IDLING).insert(42, (int) EntityFlag.CASTING).insert(43, (int) EntityFlag.CHARGING).insert(44, (int) EntityFlag.WASD_CONTROLLED).insert(45, (int) EntityFlag.CAN_POWER_JUMP).insert(46, (int) EntityFlag.LINGERING).insert(47, (int) EntityFlag.HAS_COLLISION).insert(48, (int) EntityFlag.HAS_GRAVITY).insert(49, (int) EntityFlag.FIRE_IMMUNE).insert(50, (int) EntityFlag.DANCING).insert(51, (int) EntityFlag.ENCHANTED).insert(52, (int) EntityFlag.RETURN_TRIDENT).insert(53, (int) EntityFlag.CONTAINER_IS_PRIVATE).insert(54, (int) EntityFlag.IS_TRANSFORMING).insert(55, (int) EntityFlag.DAMAGE_NEARBY_MOBS).insert(56, (int) EntityFlag.SWIMMING).insert(57, (int) EntityFlag.BRIBED).insert(58, (int) EntityFlag.IS_PREGNANT).insert(59, (int) EntityFlag.LAYING_EGG).insert(60, (int) EntityFlag.RIDER_CAN_PICK).build();
    protected static final TypeMap<ParticleType> PARTICLE_TYPES = TypeMap.builder(ParticleType.class).insert(0, (int) ParticleType.UNDEFINED).insert(1, (int) ParticleType.BUBBLE).insert(2, (int) ParticleType.CRIT).insert(3, (int) ParticleType.BLOCK_FORCE_FIELD).insert(4, (int) ParticleType.SMOKE).insert(5, (int) ParticleType.EXPLODE).insert(6, (int) ParticleType.EVAPORATION).insert(7, (int) ParticleType.FLAME).insert(8, (int) ParticleType.LAVA).insert(9, (int) ParticleType.LARGE_SMOKE).insert(10, (int) ParticleType.RED_DUST).insert(11, (int) ParticleType.RISING_BORDER_DUST).insert(12, (int) ParticleType.ICON_CRACK).insert(13, (int) ParticleType.SNOWBALL_POOF).insert(14, (int) ParticleType.LARGE_EXPLODE).insert(15, (int) ParticleType.HUGE_EXPLOSION).insert(16, (int) ParticleType.MOB_FLAME).insert(17, (int) ParticleType.HEART).insert(18, (int) ParticleType.TERRAIN).insert(19, (int) ParticleType.TOWN_AURA).insert(20, (int) ParticleType.PORTAL).insert(21, (int) ParticleType.WATER_SPLASH).insert(22, (int) ParticleType.WATER_WAKE).insert(23, (int) ParticleType.DRIP_WATER).insert(24, (int) ParticleType.DRIP_LAVA).insert(25, (int) ParticleType.FALLING_DUST).insert(26, (int) ParticleType.MOB_SPELL).insert(27, (int) ParticleType.MOB_SPELL_AMBIENT).insert(28, (int) ParticleType.MOB_SPELL_INSTANTANEOUS).insert(29, (int) ParticleType.INK).insert(30, (int) ParticleType.SLIME).insert(31, (int) ParticleType.RAIN_SPLASH).insert(32, (int) ParticleType.VILLAGER_ANGRY).insert(33, (int) ParticleType.VILLAGER_HAPPY).insert(34, (int) ParticleType.ENCHANTING_TABLE).insert(35, (int) ParticleType.TRACKER_EMITTER).insert(36, (int) ParticleType.NOTE).insert(37, (int) ParticleType.WITCH_SPELL).insert(38, (int) ParticleType.CARROT_BOOST).insert(39, (int) ParticleType.MOB_APPEARANCE).insert(40, (int) ParticleType.END_ROD).insert(41, (int) ParticleType.DRAGON_BREATH).insert(42, (int) ParticleType.SPIT).insert(43, (int) ParticleType.TOTEM).insert(44, (int) ParticleType.FOOD).build();
    protected static final EntityDataTypeMap ENTITY_DATA = EntityDataTypeMap.builder().insert(EntityDataTypes.FLAGS, 0, EntityDataFormat.LONG, new FlagTransformer(ENTITY_FLAGS, 0)).insert(EntityDataTypes.STRUCTURAL_INTEGRITY, 1, EntityDataFormat.INT).insert(EntityDataTypes.VARIANT, 2, EntityDataFormat.INT).insert(EntityDataTypes.BLOCK, 2, EntityDataFormat.INT, new BlockDefinitionTransformer()).insert(EntityDataTypes.COLOR, 3, EntityDataFormat.BYTE).insert(EntityDataTypes.NAME, 4, EntityDataFormat.STRING).insert(EntityDataTypes.OWNER_EID, 5, EntityDataFormat.LONG).insert(EntityDataTypes.TARGET_EID, 6, EntityDataFormat.LONG).insert(EntityDataTypes.AIR_SUPPLY, 7, EntityDataFormat.SHORT).insert(EntityDataTypes.EFFECT_COLOR, 8, EntityDataFormat.INT).insert(EntityDataTypes.EFFECT_AMBIENCE, 9, EntityDataFormat.BYTE).insert(EntityDataTypes.JUMP_DURATION, 10, EntityDataFormat.BYTE).insert(EntityDataTypes.HURT_TICKS, 11, EntityDataFormat.INT).insert(EntityDataTypes.HURT_DIRECTION, 12, EntityDataFormat.INT).insert(EntityDataTypes.ROW_TIME_LEFT, 13, EntityDataFormat.FLOAT).insert(EntityDataTypes.ROW_TIME_RIGHT, 14, EntityDataFormat.FLOAT).insert(EntityDataTypes.VALUE, 15, EntityDataFormat.INT).insert(EntityDataTypes.DISPLAY_BLOCK_STATE, 16, EntityDataFormat.INT, new BlockDefinitionTransformer()).insert(EntityDataTypes.DISPLAY_FIREWORK, 16, EntityDataFormat.NBT).insert(EntityDataTypes.HORSE_FLAGS, 16, EntityDataFormat.INT).insert(EntityDataTypes.WITHER_SKULL_DANGEROUS, 16, EntityDataFormat.BYTE).insert(EntityDataTypes.DISPLAY_OFFSET, 17, EntityDataFormat.INT).insert(EntityDataTypes.CUSTOM_DISPLAY, 18, EntityDataFormat.BYTE).insert(EntityDataTypes.HORSE_TYPE, 19, EntityDataFormat.BYTE).insert(EntityDataTypes.OLD_SWELL, 20, EntityDataFormat.INT).insert(EntityDataTypes.SWELL_DIRECTION, 21, EntityDataFormat.INT).insert(EntityDataTypes.CHARGE_AMOUNT, 22, EntityDataFormat.BYTE).insert(EntityDataTypes.CARRY_BLOCK_STATE, 23, EntityDataFormat.INT, new BlockDefinitionTransformer()).insert(EntityDataTypes.CLIENT_EVENT, 24, EntityDataFormat.BYTE).insert(EntityDataTypes.USING_ITEM, 25, EntityDataFormat.BYTE, BooleanTransformer.INSTANCE).insert(EntityDataTypes.PLAYER_FLAGS, 26, EntityDataFormat.BYTE).insert(EntityDataTypes.PLAYER_INDEX, 27, EntityDataFormat.INT).insert(EntityDataTypes.BED_POSITION, 28, EntityDataFormat.VECTOR3I).insert(EntityDataTypes.FIREBALL_POWER_X, 29, EntityDataFormat.FLOAT).insert(EntityDataTypes.FIREBALL_POWER_Y, 30, EntityDataFormat.FLOAT).insert(EntityDataTypes.FIREBALL_POWER_Z, 31, EntityDataFormat.FLOAT).insert(EntityDataTypes.AUX_POWER, 32, EntityDataFormat.BYTE).insert(EntityDataTypes.FISH_X, 33, EntityDataFormat.FLOAT).insert(EntityDataTypes.FISH_Z, 34, EntityDataFormat.FLOAT).insert(EntityDataTypes.FISH_ANGLE, 35, EntityDataFormat.FLOAT).insert(EntityDataTypes.AUX_VALUE_DATA, 36, EntityDataFormat.SHORT).insert(EntityDataTypes.LEASH_HOLDER, 37, EntityDataFormat.LONG).insert(EntityDataTypes.SCALE, 38, EntityDataFormat.FLOAT).insert(EntityDataTypes.INTERACT_TEXT, 39, EntityDataFormat.STRING).insert(EntityDataTypes.SKIN_ID, 40, EntityDataFormat.INT).insert(EntityDataTypes.ACTIONS, 41, EntityDataFormat.STRING).insert(EntityDataTypes.AIR_SUPPLY_MAX, 42, EntityDataFormat.SHORT).insert(EntityDataTypes.MARK_VARIANT, 43, EntityDataFormat.INT).insert(EntityDataTypes.CONTAINER_TYPE, 44, EntityDataFormat.BYTE).insert(EntityDataTypes.CONTAINER_SIZE, 45, EntityDataFormat.INT).insert(EntityDataTypes.CONTAINER_STRENGTH_MODIFIER, 46, EntityDataFormat.INT).insert(EntityDataTypes.BLOCK_TARGET_POS, 47, EntityDataFormat.VECTOR3I).insert(EntityDataTypes.WITHER_INVULNERABLE_TICKS, 48, EntityDataFormat.INT).insert(EntityDataTypes.WITHER_TARGET_A, 49, EntityDataFormat.LONG).insert(EntityDataTypes.WITHER_TARGET_B, 50, EntityDataFormat.LONG).insert(EntityDataTypes.WITHER_TARGET_C, 51, EntityDataFormat.LONG).insert(EntityDataTypes.WITHER_AERIAL_ATTACK, 52, EntityDataFormat.SHORT).insert(EntityDataTypes.WIDTH, 53, EntityDataFormat.FLOAT).insert(EntityDataTypes.HEIGHT, 54, EntityDataFormat.FLOAT).insert(EntityDataTypes.FUSE_TIME, 55, EntityDataFormat.INT).insert(EntityDataTypes.SEAT_OFFSET, 56, EntityDataFormat.VECTOR3F).insert(EntityDataTypes.SEAT_LOCK_RIDER_ROTATION, 57, EntityDataFormat.BYTE, BooleanTransformer.INSTANCE).insert(EntityDataTypes.SEAT_LOCK_RIDER_ROTATION_DEGREES, 58, EntityDataFormat.FLOAT).insert(EntityDataTypes.SEAT_HAS_ROTATION, 59, EntityDataFormat.BYTE, BooleanTransformer.INSTANCE).insert(EntityDataTypes.AREA_EFFECT_CLOUD_RADIUS, 60, EntityDataFormat.FLOAT).insert(EntityDataTypes.AREA_EFFECT_CLOUD_WAITING, 61, EntityDataFormat.INT).insert(EntityDataTypes.AREA_EFFECT_CLOUD_PARTICLE, 62, EntityDataFormat.INT, new TypeMapTransformer(PARTICLE_TYPES)).insert(EntityDataTypes.SHULKER_PEEK_AMOUNT, 63, EntityDataFormat.INT).insert(EntityDataTypes.SHULKER_ATTACH_FACE, 64, EntityDataFormat.INT).insert(EntityDataTypes.SHULKER_ATTACHED, 65, EntityDataFormat.BYTE, BooleanTransformer.INSTANCE).insert(EntityDataTypes.SHULKER_ATTACH_POS, 66, EntityDataFormat.VECTOR3I).insert(EntityDataTypes.TRADE_TARGET_EID, 67, EntityDataFormat.LONG).insert(EntityDataTypes.CAREER, 68, EntityDataFormat.INT).insert(EntityDataTypes.COMMAND_BLOCK_ENABLED, 69, EntityDataFormat.BYTE, BooleanTransformer.INSTANCE).insert(EntityDataTypes.COMMAND_BLOCK_NAME, 70, EntityDataFormat.STRING).insert(EntityDataTypes.COMMAND_BLOCK_LAST_OUTPUT, 71, EntityDataFormat.STRING).insert(EntityDataTypes.COMMAND_BLOCK_TRACK_OUTPUT, 72, EntityDataFormat.BYTE, BooleanTransformer.INSTANCE).insert(EntityDataTypes.CONTROLLING_RIDER_SEAT_INDEX, 73, EntityDataFormat.BYTE).insert(EntityDataTypes.STRENGTH, 74, EntityDataFormat.INT).insert(EntityDataTypes.STRENGTH_MAX, 75, EntityDataFormat.INT).insert(EntityDataTypes.EVOKER_SPELL_CASTING_COLOR, 76, EntityDataFormat.INT).insert(EntityDataTypes.DATA_LIFETIME_TICKS, 77, EntityDataFormat.INT).insert(EntityDataTypes.ARMOR_STAND_POSE_INDEX, 78, EntityDataFormat.INT).insert(EntityDataTypes.END_CRYSTAL_TICK_OFFSET, 79, EntityDataFormat.INT).insert(EntityDataTypes.NAMETAG_ALWAYS_SHOW, 80, EntityDataFormat.BYTE).insert(EntityDataTypes.COLOR_2, 81, EntityDataFormat.BYTE).insert(EntityDataTypes.NAME_AUTHOR, 82, EntityDataFormat.STRING).insert(EntityDataTypes.SCORE, 83, EntityDataFormat.STRING).insert(EntityDataTypes.BALLOON_ANCHOR_EID, 84, EntityDataFormat.LONG).insert(EntityDataTypes.PUFFED_STATE, 85, EntityDataFormat.BYTE).insert(EntityDataTypes.BOAT_BUBBLE_TIME, 86, EntityDataFormat.INT).insert(EntityDataTypes.AGENT_EID, 87, EntityDataFormat.LONG).build();
    protected static final TypeMap<Class<?>> GAME_RULE_TYPES = TypeMap.builder("GameRuleType").insert(1, (int) Boolean.class).insert(2, (int) Integer.class).insert(3, (int) Float.class).build();
    protected static final int LEVEL_EVENT_PARTICLE = 2000;
    protected static final int LEVEL_EVENT_BLOCK = 3500;
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = TypeMap.builder(LevelEventType.class).insert(0, (int) LevelEvent.UNDEFINED).insert(1000, (int) LevelEvent.SOUND_CLICK).insert(1001, (int) LevelEvent.SOUND_CLICK_FAIL).insert(PointerIconCompat.TYPE_HAND, (int) LevelEvent.SOUND_LAUNCH).insert(PointerIconCompat.TYPE_HELP, (int) LevelEvent.SOUND_DOOR_OPEN).insert(PointerIconCompat.TYPE_WAIT, (int) LevelEvent.SOUND_FIZZ).insert(WebSocketProtocol.CLOSE_NO_STATUS_CODE, (int) LevelEvent.SOUND_FUSE).insert(PointerIconCompat.TYPE_CELL, (int) LevelEvent.SOUND_PLAY_RECORDING).insert(PointerIconCompat.TYPE_CROSSHAIR, (int) LevelEvent.SOUND_GHAST_WARNING).insert(PointerIconCompat.TYPE_TEXT, (int) LevelEvent.SOUND_GHAST_FIREBALL).insert(PointerIconCompat.TYPE_VERTICAL_TEXT, (int) LevelEvent.SOUND_BLAZE_FIREBALL).insert(PointerIconCompat.TYPE_ALIAS, (int) LevelEvent.SOUND_ZOMBIE_DOOR_BUMP).insert(PointerIconCompat.TYPE_NO_DROP, (int) LevelEvent.SOUND_ZOMBIE_DOOR_CRASH).insert(PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, (int) LevelEvent.SOUND_ZOMBIE_INFECTED).insert(PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW, (int) LevelEvent.SOUND_ZOMBIE_CONVERTED).insert(PointerIconCompat.TYPE_ZOOM_IN, (int) LevelEvent.SOUND_ENDERMAN_TELEPORT).insert(PointerIconCompat.TYPE_GRAB, (int) LevelEvent.SOUND_ANVIL_BROKEN).insert(PointerIconCompat.TYPE_GRABBING, (int) LevelEvent.SOUND_ANVIL_USED).insert(1022, (int) LevelEvent.SOUND_ANVIL_LAND).insert(1030, (int) LevelEvent.SOUND_INFINITY_ARROW_PICKUP).insert(1032, (int) LevelEvent.SOUND_TELEPORT_ENDERPEARL).insert(1040, (int) LevelEvent.SOUND_ITEMFRAME_ITEM_ADD).insert(1041, (int) LevelEvent.SOUND_ITEMFRAME_BREAK).insert(1042, (int) LevelEvent.SOUND_ITEMFRAME_PLACE).insert(1043, (int) LevelEvent.SOUND_ITEMFRAME_ITEM_REMOVE).insert(1044, (int) LevelEvent.SOUND_ITEMFRAME_ITEM_ROTATE).insert(1051, (int) LevelEvent.SOUND_EXPERIENCE_ORB_PICKUP).insert(1052, (int) LevelEvent.SOUND_TOTEM_USED).insert(1060, (int) LevelEvent.SOUND_ARMOR_STAND_BREAK).insert(1061, (int) LevelEvent.SOUND_ARMOR_STAND_HIT).insert(1062, (int) LevelEvent.SOUND_ARMOR_STAND_LAND).insert(1063, (int) LevelEvent.SOUND_ARMOR_STAND_PLACE).insert(LEVEL_EVENT_PARTICLE, (int) LevelEvent.PARTICLE_SHOOT).insert(2001, (int) LevelEvent.PARTICLE_DESTROY_BLOCK).insert(2002, (int) LevelEvent.PARTICLE_POTION_SPLASH).insert(2003, (int) LevelEvent.PARTICLE_EYE_OF_ENDER_DEATH).insert(2004, (int) LevelEvent.PARTICLE_MOB_BLOCK_SPAWN).insert(2005, (int) LevelEvent.PARTICLE_CROP_GROWTH).insert(2006, (int) LevelEvent.PARTICLE_SOUND_GUARDIAN_GHOST).insert(2007, (int) LevelEvent.PARTICLE_DEATH_SMOKE).insert(2008, (int) LevelEvent.PARTICLE_DENY_BLOCK).insert(2009, (int) LevelEvent.PARTICLE_GENERIC_SPAWN).insert(2010, (int) LevelEvent.PARTICLE_DRAGON_EGG).insert(2011, (int) LevelEvent.PARTICLE_CROP_EATEN).insert(2012, (int) LevelEvent.PARTICLE_CRIT).insert(2013, (int) LevelEvent.PARTICLE_TELEPORT).insert(2014, (int) LevelEvent.PARTICLE_CRACK_BLOCK).insert(2015, (int) LevelEvent.PARTICLE_BUBBLES).insert(2016, (int) LevelEvent.PARTICLE_EVAPORATE).insert(2017, (int) LevelEvent.PARTICLE_DESTROY_ARMOR_STAND).insert(2018, (int) LevelEvent.PARTICLE_BREAKING_EGG).insert(2019, (int) LevelEvent.PARTICLE_DESTROY_EGG).insert(2020, (int) LevelEvent.PARTICLE_EVAPORATE_WATER).insert(2021, (int) LevelEvent.PARTICLE_DESTROY_BLOCK_NO_SOUND).insert(3001, (int) LevelEvent.START_RAINING).insert(3002, (int) LevelEvent.START_THUNDERSTORM).insert(3003, (int) LevelEvent.STOP_RAINING).insert(3004, (int) LevelEvent.STOP_THUNDERSTORM).insert(3005, (int) LevelEvent.GLOBAL_PAUSE).insert(3006, (int) LevelEvent.SIM_TIME_STEP).insert(3007, (int) LevelEvent.SIM_TIME_SCALE).insert(LEVEL_EVENT_BLOCK, (int) LevelEvent.ACTIVATE_BLOCK).insert(3501, (int) LevelEvent.CAULDRON_EXPLODE).insert(3502, (int) LevelEvent.CAULDRON_DYE_ARMOR).insert(3503, (int) LevelEvent.CAULDRON_CLEAN_ARMOR).insert(3504, (int) LevelEvent.CAULDRON_FILL_POTION).insert(3505, (int) LevelEvent.CAULDRON_TAKE_POTION).insert(3506, (int) LevelEvent.CAULDRON_FILL_WATER).insert(3507, (int) LevelEvent.CAULDRON_TAKE_WATER).insert(3508, (int) LevelEvent.CAULDRON_ADD_DYE).insert(3509, (int) LevelEvent.CAULDRON_CLEAN_BANNER).insert(3510, (int) LevelEvent.CAULDRON_FLUSH).insert(16384, (TypeMap) PARTICLE_TYPES).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = TypeMap.builder(SoundEvent.class).insert(0, (int) SoundEvent.ITEM_USE_ON).insert(1, (int) SoundEvent.HIT).insert(2, (int) SoundEvent.STEP).insert(3, (int) SoundEvent.FLY).insert(4, (int) SoundEvent.JUMP).insert(5, (int) SoundEvent.BREAK).insert(6, (int) SoundEvent.PLACE).insert(7, (int) SoundEvent.HEAVY_STEP).insert(8, (int) SoundEvent.GALLOP).insert(9, (int) SoundEvent.FALL).insert(10, (int) SoundEvent.AMBIENT).insert(11, (int) SoundEvent.AMBIENT_BABY).insert(12, (int) SoundEvent.AMBIENT_IN_WATER).insert(13, (int) SoundEvent.BREATHE).insert(14, (int) SoundEvent.DEATH).insert(15, (int) SoundEvent.DEATH_IN_WATER).insert(16, (int) SoundEvent.DEATH_TO_ZOMBIE).insert(17, (int) SoundEvent.HURT).insert(18, (int) SoundEvent.HURT_IN_WATER).insert(19, (int) SoundEvent.MAD).insert(20, (int) SoundEvent.BOOST).insert(21, (int) SoundEvent.BOW).insert(22, (int) SoundEvent.SQUISH_BIG).insert(23, (int) SoundEvent.SQUISH_SMALL).insert(24, (int) SoundEvent.FALL_BIG).insert(25, (int) SoundEvent.FALL_SMALL).insert(26, (int) SoundEvent.SPLASH).insert(27, (int) SoundEvent.FIZZ).insert(28, (int) SoundEvent.FLAP).insert(29, (int) SoundEvent.SWIM).insert(30, (int) SoundEvent.DRINK).insert(31, (int) SoundEvent.EAT).insert(32, (int) SoundEvent.TAKEOFF).insert(33, (int) SoundEvent.SHAKE).insert(34, (int) SoundEvent.PLOP).insert(35, (int) SoundEvent.LAND).insert(36, (int) SoundEvent.SADDLE).insert(37, (int) SoundEvent.ARMOR).insert(38, (int) SoundEvent.MOB_ARMOR_STAND_PLACE).insert(39, (int) SoundEvent.ADD_CHEST).insert(40, (int) SoundEvent.THROW).insert(41, (int) SoundEvent.ATTACK).insert(42, (int) SoundEvent.ATTACK_NODAMAGE).insert(43, (int) SoundEvent.ATTACK_STRONG).insert(44, (int) SoundEvent.WARN).insert(45, (int) SoundEvent.SHEAR).insert(46, (int) SoundEvent.MILK).insert(47, (int) SoundEvent.THUNDER).insert(48, (int) SoundEvent.EXPLODE).insert(49, (int) SoundEvent.FIRE).insert(50, (int) SoundEvent.IGNITE).insert(51, (int) SoundEvent.FUSE).insert(52, (int) SoundEvent.STARE).insert(53, (int) SoundEvent.SPAWN).insert(54, (int) SoundEvent.SHOOT).insert(55, (int) SoundEvent.BREAK_BLOCK).insert(56, (int) SoundEvent.LAUNCH).insert(57, (int) SoundEvent.BLAST).insert(58, (int) SoundEvent.LARGE_BLAST).insert(59, (int) SoundEvent.TWINKLE).insert(60, (int) SoundEvent.REMEDY).insert(61, (int) SoundEvent.UNFECT).insert(62, (int) SoundEvent.LEVELUP).insert(63, (int) SoundEvent.BOW_HIT).insert(64, (int) SoundEvent.BULLET_HIT).insert(65, (int) SoundEvent.EXTINGUISH_FIRE).insert(66, (int) SoundEvent.ITEM_FIZZ).insert(67, (int) SoundEvent.CHEST_OPEN).insert(68, (int) SoundEvent.CHEST_CLOSED).insert(69, (int) SoundEvent.SHULKERBOX_OPEN).insert(70, (int) SoundEvent.SHULKERBOX_CLOSED).insert(71, (int) SoundEvent.ENDERCHEST_OPEN).insert(72, (int) SoundEvent.ENDERCHEST_CLOSED).insert(73, (int) SoundEvent.POWER_ON).insert(74, (int) SoundEvent.POWER_OFF).insert(75, (int) SoundEvent.ATTACH).insert(76, (int) SoundEvent.DETACH).insert(77, (int) SoundEvent.DENY).insert(78, (int) SoundEvent.TRIPOD).insert(79, (int) SoundEvent.POP).insert(80, (int) SoundEvent.DROP_SLOT).insert(81, (int) SoundEvent.NOTE).insert(82, (int) SoundEvent.THORNS).insert(83, (int) SoundEvent.PISTON_IN).insert(84, (int) SoundEvent.PISTON_OUT).insert(85, (int) SoundEvent.PORTAL).insert(86, (int) SoundEvent.WATER).insert(87, (int) SoundEvent.LAVA_POP).insert(88, (int) SoundEvent.LAVA).insert(89, (int) SoundEvent.BURP).insert(90, (int) SoundEvent.BUCKET_FILL_WATER).insert(91, (int) SoundEvent.BUCKET_FILL_LAVA).insert(92, (int) SoundEvent.BUCKET_EMPTY_WATER).insert(93, (int) SoundEvent.BUCKET_EMPTY_LAVA).insert(94, (int) SoundEvent.ARMOR_EQUIP_CHAIN).insert(95, (int) SoundEvent.ARMOR_EQUIP_DIAMOND).insert(96, (int) SoundEvent.ARMOR_EQUIP_GENERIC).insert(97, (int) SoundEvent.ARMOR_EQUIP_GOLD).insert(98, (int) SoundEvent.ARMOR_EQUIP_IRON).insert(99, (int) SoundEvent.ARMOR_EQUIP_LEATHER).insert(100, (int) SoundEvent.ARMOR_EQUIP_ELYTRA).insert(101, (int) SoundEvent.RECORD_13).insert(102, (int) SoundEvent.RECORD_CAT).insert(103, (int) SoundEvent.RECORD_BLOCKS).insert(104, (int) SoundEvent.RECORD_CHIRP).insert(105, (int) SoundEvent.RECORD_FAR).insert(106, (int) SoundEvent.RECORD_MALL).insert(107, (int) SoundEvent.RECORD_MELLOHI).insert(108, (int) SoundEvent.RECORD_STAL).insert(109, (int) SoundEvent.RECORD_STRAD).insert(110, (int) SoundEvent.RECORD_WARD).insert(111, (int) SoundEvent.RECORD_11).insert(112, (int) SoundEvent.RECORD_WAIT).insert(113, (int) SoundEvent.STOP_RECORD).insert(114, (int) SoundEvent.FLOP).insert(115, (int) SoundEvent.ELDERGUARDIAN_CURSE).insert(116, (int) SoundEvent.MOB_WARNING).insert(117, (int) SoundEvent.MOB_WARNING_BABY).insert(118, (int) SoundEvent.TELEPORT).insert(119, (int) SoundEvent.SHULKER_OPEN).insert(120, (int) SoundEvent.SHULKER_CLOSE).insert(121, (int) SoundEvent.HAGGLE).insert(122, (int) SoundEvent.HAGGLE_YES).insert(123, (int) SoundEvent.HAGGLE_NO).insert(124, (int) SoundEvent.HAGGLE_IDLE).insert(bl.bm, (int) SoundEvent.CHORUS_GROW).insert(126, (int) SoundEvent.CHORUS_DEATH).insert(127, (int) SoundEvent.GLASS).insert(128, (int) SoundEvent.POTION_BREWED).insert(bl.bq, (int) SoundEvent.CAST_SPELL).insert(bl.br, (int) SoundEvent.PREPARE_ATTACK).insert(bl.bs, (int) SoundEvent.PREPARE_SUMMON).insert(bl.bt, (int) SoundEvent.PREPARE_WOLOLO).insert(bl.bu, (int) SoundEvent.FANG).insert(bl.bv, (int) SoundEvent.CHARGE).insert(bl.bw, (int) SoundEvent.CAMERA_TAKE_PICTURE).insert(bl.bx, (int) SoundEvent.LEASHKNOT_PLACE).insert(bl.by, (int) SoundEvent.LEASHKNOT_BREAK).insert(bl.bz, (int) SoundEvent.GROWL).insert(bl.bA, (int) SoundEvent.WHINE).insert(bl.bB, (int) SoundEvent.PANT).insert(bl.bC, (int) SoundEvent.PURR).insert(bl.bD, (int) SoundEvent.PURREOW).insert(bl.bE, (int) SoundEvent.DEATH_MIN_VOLUME).insert(bl.bF, (int) SoundEvent.DEATH_MID_VOLUME).insert(bl.bG, (int) SoundEvent.IMITATE_BLAZE).insert(bl.bH, (int) SoundEvent.IMITATE_CAVE_SPIDER).insert(bl.bI, (int) SoundEvent.IMITATE_CREEPER).insert(bl.bJ, (int) SoundEvent.IMITATE_ELDER_GUARDIAN).insert(bl.bK, (int) SoundEvent.IMITATE_ENDER_DRAGON).insert(bl.bL, (int) SoundEvent.IMITATE_ENDERMAN).insert(bl.bN, (int) SoundEvent.IMITATE_EVOCATION_ILLAGER).insert(bl.bO, (int) SoundEvent.IMITATE_GHAST).insert(bl.bP, (int) SoundEvent.IMITATE_HUSK).insert(bl.bQ, (int) SoundEvent.IMITATE_ILLUSION_ILLAGER).insert(bl.bR, (int) SoundEvent.IMITATE_MAGMA_CUBE).insert(bl.bS, (int) SoundEvent.IMITATE_POLAR_BEAR).insert(bl.bT, (int) SoundEvent.IMITATE_SHULKER).insert(bl.bU, (int) SoundEvent.IMITATE_SILVERFISH).insert(bl.bV, (int) SoundEvent.IMITATE_SKELETON).insert(bl.bW, (int) SoundEvent.IMITATE_SLIME).insert(bl.bX, (int) SoundEvent.IMITATE_SPIDER).insert(bl.bY, (int) SoundEvent.IMITATE_STRAY).insert(bl.bZ, (int) SoundEvent.IMITATE_VEX).insert(bl.b0, (int) SoundEvent.IMITATE_VINDICATION_ILLAGER).insert(bl.b1, (int) SoundEvent.IMITATE_WITCH).insert(bl.b2, (int) SoundEvent.IMITATE_WITHER).insert(bl.b3, (int) SoundEvent.IMITATE_WITHER_SKELETON).insert(bl.b4, (int) SoundEvent.IMITATE_WOLF).insert(bl.b5, (int) SoundEvent.IMITATE_ZOMBIE).insert(bl.b6, (int) SoundEvent.IMITATE_ZOMBIE_PIGMAN).insert(bl.b7, (int) SoundEvent.IMITATE_ZOMBIE_VILLAGER).insert(bl.b8, (int) SoundEvent.BLOCK_END_PORTAL_FRAME_FILL).insert(bl.b9, (int) SoundEvent.BLOCK_END_PORTAL_SPAWN).insert(bl.b_, (int) SoundEvent.RANDOM_ANVIL_USE).insert(bl.ca, (int) SoundEvent.BOTTLE_DRAGONBREATH).insert(bl.cb, (int) SoundEvent.PORTAL_TRAVEL).insert(bl.cc, (int) SoundEvent.ITEM_TRIDENT_HIT).insert(bl.cd, (int) SoundEvent.ITEM_TRIDENT_RETURN).insert(bl.ce, (int) SoundEvent.ITEM_TRIDENT_RIPTIDE_1).insert(bl.cf, (int) SoundEvent.ITEM_TRIDENT_RIPTIDE_2).insert(bl.cg, (int) SoundEvent.ITEM_TRIDENT_RIPTIDE_3).insert(bl.ch, (int) SoundEvent.ITEM_TRIDENT_THROW).insert(bl.ci, (int) SoundEvent.ITEM_TRIDENT_THUNDER).insert(bl.cj, (int) SoundEvent.ITEM_TRIDENT_HIT_GROUND).insert(bl.ck, (int) SoundEvent.DEFAULT).insert(bl.cm, (int) SoundEvent.ELEMENT_CONSTRUCTOR_OPEN).insert(bl.cn, (int) SoundEvent.ICE_BOMB_HIT).insert(bl.co, (int) SoundEvent.BALLOON_POP).insert(bl.cp, (int) SoundEvent.LT_REACTION_ICE_BOMB).insert(bl.cq, (int) SoundEvent.LT_REACTION_BLEACH).insert(bl.cr, (int) SoundEvent.LT_REACTION_E_PASTE).insert(bl.cs, (int) SoundEvent.LT_REACTION_E_PASTE2).insert(bl.cx, (int) SoundEvent.LT_REACTION_FERTILIZER).insert(200, (int) SoundEvent.LT_REACTION_FIREBALL).insert(bl.cz, (int) SoundEvent.LT_REACTION_MG_SALT).insert(bl.cA, (int) SoundEvent.LT_REACTION_MISC_FIRE).insert(bl.cB, (int) SoundEvent.LT_REACTION_FIRE).insert(bl.cC, (int) SoundEvent.LT_REACTION_MISC_EXPLOSION).insert(bl.cD, (int) SoundEvent.LT_REACTION_MISC_MYSTICAL).insert(bl.cE, (int) SoundEvent.LT_REACTION_MISC_MYSTICAL2).insert(bl.cF, (int) SoundEvent.LT_REACTION_PRODUCT).insert(bl.cG, (int) SoundEvent.SPARKLER_USE).insert(bl.cH, (int) SoundEvent.GLOWSTICK_USE).insert(bl.cI, (int) SoundEvent.SPARKLER_ACTIVE).insert(bl.cJ, (int) SoundEvent.CONVERT_TO_DROWNED).insert(bl.cK, (int) SoundEvent.BUCKET_FILL_FISH).insert(bl.cL, (int) SoundEvent.BUCKET_EMPTY_FISH).insert(bl.cM, (int) SoundEvent.BUBBLE_UP).insert(bl.cN, (int) SoundEvent.BUBBLE_DOWN).insert(bl.cO, (int) SoundEvent.BUBBLE_POP).insert(bl.cP, (int) SoundEvent.BUBBLE_UP_INSIDE).insert(bl.cQ, (int) SoundEvent.BUBBLE_DOWN_INSIDE).insert(bl.cR, (int) SoundEvent.BABY_HURT).insert(bl.cS, (int) SoundEvent.BABY_DEATH).insert(bl.cT, (int) SoundEvent.BABY_STEP).insert(bl.cU, (int) SoundEvent.BABY_SPAWN).insert(bl.cV, (int) SoundEvent.BORN).insert(bl.cW, (int) SoundEvent.BLOCK_TURTLE_EGG_BREAK).insert(bl.cX, (int) SoundEvent.BLOCK_TURTLE_EGG_CRACK).insert(bl.cY, (int) SoundEvent.BLOCK_TURTLE_EGG_HATCH).insert(bl.cZ, (int) SoundEvent.TURTLE_LAY_EGG).insert(bl.c0, (int) SoundEvent.BLOCK_TURTLE_EGG_ATTACK).insert(bl.c1, (int) SoundEvent.BEACON_ACTIVATE).insert(bl.c2, (int) SoundEvent.BEACON_AMBIENT).insert(bl.c3, (int) SoundEvent.BEACON_DEACTIVATE).insert(bl.c4, (int) SoundEvent.BEACON_POWER).insert(bl.c5, (int) SoundEvent.CONDUIT_ACTIVATE).insert(bl.c6, (int) SoundEvent.CONDUIT_AMBIENT).insert(bl.c7, (int) SoundEvent.CONDUIT_ATTACK).insert(bl.c8, (int) SoundEvent.CONDUIT_DEACTIVATE).insert(bl.c9, (int) SoundEvent.CONDUIT_SHORT).insert(bl.c_, (int) SoundEvent.SWOOP).insert(bl.da, (int) SoundEvent.UNDEFINED).build();
    protected static final TypeMap<EntityEventType> ENTITY_EVENTS = TypeMap.builder(EntityEventType.class).insert(0, (int) EntityEventType.NONE).insert(1, (int) EntityEventType.JUMP).insert(2, (int) EntityEventType.HURT).insert(3, (int) EntityEventType.DEATH).insert(4, (int) EntityEventType.ATTACK_START).insert(5, (int) EntityEventType.ATTACK_STOP).insert(6, (int) EntityEventType.TAME_FAILED).insert(7, (int) EntityEventType.TAME_SUCCEEDED).insert(8, (int) EntityEventType.SHAKE_WETNESS).insert(9, (int) EntityEventType.USE_ITEM).insert(10, (int) EntityEventType.EAT_GRASS).insert(11, (int) EntityEventType.FISH_HOOK_BUBBLE).insert(12, (int) EntityEventType.FISH_HOOK_POSITION).insert(13, (int) EntityEventType.FISH_HOOK_TIME).insert(14, (int) EntityEventType.FISH_HOOK_TEASE).insert(15, (int) EntityEventType.SQUID_FLEEING).insert(16, (int) EntityEventType.ZOMBIE_VILLAGER_CURE).insert(17, (int) EntityEventType.PLAY_AMBIENT).insert(18, (int) EntityEventType.RESPAWN).insert(19, (int) EntityEventType.GOLEM_FLOWER_OFFER).insert(20, (int) EntityEventType.GOLEM_FLOWER_WITHDRAW).insert(21, (int) EntityEventType.LOVE_PARTICLES).insert(22, (int) EntityEventType.VILLAGER_ANGRY).insert(23, (int) EntityEventType.VILLAGER_HAPPY).insert(24, (int) EntityEventType.WITCH_HAT_MAGIC).insert(25, (int) EntityEventType.FIREWORK_EXPLODE).insert(26, (int) EntityEventType.IN_LOVE_HEARTS).insert(27, (int) EntityEventType.SILVERFISH_MERGE_WITH_STONE).insert(28, (int) EntityEventType.GUARDIAN_ATTACK_ANIMATION).insert(29, (int) EntityEventType.WITCH_DRINK_POTION).insert(30, (int) EntityEventType.WITCH_THROW_POTION).insert(31, (int) EntityEventType.PRIME_TNT_MINECART).insert(32, (int) EntityEventType.PRIME_CREEPER).insert(33, (int) EntityEventType.AIR_SUPPLY).insert(34, (int) EntityEventType.PLAYER_ADD_XP_LEVELS).insert(35, (int) EntityEventType.ELDER_GUARDIAN_CURSE).insert(36, (int) EntityEventType.AGENT_ARM_SWING).insert(37, (int) EntityEventType.ENDER_DRAGON_DEATH).insert(38, (int) EntityEventType.DUST_PARTICLES).insert(39, (int) EntityEventType.ARROW_SHAKE).insert(57, (int) EntityEventType.EATING_ITEM).insert(60, (int) EntityEventType.BABY_ANIMAL_FEED).insert(61, (int) EntityEventType.DEATH_SMOKE_CLOUD).insert(62, (int) EntityEventType.COMPLETE_TRADE).insert(63, (int) EntityEventType.REMOVE_LEASH).insert(64, (int) EntityEventType.CARAVAN).insert(65, (int) EntityEventType.CONSUME_TOTEM).insert(66, (int) EntityEventType.CHECK_TREASURE_HUNTER_ACHIEVEMENT).insert(67, (int) EntityEventType.ENTITY_SPAWN).insert(68, (int) EntityEventType.DRAGON_FLAMING).insert(69, (int) EntityEventType.UPDATE_ITEM_STACK_SIZE).insert(70, (int) EntityEventType.START_SWIMMING).insert(71, (int) EntityEventType.BALLOON_POP).insert(72, (int) EntityEventType.TREASURE_HUNT).build();
    public static TypeMap<CommandParam> COMMAND_PARAMS = TypeMap.builder(CommandParam.class).insert(1, (int) CommandParam.INT).insert(2, (int) CommandParam.FLOAT).insert(3, (int) CommandParam.VALUE).insert(4, (int) CommandParam.WILDCARD_INT).insert(5, (int) CommandParam.OPERATOR).insert(6, (int) CommandParam.TARGET).insert(7, (int) CommandParam.WILDCARD_TARGET).insert(24, (int) CommandParam.STRING).insert(26, (int) CommandParam.POSITION).insert(29, (int) CommandParam.MESSAGE).insert(31, (int) CommandParam.TEXT).insert(34, (int) CommandParam.JSON).insert(41, (int) CommandParam.COMMAND).build();
    public static final BedrockCodec CODEC = BedrockCodec.builder().protocolVersion(bl.d1).minecraftVersion("1.7.0").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v291.lambda$static$0();
        }
    }).registerPacket(new BedrockCompat$$ExternalSyntheticLambda1(), LoginSerializer_v291.INSTANCE, 1).registerPacket(new BedrockCompat$$ExternalSyntheticLambda2(), PlayStatusSerializer_v291.INSTANCE, 2).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda60
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ServerToClientHandshakePacket();
        }
    }, ServerToClientHandshakeSerializer_v291.INSTANCE, 3).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda72
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ClientToServerHandshakePacket();
        }
    }, ClientToServerHandshakeSerializer_v291.INSTANCE, 4).registerPacket(new BedrockCompat$$ExternalSyntheticLambda3(), DisconnectSerializer_v291.INSTANCE, 5).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda95
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ResourcePacksInfoPacket();
        }
    }, ResourcePacksInfoSerializer_v291.INSTANCE, 6).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda107
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ResourcePackStackPacket();
        }
    }, ResourcePackStackSerializer_v291.INSTANCE, 7).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda6
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ResourcePackClientResponsePacket();
        }
    }, ResourcePackClientResponseSerializer_v291.INSTANCE, 8).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda18
        @Override // java.util.function.Supplier
        public final Object get() {
            return new TextPacket();
        }
    }, TextSerializer_v291.INSTANCE, 9).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda19
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetTimePacket();
        }
    }, SetTimeSerializer_v291.INSTANCE, 10).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda27
        @Override // java.util.function.Supplier
        public final Object get() {
            return new StartGamePacket();
        }
    }, StartGameSerializer_v291.INSTANCE, 11).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda29
        @Override // java.util.function.Supplier
        public final Object get() {
            return new AddPlayerPacket();
        }
    }, AddPlayerSerializer_v291.INSTANCE, 12).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda30
        @Override // java.util.function.Supplier
        public final Object get() {
            return new AddEntityPacket();
        }
    }, AddEntitySerializer_v291.INSTANCE, 13).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda31
        @Override // java.util.function.Supplier
        public final Object get() {
            return new RemoveEntityPacket();
        }
    }, RemoveEntitySerializer_v291.INSTANCE, 14).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda32
        @Override // java.util.function.Supplier
        public final Object get() {
            return new AddItemEntityPacket();
        }
    }, AddItemEntitySerializer_v291.INSTANCE, 15).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda33
        @Override // java.util.function.Supplier
        public final Object get() {
            return new AddHangingEntityPacket();
        }
    }, AddHangingEntitySerializer_v291.INSTANCE, 16).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda34
        @Override // java.util.function.Supplier
        public final Object get() {
            return new TakeItemEntityPacket();
        }
    }, TakeItemEntitySerializer_v291.INSTANCE, 17).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda35
        @Override // java.util.function.Supplier
        public final Object get() {
            return new MoveEntityAbsolutePacket();
        }
    }, MoveEntityAbsoluteSerializer_v291.INSTANCE, 18).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda36
        @Override // java.util.function.Supplier
        public final Object get() {
            return new MovePlayerPacket();
        }
    }, MovePlayerSerializer_v291.INSTANCE, 19).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda37
        @Override // java.util.function.Supplier
        public final Object get() {
            return new RiderJumpPacket();
        }
    }, RiderJumpSerializer_v291.INSTANCE, 20).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda39
        @Override // java.util.function.Supplier
        public final Object get() {
            return new UpdateBlockPacket();
        }
    }, UpdateBlockSerializer_v291.INSTANCE, 21).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda40
        @Override // java.util.function.Supplier
        public final Object get() {
            return new AddPaintingPacket();
        }
    }, AddPaintingSerializer_v291.INSTANCE, 22).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda41
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ExplodePacket();
        }
    }, ExplodeSerializer_v291.INSTANCE, 23).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda42
        @Override // java.util.function.Supplier
        public final Object get() {
            return new LevelSoundEvent1Packet();
        }
    }, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS), 24).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda43
        @Override // java.util.function.Supplier
        public final Object get() {
            return new LevelEventPacket();
        }
    }, new LevelEventSerializer_v291(LEVEL_EVENTS), 25).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda44
        @Override // java.util.function.Supplier
        public final Object get() {
            return new BlockEventPacket();
        }
    }, BlockEventSerializer_v291.INSTANCE, 26).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda45
        @Override // java.util.function.Supplier
        public final Object get() {
            return new EntityEventPacket();
        }
    }, new EntityEventSerializer_v291(ENTITY_EVENTS), 27).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda46
        @Override // java.util.function.Supplier
        public final Object get() {
            return new MobEffectPacket();
        }
    }, MobEffectSerializer_v291.INSTANCE, 28).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda47
        @Override // java.util.function.Supplier
        public final Object get() {
            return new UpdateAttributesPacket();
        }
    }, UpdateAttributesSerializer_v291.INSTANCE, 29).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda49
        @Override // java.util.function.Supplier
        public final Object get() {
            return new InventoryTransactionPacket();
        }
    }, InventoryTransactionSerializer_v291.INSTANCE, 30).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda50
        @Override // java.util.function.Supplier
        public final Object get() {
            return new MobEquipmentPacket();
        }
    }, MobEquipmentSerializer_v291.INSTANCE, 31).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda51
        @Override // java.util.function.Supplier
        public final Object get() {
            return new MobArmorEquipmentPacket();
        }
    }, MobArmorEquipmentSerializer_v291.INSTANCE, 32).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda52
        @Override // java.util.function.Supplier
        public final Object get() {
            return new InteractPacket();
        }
    }, InteractSerializer_v291.INSTANCE, 33).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda53
        @Override // java.util.function.Supplier
        public final Object get() {
            return new BlockPickRequestPacket();
        }
    }, BlockPickRequestSerializer_v291.INSTANCE, 34).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda54
        @Override // java.util.function.Supplier
        public final Object get() {
            return new EntityPickRequestPacket();
        }
    }, EntityPickRequestSerializer_v291.INSTANCE, 35).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda55
        @Override // java.util.function.Supplier
        public final Object get() {
            return new PlayerActionPacket();
        }
    }, PlayerActionSerializer_v291.INSTANCE, 36).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda56
        @Override // java.util.function.Supplier
        public final Object get() {
            return new EntityFallPacket();
        }
    }, EntityFallSerializer_v291.INSTANCE, 37).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda57
        @Override // java.util.function.Supplier
        public final Object get() {
            return new HurtArmorPacket();
        }
    }, HurtArmorSerializer_v291.INSTANCE, 38).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda58
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetEntityDataPacket();
        }
    }, SetEntityDataSerializer_v291.INSTANCE, 39).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda61
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetEntityMotionPacket();
        }
    }, SetEntityMotionSerializer_v291.INSTANCE, 40).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda62
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetEntityLinkPacket();
        }
    }, SetEntityLinkSerializer_v291.INSTANCE, 41).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda63
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetHealthPacket();
        }
    }, SetHealthSerializer_v291.INSTANCE, 42).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda64
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetSpawnPositionPacket();
        }
    }, SetSpawnPositionSerializer_v291.INSTANCE, 43).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda65
        @Override // java.util.function.Supplier
        public final Object get() {
            return new AnimatePacket();
        }
    }, AnimateSerializer_v291.INSTANCE, 44).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda66
        @Override // java.util.function.Supplier
        public final Object get() {
            return new RespawnPacket();
        }
    }, RespawnSerializer_v291.INSTANCE, 45).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda67
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ContainerOpenPacket();
        }
    }, ContainerOpenSerializer_v291.INSTANCE, 46).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda68
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ContainerClosePacket();
        }
    }, ContainerCloseSerializer_v291.INSTANCE, 47).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda69
        @Override // java.util.function.Supplier
        public final Object get() {
            return new PlayerHotbarPacket();
        }
    }, PlayerHotbarSerializer_v291.INSTANCE, 48).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda71
        @Override // java.util.function.Supplier
        public final Object get() {
            return new InventoryContentPacket();
        }
    }, InventoryContentSerializer_v291.INSTANCE, 49).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda73
        @Override // java.util.function.Supplier
        public final Object get() {
            return new InventorySlotPacket();
        }
    }, InventorySlotSerializer_v291.INSTANCE, 50).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda74
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ContainerSetDataPacket();
        }
    }, ContainerSetDataSerializer_v291.INSTANCE, 51).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda75
        @Override // java.util.function.Supplier
        public final Object get() {
            return new CraftingDataPacket();
        }
    }, CraftingDataSerializer_v291.INSTANCE, 52).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda76
        @Override // java.util.function.Supplier
        public final Object get() {
            return new CraftingEventPacket();
        }
    }, CraftingEventSerializer_v291.INSTANCE, 53).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda77
        @Override // java.util.function.Supplier
        public final Object get() {
            return new GuiDataPickItemPacket();
        }
    }, GuiDataPickItemSerializer_v291.INSTANCE, 54).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda78
        @Override // java.util.function.Supplier
        public final Object get() {
            return new AdventureSettingsPacket();
        }
    }, AdventureSettingsSerializer_v291.INSTANCE, 55).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda79
        @Override // java.util.function.Supplier
        public final Object get() {
            return new BlockEntityDataPacket();
        }
    }, BlockEntityDataSerializer_v291.INSTANCE, 56).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda80
        @Override // java.util.function.Supplier
        public final Object get() {
            return new PlayerInputPacket();
        }
    }, PlayerInputSerializer_v291.INSTANCE, 57).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda82
        @Override // java.util.function.Supplier
        public final Object get() {
            return new LevelChunkPacket();
        }
    }, FullChunkDataSerializer_v291.INSTANCE, 58).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda83
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetCommandsEnabledPacket();
        }
    }, SetCommandsEnabledSerializer_v291.INSTANCE, 59).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda84
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetDifficultyPacket();
        }
    }, SetDifficultySerializer_v291.INSTANCE, 60).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda85
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ChangeDimensionPacket();
        }
    }, ChangeDimensionSerializer_v291.INSTANCE, 61).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda86
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetPlayerGameTypePacket();
        }
    }, SetPlayerGameTypeSerializer_v291.INSTANCE, 62).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda87
        @Override // java.util.function.Supplier
        public final Object get() {
            return new PlayerListPacket();
        }
    }, PlayerListSerializer_v291.INSTANCE, 63).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda88
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SimpleEventPacket();
        }
    }, SimpleEventSerializer_v291.INSTANCE, 64).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda89
        @Override // java.util.function.Supplier
        public final Object get() {
            return new EventPacket();
        }
    }, EventSerializer_v291.INSTANCE, 65).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda90
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SpawnExperienceOrbPacket();
        }
    }, SpawnExperienceOrbSerializer_v291.INSTANCE, 66).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda92
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ClientboundMapItemDataPacket();
        }
    }, ClientboundMapItemDataSerializer_v291.INSTANCE, 67).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda93
        @Override // java.util.function.Supplier
        public final Object get() {
            return new MapInfoRequestPacket();
        }
    }, MapInfoRequestSerializer_v291.INSTANCE, 68).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda94
        @Override // java.util.function.Supplier
        public final Object get() {
            return new RequestChunkRadiusPacket();
        }
    }, RequestChunkRadiusSerializer_v291.INSTANCE, 69).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda96
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ChunkRadiusUpdatedPacket();
        }
    }, ChunkRadiusUpdatedSerializer_v291.INSTANCE, 70).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda97
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ItemFrameDropItemPacket();
        }
    }, ItemFrameDropItemSerializer_v291.INSTANCE, 71).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda98
        @Override // java.util.function.Supplier
        public final Object get() {
            return new GameRulesChangedPacket();
        }
    }, GameRulesChangedSerializer_v291.INSTANCE, 72).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda99
        @Override // java.util.function.Supplier
        public final Object get() {
            return new CameraPacket();
        }
    }, CameraSerializer_v291.INSTANCE, 73).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda100
        @Override // java.util.function.Supplier
        public final Object get() {
            return new BossEventPacket();
        }
    }, BossEventSerializer_v291.INSTANCE, 74).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda101
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ShowCreditsPacket();
        }
    }, ShowCreditsSerializer_v291.INSTANCE, 75).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda103
        @Override // java.util.function.Supplier
        public final Object get() {
            return new AvailableCommandsPacket();
        }
    }, new AvailableCommandsSerializer_v291(COMMAND_PARAMS), 76).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda104
        @Override // java.util.function.Supplier
        public final Object get() {
            return new CommandRequestPacket();
        }
    }, CommandRequestSerializer_v291.INSTANCE, 77).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda105
        @Override // java.util.function.Supplier
        public final Object get() {
            return new CommandBlockUpdatePacket();
        }
    }, CommandBlockUpdateSerializer_v291.INSTANCE, 78).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda106
        @Override // java.util.function.Supplier
        public final Object get() {
            return new CommandOutputPacket();
        }
    }, CommandOutputSerializer_v291.INSTANCE, 79).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda108
        @Override // java.util.function.Supplier
        public final Object get() {
            return new UpdateTradePacket();
        }
    }, UpdateTradeSerializer_v291.INSTANCE, 80).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda109
        @Override // java.util.function.Supplier
        public final Object get() {
            return new UpdateEquipPacket();
        }
    }, UpdateEquipSerializer_v291.INSTANCE, 81).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda110
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ResourcePackDataInfoPacket();
        }
    }, ResourcePackDataInfoSerializer_v291.INSTANCE, 82).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda111
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ResourcePackChunkDataPacket();
        }
    }, ResourcePackChunkDataSerializer_v291.INSTANCE, 83).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda112
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ResourcePackChunkRequestPacket();
        }
    }, ResourcePackChunkRequestSerializer_v291.INSTANCE, 84).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new TransferPacket();
        }
    }, TransferSerializer_v291.INSTANCE, 85).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new PlaySoundPacket();
        }
    }, PlaySoundSerializer_v291.INSTANCE, 86).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda3
        @Override // java.util.function.Supplier
        public final Object get() {
            return new StopSoundPacket();
        }
    }, StopSoundSerializer_v291.INSTANCE, 87).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda4
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetTitlePacket();
        }
    }, SetTitleSerializer_v291.INSTANCE, 88).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda5
        @Override // java.util.function.Supplier
        public final Object get() {
            return new AddBehaviorTreePacket();
        }
    }, AddBehaviorTreeSerializer_v291.INSTANCE, 89).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda7
        @Override // java.util.function.Supplier
        public final Object get() {
            return new StructureBlockUpdatePacket();
        }
    }, StructureBlockUpdateSerializer_v291.INSTANCE, 90).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda8
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ShowStoreOfferPacket();
        }
    }, ShowStoreOfferSerializer_v291.INSTANCE, 91).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda9
        @Override // java.util.function.Supplier
        public final Object get() {
            return new PurchaseReceiptPacket();
        }
    }, PurchaseReceiptSerializer_v291.INSTANCE, 92).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda10
        @Override // java.util.function.Supplier
        public final Object get() {
            return new PlayerSkinPacket();
        }
    }, PlayerSkinSerializer_v291.INSTANCE, 93).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda12
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SubClientLoginPacket();
        }
    }, SubClientLoginSerializer_v291.INSTANCE, 94).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda13
        @Override // java.util.function.Supplier
        public final Object get() {
            return new AutomationClientConnectPacket();
        }
    }, AutomationClientConnectSerializer_v291.INSTANCE, 95).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda14
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetLastHurtByPacket();
        }
    }, SetLastHurtBySerializer_v291.INSTANCE, 96).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda15
        @Override // java.util.function.Supplier
        public final Object get() {
            return new BookEditPacket();
        }
    }, BookEditSerializer_v291.INSTANCE, 97).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda16
        @Override // java.util.function.Supplier
        public final Object get() {
            return new NpcRequestPacket();
        }
    }, NpcRequestSerializer_v291.INSTANCE, 98).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda17
        @Override // java.util.function.Supplier
        public final Object get() {
            return new PhotoTransferPacket();
        }
    }, PhotoTransferSerializer_v291.INSTANCE, 99).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda28
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ModalFormRequestPacket();
        }
    }, ModalFormRequestSerializer_v291.INSTANCE, 100).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda38
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ModalFormResponsePacket();
        }
    }, ModalFormResponseSerializer_v291.INSTANCE, 101).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda48
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ServerSettingsRequestPacket();
        }
    }, ServerSettingsRequestSerializer_v291.INSTANCE, 102).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda59
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ServerSettingsResponsePacket();
        }
    }, ServerSettingsResponseSerializer_v291.INSTANCE, 103).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda70
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ShowProfilePacket();
        }
    }, ShowProfileSerializer_v291.INSTANCE, 104).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda81
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetDefaultGameTypePacket();
        }
    }, SetDefaultGameTypeSerializer_v291.INSTANCE, 105).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda91
        @Override // java.util.function.Supplier
        public final Object get() {
            return new RemoveObjectivePacket();
        }
    }, RemoveObjectiveSerializer_v291.INSTANCE, 106).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda102
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetDisplayObjectivePacket();
        }
    }, SetDisplayObjectiveSerializer_v291.INSTANCE, 107).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda113
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetScorePacket();
        }
    }, SetScoreSerializer_v291.INSTANCE, 108).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda11
        @Override // java.util.function.Supplier
        public final Object get() {
            return new LabTablePacket();
        }
    }, LabTableSerializer_v291.INSTANCE, 109).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda20
        @Override // java.util.function.Supplier
        public final Object get() {
            return new UpdateBlockSyncedPacket();
        }
    }, UpdateBlockSyncedSerializer_v291.INSTANCE, 110).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda21
        @Override // java.util.function.Supplier
        public final Object get() {
            return new MoveEntityDeltaPacket();
        }
    }, MoveEntityDeltaSerializer_v291.INSTANCE, 111).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda22
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetScoreboardIdentityPacket();
        }
    }, SetScoreboardIdentitySerializer_v291.INSTANCE, 112).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda23
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetLocalPlayerAsInitializedPacket();
        }
    }, SetLocalPlayerAsInitializedSerializer_v291.INSTANCE, 113).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda24
        @Override // java.util.function.Supplier
        public final Object get() {
            return new UpdateSoftEnumPacket();
        }
    }, UpdateSoftEnumSerializer_v291.INSTANCE, 114).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda25
        @Override // java.util.function.Supplier
        public final Object get() {
            return new NetworkStackLatencyPacket();
        }
    }, NetworkStackLatencySerializer_v291.INSTANCE, 115).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291$$ExternalSyntheticLambda26
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ScriptCustomEventPacket();
        }
    }, ScriptCustomEventSerializer_v291.INSTANCE, 117).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v291(ENTITY_DATA, GAME_RULE_TYPES);
    }
}
