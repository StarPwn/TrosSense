package org.cloudburstmc.protocol.bedrock.codec.v527.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.ee.LessonAction;
import org.cloudburstmc.protocol.bedrock.packet.LessonProgressPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class LessonProgressSerializer_v527 implements BedrockPacketSerializer<LessonProgressPacket> {
    private static final LessonAction[] ACTIONS = LessonAction.values();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, LessonProgressPacket packet) {
        VarInts.writeInt(buffer, packet.getAction().ordinal());
        VarInts.writeInt(buffer, packet.getScore());
        helper.writeString(buffer, packet.getActivityId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, LessonProgressPacket packet) {
        packet.setAction(ACTIONS[VarInts.readInt(buffer)]);
        packet.setScore(VarInts.readInt(buffer));
        packet.setActivityId(helper.readString(buffer));
    }
}
