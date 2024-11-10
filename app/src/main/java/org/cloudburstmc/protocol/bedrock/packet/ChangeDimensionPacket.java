package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ChangeDimensionPacket implements BedrockPacket {
    private int dimension;
    private Vector3f position;
    private boolean respawn;

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRespawn(boolean respawn) {
        this.respawn = respawn;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ChangeDimensionPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChangeDimensionPacket)) {
            return false;
        }
        ChangeDimensionPacket other = (ChangeDimensionPacket) o;
        if (!other.canEqual(this) || this.dimension != other.dimension || this.respawn != other.respawn) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        return this$position != null ? this$position.equals(other$position) : other$position == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.dimension;
        int result2 = result * 59;
        int i = this.respawn ? 79 : 97;
        Object $position = this.position;
        return ((result2 + i) * 59) + ($position == null ? 43 : $position.hashCode());
    }

    public String toString() {
        return "ChangeDimensionPacket(dimension=" + this.dimension + ", position=" + this.position + ", respawn=" + this.respawn + ")";
    }

    public int getDimension() {
        return this.dimension;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public boolean isRespawn() {
        return this.respawn;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CHANGE_DIMENSION;
    }
}
