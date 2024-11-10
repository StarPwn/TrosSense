package org.cloudburstmc.protocol.bedrock.codec.v440.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.SyncEntityPropertyPacket;

/* loaded from: classes5.dex */
public class SyncEntityPropertySerializer_v440 implements BedrockPacketSerializer<SyncEntityPropertyPacket> {
    public static final SyncEntityPropertySerializer_v440 INSTANCE = new SyncEntityPropertySerializer_v440();

    protected SyncEntityPropertySerializer_v440() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, SyncEntityPropertyPacket packet) {
        helper.writeTag(buffer, packet.getData());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, SyncEntityPropertyPacket packet) {
        packet.setData((NbtMap) helper.readTag(buffer, NbtMap.class));
    }
}
