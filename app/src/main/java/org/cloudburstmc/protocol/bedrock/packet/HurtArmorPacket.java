package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class HurtArmorPacket implements BedrockPacket {
    private long armorSlots;
    private int cause;
    private int damage;

    public void setArmorSlots(long armorSlots) {
        this.armorSlots = armorSlots;
    }

    public void setCause(int cause) {
        this.cause = cause;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    protected boolean canEqual(Object other) {
        return other instanceof HurtArmorPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HurtArmorPacket)) {
            return false;
        }
        HurtArmorPacket other = (HurtArmorPacket) o;
        return other.canEqual(this) && this.cause == other.cause && this.damage == other.damage && this.armorSlots == other.armorSlots;
    }

    public int hashCode() {
        int result = (1 * 59) + this.cause;
        int result2 = (result * 59) + this.damage;
        long $armorSlots = this.armorSlots;
        return (result2 * 59) + ((int) (($armorSlots >>> 32) ^ $armorSlots));
    }

    public String toString() {
        return "HurtArmorPacket(cause=" + this.cause + ", damage=" + this.damage + ", armorSlots=" + this.armorSlots + ")";
    }

    public int getCause() {
        return this.cause;
    }

    public int getDamage() {
        return this.damage;
    }

    public long getArmorSlots() {
        return this.armorSlots;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.HURT_ARMOR;
    }
}
