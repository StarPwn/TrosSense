package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.TransferPacket;

/* loaded from: classes5.dex */
public class TransferSerializer_v291 implements BedrockPacketSerializer<TransferPacket> {
    public static final TransferSerializer_v291 INSTANCE = new TransferSerializer_v291();

    protected TransferSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, TransferPacket packet) {
        helper.writeString(buffer, packet.getAddress());
        buffer.writeShortLE(packet.getPort());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, TransferPacket packet) {
        packet.setAddress(helper.readString(buffer));
        packet.setPort(buffer.readShortLE());
    }
}
