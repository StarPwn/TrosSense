package org.cloudburstmc.protocol.bedrock.codec.v475.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v471.serializer.SubChunkSerializer_v471;
import org.cloudburstmc.protocol.bedrock.data.HeightMapDataType;
import org.cloudburstmc.protocol.bedrock.data.SubChunkData;
import org.cloudburstmc.protocol.bedrock.data.SubChunkRequestResult;
import org.cloudburstmc.protocol.bedrock.packet.SubChunkPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class SubChunkSerializer_v475 extends SubChunkSerializer_v471 {
    public static final SubChunkSerializer_v475 INSTANCE = new SubChunkSerializer_v475();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v471.serializer.SubChunkSerializer_v471, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, SubChunkPacket packet) {
        super.serialize(buffer, helper, packet);
        buffer.writeBoolean(packet.isCacheEnabled());
        if (packet.isCacheEnabled()) {
            buffer.writeLongLE(packet.getSubChunks().get(0).getBlobId());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v471.serializer.SubChunkSerializer_v471, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, SubChunkPacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setCacheEnabled(buffer.readBoolean());
        if (packet.isCacheEnabled()) {
            packet.getSubChunks().get(0).setBlobId(buffer.readLongLE());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v471.serializer.SubChunkSerializer_v471
    protected void serializeSubChunk(ByteBuf buffer, BedrockCodecHelper helper, SubChunkPacket packet, SubChunkData subChunk) {
        helper.writeVector3i(buffer, subChunk.getPosition());
        helper.writeByteBuf(buffer, subChunk.getData());
        VarInts.writeInt(buffer, subChunk.getResult().ordinal());
        buffer.writeByte(subChunk.getHeightMapType().ordinal());
        if (subChunk.getHeightMapType() == HeightMapDataType.HAS_DATA) {
            ByteBuf heightMapBuf = subChunk.getHeightMapData();
            buffer.writeBytes(heightMapBuf, heightMapBuf.readerIndex(), 256);
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v471.serializer.SubChunkSerializer_v471
    protected SubChunkData deserializeSubChunk(ByteBuf buffer, BedrockCodecHelper helper, SubChunkPacket packet) {
        SubChunkData subChunk = new SubChunkData();
        subChunk.setPosition(helper.readVector3i(buffer));
        subChunk.setData(helper.readByteBuf(buffer));
        subChunk.setResult(SubChunkRequestResult.values()[VarInts.readInt(buffer)]);
        subChunk.setHeightMapType(HeightMapDataType.values()[buffer.readByte()]);
        if (subChunk.getHeightMapType() == HeightMapDataType.HAS_DATA) {
            subChunk.setHeightMapData(buffer.readRetainedSlice(256));
        }
        return subChunk;
    }
}
