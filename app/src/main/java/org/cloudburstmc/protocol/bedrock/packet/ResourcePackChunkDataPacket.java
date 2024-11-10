package org.cloudburstmc.protocol.bedrock.packet;

import io.netty.buffer.ByteBuf;
import io.netty.util.AbstractReferenceCounted;
import java.util.UUID;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ResourcePackChunkDataPacket extends AbstractReferenceCounted implements BedrockPacket {
    private int chunkIndex;
    private ByteBuf data;
    private UUID packId;
    private String packVersion;
    private long progress;

    public void setChunkIndex(int chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    public void setData(ByteBuf data) {
        this.data = data;
    }

    public void setPackId(UUID packId) {
        this.packId = packId;
    }

    public void setPackVersion(String packVersion) {
        this.packVersion = packVersion;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ResourcePackChunkDataPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ResourcePackChunkDataPacket)) {
            return false;
        }
        ResourcePackChunkDataPacket other = (ResourcePackChunkDataPacket) o;
        if (!other.canEqual(this) || this.chunkIndex != other.chunkIndex || this.progress != other.progress) {
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
        Object this$data = this.data;
        Object other$data = other.data;
        return this$data != null ? this$data.equals(other$data) : other$data == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.chunkIndex;
        long $progress = this.progress;
        int result2 = (result * 59) + ((int) (($progress >>> 32) ^ $progress));
        Object $packId = this.packId;
        int result3 = (result2 * 59) + ($packId == null ? 43 : $packId.hashCode());
        Object $packVersion = this.packVersion;
        int result4 = (result3 * 59) + ($packVersion == null ? 43 : $packVersion.hashCode());
        Object $data = this.data;
        return (result4 * 59) + ($data != null ? $data.hashCode() : 43);
    }

    public String toString() {
        return "ResourcePackChunkDataPacket(packId=" + this.packId + ", packVersion=" + this.packVersion + ", chunkIndex=" + this.chunkIndex + ", progress=" + this.progress + ")";
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

    public long getProgress() {
        return this.progress;
    }

    public ByteBuf getData() {
        return this.data;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.RESOURCE_PACK_CHUNK_DATA;
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        this.data.release();
    }

    @Override // io.netty.util.ReferenceCounted
    public ResourcePackChunkDataPacket touch(Object hint) {
        this.data.touch(hint);
        return this;
    }
}
