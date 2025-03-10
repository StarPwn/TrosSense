package org.cloudburstmc.protocol.bedrock.codec.v527.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.PlayerPermission;
import org.cloudburstmc.protocol.bedrock.packet.RequestPermissionsPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class RequestPermissionsSerializer_v527 implements BedrockPacketSerializer<RequestPermissionsPacket> {
    private static final PlayerPermission[] VALUES = PlayerPermission.values();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, RequestPermissionsPacket packet) {
        buffer.writeLongLE(packet.getUniqueEntityId());
        VarInts.writeInt(buffer, packet.getPermissions().ordinal());
        buffer.writeShortLE(packet.getCustomPermissions());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, RequestPermissionsPacket packet) {
        packet.setUniqueEntityId(buffer.readLongLE());
        packet.setPermissions(VALUES[VarInts.readInt(buffer)]);
        packet.setCustomPermissions(buffer.readUnsignedShortLE());
    }
}
