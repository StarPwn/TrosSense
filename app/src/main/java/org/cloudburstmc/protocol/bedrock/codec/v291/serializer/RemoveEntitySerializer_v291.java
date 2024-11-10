package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.RemoveEntityPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class RemoveEntitySerializer_v291 implements BedrockPacketSerializer<RemoveEntityPacket> {
    public static final RemoveEntitySerializer_v291 INSTANCE = new RemoveEntitySerializer_v291();

    protected RemoveEntitySerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, RemoveEntityPacket packet) {
        VarInts.writeLong(buffer, packet.getUniqueEntityId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, RemoveEntityPacket packet) {
        packet.setUniqueEntityId(VarInts.readLong(buffer));
    }
}
