package org.cloudburstmc.protocol.bedrock.codec.v332.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.NetworkStackLatencyPacket;

/* loaded from: classes5.dex */
public class NetworkStackLatencySerializer_v332 implements BedrockPacketSerializer<NetworkStackLatencyPacket> {
    public static final NetworkStackLatencySerializer_v332 INSTANCE = new NetworkStackLatencySerializer_v332();

    protected NetworkStackLatencySerializer_v332() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, NetworkStackLatencyPacket packet) {
        buffer.writeLongLE(packet.getTimestamp());
        buffer.writeBoolean(packet.isFromServer());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, NetworkStackLatencyPacket packet) {
        packet.setTimestamp(buffer.readLongLE());
        packet.setFromServer(buffer.readBoolean());
    }
}
