package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.MobArmorEquipmentPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class MobArmorEquipmentSerializer_v291 implements BedrockPacketSerializer<MobArmorEquipmentPacket> {
    public static final MobArmorEquipmentSerializer_v291 INSTANCE = new MobArmorEquipmentSerializer_v291();

    protected MobArmorEquipmentSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, MobArmorEquipmentPacket packet) {
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
        helper.writeItem(buffer, packet.getHelmet());
        helper.writeItem(buffer, packet.getChestplate());
        helper.writeItem(buffer, packet.getLeggings());
        helper.writeItem(buffer, packet.getBoots());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, MobArmorEquipmentPacket packet) {
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        packet.setHelmet(helper.readItem(buffer));
        packet.setChestplate(helper.readItem(buffer));
        packet.setLeggings(helper.readItem(buffer));
        packet.setBoots(helper.readItem(buffer));
    }
}
