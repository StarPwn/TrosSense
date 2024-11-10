package org.cloudburstmc.protocol.bedrock.codec.v354.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.OnScreenTextureAnimationPacket;

/* loaded from: classes5.dex */
public class OnScreenTextureAnimationSerializer_v354 implements BedrockPacketSerializer<OnScreenTextureAnimationPacket> {
    public static final OnScreenTextureAnimationSerializer_v354 INSTANCE = new OnScreenTextureAnimationSerializer_v354();

    protected OnScreenTextureAnimationSerializer_v354() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, OnScreenTextureAnimationPacket packet) {
        buffer.writeIntLE((int) packet.getEffectId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, OnScreenTextureAnimationPacket packet) {
        packet.setEffectId(buffer.readUnsignedIntLE());
    }
}
