package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class MoveEntityAbsolutePacket implements BedrockPacket {
    private boolean forceMove;
    private boolean onGround;
    private Vector3f position;
    private Vector3f rotation;
    private long runtimeEntityId;
    private boolean teleported;

    public void setForceMove(boolean forceMove) {
        this.forceMove = forceMove;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setTeleported(boolean teleported) {
        this.teleported = teleported;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MoveEntityAbsolutePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MoveEntityAbsolutePacket)) {
            return false;
        }
        MoveEntityAbsolutePacket other = (MoveEntityAbsolutePacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId || this.onGround != other.onGround || this.teleported != other.teleported || this.forceMove != other.forceMove) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        if (this$position != null ? !this$position.equals(other$position) : other$position != null) {
            return false;
        }
        Object this$rotation = this.rotation;
        Object other$rotation = other.rotation;
        return this$rotation != null ? this$rotation.equals(other$rotation) : other$rotation == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        int result2 = ((((result * 59) + (this.onGround ? 79 : 97)) * 59) + (this.teleported ? 79 : 97)) * 59;
        int i = this.forceMove ? 79 : 97;
        Object $position = this.position;
        int result3 = ((result2 + i) * 59) + ($position == null ? 43 : $position.hashCode());
        Object $rotation = this.rotation;
        return (result3 * 59) + ($rotation != null ? $rotation.hashCode() : 43);
    }

    public String toString() {
        return "MoveEntityAbsolutePacket(runtimeEntityId=" + this.runtimeEntityId + ", position=" + this.position + ", rotation=" + this.rotation + ", onGround=" + this.onGround + ", teleported=" + this.teleported + ", forceMove=" + this.forceMove + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public Vector3f getRotation() {
        return this.rotation;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public boolean isTeleported() {
        return this.teleported;
    }

    public boolean isForceMove() {
        return this.forceMove;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.MOVE_ENTITY_ABSOLUTE;
    }
}
