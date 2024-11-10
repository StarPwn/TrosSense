package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.StopSoundPacket;

/* loaded from: classes5.dex */
public class StopSoundSerializer_v291 implements BedrockPacketSerializer<StopSoundPacket> {
    public static final StopSoundSerializer_v291 INSTANCE = new StopSoundSerializer_v291();

    protected StopSoundSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, StopSoundPacket packet) {
        helper.writeString(buffer, packet.getSoundName());
        buffer.writeBoolean(packet.isStoppingAllSound());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, StopSoundPacket packet) {
        packet.setSoundName(helper.readString(buffer));
        packet.setStoppingAllSound(buffer.readBoolean());
    }
}
