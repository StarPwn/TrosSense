package org.cloudburstmc.netty.util;

/* loaded from: classes5.dex */
public class BitQueue {
    private int head;
    private long[] queue;
    private int tail;

    public BitQueue() {
        this(0);
    }

    public BitQueue(int capacity) {
        int capacity2 = RakUtils.powerOfTwoCeiling(capacity);
        this.queue = new long[((capacity2 <= 0 ? 64 : capacity2) + 63) >> 6];
        this.head = 0;
        this.tail = 0;
    }

    public void add(boolean bit) {
        if (((this.head + 1) & ((this.queue.length << 6) - 1)) == this.tail) {
            resize(this.queue.length << 7);
        }
        int by = this.head >> 6;
        long bi = 1 << (this.head & 63);
        long[] jArr = this.queue;
        jArr[by] = jArr[by] ^ (((bit ? -1L : 0L) ^ this.queue[by]) & bi);
        this.head = (this.head + 1) & ((this.queue.length << 6) - 1);
    }

    private void resize(int capacity) {
        long[] newQueue = new long[(capacity + 63) >> 6];
        int size = size();
        if ((this.tail & 63) == 0) {
            if (this.head <= this.tail) {
                if (this.head < this.tail) {
                    int length = this.tail >> 6;
                    int adjustedPos = (((this.queue.length << 6) - this.tail) + 63) >> 6;
                    System.arraycopy(this.queue, length, newQueue, 0, adjustedPos);
                    int length2 = (this.head + 63) >> 6;
                    System.arraycopy(this.queue, 0, newQueue, adjustedPos, length2);
                }
            } else {
                int srcPos = this.tail >> 6;
                int length3 = ((this.head - this.tail) + 63) >> 6;
                System.arraycopy(this.queue, srcPos, newQueue, 0, length3);
            }
            this.tail = 0;
            this.head = size;
        } else {
            int tailBits = this.tail & 63;
            int tailIdx = this.tail >> 6;
            int by2 = (tailIdx + 1) & (this.queue.length - 1);
            int cursor = 0;
            while (cursor < size) {
                long mask = (1 << tailBits) - 1;
                long bit1 = ((~mask) & this.queue[tailIdx]) >>> tailBits;
                long bit2 = this.queue[by2] << (64 - tailBits);
                newQueue[cursor >> 6] = bit1 | bit2;
                cursor += 64;
                tailIdx = (tailIdx + 1) & (this.queue.length - 1);
                by2 = (by2 + 1) & (this.queue.length - 1);
            }
            this.tail = 0;
            this.head = size;
        }
        this.queue = newQueue;
    }

    public int size() {
        if (this.head > this.tail) {
            return this.head - this.tail;
        }
        if (this.head < this.tail) {
            return (this.queue.length << 6) - (this.tail - this.head);
        }
        return 0;
    }

    public void set(int n, boolean bit) {
        if (n >= size() || n < 0) {
            return;
        }
        int idx = (this.tail + n) & ((this.queue.length << 6) - 1);
        int arrIdx = idx >> 6;
        long mask = 1 << (idx & 63);
        long[] jArr = this.queue;
        jArr[arrIdx] = jArr[arrIdx] ^ (((bit ? 255 : 0) ^ this.queue[arrIdx]) & mask);
    }

    public boolean get(int n) {
        if (n >= size() || n < 0) {
            return false;
        }
        int idx = (this.tail + n) & ((this.queue.length << 6) - 1);
        int arrIdx = idx >> 6;
        long mask = 1 << (idx & 63);
        return (this.queue[arrIdx] & mask) != 0;
    }

    public boolean isEmpty() {
        return this.head == this.tail;
    }

    public boolean peek() {
        if (this.head == this.tail) {
            return false;
        }
        int arrIdx = this.tail >> 6;
        long mask = 1 << (this.tail & 63);
        return (this.queue[arrIdx] & mask) != 0;
    }

    public boolean poll() {
        if (this.head == this.tail) {
            return false;
        }
        int arrIdx = this.tail >> 6;
        long mask = 1 << (this.tail & 63);
        this.tail = (this.tail + 1) & ((this.queue.length << 6) - 1);
        return (this.queue[arrIdx] & mask) != 0;
    }
}
