package org.cloudburstmc.protocol.bedrock.netty;

import io.netty.buffer.ByteBuf;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.ObjectPool;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.cloudburstmc.protocol.bedrock.data.CompressionAlgorithm;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.util.BatchFlag;

/* loaded from: classes5.dex */
public class BedrockBatchWrapper extends AbstractReferenceCounted {
    private static final ObjectPool<BedrockBatchWrapper> RECYCLER = ObjectPool.newPool(new ObjectPool.ObjectCreator() { // from class: org.cloudburstmc.protocol.bedrock.netty.BedrockBatchWrapper$$ExternalSyntheticLambda1
        @Override // io.netty.util.internal.ObjectPool.ObjectCreator
        public final Object newObject(ObjectPool.Handle handle) {
            return BedrockBatchWrapper.$r8$lambda$E5pnTTAWf2EyciElfRNtvqoTVpI(handle);
        }
    });
    private CompressionAlgorithm algorithm;
    private ByteBuf compressed;
    private final ObjectPool.Handle<BedrockBatchWrapper> handle;
    private boolean modified;
    private ByteBuf uncompressed;
    private List<BedrockPacketWrapper> packets = new ObjectArrayList();
    private Set<BatchFlag> flags = new ObjectOpenHashSet();

    public static /* synthetic */ BedrockBatchWrapper $r8$lambda$E5pnTTAWf2EyciElfRNtvqoTVpI(ObjectPool.Handle handle) {
        return new BedrockBatchWrapper(handle);
    }

    public void setAlgorithm(CompressionAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void setFlags(Set<BatchFlag> flags) {
        this.flags = flags;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public void setPackets(List<BedrockPacketWrapper> packets) {
        this.packets = packets;
    }

    public String toString() {
        return "BedrockBatchWrapper(handle=" + getHandle() + ", compressed=" + getCompressed() + ", algorithm=" + getAlgorithm() + ", uncompressed=" + getUncompressed() + ", packets=" + getPackets() + ", modified=" + isModified() + ", flags=" + getFlags() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof BedrockBatchWrapper;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BedrockBatchWrapper)) {
            return false;
        }
        BedrockBatchWrapper other = (BedrockBatchWrapper) o;
        if (!other.canEqual(this) || isModified() != other.isModified()) {
            return false;
        }
        Object this$handle = getHandle();
        Object other$handle = other.getHandle();
        if (this$handle != null ? !this$handle.equals(other$handle) : other$handle != null) {
            return false;
        }
        Object this$compressed = getCompressed();
        Object other$compressed = other.getCompressed();
        if (this$compressed != null ? !this$compressed.equals(other$compressed) : other$compressed != null) {
            return false;
        }
        Object this$algorithm = getAlgorithm();
        Object other$algorithm = other.getAlgorithm();
        if (this$algorithm != null ? !this$algorithm.equals(other$algorithm) : other$algorithm != null) {
            return false;
        }
        Object this$uncompressed = getUncompressed();
        Object other$uncompressed = other.getUncompressed();
        if (this$uncompressed != null ? !this$uncompressed.equals(other$uncompressed) : other$uncompressed != null) {
            return false;
        }
        Object this$packets = getPackets();
        Object other$packets = other.getPackets();
        if (this$packets != null ? !this$packets.equals(other$packets) : other$packets != null) {
            return false;
        }
        Object this$flags = getFlags();
        Object other$flags = other.getFlags();
        if (this$flags == null) {
            if (other$flags == null) {
                return true;
            }
        } else if (this$flags.equals(other$flags)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = (1 * 59) + (isModified() ? 79 : 97);
        Object $handle = getHandle();
        int result2 = (result * 59) + ($handle == null ? 43 : $handle.hashCode());
        Object $compressed = getCompressed();
        int result3 = (result2 * 59) + ($compressed == null ? 43 : $compressed.hashCode());
        Object $algorithm = getAlgorithm();
        int result4 = (result3 * 59) + ($algorithm == null ? 43 : $algorithm.hashCode());
        Object $uncompressed = getUncompressed();
        int result5 = (result4 * 59) + ($uncompressed == null ? 43 : $uncompressed.hashCode());
        Object $packets = getPackets();
        int result6 = (result5 * 59) + ($packets == null ? 43 : $packets.hashCode());
        Object $flags = getFlags();
        return (result6 * 59) + ($flags != null ? $flags.hashCode() : 43);
    }

    public ObjectPool.Handle<BedrockBatchWrapper> getHandle() {
        return this.handle;
    }

    public ByteBuf getCompressed() {
        return this.compressed;
    }

    public CompressionAlgorithm getAlgorithm() {
        return this.algorithm;
    }

    public ByteBuf getUncompressed() {
        return this.uncompressed;
    }

    public List<BedrockPacketWrapper> getPackets() {
        return this.packets;
    }

    public boolean isModified() {
        return this.modified;
    }

    public Set<BatchFlag> getFlags() {
        return this.flags;
    }

    private BedrockBatchWrapper(ObjectPool.Handle<BedrockBatchWrapper> handle) {
        this.handle = handle;
    }

    public static BedrockBatchWrapper newInstance() {
        return newInstance(null, null);
    }

    public static BedrockBatchWrapper newInstance(ByteBuf compressed, ByteBuf uncompressed) {
        BedrockBatchWrapper batch = RECYCLER.get();
        batch.compressed = compressed;
        batch.uncompressed = uncompressed;
        batch.setRefCnt(1);
        if (!batch.packets.isEmpty() || !batch.flags.isEmpty()) {
            throw new IllegalStateException("Batch was not deallocated");
        }
        return batch;
    }

    public static BedrockBatchWrapper create(int subClientId, BedrockPacket... packets) {
        BedrockBatchWrapper batch = newInstance();
        for (BedrockPacket packet : packets) {
            batch.getPackets().add(new BedrockPacketWrapper(0, subClientId, 0, packet, null));
        }
        return batch;
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        this.packets.forEach(new Consumer() { // from class: org.cloudburstmc.protocol.bedrock.netty.BedrockBatchWrapper$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ReferenceCountUtil.safeRelease((BedrockPacketWrapper) obj);
            }
        });
        ReferenceCountUtil.safeRelease(this.uncompressed);
        ReferenceCountUtil.safeRelease(this.compressed);
        this.compressed = null;
        this.uncompressed = null;
        this.packets.clear();
        this.modified = false;
        this.algorithm = null;
        this.flags.clear();
        this.handle.recycle(this);
    }

    public void addPacket(BedrockPacketWrapper wrapper) {
        this.packets.add(wrapper);
        modify();
    }

    public void modify() {
        this.modified = true;
    }

    public void setCompressed(ByteBuf compressed) {
        if (this.compressed != null) {
            this.compressed.release();
        }
        this.compressed = compressed;
        if (compressed == null) {
            this.algorithm = null;
        }
    }

    public void setCompressed(ByteBuf compressed, CompressionAlgorithm algorithm) {
        if (this.compressed != null) {
            this.compressed.release();
        }
        this.compressed = compressed;
        this.algorithm = algorithm;
    }

    public void setUncompressed(ByteBuf uncompressed) {
        if (this.uncompressed != null) {
            this.uncompressed.release();
        }
        this.uncompressed = uncompressed;
    }

    public void setFlag(BatchFlag flag) {
        this.flags.add(flag);
    }

    public boolean hasFlag(BatchFlag flag) {
        return this.flags.contains(flag);
    }

    public void unsetFlag(BatchFlag flag) {
        this.flags.remove(flag);
    }

    @Override // io.netty.util.ReferenceCounted
    public ReferenceCounted touch(Object o) {
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public BedrockBatchWrapper retain() {
        return (BedrockBatchWrapper) super.retain();
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public BedrockBatchWrapper retain(int increment) {
        return (BedrockBatchWrapper) super.retain(increment);
    }
}
