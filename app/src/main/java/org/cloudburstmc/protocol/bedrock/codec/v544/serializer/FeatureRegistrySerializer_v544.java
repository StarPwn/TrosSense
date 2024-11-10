package org.cloudburstmc.protocol.bedrock.codec.v544.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.definitions.FeatureDefinition;
import org.cloudburstmc.protocol.bedrock.packet.FeatureRegistryPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;

/* loaded from: classes5.dex */
public class FeatureRegistrySerializer_v544 implements BedrockPacketSerializer<FeatureRegistryPacket> {
    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, final BedrockCodecHelper helper, FeatureRegistryPacket packet) {
        helper.writeArray(buffer, packet.getFeatures(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.serializer.FeatureRegistrySerializer_v544$$ExternalSyntheticLambda0
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                FeatureRegistrySerializer_v544.lambda$serialize$0(BedrockCodecHelper.this, (ByteBuf) obj, (BedrockCodecHelper) obj2, (FeatureDefinition) obj3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$serialize$0(BedrockCodecHelper helper, ByteBuf buf, BedrockCodecHelper aHelper, FeatureDefinition data) {
        helper.writeString(buf, data.getName());
        helper.writeString(buf, data.getJson());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, final BedrockCodecHelper helper, FeatureRegistryPacket packet) {
        helper.readArray(buffer, packet.getFeatures(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.serializer.FeatureRegistrySerializer_v544$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return FeatureRegistrySerializer_v544.lambda$deserialize$1(BedrockCodecHelper.this, (ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ FeatureDefinition lambda$deserialize$1(BedrockCodecHelper helper, ByteBuf buf, BedrockCodecHelper aHelper) {
        String name = helper.readString(buf);
        String json = helper.readString(buf);
        return new FeatureDefinition(name, json);
    }
}
