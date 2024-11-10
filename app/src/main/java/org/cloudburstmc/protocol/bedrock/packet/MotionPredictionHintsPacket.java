package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class MotionPredictionHintsPacket implements BedrockPacket {
    private Vector3f motion;
    private boolean onGround;
    private long runtimeEntityId;

    public void setMotion(Vector3f motion) {
        this.motion = motion;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MotionPredictionHintsPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MotionPredictionHintsPacket)) {
            return false;
        }
        MotionPredictionHintsPacket other = (MotionPredictionHintsPacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId || this.onGround != other.onGround) {
            return false;
        }
        Object this$motion = this.motion;
        Object other$motion = other.motion;
        return this$motion != null ? this$motion.equals(other$motion) : other$motion == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        int result2 = result * 59;
        int i = this.onGround ? 79 : 97;
        Object $motion = this.motion;
        return ((result2 + i) * 59) + ($motion == null ? 43 : $motion.hashCode());
    }

    public String toString() {
        return "MotionPredictionHintsPacket(runtimeEntityId=" + this.runtimeEntityId + ", motion=" + this.motion + ", onGround=" + this.onGround + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public Vector3f getMotion() {
        return this.motion;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_ENTITY_MOTION_PLUS;
    }
}
