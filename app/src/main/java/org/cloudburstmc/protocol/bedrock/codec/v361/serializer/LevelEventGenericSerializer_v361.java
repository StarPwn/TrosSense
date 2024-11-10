package org.cloudburstmc.protocol.bedrock.codec.v361.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.nbt.NbtType;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventGenericPacket;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class LevelEventGenericSerializer_v361 implements BedrockPacketSerializer<LevelEventGenericPacket> {
    private final TypeMap<LevelEventType> typeMap;

    public LevelEventGenericSerializer_v361(TypeMap<LevelEventType> typeMap) {
        this.typeMap = typeMap;
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, LevelEventGenericPacket packet) {
        VarInts.writeInt(buffer, this.typeMap.getId(packet.getType()));
        helper.writeTagValue(buffer, packet.getTag());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, LevelEventGenericPacket packet) {
        int eventId = VarInts.readInt(buffer);
        packet.setType(this.typeMap.getType(eventId));
        packet.setTag(helper.readTagValue(buffer, NbtType.COMPOUND));
    }
}
