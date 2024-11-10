package org.cloudburstmc.protocol.bedrock.codec.v486.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.Consumer;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v475.serializer.SubChunkSerializer_v475;
import org.cloudburstmc.protocol.bedrock.data.HeightMapDataType;
import org.cloudburstmc.protocol.bedrock.data.SubChunkData;
import org.cloudburstmc.protocol.bedrock.data.SubChunkRequestResult;
import org.cloudburstmc.protocol.bedrock.packet.SubChunkPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class SubChunkSerializer_v486 extends SubChunkSerializer_v475 {
    public static final SubChunkSerializer_v486 INSTANCE = new SubChunkSerializer_v486();

    protected SubChunkSerializer_v486() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v475.serializer.SubChunkSerializer_v475, org.cloudburstmc.protocol.bedrock.codec.v471.serializer.SubChunkSerializer_v471, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(final ByteBuf buffer, final BedrockCodecHelper helper, final SubChunkPacket packet) {
        buffer.writeBoolean(packet.isCacheEnabled());
        VarInts.writeInt(buffer, packet.getDimension());
        helper.writeVector3i(buffer, packet.getCenterPosition());
        buffer.writeIntLE(packet.getSubChunks().size());
        packet.getSubChunks().forEach(new Consumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v486.serializer.SubChunkSerializer_v486$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SubChunkSerializer_v486.this.m2091x2b1c6ed(buffer, helper, packet, (SubChunkData) obj);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v475.serializer.SubChunkSerializer_v475, org.cloudburstmc.protocol.bedrock.codec.v471.serializer.SubChunkSerializer_v471, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, SubChunkPacket packet) {
        packet.setCacheEnabled(buffer.readBoolean());
        packet.setDimension(VarInts.readInt(buffer));
        packet.setCenterPosition(helper.readVector3i(buffer));
        int size = buffer.readIntLE();
        for (int i = 0; i < size; i++) {
            packet.getSubChunks().add(deserializeSubChunk(buffer, helper, packet));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v475.serializer.SubChunkSerializer_v475, org.cloudburstmc.protocol.bedrock.codec.v471.serializer.SubChunkSerializer_v471
    /* renamed from: serializeSubChunk, reason: merged with bridge method [inline-methods] */
    public void m2091x2b1c6ed(ByteBuf buffer, BedrockCodecHelper helper, SubChunkPacket packet, SubChunkData subChunk) {
        writeSubChunkOffset(buffer, subChunk.getPosition());
        buffer.writeByte(subChunk.getResult().ordinal());
        if (subChunk.getResult() != SubChunkRequestResult.SUCCESS_ALL_AIR || !packet.isCacheEnabled()) {
            helper.writeByteBuf(buffer, subChunk.getData());
        }
        buffer.writeByte(subChunk.getHeightMapType().ordinal());
        if (subChunk.getHeightMapType() == HeightMapDataType.HAS_DATA) {
            ByteBuf heightMapBuf = subChunk.getHeightMapData();
            buffer.writeBytes(heightMapBuf, heightMapBuf.readerIndex(), 256);
        }
        if (packet.isCacheEnabled()) {
            buffer.writeLongLE(subChunk.getBlobId());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v475.serializer.SubChunkSerializer_v475, org.cloudburstmc.protocol.bedrock.codec.v471.serializer.SubChunkSerializer_v471
    protected SubChunkData deserializeSubChunk(ByteBuf buffer, BedrockCodecHelper helper, SubChunkPacket packet) {
        SubChunkData subChunk = new SubChunkData();
        subChunk.setPosition(readSubChunkOffset(buffer));
        subChunk.setResult(SubChunkRequestResult.values()[buffer.readByte()]);
        if (subChunk.getResult() != SubChunkRequestResult.SUCCESS_ALL_AIR || !packet.isCacheEnabled()) {
            subChunk.setData(helper.readByteBuf(buffer));
        }
        subChunk.setHeightMapType(HeightMapDataType.values()[buffer.readByte()]);
        if (subChunk.getHeightMapType() == HeightMapDataType.HAS_DATA) {
            subChunk.setHeightMapData(buffer.readRetainedSlice(256));
        }
        if (packet.isCacheEnabled()) {
            subChunk.setBlobId(buffer.readLongLE());
        }
        return subChunk;
    }

    protected void writeSubChunkOffset(ByteBuf buffer, Vector3i offsetPosition) {
        buffer.writeByte(offsetPosition.getX());
        buffer.writeByte(offsetPosition.getY());
        buffer.writeByte(offsetPosition.getZ());
    }

    protected Vector3i readSubChunkOffset(ByteBuf buffer) {
        return Vector3i.from((int) buffer.readByte(), (int) buffer.readByte(), (int) buffer.readByte());
    }
}
