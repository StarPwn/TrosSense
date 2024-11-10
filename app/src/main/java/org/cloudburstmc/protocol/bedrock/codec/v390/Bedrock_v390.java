package org.cloudburstmc.protocol.bedrock.codec.v390;

import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v389.Bedrock_v389;
import org.cloudburstmc.protocol.bedrock.codec.v390.serializer.PlayerListSerializer_v390;
import org.cloudburstmc.protocol.bedrock.codec.v390.serializer.PlayerSkinSerializer_v390;
import org.cloudburstmc.protocol.bedrock.packet.PlayerListPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerSkinPacket;

/* loaded from: classes5.dex */
public class Bedrock_v390 extends Bedrock_v389 {
    public static BedrockCodec CODEC = Bedrock_v389.CODEC.toBuilder().protocolVersion(390).minecraftVersion("1.14.60").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v390.Bedrock_v390$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v390.lambda$static$0();
        }
    }).updateSerializer(PlayerListPacket.class, PlayerListSerializer_v390.INSTANCE).updateSerializer(PlayerSkinPacket.class, PlayerSkinSerializer_v390.INSTANCE).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v390(ENTITY_DATA, GAME_RULE_TYPES);
    }
}
