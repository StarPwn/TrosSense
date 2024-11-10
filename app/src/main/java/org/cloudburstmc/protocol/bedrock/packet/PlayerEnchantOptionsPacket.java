package org.cloudburstmc.protocol.bedrock.packet;

import java.util.ArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.inventory.EnchantOptionData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PlayerEnchantOptionsPacket implements BedrockPacket {
    private final List<EnchantOptionData> options = new ArrayList();

    protected boolean canEqual(Object other) {
        return other instanceof PlayerEnchantOptionsPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerEnchantOptionsPacket)) {
            return false;
        }
        PlayerEnchantOptionsPacket other = (PlayerEnchantOptionsPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$options = getOptions();
        Object other$options = other.getOptions();
        return this$options != null ? this$options.equals(other$options) : other$options == null;
    }

    public int hashCode() {
        Object $options = getOptions();
        int result = (1 * 59) + ($options == null ? 43 : $options.hashCode());
        return result;
    }

    public String toString() {
        return "PlayerEnchantOptionsPacket(options=" + this.options + ")";
    }

    public List<EnchantOptionData> getOptions() {
        return this.options;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.PLAYER_ENCHANT_OPTIONS;
    }
}
