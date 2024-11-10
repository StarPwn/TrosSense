package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ClientToServerHandshakePacket;

/* loaded from: classes5.dex */
public class ClientToServerHandshakeSerializer_v291 implements BedrockPacketSerializer<ClientToServerHandshakePacket> {
    public static final ClientToServerHandshakeSerializer_v291 INSTANCE = new ClientToServerHandshakeSerializer_v291();

    protected ClientToServerHandshakeSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ClientToServerHandshakePacket packet) {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ClientToServerHandshakePacket packet) {
    }
}
