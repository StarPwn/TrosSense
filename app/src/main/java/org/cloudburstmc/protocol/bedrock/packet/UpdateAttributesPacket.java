package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.AttributeData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class UpdateAttributesPacket implements BedrockPacket {
    private List<AttributeData> attributes = new ObjectArrayList();
    private long runtimeEntityId;
    private long tick;

    public void setAttributes(List<AttributeData> attributes) {
        this.attributes = attributes;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UpdateAttributesPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateAttributesPacket)) {
            return false;
        }
        UpdateAttributesPacket other = (UpdateAttributesPacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId || this.tick != other.tick) {
            return false;
        }
        Object this$attributes = this.attributes;
        Object other$attributes = other.attributes;
        return this$attributes != null ? this$attributes.equals(other$attributes) : other$attributes == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        long $tick = this.tick;
        int result2 = (result * 59) + ((int) (($tick >>> 32) ^ $tick));
        Object $attributes = this.attributes;
        return (result2 * 59) + ($attributes == null ? 43 : $attributes.hashCode());
    }

    public String toString() {
        return "UpdateAttributesPacket(runtimeEntityId=" + this.runtimeEntityId + ", attributes=" + this.attributes + ", tick=" + this.tick + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public List<AttributeData> getAttributes() {
        return this.attributes;
    }

    public long getTick() {
        return this.tick;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.UPDATE_ATTRIBUTES;
    }
}
