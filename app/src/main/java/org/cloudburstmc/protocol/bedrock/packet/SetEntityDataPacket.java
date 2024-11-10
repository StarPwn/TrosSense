package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityProperties;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetEntityDataPacket implements BedrockPacket {
    private final EntityDataMap metadata = new EntityDataMap();
    private final EntityProperties properties = new EntityProperties();
    private long runtimeEntityId;
    private long tick;

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetEntityDataPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetEntityDataPacket)) {
            return false;
        }
        SetEntityDataPacket other = (SetEntityDataPacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId || this.tick != other.tick) {
            return false;
        }
        Object this$metadata = this.metadata;
        Object other$metadata = other.metadata;
        if (this$metadata != null ? !this$metadata.equals(other$metadata) : other$metadata != null) {
            return false;
        }
        Object this$properties = this.properties;
        Object other$properties = other.properties;
        return this$properties != null ? this$properties.equals(other$properties) : other$properties == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        long $tick = this.tick;
        int result2 = (result * 59) + ((int) (($tick >>> 32) ^ $tick));
        Object $metadata = this.metadata;
        int result3 = (result2 * 59) + ($metadata == null ? 43 : $metadata.hashCode());
        Object $properties = this.properties;
        return (result3 * 59) + ($properties != null ? $properties.hashCode() : 43);
    }

    public String toString() {
        return "SetEntityDataPacket(metadata=" + this.metadata + ", runtimeEntityId=" + this.runtimeEntityId + ", tick=" + this.tick + ", properties=" + this.properties + ")";
    }

    public EntityDataMap getMetadata() {
        return this.metadata;
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public long getTick() {
        return this.tick;
    }

    public EntityProperties getProperties() {
        return this.properties;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_ENTITY_DATA;
    }
}
