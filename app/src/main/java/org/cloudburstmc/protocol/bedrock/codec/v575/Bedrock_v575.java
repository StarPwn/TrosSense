package org.cloudburstmc.protocol.bedrock.codec.v575;

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
import org.cloudburstmc.protocol.bedrock.codec.v568.Bedrock_v568;
import org.cloudburstmc.protocol.bedrock.codec.v575.serializer.CameraInstructionSerializer_v575;
import org.cloudburstmc.protocol.bedrock.codec.v575.serializer.CameraPresetsSerializer_v575;
import org.cloudburstmc.protocol.bedrock.codec.v575.serializer.PlayerAuthInputSerializer_v575;
import org.cloudburstmc.protocol.bedrock.codec.v575.serializer.UnlockedRecipesSerializer_v575;
import org.cloudburstmc.protocol.bedrock.data.Ability;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.ParticleType;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.bedrock.packet.CameraInstructionPacket;
import org.cloudburstmc.protocol.bedrock.packet.CameraPresetsPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventGenericPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.cloudburstmc.protocol.bedrock.packet.UnlockedRecipesPacket;
import org.cloudburstmc.protocol.bedrock.transformer.FlagTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v575 extends Bedrock_v568 {
    protected static final TypeMap<Ability> PLAYER_ABILITIES = Bedrock_v568.PLAYER_ABILITIES.toBuilder().insert(18, (int) Ability.PRIVILEGED_BUILDER).build();
    protected static final TypeMap<EntityFlag> ENTITY_FLAGS = Bedrock_v568.ENTITY_FLAGS.toBuilder().insert(110, (int) EntityFlag.SCENTING).insert(111, (int) EntityFlag.RISING).insert(112, (int) EntityFlag.FEELING_HAPPY).insert(113, (int) EntityFlag.SEARCHING).build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v568.ENTITY_DATA.toBuilder().update(EntityDataTypes.FLAGS, new FlagTransformer(ENTITY_FLAGS, 0)).update(EntityDataTypes.FLAGS_2, new FlagTransformer(ENTITY_FLAGS, 1)).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v568.SOUND_EVENTS.toBuilder().replace(462, SoundEvent.BRUSH).insert(463, (int) SoundEvent.BRUSH_COMPLETED).insert(464, (int) SoundEvent.SHATTER_DECORATED_POT).insert(465, (int) SoundEvent.BREAK_DECORATED_POD).insert(466, (int) SoundEvent.UNDEFINED).build();
    protected static final TypeMap<ParticleType> PARTICLE_TYPES = Bedrock_v568.PARTICLE_TYPES.toBuilder().insert(85, (int) ParticleType.BRUSH_DUST).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v568.LEVEL_EVENTS.toBuilder().insert(16384, PARTICLE_TYPES).build();
    protected static final TypeMap<CommandParam> COMMAND_PARAMS = TypeMap.builder(CommandParam.class).insert(0, (int) CommandParam.UNKNOWN).insert(1, (int) CommandParam.INT).insert(3, (int) CommandParam.FLOAT).insert(4, (int) CommandParam.VALUE).insert(5, (int) CommandParam.WILDCARD_INT).insert(6, (int) CommandParam.OPERATOR).insert(7, (int) CommandParam.COMPARE_OPERATOR).insert(8, (int) CommandParam.TARGET).insert(9, (int) CommandParam.UNKNOWN_STANDALONE).insert(10, (int) CommandParam.WILDCARD_TARGET).insert(11, (int) CommandParam.UNKNOWN_NON_ID).insert(12, (int) CommandParam.SCORE_ARG).insert(13, (int) CommandParam.SCORE_ARGS).insert(14, (int) CommandParam.SCORE_SELECT_PARAM).insert(15, (int) CommandParam.SCORE_SELECTOR).insert(16, (int) CommandParam.TAG_SELECTOR).insert(17, (int) CommandParam.FILE_PATH).insert(18, (int) CommandParam.FILE_PATH_VAL).insert(19, (int) CommandParam.FILE_PATH_CONT).insert(20, (int) CommandParam.INT_RANGE_VAL).insert(21, (int) CommandParam.INT_RANGE_POST_VAL).insert(22, (int) CommandParam.INT_RANGE).insert(23, (int) CommandParam.INT_RANGE_FULL).insert(24, (int) CommandParam.SEL_ARGS).insert(25, (int) CommandParam.ARGS).insert(26, (int) CommandParam.ARG).insert(27, (int) CommandParam.MARG).insert(28, (int) CommandParam.MVALUE).insert(29, (int) CommandParam.NAME).insert(30, (int) CommandParam.TYPE).insert(31, (int) CommandParam.FAMILY).insert(32, (int) CommandParam.TAG).insert(33, (int) CommandParam.HAS_ITEM_ELEMENT).insert(34, (int) CommandParam.HAS_ITEM_ELEMENTS).insert(35, (int) CommandParam.HAS_ITEM).insert(36, (int) CommandParam.HAS_ITEMS).insert(37, (int) CommandParam.HAS_ITEM_SELECTOR).insert(38, (int) CommandParam.EQUIPMENT_SLOTS).insert(39, (int) CommandParam.STRING).insert(40, (int) CommandParam.ID_CONT).insert(41, (int) CommandParam.COORD_X_INT).insert(42, (int) CommandParam.COORD_Y_INT).insert(43, (int) CommandParam.COORD_Z_INT).insert(44, (int) CommandParam.COORD_X_FLOAT).insert(45, (int) CommandParam.COORD_Y_FLOAT).insert(46, (int) CommandParam.COORD_Z_FLOAT).insert(47, (int) CommandParam.BLOCK_POSITION).insert(48, (int) CommandParam.POSITION).insert(49, (int) CommandParam.MESSAGE_XP).insert(50, (int) CommandParam.MESSAGE).insert(51, (int) CommandParam.MESSAGE_ROOT).insert(52, (int) CommandParam.POST_SELECTOR).insert(53, (int) CommandParam.TEXT).insert(54, (int) CommandParam.TEXT_CONT).insert(55, (int) CommandParam.JSON_VALUE).insert(56, (int) CommandParam.JSON_FIELD).insert(57, (int) CommandParam.JSON).insert(58, (int) CommandParam.JSON_OBJECT_FIELDS).insert(59, (int) CommandParam.JSON_OBJECT_CONT).insert(60, (int) CommandParam.JSON_ARRAY).insert(61, (int) CommandParam.JSON_ARRAY_VALUES).insert(62, (int) CommandParam.JSON_ARRAY_CONT).insert(63, (int) CommandParam.BLOCK_STATE).insert(64, (int) CommandParam.BLOCK_STATE_KEY).insert(65, (int) CommandParam.BLOCK_STATE_VALUE).insert(66, (int) CommandParam.BLOCK_STATE_VALUES).insert(67, (int) CommandParam.BLOCK_STATES).insert(68, (int) CommandParam.BLOCK_STATES_CONT).insert(69, (int) CommandParam.COMMAND).insert(70, (int) CommandParam.SLASH_COMMAND).build();
    public static final BedrockCodec CODEC = Bedrock_v568.CODEC.toBuilder().raknetProtocolVersion(11).protocolVersion(575).minecraftVersion("1.19.70").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v575.Bedrock_v575$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v575.lambda$static$0();
        }
    }).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).updateSerializer(LevelEventGenericPacket.class, new LevelEventGenericSerializer_v361(LEVEL_EVENTS)).updateSerializer(PlayerAuthInputPacket.class, new PlayerAuthInputSerializer_v575()).updateSerializer(AvailableCommandsPacket.class, new AvailableCommandsSerializer_v448(COMMAND_PARAMS)).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v575.Bedrock_v575$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new CameraPresetsPacket();
        }
    }, new CameraPresetsSerializer_v575(), bl.cw).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v575.Bedrock_v575$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new UnlockedRecipesPacket();
        }
    }, new UnlockedRecipesSerializer_v575(), bl.cx).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v575.Bedrock_v575$$ExternalSyntheticLambda3
        @Override // java.util.function.Supplier
        public final Object get() {
            return new CameraInstructionPacket();
        }
    }, new CameraInstructionSerializer_v575(), 300).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v575(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS);
    }
}
