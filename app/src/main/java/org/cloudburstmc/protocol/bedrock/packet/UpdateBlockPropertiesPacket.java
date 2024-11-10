package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class UpdateBlockPropertiesPacket implements BedrockPacket {
    private NbtMap properties;

    public void setProperties(NbtMap properties) {
        this.properties = properties;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UpdateBlockPropertiesPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateBlockPropertiesPacket)) {
            return false;
        }
        UpdateBlockPropertiesPacket other = (UpdateBlockPropertiesPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$properties = this.properties;
        Object other$properties = other.properties;
        return this$properties != null ? this$properties.equals(other$properties) : other$properties == null;
    }

    public int hashCode() {
        Object $properties = this.properties;
        int result = (1 * 59) + ($properties == null ? 43 : $properties.hashCode());
        return result;
    }

    public String toString() {
        return "UpdateBlockPropertiesPacket(properties=" + this.properties + ")";
    }

    public NbtMap getProperties() {
        return this.properties;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.UPDATE_BLOCK_PROPERTIES;
    }
}
