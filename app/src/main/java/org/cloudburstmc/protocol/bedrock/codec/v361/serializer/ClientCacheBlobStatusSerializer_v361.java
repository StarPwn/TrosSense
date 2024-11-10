package org.cloudburstmc.protocol.bedrock.codec.v361.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.LongList;
import java.util.function.LongConsumer;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ClientCacheBlobStatusPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class ClientCacheBlobStatusSerializer_v361 implements BedrockPacketSerializer<ClientCacheBlobStatusPacket> {
    public static final ClientCacheBlobStatusSerializer_v361 INSTANCE = new ClientCacheBlobStatusSerializer_v361();

    protected ClientCacheBlobStatusSerializer_v361() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(final ByteBuf buffer, BedrockCodecHelper helper, ClientCacheBlobStatusPacket packet) {
        LongList nacks = packet.getNaks();
        LongList acks = packet.getAcks();
        VarInts.writeUnsignedInt(buffer, nacks.size());
        VarInts.writeUnsignedInt(buffer, acks.size());
        buffer.getClass();
        nacks.forEach(new LongConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v361.serializer.ClientCacheBlobStatusSerializer_v361$$ExternalSyntheticLambda0
            @Override // java.util.function.LongConsumer
            public final void accept(long j) {
                ByteBuf.this.writeLongLE(j);
            }
        });
        buffer.getClass();
        acks.forEach(new LongConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v361.serializer.ClientCacheBlobStatusSerializer_v361$$ExternalSyntheticLambda0
            @Override // java.util.function.LongConsumer
            public final void accept(long j) {
                ByteBuf.this.writeLongLE(j);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ClientCacheBlobStatusPacket packet) {
        int naksLength = VarInts.readUnsignedInt(buffer);
        int acksLength = VarInts.readUnsignedInt(buffer);
        LongList naks = packet.getNaks();
        for (int i = 0; i < naksLength; i++) {
            naks.add(buffer.readLongLE());
        }
        LongList acks = packet.getAcks();
        for (int i2 = 0; i2 < acksLength; i2++) {
            acks.add(buffer.readLongLE());
        }
    }
}
