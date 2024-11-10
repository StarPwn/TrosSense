package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class RemoveObjectivePacket implements BedrockPacket {
    private String objectiveId;

    public void setObjectiveId(String objectiveId) {
        this.objectiveId = objectiveId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof RemoveObjectivePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RemoveObjectivePacket)) {
            return false;
        }
        RemoveObjectivePacket other = (RemoveObjectivePacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$objectiveId = this.objectiveId;
        Object other$objectiveId = other.objectiveId;
        return this$objectiveId != null ? this$objectiveId.equals(other$objectiveId) : other$objectiveId == null;
    }

    public int hashCode() {
        Object $objectiveId = this.objectiveId;
        int result = (1 * 59) + ($objectiveId == null ? 43 : $objectiveId.hashCode());
        return result;
    }

    public String toString() {
        return "RemoveObjectivePacket(objectiveId=" + this.objectiveId + ")";
    }

    public String getObjectiveId() {
        return this.objectiveId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.REMOVE_OBJECTIVE;
    }
}
