package org.cloudburstmc.protocol.bedrock.codec.v630;

import com.trossense.bl;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelSoundEvent1Serializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.LevelSoundEvent2Serializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.LevelSoundEventSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.LevelEventGenericSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v575.BedrockCodecHelper_v575;
import org.cloudburstmc.protocol.bedrock.codec.v622.Bedrock_v622;
import org.cloudburstmc.protocol.bedrock.codec.v630.serializer.SetPlayerInventoryOptionsSerializer_v360;
import org.cloudburstmc.protocol.bedrock.codec.v630.serializer.ShowStoreOfferSerializer_v630;
import org.cloudburstmc.protocol.bedrock.codec.v630.serializer.ToggleCrafterSlotRequestSerializer_v630;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.ParticleType;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventGenericPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetPlayerInventoryOptionsPacket;
import org.cloudburstmc.protocol.bedrock.packet.ShowStoreOfferPacket;
import org.cloudburstmc.protocol.bedrock.packet.ToggleCrafterSlotRequestPacket;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v630 extends Bedrock_v622 {
    protected static final TypeMap<ParticleType> PARTICLE_TYPES = Bedrock_v622.PARTICLE_TYPES.toBuilder().insert(87, (int) ParticleType.DUST_PLUME).insert(88, (int) ParticleType.WHITE_SMOKE).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v622.LEVEL_EVENTS.toBuilder().insert(2040, (int) LevelEvent.DUST_PLUME).replace(3609, LevelEvent.PARTICLE_SHOOT_WHITE_SMOKE).insert(3610, (int) LevelEvent.ALL_PLAYERS_SLEEPING).insert(16384, PARTICLE_TYPES).build();
    protected static final TypeMap<ContainerSlotType> CONTAINER_SLOT_TYPES = Bedrock_v622.CONTAINER_SLOT_TYPES.toBuilder().insert(62, (int) ContainerSlotType.CRAFTER_BLOCK_CONTAINER).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v622.SOUND_EVENTS.toBuilder().replace(479, SoundEvent.CRAFTER_CRAFT).insert(480, (int) SoundEvent.CRAFTER_FAILED).insert(481, (int) SoundEvent.DECORATED_POT_INSERT).insert(482, (int) SoundEvent.DECORATED_POT_INSERT_FAILED).insert(483, (int) SoundEvent.CRAFTER_DISABLE_SLOT).insert(490, (int) SoundEvent.COPPER_BULB_ON).insert(491, (int) SoundEvent.COPPER_BULB_OFF).insert(492, (int) SoundEvent.UNDEFINED).build();
    public static final BedrockCodec CODEC = Bedrock_v622.CODEC.toBuilder().raknetProtocolVersion(11).protocolVersion(630).minecraftVersion("1.20.50").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v630.Bedrock_v630$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v630.lambda$static$0();
        }
    }).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).updateSerializer(LevelEventGenericPacket.class, new LevelEventGenericSerializer_v361(LEVEL_EVENTS)).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).updateSerializer(ShowStoreOfferPacket.class, ShowStoreOfferSerializer_v630.INSTANCE).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v630.Bedrock_v630$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ToggleCrafterSlotRequestPacket();
        }
    }, new ToggleCrafterSlotRequestSerializer_v630(), bl.ef).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v630.Bedrock_v630$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetPlayerInventoryOptionsPacket();
        }
    }, new SetPlayerInventoryOptionsSerializer_v360(), 307).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v575(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS);
    }
}
