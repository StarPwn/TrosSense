package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class RemoveEntityPacket implements BedrockPacket {
    private long uniqueEntityId;

    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof RemoveEntityPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RemoveEntityPacket)) {
            return false;
        }
        RemoveEntityPacket other = (RemoveEntityPacket) o;
        return other.canEqual(this) && this.uniqueEntityId == other.uniqueEntityId;
    }

    public int hashCode() {
        long $uniqueEntityId = this.uniqueEntityId;
        int result = (1 * 59) + ((int) (($uniqueEntityId >>> 32) ^ $uniqueEntityId));
        return result;
    }

    public String toString() {
        return "RemoveEntityPacket(uniqueEntityId=" + this.uniqueEntityId + ")";
    }

    public long getUniqueEntityId() {
        return this.uniqueEntityId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.REMOVE_ENTITY;
    }
}
