package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class RespawnPacket implements BedrockPacket {
    private Vector3f position;
    private long runtimeEntityId;
    private State state;

    /* loaded from: classes5.dex */
    public enum State {
        SERVER_SEARCHING,
        SERVER_READY,
        CLIENT_READY
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setState(State state) {
        this.state = state;
    }

    protected boolean canEqual(Object other) {
        return other instanceof RespawnPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespawnPacket)) {
            return false;
        }
        RespawnPacket other = (RespawnPacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        if (this$position != null ? !this$position.equals(other$position) : other$position != null) {
            return false;
        }
        Object this$state = this.state;
        Object other$state = other.state;
        return this$state != null ? this$state.equals(other$state) : other$state == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        Object $position = this.position;
        int result2 = (result * 59) + ($position == null ? 43 : $position.hashCode());
        Object $state = this.state;
        return (result2 * 59) + ($state != null ? $state.hashCode() : 43);
    }

    public String toString() {
        return "RespawnPacket(position=" + this.position + ", state=" + this.state + ", runtimeEntityId=" + this.runtimeEntityId + ")";
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public State getState() {
        return this.state;
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.RESPAWN;
    }
}
