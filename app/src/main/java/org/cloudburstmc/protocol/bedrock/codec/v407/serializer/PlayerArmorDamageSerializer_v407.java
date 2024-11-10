package org.cloudburstmc.protocol.bedrock.codec.v407.serializer;

import io.netty.buffer.ByteBuf;
import java.util.Set;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.PlayerArmorDamageFlag;
import org.cloudburstmc.protocol.bedrock.packet.PlayerArmorDamagePacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class PlayerArmorDamageSerializer_v407 implements BedrockPacketSerializer<PlayerArmorDamagePacket> {
    public static final PlayerArmorDamageSerializer_v407 INSTANCE = new PlayerArmorDamageSerializer_v407();
    private static final PlayerArmorDamageFlag[] FLAGS = PlayerArmorDamageFlag.values();

    protected PlayerArmorDamageSerializer_v407() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, PlayerArmorDamagePacket packet) {
        int flags = 0;
        for (PlayerArmorDamageFlag flag : packet.getFlags()) {
            flags |= 1 << flag.ordinal();
        }
        buffer.writeByte(flags);
        int[] damage = packet.getDamage();
        for (PlayerArmorDamageFlag flag2 : packet.getFlags()) {
            int value = damage[flag2.ordinal()];
            VarInts.writeInt(buffer, value);
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, PlayerArmorDamagePacket packet) {
        int flagsVal = buffer.readUnsignedByte();
        Set<PlayerArmorDamageFlag> flags = packet.getFlags();
        int[] damage = packet.getDamage();
        for (int i = 0; i < 4; i++) {
            if (((1 << i) & flagsVal) != 0) {
                flags.add(FLAGS[i]);
                damage[i] = VarInts.readInt(buffer);
            }
        }
    }
}
