package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetTimePacket implements BedrockPacket {
    private int time;

    public void setTime(int time) {
        this.time = time;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetTimePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetTimePacket)) {
            return false;
        }
        SetTimePacket other = (SetTimePacket) o;
        return other.canEqual(this) && this.time == other.time;
    }

    public int hashCode() {
        int result = (1 * 59) + this.time;
        return result;
    }

    public String toString() {
        return "SetTimePacket(time=" + this.time + ")";
    }

    public int getTime() {
        return this.time;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_TIME;
    }
}
