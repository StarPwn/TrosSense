package org.cloudburstmc.protocol.bedrock.packet;

import java.util.EnumSet;
import java.util.Set;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class MoveEntityDeltaPacket implements BedrockPacket {
    private int deltaX;
    private int deltaY;
    private int deltaZ;
    private final Set<Flag> flags = EnumSet.noneOf(Flag.class);
    private float headYaw;
    private float pitch;
    private long runtimeEntityId;
    private float x;
    private float y;
    private float yaw;
    private float z;

    /* loaded from: classes5.dex */
    public enum Flag {
        HAS_X,
        HAS_Y,
        HAS_Z,
        HAS_PITCH,
        HAS_YAW,
        HAS_HEAD_YAW,
        ON_GROUND,
        TELEPORTING,
        FORCE_MOVE_LOCAL_ENTITY
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    public void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
    }

    public void setDeltaZ(int deltaZ) {
        this.deltaZ = deltaZ;
    }

    public void setHeadYaw(float headYaw) {
        this.headYaw = headYaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setZ(float z) {
        this.z = z;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MoveEntityDeltaPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MoveEntityDeltaPacket)) {
            return false;
        }
        MoveEntityDeltaPacket other = (MoveEntityDeltaPacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId || this.deltaX != other.deltaX || this.deltaY != other.deltaY || this.deltaZ != other.deltaZ || Float.compare(this.x, other.x) != 0 || Float.compare(this.y, other.y) != 0 || Float.compare(this.z, other.z) != 0 || Float.compare(this.pitch, other.pitch) != 0 || Float.compare(this.yaw, other.yaw) != 0 || Float.compare(this.headYaw, other.headYaw) != 0) {
            return false;
        }
        Object this$flags = this.flags;
        Object other$flags = other.flags;
        return this$flags != null ? this$flags.equals(other$flags) : other$flags == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        int result2 = (((((((((((((((((result * 59) + this.deltaX) * 59) + this.deltaY) * 59) + this.deltaZ) * 59) + Float.floatToIntBits(this.x)) * 59) + Float.floatToIntBits(this.y)) * 59) + Float.floatToIntBits(this.z)) * 59) + Float.floatToIntBits(this.pitch)) * 59) + Float.floatToIntBits(this.yaw)) * 59) + Float.floatToIntBits(this.headYaw);
        Object $flags = this.flags;
        return (result2 * 59) + ($flags == null ? 43 : $flags.hashCode());
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public Set<Flag> getFlags() {
        return this.flags;
    }

    public int getDeltaX() {
        return this.deltaX;
    }

    public int getDeltaY() {
        return this.deltaY;
    }

    public int getDeltaZ() {
        return this.deltaZ;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getHeadYaw() {
        return this.headYaw;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.MOVE_ENTITY_DELTA;
    }

    public String toString() {
        return "MoveEntityDeltaPacket(runtimeEntityId=" + this.runtimeEntityId + ", flags=" + this.flags + ", delta=(" + this.deltaX + ", " + this.deltaY + ", " + this.deltaZ + "), position=(" + this.x + ", " + this.y + ", " + this.z + "), rotation=(" + this.pitch + ", " + this.yaw + ", " + this.headYaw + "))";
    }
}
