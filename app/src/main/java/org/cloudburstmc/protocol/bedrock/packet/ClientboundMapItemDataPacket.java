package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import java.util.List;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.MapDecoration;
import org.cloudburstmc.protocol.bedrock.data.MapTrackedObject;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ClientboundMapItemDataPacket implements BedrockPacket {
    private int[] colors;
    private int dimensionId;
    private int height;
    private boolean locked;
    private Vector3i origin;
    private int scale;
    private long uniqueMapId;
    private int width;
    private int xOffset;
    private int yOffset;
    private final LongList trackedEntityIds = new LongArrayList();
    private final List<MapTrackedObject> trackedObjects = new ObjectArrayList();
    private final List<MapDecoration> decorations = new ObjectArrayList();

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public void setDimensionId(int dimensionId) {
        this.dimensionId = dimensionId;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setOrigin(Vector3i origin) {
        this.origin = origin;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void setUniqueMapId(long uniqueMapId) {
        this.uniqueMapId = uniqueMapId;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ClientboundMapItemDataPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClientboundMapItemDataPacket)) {
            return false;
        }
        ClientboundMapItemDataPacket other = (ClientboundMapItemDataPacket) o;
        if (!other.canEqual(this) || this.uniqueMapId != other.uniqueMapId || this.dimensionId != other.dimensionId || this.locked != other.locked || this.scale != other.scale || this.height != other.height || this.width != other.width || this.xOffset != other.xOffset || this.yOffset != other.yOffset) {
            return false;
        }
        Object this$trackedEntityIds = this.trackedEntityIds;
        Object other$trackedEntityIds = other.trackedEntityIds;
        if (this$trackedEntityIds != null ? !this$trackedEntityIds.equals(other$trackedEntityIds) : other$trackedEntityIds != null) {
            return false;
        }
        Object this$trackedObjects = this.trackedObjects;
        Object other$trackedObjects = other.trackedObjects;
        if (this$trackedObjects != null ? !this$trackedObjects.equals(other$trackedObjects) : other$trackedObjects != null) {
            return false;
        }
        Object this$decorations = this.decorations;
        Object other$decorations = other.decorations;
        if (this$decorations != null ? !this$decorations.equals(other$decorations) : other$decorations != null) {
            return false;
        }
        Object this$origin = this.origin;
        Object other$origin = other.origin;
        if (this$origin != null ? this$origin.equals(other$origin) : other$origin == null) {
            return Arrays.equals(this.colors, other.colors);
        }
        return false;
    }

    public int hashCode() {
        long $uniqueMapId = this.uniqueMapId;
        int result = (1 * 59) + ((int) (($uniqueMapId >>> 32) ^ $uniqueMapId));
        int result2 = (((((((((((((result * 59) + this.dimensionId) * 59) + (this.locked ? 79 : 97)) * 59) + this.scale) * 59) + this.height) * 59) + this.width) * 59) + this.xOffset) * 59) + this.yOffset;
        Object $trackedEntityIds = this.trackedEntityIds;
        int result3 = (result2 * 59) + ($trackedEntityIds == null ? 43 : $trackedEntityIds.hashCode());
        Object $trackedObjects = this.trackedObjects;
        int result4 = (result3 * 59) + ($trackedObjects == null ? 43 : $trackedObjects.hashCode());
        Object $decorations = this.decorations;
        int result5 = (result4 * 59) + ($decorations == null ? 43 : $decorations.hashCode());
        Object $origin = this.origin;
        return (((result5 * 59) + ($origin != null ? $origin.hashCode() : 43)) * 59) + Arrays.hashCode(this.colors);
    }

    public String toString() {
        return "ClientboundMapItemDataPacket(trackedEntityIds=" + this.trackedEntityIds + ", trackedObjects=" + this.trackedObjects + ", decorations=" + this.decorations + ", uniqueMapId=" + this.uniqueMapId + ", dimensionId=" + this.dimensionId + ", locked=" + this.locked + ", origin=" + this.origin + ", scale=" + this.scale + ", height=" + this.height + ", width=" + this.width + ", xOffset=" + this.xOffset + ", yOffset=" + this.yOffset + ", colors=" + Arrays.toString(this.colors) + ")";
    }

    public LongList getTrackedEntityIds() {
        return this.trackedEntityIds;
    }

    public List<MapTrackedObject> getTrackedObjects() {
        return this.trackedObjects;
    }

    public List<MapDecoration> getDecorations() {
        return this.decorations;
    }

    public long getUniqueMapId() {
        return this.uniqueMapId;
    }

    public int getDimensionId() {
        return this.dimensionId;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public Vector3i getOrigin() {
        return this.origin;
    }

    public int getScale() {
        return this.scale;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getXOffset() {
        return this.xOffset;
    }

    public int getYOffset() {
        return this.yOffset;
    }

    public int[] getColors() {
        return this.colors;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CLIENTBOUND_MAP_ITEM_DATA;
    }
}
