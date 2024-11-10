package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.PhotoTransferPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class PhotoTransferSerializer_v291 implements BedrockPacketSerializer<PhotoTransferPacket> {
    public static final PhotoTransferSerializer_v291 INSTANCE = new PhotoTransferSerializer_v291();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, PhotoTransferPacket packet) {
        helper.writeString(buffer, packet.getName());
        byte[] data = packet.getData();
        VarInts.writeUnsignedInt(buffer, data.length);
        buffer.writeBytes(data);
        helper.writeString(buffer, packet.getBookId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, PhotoTransferPacket packet) {
        packet.setName(helper.readString(buffer));
        byte[] data = new byte[VarInts.readUnsignedInt(buffer)];
        buffer.readBytes(data);
        packet.setData(data);
        packet.setBookId(helper.readString(buffer));
    }
}
