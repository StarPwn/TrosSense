package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.BlockChangeEntry;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class UpdateSubChunkBlocksPacket implements BedrockPacket {
    private int chunkX;
    private int chunkY;
    private int chunkZ;
    private final List<BlockChangeEntry> standardBlocks = new ObjectArrayList();
    private final List<BlockChangeEntry> extraBlocks = new ObjectArrayList();

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public void setChunkY(int chunkY) {
        this.chunkY = chunkY;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UpdateSubChunkBlocksPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateSubChunkBlocksPacket)) {
            return false;
        }
        UpdateSubChunkBlocksPacket other = (UpdateSubChunkBlocksPacket) o;
        if (!other.canEqual(this) || this.chunkX != other.chunkX || this.chunkY != other.chunkY || this.chunkZ != other.chunkZ) {
            return false;
        }
        Object this$standardBlocks = this.standardBlocks;
        Object other$standardBlocks = other.standardBlocks;
        if (this$standardBlocks != null ? !this$standardBlocks.equals(other$standardBlocks) : other$standardBlocks != null) {
            return false;
        }
        Object this$extraBlocks = this.extraBlocks;
        Object other$extraBlocks = other.extraBlocks;
        return this$extraBlocks != null ? this$extraBlocks.equals(other$extraBlocks) : other$extraBlocks == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.chunkX;
        int result2 = (((result * 59) + this.chunkY) * 59) + this.chunkZ;
        Object $standardBlocks = this.standardBlocks;
        int result3 = (result2 * 59) + ($standardBlocks == null ? 43 : $standardBlocks.hashCode());
        Object $extraBlocks = this.extraBlocks;
        return (result3 * 59) + ($extraBlocks != null ? $extraBlocks.hashCode() : 43);
    }

    public String toString() {
        return "UpdateSubChunkBlocksPacket(chunkX=" + this.chunkX + ", chunkY=" + this.chunkY + ", chunkZ=" + this.chunkZ + ", standardBlocks=" + this.standardBlocks + ", extraBlocks=" + this.extraBlocks + ")";
    }

    public int getChunkX() {
        return this.chunkX;
    }

    public int getChunkY() {
        return this.chunkY;
    }

    public int getChunkZ() {
        return this.chunkZ;
    }

    public List<BlockChangeEntry> getStandardBlocks() {
        return this.standardBlocks;
    }

    public List<BlockChangeEntry> getExtraBlocks() {
        return this.extraBlocks;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.UPDATE_SUB_CHUNK_BLOCKS;
    }
}
