package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class EntityFallPacket implements BedrockPacket {
    private float fallDistance;
    private boolean inVoid;
    private long runtimeEntityId;

    public void setFallDistance(float fallDistance) {
        this.fallDistance = fallDistance;
    }

    public void setInVoid(boolean inVoid) {
        this.inVoid = inVoid;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof EntityFallPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EntityFallPacket)) {
            return false;
        }
        EntityFallPacket other = (EntityFallPacket) o;
        return other.canEqual(this) && this.runtimeEntityId == other.runtimeEntityId && Float.compare(this.fallDistance, other.fallDistance) == 0 && this.inVoid == other.inVoid;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        return (((result * 59) + Float.floatToIntBits(this.fallDistance)) * 59) + (this.inVoid ? 79 : 97);
    }

    public String toString() {
        return "EntityFallPacket(runtimeEntityId=" + this.runtimeEntityId + ", fallDistance=" + this.fallDistance + ", inVoid=" + this.inVoid + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public float getFallDistance() {
        return this.fallDistance;
    }

    public boolean isInVoid() {
        return this.inVoid;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ENTITY_FALL;
    }
}
