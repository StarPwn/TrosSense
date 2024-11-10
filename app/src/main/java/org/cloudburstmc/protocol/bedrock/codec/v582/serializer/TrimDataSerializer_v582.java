package org.cloudburstmc.protocol.bedrock.codec.v582.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.TrimMaterial;
import org.cloudburstmc.protocol.bedrock.data.TrimPattern;
import org.cloudburstmc.protocol.bedrock.packet.TrimDataPacket;

/* loaded from: classes5.dex */
public class TrimDataSerializer_v582 implements BedrockPacketSerializer<TrimDataPacket> {
    public static final TrimDataSerializer_v582 INSTANCE = new TrimDataSerializer_v582();

    protected TrimDataSerializer_v582() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, final BedrockCodecHelper helper, TrimDataPacket packet) {
        helper.writeArray(buffer, packet.getPatterns(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v582.serializer.TrimDataSerializer_v582$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                TrimDataSerializer_v582.lambda$serialize$0(BedrockCodecHelper.this, (ByteBuf) obj, (TrimPattern) obj2);
            }
        });
        helper.writeArray(buffer, packet.getMaterials(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v582.serializer.TrimDataSerializer_v582$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                TrimDataSerializer_v582.lambda$serialize$1(BedrockCodecHelper.this, (ByteBuf) obj, (TrimMaterial) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$serialize$0(BedrockCodecHelper helper, ByteBuf buf, TrimPattern pattern) {
        helper.writeString(buf, pattern.getItemName());
        helper.writeString(buf, pattern.getPatternId());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$serialize$1(BedrockCodecHelper helper, ByteBuf buf, TrimMaterial pattern) {
        helper.writeString(buf, pattern.getMaterialId());
        helper.writeString(buf, pattern.getColor());
        helper.writeString(buf, pattern.getItemName());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, final BedrockCodecHelper helper, TrimDataPacket packet) {
        helper.readArray(buffer, packet.getPatterns(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v582.serializer.TrimDataSerializer_v582$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TrimDataSerializer_v582.lambda$deserialize$2(BedrockCodecHelper.this, (ByteBuf) obj);
            }
        });
        helper.readArray(buffer, packet.getMaterials(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v582.serializer.TrimDataSerializer_v582$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TrimDataSerializer_v582.lambda$deserialize$3(BedrockCodecHelper.this, (ByteBuf) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ TrimPattern lambda$deserialize$2(BedrockCodecHelper helper, ByteBuf buf) {
        String name = helper.readString(buf);
        String id = helper.readString(buf);
        return new TrimPattern(name, id);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ TrimMaterial lambda$deserialize$3(BedrockCodecHelper helper, ByteBuf buf) {
        String id = helper.readString(buf);
        String color = helper.readString(buf);
        String name = helper.readString(buf);
        return new TrimMaterial(id, color, name);
    }
}
