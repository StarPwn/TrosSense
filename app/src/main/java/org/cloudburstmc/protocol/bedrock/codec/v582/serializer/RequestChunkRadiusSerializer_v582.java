package org.cloudburstmc.protocol.bedrock.codec.v582.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.RequestChunkRadiusSerializer_v291;
import org.cloudburstmc.protocol.bedrock.packet.RequestChunkRadiusPacket;

/* loaded from: classes5.dex */
public class RequestChunkRadiusSerializer_v582 extends RequestChunkRadiusSerializer_v291 {
    public static final RequestChunkRadiusSerializer_v582 INSTANCE = new RequestChunkRadiusSerializer_v582();

    protected RequestChunkRadiusSerializer_v582() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.RequestChunkRadiusSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, RequestChunkRadiusPacket packet) {
        super.serialize(buffer, helper, packet);
        buffer.writeByte(packet.getMaxRadius());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.RequestChunkRadiusSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, RequestChunkRadiusPacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setMaxRadius(buffer.readUnsignedByte());
    }
}
