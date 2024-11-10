package org.cloudburstmc.protocol.bedrock.codec.v448.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.NpcDialoguePacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class NpcDialogueSerializer_v448 implements BedrockPacketSerializer<NpcDialoguePacket> {
    public static final NpcDialogueSerializer_v448 INSTANCE = new NpcDialogueSerializer_v448();
    private static final NpcDialoguePacket.Action[] VALUES = NpcDialoguePacket.Action.values();

    protected NpcDialogueSerializer_v448() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, NpcDialoguePacket packet) {
        buffer.writeLongLE(packet.getUniqueEntityId());
        VarInts.writeInt(buffer, packet.getAction().ordinal());
        helper.writeString(buffer, packet.getDialogue());
        helper.writeString(buffer, packet.getSceneName());
        helper.writeString(buffer, packet.getNpcName());
        helper.writeString(buffer, packet.getActionJson());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, NpcDialoguePacket packet) {
        packet.setUniqueEntityId(buffer.readLongLE());
        packet.setAction(VALUES[VarInts.readInt(buffer)]);
        packet.setDialogue(helper.readString(buffer));
        packet.setSceneName(helper.readString(buffer));
        packet.setNpcName(helper.readString(buffer));
        packet.setActionJson(helper.readString(buffer));
    }
}
