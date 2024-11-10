package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetHealthPacket implements BedrockPacket {
    private int health;

    public void setHealth(int health) {
        this.health = health;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetHealthPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetHealthPacket)) {
            return false;
        }
        SetHealthPacket other = (SetHealthPacket) o;
        return other.canEqual(this) && this.health == other.health;
    }

    public int hashCode() {
        int result = (1 * 59) + this.health;
        return result;
    }

    public String toString() {
        return "SetHealthPacket(health=" + this.health + ")";
    }

    public int getHealth() {
        return this.health;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_HEALTH;
    }
}
