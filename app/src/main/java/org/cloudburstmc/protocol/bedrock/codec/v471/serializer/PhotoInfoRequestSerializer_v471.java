package org.cloudburstmc.protocol.bedrock.codec.v471.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.PhotoInfoRequestPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class PhotoInfoRequestSerializer_v471 implements BedrockPacketSerializer<PhotoInfoRequestPacket> {
    public static final PhotoInfoRequestSerializer_v471 INSTANCE = new PhotoInfoRequestSerializer_v471();

    protected PhotoInfoRequestSerializer_v471() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, PhotoInfoRequestPacket packet) {
        VarInts.writeLong(buffer, packet.getPhotoId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, PhotoInfoRequestPacket packet) {
        packet.setPhotoId(VarInts.readLong(buffer));
    }
}
