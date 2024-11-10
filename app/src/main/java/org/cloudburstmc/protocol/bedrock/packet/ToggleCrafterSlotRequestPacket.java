package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ToggleCrafterSlotRequestPacket implements BedrockPacket {
    private Vector3i blockPosition;
    private boolean disabled;
    private byte slot;

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setSlot(byte slot) {
        this.slot = slot;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ToggleCrafterSlotRequestPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ToggleCrafterSlotRequestPacket)) {
            return false;
        }
        ToggleCrafterSlotRequestPacket other = (ToggleCrafterSlotRequestPacket) o;
        if (!other.canEqual(this) || this.slot != other.slot || this.disabled != other.disabled) {
            return false;
        }
        Object this$blockPosition = this.blockPosition;
        Object other$blockPosition = other.blockPosition;
        return this$blockPosition != null ? this$blockPosition.equals(other$blockPosition) : other$blockPosition == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.slot;
        int result2 = result * 59;
        int i = this.disabled ? 79 : 97;
        Object $blockPosition = this.blockPosition;
        return ((result2 + i) * 59) + ($blockPosition == null ? 43 : $blockPosition.hashCode());
    }

    public String toString() {
        return "ToggleCrafterSlotRequestPacket(blockPosition=" + this.blockPosition + ", slot=" + ((int) this.slot) + ", disabled=" + this.disabled + ")";
    }

    public Vector3i getBlockPosition() {
        return this.blockPosition;
    }

    public byte getSlot() {
        return this.slot;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.TOGGLE_CRAFTER_SLOT_REQUEST;
    }
}
