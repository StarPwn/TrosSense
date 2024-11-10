package org.cloudburstmc.protocol.bedrock.codec.v471.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.HeightMapDataType;
import org.cloudburstmc.protocol.bedrock.data.SubChunkData;
import org.cloudburstmc.protocol.bedrock.data.SubChunkRequestResult;
import org.cloudburstmc.protocol.bedrock.packet.SubChunkPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class SubChunkSerializer_v471 implements BedrockPacketSerializer<SubChunkPacket> {
    protected static final int HEIGHT_MAP_LENGTH = 256;
    public static final SubChunkSerializer_v471 INSTANCE = new SubChunkSerializer_v471();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, SubChunkPacket packet) {
        VarInts.writeInt(buffer, packet.getDimension());
        SubChunkData subChunk = packet.getSubChunks().get(0);
        serializeSubChunk(buffer, helper, packet, subChunk);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, SubChunkPacket packet) {
        packet.setDimension(VarInts.readInt(buffer));
        SubChunkData subChunk = deserializeSubChunk(buffer, helper, packet);
        packet.getSubChunks().add(subChunk);
    }

    protected void serializeSubChunk(ByteBuf buffer, BedrockCodecHelper helper, SubChunkPacket packet, SubChunkData subChunk) {
        helper.writeVector3i(buffer, subChunk.getPosition());
        helper.writeByteBuf(buffer, subChunk.getData());
        VarInts.writeInt(buffer, subChunk.getResult().ordinal());
        buffer.writeByte(subChunk.getHeightMapType().ordinal());
        ByteBuf heightMapBuf = subChunk.getHeightMapData();
        buffer.writeBytes(heightMapBuf, heightMapBuf.readerIndex(), 256);
    }

    protected SubChunkData deserializeSubChunk(ByteBuf buffer, BedrockCodecHelper helper, SubChunkPacket packet) {
        SubChunkData subChunk = new SubChunkData();
        subChunk.setPosition(helper.readVector3i(buffer));
        subChunk.setData(helper.readByteBuf(buffer));
        subChunk.setResult(SubChunkRequestResult.values()[VarInts.readInt(buffer)]);
        subChunk.setHeightMapType(HeightMapDataType.values()[buffer.readByte()]);
        subChunk.setHeightMapData(buffer.readRetainedSlice(256));
        return subChunk;
    }
}
