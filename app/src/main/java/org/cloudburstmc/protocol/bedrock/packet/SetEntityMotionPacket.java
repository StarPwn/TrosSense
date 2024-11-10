package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetEntityMotionPacket implements BedrockPacket {
    private Vector3f motion;
    private long runtimeEntityId;
    private long tick;

    public void setMotion(Vector3f motion) {
        this.motion = motion;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetEntityMotionPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetEntityMotionPacket)) {
            return false;
        }
        SetEntityMotionPacket other = (SetEntityMotionPacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId || this.tick != other.tick) {
            return false;
        }
        Object this$motion = this.motion;
        Object other$motion = other.motion;
        return this$motion != null ? this$motion.equals(other$motion) : other$motion == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        long $tick = this.tick;
        int result2 = (result * 59) + ((int) (($tick >>> 32) ^ $tick));
        Object $motion = this.motion;
        return (result2 * 59) + ($motion == null ? 43 : $motion.hashCode());
    }

    public String toString() {
        return "SetEntityMotionPacket(runtimeEntityId=" + this.runtimeEntityId + ", motion=" + this.motion + ", tick=" + this.tick + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public Vector3f getMotion() {
        return this.motion;
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
        return BedrockPacketType.SET_ENTITY_MOTION;
    }
}
