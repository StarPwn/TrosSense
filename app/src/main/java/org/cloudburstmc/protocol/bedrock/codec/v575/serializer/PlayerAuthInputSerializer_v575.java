package org.cloudburstmc.protocol.bedrock.codec.v575.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v527.serializer.PlayerAuthInputSerializer_v527;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;

/* loaded from: classes5.dex */
public class PlayerAuthInputSerializer_v575 extends PlayerAuthInputSerializer_v527 {
    @Override // org.cloudburstmc.protocol.bedrock.codec.v428.serializer.PlayerAuthInputSerializer_v428, org.cloudburstmc.protocol.bedrock.codec.v419.serializer.PlayerAuthInputSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.v388.serializer.PlayerAuthInputSerializer_v388, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, PlayerAuthInputPacket packet) {
        super.serialize(buffer, helper, packet);
        helper.writeVector2f(buffer, packet.getAnalogMoveVector());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v428.serializer.PlayerAuthInputSerializer_v428, org.cloudburstmc.protocol.bedrock.codec.v419.serializer.PlayerAuthInputSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.v388.serializer.PlayerAuthInputSerializer_v388, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, PlayerAuthInputPacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setAnalogMoveVector(helper.readVector2f(buffer));
    }
}
