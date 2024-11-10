package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class TakeItemEntityPacket implements BedrockPacket {
    private long itemRuntimeEntityId;
    private long runtimeEntityId;

    public void setItemRuntimeEntityId(long itemRuntimeEntityId) {
        this.itemRuntimeEntityId = itemRuntimeEntityId;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof TakeItemEntityPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TakeItemEntityPacket)) {
            return false;
        }
        TakeItemEntityPacket other = (TakeItemEntityPacket) o;
        return other.canEqual(this) && this.itemRuntimeEntityId == other.itemRuntimeEntityId && this.runtimeEntityId == other.runtimeEntityId;
    }

    public int hashCode() {
        long $itemRuntimeEntityId = this.itemRuntimeEntityId;
        int result = (1 * 59) + ((int) (($itemRuntimeEntityId >>> 32) ^ $itemRuntimeEntityId));
        long $runtimeEntityId = this.runtimeEntityId;
        return (result * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
    }

    public String toString() {
        return "TakeItemEntityPacket(itemRuntimeEntityId=" + this.itemRuntimeEntityId + ", runtimeEntityId=" + this.runtimeEntityId + ")";
    }

    public long getItemRuntimeEntityId() {
        return this.itemRuntimeEntityId;
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.TAKE_ITEM_ENTITY;
    }
}
