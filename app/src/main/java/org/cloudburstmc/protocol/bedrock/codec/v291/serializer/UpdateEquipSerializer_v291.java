package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.UpdateEquipPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class UpdateEquipSerializer_v291 implements BedrockPacketSerializer<UpdateEquipPacket> {
    public static final UpdateEquipSerializer_v291 INSTANCE = new UpdateEquipSerializer_v291();

    protected UpdateEquipSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateEquipPacket packet) {
        buffer.writeByte(packet.getWindowId());
        buffer.writeByte(packet.getWindowType());
        VarInts.writeInt(buffer, packet.getSize());
        VarInts.writeLong(buffer, packet.getUniqueEntityId());
        helper.writeTag(buffer, packet.getTag());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateEquipPacket packet) {
        packet.setWindowId(buffer.readUnsignedByte());
        packet.setWindowType(buffer.readUnsignedByte());
        packet.setSize(VarInts.readInt(buffer));
        packet.setUniqueEntityId(VarInts.readLong(buffer));
        packet.setTag((NbtMap) helper.readTag(buffer, NbtMap.class));
    }
}
