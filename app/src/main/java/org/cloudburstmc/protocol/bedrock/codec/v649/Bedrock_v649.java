package org.cloudburstmc.protocol.bedrock.codec.v649;

import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelSoundEvent1Serializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.LevelSoundEvent2Serializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.LevelSoundEventSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.LevelEventGenericSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v575.BedrockCodecHelper_v575;
import org.cloudburstmc.protocol.bedrock.codec.v630.Bedrock_v630;
import org.cloudburstmc.protocol.bedrock.codec.v649.serializer.CorrectPlayerMovePredictionSerializer_v649;
import org.cloudburstmc.protocol.bedrock.codec.v649.serializer.LevelChunkSerializer_v649;
import org.cloudburstmc.protocol.bedrock.codec.v649.serializer.PlayerAuthInputSerializer_v649;
import org.cloudburstmc.protocol.bedrock.codec.v649.serializer.PlayerListSerializer_v649;
import org.cloudburstmc.protocol.bedrock.codec.v649.serializer.SetHudSerializer_v649;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.ParticleType;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.packet.CorrectPlayerMovePredictionPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventGenericPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerListPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetHudPacket;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v649 extends Bedrock_v630 {
    protected static final TypeMap<ParticleType> PARTICLE_TYPES = Bedrock_v630.PARTICLE_TYPES.toBuilder().shift(18, 1).insert(18, (int) ParticleType.WIND_EXPLOSION).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v630.LEVEL_EVENTS.toBuilder().replace(3609, LevelEvent.PARTICLE_SHOOT_WHITE_SMOKE).replace(3610, LevelEvent.PARTICLE_WIND_EXPLOSION).insert(3611, (int) LevelEvent.PARTICLE_TRAIL_SPAWNER_DETECTION).insert(3612, (int) LevelEvent.PARTICLE_TRAIL_SPAWNER_SPAWNING).insert(3613, (int) LevelEvent.PARTICLE_TRAIL_SPAWNER_EJECTING).insert(3614, (int) LevelEvent.ALL_PLAYERS_SLEEPING).insert(16384, PARTICLE_TYPES).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v630.SOUND_EVENTS.toBuilder().replace(492, SoundEvent.AMBIENT_IN_AIR).insert(493, (int) SoundEvent.WIND_BURST).insert(494, (int) SoundEvent.IMITATE_BREEZE).insert(495, (int) SoundEvent.ARMADILLO_BRUSH).insert(496, (int) SoundEvent.ARMADILLO_SCUTE_DROP).insert(497, (int) SoundEvent.EQUIP_WOLF).insert(498, (int) SoundEvent.UNEQUIP_WOLF).insert(499, (int) SoundEvent.REFLECT).insert(500, (int) SoundEvent.UNDEFINED).build();
    public static final BedrockCodec CODEC = Bedrock_v630.CODEC.toBuilder().raknetProtocolVersion(11).protocolVersion(649).minecraftVersion("1.20.60").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v649.Bedrock_v649$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v649.lambda$static$0();
        }
    }).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).updateSerializer(LevelEventGenericPacket.class, new LevelEventGenericSerializer_v361(LEVEL_EVENTS)).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).updateSerializer(CorrectPlayerMovePredictionPacket.class, CorrectPlayerMovePredictionSerializer_v649.INSTANCE).updateSerializer(LevelChunkPacket.class, LevelChunkSerializer_v649.INSTANCE).updateSerializer(PlayerAuthInputPacket.class, new PlayerAuthInputSerializer_v649()).updateSerializer(PlayerListPacket.class, PlayerListSerializer_v649.INSTANCE).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v649.Bedrock_v649$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new SetHudPacket();
        }
    }, SetHudSerializer_v649.INSTANCE, 308).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v575(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS);
    }
}
