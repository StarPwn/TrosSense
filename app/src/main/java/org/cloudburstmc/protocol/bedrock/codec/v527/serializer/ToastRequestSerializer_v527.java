package org.cloudburstmc.protocol.bedrock.codec.v527.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ToastRequestPacket;

/* loaded from: classes5.dex */
public class ToastRequestSerializer_v527 implements BedrockPacketSerializer<ToastRequestPacket> {
    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ToastRequestPacket packet) {
        helper.writeString(buffer, packet.getTitle());
        helper.writeString(buffer, packet.getContent());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ToastRequestPacket packet) {
        packet.setTitle(helper.readString(buffer));
        packet.setContent(helper.readString(buffer));
    }
}
