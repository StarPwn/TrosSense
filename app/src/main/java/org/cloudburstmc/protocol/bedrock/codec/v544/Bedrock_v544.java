package org.cloudburstmc.protocol.bedrock.codec.v544;

import com.trossense.bl;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v534.BedrockCodecHelper_v534;
import org.cloudburstmc.protocol.bedrock.codec.v534.Bedrock_v534;
import org.cloudburstmc.protocol.bedrock.codec.v544.serializer.ClientboundMapItemDataSerializer_v544;
import org.cloudburstmc.protocol.bedrock.codec.v544.serializer.FeatureRegistrySerializer_v544;
import org.cloudburstmc.protocol.bedrock.codec.v544.serializer.MapInfoRequestSerializer_v544;
import org.cloudburstmc.protocol.bedrock.codec.v544.serializer.ModalFormResponseSerializer_v544;
import org.cloudburstmc.protocol.bedrock.codec.v544.serializer.NetworkChunkPublisherUpdateSerializer_v544;
import org.cloudburstmc.protocol.bedrock.codec.v544.serializer.StartGameSerializer_v544;
import org.cloudburstmc.protocol.bedrock.codec.v544.serializer.UpdateAttributesSerializer_v544;
import org.cloudburstmc.protocol.bedrock.packet.ClientboundMapItemDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.FeatureRegistryPacket;
import org.cloudburstmc.protocol.bedrock.packet.MapInfoRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.NetworkChunkPublisherUpdatePacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateAttributesPacket;

/* loaded from: classes5.dex */
public class Bedrock_v544 extends Bedrock_v534 {
    public static final BedrockCodec CODEC = Bedrock_v534.CODEC.toBuilder().protocolVersion(544).minecraftVersion("1.19.20").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.Bedrock_v544$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v544.lambda$static$0();
        }
    }).updateSerializer(StartGamePacket.class, new StartGameSerializer_v544()).updateSerializer(UpdateAttributesPacket.class, new UpdateAttributesSerializer_v544()).updateSerializer(ClientboundMapItemDataPacket.class, new ClientboundMapItemDataSerializer_v544()).updateSerializer(MapInfoRequestPacket.class, new MapInfoRequestSerializer_v544()).updateSerializer(ModalFormResponsePacket.class, new ModalFormResponseSerializer_v544()).updateSerializer(NetworkChunkPublisherUpdatePacket.class, new NetworkChunkPublisherUpdateSerializer_v544()).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.Bedrock_v544$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new FeatureRegistryPacket();
        }
    }, new FeatureRegistrySerializer_v544(), bl.cp).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v534(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES);
    }
}
