package com.trossense.sdk;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v589.serializer.StartGameSerializer_v589;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;

/* loaded from: classes3.dex */
public class aa extends StartGameSerializer_v589 {
    @Override // org.cloudburstmc.protocol.bedrock.codec.v589.serializer.StartGameSerializer_v589, org.cloudburstmc.protocol.bedrock.codec.v582.serializer.StartGameSerializer_v582, org.cloudburstmc.protocol.bedrock.codec.v544.serializer.StartGameSerializer_v544, org.cloudburstmc.protocol.bedrock.codec.v527.serializer.StartGameSerializer_v527, org.cloudburstmc.protocol.bedrock.codec.v440.serializer.StartGameSerializer_v440, org.cloudburstmc.protocol.bedrock.codec.v428.serializer.StartGameSerializer_v428, org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, StartGamePacket startGamePacket) {
        super.deserialize(byteBuf, bedrockCodecHelper, startGamePacket);
        byte[] bArr = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bArr);
        ((ae) startGamePacket).a(bArr);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v589.serializer.StartGameSerializer_v589, org.cloudburstmc.protocol.bedrock.codec.v582.serializer.StartGameSerializer_v582, org.cloudburstmc.protocol.bedrock.codec.v544.serializer.StartGameSerializer_v544, org.cloudburstmc.protocol.bedrock.codec.v527.serializer.StartGameSerializer_v527, org.cloudburstmc.protocol.bedrock.codec.v440.serializer.StartGameSerializer_v440, org.cloudburstmc.protocol.bedrock.codec.v428.serializer.StartGameSerializer_v428, org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, StartGamePacket startGamePacket) {
        super.serialize(byteBuf, bedrockCodecHelper, startGamePacket);
        byteBuf.writeBytes(((ae) startGamePacket).a());
    }
}
