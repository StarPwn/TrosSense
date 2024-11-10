package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class InteractPacket implements BedrockPacket {
    private Action action;
    private Vector3f mousePosition;
    private long runtimeEntityId;

    /* loaded from: classes5.dex */
    public enum Action {
        NONE,
        INTERACT,
        DAMAGE,
        LEAVE_VEHICLE,
        MOUSEOVER,
        NPC_OPEN,
        OPEN_INVENTORY
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setMousePosition(Vector3f mousePosition) {
        this.mousePosition = mousePosition;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof InteractPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InteractPacket)) {
            return false;
        }
        InteractPacket other = (InteractPacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId) {
            return false;
        }
        Object this$action = this.action;
        Object other$action = other.action;
        if (this$action != null ? !this$action.equals(other$action) : other$action != null) {
            return false;
        }
        Object this$mousePosition = this.mousePosition;
        Object other$mousePosition = other.mousePosition;
        return this$mousePosition != null ? this$mousePosition.equals(other$mousePosition) : other$mousePosition == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        Object $action = this.action;
        int result2 = (result * 59) + ($action == null ? 43 : $action.hashCode());
        Object $mousePosition = this.mousePosition;
        return (result2 * 59) + ($mousePosition != null ? $mousePosition.hashCode() : 43);
    }

    public String toString() {
        return "InteractPacket(action=" + this.action + ", runtimeEntityId=" + this.runtimeEntityId + ", mousePosition=" + this.mousePosition + ")";
    }

    public Action getAction() {
        return this.action;
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public Vector3f getMousePosition() {
        return this.mousePosition;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.INTERACT;
    }
}
