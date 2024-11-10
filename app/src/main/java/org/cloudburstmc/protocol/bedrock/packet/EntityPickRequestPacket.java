package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class EntityPickRequestPacket implements BedrockPacket {
    private int hotbarSlot;
    private long runtimeEntityId;
    private boolean withData;

    public void setHotbarSlot(int hotbarSlot) {
        this.hotbarSlot = hotbarSlot;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setWithData(boolean withData) {
        this.withData = withData;
    }

    protected boolean canEqual(Object other) {
        return other instanceof EntityPickRequestPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EntityPickRequestPacket)) {
            return false;
        }
        EntityPickRequestPacket other = (EntityPickRequestPacket) o;
        return other.canEqual(this) && this.runtimeEntityId == other.runtimeEntityId && this.hotbarSlot == other.hotbarSlot && this.withData == other.withData;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        return (((result * 59) + this.hotbarSlot) * 59) + (this.withData ? 79 : 97);
    }

    public String toString() {
        return "EntityPickRequestPacket(runtimeEntityId=" + this.runtimeEntityId + ", hotbarSlot=" + this.hotbarSlot + ", withData=" + this.withData + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public int getHotbarSlot() {
        return this.hotbarSlot;
    }

    public boolean isWithData() {
        return this.withData;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ENTITY_PICK_REQUEST;
    }
}
