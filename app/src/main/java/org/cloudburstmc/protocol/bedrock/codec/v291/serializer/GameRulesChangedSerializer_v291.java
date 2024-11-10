package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.GameRuleData;
import org.cloudburstmc.protocol.bedrock.packet.GameRulesChangedPacket;

/* loaded from: classes5.dex */
public class GameRulesChangedSerializer_v291 implements BedrockPacketSerializer<GameRulesChangedPacket> {
    public static final GameRulesChangedSerializer_v291 INSTANCE = new GameRulesChangedSerializer_v291();

    protected GameRulesChangedSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, GameRulesChangedPacket packet) {
        List<GameRuleData<?>> gameRules = packet.getGameRules();
        helper.getClass();
        helper.writeArray(buffer, gameRules, new GameRulesChangedSerializer_v291$$ExternalSyntheticLambda1(helper));
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, GameRulesChangedPacket packet) {
        List<GameRuleData<?>> gameRules = packet.getGameRules();
        helper.getClass();
        helper.readArray(buffer, gameRules, new GameRulesChangedSerializer_v291$$ExternalSyntheticLambda0(helper));
    }
}
