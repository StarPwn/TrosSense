package org.cloudburstmc.protocol.bedrock.codec.v567;

import com.trossense.bl;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelSoundEvent1Serializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.LevelSoundEvent2Serializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.LevelSoundEventSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v557.BedrockCodecHelper_v557;
import org.cloudburstmc.protocol.bedrock.codec.v560.Bedrock_v560;
import org.cloudburstmc.protocol.bedrock.codec.v567.serializer.ClientCheatAbilitySerializer_v567;
import org.cloudburstmc.protocol.bedrock.codec.v567.serializer.CommandRequestSerializer_v567;
import org.cloudburstmc.protocol.bedrock.codec.v567.serializer.CraftingDataSerializer_v567;
import org.cloudburstmc.protocol.bedrock.codec.v567.serializer.StartGameSerializer_v567;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.packet.ClientCheatAbilityPacket;
import org.cloudburstmc.protocol.bedrock.packet.CommandRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.CraftingDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v567 extends Bedrock_v560 {
    public static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v560.SOUND_EVENTS.toBuilder().replace(458, SoundEvent.INSERT).insert(459, (int) SoundEvent.PICKUP).insert(460, (int) SoundEvent.INSERT_ENCHANTED).insert(461, (int) SoundEvent.PICKUP_ENCHANTED).insert(462, (int) SoundEvent.UNDEFINED).build();
    public static final BedrockCodec CODEC = Bedrock_v560.CODEC.toBuilder().raknetProtocolVersion(11).protocolVersion(567).minecraftVersion("1.19.60").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v567.Bedrock_v567$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v567.lambda$static$0();
        }
    }).updateSerializer(StartGamePacket.class, new StartGameSerializer_v567()).updateSerializer(CommandRequestPacket.class, new CommandRequestSerializer_v567()).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).updateSerializer(CraftingDataPacket.class, new CraftingDataSerializer_v567()).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v567.Bedrock_v567$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ClientCheatAbilityPacket();
        }
    }, new ClientCheatAbilitySerializer_v567(), bl.cv).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v557(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS);
    }
}
