package org.cloudburstmc.protocol.bedrock.codec.v388.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.NetworkSettingsPacket;

/* loaded from: classes5.dex */
public class NetworkSettingsSerializer_v388 implements BedrockPacketSerializer<NetworkSettingsPacket> {
    public static final NetworkSettingsSerializer_v388 INSTANCE = new NetworkSettingsSerializer_v388();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, NetworkSettingsPacket packet) {
        buffer.writeShortLE(packet.getCompressionThreshold());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, NetworkSettingsPacket packet) {
        packet.setCompressionThreshold(buffer.readUnsignedShortLE());
    }
}
