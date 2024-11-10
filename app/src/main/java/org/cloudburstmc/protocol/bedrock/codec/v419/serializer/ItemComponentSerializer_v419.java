package org.cloudburstmc.protocol.bedrock.codec.v419.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiFunction;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.inventory.ComponentItemData;
import org.cloudburstmc.protocol.bedrock.packet.ItemComponentPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;

/* loaded from: classes5.dex */
public class ItemComponentSerializer_v419 implements BedrockPacketSerializer<ItemComponentPacket> {
    public static final ItemComponentSerializer_v419 INSTANCE = new ItemComponentSerializer_v419();

    protected ItemComponentSerializer_v419() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ItemComponentPacket packet) {
        helper.writeArray(buffer, packet.getItems(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.ItemComponentSerializer_v419$$ExternalSyntheticLambda0
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                ItemComponentSerializer_v419.lambda$serialize$0((ByteBuf) obj, (BedrockCodecHelper) obj2, (ComponentItemData) obj3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$serialize$0(ByteBuf buf, BedrockCodecHelper packetHelper, ComponentItemData item) {
        packetHelper.writeString(buf, item.getName());
        packetHelper.writeTag(buf, item.getData());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ItemComponentPacket packet) {
        helper.readArray(buffer, packet.getItems(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.ItemComponentSerializer_v419$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ItemComponentSerializer_v419.lambda$deserialize$1((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ ComponentItemData lambda$deserialize$1(ByteBuf buf, BedrockCodecHelper packetHelper) {
        String name = packetHelper.readString(buf);
        NbtMap data = (NbtMap) packetHelper.readTag(buf, NbtMap.class);
        return new ComponentItemData(name, data);
    }
}
