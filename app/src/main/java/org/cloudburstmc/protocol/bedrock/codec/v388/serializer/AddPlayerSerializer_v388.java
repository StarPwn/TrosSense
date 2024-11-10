package org.cloudburstmc.protocol.bedrock.codec.v388.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddPlayerSerializer_v291;
import org.cloudburstmc.protocol.bedrock.packet.AddPlayerPacket;

/* loaded from: classes5.dex */
public class AddPlayerSerializer_v388 extends AddPlayerSerializer_v291 {
    public static final AddPlayerSerializer_v388 INSTANCE = new AddPlayerSerializer_v388();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddPlayerSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, AddPlayerPacket packet) {
        super.serialize(buffer, helper, packet);
        buffer.writeIntLE(packet.getBuildPlatform());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddPlayerSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, AddPlayerPacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setBuildPlatform(buffer.readIntLE());
    }
}
