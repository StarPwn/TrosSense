package org.cloudburstmc.protocol.bedrock.codec.v407.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.packet.InventoryContentPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class InventoryContentSerializer_v407 implements BedrockPacketSerializer<InventoryContentPacket> {
    public static final InventoryContentSerializer_v407 INSTANCE = new InventoryContentSerializer_v407();

    protected InventoryContentSerializer_v407() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, final BedrockCodecHelper helper, InventoryContentPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getContainerId());
        helper.writeArray(buffer, packet.getContents(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.InventoryContentSerializer_v407$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper.this.writeNetItem((ByteBuf) obj, (ItemData) obj2);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, final BedrockCodecHelper helper, InventoryContentPacket packet) {
        packet.setContainerId(VarInts.readUnsignedInt(buffer));
        helper.readArray(buffer, packet.getContents(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.InventoryContentSerializer_v407$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                ItemData readNetItem;
                readNetItem = BedrockCodecHelper.this.readNetItem((ByteBuf) obj);
                return readNetItem;
            }
        });
    }
}
