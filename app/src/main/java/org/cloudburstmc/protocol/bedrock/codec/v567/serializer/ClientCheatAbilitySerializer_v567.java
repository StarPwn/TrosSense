package org.cloudburstmc.protocol.bedrock.codec.v567.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ClientCheatAbilityPacket;

/* loaded from: classes5.dex */
public class ClientCheatAbilitySerializer_v567 implements BedrockPacketSerializer<ClientCheatAbilityPacket> {
    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ClientCheatAbilityPacket packet) {
        helper.writePlayerAbilities(buffer, packet);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ClientCheatAbilityPacket packet) {
        helper.readPlayerAbilities(buffer, packet);
    }
}
