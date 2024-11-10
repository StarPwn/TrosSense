package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.MapInfoRequestPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class MapInfoRequestSerializer_v291 implements BedrockPacketSerializer<MapInfoRequestPacket> {
    public static final MapInfoRequestSerializer_v291 INSTANCE = new MapInfoRequestSerializer_v291();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, MapInfoRequestPacket packet) {
        VarInts.writeLong(buffer, packet.getUniqueMapId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, MapInfoRequestPacket packet) {
        packet.setUniqueMapId(VarInts.readLong(buffer));
    }
}
