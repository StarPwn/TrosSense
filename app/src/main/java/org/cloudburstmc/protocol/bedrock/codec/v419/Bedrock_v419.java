package org.cloudburstmc.protocol.bedrock.codec.v419;

import com.trossense.bl;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelSoundEvent1Serializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.LevelSoundEvent2Serializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.LevelSoundEventSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AvailableCommandsSerializer_v388;
import org.cloudburstmc.protocol.bedrock.codec.v408.Bedrock_v408;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.AnimateEntitySerializer_v419;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.CameraShakeSerializer_v419;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.ContainerCloseSerializer_v419;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.CorrectPlayerMovePredictionSerializer_v419;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.ItemComponentSerializer_v419;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.ItemStackResponseSerializer_v419;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.MotionPredictionHintsSerializer_v419;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.MoveEntityDeltaSerializer_v419;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.MovePlayerSerializer_v419;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.PlayerAuthInputSerializer_v419;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.PlayerFogSerializer_v419;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.ResourcePackStackSerializer_v419;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.SetEntityDataSerializer_v419;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.UpdateAttributesSerializer_v419;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.packet.AnimateEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.bedrock.packet.CameraShakePacket;
import org.cloudburstmc.protocol.bedrock.packet.ContainerClosePacket;
import org.cloudburstmc.protocol.bedrock.packet.CorrectPlayerMovePredictionPacket;
import org.cloudburstmc.protocol.bedrock.packet.ItemComponentPacket;
import org.cloudburstmc.protocol.bedrock.packet.ItemStackResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.MotionPredictionHintsPacket;
import org.cloudburstmc.protocol.bedrock.packet.MoveEntityDeltaPacket;
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerFogPacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackStackPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetEntityDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateAttributesPacket;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v419 extends Bedrock_v408 {
    protected static final TypeMap<CommandParam> COMMAND_PARAMS = Bedrock_v408.COMMAND_PARAMS.toBuilder().shift(7, 1).shift(30, 1).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v408.SOUND_EVENTS.toBuilder().replace(317, SoundEvent.EQUIP_NETHERITE).insert(318, (int) SoundEvent.UNDEFINED).build();
    public static BedrockCodec CODEC = Bedrock_v408.CODEC.toBuilder().protocolVersion(419).minecraftVersion("1.16.100").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.Bedrock_v419$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v419.lambda$static$0();
        }
    }).updateSerializer(ResourcePackStackPacket.class, ResourcePackStackSerializer_v419.INSTANCE).updateSerializer(StartGamePacket.class, StartGameSerializer_v419.INSTANCE).updateSerializer(MovePlayerPacket.class, MovePlayerSerializer_v419.INSTANCE).updateSerializer(UpdateAttributesPacket.class, UpdateAttributesSerializer_v419.INSTANCE).updateSerializer(SetEntityDataPacket.class, SetEntityDataSerializer_v419.INSTANCE).updateSerializer(ContainerClosePacket.class, ContainerCloseSerializer_v419.INSTANCE).updateSerializer(MoveEntityDeltaPacket.class, MoveEntityDeltaSerializer_v419.INSTANCE).updateSerializer(PlayerAuthInputPacket.class, PlayerAuthInputSerializer_v419.INSTANCE).updateSerializer(ItemStackResponsePacket.class, ItemStackResponseSerializer_v419.INSTANCE).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).updateSerializer(AvailableCommandsPacket.class, new AvailableCommandsSerializer_v388(COMMAND_PARAMS)).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.Bedrock_v419$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new MotionPredictionHintsPacket();
        }
    }, MotionPredictionHintsSerializer_v419.INSTANCE, bl.bS).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.Bedrock_v419$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new AnimateEntityPacket();
        }
    }, AnimateEntitySerializer_v419.INSTANCE, bl.bT).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.Bedrock_v419$$ExternalSyntheticLambda3
        @Override // java.util.function.Supplier
        public final Object get() {
            return new CameraShakePacket();
        }
    }, CameraShakeSerializer_v419.INSTANCE, bl.bU).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.Bedrock_v419$$ExternalSyntheticLambda4
        @Override // java.util.function.Supplier
        public final Object get() {
            return new PlayerFogPacket();
        }
    }, PlayerFogSerializer_v419.INSTANCE, bl.bV).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.Bedrock_v419$$ExternalSyntheticLambda5
        @Override // java.util.function.Supplier
        public final Object get() {
            return new CorrectPlayerMovePredictionPacket();
        }
    }, CorrectPlayerMovePredictionSerializer_v419.INSTANCE, bl.bW).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.Bedrock_v419$$ExternalSyntheticLambda6
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ItemComponentPacket();
        }
    }, ItemComponentSerializer_v419.INSTANCE, bl.bX).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v419(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES);
    }
}
