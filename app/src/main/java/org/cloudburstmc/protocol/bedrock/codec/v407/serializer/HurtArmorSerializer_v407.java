package org.cloudburstmc.protocol.bedrock.codec.v407.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.HurtArmorPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class HurtArmorSerializer_v407 implements BedrockPacketSerializer<HurtArmorPacket> {
    public static final HurtArmorSerializer_v407 INSTANCE = new HurtArmorSerializer_v407();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, HurtArmorPacket packet) {
        VarInts.writeInt(buffer, packet.getCause());
        VarInts.writeInt(buffer, packet.getDamage());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, HurtArmorPacket packet) {
        packet.setCause(VarInts.readInt(buffer));
        packet.setDamage(VarInts.readInt(buffer));
    }
}
