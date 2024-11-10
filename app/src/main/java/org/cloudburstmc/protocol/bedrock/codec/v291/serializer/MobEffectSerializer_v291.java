package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.MobEffectPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class MobEffectSerializer_v291 implements BedrockPacketSerializer<MobEffectPacket> {
    public static final MobEffectSerializer_v291 INSTANCE = new MobEffectSerializer_v291();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, MobEffectPacket packet) {
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
        buffer.writeByte(packet.getEvent().ordinal());
        VarInts.writeInt(buffer, packet.getEffectId());
        VarInts.writeInt(buffer, packet.getAmplifier());
        buffer.writeBoolean(packet.isParticles());
        VarInts.writeInt(buffer, packet.getDuration());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, MobEffectPacket packet) {
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        packet.setEvent(MobEffectPacket.Event.values()[buffer.readUnsignedByte()]);
        packet.setEffectId(VarInts.readInt(buffer));
        packet.setAmplifier(VarInts.readInt(buffer));
        packet.setParticles(buffer.readBoolean());
        packet.setDuration(VarInts.readInt(buffer));
    }
}
