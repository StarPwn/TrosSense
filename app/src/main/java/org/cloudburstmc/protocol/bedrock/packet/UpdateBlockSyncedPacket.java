package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.BlockSyncType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class UpdateBlockSyncedPacket extends UpdateBlockPacket {
    private BlockSyncType entityBlockSyncType;
    private long runtimeEntityId;

    public void setEntityBlockSyncType(BlockSyncType entityBlockSyncType) {
        this.entityBlockSyncType = entityBlockSyncType;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket
    protected boolean canEqual(Object other) {
        return other instanceof UpdateBlockSyncedPacket;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateBlockSyncedPacket)) {
            return false;
        }
        UpdateBlockSyncedPacket other = (UpdateBlockSyncedPacket) o;
        if (!other.canEqual(this) || !super.equals(o) || this.runtimeEntityId != other.runtimeEntityId) {
            return false;
        }
        Object this$entityBlockSyncType = this.entityBlockSyncType;
        Object other$entityBlockSyncType = other.entityBlockSyncType;
        return this$entityBlockSyncType != null ? this$entityBlockSyncType.equals(other$entityBlockSyncType) : other$entityBlockSyncType == null;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket
    public int hashCode() {
        int result = super.hashCode();
        long $runtimeEntityId = this.runtimeEntityId;
        int result2 = (result * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        Object $entityBlockSyncType = this.entityBlockSyncType;
        return (result2 * 59) + ($entityBlockSyncType == null ? 43 : $entityBlockSyncType.hashCode());
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public BlockSyncType getEntityBlockSyncType() {
        return this.entityBlockSyncType;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket, org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket, org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.UPDATE_BLOCK_SYNCED;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket
    public String toString() {
        return "UpdateBlockSyncedPacket(runtimeEntityId=" + this.runtimeEntityId + ", entityBlockSyncType=" + this.entityBlockSyncType + ", flags=" + this.flags + ", blockPosition=" + this.blockPosition + ", definition=" + this.definition + ", dataLayer=" + this.dataLayer + ")";
    }
}
