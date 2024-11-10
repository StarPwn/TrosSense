package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.UUID;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class EmoteListPacket implements BedrockPacket {
    private final List<UUID> pieceIds = new ObjectArrayList();
    private long runtimeEntityId;

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof EmoteListPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EmoteListPacket)) {
            return false;
        }
        EmoteListPacket other = (EmoteListPacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId) {
            return false;
        }
        Object this$pieceIds = this.pieceIds;
        Object other$pieceIds = other.pieceIds;
        return this$pieceIds != null ? this$pieceIds.equals(other$pieceIds) : other$pieceIds == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        Object $pieceIds = this.pieceIds;
        return (result * 59) + ($pieceIds == null ? 43 : $pieceIds.hashCode());
    }

    public String toString() {
        return "EmoteListPacket(runtimeEntityId=" + this.runtimeEntityId + ", pieceIds=" + this.pieceIds + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public List<UUID> getPieceIds() {
        return this.pieceIds;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.EMOTE_LIST;
    }
}
