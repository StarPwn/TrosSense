package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class InventorySlotPacket implements BedrockPacket {
    private int containerId;
    private ItemData item;
    private int slot;

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public void setItem(ItemData item) {
        this.item = item;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    protected boolean canEqual(Object other) {
        return other instanceof InventorySlotPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InventorySlotPacket)) {
            return false;
        }
        InventorySlotPacket other = (InventorySlotPacket) o;
        if (!other.canEqual(this) || this.containerId != other.containerId || this.slot != other.slot) {
            return false;
        }
        Object this$item = this.item;
        Object other$item = other.item;
        return this$item != null ? this$item.equals(other$item) : other$item == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.containerId;
        int result2 = (result * 59) + this.slot;
        Object $item = this.item;
        return (result2 * 59) + ($item == null ? 43 : $item.hashCode());
    }

    public String toString() {
        return "InventorySlotPacket(containerId=" + this.containerId + ", slot=" + this.slot + ", item=" + this.item + ")";
    }

    public int getContainerId() {
        return this.containerId;
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemData getItem() {
        return this.item;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.INVENTORY_SLOT;
    }
}
