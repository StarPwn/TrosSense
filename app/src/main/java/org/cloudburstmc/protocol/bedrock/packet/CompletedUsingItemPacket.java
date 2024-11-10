package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemUseType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class CompletedUsingItemPacket implements BedrockPacket {
    private int itemId;
    private ItemUseType type;

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setType(ItemUseType type) {
        this.type = type;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CompletedUsingItemPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CompletedUsingItemPacket)) {
            return false;
        }
        CompletedUsingItemPacket other = (CompletedUsingItemPacket) o;
        if (!other.canEqual(this) || this.itemId != other.itemId) {
            return false;
        }
        Object this$type = this.type;
        Object other$type = other.type;
        return this$type != null ? this$type.equals(other$type) : other$type == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.itemId;
        Object $type = this.type;
        return (result * 59) + ($type == null ? 43 : $type.hashCode());
    }

    public String toString() {
        return "CompletedUsingItemPacket(itemId=" + this.itemId + ", type=" + this.type + ")";
    }

    public int getItemId() {
        return this.itemId;
    }

    public ItemUseType getType() {
        return this.type;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.COMPLETED_USING_ITEM;
    }
}
