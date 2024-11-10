package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.SetDifficultyPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class SetDifficultySerializer_v291 implements BedrockPacketSerializer<SetDifficultyPacket> {
    public static final SetDifficultySerializer_v291 INSTANCE = new SetDifficultySerializer_v291();

    protected SetDifficultySerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, SetDifficultyPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getDifficulty());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, SetDifficultyPacket packet) {
        packet.setDifficulty(VarInts.readUnsignedInt(buffer));
    }
}
