package org.cloudburstmc.protocol.bedrock.codec.v544.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiFunction;
import java.util.function.ObjIntConsumer;
import java.util.function.ToLongFunction;
import org.cloudburstmc.math.vector.Vector2i;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.NetworkChunkPublisherUpdateSerializer_v313;
import org.cloudburstmc.protocol.bedrock.packet.NetworkChunkPublisherUpdatePacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class NetworkChunkPublisherUpdateSerializer_v544 extends NetworkChunkPublisherUpdateSerializer_v313 {
    @Override // org.cloudburstmc.protocol.bedrock.codec.v313.serializer.NetworkChunkPublisherUpdateSerializer_v313, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, NetworkChunkPublisherUpdatePacket packet) {
        super.serialize(buffer, helper, packet);
        helper.writeArray(buffer, packet.getSavedChunks(), new ObjIntConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.serializer.NetworkChunkPublisherUpdateSerializer_v544$$ExternalSyntheticLambda0
            @Override // java.util.function.ObjIntConsumer
            public final void accept(Object obj, int i) {
                ((ByteBuf) obj).writeIntLE(i);
            }
        }, new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.serializer.NetworkChunkPublisherUpdateSerializer_v544$$ExternalSyntheticLambda1
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                NetworkChunkPublisherUpdateSerializer_v544.this.writeSavedChunk((ByteBuf) obj, (BedrockCodecHelper) obj2, (Vector2i) obj3);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v313.serializer.NetworkChunkPublisherUpdateSerializer_v313, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, NetworkChunkPublisherUpdatePacket packet) {
        super.deserialize(buffer, helper, packet);
        helper.readArray(buffer, packet.getSavedChunks(), new ToLongFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.serializer.NetworkChunkPublisherUpdateSerializer_v544$$ExternalSyntheticLambda2
            @Override // java.util.function.ToLongFunction
            public final long applyAsLong(Object obj) {
                int readIntLE;
                readIntLE = ((ByteBuf) obj).readIntLE();
                return readIntLE;
            }
        }, new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.serializer.NetworkChunkPublisherUpdateSerializer_v544$$ExternalSyntheticLambda3
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return NetworkChunkPublisherUpdateSerializer_v544.this.readSavedChunk((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeSavedChunk(ByteBuf buffer, BedrockCodecHelper helper, Vector2i savedChunk) {
        VarInts.writeInt(buffer, savedChunk.getX());
        VarInts.writeInt(buffer, savedChunk.getY());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Vector2i readSavedChunk(ByteBuf buffer, BedrockCodecHelper helper) {
        return Vector2i.from(VarInts.readInt(buffer), VarInts.readInt(buffer));
    }
}
