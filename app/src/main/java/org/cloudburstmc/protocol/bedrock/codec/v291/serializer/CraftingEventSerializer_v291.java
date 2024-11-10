package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.inventory.CraftingType;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.packet.CraftingEventPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class CraftingEventSerializer_v291 implements BedrockPacketSerializer<CraftingEventPacket> {
    public static final CraftingEventSerializer_v291 INSTANCE = new CraftingEventSerializer_v291();

    protected CraftingEventSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, final BedrockCodecHelper helper, CraftingEventPacket packet) {
        buffer.writeByte(packet.getContainerId());
        VarInts.writeInt(buffer, packet.getType().ordinal());
        helper.writeUuid(buffer, packet.getUuid());
        helper.writeArray(buffer, packet.getInputs(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingEventSerializer_v291$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper.this.writeItem((ByteBuf) obj, (ItemData) obj2);
            }
        });
        helper.writeArray(buffer, packet.getOutputs(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingEventSerializer_v291$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper.this.writeItem((ByteBuf) obj, (ItemData) obj2);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, final BedrockCodecHelper helper, CraftingEventPacket packet) {
        packet.setContainerId(buffer.readByte());
        packet.setType(CraftingType.values()[VarInts.readInt(buffer)]);
        packet.setUuid(helper.readUuid(buffer));
        helper.readArray(buffer, packet.getInputs(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingEventSerializer_v291$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                ItemData readItem;
                readItem = BedrockCodecHelper.this.readItem((ByteBuf) obj);
                return readItem;
            }
        });
        helper.readArray(buffer, packet.getOutputs(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingEventSerializer_v291$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                ItemData readItem;
                readItem = BedrockCodecHelper.this.readItem((ByteBuf) obj);
                return readItem;
            }
        });
    }
}
