package org.cloudburstmc.protocol.bedrock.codec.v313.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.AvailableEntityIdentifiersPacket;

/* loaded from: classes5.dex */
public class AvailableEntityIdentifiersSerializer_v313 implements BedrockPacketSerializer<AvailableEntityIdentifiersPacket> {
    public static final AvailableEntityIdentifiersSerializer_v313 INSTANCE = new AvailableEntityIdentifiersSerializer_v313();

    protected AvailableEntityIdentifiersSerializer_v313() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, AvailableEntityIdentifiersPacket packet) {
        helper.writeTag(buffer, packet.getIdentifiers());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, AvailableEntityIdentifiersPacket packet) {
        packet.setIdentifiers((NbtMap) helper.readTag(buffer, NbtMap.class));
    }
}
