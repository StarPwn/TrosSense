package org.cloudburstmc.protocol.bedrock.codec.v662.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetEntityMotionSerializer_v291;
import org.cloudburstmc.protocol.bedrock.packet.SetEntityMotionPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class SetEntityMotionSerializer_v662 extends SetEntityMotionSerializer_v291 {
    public static final SetEntityMotionSerializer_v662 INSTANCE = new SetEntityMotionSerializer_v662();

    protected SetEntityMotionSerializer_v662() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetEntityMotionSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, SetEntityMotionPacket packet) {
        super.serialize(buffer, helper, packet);
        VarInts.writeUnsignedLong(buffer, packet.getTick());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetEntityMotionSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, SetEntityMotionPacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setTick(VarInts.readUnsignedLong(buffer));
    }
}
