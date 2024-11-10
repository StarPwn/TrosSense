package io.netty.buffer;

import io.netty.buffer.PoolArena;
import io.netty.util.Recycler;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectPool;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class PoolThreadCache {
    private static final int INTEGER_SIZE_MINUS_ONE = 31;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) PoolThreadCache.class);
    private int allocations;
    final PoolArena<ByteBuffer> directArena;
    private final FreeOnFinalize freeOnFinalize;
    private final int freeSweepAllocationThreshold;
    private final AtomicBoolean freed = new AtomicBoolean();
    final PoolArena<byte[]> heapArena;
    private final MemoryRegionCache<ByteBuffer>[] normalDirectCaches;
    private final MemoryRegionCache<byte[]>[] normalHeapCaches;
    private final MemoryRegionCache<ByteBuffer>[] smallSubPageDirectCaches;
    private final MemoryRegionCache<byte[]>[] smallSubPageHeapCaches;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PoolThreadCache(PoolArena<byte[]> poolArena, PoolArena<ByteBuffer> poolArena2, int i, int i2, int i3, int i4, boolean z) {
        ObjectUtil.checkPositiveOrZero(i3, "maxCachedBufferCapacity");
        this.freeSweepAllocationThreshold = i4;
        this.heapArena = poolArena;
        this.directArena = poolArena2;
        byte b = 0;
        if (poolArena2 != null) {
            this.smallSubPageDirectCaches = createSubPageCaches(i, poolArena2.sizeClass.nSubpages);
            this.normalDirectCaches = createNormalCaches(i2, i3, poolArena2);
            poolArena2.numThreadCaches.getAndIncrement();
        } else {
            this.smallSubPageDirectCaches = null;
            this.normalDirectCaches = null;
        }
        if (poolArena != null) {
            this.smallSubPageHeapCaches = createSubPageCaches(i, poolArena.sizeClass.nSubpages);
            this.normalHeapCaches = createNormalCaches(i2, i3, poolArena);
            poolArena.numThreadCaches.getAndIncrement();
        } else {
            this.smallSubPageHeapCaches = null;
            this.normalHeapCaches = null;
        }
        if ((this.smallSubPageDirectCaches != null || this.normalDirectCaches != null || this.smallSubPageHeapCaches != null || this.normalHeapCaches != null) && i4 < 1) {
            throw new IllegalArgumentException("freeSweepAllocationThreshold: " + i4 + " (expected: > 0)");
        }
        this.freeOnFinalize = z ? new FreeOnFinalize() : null;
    }

    private static <T> MemoryRegionCache<T>[] createSubPageCaches(int cacheSize, int numCaches) {
        if (cacheSize > 0 && numCaches > 0) {
            MemoryRegionCache<T>[] cache = new MemoryRegionCache[numCaches];
            for (int i = 0; i < cache.length; i++) {
                cache[i] = new SubPageMemoryRegionCache(cacheSize);
            }
            return cache;
        }
        return null;
    }

    private static <T> MemoryRegionCache<T>[] createNormalCaches(int cacheSize, int maxCachedBufferCapacity, PoolArena<T> area) {
        if (cacheSize > 0 && maxCachedBufferCapacity > 0) {
            int max = Math.min(area.sizeClass.chunkSize, maxCachedBufferCapacity);
            List<MemoryRegionCache<T>> cache = new ArrayList<>();
            for (int idx = area.sizeClass.nSubpages; idx < area.sizeClass.nSizes && area.sizeClass.sizeIdx2size(idx) <= max; idx++) {
                cache.add(new NormalMemoryRegionCache<>(cacheSize));
            }
            return (MemoryRegionCache[]) cache.toArray(new MemoryRegionCache[0]);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int log2(int val) {
        return 31 - Integer.numberOfLeadingZeros(val);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean allocateSmall(PoolArena<?> area, PooledByteBuf<?> buf, int reqCapacity, int sizeIdx) {
        return allocate(cacheForSmall(area, sizeIdx), buf, reqCapacity);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean allocateNormal(PoolArena<?> area, PooledByteBuf<?> buf, int reqCapacity, int sizeIdx) {
        return allocate(cacheForNormal(area, sizeIdx), buf, reqCapacity);
    }

    private boolean allocate(MemoryRegionCache<?> cache, PooledByteBuf buf, int reqCapacity) {
        if (cache == null) {
            return false;
        }
        boolean allocated = cache.allocate(buf, reqCapacity, this);
        int i = this.allocations + 1;
        this.allocations = i;
        if (i >= this.freeSweepAllocationThreshold) {
            this.allocations = 0;
            trim();
        }
        return allocated;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean add(PoolArena<?> area, PoolChunk chunk, ByteBuffer nioBuffer, long handle, int normCapacity, PoolArena.SizeClass sizeClass) {
        int sizeIdx = area.sizeClass.size2SizeIdx(normCapacity);
        MemoryRegionCache<?> cache = cache(area, sizeIdx, sizeClass);
        if (cache == null || this.freed.get()) {
            return false;
        }
        return cache.add(chunk, nioBuffer, handle, normCapacity);
    }

    private MemoryRegionCache<?> cache(PoolArena<?> area, int sizeIdx, PoolArena.SizeClass sizeClass) {
        switch (sizeClass) {
            case Normal:
                return cacheForNormal(area, sizeIdx);
            case Small:
                return cacheForSmall(area, sizeIdx);
            default:
                throw new Error();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void free(boolean finalizer) {
        if (this.freed.compareAndSet(false, true)) {
            int numFreed = free(this.smallSubPageDirectCaches, finalizer) + free(this.normalDirectCaches, finalizer) + free((MemoryRegionCache<?>[]) this.smallSubPageHeapCaches, finalizer) + free((MemoryRegionCache<?>[]) this.normalHeapCaches, finalizer);
            if (numFreed > 0 && logger.isDebugEnabled()) {
                logger.debug("Freed {} thread-local buffer(s) from thread: {}", Integer.valueOf(numFreed), Thread.currentThread().getName());
            }
            if (this.directArena != null) {
                this.directArena.numThreadCaches.getAndDecrement();
            }
            if (this.heapArena != null) {
                this.heapArena.numThreadCaches.getAndDecrement();
                return;
            }
            return;
        }
        checkCacheMayLeak(this.smallSubPageDirectCaches, "SmallSubPageDirectCaches");
        checkCacheMayLeak(this.normalDirectCaches, "NormalDirectCaches");
        checkCacheMayLeak(this.smallSubPageHeapCaches, "SmallSubPageHeapCaches");
        checkCacheMayLeak(this.normalHeapCaches, "NormalHeapCaches");
    }

    private static void checkCacheMayLeak(MemoryRegionCache<?>[] caches, String type) {
        for (MemoryRegionCache<?> cache : caches) {
            if (!((MemoryRegionCache) cache).queue.isEmpty()) {
                logger.debug("{} memory may leak.", type);
                return;
            }
        }
    }

    private static int free(MemoryRegionCache<?>[] caches, boolean finalizer) {
        if (caches == null) {
            return 0;
        }
        int numFreed = 0;
        for (MemoryRegionCache<?> c : caches) {
            numFreed += free(c, finalizer);
        }
        return numFreed;
    }

    private static int free(MemoryRegionCache<?> cache, boolean finalizer) {
        if (cache == null) {
            return 0;
        }
        return cache.free(finalizer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void trim() {
        trim(this.smallSubPageDirectCaches);
        trim(this.normalDirectCaches);
        trim((MemoryRegionCache<?>[]) this.smallSubPageHeapCaches);
        trim((MemoryRegionCache<?>[]) this.normalHeapCaches);
    }

    private static void trim(MemoryRegionCache<?>[] caches) {
        if (caches == null) {
            return;
        }
        for (MemoryRegionCache<?> c : caches) {
            trim(c);
        }
    }

    private static void trim(MemoryRegionCache<?> cache) {
        if (cache == null) {
            return;
        }
        cache.trim();
    }

    private MemoryRegionCache<?> cacheForSmall(PoolArena<?> area, int sizeIdx) {
        if (area.isDirect()) {
            return cache(this.smallSubPageDirectCaches, sizeIdx);
        }
        return cache(this.smallSubPageHeapCaches, sizeIdx);
    }

    private MemoryRegionCache<?> cacheForNormal(PoolArena<?> area, int sizeIdx) {
        int idx = sizeIdx - area.sizeClass.nSubpages;
        if (area.isDirect()) {
            return cache(this.normalDirectCaches, idx);
        }
        return cache(this.normalHeapCaches, idx);
    }

    private static <T> MemoryRegionCache<T> cache(MemoryRegionCache<T>[] cache, int sizeIdx) {
        if (cache == null || sizeIdx > cache.length - 1) {
            return null;
        }
        return cache[sizeIdx];
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class SubPageMemoryRegionCache<T> extends MemoryRegionCache<T> {
        SubPageMemoryRegionCache(int size) {
            super(size, PoolArena.SizeClass.Small);
        }

        @Override // io.netty.buffer.PoolThreadCache.MemoryRegionCache
        protected void initBuf(PoolChunk<T> chunk, ByteBuffer nioBuffer, long handle, PooledByteBuf<T> buf, int reqCapacity, PoolThreadCache threadCache) {
            chunk.initBufWithSubpage(buf, nioBuffer, handle, reqCapacity, threadCache);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class NormalMemoryRegionCache<T> extends MemoryRegionCache<T> {
        NormalMemoryRegionCache(int size) {
            super(size, PoolArena.SizeClass.Normal);
        }

        @Override // io.netty.buffer.PoolThreadCache.MemoryRegionCache
        protected void initBuf(PoolChunk<T> chunk, ByteBuffer nioBuffer, long handle, PooledByteBuf<T> buf, int reqCapacity, PoolThreadCache threadCache) {
            chunk.initBuf(buf, nioBuffer, handle, reqCapacity, threadCache);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static abstract class MemoryRegionCache<T> {
        private static final ObjectPool<Entry> RECYCLER = ObjectPool.newPool(new ObjectPool.ObjectCreator<Entry>() { // from class: io.netty.buffer.PoolThreadCache.MemoryRegionCache.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // io.netty.util.internal.ObjectPool.ObjectCreator
            public Entry newObject(ObjectPool.Handle<Entry> handle) {
                return new Entry(handle);
            }
        });
        private int allocations;
        private final Queue<Entry<T>> queue;
        private final int size;
        private final PoolArena.SizeClass sizeClass;

        protected abstract void initBuf(PoolChunk<T> poolChunk, ByteBuffer byteBuffer, long j, PooledByteBuf<T> pooledByteBuf, int i, PoolThreadCache poolThreadCache);

        MemoryRegionCache(int size, PoolArena.SizeClass sizeClass) {
            this.size = MathUtil.safeFindNextPositivePowerOfTwo(size);
            this.queue = PlatformDependent.newFixedMpscQueue(this.size);
            this.sizeClass = sizeClass;
        }

        public final boolean add(PoolChunk<T> chunk, ByteBuffer nioBuffer, long handle, int normCapacity) {
            Entry<T> entry = newEntry(chunk, nioBuffer, handle, normCapacity);
            boolean queued = this.queue.offer(entry);
            if (!queued) {
                entry.unguardedRecycle();
            }
            return queued;
        }

        public final boolean allocate(PooledByteBuf<T> buf, int reqCapacity, PoolThreadCache threadCache) {
            Entry<T> entry = this.queue.poll();
            if (entry == null) {
                return false;
            }
            initBuf(entry.chunk, entry.nioBuffer, entry.handle, buf, reqCapacity, threadCache);
            entry.unguardedRecycle();
            this.allocations++;
            return true;
        }

        public final int free(boolean finalizer) {
            return free(Integer.MAX_VALUE, finalizer);
        }

        private int free(int max, boolean finalizer) {
            int numFreed = 0;
            while (numFreed < max) {
                Entry<T> entry = this.queue.poll();
                if (entry != null) {
                    freeEntry(entry, finalizer);
                    numFreed++;
                } else {
                    return numFreed;
                }
            }
            return numFreed;
        }

        public final void trim() {
            int free = this.size - this.allocations;
            this.allocations = 0;
            if (free > 0) {
                free(free, false);
            }
        }

        private void freeEntry(Entry entry, boolean finalizer) {
            PoolChunk chunk = entry.chunk;
            long handle = entry.handle;
            ByteBuffer nioBuffer = entry.nioBuffer;
            int normCapacity = entry.normCapacity;
            if (!finalizer) {
                entry.recycle();
            }
            chunk.arena.freeChunk(chunk, handle, normCapacity, this.sizeClass, nioBuffer, finalizer);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes4.dex */
        public static final class Entry<T> {
            PoolChunk<T> chunk;
            long handle = -1;
            ByteBuffer nioBuffer;
            int normCapacity;
            final Recycler.EnhancedHandle<Entry<?>> recyclerHandle;

            Entry(ObjectPool.Handle<Entry<?>> recyclerHandle) {
                this.recyclerHandle = (Recycler.EnhancedHandle) recyclerHandle;
            }

            void recycle() {
                this.chunk = null;
                this.nioBuffer = null;
                this.handle = -1L;
                this.recyclerHandle.recycle(this);
            }

            void unguardedRecycle() {
                this.chunk = null;
                this.nioBuffer = null;
                this.handle = -1L;
                this.recyclerHandle.unguardedRecycle(this);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        private static Entry newEntry(PoolChunk<?> poolChunk, ByteBuffer nioBuffer, long handle, int normCapacity) {
            Entry entry = RECYCLER.get();
            entry.chunk = poolChunk;
            entry.nioBuffer = nioBuffer;
            entry.handle = handle;
            entry.normCapacity = normCapacity;
            return entry;
        }
    }

    /* loaded from: classes4.dex */
    private static final class FreeOnFinalize {
        private final PoolThreadCache cache;

        private FreeOnFinalize(PoolThreadCache cache) {
            this.cache = cache;
        }

        protected void finalize() throws Throwable {
            try {
                super.finalize();
            } finally {
                this.cache.free(true);
            }
        }
    }
}
