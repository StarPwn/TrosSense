package org.cloudburstmc.protocol.bedrock.codec.v662.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MobEffectSerializer_v291;
import org.cloudburstmc.protocol.bedrock.packet.MobEffectPacket;

/* loaded from: classes5.dex */
public class MobEffectSerializer_v662 extends MobEffectSerializer_v291 {
    public static final MobEffectSerializer_v662 INSTANCE = new MobEffectSerializer_v662();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MobEffectSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, MobEffectPacket packet) {
        super.serialize(buffer, helper, packet);
        buffer.writeLongLE(packet.getTick());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MobEffectSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, MobEffectPacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setTick(buffer.readLongLE());
    }
}
