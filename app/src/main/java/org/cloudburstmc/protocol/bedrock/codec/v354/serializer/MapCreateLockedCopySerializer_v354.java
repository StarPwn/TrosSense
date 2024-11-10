package org.cloudburstmc.protocol.bedrock.codec.v354.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.MapCreateLockedCopyPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class MapCreateLockedCopySerializer_v354 implements BedrockPacketSerializer<MapCreateLockedCopyPacket> {
    public static final MapCreateLockedCopySerializer_v354 INSTANCE = new MapCreateLockedCopySerializer_v354();

    protected MapCreateLockedCopySerializer_v354() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, MapCreateLockedCopyPacket packet) {
        VarInts.writeLong(buffer, packet.getOriginalMapId());
        VarInts.writeLong(buffer, packet.getNewMapId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, MapCreateLockedCopyPacket packet) {
        packet.setOriginalMapId(VarInts.readLong(buffer));
        packet.setNewMapId(VarInts.readLong(buffer));
    }
}
