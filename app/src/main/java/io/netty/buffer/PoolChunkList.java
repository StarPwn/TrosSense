package io.netty.buffer;

import io.netty.util.internal.StringUtil;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class PoolChunkList<T> implements PoolChunkListMetric {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final Iterator<PoolChunkMetric> EMPTY_METRICS = Collections.emptyList().iterator();
    private final PoolArena<T> arena;
    private final int freeMaxThreshold;
    private final int freeMinThreshold;
    private PoolChunk<T> head;
    private final int maxCapacity;
    private final int maxUsage;
    private final int minUsage;
    private final PoolChunkList<T> nextList;
    private PoolChunkList<T> prevList;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PoolChunkList(PoolArena<T> arena, PoolChunkList<T> nextList, int minUsage, int maxUsage, int chunkSize) {
        if (minUsage > maxUsage) {
            throw new AssertionError();
        }
        this.arena = arena;
        this.nextList = nextList;
        this.minUsage = minUsage;
        this.maxUsage = maxUsage;
        this.maxCapacity = calculateMaxCapacity(minUsage, chunkSize);
        this.freeMinThreshold = maxUsage == 100 ? 0 : (int) ((chunkSize * ((100.0d - maxUsage) + 0.99999999d)) / 100.0d);
        this.freeMaxThreshold = minUsage != 100 ? (int) ((chunkSize * ((100.0d - minUsage) + 0.99999999d)) / 100.0d) : 0;
    }

    private static int calculateMaxCapacity(int minUsage, int chunkSize) {
        int minUsage2 = minUsage0(minUsage);
        if (minUsage2 == 100) {
            return 0;
        }
        return (int) ((chunkSize * (100 - minUsage2)) / 100);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prevList(PoolChunkList<T> prevList) {
        if (this.prevList != null) {
            throw new AssertionError();
        }
        this.prevList = prevList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean allocate(PooledByteBuf<T> buf, int reqCapacity, int sizeIdx, PoolThreadCache threadCache) {
        int normCapacity = this.arena.sizeClass.sizeIdx2size(sizeIdx);
        if (normCapacity > this.maxCapacity) {
            return false;
        }
        for (PoolChunk<T> cur = this.head; cur != null; cur = cur.next) {
            if (cur.allocate(buf, reqCapacity, sizeIdx, threadCache)) {
                if (cur.freeBytes <= this.freeMinThreshold) {
                    remove(cur);
                    this.nextList.add(cur);
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean free(PoolChunk<T> chunk, long handle, int normCapacity, ByteBuffer nioBuffer) {
        chunk.free(handle, normCapacity, nioBuffer);
        if (chunk.freeBytes > this.freeMaxThreshold) {
            remove(chunk);
            return move0(chunk);
        }
        return true;
    }

    private boolean move(PoolChunk<T> chunk) {
        if (chunk.usage() >= this.maxUsage) {
            throw new AssertionError();
        }
        if (chunk.freeBytes > this.freeMaxThreshold) {
            return move0(chunk);
        }
        add0(chunk);
        return true;
    }

    private boolean move0(PoolChunk<T> chunk) {
        if (this.prevList == null) {
            if (chunk.usage() != 0) {
                throw new AssertionError();
            }
            return false;
        }
        return this.prevList.move(chunk);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(PoolChunk<T> chunk) {
        if (chunk.freeBytes <= this.freeMinThreshold) {
            this.nextList.add(chunk);
        } else {
            add0(chunk);
        }
    }

    void add0(PoolChunk<T> chunk) {
        chunk.parent = this;
        if (this.head == null) {
            this.head = chunk;
            chunk.prev = null;
            chunk.next = null;
        } else {
            chunk.prev = null;
            chunk.next = this.head;
            this.head.prev = chunk;
            this.head = chunk;
        }
    }

    private void remove(PoolChunk<T> cur) {
        if (cur == this.head) {
            this.head = cur.next;
            if (this.head != null) {
                this.head.prev = null;
                return;
            }
            return;
        }
        PoolChunk<T> next = cur.next;
        cur.prev.next = next;
        if (next != null) {
            next.prev = cur.prev;
        }
    }

    @Override // io.netty.buffer.PoolChunkListMetric
    public int minUsage() {
        return minUsage0(this.minUsage);
    }

    @Override // io.netty.buffer.PoolChunkListMetric
    public int maxUsage() {
        return Math.min(this.maxUsage, 100);
    }

    private static int minUsage0(int value) {
        return Math.max(1, value);
    }

    @Override // java.lang.Iterable
    public Iterator<PoolChunkMetric> iterator() {
        this.arena.lock();
        try {
            if (this.head == null) {
                return EMPTY_METRICS;
            }
            List<PoolChunkMetric> metrics = new ArrayList<>();
            PoolChunk<T> cur = this.head;
            do {
                metrics.add(cur);
                cur = cur.next;
            } while (cur != null);
            return metrics.iterator();
        } finally {
            this.arena.unlock();
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        this.arena.lock();
        try {
            if (this.head == null) {
                return "none";
            }
            PoolChunk<T> cur = this.head;
            while (true) {
                buf.append(cur);
                cur = cur.next;
                if (cur != null) {
                    buf.append(StringUtil.NEWLINE);
                } else {
                    this.arena.unlock();
                    return buf.toString();
                }
            }
        } finally {
            this.arena.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroy(PoolArena<T> arena) {
        for (PoolChunk<T> chunk = this.head; chunk != null; chunk = chunk.next) {
            arena.destroyChunk(chunk);
        }
        this.head = null;
    }
}
