package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PlayerHotbarPacket implements BedrockPacket {
    private int containerId;
    private boolean selectHotbarSlot;
    private int selectedHotbarSlot;

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public void setSelectHotbarSlot(boolean selectHotbarSlot) {
        this.selectHotbarSlot = selectHotbarSlot;
    }

    public void setSelectedHotbarSlot(int selectedHotbarSlot) {
        this.selectedHotbarSlot = selectedHotbarSlot;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PlayerHotbarPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerHotbarPacket)) {
            return false;
        }
        PlayerHotbarPacket other = (PlayerHotbarPacket) o;
        return other.canEqual(this) && this.selectedHotbarSlot == other.selectedHotbarSlot && this.containerId == other.containerId && this.selectHotbarSlot == other.selectHotbarSlot;
    }

    public int hashCode() {
        int result = (1 * 59) + this.selectedHotbarSlot;
        return (((result * 59) + this.containerId) * 59) + (this.selectHotbarSlot ? 79 : 97);
    }

    public String toString() {
        return "PlayerHotbarPacket(selectedHotbarSlot=" + this.selectedHotbarSlot + ", containerId=" + this.containerId + ", selectHotbarSlot=" + this.selectHotbarSlot + ")";
    }

    public int getSelectedHotbarSlot() {
        return this.selectedHotbarSlot;
    }

    public int getContainerId() {
        return this.containerId;
    }

    public boolean isSelectHotbarSlot() {
        return this.selectHotbarSlot;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.PLAYER_HOTBAR;
    }
}
