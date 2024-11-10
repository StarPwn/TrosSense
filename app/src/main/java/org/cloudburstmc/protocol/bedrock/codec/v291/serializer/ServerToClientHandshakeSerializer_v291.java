package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.annotation.NoEncryption;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ServerToClientHandshakePacket;

@NoEncryption
/* loaded from: classes5.dex */
public class ServerToClientHandshakeSerializer_v291 implements BedrockPacketSerializer<ServerToClientHandshakePacket> {
    public static final ServerToClientHandshakeSerializer_v291 INSTANCE = new ServerToClientHandshakeSerializer_v291();

    protected ServerToClientHandshakeSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ServerToClientHandshakePacket packet) {
        helper.writeString(buffer, packet.getJwt());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ServerToClientHandshakePacket packet) {
        packet.setJwt(helper.readString(buffer));
    }
}
