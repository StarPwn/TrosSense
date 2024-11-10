package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.Set;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.BlockSyncType;
import org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateBlockSyncedPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class UpdateBlockSyncedSerializer_v291 implements BedrockPacketSerializer<UpdateBlockSyncedPacket> {
    public static final UpdateBlockSyncedSerializer_v291 INSTANCE = new UpdateBlockSyncedSerializer_v291();

    protected UpdateBlockSyncedSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateBlockSyncedPacket packet) {
        helper.writeBlockPosition(buffer, packet.getBlockPosition());
        VarInts.writeUnsignedInt(buffer, packet.getDefinition().getRuntimeId());
        int flagValue = 0;
        for (UpdateBlockPacket.Flag flag : packet.getFlags()) {
            flagValue |= 1 << flag.ordinal();
        }
        VarInts.writeUnsignedInt(buffer, flagValue);
        VarInts.writeUnsignedInt(buffer, packet.getDataLayer());
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
        VarInts.writeUnsignedLong(buffer, packet.getEntityBlockSyncType().ordinal());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateBlockSyncedPacket packet) {
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
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        packet.setEntityBlockSyncType(BlockSyncType.values()[(int) VarInts.readUnsignedLong(buffer)]);
    }
}
