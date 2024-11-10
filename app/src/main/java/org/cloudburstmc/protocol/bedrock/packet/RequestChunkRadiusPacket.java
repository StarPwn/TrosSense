package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class RequestChunkRadiusPacket implements BedrockPacket {
    private int maxRadius;
    private int radius;

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    protected boolean canEqual(Object other) {
        return other instanceof RequestChunkRadiusPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RequestChunkRadiusPacket)) {
            return false;
        }
        RequestChunkRadiusPacket other = (RequestChunkRadiusPacket) o;
        return other.canEqual(this) && this.radius == other.radius && this.maxRadius == other.maxRadius;
    }

    public int hashCode() {
        int result = (1 * 59) + this.radius;
        return (result * 59) + this.maxRadius;
    }

    public String toString() {
        return "RequestChunkRadiusPacket(radius=" + this.radius + ", maxRadius=" + this.maxRadius + ")";
    }

    public int getRadius() {
        return this.radius;
    }

    public int getMaxRadius() {
        return this.maxRadius;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.REQUEST_CHUNK_RADIUS;
    }
}
