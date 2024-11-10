package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ContainerOpenPacket implements BedrockPacket {
    private Vector3i blockPosition;
    private byte id;
    private ContainerType type;
    private long uniqueEntityId = -1;

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public void setType(ContainerType type) {
        this.type = type;
    }

    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ContainerOpenPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ContainerOpenPacket)) {
            return false;
        }
        ContainerOpenPacket other = (ContainerOpenPacket) o;
        if (!other.canEqual(this) || this.id != other.id || this.uniqueEntityId != other.uniqueEntityId) {
            return false;
        }
        Object this$type = this.type;
        Object other$type = other.type;
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$blockPosition = this.blockPosition;
        Object other$blockPosition = other.blockPosition;
        return this$blockPosition != null ? this$blockPosition.equals(other$blockPosition) : other$blockPosition == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.id;
        long $uniqueEntityId = this.uniqueEntityId;
        int result2 = (result * 59) + ((int) (($uniqueEntityId >>> 32) ^ $uniqueEntityId));
        Object $type = this.type;
        int result3 = (result2 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $blockPosition = this.blockPosition;
        return (result3 * 59) + ($blockPosition != null ? $blockPosition.hashCode() : 43);
    }

    public String toString() {
        return "ContainerOpenPacket(id=" + ((int) this.id) + ", type=" + this.type + ", blockPosition=" + this.blockPosition + ", uniqueEntityId=" + this.uniqueEntityId + ")";
    }

    public byte getId() {
        return this.id;
    }

    public ContainerType getType() {
        return this.type;
    }

    public Vector3i getBlockPosition() {
        return this.blockPosition;
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
        return BedrockPacketType.CONTAINER_OPEN;
    }
}
