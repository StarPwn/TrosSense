package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ChunkRadiusUpdatedPacket implements BedrockPacket {
    private int radius;

    public void setRadius(int radius) {
        this.radius = radius;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ChunkRadiusUpdatedPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChunkRadiusUpdatedPacket)) {
            return false;
        }
        ChunkRadiusUpdatedPacket other = (ChunkRadiusUpdatedPacket) o;
        return other.canEqual(this) && this.radius == other.radius;
    }

    public int hashCode() {
        int result = (1 * 59) + this.radius;
        return result;
    }

    public String toString() {
        return "ChunkRadiusUpdatedPacket(radius=" + this.radius + ")";
    }

    public int getRadius() {
        return this.radius;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CHUNK_RADIUS_UPDATED;
    }
}
