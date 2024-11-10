package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.math.vector.Vector2i;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class NetworkChunkPublisherUpdatePacket implements BedrockPacket {
    private Vector3i position;
    private int radius;
    private final List<Vector2i> savedChunks = new ObjectArrayList();

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    protected boolean canEqual(Object other) {
        return other instanceof NetworkChunkPublisherUpdatePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NetworkChunkPublisherUpdatePacket)) {
            return false;
        }
        NetworkChunkPublisherUpdatePacket other = (NetworkChunkPublisherUpdatePacket) o;
        if (!other.canEqual(this) || this.radius != other.radius) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        if (this$position != null ? !this$position.equals(other$position) : other$position != null) {
            return false;
        }
        Object this$savedChunks = this.savedChunks;
        Object other$savedChunks = other.savedChunks;
        return this$savedChunks != null ? this$savedChunks.equals(other$savedChunks) : other$savedChunks == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.radius;
        Object $position = this.position;
        int result2 = (result * 59) + ($position == null ? 43 : $position.hashCode());
        Object $savedChunks = this.savedChunks;
        return (result2 * 59) + ($savedChunks != null ? $savedChunks.hashCode() : 43);
    }

    public String toString() {
        return "NetworkChunkPublisherUpdatePacket(position=" + this.position + ", radius=" + this.radius + ", savedChunks=" + this.savedChunks + ")";
    }

    public Vector3i getPosition() {
        return this.position;
    }

    public int getRadius() {
        return this.radius;
    }

    public List<Vector2i> getSavedChunks() {
        return this.savedChunks;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.NETWORK_CHUNK_PUBLISHER_UPDATE;
    }
}
