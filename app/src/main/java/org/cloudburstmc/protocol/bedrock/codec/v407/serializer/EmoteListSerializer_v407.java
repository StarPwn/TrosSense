package org.cloudburstmc.protocol.bedrock.codec.v407.serializer;

import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.EmoteListPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class EmoteListSerializer_v407 implements BedrockPacketSerializer<EmoteListPacket> {
    public static final EmoteListSerializer_v407 INSTANCE = new EmoteListSerializer_v407();

    protected EmoteListSerializer_v407() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, final BedrockCodecHelper helper, EmoteListPacket packet) {
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
        List<UUID> pieceIds = packet.getPieceIds();
        helper.getClass();
        helper.writeArray(buffer, pieceIds, new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.EmoteListSerializer_v407$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper.this.writeUuid((ByteBuf) obj, (UUID) obj2);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, final BedrockCodecHelper helper, EmoteListPacket packet) {
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        List<UUID> pieceIds = packet.getPieceIds();
        helper.getClass();
        helper.readArray(buffer, pieceIds, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.EmoteListSerializer_v407$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper.this.readUuid((ByteBuf) obj);
            }
        });
    }
}
