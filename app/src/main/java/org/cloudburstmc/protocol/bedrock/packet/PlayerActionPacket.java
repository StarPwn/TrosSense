package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.PlayerActionType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PlayerActionPacket implements BedrockPacket {
    private PlayerActionType action;
    private Vector3i blockPosition;
    private int face;
    private Vector3i resultPosition;
    private long runtimeEntityId;

    public void setAction(PlayerActionType action) {
        this.action = action;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public void setResultPosition(Vector3i resultPosition) {
        this.resultPosition = resultPosition;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PlayerActionPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerActionPacket)) {
            return false;
        }
        PlayerActionPacket other = (PlayerActionPacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId || this.face != other.face) {
            return false;
        }
        Object this$action = this.action;
        Object other$action = other.action;
        if (this$action != null ? !this$action.equals(other$action) : other$action != null) {
            return false;
        }
        Object this$blockPosition = this.blockPosition;
        Object other$blockPosition = other.blockPosition;
        if (this$blockPosition != null ? !this$blockPosition.equals(other$blockPosition) : other$blockPosition != null) {
            return false;
        }
        Object this$resultPosition = this.resultPosition;
        Object other$resultPosition = other.resultPosition;
        return this$resultPosition != null ? this$resultPosition.equals(other$resultPosition) : other$resultPosition == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        int result2 = (result * 59) + this.face;
        Object $action = this.action;
        int result3 = (result2 * 59) + ($action == null ? 43 : $action.hashCode());
        Object $blockPosition = this.blockPosition;
        int result4 = (result3 * 59) + ($blockPosition == null ? 43 : $blockPosition.hashCode());
        Object $resultPosition = this.resultPosition;
        return (result4 * 59) + ($resultPosition != null ? $resultPosition.hashCode() : 43);
    }

    public String toString() {
        return "PlayerActionPacket(runtimeEntityId=" + this.runtimeEntityId + ", action=" + this.action + ", blockPosition=" + this.blockPosition + ", resultPosition=" + this.resultPosition + ", face=" + this.face + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public PlayerActionType getAction() {
        return this.action;
    }

    public Vector3i getBlockPosition() {
        return this.blockPosition;
    }

    public Vector3i getResultPosition() {
        return this.resultPosition;
    }

    public int getFace() {
        return this.face;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.PLAYER_ACTION;
    }
}
