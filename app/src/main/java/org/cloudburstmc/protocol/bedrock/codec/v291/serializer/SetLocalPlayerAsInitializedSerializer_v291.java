package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.SetLocalPlayerAsInitializedPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class SetLocalPlayerAsInitializedSerializer_v291 implements BedrockPacketSerializer<SetLocalPlayerAsInitializedPacket> {
    public static final SetLocalPlayerAsInitializedSerializer_v291 INSTANCE = new SetLocalPlayerAsInitializedSerializer_v291();

    protected SetLocalPlayerAsInitializedSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, SetLocalPlayerAsInitializedPacket packet) {
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, SetLocalPlayerAsInitializedPacket packet) {
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
    }
}
