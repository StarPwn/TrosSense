package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class CameraPacket implements BedrockPacket {
    private long cameraUniqueEntityId;
    private long playerUniqueEntityId;

    public void setCameraUniqueEntityId(long cameraUniqueEntityId) {
        this.cameraUniqueEntityId = cameraUniqueEntityId;
    }

    public void setPlayerUniqueEntityId(long playerUniqueEntityId) {
        this.playerUniqueEntityId = playerUniqueEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CameraPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CameraPacket)) {
            return false;
        }
        CameraPacket other = (CameraPacket) o;
        return other.canEqual(this) && this.cameraUniqueEntityId == other.cameraUniqueEntityId && this.playerUniqueEntityId == other.playerUniqueEntityId;
    }

    public int hashCode() {
        long $cameraUniqueEntityId = this.cameraUniqueEntityId;
        int result = (1 * 59) + ((int) (($cameraUniqueEntityId >>> 32) ^ $cameraUniqueEntityId));
        long $playerUniqueEntityId = this.playerUniqueEntityId;
        return (result * 59) + ((int) (($playerUniqueEntityId >>> 32) ^ $playerUniqueEntityId));
    }

    public String toString() {
        return "CameraPacket(cameraUniqueEntityId=" + this.cameraUniqueEntityId + ", playerUniqueEntityId=" + this.playerUniqueEntityId + ")";
    }

    public long getCameraUniqueEntityId() {
        return this.cameraUniqueEntityId;
    }

    public long getPlayerUniqueEntityId() {
        return this.playerUniqueEntityId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CAMERA;
    }
}
