package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PositionTrackingDBClientRequestPacket implements BedrockPacket {
    private Action action;
    private int trackingId;

    /* loaded from: classes5.dex */
    public enum Action {
        QUERY
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setTrackingId(int trackingId) {
        this.trackingId = trackingId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PositionTrackingDBClientRequestPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PositionTrackingDBClientRequestPacket)) {
            return false;
        }
        PositionTrackingDBClientRequestPacket other = (PositionTrackingDBClientRequestPacket) o;
        if (!other.canEqual(this) || this.trackingId != other.trackingId) {
            return false;
        }
        Object this$action = this.action;
        Object other$action = other.action;
        return this$action != null ? this$action.equals(other$action) : other$action == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.trackingId;
        Object $action = this.action;
        return (result * 59) + ($action == null ? 43 : $action.hashCode());
    }

    public String toString() {
        return "PositionTrackingDBClientRequestPacket(action=" + this.action + ", trackingId=" + this.trackingId + ")";
    }

    public Action getAction() {
        return this.action;
    }

    public int getTrackingId() {
        return this.trackingId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.POSITION_TRACKING_DB_CLIENT_REQUEST;
    }
}
