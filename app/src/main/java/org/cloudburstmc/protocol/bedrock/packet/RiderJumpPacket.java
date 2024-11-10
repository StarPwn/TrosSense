package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class RiderJumpPacket implements BedrockPacket {
    private int jumpStrength;

    public void setJumpStrength(int jumpStrength) {
        this.jumpStrength = jumpStrength;
    }

    protected boolean canEqual(Object other) {
        return other instanceof RiderJumpPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RiderJumpPacket)) {
            return false;
        }
        RiderJumpPacket other = (RiderJumpPacket) o;
        return other.canEqual(this) && this.jumpStrength == other.jumpStrength;
    }

    public int hashCode() {
        int result = (1 * 59) + this.jumpStrength;
        return result;
    }

    public String toString() {
        return "RiderJumpPacket(jumpStrength=" + this.jumpStrength + ")";
    }

    public int getJumpStrength() {
        return this.jumpStrength;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.RIDER_JUMP;
    }
}
