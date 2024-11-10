package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class BlockPickRequestPacket implements BedrockPacket {
    private boolean addUserData;
    private Vector3i blockPosition;
    private int hotbarSlot;

    public void setAddUserData(boolean addUserData) {
        this.addUserData = addUserData;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public void setHotbarSlot(int hotbarSlot) {
        this.hotbarSlot = hotbarSlot;
    }

    protected boolean canEqual(Object other) {
        return other instanceof BlockPickRequestPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BlockPickRequestPacket)) {
            return false;
        }
        BlockPickRequestPacket other = (BlockPickRequestPacket) o;
        if (!other.canEqual(this) || this.addUserData != other.addUserData || this.hotbarSlot != other.hotbarSlot) {
            return false;
        }
        Object this$blockPosition = this.blockPosition;
        Object other$blockPosition = other.blockPosition;
        return this$blockPosition != null ? this$blockPosition.equals(other$blockPosition) : other$blockPosition == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.addUserData ? 79 : 97);
        int result2 = (result * 59) + this.hotbarSlot;
        Object $blockPosition = this.blockPosition;
        return (result2 * 59) + ($blockPosition == null ? 43 : $blockPosition.hashCode());
    }

    public String toString() {
        return "BlockPickRequestPacket(blockPosition=" + this.blockPosition + ", addUserData=" + this.addUserData + ", hotbarSlot=" + this.hotbarSlot + ")";
    }

    public Vector3i getBlockPosition() {
        return this.blockPosition;
    }

    public boolean isAddUserData() {
        return this.addUserData;
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
        return BedrockPacketType.BLOCK_PICK_REQUEST;
    }
}
