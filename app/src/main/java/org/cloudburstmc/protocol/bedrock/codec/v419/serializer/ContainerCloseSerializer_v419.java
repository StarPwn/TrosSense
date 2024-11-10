package org.cloudburstmc.protocol.bedrock.codec.v419.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ContainerClosePacket;

/* loaded from: classes5.dex */
public class ContainerCloseSerializer_v419 implements BedrockPacketSerializer<ContainerClosePacket> {
    public static final ContainerCloseSerializer_v419 INSTANCE = new ContainerCloseSerializer_v419();

    private ContainerCloseSerializer_v419() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ContainerClosePacket packet) {
        buffer.writeByte(packet.getId());
        buffer.writeBoolean(packet.isServerInitiated());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ContainerClosePacket packet) {
        packet.setId(buffer.readByte());
        packet.setServerInitiated(buffer.readBoolean());
    }
}
