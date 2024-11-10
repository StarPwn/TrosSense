package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.Set;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class UpdateBlockSerializer_v291 implements BedrockPacketSerializer<UpdateBlockPacket> {
    public static final UpdateBlockSerializer_v291 INSTANCE = new UpdateBlockSerializer_v291();

    protected UpdateBlockSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateBlockPacket packet) {
        helper.writeBlockPosition(buffer, packet.getBlockPosition());
        VarInts.writeUnsignedInt(buffer, packet.getDefinition().getRuntimeId());
        int flagValue = 0;
        for (UpdateBlockPacket.Flag flag : packet.getFlags()) {
            flagValue |= 1 << flag.ordinal();
        }
        VarInts.writeUnsignedInt(buffer, flagValue);
        VarInts.writeUnsignedInt(buffer, packet.getDataLayer());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateBlockPacket packet) {
        packet.setBlockPosition(helper.readBlockPosition(buffer));
        packet.setDefinition(helper.getBlockDefinitions().getDefinition(VarInts.readUnsignedInt(buffer)));
        int flagValue = VarInts.readUnsignedInt(buffer);
        Set<UpdateBlockPacket.Flag> flags = packet.getFlags();
        for (UpdateBlockPacket.Flag flag : UpdateBlockPacket.Flag.values()) {
            if (((1 << flag.ordinal()) & flagValue) != 0) {
                flags.add(flag);
            }
        }
        packet.setDataLayer(VarInts.readUnsignedInt(buffer));
    }
}
