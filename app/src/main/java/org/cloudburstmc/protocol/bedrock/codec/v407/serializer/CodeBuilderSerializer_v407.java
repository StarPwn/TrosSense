package org.cloudburstmc.protocol.bedrock.codec.v407.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.CodeBuilderPacket;

/* loaded from: classes5.dex */
public class CodeBuilderSerializer_v407 implements BedrockPacketSerializer<CodeBuilderPacket> {
    public static final CodeBuilderSerializer_v407 INSTANCE = new CodeBuilderSerializer_v407();

    protected CodeBuilderSerializer_v407() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, CodeBuilderPacket packet) {
        helper.writeString(buffer, packet.getUrl());
        buffer.writeBoolean(packet.isOpening());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, CodeBuilderPacket packet) {
        packet.setUrl(helper.readString(buffer));
        packet.setOpening(buffer.readBoolean());
    }
}
