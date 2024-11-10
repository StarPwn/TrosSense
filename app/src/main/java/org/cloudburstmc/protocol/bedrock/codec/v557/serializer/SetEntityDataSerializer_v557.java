package org.cloudburstmc.protocol.bedrock.codec.v557.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetEntityDataSerializer_v291;
import org.cloudburstmc.protocol.bedrock.packet.SetEntityDataPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class SetEntityDataSerializer_v557 extends SetEntityDataSerializer_v291 {
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetEntityDataSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, SetEntityDataPacket packet) {
        super.serialize(buffer, helper, packet);
        helper.writeEntityProperties(buffer, packet.getProperties());
        VarInts.writeUnsignedLong(buffer, packet.getTick());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetEntityDataSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, SetEntityDataPacket packet) {
        super.deserialize(buffer, helper, packet);
        helper.readEntityProperties(buffer, packet.getProperties());
        packet.setTick(VarInts.readUnsignedLong(buffer));
    }
}
