package org.cloudburstmc.protocol.bedrock.codec.v622.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.DisconnectSerializer_v291;
import org.cloudburstmc.protocol.bedrock.data.DisconnectFailReason;
import org.cloudburstmc.protocol.bedrock.packet.DisconnectPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class DisconnectSerializer_v622 extends DisconnectSerializer_v291 {
    public static final DisconnectSerializer_v622 INSTANCE = new DisconnectSerializer_v622();

    protected DisconnectSerializer_v622() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.DisconnectSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, DisconnectPacket packet) {
        VarInts.writeInt(buffer, packet.getReason().ordinal());
        super.serialize(buffer, helper, packet);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.DisconnectSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, DisconnectPacket packet) {
        packet.setReason(DisconnectFailReason.values()[VarInts.readInt(buffer)]);
        super.deserialize(buffer, helper, packet);
    }
}
