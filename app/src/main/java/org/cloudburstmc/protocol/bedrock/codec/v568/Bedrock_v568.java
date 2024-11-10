package org.cloudburstmc.protocol.bedrock.codec.v568;

import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v567.Bedrock_v567;

/* loaded from: classes5.dex */
public class Bedrock_v568 extends Bedrock_v567 {
    public static final BedrockCodec CODEC = Bedrock_v567.CODEC.toBuilder().raknetProtocolVersion(11).protocolVersion(568).minecraftVersion("1.19.63").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v568.Bedrock_v568$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v568.lambda$static$0();
        }
    }).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v568(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS);
    }
}
