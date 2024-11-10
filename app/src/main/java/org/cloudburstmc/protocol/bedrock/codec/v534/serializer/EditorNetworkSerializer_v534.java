package org.cloudburstmc.protocol.bedrock.codec.v534.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.EditorNetworkPacket;

/* loaded from: classes5.dex */
public class EditorNetworkSerializer_v534 implements BedrockPacketSerializer<EditorNetworkPacket> {
    public static final EditorNetworkSerializer_v534 INSTANCE = new EditorNetworkSerializer_v534();

    protected EditorNetworkSerializer_v534() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, EditorNetworkPacket packet) {
        helper.writeTag(buffer, packet.getPayload());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, EditorNetworkPacket packet) {
        packet.setPayload(helper.readTag(buffer));
    }
}
