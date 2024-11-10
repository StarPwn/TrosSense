package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.NpcRequestType;
import org.cloudburstmc.protocol.bedrock.packet.NpcRequestPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class NpcRequestSerializer_v291 implements BedrockPacketSerializer<NpcRequestPacket> {
    public static final NpcRequestSerializer_v291 INSTANCE = new NpcRequestSerializer_v291();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, NpcRequestPacket packet) {
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
        buffer.writeByte(packet.getRequestType().ordinal());
        helper.writeString(buffer, packet.getCommand());
        buffer.writeByte(packet.getActionType());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, NpcRequestPacket packet) {
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        packet.setRequestType(NpcRequestType.values()[buffer.readUnsignedByte()]);
        packet.setCommand(helper.readString(buffer));
        packet.setActionType(buffer.readUnsignedByte());
    }
}
