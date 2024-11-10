package io.netty.buffer;

import io.netty.util.internal.LongCounter;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public abstract class PoolArena<T> implements PoolArenaMetric {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final boolean HAS_UNSAFE = PlatformDependent.hasUnsafe();
    private long allocationsNormal;
    private final List<PoolChunkListMetric> chunkListMetrics;
    private long deallocationsNormal;
    private long deallocationsSmall;
    final PooledByteBufAllocator parent;
    private final PoolChunkList<T> q000;
    private final PoolChunkList<T> q025;
    private final PoolChunkList<T> q050;
    private final PoolChunkList<T> q075;
    private final PoolChunkList<T> q100;
    private final PoolChunkList<T> qInit;
    final SizeClasses sizeClass;
    final PoolSubpage<T>[] smallSubpagePools;
    private final LongCounter allocationsSmall = PlatformDependent.newLongCounter();
    private final LongCounter allocationsHuge = PlatformDependent.newLongCounter();
    private final LongCounter activeBytesHuge = PlatformDependent.newLongCounter();
    private final LongCounter deallocationsHuge = PlatformDependent.newLongCounter();
    final AtomicInteger numThreadCaches = new AtomicInteger();
    private final ReentrantLock lock = new ReentrantLock();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public enum SizeClass {
        Small,
        Normal
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void destroyChunk(PoolChunk<T> poolChunk);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean isDirect();

    protected abstract void memoryCopy(T t, int i, PooledByteBuf<T> pooledByteBuf, int i2);

    protected abstract PooledByteBuf<T> newByteBuf(int i);

    protected abstract PoolChunk<T> newChunk(int i, int i2, int i3, int i4);

    protected abstract PoolChunk<T> newUnpooledChunk(int i);

    protected PoolArena(PooledByteBufAllocator parent, SizeClasses sizeClass) {
        if (sizeClass == null) {
            throw new AssertionError();
        }
        this.parent = parent;
        this.sizeClass = sizeClass;
        this.smallSubpagePools = newSubpagePoolArray(sizeClass.nSubpages);
        for (int i = 0; i < this.smallSubpagePools.length; i++) {
            this.smallSubpagePools[i] = newSubpagePoolHead(i);
        }
        this.q100 = new PoolChunkList<>(this, null, 100, Integer.MAX_VALUE, sizeClass.chunkSize);
        this.q075 = new PoolChunkList<>(this, this.q100, 75, 100, sizeClass.chunkSize);
        this.q050 = new PoolChunkList<>(this, this.q075, 50, 100, sizeClass.chunkSize);
        this.q025 = new PoolChunkList<>(this, this.q050, 25, 75, sizeClass.chunkSize);
        this.q000 = new PoolChunkList<>(this, this.q025, 1, 50, sizeClass.chunkSize);
        this.qInit = new PoolChunkList<>(this, this.q000, Integer.MIN_VALUE, 25, sizeClass.chunkSize);
        this.q100.prevList(this.q075);
        this.q075.prevList(this.q050);
        this.q050.prevList(this.q025);
        this.q025.prevList(this.q000);
        this.q000.prevList(null);
        this.qInit.prevList(this.qInit);
        List<PoolChunkListMetric> metrics = new ArrayList<>(6);
        metrics.add(this.qInit);
        metrics.add(this.q000);
        metrics.add(this.q025);
        metrics.add(this.q050);
        metrics.add(this.q075);
        metrics.add(this.q100);
        this.chunkListMetrics = Collections.unmodifiableList(metrics);
    }

    private PoolSubpage<T> newSubpagePoolHead(int index) {
        PoolSubpage<T> head = new PoolSubpage<>(index);
        head.prev = head;
        head.next = head;
        return head;
    }

    private PoolSubpage<T>[] newSubpagePoolArray(int size) {
        return new PoolSubpage[size];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PooledByteBuf<T> allocate(PoolThreadCache cache, int reqCapacity, int maxCapacity) {
        PooledByteBuf<T> buf = newByteBuf(maxCapacity);
        allocate(cache, buf, reqCapacity);
        return buf;
    }

    private void allocate(PoolThreadCache cache, PooledByteBuf<T> buf, int reqCapacity) {
        int sizeIdx = this.sizeClass.size2SizeIdx(reqCapacity);
        if (sizeIdx <= this.sizeClass.smallMaxSizeIdx) {
            tcacheAllocateSmall(cache, buf, reqCapacity, sizeIdx);
        } else if (sizeIdx < this.sizeClass.nSizes) {
            tcacheAllocateNormal(cache, buf, reqCapacity, sizeIdx);
        } else {
            int normCapacity = this.sizeClass.directMemoryCacheAlignment > 0 ? this.sizeClass.normalizeSize(reqCapacity) : reqCapacity;
            allocateHuge(buf, normCapacity);
        }
    }

    private void tcacheAllocateSmall(PoolThreadCache cache, PooledByteBuf<T> buf, int reqCapacity, int sizeIdx) {
        if (cache.allocateSmall(this, buf, reqCapacity, sizeIdx)) {
            return;
        }
        PoolSubpage<T> head = this.smallSubpagePools[sizeIdx];
        head.lock();
        try {
            PoolSubpage<T> s = head.next;
            boolean needsNormalAllocation = s == head;
            if (!needsNormalAllocation) {
                if (!s.doNotDestroy || s.elemSize != this.sizeClass.sizeIdx2size(sizeIdx)) {
                    throw new AssertionError("doNotDestroy=" + s.doNotDestroy + ", elemSize=" + s.elemSize + ", sizeIdx=" + sizeIdx);
                }
                long handle = s.allocate();
                if (handle < 0) {
                    throw new AssertionError();
                }
                s.chunk.initBufWithSubpage(buf, null, handle, reqCapacity, cache);
            }
            if (needsNormalAllocation) {
                lock();
                try {
                    allocateNormal(buf, reqCapacity, sizeIdx, cache);
                } finally {
                    unlock();
                }
            }
            incSmallAllocation();
        } finally {
            head.unlock();
        }
    }

    private void tcacheAllocateNormal(PoolThreadCache cache, PooledByteBuf<T> buf, int reqCapacity, int sizeIdx) {
        if (cache.allocateNormal(this, buf, reqCapacity, sizeIdx)) {
            return;
        }
        lock();
        try {
            allocateNormal(buf, reqCapacity, sizeIdx, cache);
            this.allocationsNormal++;
        } finally {
            unlock();
        }
    }

    private void allocateNormal(PooledByteBuf<T> buf, int reqCapacity, int sizeIdx, PoolThreadCache threadCache) {
        if (!this.lock.isHeldByCurrentThread()) {
            throw new AssertionError();
        }
        if (this.q050.allocate(buf, reqCapacity, sizeIdx, threadCache) || this.q025.allocate(buf, reqCapacity, sizeIdx, threadCache) || this.q000.allocate(buf, reqCapacity, sizeIdx, threadCache) || this.qInit.allocate(buf, reqCapacity, sizeIdx, threadCache) || this.q075.allocate(buf, reqCapacity, sizeIdx, threadCache)) {
            return;
        }
        PoolChunk<T> c = newChunk(this.sizeClass.pageSize, this.sizeClass.nPSizes, this.sizeClass.pageShifts, this.sizeClass.chunkSize);
        boolean success = c.allocate(buf, reqCapacity, sizeIdx, threadCache);
        if (!success) {
            throw new AssertionError();
        }
        this.qInit.add(c);
    }

    private void incSmallAllocation() {
        this.allocationsSmall.increment();
    }

    private void allocateHuge(PooledByteBuf<T> buf, int reqCapacity) {
        PoolChunk<T> chunk = newUnpooledChunk(reqCapacity);
        this.activeBytesHuge.add(chunk.chunkSize());
        buf.initUnpooled(chunk, reqCapacity);
        this.allocationsHuge.increment();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void free(PoolChunk<T> chunk, ByteBuffer nioBuffer, long handle, int normCapacity, PoolThreadCache cache) {
        chunk.decrementPinnedMemory(normCapacity);
        if (chunk.unpooled) {
            int size = chunk.chunkSize();
            destroyChunk(chunk);
            this.activeBytesHuge.add(-size);
            this.deallocationsHuge.increment();
            return;
        }
        SizeClass sizeClass = sizeClass(handle);
        if (cache != null && cache.add(this, chunk, nioBuffer, handle, normCapacity, sizeClass)) {
            return;
        }
        freeChunk(chunk, handle, normCapacity, sizeClass, nioBuffer, false);
    }

    private static SizeClass sizeClass(long handle) {
        return PoolChunk.isSubpage(handle) ? SizeClass.Small : SizeClass.Normal;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void freeChunk(PoolChunk<T> chunk, long handle, int normCapacity, SizeClass sizeClass, ByteBuffer nioBuffer, boolean finalizer) {
        lock();
        if (!finalizer) {
            try {
                switch (sizeClass) {
                    case Normal:
                        this.deallocationsNormal++;
                        break;
                    case Small:
                        this.deallocationsSmall++;
                        break;
                    default:
                        throw new Error();
                }
            } finally {
                unlock();
            }
        }
        boolean destroyChunk = !chunk.parent.free(chunk, handle, normCapacity, nioBuffer);
        if (destroyChunk) {
            destroyChunk(chunk);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reallocate(PooledByteBuf<T> buf, int newCapacity) {
        int bytesToCopy;
        if (newCapacity < 0 || newCapacity > buf.maxCapacity()) {
            throw new AssertionError();
        }
        synchronized (buf) {
            int oldCapacity = buf.length;
            if (oldCapacity == newCapacity) {
                return;
            }
            PoolChunk<T> oldChunk = buf.chunk;
            ByteBuffer oldNioBuffer = buf.tmpNioBuf;
            long oldHandle = buf.handle;
            T oldMemory = buf.memory;
            int oldOffset = buf.offset;
            int oldMaxLength = buf.maxLength;
            PoolThreadCache oldCache = buf.cache;
            allocate(this.parent.threadCache(), buf, newCapacity);
            if (newCapacity > oldCapacity) {
                bytesToCopy = oldCapacity;
            } else {
                buf.trimIndicesToCapacity(newCapacity);
                bytesToCopy = newCapacity;
            }
            memoryCopy(oldMemory, oldOffset, buf, bytesToCopy);
            free(oldChunk, oldNioBuffer, oldHandle, oldMaxLength, oldCache);
        }
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public int numThreadCaches() {
        return this.numThreadCaches.get();
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public int numTinySubpages() {
        return 0;
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public int numSmallSubpages() {
        return this.smallSubpagePools.length;
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public int numChunkLists() {
        return this.chunkListMetrics.size();
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public List<PoolSubpageMetric> tinySubpages() {
        return Collections.emptyList();
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public List<PoolSubpageMetric> smallSubpages() {
        return subPageMetricList(this.smallSubpagePools);
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public List<PoolChunkListMetric> chunkLists() {
        return this.chunkListMetrics;
    }

    private static List<PoolSubpageMetric> subPageMetricList(PoolSubpage<?>[] pages) {
        List<PoolSubpageMetric> metrics = new ArrayList<>();
        for (PoolSubpage<?> head : pages) {
            if (head.next != head) {
                PoolSubpage poolSubpage = head.next;
                do {
                    metrics.add(poolSubpage);
                    poolSubpage = poolSubpage.next;
                } while (poolSubpage != head);
            }
        }
        return metrics;
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numAllocations() {
        lock();
        try {
            long allocsNormal = this.allocationsNormal;
            unlock();
            return this.allocationsSmall.value() + allocsNormal + this.allocationsHuge.value();
        } catch (Throwable th) {
            unlock();
            throw th;
        }
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numTinyAllocations() {
        return 0L;
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numSmallAllocations() {
        return this.allocationsSmall.value();
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numNormalAllocations() {
        lock();
        try {
            return this.allocationsNormal;
        } finally {
            unlock();
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // io.netty.buffer.PoolArenaMetric
    public long numDeallocations() {
        lock();
        try {
            long deallocs = this.deallocationsSmall + this.deallocationsNormal;
            unlock();
            return this.deallocationsHuge.value() + deallocs;
        } catch (Throwable th) {
            unlock();
            throw th;
        }
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numTinyDeallocations() {
        return 0L;
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numSmallDeallocations() {
        lock();
        try {
            return this.deallocationsSmall;
        } finally {
            unlock();
        }
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numNormalDeallocations() {
        lock();
        try {
            return this.deallocationsNormal;
        } finally {
            unlock();
        }
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numHugeAllocations() {
        return this.allocationsHuge.value();
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numHugeDeallocations() {
        return this.deallocationsHuge.value();
    }

    /* JADX WARN: Finally extract failed */
    @Override // io.netty.buffer.PoolArenaMetric
    public long numActiveAllocations() {
        long val = (this.allocationsSmall.value() + this.allocationsHuge.value()) - this.deallocationsHuge.value();
        lock();
        try {
            long val2 = val + (this.allocationsNormal - (this.deallocationsSmall + this.deallocationsNormal));
            unlock();
            return Math.max(val2, 0L);
        } catch (Throwable th) {
            unlock();
            throw th;
        }
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numActiveTinyAllocations() {
        return 0L;
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numActiveSmallAllocations() {
        return Math.max(numSmallAllocations() - numSmallDeallocations(), 0L);
    }

    /* JADX WARN: Finally extract failed */
    @Override // io.netty.buffer.PoolArenaMetric
    public long numActiveNormalAllocations() {
        lock();
        try {
            long val = this.allocationsNormal - this.deallocationsNormal;
            unlock();
            return Math.max(val, 0L);
        } catch (Throwable th) {
            unlock();
            throw th;
        }
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numActiveHugeAllocations() {
        return Math.max(numHugeAllocations() - numHugeDeallocations(), 0L);
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numActiveBytes() {
        long val = this.activeBytesHuge.value();
        lock();
        for (int i = 0; i < this.chunkListMetrics.size(); i++) {
            try {
                for (PoolChunkMetric m : this.chunkListMetrics.get(i)) {
                    val += m.chunkSize();
                }
            } catch (Throwable th) {
                unlock();
                throw th;
            }
        }
        unlock();
        return Math.max(0L, val);
    }

    public long numPinnedBytes() {
        long val = this.activeBytesHuge.value();
        for (int i = 0; i < this.chunkListMetrics.size(); i++) {
            for (PoolChunkMetric m : this.chunkListMetrics.get(i)) {
                val += ((PoolChunk) m).pinnedBytes();
            }
        }
        return Math.max(0L, val);
    }

    public String toString() {
        lock();
        try {
            StringBuilder buf = new StringBuilder().append("Chunk(s) at 0~25%:").append(StringUtil.NEWLINE).append(this.qInit).append(StringUtil.NEWLINE).append("Chunk(s) at 0~50%:").append(StringUtil.NEWLINE).append(this.q000).append(StringUtil.NEWLINE).append("Chunk(s) at 25~75%:").append(StringUtil.NEWLINE).append(this.q025).append(StringUtil.NEWLINE).append("Chunk(s) at 50~100%:").append(StringUtil.NEWLINE).append(this.q050).append(StringUtil.NEWLINE).append("Chunk(s) at 75~100%:").append(StringUtil.NEWLINE).append(this.q075).append(StringUtil.NEWLINE).append("Chunk(s) at 100%:").append(StringUtil.NEWLINE).append(this.q100).append(StringUtil.NEWLINE).append("small subpages:");
            appendPoolSubPages(buf, this.smallSubpagePools);
            buf.append(StringUtil.NEWLINE);
            return buf.toString();
        } finally {
            unlock();
        }
    }

    private static void appendPoolSubPages(StringBuilder buf, PoolSubpage<?>[] subpages) {
        for (int i = 0; i < subpages.length; i++) {
            PoolSubpage<?> head = subpages[i];
            if (head.next != head && head.next != null) {
                buf.append(StringUtil.NEWLINE).append(i).append(": ");
                PoolSubpage poolSubpage = head.next;
                while (poolSubpage != null) {
                    buf.append(poolSubpage);
                    poolSubpage = poolSubpage.next;
                    if (poolSubpage == head) {
                        break;
                    }
                }
            }
        }
    }

    protected final void finalize() throws Throwable {
        try {
            super.finalize();
            destroyPoolSubPages(this.smallSubpagePools);
            destroyPoolChunkLists(this.qInit, this.q000, this.q025, this.q050, this.q075, this.q100);
        } catch (Throwable th) {
            destroyPoolSubPages(this.smallSubpagePools);
            destroyPoolChunkLists(this.qInit, this.q000, this.q025, this.q050, this.q075, this.q100);
            throw th;
        }
    }

    private static void destroyPoolSubPages(PoolSubpage<?>[] pages) {
        for (PoolSubpage<?> page : pages) {
            page.destroy();
        }
    }

    private void destroyPoolChunkLists(PoolChunkList<T>... chunkLists) {
        for (PoolChunkList<T> chunkList : chunkLists) {
            chunkList.destroy(this);
        }
    }

    /* loaded from: classes4.dex */
    static final class HeapArena extends PoolArena<byte[]> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public HeapArena(PooledByteBufAllocator parent, SizeClasses sizeClass) {
            super(parent, sizeClass);
        }

        private static byte[] newByteArray(int size) {
            return PlatformDependent.allocateUninitializedArray(size);
        }

        @Override // io.netty.buffer.PoolArena
        boolean isDirect() {
            return false;
        }

        @Override // io.netty.buffer.PoolArena
        protected PoolChunk<byte[]> newChunk(int pageSize, int maxPageIdx, int pageShifts, int chunkSize) {
            return new PoolChunk<>(this, null, newByteArray(chunkSize), pageSize, pageShifts, chunkSize, maxPageIdx);
        }

        @Override // io.netty.buffer.PoolArena
        protected PoolChunk<byte[]> newUnpooledChunk(int capacity) {
            return new PoolChunk<>(this, null, newByteArray(capacity), capacity);
        }

        @Override // io.netty.buffer.PoolArena
        protected void destroyChunk(PoolChunk<byte[]> chunk) {
        }

        @Override // io.netty.buffer.PoolArena
        protected PooledByteBuf<byte[]> newByteBuf(int maxCapacity) {
            return PoolArena.HAS_UNSAFE ? PooledUnsafeHeapByteBuf.newUnsafeInstance(maxCapacity) : PooledHeapByteBuf.newInstance(maxCapacity);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.buffer.PoolArena
        public void memoryCopy(byte[] src, int srcOffset, PooledByteBuf<byte[]> dst, int length) {
            if (length == 0) {
                return;
            }
            System.arraycopy(src, srcOffset, dst.memory, dst.offset, length);
        }
    }

    /* loaded from: classes4.dex */
    static final class DirectArena extends PoolArena<ByteBuffer> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public DirectArena(PooledByteBufAllocator parent, SizeClasses sizeClass) {
            super(parent, sizeClass);
        }

        @Override // io.netty.buffer.PoolArena
        boolean isDirect() {
            return true;
        }

        @Override // io.netty.buffer.PoolArena
        protected PoolChunk<ByteBuffer> newChunk(int pageSize, int maxPageIdx, int pageShifts, int chunkSize) {
            if (this.sizeClass.directMemoryCacheAlignment == 0) {
                ByteBuffer memory = allocateDirect(chunkSize);
                return new PoolChunk<>(this, memory, memory, pageSize, pageShifts, chunkSize, maxPageIdx);
            }
            ByteBuffer base = allocateDirect(this.sizeClass.directMemoryCacheAlignment + chunkSize);
            return new PoolChunk<>(this, base, PlatformDependent.alignDirectBuffer(base, this.sizeClass.directMemoryCacheAlignment), pageSize, pageShifts, chunkSize, maxPageIdx);
        }

        @Override // io.netty.buffer.PoolArena
        protected PoolChunk<ByteBuffer> newUnpooledChunk(int capacity) {
            if (this.sizeClass.directMemoryCacheAlignment == 0) {
                ByteBuffer memory = allocateDirect(capacity);
                return new PoolChunk<>(this, memory, memory, capacity);
            }
            ByteBuffer base = allocateDirect(this.sizeClass.directMemoryCacheAlignment + capacity);
            return new PoolChunk<>(this, base, PlatformDependent.alignDirectBuffer(base, this.sizeClass.directMemoryCacheAlignment), capacity);
        }

        private static ByteBuffer allocateDirect(int capacity) {
            return PlatformDependent.useDirectBufferNoCleaner() ? PlatformDependent.allocateDirectNoCleaner(capacity) : ByteBuffer.allocateDirect(capacity);
        }

        @Override // io.netty.buffer.PoolArena
        protected void destroyChunk(PoolChunk<ByteBuffer> chunk) {
            if (PlatformDependent.useDirectBufferNoCleaner()) {
                PlatformDependent.freeDirectNoCleaner((ByteBuffer) chunk.base);
            } else {
                PlatformDependent.freeDirectBuffer((ByteBuffer) chunk.base);
            }
        }

        @Override // io.netty.buffer.PoolArena
        protected PooledByteBuf<ByteBuffer> newByteBuf(int maxCapacity) {
            if (PoolArena.HAS_UNSAFE) {
                return PooledUnsafeDirectByteBuf.newInstance(maxCapacity);
            }
            return PooledDirectByteBuf.newInstance(maxCapacity);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.buffer.PoolArena
        public void memoryCopy(ByteBuffer src, int srcOffset, PooledByteBuf<ByteBuffer> dstBuf, int length) {
            if (length != 0) {
                if (PoolArena.HAS_UNSAFE) {
                    PlatformDependent.copyMemory(PlatformDependent.directBufferAddress(src) + srcOffset, PlatformDependent.directBufferAddress(dstBuf.memory) + dstBuf.offset, length);
                    return;
                }
                ByteBuffer src2 = src.duplicate();
                ByteBuffer dst = dstBuf.internalNioBuffer();
                src2.position(srcOffset).limit(srcOffset + length);
                dst.position(dstBuf.offset);
                dst.put(src2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void lock() {
        this.lock.lock();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unlock() {
        this.lock.unlock();
    }

    @Override // io.netty.buffer.SizeClassesMetric
    public int sizeIdx2size(int sizeIdx) {
        return this.sizeClass.sizeIdx2size(sizeIdx);
    }

    @Override // io.netty.buffer.SizeClassesMetric
    public int sizeIdx2sizeCompute(int sizeIdx) {
        return this.sizeClass.sizeIdx2sizeCompute(sizeIdx);
    }

    @Override // io.netty.buffer.SizeClassesMetric
    public long pageIdx2size(int pageIdx) {
        return this.sizeClass.pageIdx2size(pageIdx);
    }

    @Override // io.netty.buffer.SizeClassesMetric
    public long pageIdx2sizeCompute(int pageIdx) {
        return this.sizeClass.pageIdx2sizeCompute(pageIdx);
    }

    @Override // io.netty.buffer.SizeClassesMetric
    public int size2SizeIdx(int size) {
        return this.sizeClass.size2SizeIdx(size);
    }

    @Override // io.netty.buffer.SizeClassesMetric
    public int pages2pageIdx(int pages) {
        return this.sizeClass.pages2pageIdx(pages);
    }

    @Override // io.netty.buffer.SizeClassesMetric
    public int pages2pageIdxFloor(int pages) {
        return this.sizeClass.pages2pageIdxFloor(pages);
    }

    @Override // io.netty.buffer.SizeClassesMetric
    public int normalizeSize(int size) {
        return this.sizeClass.normalizeSize(size);
    }
}
