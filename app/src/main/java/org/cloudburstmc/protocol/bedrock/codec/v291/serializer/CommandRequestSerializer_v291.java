package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.CommandRequestPacket;

/* loaded from: classes5.dex */
public class CommandRequestSerializer_v291 implements BedrockPacketSerializer<CommandRequestPacket> {
    public static final CommandRequestSerializer_v291 INSTANCE = new CommandRequestSerializer_v291();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, CommandRequestPacket packet) {
        helper.writeString(buffer, packet.getCommand());
        helper.writeCommandOrigin(buffer, packet.getCommandOriginData());
        buffer.writeBoolean(packet.isInternal());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, CommandRequestPacket packet) {
        packet.setCommand(helper.readString(buffer));
        packet.setCommandOriginData(helper.readCommandOrigin(buffer));
        packet.setInternal(buffer.readBoolean());
    }
}
