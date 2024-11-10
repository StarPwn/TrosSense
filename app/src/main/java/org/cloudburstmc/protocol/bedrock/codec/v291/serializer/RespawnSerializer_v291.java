package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.RespawnPacket;

/* loaded from: classes5.dex */
public class RespawnSerializer_v291 implements BedrockPacketSerializer<RespawnPacket> {
    public static final RespawnSerializer_v291 INSTANCE = new RespawnSerializer_v291();

    protected RespawnSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, RespawnPacket packet) {
        helper.writeVector3f(buffer, packet.getPosition());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, RespawnPacket packet) {
        packet.setPosition(helper.readVector3f(buffer));
    }
}
