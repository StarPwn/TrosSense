package org.cloudburstmc.protocol.bedrock.codec.v407.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.DebugInfoPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class DebugInfoSerializer_v407 implements BedrockPacketSerializer<DebugInfoPacket> {
    public static final DebugInfoSerializer_v407 INSTANCE = new DebugInfoSerializer_v407();

    protected DebugInfoSerializer_v407() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, DebugInfoPacket packet) {
        VarInts.writeLong(buffer, packet.getUniqueEntityId());
        helper.writeString(buffer, packet.getData());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, DebugInfoPacket packet) {
        packet.setUniqueEntityId(VarInts.readLong(buffer));
        packet.setData(helper.readString(buffer));
    }
}
