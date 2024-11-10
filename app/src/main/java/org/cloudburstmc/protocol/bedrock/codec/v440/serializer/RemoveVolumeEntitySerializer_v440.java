package org.cloudburstmc.protocol.bedrock.codec.v440.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.RemoveVolumeEntityPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class RemoveVolumeEntitySerializer_v440 implements BedrockPacketSerializer<RemoveVolumeEntityPacket> {
    public static final RemoveVolumeEntitySerializer_v440 INSTANCE = new RemoveVolumeEntitySerializer_v440();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, RemoveVolumeEntityPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, RemoveVolumeEntityPacket packet) {
        packet.setId(VarInts.readUnsignedInt(buffer));
    }
}
