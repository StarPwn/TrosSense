package org.cloudburstmc.protocol.bedrock.codec.v560.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.UpdateClientInputLocksPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class UpdateClientInputLocksSerializer_v560 implements BedrockPacketSerializer<UpdateClientInputLocksPacket> {
    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateClientInputLocksPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getLockComponentData());
        helper.writeVector3f(buffer, packet.getServerPosition());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateClientInputLocksPacket packet) {
        packet.setLockComponentData(VarInts.readUnsignedInt(buffer));
        packet.setServerPosition(helper.readVector3f(buffer));
    }
}
