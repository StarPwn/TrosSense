package org.cloudburstmc.protocol.bedrock.codec.v544.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.LongList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v354.serializer.ClientboundMapItemDataSerializer_v354;
import org.cloudburstmc.protocol.bedrock.data.MapDecoration;
import org.cloudburstmc.protocol.bedrock.data.MapTrackedObject;
import org.cloudburstmc.protocol.bedrock.packet.ClientboundMapItemDataPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class ClientboundMapItemDataSerializer_v544 extends ClientboundMapItemDataSerializer_v354 {
    @Override // org.cloudburstmc.protocol.bedrock.codec.v354.serializer.ClientboundMapItemDataSerializer_v354, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ClientboundMapItemDataPacket packet) {
        VarInts.writeLong(buffer, packet.getUniqueMapId());
        int type = 0;
        int[] colors = packet.getColors();
        if (colors != null && colors.length > 0) {
            type = 0 | 2;
        }
        List<MapDecoration> decorations = packet.getDecorations();
        List<MapTrackedObject> trackedObjects = packet.getTrackedObjects();
        if (!decorations.isEmpty() && !trackedObjects.isEmpty()) {
            type |= 4;
        }
        LongList trackedEntityIds = packet.getTrackedEntityIds();
        if (!trackedEntityIds.isEmpty()) {
            type |= 8;
        }
        VarInts.writeUnsignedInt(buffer, type);
        buffer.writeByte(packet.getDimensionId());
        buffer.writeBoolean(packet.isLocked());
        helper.writeBlockPosition(buffer, packet.getOrigin());
        if ((type & 8) != 0) {
            writeMapCreation(buffer, helper, packet);
        }
        if ((type & 14) != 0) {
            buffer.writeByte(packet.getScale());
        }
        if ((type & 4) != 0) {
            writeMapDecorations(buffer, helper, packet);
        }
        if ((type & 2) != 0) {
            writeTextureUpdate(buffer, helper, packet);
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v354.serializer.ClientboundMapItemDataSerializer_v354, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ClientboundMapItemDataPacket packet) {
        packet.setUniqueMapId(VarInts.readLong(buffer));
        int type = VarInts.readUnsignedInt(buffer);
        packet.setDimensionId(buffer.readUnsignedByte());
        packet.setLocked(buffer.readBoolean());
        packet.setOrigin(helper.readBlockPosition(buffer));
        if ((type & 8) != 0) {
            readMapCreation(buffer, helper, packet);
        }
        if ((type & 14) != 0) {
            packet.setScale(buffer.readUnsignedByte());
        }
        if ((type & 4) != 0) {
            readMapDecorations(buffer, helper, packet);
        }
        if ((type & 2) != 0) {
            readTextureUpdate(buffer, helper, packet);
        }
    }
}
