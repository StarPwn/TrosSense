package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackChunkDataPacket;

/* loaded from: classes5.dex */
public class ResourcePackChunkDataSerializer_v291 implements BedrockPacketSerializer<ResourcePackChunkDataPacket> {
    public static final ResourcePackChunkDataSerializer_v291 INSTANCE = new ResourcePackChunkDataSerializer_v291();

    protected ResourcePackChunkDataSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ResourcePackChunkDataPacket packet) {
        String packInfo = packet.getPackId().toString() + (packet.getPackVersion() == null ? "" : '_' + packet.getPackVersion());
        helper.writeString(buffer, packInfo);
        buffer.writeIntLE(packet.getChunkIndex());
        buffer.writeLongLE(packet.getProgress());
        ByteBuf data = packet.getData();
        buffer.writeIntLE(data.readableBytes());
        buffer.writeBytes(data, data.readerIndex(), data.writerIndex());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ResourcePackChunkDataPacket packet) {
        String[] packInfo = helper.readString(buffer).split("_");
        packet.setPackId(UUID.fromString(packInfo[0]));
        if (packInfo.length > 1) {
            packet.setPackVersion(packInfo[1]);
        }
        packet.setChunkIndex(buffer.readIntLE());
        packet.setProgress(buffer.readLongLE());
        ByteBuf data = buffer.readRetainedSlice(buffer.readIntLE());
        packet.setData(data);
    }
}
