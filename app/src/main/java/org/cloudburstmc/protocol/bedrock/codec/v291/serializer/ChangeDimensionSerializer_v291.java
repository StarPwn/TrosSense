package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ChangeDimensionPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class ChangeDimensionSerializer_v291 implements BedrockPacketSerializer<ChangeDimensionPacket> {
    public static final ChangeDimensionSerializer_v291 INSTANCE = new ChangeDimensionSerializer_v291();

    protected ChangeDimensionSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ChangeDimensionPacket packet) {
        VarInts.writeInt(buffer, packet.getDimension());
        helper.writeVector3f(buffer, packet.getPosition());
        buffer.writeBoolean(packet.isRespawn());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ChangeDimensionPacket packet) {
        packet.setDimension(VarInts.readInt(buffer));
        packet.setPosition(helper.readVector3f(buffer));
        packet.setRespawn(buffer.readBoolean());
    }
}
