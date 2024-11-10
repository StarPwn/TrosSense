package org.cloudburstmc.protocol.bedrock.netty.codec.packet;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.netty.BedrockPacketWrapper;

/* loaded from: classes5.dex */
public class BedrockPacketCodec_v2 extends BedrockPacketCodec {
    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec
    public void encodeHeader(ByteBuf buf, BedrockPacketWrapper msg) {
        buf.writeByte(msg.getPacketId());
        buf.writeByte(msg.getSenderSubClientId());
        buf.writeByte(msg.getTargetSubClientId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec
    public void decodeHeader(ByteBuf buf, BedrockPacketWrapper msg) {
        msg.setPacketId(buf.readUnsignedByte());
        msg.setSenderSubClientId(buf.readUnsignedByte());
        msg.setTargetSubClientId(buf.readUnsignedByte());
    }
}
