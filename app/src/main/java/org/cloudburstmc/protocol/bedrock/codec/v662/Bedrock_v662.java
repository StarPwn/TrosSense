package org.cloudburstmc.protocol.bedrock.codec.v662;

import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.LevelEventGenericSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v575.BedrockCodecHelper_v575;
import org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AvailableCommandsSerializer_v594;
import org.cloudburstmc.protocol.bedrock.codec.v649.Bedrock_v649;
import org.cloudburstmc.protocol.bedrock.codec.v662.serializer.LecternUpdateSerializer_v662;
import org.cloudburstmc.protocol.bedrock.codec.v662.serializer.MobEffectSerializer_v662;
import org.cloudburstmc.protocol.bedrock.codec.v662.serializer.PlayerAuthInputSerializer_v662;
import org.cloudburstmc.protocol.bedrock.codec.v662.serializer.ResourcePacksInfoSerializer_v622;
import org.cloudburstmc.protocol.bedrock.codec.v662.serializer.SetEntityMotionSerializer_v662;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.ParticleType;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.TextProcessingEventOrigin;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.bedrock.packet.ItemFrameDropItemPacket;
import org.cloudburstmc.protocol.bedrock.packet.LecternUpdatePacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventGenericPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.MobEffectPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePacksInfoPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetEntityMotionPacket;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v662 extends Bedrock_v649 {
    protected static final TypeMap<CommandParam> COMMAND_PARAMS = Bedrock_v649.COMMAND_PARAMS.toBuilder().shift(24, 4).insert(24, (int) CommandParam.RATIONAL_RANGE_VAL).insert(25, (int) CommandParam.RATIONAL_RANGE_POST_VAL).insert(26, (int) CommandParam.RATIONAL_RANGE).insert(27, (int) CommandParam.RATIONAL_RANGE_FULL).shift(48, 8).insert(48, (int) CommandParam.PROPERTY_VALUE).insert(49, (int) CommandParam.HAS_PROPERTY_PARAM_VALUE).insert(50, (int) CommandParam.HAS_PROPERTY_PARAM_ENUM_VALUE).insert(51, (int) CommandParam.HAS_PROPERTY_ARG).insert(52, (int) CommandParam.HAS_PROPERTY_ARGS).insert(53, (int) CommandParam.HAS_PROPERTY_ELEMENT).insert(54, (int) CommandParam.HAS_PROPERTY_ELEMENTS).insert(55, (int) CommandParam.HAS_PROPERTY_SELECTOR).build();
    protected static final TypeMap<ParticleType> PARTICLE_TYPES = Bedrock_v649.PARTICLE_TYPES.toBuilder().replace(18, ParticleType.BREEZE_WIND_EXPLOSION).insert(90, (int) ParticleType.VAULT_CONNECTION).insert(91, (int) ParticleType.WIND_EXPLOSION).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v649.LEVEL_EVENTS.toBuilder().replace(3610, LevelEvent.PARTICLE_BREEZE_WIND_EXPLOSION).replace(3614, LevelEvent.PARTICLE_WIND_EXPLOSION).insert(3615, (int) LevelEvent.ALL_PLAYERS_SLEEPING).insert(9811, (int) LevelEvent.ANIMATION_VAULT_ACTIVATE).insert(9812, (int) LevelEvent.ANIMATION_VAULT_DEACTIVATE).insert(9813, (int) LevelEvent.ANIMATION_VAULT_EJECT_ITEM).insert(16384, PARTICLE_TYPES).build();
    protected static final TypeMap<TextProcessingEventOrigin> TEXT_PROCESSING_ORIGINS = Bedrock_v649.TEXT_PROCESSING_ORIGINS.toBuilder().replace(14, TextProcessingEventOrigin.SERVER_FORM).build();
    public static final BedrockCodec CODEC = Bedrock_v649.CODEC.toBuilder().raknetProtocolVersion(11).protocolVersion(662).minecraftVersion("1.20.70").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v662.Bedrock_v662$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v662.lambda$static$0();
        }
    }).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).updateSerializer(LevelEventGenericPacket.class, new LevelEventGenericSerializer_v361(LEVEL_EVENTS)).updateSerializer(AvailableCommandsPacket.class, new AvailableCommandsSerializer_v594(COMMAND_PARAMS)).updateSerializer(LecternUpdatePacket.class, LecternUpdateSerializer_v662.INSTANCE).updateSerializer(MobEffectPacket.class, MobEffectSerializer_v662.INSTANCE).updateSerializer(PlayerAuthInputPacket.class, new PlayerAuthInputSerializer_v662()).updateSerializer(ResourcePacksInfoPacket.class, ResourcePacksInfoSerializer_v622.INSTANCE).updateSerializer(SetEntityMotionPacket.class, SetEntityMotionSerializer_v662.INSTANCE).deregisterPacket(ItemFrameDropItemPacket.class).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v575(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS);
    }
}
