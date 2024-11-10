package org.cloudburstmc.protocol.bedrock.codec.v589;

import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelSoundEvent1Serializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.LevelSoundEvent2Serializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.LevelSoundEventSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v575.BedrockCodecHelper_v575;
import org.cloudburstmc.protocol.bedrock.codec.v582.Bedrock_v582;
import org.cloudburstmc.protocol.bedrock.codec.v589.serializer.EmoteSerializer_v589;
import org.cloudburstmc.protocol.bedrock.codec.v589.serializer.EventSerializer_v589;
import org.cloudburstmc.protocol.bedrock.codec.v589.serializer.StartGameSerializer_v589;
import org.cloudburstmc.protocol.bedrock.codec.v589.serializer.UnlockedRecipesSerializer_v589;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.TextProcessingEventOrigin;
import org.cloudburstmc.protocol.bedrock.packet.EmotePacket;
import org.cloudburstmc.protocol.bedrock.packet.EventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.bedrock.packet.UnlockedRecipesPacket;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v589 extends Bedrock_v582 {
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v582.SOUND_EVENTS.toBuilder().replace(466, SoundEvent.SNIFFER_EGG_CRACK).insert(467, (int) SoundEvent.SNIFFER_EGG_HATCHED).insert(468, (int) SoundEvent.WAXED_SIGN_INTERACT_FAIL).insert(469, (int) SoundEvent.RECORD_RELIC).insert(470, (int) SoundEvent.UNDEFINED).build();
    protected static final TypeMap<TextProcessingEventOrigin> TEXT_PROCESSING_ORIGINS = TypeMap.builder(TextProcessingEventOrigin.class).insert(0, (int) TextProcessingEventOrigin.SERVER_CHAT_PUBLIC).insert(1, (int) TextProcessingEventOrigin.SERVER_CHAT_WHISPER).insert(2, (int) TextProcessingEventOrigin.SIGN_TEXT).insert(3, (int) TextProcessingEventOrigin.ANVIL_TEXT).insert(4, (int) TextProcessingEventOrigin.BOOK_AND_QUILL_TEXT).insert(5, (int) TextProcessingEventOrigin.COMMAND_BLOCK_TEXT).insert(6, (int) TextProcessingEventOrigin.BLOCK_ENTITY_DATA_TEXT).insert(7, (int) TextProcessingEventOrigin.JOIN_EVENT_TEXT).insert(8, (int) TextProcessingEventOrigin.LEAVE_EVENT_TEXT).insert(9, (int) TextProcessingEventOrigin.SLASH_COMMAND_TEXT).insert(10, (int) TextProcessingEventOrigin.CARTOGRAPHY_TEXT).insert(11, (int) TextProcessingEventOrigin.KICK_COMMAND).insert(12, (int) TextProcessingEventOrigin.TITLE_COMMAND).insert(13, (int) TextProcessingEventOrigin.SUMMON_COMMAND).build();
    public static final BedrockCodec CODEC = Bedrock_v582.CODEC.toBuilder().raknetProtocolVersion(11).protocolVersion(589).minecraftVersion("1.20.0").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v589.Bedrock_v589$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v589.lambda$static$0();
        }
    }).updateSerializer(StartGamePacket.class, new StartGameSerializer_v589()).updateSerializer(EventPacket.class, EventSerializer_v589.INSTANCE).updateSerializer(EmotePacket.class, EmoteSerializer_v589.INSTANCE).updateSerializer(UnlockedRecipesPacket.class, new UnlockedRecipesSerializer_v589()).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v575(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS);
    }
}
