package org.cloudburstmc.protocol.bedrock.codec.v419.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.CameraShakeType;
import org.cloudburstmc.protocol.bedrock.packet.CameraShakePacket;

/* loaded from: classes5.dex */
public class CameraShakeSerializer_v419 implements BedrockPacketSerializer<CameraShakePacket> {
    public static final CameraShakeSerializer_v419 INSTANCE = new CameraShakeSerializer_v419();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, CameraShakePacket packet) {
        buffer.writeFloatLE(packet.getIntensity());
        buffer.writeFloatLE(packet.getDuration());
        buffer.writeByte(packet.getShakeType().ordinal());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, CameraShakePacket packet) {
        packet.setIntensity(buffer.readFloatLE());
        packet.setDuration(buffer.readFloatLE());
        packet.setShakeType(CameraShakeType.values()[buffer.readByte()]);
    }
}
