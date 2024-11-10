package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.SimpleEventType;
import org.cloudburstmc.protocol.bedrock.packet.SimpleEventPacket;

/* loaded from: classes5.dex */
public class SimpleEventSerializer_v291 implements BedrockPacketSerializer<SimpleEventPacket> {
    public static final SimpleEventSerializer_v291 INSTANCE = new SimpleEventSerializer_v291();

    protected SimpleEventSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, SimpleEventPacket packet) {
        buffer.writeShortLE(packet.getEvent().ordinal());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, SimpleEventPacket packet) {
        packet.setEvent(SimpleEventType.values()[buffer.readUnsignedShortLE()]);
    }
}
