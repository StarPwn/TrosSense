package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ShowCreditsPacket implements BedrockPacket {
    private long runtimeEntityId;
    private Status status;

    /* loaded from: classes5.dex */
    public enum Status {
        START_CREDITS,
        END_CREDITS
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ShowCreditsPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ShowCreditsPacket)) {
            return false;
        }
        ShowCreditsPacket other = (ShowCreditsPacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId) {
            return false;
        }
        Object this$status = this.status;
        Object other$status = other.status;
        return this$status != null ? this$status.equals(other$status) : other$status == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        Object $status = this.status;
        return (result * 59) + ($status == null ? 43 : $status.hashCode());
    }

    public String toString() {
        return "ShowCreditsPacket(runtimeEntityId=" + this.runtimeEntityId + ", status=" + this.status + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public Status getStatus() {
        return this.status;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SHOW_CREDITS;
    }
}
