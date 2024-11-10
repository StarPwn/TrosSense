package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class AnvilDamagePacket implements BedrockPacket {
    private int damage;
    private Vector3i position;

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AnvilDamagePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AnvilDamagePacket)) {
            return false;
        }
        AnvilDamagePacket other = (AnvilDamagePacket) o;
        if (!other.canEqual(this) || this.damage != other.damage) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        return this$position != null ? this$position.equals(other$position) : other$position == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.damage;
        Object $position = this.position;
        return (result * 59) + ($position == null ? 43 : $position.hashCode());
    }

    public String toString() {
        return "AnvilDamagePacket(damage=" + this.damage + ", position=" + this.position + ")";
    }

    public int getDamage() {
        return this.damage;
    }

    public Vector3i getPosition() {
        return this.position;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ANVIL_DAMAGE;
    }
}
