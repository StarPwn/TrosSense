package org.cloudburstmc.protocol.bedrock.codec.v407.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.packet.CreativeContentPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class CreativeContentSerializer_v407 implements BedrockPacketSerializer<CreativeContentPacket> {
    public static final CreativeContentSerializer_v407 INSTANCE = new CreativeContentSerializer_v407();
    private static final ItemData[] EMPTY = new ItemData[0];

    protected CreativeContentSerializer_v407() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, CreativeContentPacket packet) {
        helper.writeArray(buffer, packet.getContents(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.CreativeContentSerializer_v407$$ExternalSyntheticLambda0
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                CreativeContentSerializer_v407.this.writeCreativeItem((ByteBuf) obj, (BedrockCodecHelper) obj2, (ItemData) obj3);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, CreativeContentPacket packet) {
        packet.setContents((ItemData[]) helper.readArray(buffer, EMPTY, new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.CreativeContentSerializer_v407$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return CreativeContentSerializer_v407.this.readCreativeItem((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ItemData readCreativeItem(ByteBuf buffer, BedrockCodecHelper helper) {
        int netId = VarInts.readUnsignedInt(buffer);
        ItemData item = helper.readItemInstance(buffer);
        item.setNetId(netId);
        return item;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeCreativeItem(ByteBuf buffer, BedrockCodecHelper helper, ItemData item) {
        VarInts.writeUnsignedInt(buffer, item.getNetId());
        helper.writeItemInstance(buffer, item);
    }
}
