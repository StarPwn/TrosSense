package org.cloudburstmc.protocol.bedrock.packet;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class MovePlayerPacket implements BedrockPacket {
    private int entityType;
    private Mode mode;
    private boolean onGround;
    private Vector3f position;
    private long ridingRuntimeEntityId;
    private Vector3f rotation;
    private long runtimeEntityId;
    private TeleportationCause teleportationCause;
    private long tick;

    /* loaded from: classes5.dex */
    public enum Mode {
        NORMAL,
        RESPAWN,
        TELEPORT,
        HEAD_ROTATION
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRidingRuntimeEntityId(long ridingRuntimeEntityId) {
        this.ridingRuntimeEntityId = ridingRuntimeEntityId;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setTeleportationCause(TeleportationCause teleportationCause) {
        this.teleportationCause = teleportationCause;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MovePlayerPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MovePlayerPacket)) {
            return false;
        }
        MovePlayerPacket other = (MovePlayerPacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId || this.onGround != other.onGround || this.ridingRuntimeEntityId != other.ridingRuntimeEntityId || this.entityType != other.entityType || this.tick != other.tick) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        if (this$position != null ? !this$position.equals(other$position) : other$position != null) {
            return false;
        }
        Object this$rotation = this.rotation;
        Object other$rotation = other.rotation;
        if (this$rotation != null ? !this$rotation.equals(other$rotation) : other$rotation != null) {
            return false;
        }
        Object this$mode = this.mode;
        Object other$mode = other.mode;
        if (this$mode != null ? !this$mode.equals(other$mode) : other$mode != null) {
            return false;
        }
        Object this$teleportationCause = this.teleportationCause;
        Object other$teleportationCause = other.teleportationCause;
        return this$teleportationCause != null ? this$teleportationCause.equals(other$teleportationCause) : other$teleportationCause == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        int result2 = (result * 59) + (this.onGround ? 79 : 97);
        long $ridingRuntimeEntityId = this.ridingRuntimeEntityId;
        int result3 = (((result2 * 59) + ((int) (($ridingRuntimeEntityId >>> 32) ^ $ridingRuntimeEntityId))) * 59) + this.entityType;
        long $tick = this.tick;
        int result4 = (result3 * 59) + ((int) (($tick >>> 32) ^ $tick));
        Object $position = this.position;
        int result5 = (result4 * 59) + ($position == null ? 43 : $position.hashCode());
        Object $rotation = this.rotation;
        int result6 = (result5 * 59) + ($rotation == null ? 43 : $rotation.hashCode());
        Object $mode = this.mode;
        int result7 = (result6 * 59) + ($mode == null ? 43 : $mode.hashCode());
        Object $teleportationCause = this.teleportationCause;
        return (result7 * 59) + ($teleportationCause != null ? $teleportationCause.hashCode() : 43);
    }

    public String toString() {
        return "MovePlayerPacket(runtimeEntityId=" + this.runtimeEntityId + ", position=" + this.position + ", rotation=" + this.rotation + ", mode=" + this.mode + ", onGround=" + this.onGround + ", ridingRuntimeEntityId=" + this.ridingRuntimeEntityId + ", teleportationCause=" + this.teleportationCause + ", entityType=" + this.entityType + ", tick=" + this.tick + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public Vector3f getRotation() {
        return this.rotation;
    }

    public Mode getMode() {
        return this.mode;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public long getRidingRuntimeEntityId() {
        return this.ridingRuntimeEntityId;
    }

    public TeleportationCause getTeleportationCause() {
        return this.teleportationCause;
    }

    public int getEntityType() {
        return this.entityType;
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
        return BedrockPacketType.MOVE_PLAYER;
    }

    /* loaded from: classes5.dex */
    public enum TeleportationCause {
        UNKNOWN,
        PROJECTILE,
        CHORUS_FRUIT,
        COMMAND,
        BEHAVIOR;

        private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) TeleportationCause.class);
        private static final TeleportationCause[] VALUES = values();

        public static TeleportationCause byId(int id) {
            if (id >= 0 && id < VALUES.length) {
                return VALUES[id];
            }
            log.debug("Unknown teleportation cause ID: {}", Integer.valueOf(id));
            return UNKNOWN;
        }
    }
}
