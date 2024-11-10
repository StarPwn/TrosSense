package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class AddHangingEntityPacket implements BedrockPacket {
    private int direction;
    private Vector3f position;
    private long runtimeEntityId;
    private long uniqueEntityId;

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AddHangingEntityPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AddHangingEntityPacket)) {
            return false;
        }
        AddHangingEntityPacket other = (AddHangingEntityPacket) o;
        if (!other.canEqual(this) || this.uniqueEntityId != other.uniqueEntityId || this.runtimeEntityId != other.runtimeEntityId || this.direction != other.direction) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        return this$position != null ? this$position.equals(other$position) : other$position == null;
    }

    public int hashCode() {
        long $uniqueEntityId = this.uniqueEntityId;
        int result = (1 * 59) + ((int) (($uniqueEntityId >>> 32) ^ $uniqueEntityId));
        long $runtimeEntityId = this.runtimeEntityId;
        int result2 = (((result * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId))) * 59) + this.direction;
        Object $position = this.position;
        return (result2 * 59) + ($position == null ? 43 : $position.hashCode());
    }

    public String toString() {
        return "AddHangingEntityPacket(uniqueEntityId=" + this.uniqueEntityId + ", runtimeEntityId=" + this.runtimeEntityId + ", position=" + this.position + ", direction=" + this.direction + ")";
    }

    public long getUniqueEntityId() {
        return this.uniqueEntityId;
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public int getDirection() {
        return this.direction;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ADD_HANGING_ENTITY;
    }
}
