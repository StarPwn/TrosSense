package org.cloudburstmc.protocol.bedrock.codec.v554;

import com.trossense.bl;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.compat.BedrockCompat$$ExternalSyntheticLambda4;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelSoundEvent1Serializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.LevelSoundEvent2Serializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.LevelSoundEventSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v545.Bedrock_v545;
import org.cloudburstmc.protocol.bedrock.codec.v554.serializer.GameTestRequestSerializer_v554;
import org.cloudburstmc.protocol.bedrock.codec.v554.serializer.GameTestResultsSerializer_v554;
import org.cloudburstmc.protocol.bedrock.codec.v554.serializer.NetworkSettingsSerializer_v554;
import org.cloudburstmc.protocol.bedrock.codec.v554.serializer.RequestNetworkSettingsSerializer_v554;
import org.cloudburstmc.protocol.bedrock.codec.v554.serializer.ServerStatsSerializer_v554;
import org.cloudburstmc.protocol.bedrock.codec.v554.serializer.StructureBlockUpdateSerializer_v554;
import org.cloudburstmc.protocol.bedrock.codec.v554.serializer.TextSerializer_v554;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.TextProcessingEventOrigin;
import org.cloudburstmc.protocol.bedrock.packet.GameTestRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.GameTestResultsPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.NetworkSettingsPacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerStatsPacket;
import org.cloudburstmc.protocol.bedrock.packet.StructureBlockUpdatePacket;
import org.cloudburstmc.protocol.bedrock.packet.TextPacket;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v554 extends Bedrock_v545 {
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v545.SOUND_EVENTS.toBuilder().replace(442, SoundEvent.ENCHANTING_TABLE_USE).insert(443, (int) SoundEvent.UNDEFINED).build();
    protected static final TypeMap<TextProcessingEventOrigin> TEXT_PROCESSING_ORIGINS = TypeMap.fromEnum(TextProcessingEventOrigin.class, 13);
    public static final BedrockCodec CODEC = Bedrock_v545.CODEC.toBuilder().raknetProtocolVersion(11).protocolVersion(554).minecraftVersion("1.19.30").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v554.Bedrock_v554$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v554.lambda$static$0();
        }
    }).updateSerializer(TextPacket.class, new TextSerializer_v554()).updateSerializer(NetworkSettingsPacket.class, new NetworkSettingsSerializer_v554()).updateSerializer(StructureBlockUpdatePacket.class, new StructureBlockUpdateSerializer_v554()).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v554.Bedrock_v554$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ServerStatsPacket();
        }
    }, new ServerStatsSerializer_v554(), bl.cq).registerPacket(new BedrockCompat$$ExternalSyntheticLambda4(), new RequestNetworkSettingsSerializer_v554(), bl.cr).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v554.Bedrock_v554$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new GameTestRequestPacket();
        }
    }, new GameTestRequestSerializer_v554(), bl.cs).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v554.Bedrock_v554$$ExternalSyntheticLambda3
        @Override // java.util.function.Supplier
        public final Object get() {
            return new GameTestResultsPacket();
        }
    }, new GameTestResultsSerializer_v554(), bl.ct).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v554(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS);
    }
}
