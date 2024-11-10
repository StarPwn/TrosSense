package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetLastHurtByPacket implements BedrockPacket {
    private int entityTypeId;

    public void setEntityTypeId(int entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetLastHurtByPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetLastHurtByPacket)) {
            return false;
        }
        SetLastHurtByPacket other = (SetLastHurtByPacket) o;
        return other.canEqual(this) && this.entityTypeId == other.entityTypeId;
    }

    public int hashCode() {
        int result = (1 * 59) + this.entityTypeId;
        return result;
    }

    public String toString() {
        return "SetLastHurtByPacket(entityTypeId=" + this.entityTypeId + ")";
    }

    public int getEntityTypeId() {
        return this.entityTypeId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_LAST_HURT_BY;
    }
}
