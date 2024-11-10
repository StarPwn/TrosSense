package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PlayerStartItemCooldownPacket implements BedrockPacket {
    private int cooldownDuration;
    private String itemCategory;

    public void setCooldownDuration(int cooldownDuration) {
        this.cooldownDuration = cooldownDuration;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PlayerStartItemCooldownPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerStartItemCooldownPacket)) {
            return false;
        }
        PlayerStartItemCooldownPacket other = (PlayerStartItemCooldownPacket) o;
        if (!other.canEqual(this) || this.cooldownDuration != other.cooldownDuration) {
            return false;
        }
        Object this$itemCategory = this.itemCategory;
        Object other$itemCategory = other.itemCategory;
        return this$itemCategory != null ? this$itemCategory.equals(other$itemCategory) : other$itemCategory == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.cooldownDuration;
        Object $itemCategory = this.itemCategory;
        return (result * 59) + ($itemCategory == null ? 43 : $itemCategory.hashCode());
    }

    public String toString() {
        return "PlayerStartItemCooldownPacket(itemCategory=" + this.itemCategory + ", cooldownDuration=" + this.cooldownDuration + ")";
    }

    public String getItemCategory() {
        return this.itemCategory;
    }

    public int getCooldownDuration() {
        return this.cooldownDuration;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.PLAYER_START_ITEM_COOLDOWN;
    }
}
