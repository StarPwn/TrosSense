package io.netty.buffer;

import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class PoolSubpage<T> implements PoolSubpageMetric {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final long[] bitmap;
    private final int bitmapLength;
    final PoolChunk<T> chunk;
    boolean doNotDestroy;
    final int elemSize;
    final int headIndex;
    final ReentrantLock lock;
    private final int maxNumElems;
    PoolSubpage<T> next;
    private int nextAvail;
    private int numAvail;
    private final int pageShifts;
    PoolSubpage<T> prev;
    private final int runOffset;
    private final int runSize;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PoolSubpage(int headIndex) {
        this.chunk = null;
        this.lock = new ReentrantLock();
        this.pageShifts = -1;
        this.runOffset = -1;
        this.elemSize = -1;
        this.runSize = -1;
        this.bitmap = null;
        this.bitmapLength = -1;
        this.maxNumElems = 0;
        this.headIndex = headIndex;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PoolSubpage(PoolSubpage<T> head, PoolChunk<T> chunk, int pageShifts, int runOffset, int runSize, int elemSize) {
        this.headIndex = head.headIndex;
        this.chunk = chunk;
        this.pageShifts = pageShifts;
        this.runOffset = runOffset;
        this.runSize = runSize;
        this.elemSize = elemSize;
        this.doNotDestroy = true;
        int i = runSize / elemSize;
        this.numAvail = i;
        this.maxNumElems = i;
        int bitmapLength = this.maxNumElems >>> 6;
        bitmapLength = (this.maxNumElems & 63) != 0 ? bitmapLength + 1 : bitmapLength;
        this.bitmapLength = bitmapLength;
        this.bitmap = new long[bitmapLength];
        this.nextAvail = 0;
        this.lock = null;
        addToPool(head);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long allocate() {
        if (this.numAvail == 0 || !this.doNotDestroy) {
            return -1L;
        }
        int bitmapIdx = getNextAvail();
        if (bitmapIdx < 0) {
            removeFromPool();
            throw new AssertionError("No next available bitmap index found (bitmapIdx = " + bitmapIdx + "), even though there are supposed to be (numAvail = " + this.numAvail + ") out of (maxNumElems = " + this.maxNumElems + ") available indexes.");
        }
        int q = bitmapIdx >>> 6;
        int r = bitmapIdx & 63;
        if (((this.bitmap[q] >>> r) & 1) != 0) {
            throw new AssertionError();
        }
        long[] jArr = this.bitmap;
        jArr[q] = (1 << r) | jArr[q];
        int i = this.numAvail - 1;
        this.numAvail = i;
        if (i == 0) {
            removeFromPool();
        }
        return toHandle(bitmapIdx);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean free(PoolSubpage<T> head, int bitmapIdx) {
        int q = bitmapIdx >>> 6;
        int r = bitmapIdx & 63;
        if (((this.bitmap[q] >>> r) & 1) == 0) {
            throw new AssertionError();
        }
        long[] jArr = this.bitmap;
        jArr[q] = (1 << r) ^ jArr[q];
        setNextAvail(bitmapIdx);
        int i = this.numAvail;
        this.numAvail = i + 1;
        if (i == 0) {
            addToPool(head);
            if (this.maxNumElems > 1) {
                return true;
            }
        }
        if (this.numAvail != this.maxNumElems || this.prev == this.next) {
            return true;
        }
        this.doNotDestroy = false;
        removeFromPool();
        return false;
    }

    private void addToPool(PoolSubpage<T> head) {
        if (this.prev != null || this.next != null) {
            throw new AssertionError();
        }
        this.prev = head;
        this.next = head.next;
        this.next.prev = this;
        head.next = this;
    }

    private void removeFromPool() {
        if (this.prev == null || this.next == null) {
            throw new AssertionError();
        }
        this.prev.next = this.next;
        this.next.prev = this.prev;
        this.next = null;
        this.prev = null;
    }

    private void setNextAvail(int bitmapIdx) {
        this.nextAvail = bitmapIdx;
    }

    private int getNextAvail() {
        int nextAvail = this.nextAvail;
        if (nextAvail >= 0) {
            this.nextAvail = -1;
            return nextAvail;
        }
        return findNextAvail();
    }

    private int findNextAvail() {
        for (int i = 0; i < this.bitmapLength; i++) {
            long bits = this.bitmap[i];
            if ((~bits) != 0) {
                return findNextAvail0(i, bits);
            }
        }
        return -1;
    }

    private int findNextAvail0(int i, long bits) {
        int baseVal = i << 6;
        for (int j = 0; j < 64; j++) {
            if ((1 & bits) == 0) {
                int val = baseVal | j;
                if (val < this.maxNumElems) {
                    return val;
                }
                return -1;
            }
            bits >>>= 1;
        }
        return -1;
    }

    private long toHandle(int bitmapIdx) {
        int pages = this.runSize >> this.pageShifts;
        return (this.runOffset << 49) | (pages << 34) | 8589934592L | 4294967296L | bitmapIdx;
    }

    public String toString() {
        int numAvail;
        if (this.chunk == null) {
            numAvail = 0;
        } else {
            PoolSubpage<T> head = this.chunk.arena.smallSubpagePools[this.headIndex];
            head.lock();
            try {
                boolean doNotDestroy = this.doNotDestroy;
                int numAvail2 = this.numAvail;
                if (doNotDestroy) {
                    numAvail = numAvail2;
                } else {
                    return "(" + this.runOffset + ": not in use)";
                }
            } finally {
                head.unlock();
            }
        }
        return "(" + this.runOffset + ": " + (this.maxNumElems - numAvail) + '/' + this.maxNumElems + ", offset: " + this.runOffset + ", length: " + this.runSize + ", elemSize: " + this.elemSize + ')';
    }

    @Override // io.netty.buffer.PoolSubpageMetric
    public int maxNumElements() {
        return this.maxNumElems;
    }

    @Override // io.netty.buffer.PoolSubpageMetric
    public int numAvailable() {
        if (this.chunk == null) {
            return 0;
        }
        PoolSubpage<T> head = this.chunk.arena.smallSubpagePools[this.headIndex];
        head.lock();
        try {
            return this.numAvail;
        } finally {
            head.unlock();
        }
    }

    @Override // io.netty.buffer.PoolSubpageMetric
    public int elementSize() {
        return this.elemSize;
    }

    @Override // io.netty.buffer.PoolSubpageMetric
    public int pageSize() {
        return 1 << this.pageShifts;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDoNotDestroy() {
        if (this.chunk == null) {
            return true;
        }
        PoolSubpage<T> head = this.chunk.arena.smallSubpagePools[this.headIndex];
        head.lock();
        try {
            return this.doNotDestroy;
        } finally {
            head.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroy() {
        if (this.chunk != null) {
            this.chunk.destroy();
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
}
