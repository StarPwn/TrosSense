package org.cloudburstmc.protocol.bedrock.codec.v554.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ServerStatsPacket;

/* loaded from: classes5.dex */
public class ServerStatsSerializer_v554 implements BedrockPacketSerializer<ServerStatsPacket> {
    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ServerStatsPacket packet) {
        buffer.writeFloatLE(packet.getServerTime());
        buffer.writeFloatLE(packet.getNetworkTime());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ServerStatsPacket packet) {
        packet.setServerTime(buffer.readFloatLE());
        packet.setNetworkTime(buffer.readFloatLE());
    }
}
