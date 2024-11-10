package org.cloudburstmc.protocol.bedrock.codec.v428.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.ClientboundDebugRendererType;
import org.cloudburstmc.protocol.bedrock.packet.ClientboundDebugRendererPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class ClientboundDebugRendererSerializer_v428 implements BedrockPacketSerializer<ClientboundDebugRendererPacket> {
    public static final ClientboundDebugRendererSerializer_v428 INSTANCE = new ClientboundDebugRendererSerializer_v428();

    protected ClientboundDebugRendererSerializer_v428() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ClientboundDebugRendererPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getDebugMarkerType().ordinal());
        if (packet.getDebugMarkerType() == ClientboundDebugRendererType.ADD_DEBUG_MARKER_CUBE) {
            helper.writeString(buffer, packet.getMarkerText());
            helper.writeVector3f(buffer, packet.getMarkerPosition());
            buffer.writeFloat(packet.getMarkerColorRed());
            buffer.writeFloat(packet.getMarkerColorGreen());
            buffer.writeFloat(packet.getMarkerColorBlue());
            buffer.writeFloat(packet.getMarkerColorAlpha());
            buffer.writeLongLE(packet.getMarkerDuration());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ClientboundDebugRendererPacket packet) {
        packet.setDebugMarkerType(ClientboundDebugRendererType.values()[VarInts.readUnsignedInt(buffer)]);
        if (packet.getDebugMarkerType() == ClientboundDebugRendererType.ADD_DEBUG_MARKER_CUBE) {
            packet.setMarkerText(helper.readString(buffer));
            packet.setMarkerPosition(helper.readVector3f(buffer));
            packet.setMarkerColorRed(buffer.readFloat());
            packet.setMarkerColorGreen(buffer.readFloat());
            packet.setMarkerColorBlue(buffer.readFloat());
            packet.setMarkerColorAlpha(buffer.readFloat());
            packet.setMarkerDuration(buffer.readLongLE());
        }
    }
}
