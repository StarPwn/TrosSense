package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ScriptCustomEventPacket;

/* loaded from: classes5.dex */
public class ScriptCustomEventSerializer_v291 implements BedrockPacketSerializer<ScriptCustomEventPacket> {
    public static final ScriptCustomEventSerializer_v291 INSTANCE = new ScriptCustomEventSerializer_v291();

    protected ScriptCustomEventSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ScriptCustomEventPacket packet) {
        helper.writeString(buffer, packet.getEventName());
        helper.writeString(buffer, packet.getData());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ScriptCustomEventPacket packet) {
        packet.setEventName(helper.readString(buffer));
        packet.setData(helper.readString(buffer));
    }
}
