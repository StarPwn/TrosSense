package org.cloudburstmc.protocol.bedrock.codec.v388.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.MultiplayerMode;
import org.cloudburstmc.protocol.bedrock.packet.MultiplayerSettingsPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class MultiplayerSettingsSerializer_v388 implements BedrockPacketSerializer<MultiplayerSettingsPacket> {
    public static final MultiplayerSettingsSerializer_v388 INSTANCE = new MultiplayerSettingsSerializer_v388();
    private static final MultiplayerMode[] VALUES = MultiplayerMode.values();

    private MultiplayerSettingsSerializer_v388() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, MultiplayerSettingsPacket packet) {
        VarInts.writeInt(buffer, packet.getMode().ordinal());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, MultiplayerSettingsPacket packet) {
        packet.setMode(VALUES[VarInts.readInt(buffer)]);
    }
}
