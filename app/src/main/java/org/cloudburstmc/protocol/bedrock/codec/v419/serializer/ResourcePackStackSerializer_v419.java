package org.cloudburstmc.protocol.bedrock.codec.v419.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePackStackSerializer_v291;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackStackPacket;

/* loaded from: classes5.dex */
public class ResourcePackStackSerializer_v419 extends ResourcePackStackSerializer_v291 {
    public static final ResourcePackStackSerializer_v419 INSTANCE = new ResourcePackStackSerializer_v419();

    protected ResourcePackStackSerializer_v419() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePackStackSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ResourcePackStackPacket packet) {
        super.serialize(buffer, helper, packet);
        helper.writeString(buffer, packet.getGameVersion());
        helper.writeExperiments(buffer, packet.getExperiments());
        buffer.writeBoolean(packet.isExperimentsPreviouslyToggled());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePackStackSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ResourcePackStackPacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setGameVersion(helper.readString(buffer));
        helper.readExperiments(buffer, packet.getExperiments());
        packet.setExperimentsPreviouslyToggled(buffer.readBoolean());
    }
}
