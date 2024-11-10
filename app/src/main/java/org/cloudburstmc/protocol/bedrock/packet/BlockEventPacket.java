package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class BlockEventPacket implements BedrockPacket {
    private Vector3i blockPosition;
    private int eventData;
    private int eventType;

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public void setEventData(int eventData) {
        this.eventData = eventData;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    protected boolean canEqual(Object other) {
        return other instanceof BlockEventPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BlockEventPacket)) {
            return false;
        }
        BlockEventPacket other = (BlockEventPacket) o;
        if (!other.canEqual(this) || this.eventType != other.eventType || this.eventData != other.eventData) {
            return false;
        }
        Object this$blockPosition = this.blockPosition;
        Object other$blockPosition = other.blockPosition;
        return this$blockPosition != null ? this$blockPosition.equals(other$blockPosition) : other$blockPosition == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.eventType;
        int result2 = (result * 59) + this.eventData;
        Object $blockPosition = this.blockPosition;
        return (result2 * 59) + ($blockPosition == null ? 43 : $blockPosition.hashCode());
    }

    public String toString() {
        return "BlockEventPacket(blockPosition=" + this.blockPosition + ", eventType=" + this.eventType + ", eventData=" + this.eventData + ")";
    }

    public Vector3i getBlockPosition() {
        return this.blockPosition;
    }

    public int getEventType() {
        return this.eventType;
    }

    public int getEventData() {
        return this.eventData;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.BLOCK_EVENT;
    }
}
