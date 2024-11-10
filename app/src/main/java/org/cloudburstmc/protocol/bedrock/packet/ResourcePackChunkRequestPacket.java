package org.cloudburstmc.protocol.bedrock.packet;

import java.util.UUID;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ResourcePackChunkRequestPacket implements BedrockPacket {
    private int chunkIndex;
    private UUID packId;
    private String packVersion;

    public void setChunkIndex(int chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    public void setPackId(UUID packId) {
        this.packId = packId;
    }

    public void setPackVersion(String packVersion) {
        this.packVersion = packVersion;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ResourcePackChunkRequestPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ResourcePackChunkRequestPacket)) {
            return false;
        }
        ResourcePackChunkRequestPacket other = (ResourcePackChunkRequestPacket) o;
        if (!other.canEqual(this) || this.chunkIndex != other.chunkIndex) {
            return false;
        }
        Object this$packId = this.packId;
        Object other$packId = other.packId;
        if (this$packId != null ? !this$packId.equals(other$packId) : other$packId != null) {
            return false;
        }
        Object this$packVersion = this.packVersion;
        Object other$packVersion = other.packVersion;
        return this$packVersion != null ? this$packVersion.equals(other$packVersion) : other$packVersion == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.chunkIndex;
        Object $packId = this.packId;
        int result2 = (result * 59) + ($packId == null ? 43 : $packId.hashCode());
        Object $packVersion = this.packVersion;
        return (result2 * 59) + ($packVersion != null ? $packVersion.hashCode() : 43);
    }

    public String toString() {
        return "ResourcePackChunkRequestPacket(packId=" + this.packId + ", packVersion=" + this.packVersion + ", chunkIndex=" + this.chunkIndex + ")";
    }

    public UUID getPackId() {
        return this.packId;
    }

    public String getPackVersion() {
        return this.packVersion;
    }

    public int getChunkIndex() {
        return this.chunkIndex;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.RESOURCE_PACK_CHUNK_REQUEST;
    }
}
