package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongListIterator;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.MapDecoration;
import org.cloudburstmc.protocol.bedrock.data.MapTrackedObject;
import org.cloudburstmc.protocol.bedrock.packet.ClientboundMapItemDataPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class ClientboundMapItemDataSerializer_v291 implements BedrockPacketSerializer<ClientboundMapItemDataPacket> {
    public static final ClientboundMapItemDataSerializer_v291 INSTANCE = new ClientboundMapItemDataSerializer_v291();

    protected ClientboundMapItemDataSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
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
        if ((type & 8) != 0) {
            VarInts.writeUnsignedInt(buffer, trackedEntityIds.size());
            LongListIterator it2 = trackedEntityIds.iterator();
            while (it2.hasNext()) {
                long trackedEntityId = it2.next().longValue();
                VarInts.writeLong(buffer, trackedEntityId);
            }
        }
        if ((type & 14) != 0) {
            buffer.writeByte(packet.getScale());
        }
        if ((type & 4) != 0) {
            VarInts.writeUnsignedInt(buffer, trackedObjects.size());
            for (MapTrackedObject object : trackedObjects) {
                switch (object.getType()) {
                    case BLOCK:
                        buffer.writeIntLE(object.getType().ordinal());
                        helper.writeBlockPosition(buffer, object.getPosition());
                        break;
                    case ENTITY:
                        buffer.writeIntLE(object.getType().ordinal());
                        VarInts.writeLong(buffer, object.getEntityId());
                        break;
                }
            }
            VarInts.writeUnsignedInt(buffer, decorations.size());
            for (MapDecoration decoration : decorations) {
                buffer.writeByte(decoration.getImage());
                buffer.writeByte(decoration.getRotation());
                buffer.writeByte(decoration.getXOffset());
                buffer.writeByte(decoration.getYOffset());
                helper.writeString(buffer, decoration.getLabel());
                VarInts.writeUnsignedInt(buffer, decoration.getColor());
            }
        }
        if ((type & 2) != 0) {
            VarInts.writeInt(buffer, packet.getWidth());
            VarInts.writeInt(buffer, packet.getHeight());
            VarInts.writeInt(buffer, packet.getXOffset());
            VarInts.writeInt(buffer, packet.getYOffset());
            VarInts.writeUnsignedInt(buffer, colors.length);
            for (int color : colors) {
                VarInts.writeUnsignedInt(buffer, color);
            }
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ClientboundMapItemDataPacket packet) {
        ByteBuf byteBuf = buffer;
        packet.setUniqueMapId(VarInts.readLong(buffer));
        int type = VarInts.readUnsignedInt(buffer);
        packet.setDimensionId(buffer.readUnsignedByte());
        if ((type & 8) != 0) {
            LongList trackedEntityIds = packet.getTrackedEntityIds();
            int length = VarInts.readUnsignedInt(buffer);
            for (int i = 0; i < length; i++) {
                trackedEntityIds.add(VarInts.readLong(buffer));
            }
        }
        if ((type & 14) != 0) {
            packet.setScale(buffer.readUnsignedByte());
        }
        if ((type & 4) != 0) {
            List<MapTrackedObject> trackedObjects = packet.getTrackedObjects();
            int length2 = VarInts.readUnsignedInt(buffer);
            for (int i2 = 0; i2 < length2; i2++) {
                MapTrackedObject.Type objectType = MapTrackedObject.Type.values()[buffer.readIntLE()];
                switch (objectType) {
                    case BLOCK:
                        trackedObjects.add(new MapTrackedObject(helper.readBlockPosition(byteBuf)));
                        break;
                    case ENTITY:
                        trackedObjects.add(new MapTrackedObject(VarInts.readLong(buffer)));
                        break;
                }
            }
            List<MapDecoration> decorations = packet.getDecorations();
            int length3 = VarInts.readUnsignedInt(buffer);
            int i3 = 0;
            while (i3 < length3) {
                int image = buffer.readUnsignedByte();
                int rotation = buffer.readUnsignedByte();
                int xOffset = buffer.readUnsignedByte();
                int yOffset = buffer.readUnsignedByte();
                String label = helper.readString(byteBuf);
                int color = VarInts.readUnsignedInt(buffer);
                decorations.add(new MapDecoration(image, rotation, xOffset, yOffset, label, color));
                i3++;
                byteBuf = buffer;
            }
        }
        if ((type & 2) != 0) {
            packet.setWidth(VarInts.readInt(buffer));
            packet.setHeight(VarInts.readInt(buffer));
            packet.setXOffset(VarInts.readInt(buffer));
            packet.setYOffset(VarInts.readInt(buffer));
            int length4 = VarInts.readUnsignedInt(buffer);
            int[] colors = new int[length4];
            for (int i4 = 0; i4 < length4; i4++) {
                colors[i4] = VarInts.readUnsignedInt(buffer);
            }
            packet.setColors(colors);
        }
    }
}
