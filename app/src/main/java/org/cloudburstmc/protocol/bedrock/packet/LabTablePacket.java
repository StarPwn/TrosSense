package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.inventory.LabTableReactionType;
import org.cloudburstmc.protocol.bedrock.data.inventory.LabTableType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class LabTablePacket implements BedrockPacket {
    private Vector3i position;
    private LabTableReactionType reactionType;
    private LabTableType type;

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public void setReactionType(LabTableReactionType reactionType) {
        this.reactionType = reactionType;
    }

    public void setType(LabTableType type) {
        this.type = type;
    }

    protected boolean canEqual(Object other) {
        return other instanceof LabTablePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LabTablePacket)) {
            return false;
        }
        LabTablePacket other = (LabTablePacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$type = this.type;
        Object other$type = other.type;
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        if (this$position != null ? !this$position.equals(other$position) : other$position != null) {
            return false;
        }
        Object this$reactionType = this.reactionType;
        Object other$reactionType = other.reactionType;
        return this$reactionType != null ? this$reactionType.equals(other$reactionType) : other$reactionType == null;
    }

    public int hashCode() {
        Object $type = this.type;
        int result = (1 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $position = this.position;
        int result2 = (result * 59) + ($position == null ? 43 : $position.hashCode());
        Object $reactionType = this.reactionType;
        return (result2 * 59) + ($reactionType != null ? $reactionType.hashCode() : 43);
    }

    public String toString() {
        return "LabTablePacket(type=" + this.type + ", position=" + this.position + ", reactionType=" + this.reactionType + ")";
    }

    public LabTableType getType() {
        return this.type;
    }

    public Vector3i getPosition() {
        return this.position;
    }

    public LabTableReactionType getReactionType() {
        return this.reactionType;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.LAB_TABLE;
    }
}
