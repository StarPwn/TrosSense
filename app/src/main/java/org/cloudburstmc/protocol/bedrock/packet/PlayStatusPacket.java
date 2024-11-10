package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PlayStatusPacket implements BedrockPacket {
    private Status status;

    /* loaded from: classes5.dex */
    public enum Status {
        LOGIN_SUCCESS,
        LOGIN_FAILED_CLIENT_OLD,
        LOGIN_FAILED_SERVER_OLD,
        PLAYER_SPAWN,
        LOGIN_FAILED_INVALID_TENANT,
        LOGIN_FAILED_EDITION_MISMATCH_EDU_TO_VANILLA,
        LOGIN_FAILED_EDITION_MISMATCH_VANILLA_TO_EDU,
        FAILED_SERVER_FULL_SUB_CLIENT,
        EDITOR_TO_VANILLA_MISMATCH,
        VANILLA_TO_EDITOR_MISMATCH
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PlayStatusPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayStatusPacket)) {
            return false;
        }
        PlayStatusPacket other = (PlayStatusPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$status = this.status;
        Object other$status = other.status;
        return this$status != null ? this$status.equals(other$status) : other$status == null;
    }

    public int hashCode() {
        Object $status = this.status;
        int result = (1 * 59) + ($status == null ? 43 : $status.hashCode());
        return result;
    }

    public String toString() {
        return "PlayStatusPacket(status=" + this.status + ")";
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
        return BedrockPacketType.PLAY_STATUS;
    }
}
