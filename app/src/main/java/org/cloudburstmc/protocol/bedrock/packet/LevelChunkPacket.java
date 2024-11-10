package org.cloudburstmc.protocol.bedrock.packet;

import io.netty.buffer.ByteBuf;
import io.netty.util.AbstractReferenceCounted;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class LevelChunkPacket extends AbstractReferenceCounted implements BedrockPacket {
    private final LongList blobIds = new LongArrayList();
    private boolean cachingEnabled;
    private int chunkX;
    private int chunkZ;
    private ByteBuf data;
    private int dimension;
    private boolean requestSubChunks;
    private int subChunkLimit;
    private int subChunksLength;

    public void setCachingEnabled(boolean cachingEnabled) {
        this.cachingEnabled = cachingEnabled;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
    }

    public void setData(ByteBuf data) {
        this.data = data;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void setRequestSubChunks(boolean requestSubChunks) {
        this.requestSubChunks = requestSubChunks;
    }

    public void setSubChunkLimit(int subChunkLimit) {
        this.subChunkLimit = subChunkLimit;
    }

    public void setSubChunksLength(int subChunksLength) {
        this.subChunksLength = subChunksLength;
    }

    public String toString() {
        return "LevelChunkPacket(chunkX=" + this.chunkX + ", chunkZ=" + this.chunkZ + ", subChunksLength=" + this.subChunksLength + ", cachingEnabled=" + this.cachingEnabled + ", requestSubChunks=" + this.requestSubChunks + ", subChunkLimit=" + this.subChunkLimit + ", blobIds=" + this.blobIds + ", dimension=" + this.dimension + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof LevelChunkPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LevelChunkPacket)) {
            return false;
        }
        LevelChunkPacket other = (LevelChunkPacket) o;
        if (!other.canEqual(this) || this.chunkX != other.chunkX || this.chunkZ != other.chunkZ || this.subChunksLength != other.subChunksLength || this.cachingEnabled != other.cachingEnabled || this.requestSubChunks != other.requestSubChunks || this.subChunkLimit != other.subChunkLimit || this.dimension != other.dimension) {
            return false;
        }
        Object this$blobIds = this.blobIds;
        Object other$blobIds = other.blobIds;
        if (this$blobIds != null ? !this$blobIds.equals(other$blobIds) : other$blobIds != null) {
            return false;
        }
        Object this$data = this.data;
        Object other$data = other.data;
        return this$data != null ? this$data.equals(other$data) : other$data == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.chunkX;
        int result2 = (((((((((((result * 59) + this.chunkZ) * 59) + this.subChunksLength) * 59) + (this.cachingEnabled ? 79 : 97)) * 59) + (this.requestSubChunks ? 79 : 97)) * 59) + this.subChunkLimit) * 59) + this.dimension;
        Object $blobIds = this.blobIds;
        int result3 = (result2 * 59) + ($blobIds == null ? 43 : $blobIds.hashCode());
        Object $data = this.data;
        return (result3 * 59) + ($data != null ? $data.hashCode() : 43);
    }

    public int getChunkX() {
        return this.chunkX;
    }

    public int getChunkZ() {
        return this.chunkZ;
    }

    public int getSubChunksLength() {
        return this.subChunksLength;
    }

    public boolean isCachingEnabled() {
        return this.cachingEnabled;
    }

    public boolean isRequestSubChunks() {
        return this.requestSubChunks;
    }

    public int getSubChunkLimit() {
        return this.subChunkLimit;
    }

    public LongList getBlobIds() {
        return this.blobIds;
    }

    public ByteBuf getData() {
        return this.data;
    }

    public int getDimension() {
        return this.dimension;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.LEVEL_CHUNK;
    }

    @Override // io.netty.util.ReferenceCounted
    public LevelChunkPacket touch(Object hint) {
        this.data.touch(hint);
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        this.data.release();
    }
}
