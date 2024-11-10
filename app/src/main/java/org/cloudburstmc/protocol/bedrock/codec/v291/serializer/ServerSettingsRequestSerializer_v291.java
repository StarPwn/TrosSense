package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsRequestPacket;

/* loaded from: classes5.dex */
public class ServerSettingsRequestSerializer_v291 implements BedrockPacketSerializer<ServerSettingsRequestPacket> {
    public static final ServerSettingsRequestSerializer_v291 INSTANCE = new ServerSettingsRequestSerializer_v291();

    protected ServerSettingsRequestSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ServerSettingsRequestPacket packet) {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ServerSettingsRequestPacket packet) {
    }
}
