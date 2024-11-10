package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.entity.EntityEventType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class EntityEventPacket implements BedrockPacket {
    private int data;
    private long runtimeEntityId;
    private EntityEventType type;

    public void setData(int data) {
        this.data = data;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setType(EntityEventType type) {
        this.type = type;
    }

    protected boolean canEqual(Object other) {
        return other instanceof EntityEventPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EntityEventPacket)) {
            return false;
        }
        EntityEventPacket other = (EntityEventPacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId || this.data != other.data) {
            return false;
        }
        Object this$type = this.type;
        Object other$type = other.type;
        return this$type != null ? this$type.equals(other$type) : other$type == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        int result2 = (result * 59) + this.data;
        Object $type = this.type;
        return (result2 * 59) + ($type == null ? 43 : $type.hashCode());
    }

    public String toString() {
        return "EntityEventPacket(runtimeEntityId=" + this.runtimeEntityId + ", type=" + this.type + ", data=" + this.data + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public EntityEventType getType() {
        return this.type;
    }

    public int getData() {
        return this.data;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ENTITY_EVENT;
    }
}
