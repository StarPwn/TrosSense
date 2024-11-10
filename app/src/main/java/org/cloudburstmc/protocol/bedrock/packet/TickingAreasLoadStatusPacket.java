package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class TickingAreasLoadStatusPacket implements BedrockPacket {
    boolean waitingForPreload;

    public void setWaitingForPreload(boolean waitingForPreload) {
        this.waitingForPreload = waitingForPreload;
    }

    protected boolean canEqual(Object other) {
        return other instanceof TickingAreasLoadStatusPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TickingAreasLoadStatusPacket)) {
            return false;
        }
        TickingAreasLoadStatusPacket other = (TickingAreasLoadStatusPacket) o;
        return other.canEqual(this) && this.waitingForPreload == other.waitingForPreload;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.waitingForPreload ? 79 : 97);
        return result;
    }

    public String toString() {
        return "TickingAreasLoadStatusPacket(waitingForPreload=" + this.waitingForPreload + ")";
    }

    public boolean isWaitingForPreload() {
        return this.waitingForPreload;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.TICKING_AREAS_LOAD_STATUS;
    }
}
