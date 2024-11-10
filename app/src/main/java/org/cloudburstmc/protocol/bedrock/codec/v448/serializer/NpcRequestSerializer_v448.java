package org.cloudburstmc.protocol.bedrock.codec.v448.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.NpcRequestSerializer_v291;
import org.cloudburstmc.protocol.bedrock.packet.NpcRequestPacket;

/* loaded from: classes5.dex */
public class NpcRequestSerializer_v448 extends NpcRequestSerializer_v291 {
    public static final NpcRequestSerializer_v448 INSTANCE = new NpcRequestSerializer_v448();

    protected NpcRequestSerializer_v448() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.NpcRequestSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, NpcRequestPacket packet) {
        super.serialize(buffer, helper, packet);
        helper.writeString(buffer, packet.getSceneName());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.NpcRequestSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, NpcRequestPacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setSceneName(helper.readString(buffer));
    }
}
