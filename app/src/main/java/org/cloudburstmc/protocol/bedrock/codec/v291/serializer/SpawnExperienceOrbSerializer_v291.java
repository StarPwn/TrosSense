package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.SpawnExperienceOrbPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class SpawnExperienceOrbSerializer_v291 implements BedrockPacketSerializer<SpawnExperienceOrbPacket> {
    public static final SpawnExperienceOrbSerializer_v291 INSTANCE = new SpawnExperienceOrbSerializer_v291();

    protected SpawnExperienceOrbSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, SpawnExperienceOrbPacket packet) {
        helper.writeVector3f(buffer, packet.getPosition());
        VarInts.writeInt(buffer, packet.getAmount());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, SpawnExperienceOrbPacket packet) {
        packet.setPosition(helper.readVector3f(buffer));
        packet.setAmount(VarInts.readInt(buffer));
    }
}
