package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PositionTrackingDBServerBroadcastPacket implements BedrockPacket {
    private Action action;
    private NbtMap tag;
    private int trackingId;

    /* loaded from: classes5.dex */
    public enum Action {
        UPDATE,
        DESTROY,
        NOT_FOUND
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setTag(NbtMap tag) {
        this.tag = tag;
    }

    public void setTrackingId(int trackingId) {
        this.trackingId = trackingId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PositionTrackingDBServerBroadcastPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PositionTrackingDBServerBroadcastPacket)) {
            return false;
        }
        PositionTrackingDBServerBroadcastPacket other = (PositionTrackingDBServerBroadcastPacket) o;
        if (!other.canEqual(this) || this.trackingId != other.trackingId) {
            return false;
        }
        Object this$action = this.action;
        Object other$action = other.action;
        if (this$action != null ? !this$action.equals(other$action) : other$action != null) {
            return false;
        }
        Object this$tag = this.tag;
        Object other$tag = other.tag;
        return this$tag != null ? this$tag.equals(other$tag) : other$tag == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.trackingId;
        Object $action = this.action;
        int result2 = (result * 59) + ($action == null ? 43 : $action.hashCode());
        Object $tag = this.tag;
        return (result2 * 59) + ($tag != null ? $tag.hashCode() : 43);
    }

    public String toString() {
        return "PositionTrackingDBServerBroadcastPacket(action=" + this.action + ", trackingId=" + this.trackingId + ", tag=" + this.tag + ")";
    }

    public Action getAction() {
        return this.action;
    }

    public int getTrackingId() {
        return this.trackingId;
    }

    public NbtMap getTag() {
        return this.tag;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.POSITION_TRACKING_DB_SERVER_BROADCAST;
    }
}
