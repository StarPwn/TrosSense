package org.cloudburstmc.protocol.bedrock.codec.v407.serializer;

import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequest;
import org.cloudburstmc.protocol.bedrock.packet.ItemStackRequestPacket;

/* loaded from: classes5.dex */
public class ItemStackRequestSerializer_v407 implements BedrockPacketSerializer<ItemStackRequestPacket> {
    public static final ItemStackRequestSerializer_v407 INSTANCE = new ItemStackRequestSerializer_v407();

    protected ItemStackRequestSerializer_v407() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, final BedrockCodecHelper helper, ItemStackRequestPacket packet) {
        List<ItemStackRequest> requests = packet.getRequests();
        helper.getClass();
        helper.writeArray(buffer, requests, new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.ItemStackRequestSerializer_v407$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper.this.writeItemStackRequest((ByteBuf) obj, (ItemStackRequest) obj2);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, final BedrockCodecHelper helper, ItemStackRequestPacket packet) {
        List<ItemStackRequest> requests = packet.getRequests();
        helper.getClass();
        helper.readArray(buffer, requests, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.ItemStackRequestSerializer_v407$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper.this.readItemStackRequest((ByteBuf) obj);
            }
        });
    }
}
