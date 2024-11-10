package org.cloudburstmc.protocol.bedrock.codec.v354.serializer;

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
public class ClientboundMapItemDataSerializer_v354 implements BedrockPacketSerializer<ClientboundMapItemDataPacket> {
    protected static final int FLAG_ALL = 14;
    protected static final int FLAG_DECORATION_UPDATE = 4;
    protected static final int FLAG_MAP_CREATION = 8;
    protected static final int FLAG_TEXTURE_UPDATE = 2;
    public static final ClientboundMapItemDataSerializer_v354 INSTANCE = new ClientboundMapItemDataSerializer_v354();

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
        buffer.writeBoolean(packet.isLocked());
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

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ClientboundMapItemDataPacket packet) {
        packet.setUniqueMapId(VarInts.readLong(buffer));
        int type = VarInts.readUnsignedInt(buffer);
        packet.setDimensionId(buffer.readUnsignedByte());
        packet.setLocked(buffer.readBoolean());
        if ((type & 8) != 0) {
            readMapCreation(buffer, helper, packet);
        }
        if ((type & 14) != 0) {
            packet.setScale(buffer.readUnsignedByte());
        }
        if ((type & 4) != 0) {
            writeMapDecorations(buffer, helper, packet);
        }
        if ((type & 2) != 0) {
            readTextureUpdate(buffer, helper, packet);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeMapCreation(ByteBuf buffer, BedrockCodecHelper helper, ClientboundMapItemDataPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getTrackedEntityIds().size());
        LongListIterator it2 = packet.getTrackedEntityIds().iterator();
        while (it2.hasNext()) {
            long trackedEntityId = it2.next().longValue();
            VarInts.writeLong(buffer, trackedEntityId);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void readMapCreation(ByteBuf buffer, BedrockCodecHelper helper, ClientboundMapItemDataPacket packet) {
        LongList trackedEntityIds = packet.getTrackedEntityIds();
        int length = VarInts.readUnsignedInt(buffer);
        for (int i = 0; i < length; i++) {
            trackedEntityIds.add(VarInts.readLong(buffer));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeMapDecorations(ByteBuf buffer, BedrockCodecHelper helper, ClientboundMapItemDataPacket packet) {
        List<MapDecoration> decorations = packet.getDecorations();
        List<MapTrackedObject> trackedObjects = packet.getTrackedObjects();
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

    /* JADX INFO: Access modifiers changed from: protected */
    public void readMapDecorations(ByteBuf buffer, BedrockCodecHelper helper, ClientboundMapItemDataPacket packet) {
        ByteBuf byteBuf = buffer;
        List<MapTrackedObject> trackedObjects = packet.getTrackedObjects();
        int length = VarInts.readUnsignedInt(buffer);
        for (int i = 0; i < length; i++) {
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
        int length2 = VarInts.readUnsignedInt(buffer);
        int i2 = 0;
        while (i2 < length2) {
            int image = buffer.readUnsignedByte();
            int rotation = buffer.readUnsignedByte();
            int xOffset = buffer.readUnsignedByte();
            int yOffset = buffer.readUnsignedByte();
            String label = helper.readString(byteBuf);
            int color = VarInts.readUnsignedInt(buffer);
            decorations.add(new MapDecoration(image, rotation, xOffset, yOffset, label, color));
            i2++;
            byteBuf = buffer;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeTextureUpdate(ByteBuf buffer, BedrockCodecHelper helper, ClientboundMapItemDataPacket packet) {
        VarInts.writeInt(buffer, packet.getWidth());
        VarInts.writeInt(buffer, packet.getHeight());
        VarInts.writeInt(buffer, packet.getXOffset());
        VarInts.writeInt(buffer, packet.getYOffset());
        int length = packet.getColors().length;
        VarInts.writeUnsignedInt(buffer, length);
        for (int color : packet.getColors()) {
            VarInts.writeUnsignedInt(buffer, color);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void readTextureUpdate(ByteBuf buffer, BedrockCodecHelper helper, ClientboundMapItemDataPacket packet) {
        packet.setWidth(VarInts.readInt(buffer));
        packet.setHeight(VarInts.readInt(buffer));
        packet.setXOffset(VarInts.readInt(buffer));
        packet.setYOffset(VarInts.readInt(buffer));
        int length = VarInts.readUnsignedInt(buffer);
        int[] colors = new int[length];
        for (int i = 0; i < length; i++) {
            colors[i] = VarInts.readUnsignedInt(buffer);
        }
        packet.setColors(colors);
    }
}
