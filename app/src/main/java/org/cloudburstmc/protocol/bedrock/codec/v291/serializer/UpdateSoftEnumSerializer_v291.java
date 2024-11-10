package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.command.SoftEnumUpdateType;
import org.cloudburstmc.protocol.bedrock.packet.UpdateSoftEnumPacket;

/* loaded from: classes5.dex */
public class UpdateSoftEnumSerializer_v291 implements BedrockPacketSerializer<UpdateSoftEnumPacket> {
    public static final UpdateSoftEnumSerializer_v291 INSTANCE = new UpdateSoftEnumSerializer_v291();

    protected UpdateSoftEnumSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateSoftEnumPacket packet) {
        helper.writeCommandEnum(buffer, packet.getSoftEnum());
        buffer.writeByte(packet.getType().ordinal());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateSoftEnumPacket packet) {
        packet.setSoftEnum(helper.readCommandEnum(buffer, true));
        packet.setType(SoftEnumUpdateType.values()[buffer.readByte()]);
    }
}
