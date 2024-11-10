package org.cloudburstmc.protocol.bedrock.codec.v428;

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
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AvailableCommandsSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v422.Bedrock_v422;
import org.cloudburstmc.protocol.bedrock.codec.v428.serializer.CameraShakeSerializer_v428;
import org.cloudburstmc.protocol.bedrock.codec.v428.serializer.ClientboundDebugRendererSerializer_v428;
import org.cloudburstmc.protocol.bedrock.codec.v428.serializer.ItemStackResponseSerializer_v428;
import org.cloudburstmc.protocol.bedrock.codec.v428.serializer.PlayerAuthInputSerializer_v428;
import org.cloudburstmc.protocol.bedrock.codec.v428.serializer.StartGameSerializer_v428;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.bedrock.packet.CameraShakePacket;
import org.cloudburstmc.protocol.bedrock.packet.ClientboundDebugRendererPacket;
import org.cloudburstmc.protocol.bedrock.packet.ItemStackResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventGenericPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.bedrock.transformer.FlagTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v428 extends Bedrock_v422 {
    protected static final TypeMap<EntityFlag> ENTITY_FLAGS = Bedrock_v422.ENTITY_FLAGS.toBuilder().insert(96, (int) EntityFlag.RAM_ATTACK).build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v422.ENTITY_DATA.toBuilder().update(EntityDataTypes.FLAGS, new FlagTransformer(ENTITY_FLAGS, 0)).update(EntityDataTypes.FLAGS_2, new FlagTransformer(ENTITY_FLAGS, 1)).shift(60, 1).insert(EntityDataTypes.SEAT_ROTATION_OFFSET_DEGREES, 60, EntityDataFormat.FLOAT).shift(120, 1).insert(EntityDataTypes.FREEZING_EFFECT_STRENGTH, 120, EntityDataFormat.FLOAT).insert(EntityDataTypes.GOAT_HORN_COUNT, 122, EntityDataFormat.INT).insert(EntityDataTypes.BASE_RUNTIME_ID, 123, EntityDataFormat.STRING).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v422.LEVEL_EVENTS.toBuilder().insert(2027, (int) LevelEvent.PARTICLE_VIBRATION_SIGNAL).insert(3514, (int) LevelEvent.CAULDRON_FILL_POWDER_SNOW).insert(3515, (int) LevelEvent.CAULDRON_TAKE_POWDER_SNOW).build();
    protected static final TypeMap<CommandParam> COMMAND_PARAMS = Bedrock_v422.COMMAND_PARAMS.toBuilder().shift(2, 1).shift(57, 6).insert(60, (int) CommandParam.BLOCK_STATES).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v422.SOUND_EVENTS.toBuilder().replace(318, SoundEvent.AMBIENT_LOOP_WARPED_FOREST).insert(319, (int) SoundEvent.AMBIENT_LOOP_SOULSAND_VALLEY).insert(320, (int) SoundEvent.AMBIENT_LOOP_NETHER_WASTES).insert(321, (int) SoundEvent.AMBIENT_LOOP_BASALT_DELTAS).insert(322, (int) SoundEvent.AMBIENT_LOOP_CRIMSON_FOREST).insert(323, (int) SoundEvent.AMBIENT_ADDITION_WARPED_FOREST).insert(324, (int) SoundEvent.AMBIENT_ADDITION_SOULSAND_VALLEY).insert(325, (int) SoundEvent.AMBIENT_ADDITION_NETHER_WASTES).insert(326, (int) SoundEvent.AMBIENT_ADDITION_BASALT_DELTAS).insert(327, (int) SoundEvent.AMBIENT_ADDITION_CRIMSON_FOREST).insert(328, (int) SoundEvent.SCULK_SENSOR_POWER_ON).insert(329, (int) SoundEvent.SCULK_SENSOR_POWER_OFF).insert(330, (int) SoundEvent.BUCKET_FILL_POWDER_SNOW).insert(331, (int) SoundEvent.BUCKET_EMPTY_POWDER_SNOW).insert(332, (int) SoundEvent.UNDEFINED).build();
    protected static final TypeMap<ItemStackRequestActionType> ITEM_STACK_REQUEST_TYPES = Bedrock_v422.ITEM_STACK_REQUEST_TYPES.toBuilder().shift(9, 1).insert(9, (int) ItemStackRequestActionType.MINE_BLOCK).build();
    public static final BedrockCodec CODEC = Bedrock_v422.CODEC.toBuilder().protocolVersion(428).minecraftVersion("1.16.210").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v428.Bedrock_v428$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v428.lambda$static$0();
        }
    }).updateSerializer(StartGamePacket.class, StartGameSerializer_v428.INSTANCE).updateSerializer(PlayerAuthInputPacket.class, PlayerAuthInputSerializer_v428.INSTANCE).updateSerializer(ItemStackResponsePacket.class, ItemStackResponseSerializer_v428.INSTANCE).updateSerializer(CameraShakePacket.class, CameraShakeSerializer_v428.INSTANCE).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).updateSerializer(AvailableCommandsPacket.class, new AvailableCommandsSerializer_v388(COMMAND_PARAMS)).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).updateSerializer(LevelEventGenericPacket.class, new LevelEventGenericSerializer_v361(LEVEL_EVENTS)).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v428.Bedrock_v428$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ClientboundDebugRendererPacket();
        }
    }, ClientboundDebugRendererSerializer_v428.INSTANCE, bl.bZ).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v428(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES);
    }
}
