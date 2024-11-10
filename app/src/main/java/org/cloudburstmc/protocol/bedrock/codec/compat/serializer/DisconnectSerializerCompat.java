package org.cloudburstmc.protocol.bedrock.codec.compat.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.DisconnectFailReason;
import org.cloudburstmc.protocol.bedrock.packet.DisconnectPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class DisconnectSerializerCompat implements BedrockPacketSerializer<DisconnectPacket> {
    private final boolean reasonEnum;

    public DisconnectSerializerCompat(boolean reasonEnum) {
        this.reasonEnum = reasonEnum;
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, DisconnectPacket packet) {
        if (this.reasonEnum) {
            VarInts.writeInt(buffer, packet.getReason().ordinal());
        }
        buffer.writeBoolean(packet.isMessageSkipped());
        if (!packet.isMessageSkipped()) {
            helper.writeString(buffer, packet.getKickMessage());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, DisconnectPacket packet) {
        if (this.reasonEnum) {
            packet.setReason(DisconnectFailReason.values()[VarInts.readInt(buffer)]);
        }
        packet.setMessageSkipped(buffer.readBoolean());
        if (!packet.isMessageSkipped()) {
            packet.setKickMessage(helper.readString(buffer));
        }
    }
}
