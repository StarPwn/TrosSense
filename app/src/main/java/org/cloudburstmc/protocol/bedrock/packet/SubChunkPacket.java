package org.cloudburstmc.protocol.bedrock.packet;

import io.netty.util.AbstractReferenceCounted;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.SubChunkData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SubChunkPacket extends AbstractReferenceCounted implements BedrockPacket {
    private boolean cacheEnabled;
    private Vector3i centerPosition;
    private int dimension;
    private List<SubChunkData> subChunks = new ObjectArrayList();

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public void setCenterPosition(Vector3i centerPosition) {
        this.centerPosition = centerPosition;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void setSubChunks(List<SubChunkData> subChunks) {
        this.subChunks = subChunks;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SubChunkPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SubChunkPacket)) {
            return false;
        }
        SubChunkPacket other = (SubChunkPacket) o;
        if (!other.canEqual(this) || this.dimension != other.dimension || this.cacheEnabled != other.cacheEnabled) {
            return false;
        }
        Object this$centerPosition = this.centerPosition;
        Object other$centerPosition = other.centerPosition;
        if (this$centerPosition != null ? !this$centerPosition.equals(other$centerPosition) : other$centerPosition != null) {
            return false;
        }
        Object this$subChunks = this.subChunks;
        Object other$subChunks = other.subChunks;
        return this$subChunks != null ? this$subChunks.equals(other$subChunks) : other$subChunks == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.dimension;
        int result2 = result * 59;
        int i = this.cacheEnabled ? 79 : 97;
        Object $centerPosition = this.centerPosition;
        int result3 = ((result2 + i) * 59) + ($centerPosition == null ? 43 : $centerPosition.hashCode());
        Object $subChunks = this.subChunks;
        return (result3 * 59) + ($subChunks != null ? $subChunks.hashCode() : 43);
    }

    public String toString() {
        return "SubChunkPacket(dimension=" + this.dimension + ", cacheEnabled=" + this.cacheEnabled + ", centerPosition=" + this.centerPosition + ", subChunks=" + this.subChunks + ")";
    }

    public int getDimension() {
        return this.dimension;
    }

    public boolean isCacheEnabled() {
        return this.cacheEnabled;
    }

    public Vector3i getCenterPosition() {
        return this.centerPosition;
    }

    public List<SubChunkData> getSubChunks() {
        return this.subChunks;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SUB_CHUNK;
    }

    @Override // io.netty.util.ReferenceCounted
    public SubChunkPacket touch(Object o) {
        this.subChunks.forEach(new Consumer() { // from class: org.cloudburstmc.protocol.bedrock.packet.SubChunkPacket$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SubChunkData) obj).touch();
            }
        });
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        this.subChunks.forEach(new Consumer() { // from class: org.cloudburstmc.protocol.bedrock.packet.SubChunkPacket$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SubChunkData) obj).release();
            }
        });
    }
}
