package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.map.MapPixel;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class MapInfoRequestPacket implements BedrockPacket {
    private final List<MapPixel> pixels = new ObjectArrayList();
    private long uniqueMapId;

    public void setUniqueMapId(long uniqueMapId) {
        this.uniqueMapId = uniqueMapId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MapInfoRequestPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MapInfoRequestPacket)) {
            return false;
        }
        MapInfoRequestPacket other = (MapInfoRequestPacket) o;
        if (!other.canEqual(this) || this.uniqueMapId != other.uniqueMapId) {
            return false;
        }
        Object this$pixels = this.pixels;
        Object other$pixels = other.pixels;
        return this$pixels != null ? this$pixels.equals(other$pixels) : other$pixels == null;
    }

    public int hashCode() {
        long $uniqueMapId = this.uniqueMapId;
        int result = (1 * 59) + ((int) (($uniqueMapId >>> 32) ^ $uniqueMapId));
        Object $pixels = this.pixels;
        return (result * 59) + ($pixels == null ? 43 : $pixels.hashCode());
    }

    public String toString() {
        return "MapInfoRequestPacket(uniqueMapId=" + this.uniqueMapId + ", pixels=" + this.pixels + ")";
    }

    public long getUniqueMapId() {
        return this.uniqueMapId;
    }

    public List<MapPixel> getPixels() {
        return this.pixels;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.MAP_INFO_REQUEST;
    }
}
