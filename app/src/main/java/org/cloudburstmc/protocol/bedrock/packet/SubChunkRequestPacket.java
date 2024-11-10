package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SubChunkRequestPacket implements BedrockPacket {
    private int dimension;
    private List<Vector3i> positionOffsets = new ObjectArrayList();
    private Vector3i subChunkPosition;

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void setPositionOffsets(List<Vector3i> positionOffsets) {
        this.positionOffsets = positionOffsets;
    }

    public void setSubChunkPosition(Vector3i subChunkPosition) {
        this.subChunkPosition = subChunkPosition;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SubChunkRequestPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SubChunkRequestPacket)) {
            return false;
        }
        SubChunkRequestPacket other = (SubChunkRequestPacket) o;
        if (!other.canEqual(this) || this.dimension != other.dimension) {
            return false;
        }
        Object this$subChunkPosition = this.subChunkPosition;
        Object other$subChunkPosition = other.subChunkPosition;
        if (this$subChunkPosition != null ? !this$subChunkPosition.equals(other$subChunkPosition) : other$subChunkPosition != null) {
            return false;
        }
        Object this$positionOffsets = this.positionOffsets;
        Object other$positionOffsets = other.positionOffsets;
        return this$positionOffsets != null ? this$positionOffsets.equals(other$positionOffsets) : other$positionOffsets == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.dimension;
        Object $subChunkPosition = this.subChunkPosition;
        int result2 = (result * 59) + ($subChunkPosition == null ? 43 : $subChunkPosition.hashCode());
        Object $positionOffsets = this.positionOffsets;
        return (result2 * 59) + ($positionOffsets != null ? $positionOffsets.hashCode() : 43);
    }

    public String toString() {
        return "SubChunkRequestPacket(dimension=" + this.dimension + ", subChunkPosition=" + this.subChunkPosition + ", positionOffsets=" + this.positionOffsets + ")";
    }

    public int getDimension() {
        return this.dimension;
    }

    public Vector3i getSubChunkPosition() {
        return this.subChunkPosition;
    }

    public List<Vector3i> getPositionOffsets() {
        return this.positionOffsets;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SUB_CHUNK_REQUEST;
    }
}
