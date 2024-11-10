package org.cloudburstmc.protocol.bedrock.codec.v465.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v440.serializer.AddVolumeEntitySerializer_v440;
import org.cloudburstmc.protocol.bedrock.packet.AddVolumeEntityPacket;

/* loaded from: classes5.dex */
public class AddVolumeEntitySerializer_v465 extends AddVolumeEntitySerializer_v440 {
    public static final AddVolumeEntitySerializer_v465 INSTANCE = new AddVolumeEntitySerializer_v465();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v440.serializer.AddVolumeEntitySerializer_v440, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, AddVolumeEntityPacket packet) {
        super.serialize(buffer, helper, packet);
        helper.writeString(buffer, packet.getEngineVersion());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v440.serializer.AddVolumeEntitySerializer_v440, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, AddVolumeEntityPacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setEngineVersion(helper.readString(buffer));
    }
}
