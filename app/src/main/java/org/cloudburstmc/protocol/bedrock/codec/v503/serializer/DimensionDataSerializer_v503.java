package org.cloudburstmc.protocol.bedrock.codec.v503.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.definitions.DimensionDefinition;
import org.cloudburstmc.protocol.bedrock.packet.DimensionDataPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class DimensionDataSerializer_v503 implements BedrockPacketSerializer<DimensionDataPacket> {
    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, DimensionDataPacket packet) {
        helper.writeArray(buffer, packet.getDefinitions(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v503.serializer.DimensionDataSerializer_v503$$ExternalSyntheticLambda0
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                DimensionDataSerializer_v503.this.writeDefinition((ByteBuf) obj, (BedrockCodecHelper) obj2, (DimensionDefinition) obj3);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, DimensionDataPacket packet) {
        helper.readArray(buffer, packet.getDefinitions(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v503.serializer.DimensionDataSerializer_v503$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return DimensionDataSerializer_v503.this.readDefinition((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeDefinition(ByteBuf buffer, BedrockCodecHelper helper, DimensionDefinition definition) {
        helper.writeString(buffer, definition.getId());
        VarInts.writeInt(buffer, definition.getMaximumHeight());
        VarInts.writeInt(buffer, definition.getMinimumHeight());
        VarInts.writeInt(buffer, definition.getGeneratorType());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DimensionDefinition readDefinition(ByteBuf buffer, BedrockCodecHelper helper) {
        String id = helper.readString(buffer);
        int maximumHeight = VarInts.readInt(buffer);
        int minimumHeight = VarInts.readInt(buffer);
        int generatorType = VarInts.readInt(buffer);
        return new DimensionDefinition(id, maximumHeight, minimumHeight, generatorType);
    }
}
