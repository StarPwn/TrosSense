package org.cloudburstmc.protocol.bedrock.packet;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import org.cloudburstmc.protocol.bedrock.data.PlayerArmorDamageFlag;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PlayerArmorDamagePacket implements BedrockPacket {
    private final Set<PlayerArmorDamageFlag> flags = EnumSet.noneOf(PlayerArmorDamageFlag.class);
    private final int[] damage = new int[4];

    protected boolean canEqual(Object other) {
        return other instanceof PlayerArmorDamagePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerArmorDamagePacket)) {
            return false;
        }
        PlayerArmorDamagePacket other = (PlayerArmorDamagePacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$flags = this.flags;
        Object other$flags = other.flags;
        if (this$flags != null ? this$flags.equals(other$flags) : other$flags == null) {
            return Arrays.equals(this.damage, other.damage);
        }
        return false;
    }

    public int hashCode() {
        Object $flags = this.flags;
        int result = (1 * 59) + ($flags == null ? 43 : $flags.hashCode());
        return (result * 59) + Arrays.hashCode(this.damage);
    }

    public String toString() {
        return "PlayerArmorDamagePacket(flags=" + this.flags + ", damage=" + Arrays.toString(this.damage) + ")";
    }

    public Set<PlayerArmorDamageFlag> getFlags() {
        return this.flags;
    }

    public int[] getDamage() {
        return this.damage;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.PLAYER_ARMOR_DAMAGE;
    }
}
