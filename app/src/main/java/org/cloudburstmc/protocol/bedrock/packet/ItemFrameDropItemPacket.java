package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.common.PacketSignal;

@Deprecated
/* loaded from: classes5.dex */
public class ItemFrameDropItemPacket implements BedrockPacket {
    private Vector3i blockPosition;

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ItemFrameDropItemPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemFrameDropItemPacket)) {
            return false;
        }
        ItemFrameDropItemPacket other = (ItemFrameDropItemPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$blockPosition = this.blockPosition;
        Object other$blockPosition = other.blockPosition;
        return this$blockPosition != null ? this$blockPosition.equals(other$blockPosition) : other$blockPosition == null;
    }

    public int hashCode() {
        Object $blockPosition = this.blockPosition;
        int result = (1 * 59) + ($blockPosition == null ? 43 : $blockPosition.hashCode());
        return result;
    }

    public String toString() {
        return "ItemFrameDropItemPacket(blockPosition=" + this.blockPosition + ")";
    }

    public Vector3i getBlockPosition() {
        return this.blockPosition;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ITEM_FRAME_DROP_ITEM;
    }
}
