package org.cloudburstmc.protocol.bedrock.codec.v388.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.InteractPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class InteractSerializer_v388 implements BedrockPacketSerializer<InteractPacket> {
    public static final InteractSerializer_v388 INSTANCE = new InteractSerializer_v388();
    private static final InteractPacket.Action[] ACTIONS = InteractPacket.Action.values();

    protected InteractSerializer_v388() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, InteractPacket packet) {
        buffer.writeByte(packet.getAction().ordinal());
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
        if (packet.getAction() == InteractPacket.Action.MOUSEOVER || packet.getAction() == InteractPacket.Action.LEAVE_VEHICLE) {
            helper.writeVector3f(buffer, packet.getMousePosition());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, InteractPacket packet) {
        packet.setAction(ACTIONS[buffer.readUnsignedByte()]);
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        if (packet.getAction() == InteractPacket.Action.MOUSEOVER || packet.getAction() == InteractPacket.Action.LEAVE_VEHICLE) {
            packet.setMousePosition(helper.readVector3f(buffer));
        }
    }
}
