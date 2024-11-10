package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackClientResponsePacket;

/* loaded from: classes5.dex */
public class ResourcePackClientResponseSerializer_v291 implements BedrockPacketSerializer<ResourcePackClientResponsePacket> {
    public static final ResourcePackClientResponseSerializer_v291 INSTANCE = new ResourcePackClientResponseSerializer_v291();

    protected ResourcePackClientResponseSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ResourcePackClientResponsePacket packet) {
        buffer.writeByte(packet.getStatus().ordinal());
        List<String> packIds = packet.getPackIds();
        helper.getClass();
        writeArrayShortLE(buffer, packIds, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14(helper));
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ResourcePackClientResponsePacket packet) {
        ResourcePackClientResponsePacket.Status status = ResourcePackClientResponsePacket.Status.values()[buffer.readUnsignedByte()];
        packet.setStatus(status);
        List<String> packIds = packet.getPackIds();
        helper.getClass();
        readArrayShortLE(buffer, packIds, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda3(helper));
    }

    protected <T> void readArrayShortLE(ByteBuf buffer, Collection<T> collection, Function<ByteBuf, T> function) {
        int length = buffer.readUnsignedShortLE();
        for (int i = 0; i < length; i++) {
            collection.add(function.apply(buffer));
        }
    }

    protected <T> void writeArrayShortLE(ByteBuf buffer, Collection<T> collection, BiConsumer<ByteBuf, T> consumer) {
        buffer.writeShortLE(collection.size());
        for (T t : collection) {
            consumer.accept(buffer, t);
        }
    }
}
