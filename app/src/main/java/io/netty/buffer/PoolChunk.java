package io.netty.buffer;

import io.netty.util.internal.LongCounter;
import io.netty.util.internal.PlatformDependent;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class PoolChunk<T> implements PoolChunkMetric {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int BITMAP_IDX_BIT_LENGTH = 32;
    private static final int INUSED_BIT_LENGTH = 1;
    static final int IS_SUBPAGE_SHIFT = 32;
    static final int IS_USED_SHIFT = 33;
    static final int RUN_OFFSET_SHIFT = 49;
    private static final int SIZE_BIT_LENGTH = 15;
    static final int SIZE_SHIFT = 34;
    private static final int SUBPAGE_BIT_LENGTH = 1;
    final PoolArena<T> arena;
    final Object base;
    private final Deque<ByteBuffer> cachedNioBuffers;
    private final int chunkSize;
    int freeBytes;
    final T memory;
    PoolChunk<T> next;
    private final int pageShifts;
    private final int pageSize;
    PoolChunkList<T> parent;
    private final LongCounter pinnedBytes;
    PoolChunk<T> prev;
    private final IntPriorityQueue[] runsAvail;
    private final ReentrantLock runsAvailLock;
    private final LongLongHashMap runsAvailMap;
    private final PoolSubpage<T>[] subpages;
    final boolean unpooled;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PoolChunk(PoolArena<T> arena, Object base, T memory, int pageSize, int pageShifts, int chunkSize, int maxPageIdx) {
        this.pinnedBytes = PlatformDependent.newLongCounter();
        this.unpooled = false;
        this.arena = arena;
        this.base = base;
        this.memory = memory;
        this.pageSize = pageSize;
        this.pageShifts = pageShifts;
        this.chunkSize = chunkSize;
        this.freeBytes = chunkSize;
        this.runsAvail = newRunsAvailqueueArray(maxPageIdx);
        this.runsAvailLock = new ReentrantLock();
        this.runsAvailMap = new LongLongHashMap(-1L);
        this.subpages = new PoolSubpage[chunkSize >> pageShifts];
        int pages = chunkSize >> pageShifts;
        long initHandle = pages << 34;
        insertAvailRun(0, pages, initHandle);
        this.cachedNioBuffers = new ArrayDeque(8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PoolChunk(PoolArena<T> arena, Object base, T memory, int size) {
        this.pinnedBytes = PlatformDependent.newLongCounter();
        this.unpooled = true;
        this.arena = arena;
        this.base = base;
        this.memory = memory;
        this.pageSize = 0;
        this.pageShifts = 0;
        this.runsAvailMap = null;
        this.runsAvail = null;
        this.runsAvailLock = null;
        this.subpages = null;
        this.chunkSize = size;
        this.cachedNioBuffers = null;
    }

    private static IntPriorityQueue[] newRunsAvailqueueArray(int size) {
        IntPriorityQueue[] queueArray = new IntPriorityQueue[size];
        for (int i = 0; i < queueArray.length; i++) {
            queueArray[i] = new IntPriorityQueue();
        }
        return queueArray;
    }

    private void insertAvailRun(int runOffset, int pages, long handle) {
        int pageIdxFloor = this.arena.sizeClass.pages2pageIdxFloor(pages);
        IntPriorityQueue queue = this.runsAvail[pageIdxFloor];
        if (!isRun(handle)) {
            throw new AssertionError();
        }
        queue.offer((int) (handle >> 32));
        insertAvailRun0(runOffset, handle);
        if (pages > 1) {
            insertAvailRun0(lastPage(runOffset, pages), handle);
        }
    }

    private void insertAvailRun0(int runOffset, long handle) {
        long pre = this.runsAvailMap.put(runOffset, handle);
        if (pre != -1) {
            throw new AssertionError();
        }
    }

    private void removeAvailRun(long handle) {
        int pageIdxFloor = this.arena.sizeClass.pages2pageIdxFloor(runPages(handle));
        this.runsAvail[pageIdxFloor].remove((int) (handle >> 32));
        removeAvailRun0(handle);
    }

    private void removeAvailRun0(long handle) {
        int runOffset = runOffset(handle);
        int pages = runPages(handle);
        this.runsAvailMap.remove(runOffset);
        if (pages > 1) {
            this.runsAvailMap.remove(lastPage(runOffset, pages));
        }
    }

    private static int lastPage(int runOffset, int pages) {
        return (runOffset + pages) - 1;
    }

    private long getAvailRunByOffset(int runOffset) {
        return this.runsAvailMap.get(runOffset);
    }

    @Override // io.netty.buffer.PoolChunkMetric
    public int usage() {
        int freeBytes;
        if (this.unpooled) {
            freeBytes = this.freeBytes;
        } else {
            this.runsAvailLock.lock();
            try {
                freeBytes = this.freeBytes;
            } finally {
                this.runsAvailLock.unlock();
            }
        }
        return usage(freeBytes);
    }

    private int usage(int freeBytes) {
        if (freeBytes == 0) {
            return 100;
        }
        int freePercentage = (int) ((freeBytes * 100) / this.chunkSize);
        if (freePercentage == 0) {
            return 99;
        }
        return 100 - freePercentage;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean allocate(PooledByteBuf<T> buf, int reqCapacity, int sizeIdx, PoolThreadCache cache) {
        long handle;
        if (sizeIdx <= this.arena.sizeClass.smallMaxSizeIdx) {
            PoolSubpage<T> head = this.arena.smallSubpagePools[sizeIdx];
            head.lock();
            try {
                PoolSubpage<T> nextSub = head.next;
                if (nextSub != head) {
                    if (!nextSub.doNotDestroy || nextSub.elemSize != this.arena.sizeClass.sizeIdx2size(sizeIdx)) {
                        throw new AssertionError("doNotDestroy=" + nextSub.doNotDestroy + ", elemSize=" + nextSub.elemSize + ", sizeIdx=" + sizeIdx);
                    }
                    long handle2 = nextSub.allocate();
                    if (handle2 < 0) {
                        throw new AssertionError();
                    }
                    if (!isSubpage(handle2)) {
                        throw new AssertionError();
                    }
                    nextSub.chunk.initBufWithSubpage(buf, null, handle2, reqCapacity, cache);
                    return true;
                }
                long handle3 = allocateSubpage(sizeIdx, head);
                if (handle3 < 0) {
                    return false;
                }
                if (!isSubpage(handle3)) {
                    throw new AssertionError();
                }
                head.unlock();
                handle = handle3;
            } finally {
                head.unlock();
            }
        } else {
            int runSize = this.arena.sizeClass.sizeIdx2size(sizeIdx);
            long handle4 = allocateRun(runSize);
            if (handle4 < 0) {
                return false;
            }
            if (isSubpage(handle4)) {
                throw new AssertionError();
            }
            handle = handle4;
        }
        ByteBuffer nioBuffer = this.cachedNioBuffers != null ? this.cachedNioBuffers.pollLast() : null;
        initBuf(buf, nioBuffer, handle, reqCapacity, cache);
        return true;
    }

    private long allocateRun(int runSize) {
        int pages = runSize >> this.pageShifts;
        int pageIdx = this.arena.sizeClass.pages2pageIdx(pages);
        this.runsAvailLock.lock();
        try {
            int queueIdx = runFirstBestFit(pageIdx);
            if (queueIdx == -1) {
                return -1L;
            }
            IntPriorityQueue queue = this.runsAvail[queueIdx];
            long handle = queue.poll();
            if (handle == -1) {
                throw new AssertionError();
            }
            long handle2 = handle << 32;
            if (isUsed(handle2)) {
                throw new AssertionError("invalid handle: " + handle2);
            }
            removeAvailRun0(handle2);
            long handle3 = splitLargeRun(handle2, pages);
            int pinnedSize = runSize(this.pageShifts, handle3);
            this.freeBytes -= pinnedSize;
            return handle3;
        } finally {
            this.runsAvailLock.unlock();
        }
    }

    private int calculateRunSize(int sizeIdx) {
        int nElements;
        int maxElements = 1 << (this.pageShifts - 4);
        int runSize = 0;
        int elemSize = this.arena.sizeClass.sizeIdx2size(sizeIdx);
        do {
            runSize += this.pageSize;
            nElements = runSize / elemSize;
            if (nElements >= maxElements) {
                break;
            }
        } while (runSize != nElements * elemSize);
        while (nElements > maxElements) {
            runSize -= this.pageSize;
            nElements = runSize / elemSize;
        }
        if (nElements <= 0) {
            throw new AssertionError();
        }
        if (runSize > this.chunkSize) {
            throw new AssertionError();
        }
        if (runSize < elemSize) {
            throw new AssertionError();
        }
        return runSize;
    }

    private int runFirstBestFit(int pageIdx) {
        if (this.freeBytes == this.chunkSize) {
            return this.arena.sizeClass.nPSizes - 1;
        }
        for (int i = pageIdx; i < this.arena.sizeClass.nPSizes; i++) {
            IntPriorityQueue queue = this.runsAvail[i];
            if (queue != null && !queue.isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private long splitLargeRun(long handle, int needPages) {
        if (needPages <= 0) {
            throw new AssertionError();
        }
        int totalPages = runPages(handle);
        if (needPages > totalPages) {
            throw new AssertionError();
        }
        int remPages = totalPages - needPages;
        if (remPages > 0) {
            int runOffset = runOffset(handle);
            int availOffset = runOffset + needPages;
            long availRun = toRunHandle(availOffset, remPages, 0);
            insertAvailRun(availOffset, remPages, availRun);
            return toRunHandle(runOffset, needPages, 1);
        }
        return handle | 8589934592L;
    }

    private long allocateSubpage(int sizeIdx, PoolSubpage<T> head) {
        int runSize = calculateRunSize(sizeIdx);
        long runHandle = allocateRun(runSize);
        if (runHandle < 0) {
            return -1L;
        }
        int runOffset = runOffset(runHandle);
        if (this.subpages[runOffset] != null) {
            throw new AssertionError();
        }
        int elemSize = this.arena.sizeClass.sizeIdx2size(sizeIdx);
        PoolSubpage<T> subpage = new PoolSubpage<>(head, this, this.pageShifts, runOffset, runSize(this.pageShifts, runHandle), elemSize);
        this.subpages[runOffset] = subpage;
        return subpage.allocate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void free(long handle, int normCapacity, ByteBuffer nioBuffer) {
        if (isSubpage(handle)) {
            int sIdx = runOffset(handle);
            PoolSubpage<T> subpage = this.subpages[sIdx];
            if (subpage == null) {
                throw new AssertionError();
            }
            PoolSubpage<T> head = subpage.chunk.arena.smallSubpagePools[subpage.headIndex];
            head.lock();
            try {
                if (!subpage.doNotDestroy) {
                    throw new AssertionError();
                }
                if (subpage.free(head, bitmapIdx(handle))) {
                    head.unlock();
                    return;
                } else {
                    if (subpage.doNotDestroy) {
                        throw new AssertionError();
                    }
                    this.subpages[sIdx] = null;
                    head.unlock();
                }
            } catch (Throwable th) {
                head.unlock();
                throw th;
            }
        }
        int runSize = runSize(this.pageShifts, handle);
        this.runsAvailLock.lock();
        try {
            long finalRun = collapseRuns(handle) & (-8589934593L) & (-4294967297L);
            insertAvailRun(runOffset(finalRun), runPages(finalRun), finalRun);
            this.freeBytes += runSize;
            if (nioBuffer == null || this.cachedNioBuffers == null || this.cachedNioBuffers.size() >= PooledByteBufAllocator.DEFAULT_MAX_CACHED_BYTEBUFFERS_PER_CHUNK) {
                return;
            }
            this.cachedNioBuffers.offer(nioBuffer);
        } finally {
            this.runsAvailLock.unlock();
        }
    }

    private long collapseRuns(long handle) {
        return collapseNext(collapsePast(handle));
    }

    private long collapsePast(long handle) {
        while (true) {
            int runOffset = runOffset(handle);
            int runPages = runPages(handle);
            long pastRun = getAvailRunByOffset(runOffset - 1);
            if (pastRun == -1) {
                return handle;
            }
            int pastOffset = runOffset(pastRun);
            int pastPages = runPages(pastRun);
            if (pastRun == handle || pastOffset + pastPages != runOffset) {
                break;
            }
            removeAvailRun(pastRun);
            handle = toRunHandle(pastOffset, pastPages + runPages, 0);
        }
        return handle;
    }

    private long collapseNext(long handle) {
        while (true) {
            int runOffset = runOffset(handle);
            int runPages = runPages(handle);
            long nextRun = getAvailRunByOffset(runOffset + runPages);
            if (nextRun == -1) {
                return handle;
            }
            int nextOffset = runOffset(nextRun);
            int nextPages = runPages(nextRun);
            if (nextRun == handle || runOffset + runPages != nextOffset) {
                break;
            }
            removeAvailRun(nextRun);
            handle = toRunHandle(runOffset, runPages + nextPages, 0);
        }
        return handle;
    }

    private static long toRunHandle(int runOffset, int runPages, int inUsed) {
        return (runOffset << 49) | (runPages << 34) | (inUsed << 33);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initBuf(PooledByteBuf<T> buf, ByteBuffer nioBuffer, long handle, int reqCapacity, PoolThreadCache threadCache) {
        if (isSubpage(handle)) {
            initBufWithSubpage(buf, nioBuffer, handle, reqCapacity, threadCache);
        } else {
            int maxLength = runSize(this.pageShifts, handle);
            buf.init(this, nioBuffer, handle, runOffset(handle) << this.pageShifts, reqCapacity, maxLength, this.arena.parent.threadCache());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initBufWithSubpage(PooledByteBuf<T> buf, ByteBuffer nioBuffer, long handle, int reqCapacity, PoolThreadCache threadCache) {
        int runOffset = runOffset(handle);
        int bitmapIdx = bitmapIdx(handle);
        PoolSubpage<T> s = this.subpages[runOffset];
        if (!s.isDoNotDestroy()) {
            throw new AssertionError();
        }
        if (reqCapacity > s.elemSize) {
            throw new AssertionError(reqCapacity + "<=" + s.elemSize);
        }
        int offset = (runOffset << this.pageShifts) + (s.elemSize * bitmapIdx);
        buf.init(this, nioBuffer, handle, offset, reqCapacity, s.elemSize, threadCache);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void incrementPinnedMemory(int delta) {
        if (delta <= 0) {
            throw new AssertionError();
        }
        this.pinnedBytes.add(delta);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void decrementPinnedMemory(int delta) {
        if (delta <= 0) {
            throw new AssertionError();
        }
        this.pinnedBytes.add(-delta);
    }

    @Override // io.netty.buffer.PoolChunkMetric
    public int chunkSize() {
        return this.chunkSize;
    }

    @Override // io.netty.buffer.PoolChunkMetric
    public int freeBytes() {
        if (this.unpooled) {
            return this.freeBytes;
        }
        this.runsAvailLock.lock();
        try {
            return this.freeBytes;
        } finally {
            this.runsAvailLock.unlock();
        }
    }

    public int pinnedBytes() {
        return (int) this.pinnedBytes.value();
    }

    public String toString() {
        int freeBytes;
        if (this.unpooled) {
            freeBytes = this.freeBytes;
        } else {
            this.runsAvailLock.lock();
            try {
                freeBytes = this.freeBytes;
            } finally {
                this.runsAvailLock.unlock();
            }
        }
        return "Chunk(" + Integer.toHexString(System.identityHashCode(this)) + ": " + usage(freeBytes) + "%, " + (this.chunkSize - freeBytes) + '/' + this.chunkSize + ')';
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroy() {
        this.arena.destroyChunk(this);
    }

    static int runOffset(long handle) {
        return (int) (handle >> 49);
    }

    static int runSize(int pageShifts, long handle) {
        return runPages(handle) << pageShifts;
    }

    static int runPages(long handle) {
        return (int) ((handle >> 34) & 32767);
    }

    static boolean isUsed(long handle) {
        return ((handle >> 33) & 1) == 1;
    }

    static boolean isRun(long handle) {
        return !isSubpage(handle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSubpage(long handle) {
        return ((handle >> 32) & 1) == 1;
    }

    static int bitmapIdx(long handle) {
        return (int) handle;
    }
}
