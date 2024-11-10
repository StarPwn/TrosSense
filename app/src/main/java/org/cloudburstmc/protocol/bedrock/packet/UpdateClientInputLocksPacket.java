package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class UpdateClientInputLocksPacket implements BedrockPacket {
    private int lockComponentData;
    private Vector3f serverPosition;

    public void setLockComponentData(int lockComponentData) {
        this.lockComponentData = lockComponentData;
    }

    public void setServerPosition(Vector3f serverPosition) {
        this.serverPosition = serverPosition;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UpdateClientInputLocksPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateClientInputLocksPacket)) {
            return false;
        }
        UpdateClientInputLocksPacket other = (UpdateClientInputLocksPacket) o;
        if (!other.canEqual(this) || this.lockComponentData != other.lockComponentData) {
            return false;
        }
        Object this$serverPosition = this.serverPosition;
        Object other$serverPosition = other.serverPosition;
        return this$serverPosition != null ? this$serverPosition.equals(other$serverPosition) : other$serverPosition == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.lockComponentData;
        Object $serverPosition = this.serverPosition;
        return (result * 59) + ($serverPosition == null ? 43 : $serverPosition.hashCode());
    }

    public String toString() {
        return "UpdateClientInputLocksPacket(lockComponentData=" + this.lockComponentData + ", serverPosition=" + this.serverPosition + ")";
    }

    public int getLockComponentData() {
        return this.lockComponentData;
    }

    public Vector3f getServerPosition() {
        return this.serverPosition;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.UPDATE_CLIENT_INPUT_LOCKS;
    }
}
