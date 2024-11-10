package org.cloudburstmc.protocol.bedrock.codec.v554.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.NetworkSettingsSerializer_v388;
import org.cloudburstmc.protocol.bedrock.data.PacketCompressionAlgorithm;
import org.cloudburstmc.protocol.bedrock.packet.NetworkSettingsPacket;

/* loaded from: classes5.dex */
public class NetworkSettingsSerializer_v554 extends NetworkSettingsSerializer_v388 {
    protected static final PacketCompressionAlgorithm[] ALGORITHMS = PacketCompressionAlgorithm.values();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.serializer.NetworkSettingsSerializer_v388, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, NetworkSettingsPacket packet) {
        super.serialize(buffer, helper, packet);
        buffer.writeShortLE(packet.getCompressionAlgorithm().ordinal());
        buffer.writeBoolean(packet.isClientThrottleEnabled());
        buffer.writeByte(packet.getClientThrottleThreshold());
        buffer.writeFloatLE(packet.getClientThrottleScalar());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.serializer.NetworkSettingsSerializer_v388, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, NetworkSettingsPacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setCompressionAlgorithm(ALGORITHMS[buffer.readUnsignedShortLE()]);
        packet.setClientThrottleEnabled(buffer.readBoolean());
        packet.setClientThrottleThreshold(buffer.readUnsignedByte());
        packet.setClientThrottleScalar(buffer.readFloatLE());
    }
}
