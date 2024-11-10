package org.cloudburstmc.protocol.bedrock.netty.codec.packet;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.netty.BedrockPacketWrapper;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class BedrockPacketCodec_v3 extends BedrockPacketCodec {
    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec
    public void encodeHeader(ByteBuf buf, BedrockPacketWrapper msg) {
        int header = 0 | (msg.getPacketId() & 1023);
        VarInts.writeUnsignedInt(buf, header | ((msg.getSenderSubClientId() & 3) << 10) | ((msg.getTargetSubClientId() & 3) << 12));
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec
    public void decodeHeader(ByteBuf buf, BedrockPacketWrapper msg) {
        int header = VarInts.readUnsignedInt(buf);
        msg.setPacketId(header & 1023);
        msg.setSenderSubClientId((header >> 10) & 3);
        msg.setTargetSubClientId((header >> 12) & 3);
    }
}
