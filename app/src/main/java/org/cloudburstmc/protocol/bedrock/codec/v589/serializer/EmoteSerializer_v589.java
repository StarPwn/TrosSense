package org.cloudburstmc.protocol.bedrock.codec.v589.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.EmoteSerializer_v388;
import org.cloudburstmc.protocol.bedrock.packet.EmotePacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class EmoteSerializer_v589 extends EmoteSerializer_v388 {
    public static final EmoteSerializer_v589 INSTANCE = new EmoteSerializer_v589();

    protected EmoteSerializer_v589() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.serializer.EmoteSerializer_v388, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, EmotePacket packet) {
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
        helper.writeString(buffer, packet.getEmoteId());
        helper.writeString(buffer, packet.getXuid());
        helper.writeString(buffer, packet.getPlatformId());
        writeFlags(buffer, helper, packet.getFlags());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.serializer.EmoteSerializer_v388, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, EmotePacket packet) {
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        packet.setEmoteId(helper.readString(buffer));
        packet.setXuid(helper.readString(buffer));
        packet.setPlatformId(helper.readString(buffer));
        readFlags(buffer, helper, packet.getFlags());
    }
}
