package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.Objects;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackStackPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;

/* loaded from: classes5.dex */
public class ResourcePackStackSerializer_v291 implements BedrockPacketSerializer<ResourcePackStackPacket> {
    public static final ResourcePackStackSerializer_v291 INSTANCE = new ResourcePackStackSerializer_v291();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ResourcePackStackPacket packet) {
        buffer.writeBoolean(packet.isForcedToAccept());
        helper.writeArray(buffer, packet.getBehaviorPacks(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePackStackSerializer_v291$$ExternalSyntheticLambda1
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                ResourcePackStackSerializer_v291.this.writeEntry((ByteBuf) obj, (BedrockCodecHelper) obj2, (ResourcePackStackPacket.Entry) obj3);
            }
        });
        helper.writeArray(buffer, packet.getResourcePacks(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePackStackSerializer_v291$$ExternalSyntheticLambda1
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                ResourcePackStackSerializer_v291.this.writeEntry((ByteBuf) obj, (BedrockCodecHelper) obj2, (ResourcePackStackPacket.Entry) obj3);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ResourcePackStackPacket packet) {
        packet.setForcedToAccept(buffer.readBoolean());
        helper.readArray(buffer, packet.getBehaviorPacks(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePackStackSerializer_v291$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ResourcePackStackSerializer_v291.this.readEntry((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        helper.readArray(buffer, packet.getResourcePacks(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePackStackSerializer_v291$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ResourcePackStackSerializer_v291.this.readEntry((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
    }

    public ResourcePackStackPacket.Entry readEntry(ByteBuf buffer, BedrockCodecHelper helper) {
        String packId = helper.readString(buffer);
        String packVersion = helper.readString(buffer);
        String subPackName = helper.readString(buffer);
        return new ResourcePackStackPacket.Entry(packId, packVersion, subPackName);
    }

    public void writeEntry(ByteBuf buffer, BedrockCodecHelper helper, ResourcePackStackPacket.Entry entry) {
        Objects.requireNonNull(entry, "ResourcePackStackPacket entry is null");
        helper.writeString(buffer, entry.getPackId());
        helper.writeString(buffer, entry.getPackVersion());
        helper.writeString(buffer, entry.getSubPackName());
    }
}
