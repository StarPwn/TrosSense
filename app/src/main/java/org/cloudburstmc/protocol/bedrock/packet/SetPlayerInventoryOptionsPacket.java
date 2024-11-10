package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.inventory.InventoryLayout;
import org.cloudburstmc.protocol.bedrock.data.inventory.InventoryTabLeft;
import org.cloudburstmc.protocol.bedrock.data.inventory.InventoryTabRight;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetPlayerInventoryOptionsPacket implements BedrockPacket {
    private InventoryLayout craftingLayout;
    private boolean filtering;
    private InventoryLayout layout;
    private InventoryTabLeft leftTab;
    private InventoryTabRight rightTab;

    public void setCraftingLayout(InventoryLayout craftingLayout) {
        this.craftingLayout = craftingLayout;
    }

    public void setFiltering(boolean filtering) {
        this.filtering = filtering;
    }

    public void setLayout(InventoryLayout layout) {
        this.layout = layout;
    }

    public void setLeftTab(InventoryTabLeft leftTab) {
        this.leftTab = leftTab;
    }

    public void setRightTab(InventoryTabRight rightTab) {
        this.rightTab = rightTab;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetPlayerInventoryOptionsPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetPlayerInventoryOptionsPacket)) {
            return false;
        }
        SetPlayerInventoryOptionsPacket other = (SetPlayerInventoryOptionsPacket) o;
        if (!other.canEqual(this) || this.filtering != other.filtering) {
            return false;
        }
        Object this$leftTab = this.leftTab;
        Object other$leftTab = other.leftTab;
        if (this$leftTab != null ? !this$leftTab.equals(other$leftTab) : other$leftTab != null) {
            return false;
        }
        Object this$rightTab = this.rightTab;
        Object other$rightTab = other.rightTab;
        if (this$rightTab != null ? !this$rightTab.equals(other$rightTab) : other$rightTab != null) {
            return false;
        }
        Object this$layout = this.layout;
        Object other$layout = other.layout;
        if (this$layout != null ? !this$layout.equals(other$layout) : other$layout != null) {
            return false;
        }
        Object this$craftingLayout = this.craftingLayout;
        Object other$craftingLayout = other.craftingLayout;
        return this$craftingLayout != null ? this$craftingLayout.equals(other$craftingLayout) : other$craftingLayout == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.filtering ? 79 : 97);
        Object $leftTab = this.leftTab;
        int result2 = (result * 59) + ($leftTab == null ? 43 : $leftTab.hashCode());
        Object $rightTab = this.rightTab;
        int result3 = (result2 * 59) + ($rightTab == null ? 43 : $rightTab.hashCode());
        Object $layout = this.layout;
        int result4 = (result3 * 59) + ($layout == null ? 43 : $layout.hashCode());
        Object $craftingLayout = this.craftingLayout;
        return (result4 * 59) + ($craftingLayout != null ? $craftingLayout.hashCode() : 43);
    }

    public String toString() {
        return "SetPlayerInventoryOptionsPacket(leftTab=" + this.leftTab + ", rightTab=" + this.rightTab + ", filtering=" + this.filtering + ", layout=" + this.layout + ", craftingLayout=" + this.craftingLayout + ")";
    }

    public InventoryTabLeft getLeftTab() {
        return this.leftTab;
    }

    public InventoryTabRight getRightTab() {
        return this.rightTab;
    }

    public boolean isFiltering() {
        return this.filtering;
    }

    public InventoryLayout getLayout() {
        return this.layout;
    }

    public InventoryLayout getCraftingLayout() {
        return this.craftingLayout;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_PLAYER_INVENTORY_OPTIONS;
    }
}
