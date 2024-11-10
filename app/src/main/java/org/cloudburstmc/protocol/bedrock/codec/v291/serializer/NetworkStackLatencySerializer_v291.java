package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.NetworkStackLatencyPacket;

/* loaded from: classes5.dex */
public class NetworkStackLatencySerializer_v291 implements BedrockPacketSerializer<NetworkStackLatencyPacket> {
    public static final NetworkStackLatencySerializer_v291 INSTANCE = new NetworkStackLatencySerializer_v291();

    protected NetworkStackLatencySerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, NetworkStackLatencyPacket packet) {
        buffer.writeLongLE(packet.getTimestamp());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, NetworkStackLatencyPacket packet) {
        packet.setTimestamp(buffer.readLongLE());
    }
}
