package org.cloudburstmc.protocol.bedrock.codec.v465.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.HurtArmorSerializer_v407;
import org.cloudburstmc.protocol.bedrock.packet.HurtArmorPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class HurtArmorSerializer_v465 extends HurtArmorSerializer_v407 {
    public static final HurtArmorSerializer_v465 INSTANCE = new HurtArmorSerializer_v465();

    protected HurtArmorSerializer_v465() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.serializer.HurtArmorSerializer_v407, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, HurtArmorPacket packet) {
        super.serialize(buffer, helper, packet);
        VarInts.writeUnsignedLong(buffer, packet.getArmorSlots());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.serializer.HurtArmorSerializer_v407, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, HurtArmorPacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setArmorSlots(VarInts.readUnsignedLong(buffer));
    }
}
