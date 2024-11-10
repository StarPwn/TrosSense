package org.cloudburstmc.protocol.bedrock.codec.v486.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongListIterator;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.LevelChunkSerializer_v361;
import org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class LevelChunkSerializer_v486 extends LevelChunkSerializer_v361 {
    public static final LevelChunkSerializer_v486 INSTANCE = new LevelChunkSerializer_v486();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v361.serializer.LevelChunkSerializer_v361, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, LevelChunkPacket packet) {
        writeChunkLocation(buffer, packet);
        if (!packet.isRequestSubChunks()) {
            VarInts.writeUnsignedInt(buffer, packet.getSubChunksLength());
        } else if (packet.getSubChunkLimit() < 0) {
            VarInts.writeUnsignedInt(buffer, -1);
        } else {
            VarInts.writeUnsignedInt(buffer, -2);
            buffer.writeShortLE(packet.getSubChunkLimit());
        }
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

    @Override // org.cloudburstmc.protocol.bedrock.codec.v361.serializer.LevelChunkSerializer_v361, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, LevelChunkPacket packet) {
        readChunkLocation(buffer, packet);
        int subChunksCount = VarInts.readUnsignedInt(buffer);
        if (subChunksCount >= 0) {
            packet.setSubChunksLength(subChunksCount);
        } else {
            packet.setRequestSubChunks(true);
            if (subChunksCount == -1) {
                packet.setSubChunkLimit(subChunksCount);
            } else if (subChunksCount == -2) {
                packet.setSubChunkLimit(buffer.readUnsignedShortLE());
            }
        }
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

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeChunkLocation(ByteBuf buffer, LevelChunkPacket packet) {
        VarInts.writeInt(buffer, packet.getChunkX());
        VarInts.writeInt(buffer, packet.getChunkZ());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void readChunkLocation(ByteBuf buffer, LevelChunkPacket packet) {
        packet.setChunkX(VarInts.readInt(buffer));
        packet.setChunkZ(VarInts.readInt(buffer));
    }
}
