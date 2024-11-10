package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class OpenSignPacket implements BedrockPacket {
    private boolean frontSide;
    private Vector3i position;

    public void setFrontSide(boolean frontSide) {
        this.frontSide = frontSide;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    protected boolean canEqual(Object other) {
        return other instanceof OpenSignPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OpenSignPacket)) {
            return false;
        }
        OpenSignPacket other = (OpenSignPacket) o;
        if (!other.canEqual(this) || this.frontSide != other.frontSide) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        return this$position != null ? this$position.equals(other$position) : other$position == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.frontSide ? 79 : 97);
        Object $position = this.position;
        return (result * 59) + ($position == null ? 43 : $position.hashCode());
    }

    public String toString() {
        return "OpenSignPacket(position=" + this.position + ", frontSide=" + this.frontSide + ")";
    }

    public Vector3i getPosition() {
        return this.position;
    }

    public boolean isFrontSide() {
        return this.frontSide;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.OPEN_SIGN;
    }
}
