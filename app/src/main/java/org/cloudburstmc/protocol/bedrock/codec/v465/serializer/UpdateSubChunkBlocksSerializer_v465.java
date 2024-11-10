package org.cloudburstmc.protocol.bedrock.codec.v465.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.BlockChangeEntry;
import org.cloudburstmc.protocol.bedrock.packet.UpdateSubChunkBlocksPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class UpdateSubChunkBlocksSerializer_v465 implements BedrockPacketSerializer<UpdateSubChunkBlocksPacket> {
    public static final UpdateSubChunkBlocksSerializer_v465 INSTANCE = new UpdateSubChunkBlocksSerializer_v465();
    private static final BlockChangeEntry.MessageType[] VALUES = BlockChangeEntry.MessageType.values();

    protected UpdateSubChunkBlocksSerializer_v465() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateSubChunkBlocksPacket packet) {
        VarInts.writeInt(buffer, packet.getChunkX());
        VarInts.writeUnsignedInt(buffer, packet.getChunkY());
        VarInts.writeInt(buffer, packet.getChunkZ());
        helper.writeArray(buffer, packet.getStandardBlocks(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.UpdateSubChunkBlocksSerializer_v465$$ExternalSyntheticLambda0
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                UpdateSubChunkBlocksSerializer_v465.this.writeBlockChangeEntry((ByteBuf) obj, (BedrockCodecHelper) obj2, (BlockChangeEntry) obj3);
            }
        });
        helper.writeArray(buffer, packet.getExtraBlocks(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.UpdateSubChunkBlocksSerializer_v465$$ExternalSyntheticLambda0
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                UpdateSubChunkBlocksSerializer_v465.this.writeBlockChangeEntry((ByteBuf) obj, (BedrockCodecHelper) obj2, (BlockChangeEntry) obj3);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateSubChunkBlocksPacket packet) {
        packet.setChunkX(VarInts.readInt(buffer));
        packet.setChunkY(VarInts.readUnsignedInt(buffer));
        packet.setChunkZ(VarInts.readInt(buffer));
        helper.readArray(buffer, packet.getStandardBlocks(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.UpdateSubChunkBlocksSerializer_v465$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return UpdateSubChunkBlocksSerializer_v465.this.readBlockChangeEntry((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        helper.readArray(buffer, packet.getExtraBlocks(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.UpdateSubChunkBlocksSerializer_v465$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return UpdateSubChunkBlocksSerializer_v465.this.readBlockChangeEntry((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeBlockChangeEntry(ByteBuf buffer, BedrockCodecHelper helper, BlockChangeEntry entry) {
        helper.writeBlockPosition(buffer, entry.getPosition());
        VarInts.writeUnsignedInt(buffer, entry.getDefinition().getRuntimeId());
        VarInts.writeUnsignedInt(buffer, entry.getUpdateFlags());
        VarInts.writeUnsignedLong(buffer, entry.getMessageEntityId());
        VarInts.writeUnsignedInt(buffer, entry.getMessageType().ordinal());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BlockChangeEntry readBlockChangeEntry(ByteBuf buffer, BedrockCodecHelper helper) {
        return new BlockChangeEntry(helper.readBlockPosition(buffer), helper.getBlockDefinitions().getDefinition(VarInts.readUnsignedInt(buffer)), VarInts.readUnsignedInt(buffer), VarInts.readUnsignedLong(buffer), VALUES[VarInts.readUnsignedInt(buffer)]);
    }
}
