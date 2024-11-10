package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.PurchaseReceiptPacket;

/* loaded from: classes5.dex */
public class PurchaseReceiptSerializer_v291 implements BedrockPacketSerializer<PurchaseReceiptPacket> {
    public static final PurchaseReceiptSerializer_v291 INSTANCE = new PurchaseReceiptSerializer_v291();

    protected PurchaseReceiptSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, PurchaseReceiptPacket packet) {
        List<String> receipts = packet.getReceipts();
        helper.getClass();
        helper.writeArray(buffer, receipts, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14(helper));
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, PurchaseReceiptPacket packet) {
        List<String> receipts = packet.getReceipts();
        helper.getClass();
        helper.readArray(buffer, receipts, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda3(helper));
    }
}
