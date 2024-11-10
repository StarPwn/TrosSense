package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ContainerClosePacket;

/* loaded from: classes5.dex */
public class ContainerCloseSerializer_v291 implements BedrockPacketSerializer<ContainerClosePacket> {
    public static final ContainerCloseSerializer_v291 INSTANCE = new ContainerCloseSerializer_v291();

    protected ContainerCloseSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ContainerClosePacket packet) {
        buffer.writeByte(packet.getId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ContainerClosePacket packet) {
        packet.setId(buffer.readByte());
    }
}
