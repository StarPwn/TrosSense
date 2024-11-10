package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetDisplayObjectivePacket implements BedrockPacket {
    private String criteria;
    private String displayName;
    private String displaySlot;
    private String objectiveId;
    private int sortOrder;

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDisplaySlot(String displaySlot) {
        this.displaySlot = displaySlot;
    }

    public void setObjectiveId(String objectiveId) {
        this.objectiveId = objectiveId;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetDisplayObjectivePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetDisplayObjectivePacket)) {
            return false;
        }
        SetDisplayObjectivePacket other = (SetDisplayObjectivePacket) o;
        if (!other.canEqual(this) || this.sortOrder != other.sortOrder) {
            return false;
        }
        Object this$displaySlot = this.displaySlot;
        Object other$displaySlot = other.displaySlot;
        if (this$displaySlot != null ? !this$displaySlot.equals(other$displaySlot) : other$displaySlot != null) {
            return false;
        }
        Object this$objectiveId = this.objectiveId;
        Object other$objectiveId = other.objectiveId;
        if (this$objectiveId != null ? !this$objectiveId.equals(other$objectiveId) : other$objectiveId != null) {
            return false;
        }
        Object this$displayName = this.displayName;
        Object other$displayName = other.displayName;
        if (this$displayName != null ? !this$displayName.equals(other$displayName) : other$displayName != null) {
            return false;
        }
        Object this$criteria = this.criteria;
        Object other$criteria = other.criteria;
        return this$criteria != null ? this$criteria.equals(other$criteria) : other$criteria == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.sortOrder;
        Object $displaySlot = this.displaySlot;
        int result2 = (result * 59) + ($displaySlot == null ? 43 : $displaySlot.hashCode());
        Object $objectiveId = this.objectiveId;
        int result3 = (result2 * 59) + ($objectiveId == null ? 43 : $objectiveId.hashCode());
        Object $displayName = this.displayName;
        int result4 = (result3 * 59) + ($displayName == null ? 43 : $displayName.hashCode());
        Object $criteria = this.criteria;
        return (result4 * 59) + ($criteria != null ? $criteria.hashCode() : 43);
    }

    public String toString() {
        return "SetDisplayObjectivePacket(displaySlot=" + this.displaySlot + ", objectiveId=" + this.objectiveId + ", displayName=" + this.displayName + ", criteria=" + this.criteria + ", sortOrder=" + this.sortOrder + ")";
    }

    public String getDisplaySlot() {
        return this.displaySlot;
    }

    public String getObjectiveId() {
        return this.objectiveId;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getCriteria() {
        return this.criteria;
    }

    public int getSortOrder() {
        return this.sortOrder;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_DISPLAY_OBJECTIVE;
    }
}
