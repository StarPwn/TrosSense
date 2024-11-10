package org.cloudburstmc.protocol.bedrock.codec.v388.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.ResourcePackStackSerializer_v313;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackStackPacket;

/* loaded from: classes5.dex */
public class ResourcePackStackSerializer_v388 extends ResourcePackStackSerializer_v313 {
    public static final ResourcePackStackSerializer_v388 INSTANCE = new ResourcePackStackSerializer_v388();

    protected ResourcePackStackSerializer_v388() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v313.serializer.ResourcePackStackSerializer_v313, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePackStackSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ResourcePackStackPacket packet) {
        super.serialize(buffer, helper, packet);
        helper.writeString(buffer, packet.getGameVersion());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v313.serializer.ResourcePackStackSerializer_v313, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePackStackSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ResourcePackStackPacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setGameVersion(helper.readString(buffer));
    }
}
