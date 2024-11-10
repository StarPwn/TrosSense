package org.cloudburstmc.protocol.bedrock.codec.v388.serializer;

import io.netty.buffer.ByteBuf;
import java.util.Set;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityDeltaSerializer_v291;
import org.cloudburstmc.protocol.bedrock.packet.MoveEntityDeltaPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class MoveEntityDeltaSerializer_v388 extends MoveEntityDeltaSerializer_v291 {
    public static final MoveEntityDeltaSerializer_v388 INSTANCE = new MoveEntityDeltaSerializer_v388();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityDeltaSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, MoveEntityDeltaPacket packet) {
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
        int flagsIndex = buffer.writerIndex();
        buffer.writeShortLE(0);
        int flags = 65535;
        for (MoveEntityDeltaPacket.Flag flag : FLAGS) {
            if (!packet.getFlags().contains(flag)) {
                flags &= ~(1 << flag.ordinal());
            } else {
                TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> writer = this.writers.get(flag);
                if (writer != null) {
                    writer.accept(buffer, helper, packet);
                }
            }
        }
        buffer.setShortLE(flagsIndex, flags);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityDeltaSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, MoveEntityDeltaPacket packet) {
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        int flags = buffer.readUnsignedShortLE();
        Set<MoveEntityDeltaPacket.Flag> flagSet = packet.getFlags();
        for (MoveEntityDeltaPacket.Flag flag : FLAGS) {
            if (((1 << flag.ordinal()) & flags) != 0) {
                flagSet.add(flag);
                TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> reader = this.readers.get(flag);
                if (reader != null) {
                    reader.accept(buffer, helper, packet);
                }
            }
        }
    }
}
