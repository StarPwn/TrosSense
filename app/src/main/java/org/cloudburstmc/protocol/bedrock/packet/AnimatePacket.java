package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class AnimatePacket implements BedrockPacket {
    private Action action;
    private float rowingTime;
    private long runtimeEntityId;

    /* loaded from: classes5.dex */
    public enum Action {
        NO_ACTION,
        SWING_ARM,
        WAKE_UP,
        CRITICAL_HIT,
        MAGIC_CRITICAL_HIT,
        ROW_RIGHT,
        ROW_LEFT
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setRowingTime(float rowingTime) {
        this.rowingTime = rowingTime;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AnimatePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AnimatePacket)) {
            return false;
        }
        AnimatePacket other = (AnimatePacket) o;
        if (!other.canEqual(this) || Float.compare(this.rowingTime, other.rowingTime) != 0 || this.runtimeEntityId != other.runtimeEntityId) {
            return false;
        }
        Object this$action = this.action;
        Object other$action = other.action;
        return this$action != null ? this$action.equals(other$action) : other$action == null;
    }

    public int hashCode() {
        int result = (1 * 59) + Float.floatToIntBits(this.rowingTime);
        long $runtimeEntityId = this.runtimeEntityId;
        int result2 = (result * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        Object $action = this.action;
        return (result2 * 59) + ($action == null ? 43 : $action.hashCode());
    }

    public String toString() {
        return "AnimatePacket(rowingTime=" + this.rowingTime + ", action=" + this.action + ", runtimeEntityId=" + this.runtimeEntityId + ")";
    }

    public float getRowingTime() {
        return this.rowingTime;
    }

    public Action getAction() {
        return this.action;
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
        return BedrockPacketType.ANIMATE;
    }
}
