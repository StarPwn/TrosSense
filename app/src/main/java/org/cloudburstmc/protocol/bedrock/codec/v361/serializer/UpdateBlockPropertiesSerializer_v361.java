package org.cloudburstmc.protocol.bedrock.codec.v361.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPropertiesPacket;

/* loaded from: classes5.dex */
public class UpdateBlockPropertiesSerializer_v361 implements BedrockPacketSerializer<UpdateBlockPropertiesPacket> {
    public static final UpdateBlockPropertiesSerializer_v361 INSTANCE = new UpdateBlockPropertiesSerializer_v361();

    protected UpdateBlockPropertiesSerializer_v361() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateBlockPropertiesPacket packet) {
        helper.writeTag(buffer, packet.getProperties());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateBlockPropertiesPacket packet) {
        packet.setProperties((NbtMap) helper.readTag(buffer, NbtMap.class));
    }
}
