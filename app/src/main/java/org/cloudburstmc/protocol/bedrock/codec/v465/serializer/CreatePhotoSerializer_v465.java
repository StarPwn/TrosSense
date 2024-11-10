package org.cloudburstmc.protocol.bedrock.codec.v465.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.CreatePhotoPacket;

/* loaded from: classes5.dex */
public class CreatePhotoSerializer_v465 implements BedrockPacketSerializer<CreatePhotoPacket> {
    public static final CreatePhotoSerializer_v465 INSTANCE = new CreatePhotoSerializer_v465();

    protected CreatePhotoSerializer_v465() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, CreatePhotoPacket packet) {
        buffer.writeLongLE(packet.getId());
        helper.writeString(buffer, packet.getPhotoName());
        helper.writeString(buffer, packet.getPhotoItemName());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, CreatePhotoPacket packet) {
        packet.setId(buffer.readLongLE());
        packet.setPhotoName(helper.readString(buffer));
        packet.setPhotoItemName(helper.readString(buffer));
    }
}
