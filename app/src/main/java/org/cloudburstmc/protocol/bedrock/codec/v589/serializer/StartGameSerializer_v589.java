package org.cloudburstmc.protocol.bedrock.codec.v589.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v582.serializer.StartGameSerializer_v582;
import org.cloudburstmc.protocol.bedrock.data.NetworkPermissions;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;

/* loaded from: classes5.dex */
public class StartGameSerializer_v589 extends StartGameSerializer_v582 {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v582.serializer.StartGameSerializer_v582, org.cloudburstmc.protocol.bedrock.codec.v544.serializer.StartGameSerializer_v544, org.cloudburstmc.protocol.bedrock.codec.v527.serializer.StartGameSerializer_v527, org.cloudburstmc.protocol.bedrock.codec.v440.serializer.StartGameSerializer_v440, org.cloudburstmc.protocol.bedrock.codec.v428.serializer.StartGameSerializer_v428, org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        super.serialize(buffer, helper, packet);
        writeNetworkPermissions(buffer, helper, packet.getNetworkPermissions());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v582.serializer.StartGameSerializer_v582, org.cloudburstmc.protocol.bedrock.codec.v544.serializer.StartGameSerializer_v544, org.cloudburstmc.protocol.bedrock.codec.v527.serializer.StartGameSerializer_v527, org.cloudburstmc.protocol.bedrock.codec.v440.serializer.StartGameSerializer_v440, org.cloudburstmc.protocol.bedrock.codec.v428.serializer.StartGameSerializer_v428, org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setNetworkPermissions(readNetworkPermissions(buffer, helper));
    }

    protected NetworkPermissions readNetworkPermissions(ByteBuf buffer, BedrockCodecHelper helper) {
        boolean serverAuthSound = buffer.readBoolean();
        return new NetworkPermissions(serverAuthSound);
    }

    protected void writeNetworkPermissions(ByteBuf buffer, BedrockCodecHelper helper, NetworkPermissions permissions) {
        buffer.writeBoolean(permissions.isServerAuthSounds());
    }
}
