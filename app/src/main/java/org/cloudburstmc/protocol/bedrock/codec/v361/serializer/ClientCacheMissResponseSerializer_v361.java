package org.cloudburstmc.protocol.bedrock.codec.v361.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ClientCacheMissResponsePacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class ClientCacheMissResponseSerializer_v361 implements BedrockPacketSerializer<ClientCacheMissResponsePacket> {
    public static final ClientCacheMissResponseSerializer_v361 INSTANCE = new ClientCacheMissResponseSerializer_v361();

    protected ClientCacheMissResponseSerializer_v361() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ClientCacheMissResponsePacket packet) {
        Long2ObjectMap<ByteBuf> blobs = packet.getBlobs();
        VarInts.writeUnsignedInt(buffer, blobs.size());
        ObjectIterator<Long2ObjectMap.Entry<ByteBuf>> it2 = blobs.long2ObjectEntrySet().iterator();
        while (it2.hasNext()) {
            Long2ObjectMap.Entry<ByteBuf> entry = it2.next();
            buffer.writeLongLE(entry.getLongKey());
            helper.writeByteBuf(buffer, entry.getValue());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ClientCacheMissResponsePacket packet) {
        Long2ObjectMap<ByteBuf> blobs = packet.getBlobs();
        int length = VarInts.readUnsignedInt(buffer);
        for (int i = 0; i < length; i++) {
            long id = buffer.readLongLE();
            ByteBuf blob = helper.readByteBuf(buffer);
            blobs.put(id, (long) blob);
        }
    }
}
