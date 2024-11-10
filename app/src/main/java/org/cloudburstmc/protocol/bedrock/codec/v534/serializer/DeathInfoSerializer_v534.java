package org.cloudburstmc.protocol.bedrock.codec.v534.serializer;

import io.netty.buffer.ByteBuf;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda3;
import org.cloudburstmc.protocol.bedrock.packet.DeathInfoPacket;

/* loaded from: classes5.dex */
public class DeathInfoSerializer_v534 implements BedrockPacketSerializer<DeathInfoPacket> {
    public static final DeathInfoSerializer_v534 INSTANCE = new DeathInfoSerializer_v534();

    protected DeathInfoSerializer_v534() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, DeathInfoPacket packet) {
        helper.writeString(buffer, packet.getCauseAttackName());
        List<String> messageList = packet.getMessageList();
        helper.getClass();
        helper.writeArray(buffer, messageList, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14(helper));
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, DeathInfoPacket packet) {
        packet.setCauseAttackName(helper.readString(buffer));
        List<String> messageList = packet.getMessageList();
        helper.getClass();
        helper.readArray(buffer, messageList, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda3(helper));
    }
}
