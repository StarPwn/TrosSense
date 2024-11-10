package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class MobEquipmentPacket implements BedrockPacket {
    private int containerId;
    private int hotbarSlot;
    private int inventorySlot;
    private ItemData item;
    private long runtimeEntityId;

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public void setHotbarSlot(int hotbarSlot) {
        this.hotbarSlot = hotbarSlot;
    }

    public void setInventorySlot(int inventorySlot) {
        this.inventorySlot = inventorySlot;
    }

    public void setItem(ItemData item) {
        this.item = item;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MobEquipmentPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MobEquipmentPacket)) {
            return false;
        }
        MobEquipmentPacket other = (MobEquipmentPacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId || this.inventorySlot != other.inventorySlot || this.hotbarSlot != other.hotbarSlot || this.containerId != other.containerId) {
            return false;
        }
        Object this$item = this.item;
        Object other$item = other.item;
        return this$item != null ? this$item.equals(other$item) : other$item == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        int result2 = (((((result * 59) + this.inventorySlot) * 59) + this.hotbarSlot) * 59) + this.containerId;
        Object $item = this.item;
        return (result2 * 59) + ($item == null ? 43 : $item.hashCode());
    }

    public String toString() {
        return "MobEquipmentPacket(runtimeEntityId=" + this.runtimeEntityId + ", item=" + this.item + ", inventorySlot=" + this.inventorySlot + ", hotbarSlot=" + this.hotbarSlot + ", containerId=" + this.containerId + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public ItemData getItem() {
        return this.item;
    }

    public int getInventorySlot() {
        return this.inventorySlot;
    }

    public int getHotbarSlot() {
        return this.hotbarSlot;
    }

    public int getContainerId() {
        return this.containerId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.MOB_EQUIPMENT;
    }
}
