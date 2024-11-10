package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class GuiDataPickItemPacket implements BedrockPacket {
    private String description;
    private int hotbarSlot;
    private String itemEffects;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHotbarSlot(int hotbarSlot) {
        this.hotbarSlot = hotbarSlot;
    }

    public void setItemEffects(String itemEffects) {
        this.itemEffects = itemEffects;
    }

    protected boolean canEqual(Object other) {
        return other instanceof GuiDataPickItemPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GuiDataPickItemPacket)) {
            return false;
        }
        GuiDataPickItemPacket other = (GuiDataPickItemPacket) o;
        if (!other.canEqual(this) || this.hotbarSlot != other.hotbarSlot) {
            return false;
        }
        Object this$description = this.description;
        Object other$description = other.description;
        if (this$description != null ? !this$description.equals(other$description) : other$description != null) {
            return false;
        }
        Object this$itemEffects = this.itemEffects;
        Object other$itemEffects = other.itemEffects;
        return this$itemEffects != null ? this$itemEffects.equals(other$itemEffects) : other$itemEffects == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.hotbarSlot;
        Object $description = this.description;
        int result2 = (result * 59) + ($description == null ? 43 : $description.hashCode());
        Object $itemEffects = this.itemEffects;
        return (result2 * 59) + ($itemEffects != null ? $itemEffects.hashCode() : 43);
    }

    public String toString() {
        return "GuiDataPickItemPacket(description=" + this.description + ", itemEffects=" + this.itemEffects + ", hotbarSlot=" + this.hotbarSlot + ")";
    }

    public String getDescription() {
        return this.description;
    }

    public String getItemEffects() {
        return this.itemEffects;
    }

    public int getHotbarSlot() {
        return this.hotbarSlot;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.GUI_DATA_PICK_ITEM;
    }
}
