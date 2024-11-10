package org.cloudburstmc.protocol.bedrock.codec.v560;

import com.trossense.bl;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelSoundEvent1Serializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.LevelSoundEvent2Serializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.LevelSoundEventSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v557.BedrockCodecHelper_v557;
import org.cloudburstmc.protocol.bedrock.codec.v557.Bedrock_v557;
import org.cloudburstmc.protocol.bedrock.codec.v560.serializer.UpdateClientInputLocksSerializer_v560;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateClientInputLocksPacket;
import org.cloudburstmc.protocol.bedrock.transformer.FlagTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v560 extends Bedrock_v557 {
    protected static final TypeMap<EntityFlag> ENTITY_FLAGS = Bedrock_v557.ENTITY_FLAGS.toBuilder().shift(46, 1).insert(46, (int) EntityFlag.CAN_DASH).insert(108, (int) EntityFlag.HAS_DASH_COOLDOWN).insert(109, (int) EntityFlag.PUSH_TOWARDS_CLOSEST_SPACE).build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v557.ENTITY_DATA.toBuilder().update(EntityDataTypes.FLAGS, new FlagTransformer(ENTITY_FLAGS, 0)).update(EntityDataTypes.FLAGS_2, new FlagTransformer(ENTITY_FLAGS, 1)).build();
    protected static final TypeMap<ContainerSlotType> CONTAINER_SLOT_TYPES = Bedrock_v557.CONTAINER_SLOT_TYPES.toBuilder().shift(21, 1).insert(21, (int) ContainerSlotType.RECIPE_BOOK).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v557.SOUND_EVENTS.toBuilder().remove(448).insert(448, (int) SoundEvent.PRESSURE_PLATE_CLICK_OFF).insert(449, (int) SoundEvent.PRESSURE_PLATE_CLICK_ON).insert(450, (int) SoundEvent.BUTTON_CLICK_OFF).insert(451, (int) SoundEvent.BUTTON_CLICK_ON).insert(452, (int) SoundEvent.DOOR_OPEN).insert(453, (int) SoundEvent.DOOR_CLOSE).insert(454, (int) SoundEvent.TRAPDOOR_OPEN).insert(455, (int) SoundEvent.TRAPDOOR_CLOSE).insert(456, (int) SoundEvent.FENCE_GATE_OPEN).insert(457, (int) SoundEvent.FENCE_GATE_CLOSE).insert(458, (int) SoundEvent.UNDEFINED).build();
    public static final BedrockCodec CODEC = Bedrock_v557.CODEC.toBuilder().raknetProtocolVersion(11).protocolVersion(560).minecraftVersion("1.19.50").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v560.Bedrock_v560$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v560.lambda$static$0();
        }
    }).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v560.Bedrock_v560$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new UpdateClientInputLocksPacket();
        }
    }, new UpdateClientInputLocksSerializer_v560(), bl.cu).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v557(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS);
    }
}
