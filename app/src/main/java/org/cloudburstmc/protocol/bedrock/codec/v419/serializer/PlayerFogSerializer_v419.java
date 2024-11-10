package org.cloudburstmc.protocol.bedrock.codec.v419.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.PlayerFogPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;

/* loaded from: classes5.dex */
public class PlayerFogSerializer_v419 implements BedrockPacketSerializer<PlayerFogPacket> {
    public static final PlayerFogSerializer_v419 INSTANCE = new PlayerFogSerializer_v419();

    protected PlayerFogSerializer_v419() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, PlayerFogPacket packet) {
        helper.writeArray(buffer, packet.getFogStack(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.PlayerFogSerializer_v419$$ExternalSyntheticLambda0
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                ((BedrockCodecHelper) obj2).writeString((ByteBuf) obj, (String) obj3);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, PlayerFogPacket packet) {
        helper.readArray(buffer, packet.getFogStack(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.PlayerFogSerializer_v419$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                String readString;
                readString = ((BedrockCodecHelper) obj2).readString((ByteBuf) obj);
                return readString;
            }
        });
    }
}
