package org.cloudburstmc.protocol.bedrock.codec.v361.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongListIterator;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class LevelChunkSerializer_v361 implements BedrockPacketSerializer<LevelChunkPacket> {
    public static final LevelChunkSerializer_v361 INSTANCE = new LevelChunkSerializer_v361();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, LevelChunkPacket packet) {
        VarInts.writeInt(buffer, packet.getChunkX());
        VarInts.writeInt(buffer, packet.getChunkZ());
        VarInts.writeUnsignedInt(buffer, packet.getSubChunksLength());
        buffer.writeBoolean(packet.isCachingEnabled());
        if (packet.isCachingEnabled()) {
            LongList blobIds = packet.getBlobIds();
            VarInts.writeUnsignedInt(buffer, blobIds.size());
            LongListIterator it2 = blobIds.iterator();
            while (it2.hasNext()) {
                long blobId = it2.next().longValue();
                buffer.writeLongLE(blobId);
            }
        }
        helper.writeByteBuf(buffer, packet.getData());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, LevelChunkPacket packet) {
        packet.setChunkX(VarInts.readInt(buffer));
        packet.setChunkZ(VarInts.readInt(buffer));
        packet.setSubChunksLength(VarInts.readUnsignedInt(buffer));
        packet.setCachingEnabled(buffer.readBoolean());
        if (packet.isCachingEnabled()) {
            LongList blobIds = packet.getBlobIds();
            int length = VarInts.readUnsignedInt(buffer);
            for (int i = 0; i < length; i++) {
                blobIds.add(buffer.readLongLE());
            }
        }
        packet.setData(helper.readByteBuf(buffer));
    }
}
