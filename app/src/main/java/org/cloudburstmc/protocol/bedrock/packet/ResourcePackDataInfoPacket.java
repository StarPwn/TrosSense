package org.cloudburstmc.protocol.bedrock.packet;

import java.util.Arrays;
import java.util.UUID;
import org.cloudburstmc.protocol.bedrock.data.ResourcePackType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ResourcePackDataInfoPacket implements BedrockPacket {
    private long chunkCount;
    private long compressedPackSize;
    private byte[] hash;
    private long maxChunkSize;
    private UUID packId;
    private String packVersion;
    private boolean premium;
    private ResourcePackType type;

    public void setChunkCount(long chunkCount) {
        this.chunkCount = chunkCount;
    }

    public void setCompressedPackSize(long compressedPackSize) {
        this.compressedPackSize = compressedPackSize;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public void setMaxChunkSize(long maxChunkSize) {
        this.maxChunkSize = maxChunkSize;
    }

    public void setPackId(UUID packId) {
        this.packId = packId;
    }

    public void setPackVersion(String packVersion) {
        this.packVersion = packVersion;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public void setType(ResourcePackType type) {
        this.type = type;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ResourcePackDataInfoPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ResourcePackDataInfoPacket)) {
            return false;
        }
        ResourcePackDataInfoPacket other = (ResourcePackDataInfoPacket) o;
        if (!other.canEqual(this) || this.maxChunkSize != other.maxChunkSize || this.chunkCount != other.chunkCount || this.compressedPackSize != other.compressedPackSize || this.premium != other.premium) {
            return false;
        }
        Object this$packId = this.packId;
        Object other$packId = other.packId;
        if (this$packId != null ? !this$packId.equals(other$packId) : other$packId != null) {
            return false;
        }
        Object this$packVersion = this.packVersion;
        Object other$packVersion = other.packVersion;
        if (this$packVersion != null ? !this$packVersion.equals(other$packVersion) : other$packVersion != null) {
            return false;
        }
        if (!Arrays.equals(this.hash, other.hash)) {
            return false;
        }
        Object this$type = this.type;
        Object other$type = other.type;
        return this$type != null ? this$type.equals(other$type) : other$type == null;
    }

    public int hashCode() {
        long $maxChunkSize = this.maxChunkSize;
        int result = (1 * 59) + ((int) (($maxChunkSize >>> 32) ^ $maxChunkSize));
        long $chunkCount = this.chunkCount;
        long $compressedPackSize = this.compressedPackSize;
        int result2 = ((((result * 59) + ((int) (($chunkCount >>> 32) ^ $chunkCount))) * 59) + ((int) (($compressedPackSize >>> 32) ^ $compressedPackSize))) * 59;
        int i = this.premium ? 79 : 97;
        Object $packId = this.packId;
        int result3 = ((result2 + i) * 59) + ($packId == null ? 43 : $packId.hashCode());
        Object $packVersion = this.packVersion;
        int result4 = (((result3 * 59) + ($packVersion == null ? 43 : $packVersion.hashCode())) * 59) + Arrays.hashCode(this.hash);
        Object $type = this.type;
        return (result4 * 59) + ($type != null ? $type.hashCode() : 43);
    }

    public String toString() {
        return "ResourcePackDataInfoPacket(packId=" + this.packId + ", packVersion=" + this.packVersion + ", maxChunkSize=" + this.maxChunkSize + ", chunkCount=" + this.chunkCount + ", compressedPackSize=" + this.compressedPackSize + ", hash=" + Arrays.toString(this.hash) + ", premium=" + this.premium + ", type=" + this.type + ")";
    }

    public UUID getPackId() {
        return this.packId;
    }

    public String getPackVersion() {
        return this.packVersion;
    }

    public long getMaxChunkSize() {
        return this.maxChunkSize;
    }

    public long getChunkCount() {
        return this.chunkCount;
    }

    public long getCompressedPackSize() {
        return this.compressedPackSize;
    }

    public byte[] getHash() {
        return this.hash;
    }

    public boolean isPremium() {
        return this.premium;
    }

    public ResourcePackType getType() {
        return this.type;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.RESOURCE_PACK_DATA_INFO;
    }
}
