package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetSpawnPositionPacket implements BedrockPacket {
    private Vector3i blockPosition;
    private int dimensionId;

    @Deprecated
    private boolean spawnForced;
    private Vector3i spawnPosition = Vector3i.from(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    private Type spawnType;

    /* loaded from: classes5.dex */
    public enum Type {
        PLAYER_SPAWN,
        WORLD_SPAWN
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public void setDimensionId(int dimensionId) {
        this.dimensionId = dimensionId;
    }

    @Deprecated
    public void setSpawnForced(boolean spawnForced) {
        this.spawnForced = spawnForced;
    }

    public void setSpawnPosition(Vector3i spawnPosition) {
        this.spawnPosition = spawnPosition;
    }

    public void setSpawnType(Type spawnType) {
        this.spawnType = spawnType;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetSpawnPositionPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetSpawnPositionPacket)) {
            return false;
        }
        SetSpawnPositionPacket other = (SetSpawnPositionPacket) o;
        if (!other.canEqual(this) || this.dimensionId != other.dimensionId || this.spawnForced != other.spawnForced) {
            return false;
        }
        Object this$spawnType = this.spawnType;
        Object other$spawnType = other.spawnType;
        if (this$spawnType != null ? !this$spawnType.equals(other$spawnType) : other$spawnType != null) {
            return false;
        }
        Object this$blockPosition = this.blockPosition;
        Object other$blockPosition = other.blockPosition;
        if (this$blockPosition != null ? !this$blockPosition.equals(other$blockPosition) : other$blockPosition != null) {
            return false;
        }
        Object this$spawnPosition = this.spawnPosition;
        Object other$spawnPosition = other.spawnPosition;
        return this$spawnPosition != null ? this$spawnPosition.equals(other$spawnPosition) : other$spawnPosition == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.dimensionId;
        int result2 = result * 59;
        int i = this.spawnForced ? 79 : 97;
        Object $spawnType = this.spawnType;
        int result3 = ((result2 + i) * 59) + ($spawnType == null ? 43 : $spawnType.hashCode());
        Object $blockPosition = this.blockPosition;
        int result4 = (result3 * 59) + ($blockPosition == null ? 43 : $blockPosition.hashCode());
        Object $spawnPosition = this.spawnPosition;
        return (result4 * 59) + ($spawnPosition != null ? $spawnPosition.hashCode() : 43);
    }

    public String toString() {
        return "SetSpawnPositionPacket(spawnType=" + this.spawnType + ", blockPosition=" + this.blockPosition + ", dimensionId=" + this.dimensionId + ", spawnPosition=" + this.spawnPosition + ", spawnForced=" + this.spawnForced + ")";
    }

    public Type getSpawnType() {
        return this.spawnType;
    }

    public Vector3i getBlockPosition() {
        return this.blockPosition;
    }

    public int getDimensionId() {
        return this.dimensionId;
    }

    public Vector3i getSpawnPosition() {
        return this.spawnPosition;
    }

    @Deprecated
    public boolean isSpawnForced() {
        return this.spawnForced;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_SPAWN_POSITION;
    }
}
