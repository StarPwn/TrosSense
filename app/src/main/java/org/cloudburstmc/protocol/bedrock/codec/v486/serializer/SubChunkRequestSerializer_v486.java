package org.cloudburstmc.protocol.bedrock.codec.v486.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.Consumer;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v471.serializer.SubChunkRequestSerializer_v471;
import org.cloudburstmc.protocol.bedrock.packet.SubChunkRequestPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class SubChunkRequestSerializer_v486 extends SubChunkRequestSerializer_v471 {
    public static final SubChunkRequestSerializer_v486 INSTANCE = new SubChunkRequestSerializer_v486();

    protected SubChunkRequestSerializer_v486() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v471.serializer.SubChunkRequestSerializer_v471, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(final ByteBuf buffer, BedrockCodecHelper helper, SubChunkRequestPacket packet) {
        VarInts.writeInt(buffer, packet.getDimension());
        helper.writeVector3i(buffer, packet.getSubChunkPosition());
        buffer.writeIntLE(packet.getPositionOffsets().size());
        packet.getPositionOffsets().forEach(new Consumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v486.serializer.SubChunkRequestSerializer_v486$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SubChunkRequestSerializer_v486.this.m2090xbbe9af16(buffer, (Vector3i) obj);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v471.serializer.SubChunkRequestSerializer_v471, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, SubChunkRequestPacket packet) {
        packet.setDimension(VarInts.readInt(buffer));
        packet.setSubChunkPosition(helper.readVector3i(buffer));
        int size = buffer.readIntLE();
        for (int i = 0; i < size; i++) {
            packet.getPositionOffsets().add(readSubChunkOffset(buffer));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: writeSubChunkOffset, reason: merged with bridge method [inline-methods] */
    public void m2090xbbe9af16(ByteBuf buffer, Vector3i offsetPosition) {
        buffer.writeByte(offsetPosition.getX());
        buffer.writeByte(offsetPosition.getY());
        buffer.writeByte(offsetPosition.getZ());
    }

    protected Vector3i readSubChunkOffset(ByteBuf buffer) {
        return Vector3i.from((int) buffer.readByte(), (int) buffer.readByte(), (int) buffer.readByte());
    }
}
