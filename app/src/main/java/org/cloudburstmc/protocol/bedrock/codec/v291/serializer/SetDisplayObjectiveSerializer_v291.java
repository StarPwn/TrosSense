package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.SetDisplayObjectivePacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class SetDisplayObjectiveSerializer_v291 implements BedrockPacketSerializer<SetDisplayObjectivePacket> {
    public static final SetDisplayObjectiveSerializer_v291 INSTANCE = new SetDisplayObjectiveSerializer_v291();

    protected SetDisplayObjectiveSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, SetDisplayObjectivePacket packet) {
        helper.writeString(buffer, packet.getDisplaySlot());
        helper.writeString(buffer, packet.getObjectiveId());
        helper.writeString(buffer, packet.getDisplayName());
        helper.writeString(buffer, packet.getCriteria());
        VarInts.writeInt(buffer, packet.getSortOrder());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, SetDisplayObjectivePacket packet) {
        packet.setDisplaySlot(helper.readString(buffer));
        packet.setObjectiveId(helper.readString(buffer));
        packet.setDisplayName(helper.readString(buffer));
        packet.setCriteria(helper.readString(buffer));
        packet.setSortOrder(VarInts.readInt(buffer));
    }
}
