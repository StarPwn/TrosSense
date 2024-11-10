package org.cloudburstmc.protocol.bedrock.codec.v407.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.PositionTrackingDBClientRequestPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class PositionTrackingDBClientRequestSerializer_v407 implements BedrockPacketSerializer<PositionTrackingDBClientRequestPacket> {
    public static final PositionTrackingDBClientRequestSerializer_v407 INSTANCE = new PositionTrackingDBClientRequestSerializer_v407();
    protected static final PositionTrackingDBClientRequestPacket.Action[] ACTIONS = PositionTrackingDBClientRequestPacket.Action.values();

    protected PositionTrackingDBClientRequestSerializer_v407() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, PositionTrackingDBClientRequestPacket packet) {
        buffer.writeByte(packet.getAction().ordinal());
        VarInts.writeInt(buffer, packet.getTrackingId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, PositionTrackingDBClientRequestPacket packet) {
        packet.setAction(ACTIONS[buffer.readByte()]);
        packet.setTrackingId(VarInts.readInt(buffer));
    }
}
