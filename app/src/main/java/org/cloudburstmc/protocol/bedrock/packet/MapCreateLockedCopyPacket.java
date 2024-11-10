package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class MapCreateLockedCopyPacket implements BedrockPacket {
    private long newMapId;
    private long originalMapId;

    public void setNewMapId(long newMapId) {
        this.newMapId = newMapId;
    }

    public void setOriginalMapId(long originalMapId) {
        this.originalMapId = originalMapId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MapCreateLockedCopyPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MapCreateLockedCopyPacket)) {
            return false;
        }
        MapCreateLockedCopyPacket other = (MapCreateLockedCopyPacket) o;
        return other.canEqual(this) && this.originalMapId == other.originalMapId && this.newMapId == other.newMapId;
    }

    public int hashCode() {
        long $originalMapId = this.originalMapId;
        int result = (1 * 59) + ((int) (($originalMapId >>> 32) ^ $originalMapId));
        long $newMapId = this.newMapId;
        return (result * 59) + ((int) (($newMapId >>> 32) ^ $newMapId));
    }

    public String toString() {
        return "MapCreateLockedCopyPacket(originalMapId=" + this.originalMapId + ", newMapId=" + this.newMapId + ")";
    }

    public long getOriginalMapId() {
        return this.originalMapId;
    }

    public long getNewMapId() {
        return this.newMapId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.MAP_CREATE_LOCKED_COPY;
    }
}
